package br.com.rtools.homologacao.beans;

import br.com.rtools.atendimento.AteMovimento;
import br.com.rtools.atendimento.AteStatus;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.ListaAgendamento;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.dao.HomologacaoDao;
import br.com.rtools.homologacao.db.*;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class HomologacaoBean extends PesquisarProfissaoBean implements Serializable {

    private String msgConfirma = "";
    private String strEndereco = "";
    private String tipoAviso = "true";
    private String strData = DataHoje.data();
    private String statusEmpresa = "REGULAR";
    private boolean renderHomologar = false;
    private boolean renderCancelarHorario = false;
    private Date data = DataHoje.dataHoje();
    private int idStatus = 0;
    private int idMotivoDemissao = 0;
    private int idIndex = -1;
    private List<ListaAgendamento> listaHomologacoes = new ArrayList();
    private final List<SelectItem> listaStatus = new ArrayList();
    private final List<SelectItem> listaDemissao = new ArrayList();
    private Agendamento agendamento = new Agendamento();
    private Juridica juridica = new Juridica();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private Fisica fisica = new Fisica();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private MacFilial macFilial = null;
    private Registro registro = new Registro();
    private Cancelamento cancelamento = new Cancelamento();
    private Senha senhaAtendimento = new Senha();
    private List<Senha> listaAtendimentoSimples = new ArrayList();
    private List listFiles = new ArrayList();

    private boolean visibleModal = false;
    private String tipoTelefone = "telefone";

    public HomologacaoBean() {
        macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
        registro = (Registro) new SalvarAcumuladoDBToplink().find("Registro", 1);

        if (macFilial != null) {
            this.loadListaHomologacao();
            this.loadListaAtendimentoSimples();
        }
    }

    public void alterarTipoMascara() {
        if (tipoTelefone.equals("telefone")) {
            tipoTelefone = "celular";
        } else {
            tipoTelefone = "telefone";
        }
        agendamento.setTelefone("");
    }

    public String retornaOposicaoPessoa(String documento) {
        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
        if (atendimentoDB.pessoaOposicao(documento)) {
            return "tblOposicaox";
        } else {
            return "";
        }
    }

    public final void loadListaHomologacao() {
        listaHomologacoes.clear();
        try {
            Polling polling = new Polling();
            polling.existeUsuarioSessao();
        } catch (IOException e) {
            return;
        }

        if (macFilial == null) {
            return;
        }

        if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) > DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            return;
        }

        HomologacaoDB db = new HomologacaoDBToplink();
        Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        int idUsuario;
        int idCaso = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        if (idCaso <= 0) {
            return;
        }
        if (idCaso == 2 || idCaso == 7) {
            idUsuario = 0;
        } else {
            idUsuario = us.getId();
        }
        List<Agendamento> agendamentos = db.pesquisaAgendamento(idCaso, macFilial.getFilial().getId(), data, null, idUsuario, 0, 0, false, false);

        for (int i = 0; i < agendamentos.size(); i++) {
            ListaAgendamento listaAgendamento = new ListaAgendamento();
            listaAgendamento.setAgendamento(agendamentos.get(i));
            if (registro.isSenhaHomologacao()) {
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

            AtendimentoDB dbat = new AtendimentoDBTopLink();
            if (dbat.pessoaOposicao(agendamentos.get(i).getPessoaEmpresa().getFisica().getPessoa().getDocumento())) {
                listaAgendamento.setTblEstilo("tblAgendamentoOposicaox");
            }

            if (agendamentos.get(i).getAgendador() == null) {
                listaAgendamento.setUsuarioAgendador("** Web User **");
            } else {
                listaAgendamento.setUsuarioAgendador(agendamentos.get(i).getAgendador().getPessoa().getNome());
            }
            listaHomologacoes.add(listaAgendamento);
        }
    }

    public final void loadListaAtendimentoSimples() {
        listaAtendimentoSimples.clear();

        if (macFilial != null) {
            HomologacaoDB db = new HomologacaoDBToplink();
            SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();

            listaAtendimentoSimples = db.listaAtendimentoIniciadoSimples(macFilial.getFilial().getId(), su.getSessaoUsuario().getId());
        }
    }

    public void reservarAtendimento(AteMovimento amov) {
        Dao dao = new Dao();
        SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();

        dao.openTransaction();
        if (amov.getReserva() == null) {
            amov.setReserva(su.getSessaoUsuario());
            if (!dao.update(amov)) {
                GenericaMensagem.error("Erro", "Não foi possível salvar reserva!");
                dao.rollback();
                return;
            }
            GenericaMensagem.info("OK", "Atendimento Reservado!");
        } else {
            if (su.getSessaoUsuario().getId() == amov.getReserva().getId()) {
                amov.setReserva(null);
                if (!dao.update(amov)) {
                    GenericaMensagem.error("Erro", "Não foi possível salvar reserva!");
                    dao.rollback();
                    return;
                }
                GenericaMensagem.info("OK", "Reserva desfeita!");
            } else {
                GenericaMensagem.warn("Atenção", "Atendimento não pode ser desfeito!");
                dao.rollback();
                return;
            }
        }
        dao.commit();
        PF.openDialog("dlg_reserva_atendimento");
    }

    public String excluirSenha() {
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        Senha senha = homologacaoDB.pesquisaSenhaAgendamento(agendamento.getId());
        if (senha.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Não existe senha para ser excluida!");
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.find("Senha", senha.getId()))) {
            GenericaMensagem.error("Atenção", "Erro ao excluir senha!");
            return null;
        }
        agendamento.setStatus((Status) sv.find("Status", 2));
        agendamento.setHomologador(null);
        if (!sv.alterarObjeto(agendamento)) {
            //msgHomologacao = "Erro ao atualizar Agendamento";
            GenericaMensagem.error("Erro", "Não foi possível atualizar Agendamento!");
            return null;
        }
        GenericaMensagem.info("Sucesso", "Senha Excluída !");
        sv.comitarTransacao();
        strEndereco = "";
        visibleModal = false;
        tipoAviso = "true";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        loadListaHomologacao();
        return null;
    }

    public void atualizarSenhaSimples(String tipo) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();
        Senha senha = (Senha) sv.pesquisaCodigo(senhaAtendimento.getId(), "Senha");
        if (tipo.equals("atendido")) {
            senha.getAteMovimento().setStatus((AteStatus) sv.pesquisaCodigo(2, "AteStatus"));
        } else {
            senha.getAteMovimento().setStatus((AteStatus) sv.pesquisaCodigo(3, "AteStatus"));
        }

        if (!sv.alterarObjeto(senha)) {
            sv.desfazerTransacao();
        } else {
            sv.comitarTransacao();
        }

        senhaAtendimento = new Senha();
        loadListaAtendimentoSimples();
        PF.closeDialog("dlg_atendimento_simples");
    }

    public void excluirSenhaAtendimento() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();
        Senha senha = (Senha) sv.pesquisaCodigo(senhaAtendimento.getId(), "Senha");
        AteMovimento at = (AteMovimento) sv.pesquisaCodigo(senhaAtendimento.getAteMovimento().getId(), "AteMovimento");

        // DELETA A SENHA
        if (!sv.deletarObjeto(senha)) {
            sv.desfazerTransacao();
            return;
        }

        // DELETA TAMBEM O ATENDIMENTO
        if (!sv.deletarObjeto(at)) {
            sv.desfazerTransacao();
            return;
        }

        sv.comitarTransacao();
        senhaAtendimento = new Senha();

        loadListaAtendimentoSimples();
        PF.closeDialog("dlg_atendimento_simples");
    }

    public List<Senha> getListaAtendimentoSimples() {
        return listaAtendimentoSimples;
    }

    public String retornaSequenciaSenha() {
        HomologacaoDB dbh = new HomologacaoDBToplink();
        SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();
        Dao di = new Dao();

        // SENHA COM HOMOLOGAÇÃO INICIADA -----
        Senha senha = dbh.pesquisaAtendimentoIniciado(su.getSessaoUsuario().getId(), macFilial.getMesa(), macFilial.getFilial().getId());
        if (senha.getId() != -1) {
            agendamento = senha.getAgendamento();
            if (agendamento.getTelefone().length() > 14) {
                tipoTelefone = "celular";
            } else {
                tipoTelefone = "telefone";
            }
            fisica = senha.getAgendamento().getPessoaEmpresa().getFisica();
            juridica = senha.getAgendamento().getPessoaEmpresa().getJuridica();
            pessoaEmpresa = senha.getAgendamento().getPessoaEmpresa();
            profissao = senha.getAgendamento().getPessoaEmpresa().getFuncao();
            renderHomologar = true;
            visibleModal = true;
            renderHomologar = true;
            renderCancelarHorario = true;

            GenericaSessao.put("juridicaPesquisa", juridica);

            for (int i = 0; i < getListaDemissao().size(); i++) {
                if (Integer.parseInt(listaDemissao.get(i).getDescription()) == agendamento.getDemissao().getId()) {
                    idMotivoDemissao = (Integer) listaDemissao.get(i).getValue();
                    break;
                }
            }

            tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
            agendamento.setStatus((Status) di.find(new Status(), 5));
            agendamento.setHomologador(su.getSessaoUsuario());

            PF.update("formConcluirHomologacao");
            PF.openDialog("dlg_homologacao");

            return null;
        }

        List<Senha> listax = dbh.listaAtendimentoIniciadoSimplesUsuario(macFilial.getFilial().getId(), su.getSessaoUsuario().getId());

        if (!listax.isEmpty()) {
            senhaAtendimento = listax.get(0);
            PF.update("formHomologacao:tbl_at");
            PF.openDialog("dlg_atendimento_simples");
            return null;
        }
//        
//        // SENHA COM ATENDIMENTO INICIADO -----
//        Senha senhaAtendimentoIniciado = dbh.pesquisaAtendimentoIniciadoSimples(macFilial.getFilial().getId());
//        if (senhaAtendimentoIniciado.getId() != -1){
//            return senhaAtendimentoIniciado;
//        }

        List<Senha> listaSenha = dbh.listaSequenciaSenha(macFilial.getFilial().getId());

        if (listaSenha.isEmpty()) {
            return null;
        }

        for (Senha senhax : listaSenha) {
//            // SENHA DE ATENDIMENTO INICIADO -----
//            if (senhax.getAteMovimento() != null && senhax.getAteMovimento().getAtendente() != null){
//                if (senhax.getAteMovimento().getAtendente().getId() == su.getSessaoUsuario().getId() && senhax.getAteMovimento().getStatus().getId() == 4){
//                    senhaAtendimento = senhax;
//                    PF.update(":form_cancelar_data_table:tbl_at");
//                    PF.openDialog("dlg_atendimento_simples");
//                    return null;
//                }
//            }

            // SENHA DE HOMOLOGAÇÃO --------------------------------------------------------------------------------------------------------------------------
            if (senhax.getAgendamento() != null) {
                if (senhax.getAgendamento().getStatus().getId() != 2) {
                    continue;
                }

                agendamento = senhax.getAgendamento();
                if (agendamento.getTelefone().length() > 14) {
                    tipoTelefone = "celular";
                } else {
                    tipoTelefone = "telefone";
                }
                fisica = senhax.getAgendamento().getPessoaEmpresa().getFisica();
                juridica = senhax.getAgendamento().getPessoaEmpresa().getJuridica();
                pessoaEmpresa = senhax.getAgendamento().getPessoaEmpresa();
                profissao = senhax.getAgendamento().getPessoaEmpresa().getFuncao();
                renderHomologar = true;
                visibleModal = true;
                renderHomologar = true;
                renderCancelarHorario = true;

                GenericaSessao.put("juridicaPesquisa", juridica);

                for (int i = 0; i < getListaDemissao().size(); i++) {
                    if (Integer.parseInt(listaDemissao.get(i).getDescription()) == agendamento.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) listaDemissao.get(i).getValue();
                        break;
                    }
                }

                tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
                agendamento.setStatus((Status) di.find(new Status(), 5));
                agendamento.setHomologador(su.getSessaoUsuario());
                di.openTransaction();
                if (!di.update(agendamento)) {
                    GenericaMensagem.error("Erro", "Não foi possível atualizar Agendamento!");
                    return null;
                }

                senhax.setMesa(macFilial.getMesa());
                senhax.setHoraChamada(DataHoje.horaMinuto());
                if (!di.update(senhax)) {
                    GenericaMensagem.error("Erro", "Não foi possível atualizar Senha!");
                    return null;
                }
                di.commit();

                PF.update("formConcluirHomologacao");
                PF.openDialog("dlg_homologacao");

                return null;
            }

            // SENHA DE ATENDIMENTO --------------------------------------------------------------------------------------------------------------------------
            if (senhax.getAteMovimento() != null) {
                // STATUS 1 - AGUARDANDO
                if (senhax.getAteMovimento().getStatus().getId() == 1) {
                    // OPERAÇÃO 8 - DSR - OBRIGATORIO SENHA TER RESERVADA ----
                    if (senhax.getAteMovimento().getOperacao().getId() == 8 && senhax.getAteMovimento().getReserva() == null) {
                        continue;
                    }

                    // RESERVADO PARA O USUÁRIO ------
                    if (senhax.getAteMovimento().getReserva() != null && (senhax.getAteMovimento().getReserva().getId() == su.getSessaoUsuario().getId())) {
                        di.openTransaction();

                        senhax.getAteMovimento().setStatus((AteStatus) di.find(new AteStatus(), 4));
                        senhax.getAteMovimento().setAtendente(su.getSessaoUsuario());

                        senhax.setHoraChamada(DataHoje.horaMinuto());
                        senhax.setMesa(macFilial.getMesa());

                        di.update(senhax);
                        di.update(senhax.getAteMovimento());

                        di.commit();

                        //listaAtendimentoSimples.clear();
                        senhaAtendimento = senhax;
                        PF.update(":formHomologacao:tbl_at");
                        PF.openDialog("dlg_atendimento_simples");
                        return null;
                    }

                    // NÃO É RESERVA ----
                    if (senhax.getAteMovimento().getReserva() == null) {
                        di.openTransaction();

                        senhax.getAteMovimento().setStatus((AteStatus) di.find(new AteStatus(), 4));
                        senhax.getAteMovimento().setAtendente(su.getSessaoUsuario());

                        senhax.setHoraChamada(DataHoje.horaMinuto()); // aqui
                        senhax.setMesa(macFilial.getMesa());

                        di.update(senhax);
                        di.update(senhax.getAteMovimento());

                        di.commit();

                        //listaAtendimentoSimples.clear();
                        senhaAtendimento = senhax;
                        PF.update(":formHomologacao:tbl_at");
                        PF.openDialog("dlg_atendimento_simples");
                        return null;
                    }
                }
            }
        }
        return "homologacao";
    }

    public String atendimento() {
        if (macFilial.getId() == -1) {
            //msgHomologacao = "Mac não encontrado!";
            GenericaMensagem.warn("Atenção", "MAC não foi encontrado!");
            return "homologacao";
        }

        retornaSequenciaSenha();

        if (1 == 1) {
            return null;
        }
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        Senha senhaAtendimentoReserva = homologacaoDB.pesquisaAtendimentoReserva(macFilial.getFilial().getId(), su.getSessaoUsuario().getId());
        Senha senhaAtendimentox = homologacaoDB.pesquisaAtendimentoIniciadoSimples(macFilial.getFilial().getId());
        Senha senhaHomologacao = homologacaoDB.pesquisaSenhaAtendimento(macFilial.getFilial().getId());

        // SENHA DE ATENDIMENTO RESERVADA
        if (senhaAtendimentoReserva != null) {
            sv.abrirTransacao();

            senhaAtendimentoReserva.getAteMovimento().setStatus((AteStatus) sv.pesquisaCodigo(4, "AteStatus"));
            senhaAtendimentoReserva.getAteMovimento().setAtendente(su.getSessaoUsuario());

            senhaAtendimentoReserva.setHoraChamada(DataHoje.horaMinuto());
            senhaAtendimentoReserva.setMesa(macFilial.getMesa());

            sv.alterarObjeto(senhaAtendimentoReserva);
            sv.alterarObjeto(senhaAtendimentoReserva.getAteMovimento());

            sv.comitarTransacao();

            listaAtendimentoSimples.clear();

            senhaAtendimento = senhaAtendimentoReserva;
            PF.update(":formHomologacao:tbl_at");
            PF.openDialog("dlg_atendimento_simples");
            return null;
        }

        // SENHA DE ATENDIMENTO
        if (senhaAtendimentox != null && senhaHomologacao.getId() == -1) {
            sv.abrirTransacao();

            senhaAtendimentox.getAteMovimento().setStatus((AteStatus) sv.pesquisaCodigo(4, "AteStatus"));
            senhaAtendimentox.getAteMovimento().setAtendente(su.getSessaoUsuario());

            senhaAtendimentox.setHoraChamada(DataHoje.horaMinuto());
            senhaAtendimentox.setMesa(macFilial.getMesa());

            sv.alterarObjeto(senhaAtendimentox);
            sv.alterarObjeto(senhaAtendimentox.getAteMovimento());

            sv.comitarTransacao();

            listaAtendimentoSimples.clear();

            senhaAtendimento = senhaAtendimentox;
            RequestContext.getCurrentInstance().update(":formHomologacao:tbl_at");
            RequestContext.getCurrentInstance().execute("PF('dlg_atendimento_simples').show();");
            return null;
        } else if (senhaAtendimentox != null && (senhaAtendimentox.getSenha() < senhaHomologacao.getSenha())) {
            sv.abrirTransacao();

            senhaAtendimentox.getAteMovimento().setStatus((AteStatus) sv.pesquisaCodigo(4, "AteStatus"));
            senhaAtendimentox.getAteMovimento().setAtendente(su.getSessaoUsuario());

            senhaAtendimentox.setHoraChamada(DataHoje.horaMinuto());
            senhaAtendimentox.setMesa(macFilial.getMesa());

            sv.alterarObjeto(senhaAtendimentox);
            sv.alterarObjeto(senhaAtendimentox.getAteMovimento());

            sv.comitarTransacao();

            listaAtendimentoSimples.clear();

            senhaAtendimento = senhaAtendimentox;
            RequestContext.getCurrentInstance().update(":formHomologacao:tbl_at");
            RequestContext.getCurrentInstance().execute("PF('dlg_atendimento_simples').show();");
            return null;
        }

        // SENHA PADRÃO DE HOMOLOGAÇÃO
        Senha senhaHomologacaoI = homologacaoDB.pesquisaAtendimentoIniciado(su.getSessaoUsuario().getId(), macFilial.getMesa(), macFilial.getFilial().getId());
        if (senhaHomologacaoI.getId() != -1) {
            senhaHomologacao = senhaHomologacaoI;
        }

        if (senhaHomologacao.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Senha não encontrada!");
            return null;
        }

        agendamento = senhaHomologacao.getAgendamento();
        fisica = senhaHomologacao.getAgendamento().getPessoaEmpresa().getFisica();
        juridica = senhaHomologacao.getAgendamento().getPessoaEmpresa().getJuridica();
        pessoaEmpresa = senhaHomologacao.getAgendamento().getPessoaEmpresa();
        profissao = senhaHomologacao.getAgendamento().getPessoaEmpresa().getFuncao();
        renderHomologar = true;
        visibleModal = true;
        renderHomologar = true;
        renderCancelarHorario = true;

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);

        for (int i = 0; i < getListaDemissao().size(); i++) {
            if (Integer.parseInt(listaDemissao.get(i).getDescription()) == agendamento.getDemissao().getId()) {
                idMotivoDemissao = (Integer) listaDemissao.get(i).getValue();
                break;
            }
        }
        tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
        agendamento.setStatus((Status) sv.find("Status", 5));
        agendamento.setHomologador(su.getSessaoUsuario());
        sv.abrirTransacao();
        if (!sv.alterarObjeto(agendamento)) {
            //msgHomologacao = "Erro ao atualizar Agendamento";
            GenericaMensagem.error("Erro", "Não foi possível atualizar Agendamento!");
            return "homologacao";
        }
        senhaHomologacao.setMesa(macFilial.getMesa());
        senhaHomologacao.setHoraChamada(DataHoje.horaMinuto());
        if (!sv.alterarObjeto(senhaHomologacao)) {
            //msgHomologacao = "Erro ao atualizar Senha";
            GenericaMensagem.error("Erro", "Não foi possível atualizar Senha!");
            return "homologacao";
        }
        sv.comitarTransacao();

        return "homologacao";
    }

    public void fecharModalSenha() {
        if (senhaAtendimento.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            sv.abrirTransacao();

            senhaAtendimento.getAteMovimento().setStatus((AteStatus) sv.pesquisaCodigo(1, "AteStatus"));
            senhaAtendimento.getAteMovimento().setAtendente(null);

            senhaAtendimento.setHoraChamada("");
            senhaAtendimento.setMesa(0);

            sv.alterarObjeto(senhaAtendimento);
            sv.alterarObjeto(senhaAtendimento.getAteMovimento());

            sv.comitarTransacao();
            senhaAtendimento = new Senha();
            //listaAtendimentoSimples.clear();
            loadListaAtendimentoSimples();
        }
        PF.closeDialog("dlg_atendimento_simples");
        listFiles.clear();
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

//    public synchronized List getListaHorarios() {
//        if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) > DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
//            return new ArrayList();
//        }
//        if (macFilial == null) {
//            return new ArrayList();
//        }
////        listaGrid.clear();
////        listaGrid = new ArrayList();
//        List<Agendamento> ag;
//        HomologacaoDB db = new HomologacaoDBToplink();
//        String agendador;
//        String homologador;
//        DataObject dtObj;
//        Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
//        Registro reg = (Registro) new SalvarAcumuladoDBToplink().find("Registro", 1);
//        int idUsuario;
//        int idCaso = Integer.parseInt(((SelectItem) listaStatus.get(idStatus)).getDescription());
//        if (idCaso <= 0) {
//            return new ArrayList();
//        }
//        if (idCaso == 2 || idCaso == 7) {
//            idUsuario = 0;
//        } else {
//            idUsuario = us.getId();
//        }
//        ag = db.pesquisaAgendamento(idCaso, macFilial.getFilial().getId(), data, null, idUsuario, 0, 0);
//        for (int i = 0; i < ag.size(); i++) {
//            Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
//            if (ag.get(i).getAgendador() != null) {
//                agendador = ag.get(i).getAgendador().getPessoa().getNome();
//            } else {
//                agendador = "** Web User **";
//            }
//            if (ag.get(i).getHomologador() != null) {
//                homologador = ag.get(i).getHomologador().getPessoa().getNome();
//            } else {
//                homologador = "";
//            }
//            boolean isHabilitaAlteracao = false;
//            if (reg.isSenhaHomologacao()) {
//                if (DataHoje.converteDataParaInteger(DataHoje.converteData(ag.get(i).getDtData())) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
//                    if (ag.get(i).getStatus().getId() == 2) {
//                        isHabilitaAlteracao = true;
//                    } else {
//                        isHabilitaAlteracao = false;
//                    }
//                }
//                if (senha.getId() == -1) {
//                    if (ag.get(i).getStatus().getId() != 7
//                            && ag.get(i).getStatus().getId() != 3
//                            && ag.get(i).getStatus().getId() != 4
//                            && ag.get(i).getStatus().getId() != 5) {
//                        continue;
//                    }
//                }
//            } else {
//                if (DataHoje.converteDataParaInteger(DataHoje.converteData(ag.get(i).getDtData())) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
//                    isHabilitaAlteracao = false;
//                }
//            }
//            if (DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje())) > DataHoje.converteDataParaInteger(DataHoje.converteData(ag.get(i).getDtData()))) {
//                isHabilitaAlteracao = false;
//            }
//            dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
//                    ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
//                    ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
//                    homologador, //ARG 3 HOMOLOGADOR
//                    ag.get(i).getContato(), // ARG 4 CONTATO
//                    ag.get(i).getTelefone(), // ARG 5 TELEFONE
//                    agendador, // ARG 6 USUARIO
//                    ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
//                    null, // ARG 8
//                    ag.get(i), // ARG 9 AGENDAMENTO
//                    senha.getId() == -1 ? null : senha.getSenha(), // ARG 10 SENHA PARA ATENDIMENTO
//                    null,
//                    null,
//                    null,
//                    null,
//                    isHabilitaAlteracao // ARG 15 HABILITA ALTERAÇÃO DO STATUS
//            );
//
//            listaGrid.add(dtObj);
//        }
//        return listaGrid;
//    }
    public String agendar(Agendamento a) {
        HomologacaoDB db = new HomologacaoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendamento = a;
        cancelamento = new Cancelamento();
        int nrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        boolean hc = false;
        if (registro.getHomolocaoHabilitaCorrecao() != null && DataHoje.converteData(registro.getHomolocaoHabilitaCorrecao()).equals(DataHoje.data())) {
            hc = true;
        }
        if (nrStatus == 4) {
            if (!desabilitaEdicao(agendamento.getDtData(), 30) && !hc) {
                GenericaMensagem.warn("Atenção", "Não é possível realizar alterações com datas superiores a 30 dias a data de hoje. Contate o administrador do sistema para habilitar a correção de homologações pendentes!");
                PF.update("form_homologacao:i_msg");
//                msgConfirma = "Não é possível realizar alterações com datas superiores a 30 dias a data de hoje. Contate o administrador do sistema para habilitar a correção de homologações pendentes!";
//                PF.update("form_homologacao:i_painel_mensagem");
//                PF.openDialog("dgl_painel_mensagem");
                return null;
            }
        } else if (nrStatus == 3 || nrStatus == 5) {
            if (!desabilitaEdicao(agendamento.getDtData(), 30) && !hc) {
                GenericaMensagem.warn("Atenção", "Não é possível realizar alterações com datas superiores a 30 dias a data de hoje. Contate o administrador do sistema para habilitar a correção de homologações pendentes!");
                PF.update("form_homologacao:i_msg");
//                msgConfirma = "Não é possível realizar alterações com datas superiores a 30 dias a data de hoje. Contate o administrador do sistema para habilitar a correção de homologações pendentes!";
//                PF.update("form_homologacao:i_painel_mensagem");
//                PF.openDialog("dgl_painel_mensagem");
                return null;
            }
        } else {
            if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                if (registro.isSenhaHomologacao()) {
                    Senha senha = db.pesquisaSenhaAgendamento(agendamento.getId());
                    if (senha.getId() == -1) {
//                        msgConfirma = "Não há senha definida!";
                        GenericaMensagem.warn("Atenção", "Não existe uma senha para essa Homologação!");
                        PF.update("form_homologacao:i_msg");
                        //PF.openDialog("dgl_painel_mensagem");
                        return null;
                    }
                }
            }
        }

        visibleModal = true;

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
                    visibleModal = true;
                    break;
                }
                case 5: {
                    if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == agendamento.getHomologador().getId()) {
                        renderHomologar = true;
                        renderCancelarHorario = true;
                        visibleModal = true;
                        break;
                    } else {
                        renderHomologar = false;
                        renderCancelarHorario = false;
                        visibleModal = false;
                        agendamento = new Agendamento();
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

        if (agendamento.getTelefone().length() > 14) {
            tipoTelefone = "celular";
        } else {
            tipoTelefone = "telefone";
        }
        return "homologacao";
    }

    public void salvar() {
        List listDocumento;
        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
            GenericaMensagem.error("Atenção", "Digite o nome do Funcionário!");
            return;
        }
        // SALVAR FISICA -----------------------------------------------
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        fisica.getPessoa().setTipoDocumento((TipoDocumento) sv.find("TipoDocumento", 1));
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
            GenericaMensagem.error("Atenção", "Documento Inválido!");
            return;
        }
        FisicaDB fisicaDB = new FisicaDBToplink();
        sv.abrirTransacao();
        if (fisica.getId() == -1) {
            if (!fisicaDB.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(), fisica.getDtNascimento(), fisica.getRg()).isEmpty()) {
                GenericaMensagem.error("Atenção", "Esta pessoa já esta cadastrada!");
                return;
            }
            listDocumento = fisicaDB.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!listDocumento.isEmpty()) {
                GenericaMensagem.error("Atenção", "Documento já existente!");
                return;
            }
            if (sv.inserirObjeto(fisica.getPessoa())) {
                sv.inserirObjeto(fisica);
            } else {
                GenericaMensagem.error("Atenção", "Erro ao Inserir pessoa!");
                sv.desfazerTransacao();
                return;
            }
        } else {
            listDocumento = fisicaDB.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            for (Object listDocumento1 : listDocumento) {
                if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != fisica.getId()) {
                    GenericaMensagem.error("Atenção", "Documento já existente!");
                    return;
                }
            }
            List<Fisica> fisi = fisicaDB.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(), fisica.getDtNascimento(), fisica.getRg());
            if (!fisi.isEmpty()) {
                for (Fisica fisi1 : fisi) {
                    if (fisi1.getId() != fisica.getId()) {
                        GenericaMensagem.error("Atenção", "Esta pessoa já esta cadastrada!");
                        return;
                    }
                }
            }
            if (sv.alterarObjeto(fisica.getPessoa())) {
                if (sv.alterarObjeto(fisica)) {
                } else {
                    GenericaMensagem.error("Atenção", "Erro ao alterar Física!");
                    sv.desfazerTransacao();
                    return;
                }
            } else {
                GenericaMensagem.error("Atenção", "Erro ao alterar Pessoa!");
                sv.desfazerTransacao();
                return;
            }
        }
        // -------------------------------------------------------------
        pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
        if (this.profissao.getId() == -1) {
            pessoaEmpresa.setFuncao(null);
        } else {
            pessoaEmpresa.setFuncao(this.profissao);
        }
        if (!sv.alterarObjeto(pessoaEmpresa)) {
            GenericaMensagem.error("Atenção", "Erro ao alterar Pessoa Empresa!");
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
                    GenericaMensagem.error("Atenção", "Erro ao atualizar Senha!");
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
            GenericaMensagem.info("Sucesso", "Registro atualizado!");
            //limpar();
        }
        loadListaHomologacao();
    }

    public void closeModal() {
        HomologacaoDB db = new HomologacaoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        int nrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        sv.abrirTransacao();
        if (registro.isSenhaHomologacao()) {
            if (nrStatus != 4 && nrStatus != 7) {
                Senha senha = db.pesquisaSenhaAgendamento(agendamento.getId());
                senha.setMesa(0);
                senha.setHoraChamada("");
                if (!sv.alterarObjeto(senha)) {
                    msgConfirma = "Erro ao atualizar Senha!";
                    sv.desfazerTransacao();
                    return;
                }
            }
        }

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
            sv.comitarTransacao();
        } else {
            sv.desfazerTransacao();
        }
    }

    public void homologar() {
        if (pessoaEmpresa.getAdmissao().isEmpty()) {
            GenericaMensagem.error("Atenção", "Data de Admissão não pode ser vazio!");
            return;
        }

        if (pessoaEmpresa.getDemissao().isEmpty()) {
            GenericaMensagem.error("Atenção", "Data de Demissão não pode ser vazio!");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        agendamento.setHomologador((Usuario) GenericaSessao.getObject("sessaoUsuario"));
        agendamento.setStatus((Status) sv.find(new Status(), 4));
        new Cancelamento().getAgendamento().getId();
        Cancelamento c = (Cancelamento) sv.pesquisaObjeto(agendamento.getId(), "Cancelamento", "agendamento.id");
        sv.abrirTransacao();
        if (c != null) {
            if (!sv.deletarObjeto(c)) {
                GenericaMensagem.error("Atenção", "Erro ao homologar!");
                sv.desfazerTransacao();
                return;
            }
        }
        if (sv.alterarObjeto(agendamento) && sv.alterarObjeto(pessoaEmpresa)) {
            GenericaMensagem.info("Sucesso", "Agendamento homologado!");
            sv.comitarTransacao();
        } else {
            GenericaMensagem.error("Atenção", "Erro ao homologar!");
            sv.desfazerTransacao();
        }
        strEndereco = "";
        visibleModal = false;
        tipoAviso = "true";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        loadListaHomologacao();
    }

    public String cancelarHomologacao() {
        if (cancelamento.getMotivo().isEmpty() || cancelamento.getMotivo().length() <= 5) {
            GenericaMensagem.warn("Atenção", "Motivo de cancelamento inválido");
            return null;
        }
        Dao dao = new Dao();
        Status s = agendamento.getStatus();
        dao.openTransaction();
        if (!dao.update(agendamento)) {
            GenericaMensagem.error("Atenção", "Erro ao cancelar homologagação!");
            dao.rollback();
            return null;
        }
        pessoaEmpresa.setDtDemissao(null);
        if (!dao.update(pessoaEmpresa)) {
            GenericaMensagem.error("Atenção", "Erro ao atualizar Pessoa Empresa");
            return null;
        }
        cancelamento.setAgendamento(agendamento);
        cancelamento.setDtData(DataHoje.dataHoje());
        cancelamento.setUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")));
        if (!dao.save(cancelamento)) {
            GenericaMensagem.error("Atenção", "Erro ao salvar cancelamento");
            return null;
        }
        agendamento.setStatus((Status) dao.find(new Status(), 3));
        if (!dao.update(agendamento)) {
            dao.rollback();
            agendamento.setStatus(s);
            GenericaMensagem.error("Atenção", "Erro ao cancelar homologação!");
            return null;
        }
        cancelamento = new Cancelamento();
        GenericaMensagem.info("Sucesso", "Homologação Cancelada !");
        strEndereco = "";
        visibleModal = false;
        tipoAviso = "true";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        dao.commit();
        loadListaHomologacao();
        return null;
    }

    public void limpar() {
        strEndereco = "";
        visibleModal = false;
        tipoAviso = "true";
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
        PessoaEmpresa pe = db.pesquisaPessoaEmpresaPertencente(fisica.getPessoa().getDocumento());
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

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
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

    public synchronized List<ListaAgendamento> getListaHomologacoes() {
        return listaHomologacoes;
    }

    public void setListaHomologacoes(List<ListaAgendamento> listaHomologacoes) {
        this.listaHomologacoes = listaHomologacoes;
    }

    public List<Agendamento> listAtendimentoAberto() {
        List<Agendamento> list = new ArrayList();
        if (GenericaSessao.exists("sessaoUsuario")) {
            HomologacaoDao dao = new HomologacaoDao();
            list = (List<Agendamento>) dao.pesquisaAgendamentoAtendimentoAberto(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId());
        }
        return list;
    }

    public Senha getSenhaAtendimento() {
        return senhaAtendimento;
    }

    public void setSenhaAtendimento(Senha senhaAtendimento) {
        this.senhaAtendimento = senhaAtendimento;
    }

    public boolean isVisibleModal() {
        return visibleModal;
    }

    public void setVisibleModal(boolean visibleModal) {
        this.visibleModal = visibleModal;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    // ARQUIVOS
    public List getListFiles() {
        listFiles.clear();
        listFiles = Diretorio.listaArquivos("Arquivos/homologacao/" + agendamento.getId());
        return listFiles;
    }

    public void upload(FileUploadEvent event) {
        ConfiguracaoUpload configuracaoUpload = new ConfiguracaoUpload();
        configuracaoUpload.setArquivo(event.getFile().getFileName());
        configuracaoUpload.setDiretorio("Arquivos/homologacao/" + agendamento.getId());
        configuracaoUpload.setEvent(event);
        if (Upload.enviar(configuracaoUpload, true)) {
            listFiles.clear();
        }
        getListFiles();
    }

    public void deleteFiles(int index) {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/homologacao/" + agendamento.getId() + "/" + (String) ((DataObject) listFiles.get(index)).getArgumento1());
        File fl = new File(caminho);
        fl.delete();
        listFiles.remove(index);
        listFiles.clear();
        getListFiles();
        PF.update("formConcluirHomolocagao:id_grid_uploads");
        PF.update("formConcluirHomolocagao:id_btn_anexo");
    }

}
