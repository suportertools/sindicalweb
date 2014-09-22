package br.com.rtools.homologacao.beans;

import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.ListaAgendamento;
import br.com.rtools.homologacao.Recepcao;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.impressao.ParametroSenha;
import br.com.rtools.impressao.beans.SenhaHomologacao;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Polling;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
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
    private List listaGrid;
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
    private List<Senha> listaAtendimentoSimples = new ArrayList<Senha>();
    private String dataInicialString;
    private String dataFinalString;
    private boolean openDialog;
    
    @PostConstruct
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
        listaGrid = new ArrayList();
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
        listaRecepcaos = new ArrayList<ListaAgendamento>();
        macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
        dataInicialString = DataHoje.data();
        dataFinalString = DataHoje.data();
        openDialog = false;
        if (getRegistro().isSenhaHomologacao())
            agendamentoEdit.setRecepcao(new Recepcao());
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("recepcaoBean");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("fisicaPesquisa");
    }

    public void fecharModal(){
        agendamentoEdit = new Agendamento();
        openDialog = false;
        recepcao = new Recepcao();
    }
    
    public void gerarSenha() {
        // parei aqui.. testar o gerar senha e alterar a variavel recepcao para agendamentoEdit.recepcao
        Dao di = new Dao();
        di.openTransaction();
        
        if (registro.isSenhaHomologacao()){
            if (!recepcao.getPreposto().isEmpty()) {
                //msgConfirma = "Informar o nome do preposto!";
//                GenericaMensagem.warn("Atenção", "Informar o NOME DO PREPOSTO!");
//                return;
                if (recepcao.getHoraInicialPreposto().isEmpty()) {
                    GenericaMensagem.warn("Atenção", "Informar o HORÁRIO que o preposto chegou!");
                    di.rollback();
                    return;
                }
            }         
            
//            if (agendamentoEdit.getRecepcao().getHoraInicialPreposto().isEmpty()) {
//                //msgConfirma = "Preposto ainda não esta presente, aguarde sua chegada!";
//                GenericaMensagem.warn("Atenção", "PREPOSTO ainda não esta presente, aguarde sua chegada!");
//                return;
//            }

            if (recepcao.getHoraInicialFuncionario().isEmpty()) {
                //msgConfirma = "Funcionário ainda não esta presente, aguarde sua chegada!";
                GenericaMensagem.warn("Atenção", "FUNCIONÁRIO ainda não esta presente, aguarde sua chegada!");
                di.rollback();
                return;
            }

            if (recepcao.getId() == -1) {
                if (!di.save(recepcao)) {
                    //msgConfirma = "Erro ao salvar recepção!";
                    GenericaMensagem.error("Erro", "Não foi possível SALVAR Recepção!");
                    di.rollback();
                    return;
                }
            } else {
                if (!di.update(recepcao)) {
                    //msgConfirma = "Erro ao atualizar recepção!";
                    GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Recepção!");
                    di.rollback();
                    return;
                }
            }
            agendamentoEdit.setRecepcao(recepcao);
            

//            if (recepcao == null || recepcao.getId() == -1) {
//                //msgConfirma = "Agendamento ainda não possui cadastro de preposto!";
//                GenericaMensagem.warn("Atenção", "Agendamento ainda não possui cadastro de preposto!");
//                return;
//            }
        }
        
//        if (recepcao.getId() == -1)
//            agendamentoEdit.setRecepcao(null);
        
        if (!di.update(agendamentoEdit)) {
            //msgConfirma = "Erro ao atualizar agendamento!";
            GenericaMensagem.error("Erro", "Não foi possível ATUALIZAR Agendamento!");
            di.rollback();
            return;
        }
        
        SenhaHomologacao senhaHomologacao = new SenhaHomologacao();
        Collection<ParametroSenha> list = senhaHomologacao.parametros(agendamentoEdit, di);
        if (!list.isEmpty()) {
            //msgConfirma = "Senha gerada com sucesso";
            GenericaMensagem.info("Sucesso", "Senha Gerada!");
            senhaHomologacao.imprimir(agendamentoEdit);
        } else {
            //msgConfirma = "Erro ao gerar senha!";
            di.rollback();
            GenericaMensagem.error("Erro", "Não foi possível GERAR SENHA!");
            return;
        }
        di.commit();
    }

    public void pesquisarProtocolo() {

    }

    public void cancelarHorario() {
        if (cancelamento.getMotivo().isEmpty() || cancelamento.getMotivo().length() <= 5) {
            //msgConfirma = "Motivo de cancelamento inválido";
            GenericaMensagem.warn("Atenção", "Motivo de Cancelamento inválido");
            return;
        }
        
        DaoInterface di = new Dao();
        agendamentoEdit.setStatus((Status) di.find(new Status(), 3));
        di.openTransaction();
        if (!di.update(agendamentoEdit)) {
            //msgConfirma = "Erro ao atualizar agendamento";
            di.rollback();
            GenericaMensagem.error("Erro", "Erro ao atualizar Agendamento");
            return;
        }
        agendamentoEdit.getPessoaEmpresa().setDtDemissao(null);
        if (!di.update(agendamentoEdit.getPessoaEmpresa())) {
            //msgConfirma = "Erro ao atualizar Pessoa Empresa";
            di.rollback();
            GenericaMensagem.error("Erro", "Erro ao atualizar Pessoa Empresa");
            return;
        }
        
        cancelamento.setAgendamento(agendamentoEdit);
        cancelamento.setDtData(DataHoje.dataHoje());
        cancelamento.setUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")));
        
        if (!di.save(cancelamento)) {
            //msgConfirma = "Erro ao salvar cancelamento";
            di.rollback();
            GenericaMensagem.error("Erro", "Erro ao salvar Cancelamento");
            return;
        }
        
//        msgConfirma = "Horário cancelado com sucesso!";
//        msgRecepcao = "";
        //fisica = new Fisica();
        //juridica = new Juridica();
        //agendamento = new Agendamento();
        //profissao = new Profissao();
        //GenericaSessao.remove("juridicaPesquisa");
        //GenericaSessao.remove("fisicaPesquisa");
        GenericaMensagem.info("Sucesso", "Homologação Cancelada!");
        di.commit();
        cancelamento = new Cancelamento();
    }

    public String pesquisar() {
        if (tipoPesquisa.equals("juridica")) {
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
        return null;
    }

    public List<SelectItem> getListaStatus() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        List<Status> list = (List<Status>) sadb.pesquisaObjeto(new int[]{2, 3, 4, 5, 7}, "Status");
        if (!list.isEmpty()) {
        int i = 0;
            for (i = 0; i < list.size(); i++) {
                result.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
            result.add(new SelectItem(i++, "Todos", "6"));
        }
        return result;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        List<SelectItem> listaMotivoDemissao = new ArrayList<SelectItem>();
        DaoInterface di = new Dao();
        List<Demissao> list = (List<Demissao>) di.list(new Demissao(), true);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                listaMotivoDemissao.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaMotivoDemissao;
    }

    public void salvar() {
        Dao di = new Dao();
        di.openTransaction();
        if (registro.isSenhaHomologacao()) {
            if (!recepcao.getPreposto().isEmpty()) {
                //msgConfirma = "Informar o nome do preposto!";
//                GenericaMensagem.warn("Atenção", "Informar o NOME DO PREPOSTO!");
//                return;
                if (recepcao.getHoraInicialPreposto().isEmpty()) {
                    GenericaMensagem.warn("Atenção", "Informar o HORÁRIO que o preposto chegou!");
                    di.rollback();
                    return;
                }
            }

            if (!recepcao.getHoraInicialFuncionario().isEmpty()) {
                if (recepcao.getId() == -1) {
                    if (!di.save(recepcao)) {
                        //msgConfirma = "Erro ao salvar recepção!";
                        GenericaMensagem.error("Erro", "Erro ao salvar Recepção!");
                        di.rollback();
                        return;
                    }
                } else {
                    if (!di.update(recepcao)) {
                        //msgConfirma = "Erro ao atualizar recepção!";
                        GenericaMensagem.error("Erro", "Erro ao atualizar Recepção!");
                        di.rollback();
                        return;
                    }
                }
                agendamentoEdit.setRecepcao(recepcao);
            }
        }
        
        if (!di.update(agendamentoEdit)) {
            //msgConfirma = "Erro ao atualizar!";
            GenericaMensagem.error("Erro", "Erro ao atualizar Agendamento!");
            di.rollback();
            return;
        } else {
            //agendamento.setRecepcao(null);
        }
        //msgConfirma = "Agendamento atualizado com sucesso!";
        GenericaMensagem.info("Sucesso", "Agendamento atualizado!");
        di.commit();
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
                //fisica = ((PessoaEmpresa) datao.getArgumento7()).getFisica();
                //juridica = ((PessoaEmpresa) datao.getArgumento7()).getJuridica();
                //pessoaEmpresa = agendamentoEdit.getPessoaEmpresa();
                profissao = ((PessoaEmpresa) datao.getArgumento7()).getFuncao();
//                if (agendamentoEdit.getRecepcao() != null) {
//                    recepcao = agendamentoEdit.getRecepcao();
//                } else {
//                    recepcao = new Recepcao();
//                }
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
                    //fisica = ((PessoaEmpresa) datao.getArgumento7()).getFisica();
                    //juridica = ((PessoaEmpresa) datao.getArgumento7()).getJuridica();
                    //pessoaEmpresa = agendamentoEdit.getPessoaEmpresa();
                    profissao = ((PessoaEmpresa) datao.getArgumento7()).getFuncao();
//                    if (agendamentoEdit.getRecepcao() != null) {
//                        recepcao = agendamentoEdit.getRecepcao();
//                    } else {
//                        recepcao = new Recepcao();
//                    }
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
        if (cancelamento == null) cancelamento = new Cancelamento();
        
        if (agendamentoEdit.getRecepcao() != null)
            recepcao = agendamentoEdit.getRecepcao();
        
        getStrEndereco();
        openDialog = true;
    }

//    public String agendarx(Agendamento a) {
//        if (getData() != null) {
//            if (DataHoje.converteDataParaInteger(DataHoje.converteData(getData())) < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
//                return "recepcao";
//            }
//        }
//
//        int idCaso = a.getStatus().getId();
//        agendamento = a;
//        if (idCaso == 5) {
//            if (((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId() != agendamento.getHomologador().getId()) {
//                agendamento = new Agendamento();
//            }
//        }
//        if (idCaso == 2 || idCaso == 3 || idCaso == 4 || idCaso == 5 || idCaso == 7) {
//            fisica = a.getPessoaEmpresa().getFisica();
//            juridica = a.getPessoaEmpresa().getJuridica();
//            pessoaEmpresa = a.getPessoaEmpresa();
//            profissao = a.getPessoaEmpresa().getFuncao();
//            if (agendamento.getRecepcao() != null) {
//                recepcao = agendamento.getRecepcao();
//            } else {
//                recepcao = new Recepcao();
//            }
//            for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
//                if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
//                    idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
//                    break;
//                }
//            }
//        }
//        return "recepcao";
//    }

    public synchronized List getListaHorarios() {
        if (macFilial == null) {
            return new ArrayList();
        }
        if (isPesquisarPessoaFisicaFiltro == true) {
            juridica = new Juridica();
        }
        if (isPesquisarPessoaJuridicaFiltro == true) {
            fisica = new Fisica();
        }
        listaGrid.clear();
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
        DataObject dtObj;
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
                ag = db.pesquisaAgendamento(idCasoStatus, macFilial.getFilial().getId(), dataInicialA, dataFinalA, 0, fisica.getId(), juridica.getId());
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
            
            if (senha != null && !isOposicao) {
                if (senha.getId() != -1) {
                    // senhaString = senha.getId() == -1 ? null : senha.getSenha();
                    senhaId = "tblListaRecepcaox";
                    senhaString = ((Integer) senha.getSenha()).toString();
                } else {
                }
            }else{
                senhaId = "tblAgendamentoOposicaox";
            }
            
            dtObj = new DataObject(
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
                    null);
            listaGrid.add(dtObj);
        }
        return listaGrid;
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
        if (GenericaSessao.exists("juridicaPesquisa") && tipoPesquisa.equals("juridica")) {
            idStatus = 5;
            numeroProtocolo = "";
            isPesquisarPessoaJuridicaFiltro = true;
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            GenericaSessao.remove("fisicaPesquisa");
            dataPesquisaTodas = true;
        }
        if (juridica.getId() != -1 || fisica.getId() != -1) {
            desabilitaPesquisaProtocolo = true;
        } else {
            desabilitaPesquisaProtocolo = false;
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
        }
        if (juridica.getId() != -1 || fisica.getId() != -1) {
            desabilitaPesquisaProtocolo = true;
        } else {
            desabilitaPesquisaProtocolo = false;
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
        listaRecepcaos.clear();
        getListaRecepcaos();
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

    public List<ListaAgendamento> getListaRecepcaos() {
        try {
            Polling polling = new Polling();
            polling.existeUsuarioSessao();
        } catch (IOException e) {
            return new ArrayList();
        }
        if (macFilial == null) {
            return new ArrayList();
        }
        if (macFilial == null) {
            return new ArrayList();
        }
        if (isPesquisarPessoaFisicaFiltro == true) {
            juridica = new Juridica();
        }
        if (isPesquisarPessoaJuridicaFiltro == true) {
            fisica = new Fisica();
        }
        ocultaData = false;
        ocultaHomologador = false;
        ocultaPreposto = false;
        ocultaStatus = false;
        ocultaColunaPessoaFisica = false;
        ocultaColunaEmpresa = false;
        ocultaSenha = false;
        listaRecepcaos.clear();
        listaGrid.clear();
        listaGrid = new ArrayList();
        HomologacaoDB db = new HomologacaoDBToplink();
        Usuario us = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        int idCaso = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        Date dataInicialA = null;
        Date dataFinalA = null;
        desabilitaAtualizacaoAutomatica = false;
        if (!numeroProtocolo.equals("")) {
            if (Integer.parseInt(numeroProtocolo) > 0) {
                desabilitaAtualizacaoAutomatica = true;
            }
        }
        if (idCaso > 0) {
            if (idCaso == 6) {
                idCaso = 0;
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
        List<Agendamento> agendamentos;
        if (desabilitaAtualizacaoAutomatica) {
            agendamentos = (List<Agendamento>) db.pesquisaAgendamentoPorProtocolo(Integer.parseInt(numeroProtocolo));
        } else {
            agendamentos = db.pesquisaAgendamento(idCaso, macFilial.getFilial().getId(), dataInicialA, dataFinalA, 0, fisica.getId(), juridica.getId());
        }
        DaoInterface di = new Dao();
        Registro reg = (Registro) di.find(new Registro(), 1);
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
//                if (senha.getId() == -1) {
//                    if (agendamentos.get(i).getStatus().getId() != 7 && agendamentos.get(i).getStatus().getId() != 3 && agendamentos.get(i).getStatus().getId() != 4 && agendamentos.get(i).getStatus().getId() != 5) {
//                        continue;
//                    }
//                } else {
//                } 
                listaAgendamento.setSenha(senha);
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
            listaRecepcaos.add(listaAgendamento);
        }
        return listaRecepcaos;
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

//    public String getMotivoCancelamento() {
//        String motivo = "";
//        if (agendamentoEdit.getId() == -1) {
//            return motivo;
//        }
//        if (agendamentoEdit.getStatus().getId() != 3) {
//            return motivo;
//        }
//        HomologacaoDB dB = new HomologacaoDBToplink();
//        Cancelamento c = (Cancelamento) dB.pesquisaCancelamentoPorAgendanto(agendamentoEdit.getId());
//        if(c == null) {
//            motivo = "Cancelado no agendamento."; 
//        } else {
//            motivo = "Data: " + c.getData() + " - Motivo: " + c.getMotivo() + " - Cancelado por: " + c.getUsuario().getPessoa().getNome();            
//        }
//        return motivo;
//    }

    public List<Senha> getListaAtendimentoSimples() {
        if (listaAtendimentoSimples.isEmpty()){
            HomologacaoDB db = new HomologacaoDBToplink();
            SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();
            
            listaAtendimentoSimples = db.listaAtendimentoIniciadoSimples(macFilial.getFilial().getId(), su.getSessaoUsuario().getId());
        }
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
}
