package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.db.*;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import br.com.rtools.utilitarios.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class HomologacaoJSFBean extends PesquisarProfissaoJSFBean {

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
    private Agendamento agendamento = new Agendamento();
    private Juridica juridica = new Juridica();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private Fisica fisica = new Fisica();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private MacFilial macFilial = null;
    private Registro registro = new Registro();
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private int id_protocolo = -1;

    public String excluirSenha() {
        AgendamentoDB db = new AgendamentoDBToplink();
        Senha senha = db.pesquisaSenhaAgendamento(agendamento.getId());
        if (senha.getId() == -1) {
            msgConfirma = "Não existe senha para ser excluida!";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        if (!sv.deletarObjeto(sv.pesquisaCodigo(senha.getId(), "Senha"))) {
            msgConfirma = "Erro ao excluir senha!";
            return null;
        }

        agendamento.setStatus((Status) sv.pesquisaCodigo(2, "Status"));
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
        AgendamentoDB db = new AgendamentoDBToplink();

        if (macFilial.getId() == -1) {
            msgHomologacao = "Mac não encontrado!";
            return "homologacao";
        }

        Senha senha = db.pesquisaAtendimentoIniciado(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId(), macFilial.getMesa(), macFilial.getFilial().getId());
        if (senha.getId() == -1) {
            senha = db.pesquisaSenhaAtendimento(macFilial.getFilial().getId());
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
        for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
            if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                break;
            }
        }

        tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
        agendamento.setStatus((Status) sv.pesquisaCodigo(5, "Status"));
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
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        StatusDB db = new StatusDBToplink();
        List select = new ArrayList();
        select.add(db.pesquisaCodigo(2));
        select.add(db.pesquisaCodigo(3));
        select.add(db.pesquisaCodigo(4));
        select.add(db.pesquisaCodigo(5));
        select.add(db.pesquisaCodigo(7));
        if (!select.isEmpty()) {
            while (i < select.size()) {
                result.add(new SelectItem(new Integer(i),
                        (String) ((Status) select.get(i)).getDescricao(),
                        Integer.toString(((Status) select.get(i)).getId())));
                i++;
            }
        }
        return result;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        DemissaoDB db = new DemissaoDBToplink();
        List select = db.pesquisaTodos();
        if (!select.isEmpty()) {
            while (i < select.size()) {
                result.add(new SelectItem(new Integer(i),
                        (String) ((Demissao) select.get(i)).getDescricao(),
                        Integer.toString(((Demissao) select.get(i)).getId())));
                i++;
            }
        }
        return result;
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
        AgendamentoDB db = new AgendamentoDBToplink();
        String agendador;
        String homologador;
        DataObject dtObj;
        Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        Registro reg = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");

        int idUsuario;
        int idCaso = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());

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
                    if (    ag.get(i).getStatus().getId() != 7 && 
                            ag.get(i).getStatus().getId() != 3 && 
                            ag.get(i).getStatus().getId() != 4 && 
                            ag.get(i).getStatus().getId() != 5) {
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


        switch (idCaso) {
            // STATUS AGENDADO -----------------------------------------------------------------------------------------------
            case 2: {
//                ag = db.pesquisaAgendamento(2, macFilial.getFilial().getId(), data, null, 0, 0 , 0);
//                for (int i = 0; i < ag.size(); i++) {
//                    if (ag.get(i).getAgendador() != null) {
//                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
//                    } else {
//                        agendador = "** Web User **";
//                    }
//                    if (ag.get(i).getHomologador() != null) {
//                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
//                    } else {
//                        homologador = "";
//                    }
//                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
//                            homologador, //ARG 3 HOMOLOGADOR
//                            ag.get(i).getContato(), // ARG 4 CONTATO
//                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
//                            agendador, // ARG 6 USUARIO
//                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
//                            null, // ARG 8
//                            ag.get(i), // ARG 9 AGENDAMENTO
//                            null,
//                            null);
//                    Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
//                    if (reg.isSenhaHomologacao()) {
//                        if (senha.getId() != -1) {
//                            listaGrid.add(dtObj);
//                        }
//                    } else {
//                        listaGrid.add(dtObj);
//                    }
//                }
//                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------

            // STATUS CANCELADO -----------------------------------------------------------------------------------------------
            case 3: {
//                ag = db.pesquisaAgendamento(3, macFilial.getFilial().getId(), data, null, us.getId(), 0, 0);
//                for (int i = 0; i < ag.size(); i++) {
//                    if (ag.get(i).getAgendador() != null) {
//                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
//                    } else {
//                        agendador = "** Web User **";
//                    }
//                    if (ag.get(i).getHomologador() != null) {
//                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
//                    } else {
//                        homologador = "";
//                    }
//
//                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
//                            homologador, //ARG 3 HOMOLOGADOR
//                            ag.get(i).getContato(), // ARG 4 CONTATO
//                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
//                            agendador, // ARG 6 USUARIO
//                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
//                            null, // ARG 8
//                            ag.get(i), // ARG 9 AGENDAMENTO
//                            null, // ARG 10 DESABILITA CAMPOS
//                            null);
//                    Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
//                    if (reg.isSenhaHomologacao()) {
//                        if (senha.getId() != -1) {
//                            listaGrid.add(dtObj);
//                        }
//                    } else {
//                        listaGrid.add(dtObj);
//                    }
//                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------

            // STATUS HOMOLOGADO -----------------------------------------------------------------------------------------------
            case 4: {
//                ag = db.pesquisaAgendamento(4, macFilial.getFilial().getId(), data, null, us.getId(), 0, 0);
//                for (int i = 0; i < ag.size(); i++) {
//                    if (ag.get(i).getAgendador() != null) {
//                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
//                    } else {
//                        agendador = "** Web User **";
//                    }
//                    if (ag.get(i).getHomologador() != null) {
//                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
//                    } else {
//                        homologador = "";
//                    }
//
//                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
//                            homologador, //ARG 3 HOMOLOGADOR
//                            ag.get(i).getContato(), // ARG 4 CONTATO
//                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
//                            agendador, // ARG 6 USUARIO
//                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
//                            null, // ARG 8
//                            ag.get(i), // ARG 9 AGENDAMENTO
//                            null, // ARG 10 DESABILITA CAMPOS
//                            null);
//                    Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
//                    if (reg.isSenhaHomologacao()) {
//                        if (senha.getId() != -1) {
//                            listaGrid.add(dtObj);
//                        }
//                    } else {
//                        listaGrid.add(dtObj);
//                    }
//
//                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------

            // STATUS ATENDIMENTO -----------------------------------------------------------------------------------------------
            case 5: {
//                ag = db.pesquisaAgendamento(5, macFilial.getFilial().getId(), data, null, us.getId(), 0, 0);
//                for (int i = 0; i < ag.size(); i++) {
//                    if (ag.get(i).getAgendador() != null) {
//                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
//                    } else {
//                        agendador = "** Web User **";
//                    }
//                    if (ag.get(i).getHomologador() != null) {
//                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
//                    } else {
//                        homologador = "";
//                    }
//
//                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
//                            homologador, //ARG 3 HOMOLOGADOR
//                            ag.get(i).getContato(), // ARG 4 CONTATO
//                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
//                            agendador, // ARG 6 USUARIO
//                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
//                            null, // ARG 8
//                            ag.get(i), // ARG 9 AGENDAMENTO
//                            null, // ARG 10 DESABILITA CAMPOS
//                            null);
//                    Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
//                    if (reg.isSenhaHomologacao()) {
//                        if (senha.getId() != -1) {
//                            listaGrid.add(dtObj);
//                        }
//                    } else {
//                        listaGrid.add(dtObj);
//                    }
//                }
                break;
            }

            // STATUS NÃO COMPARECEU -----------------------------------------------------------------------------------------------
            case 7: {
//                ag = db.pesquisaAgendamento(7, macFilial.getFilial().getId(), data, null, 0, 0, 0);
//                for (int i = 0; i < ag.size(); i++) {
//                    if (ag.get(i).getAgendador() != null) {
//                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
//                    } else {
//                        agendador = "** Web User **";
//                    }
//                    if (ag.get(i).getHomologador() != null) {
//                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
//                    } else {
//                        homologador = "";
//                    }
//
//                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
//                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
//                            homologador, //ARG 3 HOMOLOGADOR
//                            ag.get(i).getContato(), // ARG 4 CONTATO
//                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
//                            agendador, // ARG 6 USUARIO
//                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
//                            null, // ARG 8
//                            ag.get(i), // ARG 9 AGENDAMENTO
//                            null, // ARG 10 DESABILITA CAMPOS
//                            null);
//                    listaGrid.add(dtObj);
//                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------
        }
        return listaGrid;
    }

    public String agendar() {
        AgendamentoDB db = new AgendamentoDBToplink();
        agendamento = (Agendamento) ((DataObject) listaGrid.get(idIndex)).getArgumento9();
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

        StatusDB dbSta = new StatusDBToplink();
        renderHomologacao = false;
        renderConcluir = true;
        
        if(nrStatus > 0) {
            fisica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFisica();
            juridica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getJuridica();
            pessoaEmpresa = agendamento.getPessoaEmpresa();
            profissao = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFuncao();
            tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
            for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                    idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                    break;
                }
            }
            switch (nrStatus) {
                case 2: {
                    renderHomologar = true;
                    renderCancelarHorario = true;
                    agendamento.setStatus(dbSta.pesquisaCodigo(5));
                    agendamento.setHomologador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                    db.update(agendamento);
                    break;
                }
                case 3: {
                    renderHomologar = true;
                    renderCancelarHorario = false;
                    agendamento.setStatus(dbSta.pesquisaCodigo(3));
                    agendamento.setHomologador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                    db.update(agendamento);
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

    public String salvar() {
        FisicaDB dbFis = new FisicaDBToplink();
        AgendamentoDB dbAg = new AgendamentoDBToplink();
        TipoDocumentoDB dbTipo = new TipoDocumentoDBToplink();
        DemissaoDB dbDem = new DemissaoDBToplink();
        StatusDB dbSta = new StatusDBToplink();
        List listDocumento;
        // List listDocumento = new ArrayList();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
            msgConfirma = "Digite o nome do Funcionário!";
            return null;
        }

//        DataHoje dataH = new DataHoje();
//        if (!pessoaEmpresa.getDemissao().equals("") || pessoaEmpresa.getDemissao() != null){
//            if ( DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao()) >
//                    DataHoje.converteDataParaInteger(dataH.incrementarMeses(1, DataHoje.data())) ){
//                    msgConfirma = "Data de Demissão maior que 30 dias!";
//                    return null;
//            }else if (DataHoje.converteDataParaInteger(pessoaEmpresa.getAdmissao()) >
//                    DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())){
//                    msgConfirma = "Data de Admissão é maior que data de Demissão!";
//                    return null;
//            }
//        }

        // SALVAR FISICA -----------------------------------------------
        fisica.getPessoa().setTipoDocumento(dbTipo.pesquisaCodigo(1));
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
            msgConfirma = "Documento Inválido!";
            return null;
        }
        sv.abrirTransacao();
        if (fisica.getId() == -1) {
            if (!dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg()).isEmpty()) {
                msgConfirma = "Esta pessoa já esta cadastrada!";
                return null;
            }
            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!listDocumento.isEmpty()) {
                msgConfirma = "Documento já existente!";
                return null;
            }
            if (sv.inserirObjeto(fisica.getPessoa())) {
                sv.inserirObjeto(fisica);
            } else {
                msgConfirma = "Erro ao Inserir pessoa!";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            for (int i = 0; i < listDocumento.size(); i++) {
                if (!listDocumento.isEmpty() && ((Fisica) listDocumento.get(i)).getId() != fisica.getId()) {
                    msgConfirma = "Documento já existente!";
                    return null;
                }
            }
            List<Fisica> fisi = dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg());
            if (!fisi.isEmpty()) {
                for (int i = 0; i < fisi.size(); i++) {
                    if (fisi.get(i).getId() != fisica.getId()) {
                        msgConfirma = "Esta pessoa já esta cadastrada!";
                        return null;
                    }
                }
            }
            if (sv.alterarObjeto(fisica.getPessoa())) {
                if (sv.alterarObjeto(fisica)) {
                } else {
                    msgConfirma = "Erro ao alterar Física!";
                    sv.desfazerTransacao();
                    return null;
                }
            } else {
                msgConfirma = "Erro ao alterar Pessoa!";
                sv.desfazerTransacao();
                return null;
            }
        }
        // -------------------------------------------------------------

        //pessoaEmpresa.setFisica(fisica);
        //pessoaEmpresa.setJuridica(juridica);
        //pessoaEmpresa.setFuncao(profissao);
        pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
        if (!sv.alterarObjeto(pessoaEmpresa)) {
            msgConfirma = "Erro ao alterar Pessoa Empresa!";
            sv.desfazerTransacao();
            return null;
        }

        agendamento.setDemissao(dbDem.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription())));
        agendamento.setPessoaEmpresa(pessoaEmpresa);
        //agendamento.setHomologador(null);

        if (registro.isSenhaHomologacao()) {
            if (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) != 4 && 
                Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) != 7) {
                Senha senha = dbAg.pesquisaSenhaAgendamento(agendamento.getId());
                senha.setMesa(0);
                senha.setHoraChamada("");
                if (!sv.alterarObjeto(senha)) {
                    msgConfirma = "Erro ao atualizar Senha!";
                    return null;
                }
            }
        }

        if (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) == 2) {
            agendamento.setStatus(dbSta.pesquisaCodigo(2));
        } else if (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) == 4) {
            agendamento.setStatus(dbSta.pesquisaCodigo(4));
        } else if (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) == 7) {
            agendamento.setStatus(dbSta.pesquisaCodigo(7));
            agendamento.setHomologador(null);
        } else if (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) == 5) {
            if(DataHoje.converteDataParaInteger(agendamento.getData()) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                agendamento.setStatus(dbSta.pesquisaCodigo(2));
                agendamento.setHomologador(null);
            }else {
                agendamento.setStatus(dbSta.pesquisaCodigo(5));
            }
        }

        if (sv.alterarObjeto(agendamento)) {
            sv.comitarTransacao();
            msgConfirma = "Registro atualizado com Sucesso!";
            limpar();
        }
        return null;
    }

    public String homologar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        StatusDB dbSta = new StatusDBToplink();
        agendamento.setHomologador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
        agendamento.setStatus(dbSta.pesquisaCodigo(4));
        sv.abrirTransacao();
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
        return null;
    }

    public String cancelarHorario() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        StatusDB dbSta = new StatusDBToplink();
        agendamento.setStatus(dbSta.pesquisaCodigo(3));

        sv.abrirTransacao();
        if (!sv.alterarObjeto(agendamento)) {
            sv.desfazerTransacao();
            return null;
        }

        pessoaEmpresa.setDtDemissao(null);
        if (!sv.alterarObjeto(pessoaEmpresa)) {
            sv.desfazerTransacao();
            return null;
        }

        strEndereco = "";
        renderHomologacao = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgConfirma = "";
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
        //msgConfirma ="";
        msgHomologacao = "";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
    }

    public String cancelar() {
        AgendamentoDB dbAg = new AgendamentoDBToplink();
        StatusDB dbSta = new StatusDBToplink();
        agendamento.setStatus(dbSta.pesquisaCodigo(5));
        dbAg.update(agendamento);
        strEndereco = "";
        renderHomologacao = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgConfirma = "";
        msgHomologacao = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        return null;
    }

    public String pesquisarFuncionarioCPF() {
        AgendamentoDB db = new AgendamentoDBToplink();
        TipoDocumentoDB dbTipo = new TipoDocumentoDBToplink();
        FisicaDB dbFis = new FisicaDBToplink();
        fisica.getPessoa().setTipoDocumento(dbTipo.pesquisaCodigo(1));
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

    public void refreshForm() {
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
        AgendamentoDB db = new AgendamentoDBToplink();
        List lista = new ArrayList();
        if (juridica.getId() != -1) {
            lista = db.pesquisaEmpresaEmDebito(juridica.getPessoa().getId(), DataHoje.data());
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
            registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String imprimirProtocolo(int proto) {
        if (proto == -1) {
            proto = getId_protocolo();
        }

        Collection lista = new ArrayList<ParametroProtocolo>();
        try {
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));

            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            Agendamento age = (Agendamento) sv.pesquisaCodigo(proto, "Agendamento");
            Juridica sindicato = (Juridica) sv.pesquisaCodigo(1, "Juridica");

            Juridica contabilidade;
            if (age.getPessoaEmpresa().getJuridica().getContabilidade() != null) {
                contabilidade = age.getPessoaEmpresa().getJuridica().getContabilidade();
            } else {
                contabilidade = new Juridica();
            }

            getEnderecoFilial();

            String datax = "", horario = "";
            if (!age.getData().isEmpty()) {
                datax = age.getData();
                horario = age.getHorarios().getHora();
            }
            Registro r = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");

            lista.add(new ParametroProtocolo(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Imagens/LogoCliente.png"),
                    sindicato.getPessoa().getNome(),
                    sindicato.getPessoa().getSite(),
                    sindicato.getPessoa().getTipoDocumento().getDescricao(),
                    sindicato.getPessoa().getDocumento(),
                    enderecoFilial.getEndereco().getDescricaoEndereco().getDescricao(),
                    enderecoFilial.getEndereco().getLogradouro().getDescricao(),
                    enderecoFilial.getNumero(),
                    enderecoFilial.getComplemento(),
                    enderecoFilial.getEndereco().getBairro().getDescricao(),
                    enderecoFilial.getEndereco().getCep(),
                    enderecoFilial.getEndereco().getCidade().getCidade(),
                    enderecoFilial.getEndereco().getCidade().getUf(),
                    macFilial.getFilial().getFilial().getPessoa().getTelefone1(),
                    macFilial.getFilial().getFilial().getPessoa().getEmail1(),
                    String.valueOf(age.getId()),
                    datax,
                    horario,
                    age.getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
                    age.getPessoaEmpresa().getJuridica().getPessoa().getNome(),
                    contabilidade.getPessoa().getNome(),
                    age.getPessoaEmpresa().getFisica().getPessoa().getNome(),
                    age.getPessoaEmpresa().getFisica().getPessoa().getDocumento(),
                    r.getDocumentoHomologacao(),
                    r.getFormaPagamentoHomologacao(),
                    age.getEmissao()));

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = "imp_protocolo_" + proto + ".pdf";

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Arquivos/downloads/protocolo");

            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload,
                    pathPasta,
                    "application/pdf",
                    FacesContext.getCurrentInstance());
            download.baixar();
        } catch (Exception e) {
        }
        return null;
    }

    public PessoaEndereco getEnderecoFilial() {
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        if (enderecoFilial.getId() == -1) {
            enderecoFilial = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(macFilial.getFilial().getFilial().getPessoa().getId(), 2);
        }
        return enderecoFilial;
    }

    public void setEnderecoFilial(PessoaEndereco enderecoFilial) {
        this.enderecoFilial = enderecoFilial;
    }

    public int getId_protocolo() {
        return id_protocolo;
    }

    public void setId_protocolo(int id_protocolo) {
        this.id_protocolo = id_protocolo;
    }

    public int senhaHomologacao(int id) {
        AgendamentoDB db = new AgendamentoDBToplink();
        Senha senha = db.pesquisaSenhaAgendamento(id);
        return senha.getSenha();
    }
}
