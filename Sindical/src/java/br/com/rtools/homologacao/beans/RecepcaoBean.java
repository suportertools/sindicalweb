package br.com.rtools.homologacao.beans;

import br.com.rtools.atendimento.AteMovimento;
import br.com.rtools.atendimento.AteStatus;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.ListaAgendamento;
import br.com.rtools.homologacao.Recepcao;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.dao.HomologacaoDao;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.impressao.ParametroSenha;
import br.com.rtools.impressao.beans.SenhaHomologacao;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.Upload;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

@ManagedBean
@SessionScoped
public class RecepcaoBean implements Serializable {

    private Agendamento agendamento;
    private Agendamento agendamentoEdit;
    private int idStatus;
    private boolean ocultaData;
    private boolean ocultaSenha;
    private boolean ocultaStatus;
    private boolean ocultaPreposto;
    private boolean ocultaHomologador;
    private boolean ocultaDatapesquisa;
    private boolean ocultaColunaEmpresa;
    private boolean ocultaColunaPessoaFisica;
    private boolean isPesquisarPessoaFisicaFiltro;
    private boolean isPesquisarPessoaJuridicaFiltro;
    private boolean desabilitaAtualizacaoAutomatica;
    private boolean desabilitaPesquisaProtocolo;
    private boolean dataPesquisaTodas;
    private int isCaso;
    private List<DataObject> listaHorarios;
    private MacFilial macFilial;
    private String strData;
    private String strDataFinal;
    private String strEndereco;
    private String msgRecepcao;
    private String msgConfirma;
    private String statusEmpresa;
    private Date data;
    private Date dataInicial;
    private Date dataFinal;
    private Fisica fisica;
    private Juridica juridica;
    private Recepcao recepcao;
    private Registro registro;
    private Profissao profissao;
    private int idMotivoDemissao;
    private String tipoPesquisa;
    private String dataPesquisa;
    private Cancelamento cancelamento;
    private int id_protocolo;
    private String numeroProtocolo;
    private List<ListaAgendamento> listaRecepcaos;
    private List<Senha> listaAtendimentoSimples;
    private String dataInicialString;
    private String dataFinalString;
    private boolean openDialog;
    private List<SelectItem> listaStatus;
    private List<SelectItem> listaMotivoDemissao;
    private int progressUpdate;
    private int progressLabel;
    private boolean startPooling;

    private int idStatusAtendimento;
    private List<SelectItem> listaStatusAtendimento;
    private String dataPesquisaAtendimento;
    private String dataInicialAtendimento;
    private String dataFinalAtendimento;
    private int indexTab = 0;
    private String descricaoFisica;
    private String tipoPesquisaAtendimento;
    private String tipoFisicaPesquisa;
    private List listFiles;

    /*    @PostConstruct
     public void init() {
     agendamento = new Agendamento();
     agendamentoEdit = new Agendamento();
     idStatus = 0;
     ocultaData = true;
     ocultaSenha = false;
     ocultaStatus = true;
     ocultaPreposto = false;
     ocultaHomologador = false;
     ocultaDatapesquisa = false;
     ocultaColunaEmpresa = false;
     ocultaColunaPessoaFisica = false;
     isPesquisarPessoaFisicaFiltro = false;
     isPesquisarPessoaJuridicaFiltro = false;
     desabilitaAtualizacaoAutomatica = false;
     desabilitaPesquisaProtocolo = false;
     dataPesquisaTodas = false;
     isCaso = 0;
     macFilial = null;
     strData = DataHoje.data();
     strDataFinal = DataHoje.data();
     strEndereco = "";
     msgRecepcao = "";
     msgConfirma = "";
     statusEmpresa = "";
     data = DataHoje.dataHoje();
     dataInicial = DataHoje.dataHoje();
     dataFinal = DataHoje.dataHoje();
     fisica = new Fisica();
     juridica = new Juridica();
     recepcao = new Recepcao();
     registro = new Registro();
     profissao = new Profissao();
     idMotivoDemissao = 0;
     tipoPesquisa = "";
     dataPesquisa = "hoje";
     cancelamento = new Cancelamento();
     id_protocolo = -1;
     numeroProtocolo = "";
     listaRecepcaos = new ArrayList();
     macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
     dataInicialString = DataHoje.data();
     dataFinalString = DataHoje.data();
     openDialog = false;
     listaAtendimentoSimples = new ArrayList();
     listaStatus = new ArrayList();
     listaMotivoDemissao = new ArrayList();
     listaHorarios = new ArrayList();
     progressUpdate = 100;
     progressLabel = 10;
     startPooling = true;
        
     idStatusAtendimento = 0;
     listaStatus = new ArrayList();
     dataPesquisaAtendimento = "hoje";
     listaStatusAtendimento = new ArrayList();
     tipoPesquisaAtendimento = "juridica";
        
     getListaStatusAtendimento();
     loadListHorarios();
     loadListaAtendimentoSimples();
        
     dataInicialAtendimento = DataHoje.data();
     dataFinalAtendimento = DataHoje.data();
        
     descricaoFisica = "";
     tipoFisicaPesquisa = "";
     }

     @PreDestroy
     public void destroy() {
     GenericaSessao.remove("recepcaoBean");
     GenericaSessao.remove("juridicaPesquisa");
     GenericaSessao.remove("fisicaPesquisa");
     }
     */
    public RecepcaoBean() {
        agendamento = new Agendamento();
        agendamentoEdit = new Agendamento();
        idStatus = 0;
        ocultaData = true;
        ocultaSenha = false;
        ocultaStatus = true;
        ocultaPreposto = false;
        ocultaHomologador = false;
        ocultaDatapesquisa = false;
        ocultaColunaEmpresa = false;
        ocultaColunaPessoaFisica = false;
        isPesquisarPessoaFisicaFiltro = false;
        isPesquisarPessoaJuridicaFiltro = false;
        desabilitaAtualizacaoAutomatica = false;
        desabilitaPesquisaProtocolo = false;
        dataPesquisaTodas = false;
        isCaso = 0;
        macFilial = null;
        strData = DataHoje.data();
        strDataFinal = DataHoje.data();
        strEndereco = "";
        msgRecepcao = "";
        msgConfirma = "";
        statusEmpresa = "";
        data = DataHoje.dataHoje();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        fisica = new Fisica();
        juridica = new Juridica();
        recepcao = new Recepcao();
        registro = new Registro();
        profissao = new Profissao();
        idMotivoDemissao = 0;
        tipoPesquisa = "";
        dataPesquisa = "hoje";
        cancelamento = new Cancelamento();
        id_protocolo = -1;
        numeroProtocolo = "";
        listaRecepcaos = new ArrayList();
        macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
        dataInicialString = DataHoje.data();
        dataFinalString = DataHoje.data();
        openDialog = false;
        listaAtendimentoSimples = new ArrayList();
        listaStatus = new ArrayList();
        listaMotivoDemissao = new ArrayList();
        listaHorarios = new ArrayList();
        progressUpdate = 100;
        progressLabel = 10;
        startPooling = true;

        idStatusAtendimento = 0;
        listaStatus = new ArrayList();
        dataPesquisaAtendimento = "hoje";
        listaStatusAtendimento = new ArrayList();
        tipoPesquisaAtendimento = "juridica";

        descricaoFisica = "";
        tipoFisicaPesquisa = "cpf";

        dataInicialAtendimento = DataHoje.data();
        dataFinalAtendimento = DataHoje.data();

        listFiles = new ArrayList();

        getListaStatusAtendimento();
        loadListHorarios();
        loadListaAtendimentoSimples();
    }

    public void alterTab(TabChangeEvent event) {
        indexTab = ((TabView) event.getComponent()).getActiveIndex();
    }

    public void startStopPolling() {
//        if (startPooling)
//            setStartPooling(false);
//        else
//            setStartPooling(true);
    }

    public void progress() {
        progressUpdate = progressUpdate - 10;
        progressLabel--;
        if (progressUpdate == 0) {
            progressUpdate = 100;
            progressLabel = 10;
            loadListHorarios();
            //PF.update(":formRecepcao:i_tabview:i_status");
            //PF.update(":formRecepcao:i_tabview:i_panel_tbl");
            PF.update("formRecepcao:i_tabview:i_panel_tbl");
//            try {
//                FacesContext.getCurrentInstance().getExternalContext().redirect("");
//            } catch (IOException ex) {
//                Logger.getLogger(RecepcaoBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    public void fecharModal() {
        agendamentoEdit = new Agendamento();
        openDialog = false;
        recepcao = new Recepcao();
        listFiles.clear();
    }

    public void gerarSenha() {
        Dao di = new Dao();
        di.openTransaction();

        if (registro.isSenhaHomologacao()) {
            if (recepcao.getHoraInicialFuncionario().isEmpty()) {
                GenericaMensagem.warn("Atenção", "FUNCIONÁRIO ainda não esta presente, aguarde sua chegada!");
                di.rollback();
                return;
            }

            if (recepcao.getPreposto().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Informar o NOME do preposto!");
                di.rollback();
                return;
            }

            if (recepcao.getHoraInicialPreposto().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Informar o HORÁRIO que o preposto chegou!");
                di.rollback();
                return;
            }

            if (recepcao.getId() == -1) {
                if (!di.save(recepcao)) {
                    GenericaMensagem.error("Erro", "Não foi possível SALVAR Recepção!");
                    di.rollback();
                    return;
                }
            } else {
                if (!di.update(recepcao)) {
                    GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Recepção!");
                    di.rollback();
                    return;
                }
            }
            agendamentoEdit.setRecepcao(recepcao);
        }

        if (!di.update(agendamentoEdit)) {
            GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Agendamento!");
            di.rollback();
            return;
        }

        SenhaHomologacao senhaHomologacao = new SenhaHomologacao();
        Collection<ParametroSenha> list = senhaHomologacao.parametros(agendamentoEdit, di);
        if (!list.isEmpty()) {
            GenericaMensagem.info("Sucesso", "Senha Gerada!");
            //senhaHomologacao.imprimir(agendamentoEdit, list);
            senhaHomologacao.imprimir(list);
        } else {
            di.rollback();
            GenericaMensagem.error("Erro", "Não foi possível GERAR SENHA!");
            return;
        }
        di.commit();
        loadListHorarios();
    }

    public void cancelarHorario() {
        if (cancelamento.getMotivo().isEmpty() || cancelamento.getMotivo().length() <= 5) {
            GenericaMensagem.warn("Atenção", "Motivo de Cancelamento inválido");
            return;
        }

        DaoInterface di = new Dao();
        agendamentoEdit.setStatus((Status) di.find(new Status(), 3));
        di.openTransaction();
        if (!di.update(agendamentoEdit)) {
            di.rollback();
            GenericaMensagem.error("Erro", "Erro ao atualizar Agendamento");
            return;
        }
        agendamentoEdit.getPessoaEmpresa().setDtDemissao(null);

        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        PessoaEmpresa pem = db.pesquisaPessoaEmpresaPorFisica(agendamentoEdit.getPessoaEmpresa().getFisica().getId());

        if (pem.getId() == -1) {
            agendamentoEdit.getPessoaEmpresa().setPrincipal(true);
        }

        if (!di.update(agendamentoEdit.getPessoaEmpresa())) {
            di.rollback();
            GenericaMensagem.error("Erro", "Erro ao atualizar Pessoa Empresa");
            return;
        }

        cancelamento.setAgendamento(agendamentoEdit);
        cancelamento.setDtData(DataHoje.dataHoje());
        cancelamento.setUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")));

        if (!di.save(cancelamento)) {
            di.rollback();
            GenericaMensagem.error("Erro", "Erro ao salvar Cancelamento");
            return;
        }

        GenericaMensagem.info("Sucesso", "Homologação Cancelada!");
        di.commit();
        //cancelamento = new Cancelamento();
        loadListHorarios();
    }

    public String pesquisarPessoa() {
        if (tipoPesquisa.equals("juridica")) {
            return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pesquisaPessoaJuridica();
        } else {
            return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pesquisaPessoaFisica();
        }
    }

    public String pesquisarPessoaAtendimento() {
        if (tipoPesquisaAtendimento.equals("juridica")) {
            return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pesquisaPessoaJuridica();
        } else {
            return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pesquisaPessoaFisica();
        }
    }

    public String limparPesquisa() {
        juridica = new Juridica();
        fisica = new Fisica();
        isPesquisarPessoaFisicaFiltro = false;
        isPesquisarPessoaJuridicaFiltro = false;
        desabilitaPesquisaProtocolo = false;
        if (indexTab == 0) {
            loadListHorarios();
        } else {
            loadListaAtendimentoSimples();
        }
        return null;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            List<Status> list = (List<Status>) sadb.pesquisaObjeto(new int[]{2, 3, 4, 5, 7}, "Status");
            if (!list.isEmpty()) {
                int i = 0;
                for (i = 0; i < list.size(); i++) {
                    listaStatus.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                }
                listaStatus.add(new SelectItem(i++, "Todos", "6"));
            }
        }
        return listaStatus;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        if (listaMotivoDemissao.isEmpty()) {
            DaoInterface di = new Dao();
            List<Demissao> list = (List<Demissao>) di.list(new Demissao(), true);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    listaMotivoDemissao.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                }
            }
        }
        return listaMotivoDemissao;
    }

    public void salvar() {
        Dao di = new Dao();
        di.openTransaction();
        if (registro.isSenhaHomologacao()) {
            if (!recepcao.getPreposto().isEmpty()) {
                if (recepcao.getHoraInicialPreposto().isEmpty()) {
                    GenericaMensagem.warn("Atenção", "Informar o HORÁRIO que o preposto chegou!");
                    di.rollback();
                    return;
                }
            }

            //if (!recepcao.getHoraInicialFuncionario().isEmpty()) {
            if (recepcao.getId() == -1) {
                if (!di.save(recepcao)) {
                    GenericaMensagem.error("Erro", "Erro ao salvar Recepção!");
                    di.rollback();
                    return;
                }
            } else {
                if (!di.update(recepcao)) {
                    GenericaMensagem.error("Erro", "Erro ao atualizar Recepção!");
                    di.rollback();
                    return;
                }
            }
            agendamentoEdit.setRecepcao(recepcao);
            //}
        }

        if (!di.update(agendamentoEdit)) {
            GenericaMensagem.error("Erro", "Erro ao atualizar Agendamento!");
            di.rollback();
            return;
        } else {
            //agendamento.setRecepcao(null);
        }
        GenericaMensagem.info("Sucesso", "Agendamento atualizado!");
        di.commit();
        loadListHorarios();
    }

    public void agendar(DataObject datao) {
        if (getData() != null) {
            if (DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))
                    < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                return;
            }
        }
        Dao di = new Dao();
        int idCaso = Integer.parseInt(datao.getArgumento12().toString());
        switch (idCaso) {
            case 2:
            case 3:
            case 4:
            case 7: {
                agendamentoEdit = (Agendamento) di.find(datao.getArgumento9());
                profissao = ((PessoaEmpresa) datao.getArgumento7()).getFuncao();
                for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                    if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamentoEdit.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                        break;
                    }
                }
                break;
            }
            case 5: {
                agendamentoEdit = (Agendamento) di.find(datao.getArgumento9());
                if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == agendamentoEdit.getHomologador().getId()) {
                    profissao = ((PessoaEmpresa) datao.getArgumento7()).getFuncao();
                    for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                        if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamentoEdit.getDemissao().getId()) {
                            idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                            break;
                        }
                    }
                    break;
                } else {
                    agendamentoEdit = new Agendamento();
                    break;
                }
            }
        }

        HomologacaoDB dB = new HomologacaoDBToplink();
        cancelamento = (Cancelamento) dB.pesquisaCancelamentoPorAgendanto(agendamentoEdit.getId());
        if (cancelamento == null) {
            cancelamento = new Cancelamento();
        }

        if (agendamentoEdit.getRecepcao() != null) {
            recepcao = agendamentoEdit.getRecepcao();
        }

        getStrEndereco();
        openDialog = true;
    }

    public List<DataObject> getListaHorarios() {
        return listaHorarios;
    }

    public String voltar() {
        if (!isPesquisarPessoaFisicaFiltro && !isPesquisarPessoaJuridicaFiltro) {
            juridica = new Juridica();
            fisica = new Fisica();
        }
        agendamento = new Agendamento();
        profissao = new Profissao();
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("fisicaPesquisa");
        return "recepcao";
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdMotivoDemissao() {
        return idMotivoDemissao;
    }

    public void setIdMotivoDemissao(int idMotivoDemissao) {
        this.idMotivoDemissao = idMotivoDemissao;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getMsgRecepcao() {
        if (!GenericaSessao.exists("acessoFilial")) {
            msgRecepcao = "Não existe filial definida!";
        } else {
            macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
        }
        return msgRecepcao;
    }

    public void setMsgRecepcao(String msgRecepcao) {
        this.msgRecepcao = msgRecepcao;
    }

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            idStatus = 5;
            numeroProtocolo = "";
            isPesquisarPessoaJuridicaFiltro = true;
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            GenericaSessao.remove("fisicaPesquisa");
            GenericaSessao.remove("juridicaPesquisa");
            dataPesquisaTodas = true;
            if (indexTab == 0) {
                loadListHorarios();
            } else {
                loadListaAtendimentoSimples();
            }
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public String getStatusEmpresa() {
        List lista = new ArrayList();
        if (juridica.getId() != -1) {
            HomologacaoDB db = new HomologacaoDBToplink();
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

    public String getStrEndereco() {
        if (agendamentoEdit.getPessoaEmpresa().getJuridica().getId() != -1) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            PessoaEndereco enderecoEmpresa = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(agendamentoEdit.getPessoaEmpresa().getJuridica().getPessoa().getId(), 2);
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
            strEndereco = "";
        }
        return strEndereco;
    }

    public void setStrEndereco(String strEndereco) {
        this.strEndereco = strEndereco;
    }

    public Fisica getFisica() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            isPesquisarPessoaFisicaFiltro = true;
            numeroProtocolo = "";
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
            GenericaSessao.remove("juridicaPesquisa");
            dataPesquisaTodas = true;
            idStatus = 5;
            if (indexTab == 0) {
                loadListHorarios();
            } else {
                loadListaAtendimentoSimples();
            }
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

//    public Recepcao getRecepcao() {
//        return recepcao;
//    }
//
//    public void setRecepcao(Recepcao recepcao) {
//        this.recepcao = recepcao;
//    }
    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public Cancelamento getCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(Cancelamento cancelamento) {
        this.cancelamento = cancelamento;
    }

    public Registro getRegistro() {
        if (registro == null) {
            registro = new Registro();
        }
        if (registro.getId() == -1) {
            DaoInterface di = new Dao();
            registro = (Registro) di.find(new Registro(), 1);
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public boolean isOcultaData() {
        return ocultaData;
    }

    public void setOcultaData(boolean ocultaData) {
        this.ocultaData = ocultaData;
    }

    public boolean isOcultaStatus() {
        return ocultaStatus;
    }

    public void setOcultaStatus(boolean ocultaStatus) {
        this.ocultaStatus = ocultaStatus;
    }

    public int getId_protocolo() {
        return id_protocolo;
    }

    public void setId_protocolo(int id_protocolo) {
        this.id_protocolo = id_protocolo;
    }

    public String getDataPesquisa() {
        if (dataPesquisa.equals("hoje")) {
            if (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription()) != 6) {
                data = DataHoje.dataHoje();
            }
            ocultaDatapesquisa = false;
        } else {
            ocultaDatapesquisa = true;
        }
        return dataPesquisa;
    }

    public void setDataPesquisa(String dataPesquisa) {
        this.dataPesquisa = dataPesquisa;
    }

    public String getStrDataFinal() {
        return strDataFinal;
    }

    public void setStrDataFinal(String strDataFinal) {
        this.strDataFinal = strDataFinal;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataInicialString() {
        return dataInicialString;
    }

    public void setDataInicialString(String dataInicialString) {
        this.dataInicialString = dataInicialString;
        this.dataInicial = DataHoje.converte(dataInicialString);
    }

    public String getDataFinalString() {
        return dataFinalString;
    }

    public void setDataFinalString(String dataFinalString) {
        this.dataFinalString = dataFinalString;
        this.dataFinal = DataHoje.converte(dataFinalString);
    }

    public void setData(String dataFinal) {
        this.dataFinal = DataHoje.converte(dataFinal);
    }

    public boolean isOcultaDatapesquisa() {
        return ocultaDatapesquisa;
    }

    public void setOcultaDatapesquisa(boolean ocultaDatapesquisa) {
        this.ocultaDatapesquisa = ocultaDatapesquisa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public int getIsCaso() {
        return isCaso;
    }

    public void setIsCaso(int isCaso) {
        this.isCaso = isCaso;
    }

    public boolean isOcultaPreposto() {
        return ocultaPreposto;
    }

    public void setOcultaPreposto(boolean ocultaPreposto) {
        this.ocultaPreposto = ocultaPreposto;
    }

    public boolean isOcultaHomologador() {
        return ocultaHomologador;
    }

    public void setOcultaHomologador(boolean ocultaHomologador) {
        this.ocultaHomologador = ocultaHomologador;
    }

    public boolean isOcultaColunaEmpresa() {
        return ocultaColunaEmpresa;
    }

    public void setOcultaColunaEmpresa(boolean ocultaColunaEmpresa) {
        this.ocultaColunaEmpresa = ocultaColunaEmpresa;
    }

    public boolean isOcultaColunaPessoaFisica() {
        return ocultaColunaPessoaFisica;
    }

    public void setOcultaColunaPessoaFisica(boolean ocultaColunaPessoaFisica) {
        this.ocultaColunaPessoaFisica = ocultaColunaPessoaFisica;
    }

    public boolean isOcultaSenha() {
        return ocultaSenha;
    }

    public void setOcultaSenha(boolean ocultaSenha) {
        this.ocultaSenha = ocultaSenha;
    }

    public boolean isIsPesquisarPessoaFisicaFiltro() {
        return isPesquisarPessoaFisicaFiltro;
    }

    public void setIsPesquisarPessoaFisicaFiltro(boolean isPesquisarPessoaFisicaFiltro) {
        this.isPesquisarPessoaFisicaFiltro = isPesquisarPessoaFisicaFiltro;
    }

    public boolean isIsPesquisarPessoaJuridicaFiltro() {
        return isPesquisarPessoaJuridicaFiltro;
    }

    public void setIsPesquisarPessoaJuridicaFiltro(boolean isPesquisarPessoaJuridicaFiltro) {
        this.isPesquisarPessoaJuridicaFiltro = isPesquisarPessoaJuridicaFiltro;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public boolean isDesabilitaAtualizacaoAutomatica() {
        return desabilitaAtualizacaoAutomatica;
    }

    public void setDesabilitaAtualizacaoAutomatica(boolean desabilitaAtualizacaoAutomatica) {
        this.desabilitaAtualizacaoAutomatica = desabilitaAtualizacaoAutomatica;
    }

    public void limparPesquisaProtocolo() {
        numeroProtocolo = "";
        //listaRecepcaos.clear();
        //getListaRecepcaos();
    }

    public boolean isDesabilitaPesquisaProtocolo() {
        return desabilitaPesquisaProtocolo;
    }

    public void setDesabilitaPesquisaProtocolo(boolean desabilitaPesquisaProtocolo) {
        this.desabilitaPesquisaProtocolo = desabilitaPesquisaProtocolo;
    }

    public boolean isDataPesquisaTodas() {
        return dataPesquisaTodas;
    }

    public void setDataPesquisaTodas(boolean dataPesquisaTodas) {
        this.dataPesquisaTodas = dataPesquisaTodas;
    }

    public void setListaRecepcaos(List<ListaAgendamento> listaRecepcaos) {
        this.listaRecepcaos = listaRecepcaos;
    }

    public void selecionaDataInicial(SelectEvent selectEvent) {
        ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        setDataInicial(event.getStartDate());
    }

    public void selecionaDataFinal(SelectEvent selectEvent) {
        ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        setDataFinal(event.getStartDate());
    }

    public void loadListHorarios() {
        listaHorarios.clear();

        if (macFilial == null) {
            return;
        }
        if (isPesquisarPessoaFisicaFiltro == true) {
            juridica = new Juridica();
        }
        if (isPesquisarPessoaJuridicaFiltro == true) {
            fisica = new Fisica();
        }

        if (dataPesquisa.equals("hoje")) {
            dataInicial = DataHoje.dataHoje();
            dataInicialString = DataHoje.data();
        }

        if (juridica.getId() != -1 || fisica.getId() != -1) {
            desabilitaPesquisaProtocolo = true;
            numeroProtocolo = "";
        } else {
            desabilitaPesquisaProtocolo = false;
        }

        HomologacaoDB db = new HomologacaoDBToplink();
        List<Agendamento> ag;
        setData(DataHoje.dataHoje());
        String agendador;
        String homologador;
        String contabilidade;
        ocultaData = false;
        ocultaHomologador = false;
        ocultaPreposto = false;
        ocultaStatus = false;
        ocultaColunaPessoaFisica = false;
        ocultaColunaEmpresa = false;
        ocultaSenha = false;
        int idCaso = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        int idCasoStatus;
        Date dataInicialA;
        Date dataFinalA;
        desabilitaAtualizacaoAutomatica = false;
        if (!numeroProtocolo.equals("")) {
            if (Integer.parseInt(numeroProtocolo) > 0) {
                ag = db.pesquisaAgendamentoPorProtocolo(Integer.parseInt(numeroProtocolo));
                if (!ag.isEmpty()) {
                    desabilitaAtualizacaoAutomatica = true;
                }
            } else {
                ag = new ArrayList();
            }
        } else {
            ag = new ArrayList();
        }
        if (idCaso > 0 && ag.isEmpty()) {
            if (idCaso == 6) {
                idCasoStatus = 0;
                ocultaStatus = false;
                if (dataPesquisa.equals("hoje")) {
                    ocultaData = true;
                    dataInicial = DataHoje.dataHoje();
                    dataFinal = DataHoje.dataHoje();
                    strData = getDataInicialString();
                    if (fisica.getId() != -1 || juridica.getId() != -1) {
                        dataInicialA = dataInicial;
                        dataFinalA = null;
                    } else {
                        dataInicialA = dataInicial;
                        dataFinalA = null;
                    }
                } else if (dataPesquisa.equals("periodo") && !tipoPesquisa.equals("todos") && juridica.getId() == -1 && fisica.getId() == -1) {
                    ocultaData = false;
                    dataInicialA = dataInicial;
                    dataFinalA = dataFinal;
                } else {
                    ocultaData = false;
                    if (fisica.getId() != -1 || juridica.getId() != -1) {
                        dataInicialA = dataInicial;
                        dataFinalA = dataFinal;
                    } else {
                        dataInicialA = dataInicial;
                        dataFinalA = null;
                    }
                }
            } else {
                idCasoStatus = idCaso;
                ocultaStatus = true;
                if (dataPesquisa.equals("hoje")) {
                    ocultaData = true;
                    dataInicial = DataHoje.dataHoje();
                    dataFinal = DataHoje.dataHoje();
                    strData = getDataInicialString();
                    dataInicialA = dataInicial;
                    dataFinalA = null;
                } else {
                    ocultaData = false;
                    dataInicialA = dataInicial;
                    dataFinalA = dataFinal;
                }
            }

            if (dataPesquisaTodas) {
                if (juridica.getId() != -1 || fisica.getId() != -1) {
                    dataInicialA = null;
                    dataFinalA = null;
                }
            }

            if (numeroProtocolo.equals("")) {
                ag = db.pesquisaAgendamento(idCasoStatus, macFilial.getFilial().getId(), dataInicialA, dataFinalA, 0, fisica.getId(), juridica.getId(), true, false);
            } else {
                ag = new ArrayList();
            }

            if (idCaso == 7) {
                this.ocultaPreposto = true;
                this.ocultaHomologador = true;
                this.ocultaSenha = true;
            }

            if (juridica.getId() != -1) {
                ocultaColunaEmpresa = true;
            }

            if (fisica.getId() != -1) {
                ocultaColunaPessoaFisica = true;
            }

        }

        for (int i = 0; i < ag.size(); i++) {
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
            if (ag.get(i).getPessoaEmpresa().getJuridica().getContabilidade() != null) {
                contabilidade = ag.get(i).getPessoaEmpresa().getJuridica().getContabilidade().getPessoa().getNome();
            } else {
                contabilidade = "";
            }
            Senha senha = db.pesquisaSenhaAgendamento(ag.get(i).getId());
            String senhaId = "";
            String senhaString = "";
            boolean isOposicao = false;
            AtendimentoDB dbat = new AtendimentoDBTopLink();
            if (dbat.pessoaOposicao(ag.get(i).getPessoaEmpresa().getFisica().getPessoa().getDocumento())) {
                isOposicao = true;
            }

            if (senha != null && senha.getId() != -1) {
                senhaString = ((Integer) senha.getSenha()).toString();
                senhaString = (senhaString.length() == 1) ? "0" + senhaString : senhaString;
            }

            if (!isOposicao) {
                Cancelamento can = (Cancelamento) db.pesquisaCancelamentoPorAgendanto(ag.get(i).getId());
                if (senha != null && senha.getId() != -1 && can == null) {
                    senhaId = "tblListaRecepcaox";
                }
            } else {
                senhaId = "tblAgendamentoOposicaox";

            }

            listaHorarios.add(new DataObject(
                    ag.get(i).getHorarios(), // ARG 0 HORA
                    ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                    ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                    homologador, //ARG 3 HOMOLOGADOR
                    ag.get(i).getContato(), // ARG 4 CONTATO
                    ag.get(i).getTelefone(), // ARG 5 TELEFONE
                    agendador, // ARG 6 USUARIO
                    ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                    senhaId, // senha.getId() == -1 ? "" : "tblListaRecepcao", // ARG 8 SE TIVER SENHA COR VERDE
                    ag.get(i), // ARG 9 AGENDAMENTO
                    senhaString, // senha.getId() == -1 ? null : senha.getSenha(), // ARG 10 SENHA PARA ATENDIMENTO
                    ag.get(i).getData(), // ARG 11 DATA DO AGENDAMENTO
                    ag.get(i).getStatus().getId(), // ARG 12 STATUS ID
                    ag.get(i).getStatus().getDescricao(), // ARG 13 STATUS DESCRIÇÃO
                    contabilidade, //ARG 14 CONTABILIDADE
                    null)
            );
        }
    }

    public void loadListaAtendimentoSimples() {
        listaAtendimentoSimples.clear();
        if (macFilial != null) {
            HomologacaoDB db = new HomologacaoDBToplink();
            SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();

            listaAtendimentoSimples = db.listaAtendimentoIniciadoSimplesPesquisa(
                    macFilial.getFilial().getId(),
                    su.getSessaoUsuario().getId(),
                    Integer.valueOf(listaStatusAtendimento.get(idStatusAtendimento).getDescription()),
                    dataPesquisaAtendimento,
                    dataInicialAtendimento,
                    dataFinalAtendimento,
                    juridica.getId(),
                    descricaoFisica,
                    (tipoPesquisaAtendimento.equals("fisica")) ? tipoFisicaPesquisa : ""
            );
        }
    }

    public void clearDescricaoFisica() {
        descricaoFisica = "";
        loadListaAtendimentoSimples();
    }

    public void cleanTipoFisica() {
        getTipoFisicaPesquisa();
        loadListaAtendimentoSimples();
    }

    public boolean renderedInput(String tipo) {
        return (tipoFisicaPesquisa.equals(tipo));
    }

    public String estiloLinha(AteMovimento atm) {
        if (atm.getStatus().getId() == 1) {
            return "tblListaRecepcaox";
        } else {
            return "";
        }
    }

    public List<Senha> getListaAtendimentoSimples() {
        return listaAtendimentoSimples;
    }

    public void setListaAtendimentoSimples(List<Senha> listaAtendimentoSimples) {
        this.listaAtendimentoSimples = listaAtendimentoSimples;
    }

    public Agendamento getAgendamentoEdit() {
        return agendamentoEdit;
    }

    public void setAgendamentoEdit(Agendamento agendamentoEdit) {
        this.agendamentoEdit = agendamentoEdit;
    }

    public boolean isOpenDialog() {
        return openDialog;
    }

    public void setOpenDialog(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public Recepcao getRecepcao() {
        return recepcao;
    }

    public void setRecepcao(Recepcao recepcao) {
        this.recepcao = recepcao;
    }

    public int getProgressUpdate() {
        return progressUpdate;
    }

    public void setProgressUpdate(int progressUpdate) {
        this.progressUpdate = progressUpdate;
    }

    public int getProgressLabel() {
        return progressLabel;
    }

    public void setProgressLabel(int progressLabel) {
        this.progressLabel = progressLabel;
    }

    public boolean isStartPooling() {
        return startPooling;
    }

    public void setStartPooling(boolean startPooling) {
        this.startPooling = startPooling;
    }

    public int getIdStatusAtendimento() {
        return idStatusAtendimento;
    }

    public void setIdStatusAtendimento(int idStatusAtendimento) {
        this.idStatusAtendimento = idStatusAtendimento;
    }

    public List<SelectItem> getListaStatusAtendimento() {
        if (listaStatusAtendimento.isEmpty()) {
            List<AteStatus> result = new Dao().list("AteStatus");
            listaStatusAtendimento.add(new SelectItem(0, "Todos", "0"));
            for (int i = 0; i < result.size(); i++) {
                listaStatusAtendimento.add(new SelectItem(i + 1, result.get(i).getDescricao(), Integer.toString(result.get(i).getId())));
            }
        }
        return listaStatusAtendimento;
    }

    public void setListaStatusAtendimento(List<SelectItem> listaStatusAtendimento) {
        this.listaStatusAtendimento = listaStatusAtendimento;
    }

    public String getDataPesquisaAtendimento() {
        return dataPesquisaAtendimento;
    }

    public void setDataPesquisaAtendimento(String dataPesquisaAtendimento) {
        this.dataPesquisaAtendimento = dataPesquisaAtendimento;
    }

    public String getDataInicialAtendimento() {
        return dataInicialAtendimento;
    }

    public void setDataInicialAtendimento(String dataInicialAtendimento) {
        this.dataInicialAtendimento = dataInicialAtendimento;
    }

    public String getDataFinalAtendimento() {
        return dataFinalAtendimento;
    }

    public void setDataFinalAtendimento(String dataFinalAtendimento) {
        this.dataFinalAtendimento = dataFinalAtendimento;
    }

    public int getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(int indexTab) {
        this.indexTab = indexTab;
    }

    public String getDescricaoFisica() {
        return descricaoFisica;
    }

    public void setDescricaoFisica(String descricaoFisica) {
        this.descricaoFisica = descricaoFisica;
    }

    public String getTipoPesquisaAtendimento() {
        return tipoPesquisaAtendimento;
    }

    public void setTipoPesquisaAtendimento(String tipoPesquisaAtendimento) {
        this.tipoPesquisaAtendimento = tipoPesquisaAtendimento;
    }

    public String getTipoFisicaPesquisa() {
        return tipoFisicaPesquisa;
    }

    public void setTipoFisicaPesquisa(String tipoFisicaPesquisa) {
        this.tipoFisicaPesquisa = tipoFisicaPesquisa;
    }

    // ARQUIVOS
    public List getListFiles() {
        listFiles.clear();
        if (agendamentoEdit.getId() != -1) {
            listFiles = Diretorio.listaArquivos("Arquivos/homologacao/" + agendamentoEdit.getId());
        }
        return listFiles;
    }

    public void upload(FileUploadEvent event) {
        ConfiguracaoUpload configuracaoUpload = new ConfiguracaoUpload();
        configuracaoUpload.setArquivo(event.getFile().getFileName());
        configuracaoUpload.setDiretorio("Arquivos/homologacao/" + agendamentoEdit.getId());
        configuracaoUpload.setEvent(event);
        if (Upload.enviar(configuracaoUpload, true)) {
            listFiles.clear();
        }
        getListFiles();
    }

    public void deleteFiles(int index) {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/homologacao/" + agendamentoEdit.getId() + "/" + (String) ((DataObject) listFiles.get(index)).getArgumento1());
        File fl = new File(caminho);
        fl.delete();
        listFiles.remove(index);
        listFiles.clear();
        getListFiles();
        PF.update("formConcluirHomolocagao:id_grid_uploads");
        PF.update("formConcluirHomolocagao:id_btn_anexo");
        PF.update("form_recepcao_upload:id_grid_uploads");
        PF.update("formRecepcao:id_btn_anexo");
    }
}

//    public void gerarSenha() {
//        // parei aqui.. testar o gerar senha e alterar a variavel recepcao para agendamentoEdit.recepcao
//        DB db = new DB();
//        EntityManager em = db.getEntityManager();
//        em.getTransaction().begin();
//        if (registro.isSenhaHomologacao()) {
//            if (!recepcao.getPreposto().isEmpty()) {
//                //msgConfirma = "Informar o nome do preposto!";
////                GenericaMensagem.warn("Atenção", "Informar o NOME DO PREPOSTO!");
////                return;
//                if (recepcao.getHoraInicialPreposto().isEmpty()) {
//                    GenericaMensagem.warn("Atenção", "Informar o HORÁRIO que o preposto chegou!");
//                    em.getTransaction().rollback();
//                    return;
//                }
//            }
//
////            if (agendamentoEdit.getRecepcao().getHoraInicialPreposto().isEmpty()) {
////                //msgConfirma = "Preposto ainda não esta presente, aguarde sua chegada!";
////                GenericaMensagem.warn("Atenção", "PREPOSTO ainda não esta presente, aguarde sua chegada!");
////                return;
////            }
//            if (recepcao.getHoraInicialFuncionario().isEmpty()) {
//                //msgConfirma = "Funcionário ainda não esta presente, aguarde sua chegada!";
//                GenericaMensagem.warn("Atenção", "FUNCIONÁRIO ainda não esta presente, aguarde sua chegada!");
//                em.getTransaction().rollback();
//                return;
//            }
//
//            boolean sucesso = true;
//            if (recepcao.getId() == -1) {
//                try {
//                    em.persist(recepcao);
//                    em.flush();
//                } catch (Exception e) {
//                    //msgConfirma = "Erro ao atualizar recepção!";
//                    sucesso = false;
//                    GenericaMensagem.error("Erro", "Não foi possível SALVAR Recepção!");
//                    em.getTransaction().rollback();
//                    return;
//                }
//            } else {
//                try {
//                    em.merge(recepcao);
//                    em.flush();
//                } catch (Exception e) {
//                    //msgConfirma = "Erro ao atualizar recepção!";
//                    sucesso = false;
//                    GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Recepção!");
//                    em.getTransaction().rollback();
//                    return;
//                }
//            }
////            if (recepcao == null || recepcao.getId() == -1) {
////                //msgConfirma = "Agendamento ainda não possui cadastro de preposto!";
////                GenericaMensagem.warn("Atenção", "Agendamento ainda não possui cadastro de preposto!");
////                return;
////            }
//        }
//
////        if (recepcao.getId() == -1)
////            agendamentoEdit.setRecepcao(null);
//        agendamentoEdit.setRecepcao(recepcao);
//        if (recepcao.getId() == -1) {
//            try {
//                em.merge(agendamentoEdit);
//                em.flush();
//            } catch (Exception e) {
//                //msgConfirma = "Erro ao atualizar agendamento!";
//                GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Agendamento!");
//                em.getTransaction().rollback();
//                return;
//            }
//        }
//
//        SenhaHomologacao senhaHomologacao = new SenhaHomologacao();
//        Collection lista = new ArrayList<ParametroSenha>();
//        HomologacaoDB hdb = new HomologacaoDBToplink();
//        Senha senha = hdb.pesquisaSenhaAgendamento(agendamentoEdit.getId());
//        MacFilial mc = MacFilial.getAcessoFilial();
//        if (senha.getId() == -1) {
//            senha.setAgendamento(agendamentoEdit);
//            senha.setDtData(DataHoje.dataHoje());
//            senha.setHora(DataHoje.horaMinuto());
//            senha.setUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")));
//            senha.setFilial(mc.getFilial());
//            senha.setSenha(hdb.pesquisaUltimaSenha(mc.getFilial().getId()) + 1);
//            try {
//                em.persist(senha);
//                em.flush();
//            } catch (Exception e) {
//                //msgConfirma = "Erro ao atualizar agendamento!";
//                GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Agendamento!");
//                em.getTransaction().rollback();
//                return;
//            }
//        } else {
//            try {
//                em.merge(senha);
//                em.flush();
//            } catch (Exception e) {
//                //msgConfirma = "Erro ao atualizar recepção!";
//                GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Recepção!");
//                em.getTransaction().rollback();
//                return;
//            }
//        }
//        try {
//            if (senha.getId() != -1) {
//                lista.add(new ParametroSenha(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
//                        senha.getFilial().getFilial().getPessoa().getNome(),
//                        senha.getFilial().getFilial().getPessoa().getDocumento(),
//                        senha.getAgendamento().getPessoaEmpresa().getJuridica().getPessoa().getNome(),
//                        senha.getAgendamento().getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
//                        (senha.getAgendamento().getRecepcao() == null) ? "" : senha.getAgendamento().getRecepcao().getPreposto(),
//                        senha.getAgendamento().getPessoaEmpresa().getFisica().getPessoa().getNome(),
//                        senha.getUsuario().getPessoa().getNome(),
//                        senha.getData(),
//                        senha.getHora(),
//                        String.valueOf(senha.getSenha())));
//            }
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            return;
//        }
//        
//        em.getTransaction().commit();
//        
//        try {
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File((((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/HOM_SENHA.jasper"))));
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
//            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//            String nomeDownload = "senha_" + DataHoje.hora().replace(":", "") + ".pdf";
//            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/senhas");
//            Diretorio.criar("Arquivos/senhas");
//            if (!new File(pathPasta).exists()) {
//                File file = new File(pathPasta);
//                file.mkdir();
//            }
//            SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
//            salvaArquivos.salvaNaPasta(pathPasta);
//            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
//            download.baixar();
//            download.remover();
//        } catch (JRException e) {
//        }        
//        return;
//    }   

