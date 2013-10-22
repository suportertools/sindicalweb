package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Recepcao;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.homologacao.db.DemissaoDB;
import br.com.rtools.homologacao.db.DemissaoDBToplink;
import br.com.rtools.homologacao.db.StatusDB;
import br.com.rtools.homologacao.db.StatusDBToplink;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.impressao.ParametroSenha;
import br.com.rtools.logSistema.NovoLog;
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
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class RecepcaoBean implements Serializable {

    private Agendamento agendamento = new Agendamento();
    private int idIndex = 0;
    private int idStatus = 0;
    private boolean renderConcluir = false;
    private boolean ocultaData = false;
    private boolean ocultaSenha = false;
    private boolean ocultaStatus = false;
    private boolean ocultaPreposto = false;
    private boolean ocultaHomologador = false;
    private boolean ocultaDatapesquisa = false;
    private boolean ocultaColunaEmpresa = false;
    private boolean ocultaColunaPessoaFisica = false;
    private boolean isPesquisarPessoaFisicaFiltro = false;
    private boolean isPesquisarPessoaJuridicaFiltro = false;
    private boolean desabilitaAtualizacaoAutomatica = false;
    private boolean desabilitaPesquisaProtocolo = false;
    private boolean dataPesquisaTodas = false;
    private int isCaso = 0;
    private List listaGrid = new ArrayList();
    private MacFilial macFilial = null;
    private String strData = DataHoje.data();
    private String strDataFinal = DataHoje.data();
    private String strEndereco = "";
    private String msgRecepcao = "";
    private String msgConfirma = "";
    private String statusEmpresa = "";
    private Date data = DataHoje.dataHoje();
    private Date dataInicial = DataHoje.dataHoje();
    private Date dataFinal = DataHoje.dataHoje();
    private Fisica fisica = new Fisica();
    private Juridica juridica = new Juridica();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private Recepcao recepcao = new Recepcao();
    private Registro registro = new Registro();
    private Profissao profissao = new Profissao();
    private int idMotivoDemissao = 0;
    private String tipoPesquisa = "";
    private String dataPesquisa = "hoje";
    private Cancelamento cancelamento = new Cancelamento();
    private List<ParametroSenha> listax = new ArrayList();
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private int id_protocolo = -1;
    private String numeroProtocolo = "";

    public String gerarSenha() {
        if (agendamento.getId() == -1) {
            msgConfirma = "Agendamento não foi encontrado!";
            return null;
        }

        if (recepcao.getHoraInicialPreposto().isEmpty()) {
            msgConfirma = "Preposto ainda não esta presente, aguarde sua chegada!";
            return null;
        }

        if (recepcao.getHoraInicialFuncionario().isEmpty()) {
            msgConfirma = "Funcionário ainda não esta presente, aguarde sua chegada!";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        if (agendamento.getRecepcao() == null) {
            if (recepcao.getId() == -1) {
                if (!sv.inserirObjeto(recepcao)) {
                    msgConfirma = "Erro ao salvar recepção!";
                    sv.desfazerTransacao();
                    return null;
                }
            } else {
                if (!sv.alterarObjeto(recepcao)) {
                    msgConfirma = "Erro ao atualizar recepção!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
            agendamento.setRecepcao(recepcao);
        } else {
            recepcao = agendamento.getRecepcao();
            if (!sv.alterarObjeto(recepcao)) {
                msgConfirma = "Erro ao atualizar recepção!";
                sv.desfazerTransacao();
                return null;
            }
        }

        if (recepcao == null || recepcao.getId() == -1) {
            msgConfirma = "Agendamento ainda não possui cadastro de preposto!";
            return null;
        }


        if (!sv.alterarObjeto(agendamento)) {
            msgConfirma = "Erro ao atualizar agendamento!";
            sv.desfazerTransacao();
            return null;
        }
        HomologacaoDB db = new HomologacaoDBToplink();
        Senha senha = db.pesquisaSenhaAgendamento(agendamento.getId());
        if (senha.getId() == -1) {
            senha.setAgendamento(agendamento);
            senha.setDtData(DataHoje.dataHoje());
            senha.setHora(DataHoje.horaMinuto());
            senha.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
            senha.setFilial(macFilial.getFilial());
            senha.setSenha(db.pesquisaUltimaSenha(macFilial.getFilial().getId()) + 1);
            if (!sv.inserirObjeto(senha)) {
                msgConfirma = "Erro ao gerar Senha";
                return null;
            }
            msgConfirma = "Senha gerada com Sucesso!";
        } else {
            if (!sv.alterarObjeto(senha)) {
                msgConfirma = "Erro ao atualizar Senha";
                return null;
            }
            msgConfirma = "Senha existente!";
        }

        listax.add(new ParametroSenha(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
                macFilial.getFilial().getFilial().getPessoa().getNome(),
                macFilial.getFilial().getMatriz().getPessoa().getDocumento(),
                senha.getAgendamento().getPessoaEmpresa().getJuridica().getPessoa().getNome(),
                senha.getAgendamento().getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
                senha.getAgendamento().getRecepcao().getPreposto(),
                senha.getAgendamento().getPessoaEmpresa().getFisica().getPessoa().getNome(),
                senha.getUsuario().getPessoa().getNome(),
                senha.getData(),
                senha.getHora(),
                String.valueOf(senha.getSenha())));

        sv.comitarTransacao();
        return null;
    }

    public void pesquisarProtocolo() {
//        desabilitaAtualizacaoAutomatica = true;
//        getListaHorarios();
    }

    public String imprimirSenha() {
        if (!listax.isEmpty()) {
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
            String nomeArq = "senha_";
            try {
                JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/HOM_SENHA.jasper"));
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                String nomeDownload = nomeArq + DataHoje.hora().replace(":", "") + ".pdf";
                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/senhas");
                sa.salvaNaPasta(pathPasta);
                Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                download.baixar();
            } catch (Exception e) {
                GenericaMensagem.warn("Erro", "Erro ao imprimir senha");
                NovoLog log = new NovoLog();
                log.novo("Erro ao imprimir senha:", "Mensagem: " + e.getMessage() + " - Causa: " + e.getCause() + " - Caminho: " + e.getStackTrace().toString());
                return null;
            }
            listax.clear();
            return "recepcao";
        }
        return "recepcao";
    }

    public String cancelarHorario() {
        if (cancelamento.getMotivo().isEmpty() || cancelamento.getMotivo().length() <= 5) {
            msgConfirma = "Motivo de cancelamento inválido";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        agendamento.setStatus((Status) sv.pesquisaCodigo(3, "Status"));

        sv.abrirTransacao();

        if (!sv.alterarObjeto(agendamento)) {
            msgConfirma = "Erro ao atualizar agendamento";
            return null;
        }

        pessoaEmpresa.setDtDemissao(null);
        if (!sv.alterarObjeto(pessoaEmpresa)) {
            msgConfirma = "Erro ao atualizar Pessoa Empresa";
            return null;
        }

        cancelamento.setAgendamento(agendamento);
        cancelamento.setDtData(DataHoje.dataHoje());
        cancelamento.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));

        if (!sv.inserirObjeto(cancelamento)) {
            msgConfirma = "Erro ao salvar cancelamento";
            return null;
        }

        strEndereco = "";
        renderConcluir = false;
        msgConfirma = "Horário cancelado com sucesso!";
        msgRecepcao = "";
        fisica = new Fisica();
        juridica = new Juridica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        sv.comitarTransacao();
        return null;
    }

    public String pesquisar() {
        if (tipoPesquisa.equals("juridica")) {
            return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pesquisaPessoaJuridica();
        } else {
            return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pesquisaPessoaFisica();
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
        StatusDB db = new StatusDBToplink();
        List select = new ArrayList();
        select.add(db.pesquisaCodigo(2));
        select.add(db.pesquisaCodigo(3));
        select.add(db.pesquisaCodigo(4));
        select.add(db.pesquisaCodigo(5));
        select.add(db.pesquisaCodigo(7));
        if (!select.isEmpty()) {
            int i = 0;
            while (i < select.size()) {
                result.add(new SelectItem(new Integer(i),
                        (String) ((Status) select.get(i)).getDescricao(),
                        Integer.toString(((Status) select.get(i)).getId())));
                i++;
            }
            //if(isPesquisarPessoaFisicaFiltro || isPesquisarPessoaJuridicaFiltro){
            result.add(new SelectItem(new Integer(i), "Todos", "6"));
            //}            
        }
        return result;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        DemissaoDB db = new DemissaoDBToplink();
        List select = db.pesquisaTodos();
        if (!select.isEmpty()) {
            int i = 0;
            while (i < select.size()) {
                result.add(new SelectItem(new Integer(i),
                        (String) ((Demissao) select.get(i)).getDescricao(),
                        Integer.toString(((Demissao) select.get(i)).getId())));
                i++;
            }
        }
        return result;
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        AgendamentoDB db = new AgendamentoDBToplink();
        sv.abrirTransacao();

        if (registro.isSenhaHomologacao()) {
            if (recepcao.getId() == -1) {
                if (!sv.inserirObjeto(recepcao)) {
                    msgConfirma = "Erro ao salvar recepção!";
                    sv.desfazerTransacao();
                    return null;
                }
            } else {
                if (!sv.alterarObjeto(recepcao)) {
                    msgConfirma = "Erro ao atualizar recepção!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
            agendamento.setRecepcao(recepcao);
        }

        if (!sv.alterarObjeto(agendamento)) {
            msgConfirma = "Erro ao atualizar!";
            sv.desfazerTransacao();
            return null;
        } else {
            //agendamento.setRecepcao(null);
        }

//        if ( ((Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro")).isSenhaHomologacao() ){
//            Senha senha = new Senha();
//            senha = db.pesquisaSenhaAgendamento(agendamento.getId());
//            if (senha.getId() == -1){
//                senha.setAgendamento(agendamento);
//                senha.setDtData(DataHoje.dataHoje());
//                senha.setHora(DataHoje.horaMinuto());
//                senha.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
//                senha.setFilial(macFilial.getFilial());
//                senha.setSenha(db.pesquisaUltimaSenha()+1);
//                if (!sv.inserirObjeto(senha)){
//                    msgConfirma = "Erro ao gerar Senha";
//                    return null;
//                }
//                msgConfirma = "Senha gerada com Sucesso!";
//            }else{
//                if (!sv.alterarObjeto(senha)){
//                    msgConfirma = "Erro ao atualizar Senha";
//                    return null;
//                }
//                msgConfirma = "Senha existente!";
//            }
//        }

//        strEndereco = "";
//        fisica = new Fisica();
//        agendamento = new Agendamento();
//        pessoaEmpresa = new PessoaEmpresa();
//        profissao = new Profissao();
//        juridica = new Juridica();
        msgConfirma = "Agendamento atualizado com sucesso!";
        sv.comitarTransacao();
        return null;
    }

    public String agendar() {
        if (getData() != null) {
            if (DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))
                    < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                return "recepcao";
            }
        }
        HomologacaoDB db = new HomologacaoDBToplink();
        renderConcluir = true;
        int idCaso = Integer.parseInt((String) ((DataObject) listaGrid.get(idIndex)).getArgumento12().toString());
        switch (idCaso) {
            case 2:
            case 3:
            case 4:
            case 7: {
                agendamento = (Agendamento) ((DataObject) listaGrid.get(idIndex)).getArgumento9();
                fisica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFisica();
                juridica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getJuridica();
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                profissao = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFuncao();
                if (agendamento.getRecepcao() != null) {
                    recepcao = agendamento.getRecepcao();
                } else {
                    recepcao = new Recepcao();
                }
                for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                    if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                        break;
                    }
                }
                break;
            }
            case 5: {
                agendamento = (Agendamento) ((DataObject) listaGrid.get(idIndex)).getArgumento9();
                if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == agendamento.getHomologador().getId()) {
                    fisica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFisica();
                    juridica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getJuridica();
                    pessoaEmpresa = agendamento.getPessoaEmpresa();
                    profissao = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFuncao();
                    if (agendamento.getRecepcao() != null) {
                        recepcao = agendamento.getRecepcao();
                    } else {
                        recepcao = new Recepcao();
                    }
                    for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                        if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                            idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                            break;
                        }
                    }
                    break;
                } else {
                    agendamento = new Agendamento();
                    renderConcluir = false;
                    break;
                }
            }
        }
        return "recepcao";
    }

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
            if (senha != null) {
                if (senha.getId() != -1) {
                    // senhaString = senha.getId() == -1 ? null : senha.getSenha();
                    senhaId = "tblListaRecepcao";
                    senhaString = ((Integer) senha.getSenha()).toString();
                } else {
                }
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
        strEndereco = "";
        renderConcluir = false;
        agendamento = new Agendamento();
        //pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public boolean isRenderConcluir() {
        return renderConcluir;
    }

    public void setRenderConcluir(boolean renderConcluir) {
        this.renderConcluir = renderConcluir;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getMsgRecepcao() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") == null) {
            msgRecepcao = "Não existe filial definida!";
        } else {
            macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
        }
        return msgRecepcao;
    }

    public void setMsgRecepcao(String msgRecepcao) {
        this.msgRecepcao = msgRecepcao;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null && tipoPesquisa.equals("juridica")) {
            idStatus = 5;
            numeroProtocolo = "";
            isPesquisarPessoaJuridicaFiltro = true;
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
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

    public PessoaEndereco getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(PessoaEndereco enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String getStrEndereco() {
        if (juridica.getId() != -1) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
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

    public Fisica getFisica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null) {
            isPesquisarPessoaFisicaFiltro = true;
            numeroProtocolo = "";
            fisica = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
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

    public Recepcao getRecepcao() {
        return recepcao;
    }

    public void setRecepcao(Recepcao recepcao) {
        this.recepcao = recepcao;
    }

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
            registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
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
            Registro reg = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");

            lista.add(new ParametroProtocolo(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                    reg.getDocumentoHomologacao(),
                    reg.getFormaPagamentoHomologacao(),
                    age.getEmissao()));


            //byte[] arquivo = new byte[0];
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = "imp_protocolo_" + proto + ".pdf";

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");

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
        return DataHoje.data();
    }

    public void seDataInicialString(String dataInicial) {
        this.dataInicial = DataHoje.converte(dataInicial);
    }

    public String getDataFinalString() {
        return DataHoje.converteData(dataFinal);
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
}
