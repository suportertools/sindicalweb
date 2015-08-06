package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Status;
import br.com.rtools.impressao.ParametroHomologacao;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioHomologacaoDao;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioHomologacaoBean implements Serializable {

    private Fisica funcionario;
    private Juridica empresa;
    private Usuario operador;
    private List<SelectItem>[] listSelectItem;
    private Boolean[] filtro;
    private Boolean[] disabled;
    private Date dataInicial;
    private Date dataFinal;
    private Date dataDemissaoInicial;
    private Date dataDemissaoFinal;
    private Integer[] index;
    // 1 - AGENDADOR [web / não web / todos]
    // 2 - RECEPÇÃO
    // 3 - HOMOLOGADOR
    private String tipoUsuarioOperacional;
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private String order;
    private String sexo;
    private String tipoAgendador;
    private Boolean tipoAviso;
    private Boolean printHeader;
    private Boolean webAgendamento;
    
    @PostConstruct
    public void init() {
        disabled = new Boolean[2];
        disabled[0] = false; // PERÍODO AGENDAMENTO
        disabled[1] = false; // PERÍODO DEMISSÃO
        filtro = new Boolean[12];
        filtro[0] = false; // FILIAL
        filtro[1] = false; // PERÍODO AGENDAMENTO
        filtro[2] = false; // STATUS
        filtro[3] = false; // EMPRESA
        filtro[4] = false; // FUNCIONÁRIO
        filtro[5] = false; // USUÁRIO OPERACIONAL
        filtro[6] = false; // SEXO
        filtro[7] = false; // MOTIVO DEMISSÃO
        filtro[8] = false; // TIPO AVISO
        filtro[9] = false; // PERÍODO DEMISSÃO
        filtro[10] = false; // ORDER
        filtro[11] = false; // CONVENCAO
        listSelectItem = new ArrayList[6];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        listSelectItem[3] = new ArrayList<>();
        listSelectItem[4] = new ArrayList<>();
        listSelectItem[5] = new ArrayList<>(); // CONVENCAO
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        dataDemissaoInicial = DataHoje.dataHoje();
        dataDemissaoFinal = DataHoje.dataHoje();
        index = new Integer[6];
        index[0] = null;
        index[1] = null;
        index[2] = null;
        index[3] = null;
        index[4] = null;
        index[5] = null; // CONVENCAO
        tipoAviso = null;
        tipoUsuarioOperacional = null;
        tipoAgendador = null;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        order = "";
        funcionario = new Fisica();
        empresa = new Juridica();
        operador = new Usuario();
        sexo = "";
        tipo = "todos";
        printHeader = false;
        webAgendamento = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioHomologacaoBean");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("usuarioPesquisa");
        GenericaSessao.remove("tipoPesquisaPessoaJuridica");
    }

    public void print() {
        print(0);
    }

    public void print(int tcase) {
        Dao dao = new Dao();
        Relatorios relatorios;
        if (!getListTipoRelatorios().isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            relatorios = rgdb.pesquisaRelatorios(index[0]);
        } else {
            GenericaMensagem.info("Sistema", "Nenhum relatório encontrado!");
            return;
        }
        if (relatorios == null) {
            return;
        }
        String detalheRelatorio = "";
        Integer idEmpresa = null;
        Integer idFuncionario = null;
        Integer idUsuarioOperacional = null;
        Integer idFilial = null;
        Integer idStatus = null;
        Integer idMotivoDemissao = null;
        String pIStringI = "";
        String pFStringI = "";
        String sexoString = "";
        List listDetalhePesquisa = new ArrayList();
        Integer tCase = null;
        if (filtro[1]) {
            tCase = 1;
            pIStringI = DataHoje.converteData(dataInicial);
            pFStringI = DataHoje.converteData(dataFinal);
            listDetalhePesquisa.add(" Período de Agendamento entre " + pIStringI + " e " + pFStringI);
        } else if (filtro[9]) {
            tCase = 2;
            pIStringI = DataHoje.converteData(dataInicial);
            pFStringI = DataHoje.converteData(dataFinal);
            listDetalhePesquisa.add(" Período de Demissão entre " + pIStringI + " e " + pFStringI);
        }
        if (filtro[6]) {
            if (sexo != null) {
                switch (sexo) {
                    case "M":
                        sexoString = "Masculino";
                        break;
                    case "F":
                        sexoString = "Feminino";
                        break;
                    default:
                        sexoString = "Todos";
                        break;
                }
            }
            listDetalhePesquisa.add("Sexo: " + sexoString + "");
        }
        if (empresa.getId() != -1) {
            idEmpresa = empresa.getId();
            listDetalhePesquisa.add("Empresa: " + empresa.getPessoa().getDocumento() + " - " + empresa.getPessoa().getNome());
        }
        if (funcionario.getId() != -1) {
            idFuncionario = funcionario.getId();
            listDetalhePesquisa.add("Funcionário: " + funcionario.getPessoa().getDocumento() + " - " + funcionario.getPessoa().getNome());
        }
        if (operador.getId() != -1) {
            idUsuarioOperacional = operador.getId();
            listDetalhePesquisa.add("Operador: " + operador.getPessoa().getDocumento() + " - " + operador.getPessoa().getNome());
        }
        if (index[1] != null) {
            idFilial = Integer.parseInt(listSelectItem[1].get(index[1]).getDescription());
            listDetalhePesquisa.add("Filial: " + ((Filial) dao.find(new Filial(), idFilial)).getFilial().getPessoa().getNome());
        }
        if (index[2] != null) {
            idStatus = Integer.parseInt(listSelectItem[2].get(index[2]).getDescription());
            listDetalhePesquisa.add("Status: " + ((Status) dao.find(new Status(), idStatus)).getDescricao());
        }
        if (index[3] != null) {
            idMotivoDemissao = Integer.parseInt(listSelectItem[3].get(index[3]).getDescription());
            listDetalhePesquisa.add("Motivo Demissão: " + ((Demissao) dao.find(new Demissao(), idMotivoDemissao)).getDescricao());
        }
        if (filtro[8]) {
            if (tipoAviso != null) {
                if (tipoAviso) {
                    listDetalhePesquisa.add("Tipo de aviso: trabalhado");
                } else {
                    listDetalhePesquisa.add("Tipo de aviso: indenizado");
                }
            }
        }
        
        Integer idConvencao = null;
        if (filtro[11]) {
            idConvencao = Integer.parseInt(listSelectItem[5].get(index[5]).getDescription());
            listDetalhePesquisa.add("Convenção: " + ((Convencao) dao.find(new Convencao(), idConvencao)).getDescricao());
        }
        
        if (order == null) {
            order = "";
        }
        RelatorioHomologacaoDao relatorioHomologacaoDao = new RelatorioHomologacaoDao();
        relatorioHomologacaoDao.setOrder(order);
        String operadorHeader = "";
        if (tipoUsuarioOperacional == null || tipoUsuarioOperacional.equals("id_homologador")) {
            operadorHeader = "HOMOLOGADOR";
            tipoUsuarioOperacional = "id_homologador";
        } else if (tipoUsuarioOperacional.equals("id_agendador")) {
            operadorHeader = "AGENDADOR";
        }
        List list = relatorioHomologacaoDao.find(relatorios, idEmpresa, idFuncionario, tipoUsuarioOperacional, idUsuarioOperacional, idStatus, idFilial, tCase, pIStringI, pFStringI, idMotivoDemissao, tipoAviso, tipoAgendador, sexo, webAgendamento, idConvencao);
        if (list.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }

        if (listDetalhePesquisa.isEmpty()) {
            detalheRelatorio += "Pesquisar todos registros!";
        } else {
            detalheRelatorio += "";
            for (int i = 0; i < listDetalhePesquisa.size(); i++) {
                if (i == 0) {
                    detalheRelatorio += "Detalhes: " + listDetalhePesquisa.get(i).toString();
                } else {
                    detalheRelatorio += "; " + listDetalhePesquisa.get(i).toString();
                }
            }
        }
        List<ParametroHomologacao> phs = new ArrayList<>();
        String operadorString = "";
        for (Object list1 : list) {
            if (tipoUsuarioOperacional == null || tipoUsuarioOperacional.equals("id_homologador")) {
                operadorString = AnaliseString.converteNullString(((List) list1).get(9));
            } else if (tipoUsuarioOperacional.equals("id_agendador")) {
                operadorString = AnaliseString.converteNullString(((List) list1).get(9));
                if (operadorString.isEmpty()) {
                    operadorString = "** Web ** ";
                }
            }
            phs.add(new ParametroHomologacao(
                    detalheRelatorio,
                    AnaliseString.converteNullString(((List) list1).get(2)), // DATA
                    AnaliseString.converteNullString(((List) list1).get(3)), // HORA
                    AnaliseString.converteNullString(((List) list1).get(4)), // CNPJ
                    AnaliseString.converteNullString(((List) list1).get(5)), // EMPRESA
                    AnaliseString.converteNullString(((List) list1).get(6)), // FUNCIONARIO
                    AnaliseString.converteNullString(((List) list1).get(7)), // CONTATO
                    AnaliseString.converteNullString(((List) list1).get(8)), // TELEFONE
                    operadorString, // OPERADOR
                    AnaliseString.converteNullString(((List) list1).get(10)), // OBS
                    AnaliseString.converteNullString(((List) list1).get(11)) // STATUS
            ));
        }
        if (!phs.isEmpty()) {
            Jasper.TYPE = "paisagem";
            Jasper.IS_HEADER = printHeader;
            Map map = new HashMap();
            map.put("operador_header", operadorHeader);
            Jasper.printReports(relatorios.getJasper(), "homologacao", (Collection) phs, map);
        }
    }

    public List<SelectItem> getListTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(177);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(list.get(i).getId(), list.get(i).getNome()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public void tipoRelatorioChange(TabChangeEvent event) {
        tipoRelatorio = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
        if (tipoRelatorio.equals("Simples")) {
            clear();
        }
    }

    public void selecionaDataInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void clear() {
        if (!filtro[0]) {
            listSelectItem[1] = new ArrayList();
            index[1] = null;
        }
        if (!filtro[1]) {
            if (!filtro[9]) {
                dataInicial = DataHoje.dataHoje();
                dataFinal = null;
                disabled[0] = false;
                disabled[1] = false;
            }
        } else {
            disabled[0] = true;
            disabled[1] = false;
            filtro[9] = false;
        }
        if (!filtro[2]) {
            listSelectItem[2] = new ArrayList();
            index[2] = null;
        }
        if (!filtro[3]) {
            empresa = new Juridica();
        }
        if (!filtro[4]) {
            funcionario = new Fisica();
        }
        if (!filtro[5]) {
            operador = new Usuario();
            webAgendamento = false;
            tipoUsuarioOperacional = null;
        }
        if (!filtro[6]) {
            sexo = "";
        }
        if (!filtro[7]) {
            listSelectItem[3] = new ArrayList();
            index[3] = null;
        }
        if (!filtro[8]) {
            tipoAviso = null;
        }
        if (!filtro[9]) {
            if (!filtro[1]) {
                disabled[0] = false;
                disabled[1] = false;
                dataDemissaoInicial = DataHoje.dataHoje();
                dataDemissaoInicial = null;
            }
        } else {
            disabled[0] = false;
            disabled[1] = true;
            filtro[1] = false;
        }
        if (!filtro[10]) {
            order = "";
        }
        if (!filtro[11]) {
            listSelectItem[5] = new ArrayList();
            index[5] = null;
        }
        
    }

    public void close(String close) {
        switch (close) {
            case "filial":
                listSelectItem[1] = new ArrayList();
                index[1] = null;
                filtro[0] = false;
                break;
            case "periodo_emissao":
                dataInicial = DataHoje.dataHoje();
                dataFinal = null;
                disabled[0] = false;
                disabled[1] = false;
                filtro[1] = false;
                PF.update("form_relatorio:i_panel_accordion:i_panel_avancado");
                break;
            case "status":
                listSelectItem[2] = new ArrayList();
                index[2] = null;
                filtro[2] = false;
                break;
            case "empresa":
                empresa = new Juridica();
                filtro[3] = false;
                break;
            case "funcionario":
                funcionario = new Fisica();
                filtro[4] = false;
                break;
            case "operador":
                operador = new Usuario();
                filtro[5] = false;
                webAgendamento = false;
                tipoUsuarioOperacional = null;
                break;
            case "sexo":
                filtro[6] = false;
                sexo = "";
                break;
            case "motivo_demissao":
                listSelectItem[3] = new ArrayList();
                index[3] = null;
                filtro[7] = false;
                break;
            case "tipo_aviso":
                tipoAviso = null;
                filtro[8] = false;
                break;
            case "periodo_demissao":
                dataDemissaoInicial = DataHoje.dataHoje();
                dataDemissaoFinal = null;
                disabled[0] = false;
                disabled[1] = false;
                filtro[9] = false;
                PF.update("form_relatorio:i_panel_accordion:i_panel_avancado");
                break;
            case "order":
                order = "";
                filtro[10] = false;
                break;
            case "convencao":
                filtro[11] = false;
                listSelectItem[5] = new ArrayList();
                index[5] = null;
                break;
        }
        PF.update("form_relatorio:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public List<SelectItem>[] getListSelectItem() {
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    /**
     * <strong>Index</strong>
     * <ul>
     * <li>[0] Tipos de Relatórios</li>
     * <li>[1] List[SelectItem] Convenção Período</li>
     * </ul>
     *
     * @return Integer
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    /**
     * <strong>Filtros</strong>
     * <ul>
     * <li>[0] MODALIDADE</li>
     * <li>[1] PERÍODO EMISSÃO / INATIVAÇÃO</li>
     * <li>[2] RESPONSÁVEL</li>
     * <li>[3] ALUNO</li>
     * <li>[4] SEXO</li>
     * <li>[5] ORDENAÇÃO </li>
     * <li>[6] PERIODOS </li>
     * </ul>
     *
     * @return boolean
     */
    public Boolean[] getFiltro() {
        return filtro;
    }

    public void setFiltro(Boolean[] filtro) {
        this.filtro = filtro;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Fisica getFuncionario() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            funcionario = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true));
        }
        return funcionario;
    }

    public void setFuncionario(Fisica funcionario) {
        this.funcionario = funcionario;
    }

    public Juridica getEmpresa() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            empresa = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
        }
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public Usuario getOperador() {
        if (GenericaSessao.exists("usuarioPesquisa")) {
            operador = ((Usuario) GenericaSessao.getObject("usuarioPesquisa", true));
        }
        return operador;
    }

    public void setOperador(Usuario operador) {
        this.operador = operador;
    }

    public String getTipoUsuarioOperacional() {
        return tipoUsuarioOperacional;
    }

    public void setTipoUsuarioOperacional(String tipoUsuarioOperacional) {
        this.tipoUsuarioOperacional = tipoUsuarioOperacional;
    }

    public List<SelectItem> getListFiliais() {
        if (listSelectItem[1].isEmpty()) {
            DaoInterface di = new Dao();
            List<Filial> list = (List<Filial>) di.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i,
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[1];
    }

    public List<SelectItem> getListConvencao() {
        if (listSelectItem[5].isEmpty()) {
            List<Convencao> list = new Dao().list(new Convencao());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[5].add(new SelectItem(
                        i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[5];
    }

    public List<SelectItem> getListStatus() {
        if (listSelectItem[2].isEmpty()) {
            DaoInterface di = new Dao();
            List<Status> list = (List<Status>) di.list(new Status(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[2].add(new SelectItem(i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[2];
    }

    public List<SelectItem> getListMotivoDemissao() {
        if (listSelectItem[3].isEmpty()) {
            DaoInterface di = new Dao();
            List<Demissao> list = (List<Demissao>) di.list(new Demissao(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[3].add(new SelectItem(i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[3];
    }

    public Boolean getTipoAviso() {
        return tipoAviso;
    }

    public void setTipoAviso(Boolean tipoAviso) {
        this.tipoAviso = tipoAviso;
    }

    public String getTipoAgendador() {
        return tipoAgendador;
    }

    public void setTipoAgendador(String tipoAgendador) {
        this.tipoAgendador = tipoAgendador;
    }

    public Boolean getPrintHeader() {
        return printHeader;
    }

    public void setPrintHeader(Boolean printHeader) {
        this.printHeader = printHeader;
    }

    public Date getDataDemissaoInicial() {
        return dataDemissaoInicial;
    }

    public void setDataDemissaoInicial(Date dataDemissaoInicial) {
        this.dataDemissaoInicial = dataDemissaoInicial;
    }

    public Date getDataDemissaoFinal() {
        return dataDemissaoFinal;
    }

    public void setDataDemissaoFinal(Date dataDemissaoFinal) {
        this.dataDemissaoFinal = dataDemissaoFinal;
    }

    public Boolean[] getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean[] disabled) {
        this.disabled = disabled;
    }

    public Boolean getWebAgendamento() {
        return webAgendamento;
    }

    public void setWebAgendamento(Boolean webAgendamento) {
        this.webAgendamento = webAgendamento;
    }

    public void listener(Integer tCase) {
        if (tCase == 1) {
            if (tipoUsuarioOperacional != null && tipoUsuarioOperacional.equals("id_homologador")) {
                if (!filtro[2]) {
                    filtro[2] = true;
                    getListStatus();
                    for (int i = 0; i < listSelectItem[2].size(); i++) {
                        if (Integer.parseInt(listSelectItem[2].get(i).getDescription()) == 4) {
                            index[2] = i;
                            break;
                        }
                    }
                }
            }
        }
    }
}
