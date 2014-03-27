package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.ListaAgendamento;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.db.*;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class HomologacaoBean extends PesquisarProfissaoBean implements Serializable {

    private String msgHomologacao = "";
    private String msgConfirma = "";
    private String strEndereco = "";
    private String tipoAviso = "true";
    private String strData = DataHoje.data();
    private String statusEmpresa = "REGULAR";
    private boolean renderHomologacao = true;
    private boolean renderHomologar = false;
    private boolean renderConcluir = false;
    private boolean renderCancelarHorario = false;
    private Date data = DataHoje.dataHoje();
    private int idStatus = 0;
    private int idMotivoDemissao = 0;
    private int idIndex = -1;
    private List listaGrid = new ArrayList();
    private List<ListaAgendamento> listaHomologacoes = new ArrayList<ListaAgendamento>();
    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    private List<SelectItem> listaDemissao = new ArrayList<SelectItem>();
    private Agendamento agendamento = new Agendamento();
    private Juridica juridica = new Juridica();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private Fisica fisica = new Fisica();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private MacFilial macFilial = null;
    private Registro registro = new Registro();
    private Cancelamento cancelamento = new Cancelamento();

    public String excluirSenha() {
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        Senha senha = homologacaoDB.pesquisaSenhaAgendamento(agendamento.getId());
        if (senha.getId() == -1) {
            msgConfirma = "Não existe senha para ser excluida!";
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.find("Senha", senha.getId()))) {
            msgConfirma = "Erro ao excluir senha!";
            return null;
        }
        agendamento.setStatus((Status) sv.find("Status", 2));
        agendamento.setHomologador(null);
        if (!sv.alterarObjeto(agendamento)) {
            msgHomologacao = "Erro ao atualizar Agendamento";
            return null;
        }
        msgConfirma = "Senha excluída com sucesso";
        sv.comitarTransacao();
        strEndereco = "";
        renderHomologacao = true;
        renderConcluir = false;
        tipoAviso = "true";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        return null;
    }

    public String atendimento() {
        if (macFilial.getId() == -1) {
            msgHomologacao = "Mac não encontrado!";
            return "homologacao";
        }
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        Senha senha = homologacaoDB.pesquisaAtendimentoIniciado(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId(), macFilial.getMesa(), macFilial.getFilial().getId());
        if (senha.getId() == -1) {
            senha = homologacaoDB.pesquisaSenhaAtendimento(macFilial.getFilial().getId());
            if (senha.getId() == -1) {
                msgHomologacao = "Senha não encontrada!";
                return "homologacao";
            }
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        agendamento = senha.getAgendamento();
        fisica = senha.getAgendamento().getPessoaEmpresa().getFisica();
        juridica = senha.getAgendamento().getPessoaEmpresa().getJuridica();
        pessoaEmpresa = senha.getAgendamento().getPessoaEmpresa();
        profissao = senha.getAgendamento().getPessoaEmpresa().getFuncao();
        renderHomologar = true;
        renderConcluir = true;
        renderHomologar = true;
        renderCancelarHorario = true;
        renderHomologacao = false;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
        for (int i = 0; i < getListaDemissao().size(); i++) {
            if (Integer.parseInt(listaDemissao.get(i).getDescription()) == agendamento.getDemissao().getId()) {
                idMotivoDemissao = (Integer) listaDemissao.get(i).getValue();
                break;
            }
        }
        tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
        agendamento.setStatus((Status) sv.find("Status", 5));
        agendamento.setHomologador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
        sv.abrirTransacao();
        if (!sv.alterarObjeto(agendamento)) {
            msgHomologacao = "Erro ao atualizar Agendamento";
            return "homologacao";
        }
        senha.setMesa(macFilial.getMesa());
        senha.setHoraChamada(DataHoje.horaMinuto());
        if (!sv.alterarObjeto(senha)) {
            msgHomologacao = "Erro ao atualizar Senha";
            return "homologacao";
        }
        sv.comitarTransacao();
        return "homologacao";
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Status> list = (List<Status>) salvarAcumuladoDB.pesquisaObjeto(new int[]{2, 3, 4, 5, 7}, "Status");
            for (int i = 0; i < list.size(); i++) {
                listaStatus.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaStatus;
    }

    public List<SelectItem> getListaDemissao() {
        if (listaDemissao.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Demissao> list = (List<Demissao>) salvarAcumuladoDB.listaObjeto("Demissao", true);
            for (int i = 0; i < list.size(); i++) {
                listaDemissao.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaDemissao;
    }

    public synchronized List getListaHorarios() {
        if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) > DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            return new ArrayList();
        }
        if (macFilial == null) {
            return new ArrayList();
        }
        listaGrid.clear();
        listaGrid = new ArrayList();
        List<Agendamento> ag;
        HomologacaoDB db = new HomologacaoDBToplink();
        String agendador;
        String homologador;
        DataObject dtObj;
        Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        Registro reg = (Registro) new SalvarAcumuladoDBToplink().find("Registro", 1);
        int idUsuario;
        int idCaso = Integer.parseInt(((SelectItem) listaStatus.get(idStatus)).getDescription());
        if (idCaso <= 0) {
            return new ArrayList();
        }
        if (idCaso == 2 || idCaso == 7) {
            idUsuario = 0;
        } else {
            idUsuario = us.getId();
        }
        ag = db.pesquisaAgendamento(idCaso, macFilial.getFilial().getId(), data, null, idUsuario, 0, 0);
        for (int i = 0; i < ag.size(); i++) {
            Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
            if (ag.get(i).getAgendador() != null) {
                agendador = ag.get(i).getAgendador().getPessoa().getNome();
            } else {
                agendador = "** Web User **";
            }
            if (ag.get(i).getHomologador() != null) {
                homologador = ag.get(i).getHomologador().getPessoa().getNome();
            } else {
                homologador = "";
            }
            boolean isHabilitaAlteracao = false;
            if (reg.isSenhaHomologacao()) {
                if (DataHoje.converteDataParaInteger(DataHoje.converteData(ag.get(i).getDtData())) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                    if (ag.get(i).getStatus().getId() == 2) {
                        isHabilitaAlteracao = true;
                    } else {
                        isHabilitaAlteracao = false;
                    }
                }
                if (senha.getId() == -1) {
                    if (ag.get(i).getStatus().getId() != 7
                            && ag.get(i).getStatus().getId() != 3
                            && ag.get(i).getStatus().getId() != 4
                            && ag.get(i).getStatus().getId() != 5) {
                        continue;
                    }
                }
            } else {
                if (DataHoje.converteDataParaInteger(DataHoje.converteData(ag.get(i).getDtData())) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                    isHabilitaAlteracao = false;
                }
            }
            if (DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje())) > DataHoje.converteDataParaInteger(DataHoje.converteData(ag.get(i).getDtData()))) {
                isHabilitaAlteracao = false;
            }
            dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
                    ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                    ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                    homologador, //ARG 3 HOMOLOGADOR
                    ag.get(i).getContato(), // ARG 4 CONTATO
                    ag.get(i).getTelefone(), // ARG 5 TELEFONE
                    agendador, // ARG 6 USUARIO
                    ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                    null, // ARG 8
                    ag.get(i), // ARG 9 AGENDAMENTO
                    senha.getId() == -1 ? null : senha.getSenha(), // ARG 10 SENHA PARA ATENDIMENTO
                    null,
                    null,
                    null,
                    null,
                    isHabilitaAlteracao // ARG 15 HABILITA ALTERAÇÃO DO STATUS
            );

            listaGrid.add(dtObj);
        }
        return listaGrid;
    }

    public String agendar(Agendamento a) {
        HomologacaoDB db = new HomologacaoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendamento = a;
        cancelamento = new Cancelamento();
        int nrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        if (nrStatus == 3 || nrStatus == 4 || nrStatus == 5) {
            if (!desabilitaEdicao(agendamento.getDtData(), 30)) {
                return "homologacao";
            }
        } else {
            if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                if (registro.isSenhaHomologacao()) {
                    Senha senha = db.pesquisaSenhaAgendamento(agendamento.getId());
                    if (senha.getId() == -1) {
                        return "homologacao";
                    }
                }
            }
        }
        renderHomologacao = false;
        renderConcluir = true;
        if (nrStatus > 0) {
            fisica = agendamento.getPessoaEmpresa().getFisica();
            juridica = agendamento.getPessoaEmpresa().getJuridica();
            pessoaEmpresa = agendamento.getPessoaEmpresa();
            profissao = agendamento.getPessoaEmpresa().getFuncao();
            tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
            for (int i = 0; i < getListaDemissao().size(); i++) {
                if (Integer.parseInt(listaDemissao.get(i).getDescription()) == agendamento.getDemissao().getId()) {
                    idMotivoDemissao = (Integer) listaDemissao.get(i).getValue();
                    break;
                }
            }
            switch (nrStatus) {
                case 2: {
                    renderHomologar = true;
                    renderCancelarHorario = true;
                    agendamento.setStatus((Status) salvarAcumuladoDB.pesquisaObjeto(5, "Status"));
                    agendamento.setHomologador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.alterarObjeto(agendamento)) {
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                    }
                    break;
                }
                case 3: {
                    renderHomologar = true;
                    renderCancelarHorario = false;
                    agendamento.setStatus((Status) salvarAcumuladoDB.pesquisaObjeto(5, "Status"));
                    agendamento.setHomologador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.alterarObjeto(agendamento)) {
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                    }
                    break;
                }
                case 4: {
                    renderHomologar = false;
                    renderCancelarHorario = true;
                    renderHomologacao = false;
                    renderConcluir = true;
                    break;
                }
                case 5: {
                    if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == agendamento.getHomologador().getId()) {
                        renderHomologar = true;
                        renderCancelarHorario = true;
                        renderHomologacao = false;
                        renderConcluir = true;
                        break;
                    } else {
                        renderHomologar = false;
                        renderCancelarHorario = false;
                        renderHomologacao = true;
                        agendamento = new Agendamento();
                        renderConcluir = false;
                        break;
                    }
                }
                case 7: {
                    renderHomologar = true;
                    renderCancelarHorario = true;
                    break;
                }
            }
        }
        return "homologacao";
    }

    public void salvar() {
        List listDocumento;
        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
            msgConfirma = "Digite o nome do Funcionário!";
            return;
        }
        // SALVAR FISICA -----------------------------------------------
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        fisica.getPessoa().setTipoDocumento((TipoDocumento) sv.find("TipoDocumento", 1));
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
            msgConfirma = "Documento Inválido!";
            return;
        }
        FisicaDB fisicaDB = new FisicaDBToplink();
        sv.abrirTransacao();
        if (fisica.getId() == -1) {
            if (!fisicaDB.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(), fisica.getDtNascimento(), fisica.getRg()).isEmpty()) {
                msgConfirma = "Esta pessoa já esta cadastrada!";
                return;
            }
            listDocumento = fisicaDB.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!listDocumento.isEmpty()) {
                msgConfirma = "Documento já existente!";
                return;
            }
            if (sv.inserirObjeto(fisica.getPessoa())) {
                sv.inserirObjeto(fisica);
            } else {
                msgConfirma = "Erro ao Inserir pessoa!";
                sv.desfazerTransacao();
                return;
            }
        } else {
            listDocumento = fisicaDB.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            for (Object listDocumento1 : listDocumento) {
                if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != fisica.getId()) {
                    msgConfirma = "Documento já existente!";
                    return;
                }
            }
            List<Fisica> fisi = fisicaDB.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(), fisica.getDtNascimento(), fisica.getRg());
            if (!fisi.isEmpty()) {
                for (Fisica fisi1 : fisi) {
                    if (fisi1.getId() != fisica.getId()) {
                        msgConfirma = "Esta pessoa já esta cadastrada!";
                        return;
                    }
                }
            }
            if (sv.alterarObjeto(fisica.getPessoa())) {
                if (sv.alterarObjeto(fisica)) {
                } else {
                    msgConfirma = "Erro ao alterar Física!";
                    sv.desfazerTransacao();
                    return;
                }
            } else {
                msgConfirma = "Erro ao alterar Pessoa!";
                sv.desfazerTransacao();
                return;
            }
        }
        // -------------------------------------------------------------
        pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
        pessoaEmpresa.setFuncao(this.profissao);
        if (!sv.alterarObjeto(pessoaEmpresa)) {
            msgConfirma = "Erro ao alterar Pessoa Empresa!";
            sv.desfazerTransacao();
            return;
        }
        agendamento.setDemissao((Demissao) sv.find("Demissao", Integer.parseInt(((SelectItem) getListaDemissao().get(idMotivoDemissao)).getDescription())));
        agendamento.setPessoaEmpresa(pessoaEmpresa);
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        int nrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        if (registro.isSenhaHomologacao()) {
            if (nrStatus != 4 && nrStatus != 7) {
                Senha senha = homologacaoDB.pesquisaSenhaAgendamento(agendamento.getId());
                senha.setMesa(0);
                senha.setHoraChamada("");
                if (!sv.alterarObjeto(senha)) {
                    msgConfirma = "Erro ao atualizar Senha!";
                    return;
                }
            }
        }
        int intDataHoje = DataHoje.converteDataParaInteger(DataHoje.data());
        int intDataRetroativa = DataHoje.converteDataParaInteger(registro.getAgendamentoRetroativoString());
        if (nrStatus == 2 || nrStatus == 4) {
            agendamento.setStatus((Status) sv.find("Status", nrStatus));
        } else if (nrStatus == 7) {
            agendamento.setStatus((Status) sv.find("Status", nrStatus));
            agendamento.setHomologador(null);
        } else if (nrStatus == 5) {
            if (DataHoje.converteDataParaInteger(agendamento.getData()) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                agendamento.setStatus((Status) sv.find("Status", 2));
                agendamento.setHomologador(null);
            } else {
                agendamento.setStatus((Status) sv.find("Status", 2));
                agendamento.setHomologador(null);
            }
        }
        if (sv.alterarObjeto(agendamento)) {
            sv.comitarTransacao();
            msgConfirma = "Registro atualizado com Sucesso!";
            limpar();
        }
    }

    public void homologar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        agendamento.setHomologador((Usuario) GenericaSessao.getObject("sessaoUsuario"));
        agendamento.setStatus((Status) sv.find(new Status() ,4));
        new Cancelamento().getAgendamento().getId();
        Cancelamento c = (Cancelamento) sv.pesquisaObjeto(agendamento.getId(), "Cancelamento", "agendamento.id");
        sv.abrirTransacao();
        if (c != null) {
            if (!sv.deletarObjeto(c)) {
                msgConfirma = "Erro ao homologar!";
                sv.desfazerTransacao();
                return;
            }
        }
        if (sv.alterarObjeto(agendamento)) {
            msgConfirma = "Agendamento homologado com sucesso!";
            sv.comitarTransacao();
        } else {
            msgConfirma = "Erro ao homologar!";
            sv.desfazerTransacao();
        }
        strEndereco = "";
        renderHomologacao = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgHomologacao = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
    }

    public String cancelarHomologacao() {
        if (cancelamento.getMotivo().isEmpty() || cancelamento.getMotivo().length() <= 5) {
            msgConfirma = "Motivo de cancelamento inválido";
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Status s = agendamento.getStatus();
        sv.abrirTransacao();
        if (!sv.alterarObjeto(agendamento)) {
            msgConfirma = "Erro ao cancelar homologagação!";
            sv.desfazerTransacao();
            return null;
        }
        pessoaEmpresa.setDtDemissao(null);
        if (!sv.alterarObjeto(pessoaEmpresa)) {
            msgConfirma = "Erro ao atualizar Pessoa Empresa";
            return null;
        }
        cancelamento.setAgendamento(agendamento);
        cancelamento.setDtData(DataHoje.dataHoje());
        cancelamento.setUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")));
        if (!sv.inserirObjeto(cancelamento)) {
            msgConfirma = "Erro ao salvar cancelamento";
            return null;
        }
        agendamento.setStatus((Status) sv.find("Status", 3));
        if (!sv.alterarObjeto(agendamento)) {
            sv.desfazerTransacao();
            agendamento.setStatus(s);
            msgConfirma = "Erro ao cancelar homologação!";
            return null;
        }
        cancelamento = new Cancelamento();
        msgConfirma = "Homologação cancelada com sucesso";
        strEndereco = "";
        renderHomologacao = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgHomologacao = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        sv.comitarTransacao();
        return null;
    }

    public void limpar() {
        strEndereco = "";
        renderHomologacao = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgHomologacao = "";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
    }

    public String pesquisarFuncionarioCPF() {
        HomologacaoDB db = new HomologacaoDBToplink();
        FisicaDB dbFis = new FisicaDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        fisica.getPessoa().setTipoDocumento((TipoDocumento) salvarAcumuladoDB.find("TipoDocumento", 1));
        PessoaEmpresa pe = db.pesquisaPessoaEmpresaOutra(fisica.getPessoa().getDocumento());
        if (pe.getId() != -1) {
            pessoaEmpresa = pe;
            fisica = pessoaEmpresa.getFisica();
            profissao = pessoaEmpresa.getFuncao();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            if (pessoaEmpresa.getJuridica().getContabilidade() != null) {
                agendamento.setTelefone(pessoaEmpresa.getJuridica().getContabilidade().getPessoa().getTelefone1());
            }
            return "homologacao";
        } else {
            List<Fisica> listFisica = dbFis.pesquisaFisicaPorDocSemLike(fisica.getPessoa().getDocumento());
            for (int i = 0; i < listFisica.size(); i++) {
                if (!listFisica.isEmpty() && listFisica.get(i).getId() != fisica.getId()) {
                    fisica = listFisica.get(i);
                    pessoaEmpresa = new PessoaEmpresa();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
                    return "homologacao";
                }
            }
            if (fisica.getId() != -1) {
                fisica = new Fisica();
                pessoaEmpresa = new PessoaEmpresa();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            }
        }
        return "homologacao";
    }

    public boolean desabilitaEdicao(Date date, int periodoDias) {
        long dataL = DataHoje.calculoDosDias(date, DataHoje.dataHoje());
        if (dataL <= periodoDias) {
            return true;
        }
        return false;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getMsgHomologacao() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") == null) {
            msgHomologacao = "Não existe filial definida!";
        } else {
            macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
        }
        return msgHomologacao;
    }

    public void setMsgHomologacao(String msgHomologacao) {
        this.msgHomologacao = msgHomologacao;
    }

    public boolean isRenderHomologacao() {
        return renderHomologacao;
    }

    public void setRenderHomologacao(boolean renderHomologacao) {
        this.renderHomologacao = renderHomologacao;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public boolean isRenderConcluir() {
        return renderConcluir;
    }

    public void setRenderConcluir(boolean renderConcluir) {
        this.renderConcluir = renderConcluir;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public PessoaEndereco getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(PessoaEndereco enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String getStrEndereco() {
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        if (juridica.getId() != -1) {
            enderecoEmpresa = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 2);
            if (enderecoEmpresa.getId() != -1) {
                String strCompl;
                if (enderecoEmpresa.getComplemento().equals("")) {
                    strCompl = " ";
                } else {
                    strCompl = " ( " + enderecoEmpresa.getComplemento() + " ) ";
                }

                strEndereco = enderecoEmpresa.getEndereco().getLogradouro().getDescricao() + " "
                        + enderecoEmpresa.getEndereco().getDescricaoEndereco().getDescricao() + ", " + enderecoEmpresa.getNumero() + " " + enderecoEmpresa.getEndereco().getBairro().getDescricao() + ","
                        + strCompl + enderecoEmpresa.getEndereco().getCidade().getCidade() + " - " + enderecoEmpresa.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(enderecoEmpresa.getEndereco().getCep());
            } else {
                strEndereco = "";
            }
        } else {
            enderecoEmpresa = new PessoaEndereco();
            strEndereco = "";
        }
        return strEndereco;
    }

    public void setStrEndereco(String strEndereco) {
        this.strEndereco = strEndereco;
    }

    public int getIdMotivoDemissao() {
        return idMotivoDemissao;
    }

    public void setIdMotivoDemissao(int idMotivoDemissao) {
        this.idMotivoDemissao = idMotivoDemissao;
    }

    public String getTipoAviso() {
        return tipoAviso;
    }

    public void setTipoAviso(String tipoAviso) {
        this.tipoAviso = tipoAviso;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isRenderHomologar() {
        return renderHomologar;
    }

    public void setRenderHomologar(boolean renderHomologar) {
        this.renderHomologar = renderHomologar;
    }

    public boolean isRenderCancelarHorario() {
        return renderCancelarHorario;
    }

    public void setRenderCancelarHorario(boolean renderCancelarHorario) {
        this.renderCancelarHorario = renderCancelarHorario;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getStatusEmpresa() {
        HomologacaoDB db = new HomologacaoDBToplink();
        List lista = new ArrayList();
        if (juridica.getId() != -1) {
            lista = db.pesquisaPessoaDebito(juridica.getPessoa().getId(), DataHoje.data());
        }
        if (!lista.isEmpty()) {
            statusEmpresa = "EM DÉBITO";
        } else {
            statusEmpresa = "REGULAR";
        }
        return statusEmpresa;
    }

    public void setStatusEmpresa(String statusEmpresa) {
        this.statusEmpresa = statusEmpresa;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public Registro getRegistro() {
        if (registro == null) {
            registro = new Registro();
        }
        if (registro.getId() == -1) {
            registro = (Registro) new SalvarAcumuladoDBToplink().find("Registro", 1);
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public int senhaHomologacao(int id) {
        HomologacaoDB db = new HomologacaoDBToplink();
        Senha senha = db.pesquisaSenhaAgendamento(id);
        return senha.getSenha();
    }

    public Cancelamento getCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(Cancelamento cancelamento) {
        this.cancelamento = cancelamento;
    }

    public List<ListaAgendamento> getListaHomologacoes() {
        listaHomologacoes.clear();
        try {
            Polling polling = new Polling();
            polling.existeUsuarioSessao();
        } catch (IOException e) {
            return new ArrayList();
        }
        if (macFilial == null) {
            return new ArrayList();
        }
        if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) > DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            return new ArrayList();
        }
        listaGrid.clear();
        listaGrid = new ArrayList();
        HomologacaoDB db = new HomologacaoDBToplink();
        Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        int idUsuario;
        int idCaso = Integer.parseInt(((SelectItem) listaStatus.get(idStatus)).getDescription());
        if (idCaso <= 0) {
            return new ArrayList();
        }
        if (idCaso == 2 || idCaso == 7) {
            idUsuario = 0;
        } else {
            idUsuario = us.getId();
        }
        List<Agendamento> agendamentos = db.pesquisaAgendamento(idCaso, macFilial.getFilial().getId(), data, null, idUsuario, 0, 0);
        Registro reg = (Registro) new SalvarAcumuladoDBToplink().find("Registro", 1);
        for (int i = 0; i < agendamentos.size(); i++) {
            ListaAgendamento listaAgendamento = new ListaAgendamento();
            listaAgendamento.setAgendamento(agendamentos.get(i));
            Usuario u = new Usuario();
            if (reg.isSenhaHomologacao()) {
                Senha senha = db.pesquisaSenhaAgendamento(agendamentos.get(i).getId());
                if (DataHoje.converteDataParaInteger(DataHoje.converteData(agendamentos.get(i).getDtData())) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                    if (agendamentos.get(i).getStatus().getId() == 2) {
                        listaAgendamento.setHabilitaAlteracao(true);
                    } else {
                        listaAgendamento.setHabilitaAlteracao(false);
                    }
                }
                if (senha.getId() == -1) {
                    if (agendamentos.get(i).getStatus().getId() != 7 && agendamentos.get(i).getStatus().getId() != 3 && agendamentos.get(i).getStatus().getId() != 4 && agendamentos.get(i).getStatus().getId() != 5) {
                        continue;
                    }
                } else {
                    listaAgendamento.setSenha(senha);
                }
            } else {
                if (DataHoje.converteDataParaInteger(DataHoje.converteData(agendamentos.get(i).getDtData())) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                    listaAgendamento.setHabilitaAlteracao(false);
                }
            }
            if (DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje())) > DataHoje.converteDataParaInteger(DataHoje.converteData(agendamentos.get(i).getDtData()))) {
                listaAgendamento.setHabilitaAlteracao(false);
            }
            if (agendamentos.get(i).getAgendador() == null) {
                listaAgendamento.setUsuarioAgendador("** Web User **");
            } else {
                listaAgendamento.setUsuarioAgendador(agendamentos.get(i).getAgendador().getPessoa().getNome());
            }
            listaHomologacoes.add(listaAgendamento);
        }
        return listaHomologacoes;
    }

    public void setListaHomologacoes(List<ListaAgendamento> listaHomologacoes) {
        this.listaHomologacoes = listaHomologacoes;
    }

}
