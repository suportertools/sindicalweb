package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.Midia;
import br.com.rtools.escola.EscStatus;
import br.com.rtools.escola.Professor;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.Vendedor;
import br.com.rtools.escola.dao.MatriculaEscolaDao;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.homologacao.Status;
import br.com.rtools.impressao.ParametroEscolaCadastral;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioMatriculaEscolaDao;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
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
public class RelatorioMatriculaEscolaBean implements Serializable {

    private Fisica aluno;
    private Pessoa responsavel;
    private Turma turma;
    private Usuario operador;
    private List<SelectItem>[] listSelectItem;
    private Boolean[] filtro;
    private Boolean[] disabled;
    private Date dataMatriculaInicial;
    private Date dataMatriculaFinal;
    private Date dataInicial;
    private Date dataFinal;
    private Date dataNascimentoInicial;
    private Date dataNascimentoFinal;
    private Integer[] index;
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private String order;
    private String[] horario;
    private String sexo;
    private Boolean printHeader;
    private Boolean socios;
    private Boolean tipoMatricula;
    private String title;

    @PostConstruct
    public void init() {
        horario = new String[2];
        horario[0] = "";
        horario[1] = "";
        disabled = new Boolean[2];
        disabled[0] = false;
        disabled[1] = false;
        filtro = new Boolean[17];
        filtro[0] = false; // FILIAL
        filtro[1] = false; // PERÍODO MATRÍCULA
        filtro[2] = false; // PERÍODO
        filtro[3] = false; // NASCIMENTO
        filtro[4] = false; // STATUS
        filtro[5] = false; // MÍDIA
        filtro[6] = false; // PROFESSOR
        filtro[7] = false; // VENDEDOR
        filtro[8] = false; // ALUNO
        filtro[9] = false; // SEXO
        filtro[10] = false; // RESPONSÁVEL
        filtro[11] = false; // TIPO MATRÍCULA
        filtro[12] = false; // TURMA
        filtro[13] = false; // CURSO
        filtro[14] = false; // HORÁRIO
        filtro[15] = false; // ORDER
        filtro[16] = false; // 
        listSelectItem = new ArrayList[7];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        listSelectItem[3] = new ArrayList<>();
        listSelectItem[4] = new ArrayList<>();
        listSelectItem[5] = new ArrayList<>();
        listSelectItem[6] = new ArrayList<>();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        dataMatriculaInicial = DataHoje.dataHoje();
        dataMatriculaFinal = null;
        dataInicial = DataHoje.dataHoje();
        dataFinal = null;
        dataNascimentoInicial = DataHoje.dataHoje();
        dataNascimentoFinal = null;
        index = new Integer[7];
        index[0] = null;
        index[1] = null;
        index[2] = null;
        index[3] = null;
        index[4] = null;
        index[5] = null;
        index[6] = null;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        order = "";
        aluno = new Fisica();
        responsavel = new Pessoa();
        turma = new Turma();
        sexo = "";
        tipo = "todos";
        printHeader = false;
        socios = false;
        tipoMatricula = false;
        title = "Turma";
        if (GenericaSessao.exists("title")) {
            title = GenericaSessao.getString("title", true);
        }
        if (GenericaSessao.exists("tipoMatricula")) {
            tipoMatricula = GenericaSessao.getBoolean("tipoMatricula", true);
        }
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioMatriculaEscolaBean");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("pessoaPesquisa");
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
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
        } else {
            GenericaMensagem.info("Sistema", "Nenhum relatório encontrado!");
            return;
        }
        if (relatorios == null) {
            return;
        }
        String detalheRelatorio = "";
        Integer idResponsavel = null;
        Integer idAluno = null;
        Integer idFilial = null;
        Integer idStatus = null;
        Integer idTurma = null;
        Integer idMidia = null;
        Integer idProfessor = null;
        Integer idVendedor = null;
        Integer idCursoOuTurma = null;
        String dataMatricula[] = new String[]{"", ""};
        String periodo[] = new String[]{"", ""};
        String nascimento[] = new String[]{"", ""};
        String sexoString = null;
        List listDetalhePesquisa = new ArrayList();
        if (filtro[1]) {
            dataMatricula[0] = DataHoje.converteData(dataMatriculaInicial);
            dataMatricula[1] = DataHoje.converteData(dataMatriculaFinal);
            listDetalhePesquisa.add(" Período de matrícula: " + dataMatricula[0] + " e " + dataMatricula[1]);
        }
        if (filtro[2]) {
            periodo[0] = DataHoje.converteData(dataInicial);
            periodo[1] = DataHoje.converteData(dataFinal);
            listDetalhePesquisa.add(" Período do curso: " + periodo[0] + " e " + periodo[1]);
        }
        if (filtro[3]) {
            nascimento[0] = DataHoje.converteData(dataNascimentoInicial);
            nascimento[1] = DataHoje.converteData(dataNascimentoFinal);
            listDetalhePesquisa.add(" Período de nascimento: " + nascimento[0] + " e " + nascimento[1]);
        }
        if (filtro[9]) {
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
        if (responsavel.getId() != -1) {
            idResponsavel = responsavel.getId();
            listDetalhePesquisa.add("Empresa: " + responsavel.getDocumento() + " - " + responsavel.getNome());
        }
        if (aluno.getId() != -1) {
            idAluno = aluno.getPessoa().getId();
            listDetalhePesquisa.add("Funcionário: " + aluno.getPessoa().getDocumento() + " - " + aluno.getPessoa().getNome());
        }
        if (turma.getId() != -1) {
            idCursoOuTurma = turma.getId();
            listDetalhePesquisa.add("Turma: " + aluno.getPessoa().getDocumento() + " - " + aluno.getPessoa().getNome());
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
            idMidia = Integer.parseInt(listSelectItem[3].get(index[3]).getDescription());
            listDetalhePesquisa.add("Mídia: " + ((Midia) dao.find(new Midia(), idMidia)).getDescricao());
        }
        if (index[4] != null) {
            idProfessor = Integer.parseInt(listSelectItem[4].get(index[4]).getDescription());
            listDetalhePesquisa.add("Professor: " + ((Professor) dao.find(new Professor(), idProfessor)).getProfessor().getNome());
        }
        if (index[5] != null) {
            idVendedor = Integer.parseInt(listSelectItem[5].get(index[5]).getDescription());
            listDetalhePesquisa.add("Vendedor: " + ((Vendedor) dao.find(new Vendedor(), idVendedor)).getPessoa().getNome());
        }
        if (tipoMatricula) {
            if (turma.getId() != -1) {
                idCursoOuTurma = turma.getId();
            }
        } else {
            if (index[6] != null) {
                idCursoOuTurma = Integer.parseInt(listSelectItem[6].get(index[6]).getDescription());
                listDetalhePesquisa.add("Curso: " + ((Servicos) dao.find(new Servicos(), idCursoOuTurma)).getDescricao());
            }
        }
        if (order == null) {
            order = "";
        }
        RelatorioMatriculaEscolaDao relatorioMatriculaEscolaDao = new RelatorioMatriculaEscolaDao();
        relatorioMatriculaEscolaDao.setOrder(order);
        List list = relatorioMatriculaEscolaDao.find(relatorios, idFilial, dataMatricula, periodo, nascimento, idStatus, idMidia, idProfessor, idVendedor, tipoMatricula, idCursoOuTurma, idAluno, sexo, idResponsavel, horario);
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
        List<ParametroEscolaCadastral> pec = new ArrayList<>();
        String ini;
        String ter;
        String nasc;
        for (Object list1 : list) {
            nasc = AnaliseString.converteNullString(((List) list1).get(1));
            if (!nasc.isEmpty()) {
                nasc = DataHoje.converteData(DataHoje.converteDateSqlToDate(nasc));
            }
            ini = AnaliseString.converteNullString(((List) list1).get(5));
            if (!ini.isEmpty()) {
                ini = DataHoje.converteData(DataHoje.converteDateSqlToDate(ini));
            }
            ter = AnaliseString.converteNullString(((List) list1).get(6));
            if (!ter.isEmpty()) {
                ter = DataHoje.converteData(DataHoje.converteDateSqlToDate(ter));
            }
            pec.add(new ParametroEscolaCadastral(
                    AnaliseString.converteNullString(((List) list1).get(0)), // ALUNO
                    nasc, // NASCIMENTO
                    AnaliseString.converteNullString(((List) list1).get(2)), // STATUS
                    AnaliseString.converteNullString(((List) list1).get(4)), // CURSO
                    AnaliseString.converteNullString(((List) list1).get(3)), // STATUS
                    ini, // INICIO
                    ter, // TÉRMINO
                    AnaliseString.converteNullString(((List) list1).get(7)) // CÓDIGO SÓCIO
            ));
        }
        Map map = new HashMap();
        map.put("detalhes_relatorio", detalheRelatorio);
        map.put("tipo_matricula", (" Matrícula " + title).toUpperCase());
        if (!pec.isEmpty()) {
            Jasper.TYPE = "paisagem";
            Jasper.IS_HEADER = printHeader;
            Jasper.printReports(relatorios.getJasper(), "matricula_escola", (Collection) pec, map);
        }
    }

    public List<SelectItem> getListTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list;
            if (tipoMatricula) {
                list = (List<Relatorios>) db.pesquisaTipoRelatorio(282);
            } else {
                list = (List<Relatorios>) db.pesquisaTipoRelatorio(283);

            }
            for (int i = 0; i < list.size(); i++) {
                if (index[0] == null) {
                    if (i == 0) {
                        index[0] = i;
                    }
                }
                listSelectItem[0].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListRelatorioOrdem() {
        listSelectItem[1].clear();
        if (index[0] != null) {
            RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
            List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(Integer.parseInt(getListTipoRelatorios().get(index[0]).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
        }
        return listSelectItem[1];
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

    public void selecionaPeriodoMatriculaInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataMatriculaInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaPeriodoMatriculaFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataMatriculaFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaNascimentoInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataNascimentoInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaNascimentoFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataNascimentoFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void clear() {
        if (!filtro[0]) {
            listSelectItem[1] = new ArrayList();
            index[1] = null;
        }
        if (!filtro[1]) {
            dataMatriculaInicial = DataHoje.dataHoje();
            dataMatriculaFinal = null;
        }
        if (!filtro[2]) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = null;
        }
        if (!filtro[3]) {
            dataNascimentoInicial = DataHoje.dataHoje();
            dataNascimentoFinal = null;
        }
        if (!filtro[4]) {
            listSelectItem[2] = new ArrayList();
            index[2] = null;
        }
        if (!filtro[5]) {
            listSelectItem[3] = new ArrayList();
            index[3] = null;
        }
        if (!filtro[6]) {
            listSelectItem[4] = new ArrayList();
            index[4] = null;
        }
        if (!filtro[7]) {
            listSelectItem[5] = new ArrayList();
            index[5] = null;
        }
        if (!filtro[8]) {
            aluno = new Fisica();
            disabled[0] = false;
            disabled[1] = false;
        }
        if (!filtro[9]) {
            sexo = "";
        }
        if (!filtro[10]) {
            responsavel = new Pessoa();
            disabled[0] = false;
            disabled[1] = false;
        }
        if (!filtro[12]) {
            turma = new Turma();
        }
        if (!filtro[13]) {
            listSelectItem[5] = new ArrayList();
            index[5] = null;
        }
        if (!filtro[14]) {
            horario[0] = "";
            horario[1] = "";
        }
        if (!filtro[15]) {
            order = "";
        }
        if (filtro[8]) {
            disabled[0] = false;
            disabled[1] = true;
        } else if (filtro[10]) {
            disabled[0] = true;
            disabled[1] = false;
        }
    }

    public void close(String close) {
        switch (close) {
            case "filial":
                filtro[0] = false;
                listSelectItem[1] = new ArrayList();
                index[1] = null;
                break;
            case "matricula":
                filtro[1] = false;
                dataMatriculaInicial = DataHoje.dataHoje();
                dataMatriculaFinal = null;
                break;
            case "periodo":
                filtro[2] = false;
                dataInicial = DataHoje.dataHoje();
                dataFinal = null;
                break;
            case "nascimento":
                filtro[3] = false;
                dataNascimentoInicial = DataHoje.dataHoje();
                dataNascimentoFinal = null;
                break;
            case "status":
                filtro[4] = false;
                listSelectItem[2] = new ArrayList();
                index[2] = null;
                break;
            case "midia":
                filtro[5] = false;
                listSelectItem[3] = new ArrayList();
                index[3] = null;
                break;
            case "professor":
                filtro[6] = false;
                listSelectItem[4] = new ArrayList();
                index[4] = null;
                break;
            case "vendedor":
                filtro[7] = false;
                listSelectItem[5] = new ArrayList();
                index[5] = null;
                break;
            case "aluno":
                filtro[8] = false;
                aluno = new Fisica();
                break;
            case "sexo":
                filtro[9] = false;
                sexo = "";
                break;
            case "responsavel":
                filtro[10] = false;
                responsavel = new Pessoa();
                break;
            case "turma":
                filtro[12] = false;
                turma = new Turma();
                disabled[0] = false;
                disabled[1] = false;
                break;
            case "curso":
                filtro[13] = false;
                listSelectItem[6] = new ArrayList();
                index[6] = null;
                disabled[0] = false;
                disabled[1] = false;
                break;
            case "horario":
                filtro[14] = false;
                horario[0] = "";
                horario[1] = "";
                break;
            case "order":
                filtro[15] = false;
                order = "";
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
     * <li>[1] Filial</li>
     * <li>[2] Status </li>
     * <li>[3] Mídia </li>
     * <li>[4] Professor </li>
     * <li>[5] Vendedor </li>
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
     * <li>[0] FILIAL</li>
     * <li>[1] PERÍODO MATRÍCULA</li>
     * <li>[2] PERÍODO</li>
     * <li>[3] NASCIMENTO</li>
     * <li>[4] STATUS</li>
     * <li>[5] MÍDIA </li>
     * <li>[6] PROFESSOR </li>
     * <li>[7] VENDEDOR </li>
     * <li>[8] ALUNO </li>
     * <li>[9] SEXO </li>
     * <li>[10] RESPONSÁVEL </li>
     * <li>[14] HORÁRIO </li>
     * <li>[15] ORDER </li>
     * <li>[16] TURMA </li>
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

    public Fisica getAluno() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            aluno = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true));
        }
        return aluno;
    }

    public void setAluno(Fisica aluno) {
        this.aluno = aluno;
    }

    public Pessoa getResponsavel() {
        if (GenericaSessao.exists("pesssoaPesquisa")) {
            responsavel = (Pessoa) GenericaSessao.getObject("pesssoaPesquisa", true);
        }
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
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

    public List<SelectItem> getListFiliais() {
        if (listSelectItem[1].isEmpty()) {
            Dao dao = new Dao();
            List<Filial> list = (List<Filial>) dao.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i,
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[1];
    }

    public List<SelectItem> getListStatus() {
        if (listSelectItem[2].isEmpty()) {
            Dao dao = new Dao();
            List<EscStatus> list = (List<EscStatus>) dao.list(new EscStatus());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[2].add(new SelectItem(i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[2];
    }

    public List<SelectItem> getListMidia() {
        if (listSelectItem[3].isEmpty()) {
            Dao dao = new Dao();
            List<Midia> list = (List<Midia>) dao.list(new Midia(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[3].add(new SelectItem(i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[3];
    }

    public List<SelectItem> getListProfessor() {
        if (listSelectItem[4].isEmpty()) {
            Dao dao = new Dao();
            List<Professor> list = (List<Professor>) dao.list(new Professor(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[4].add(new SelectItem(i,
                        list.get(i).getProfessor().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[4];
    }

    public List<SelectItem> getListVendedor() {
        if (listSelectItem[5].isEmpty()) {
            Dao di = new Dao();
            List<Vendedor> list = (List<Vendedor>) di.list(new Vendedor(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[5].add(new SelectItem(i,
                        list.get(i).getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[5];
    }

    public List<SelectItem> getListCursos() {
        if (listSelectItem[6].isEmpty()) {
            MatriculaEscolaDao med = new MatriculaEscolaDao();
            List<Servicos> list = med.listServicosPorMatriculaIndividual();
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[6].add(new SelectItem(i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[6];
    }

    public Boolean getPrintHeader() {
        return printHeader;
    }

    public void setPrintHeader(Boolean printHeader) {
        this.printHeader = printHeader;
    }

    public Boolean[] getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean[] disabled) {
        this.disabled = disabled;
    }

    public Boolean getSocios() {
        return socios;
    }

    public void setSocios(Boolean socios) {
        this.socios = socios;
    }

    public void listener(String tCase) {
        switch (tCase) {
            case "matriculaTurma":
                title = "Turma";
                tipoMatricula = true;
                break;
            case "matriculaIndividual":
                title = "Individual";
                tipoMatricula = false;
                break;
        }
        GenericaSessao.put("title", title);
        GenericaSessao.put("tipoMatricula", tipoMatricula);
    }

    public Date getDataMatriculaInicial() {
        return dataMatriculaInicial;
    }

    public void setDataMatriculaInicial(Date dataMatriculaInicial) {
        this.dataMatriculaInicial = dataMatriculaInicial;
    }

    public Date getDataMatriculaFinal() {
        return dataMatriculaFinal;
    }

    public void setDataMatriculaFinal(Date dataMatriculaFinal) {
        this.dataMatriculaFinal = dataMatriculaFinal;
    }

    public Date getDataNascimentoInicial() {
        return dataNascimentoInicial;
    }

    public void setDataNascimentoInicial(Date dataNascimentoInicial) {
        this.dataNascimentoInicial = dataNascimentoInicial;
    }

    public Date getDataNascimentoFinal() {
        return dataNascimentoFinal;
    }

    public void setDataNascimentoFinal(Date dataNascimentoFinal) {
        this.dataNascimentoFinal = dataNascimentoFinal;
    }

    public String[] getHorario() {
        return horario;
    }

    public void setHorario(String[] horario) {
        this.horario = horario;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Turma getTurma() {
        if (GenericaSessao.exists("turmaPesquisa")) {
            turma = (Turma) GenericaSessao.getObject("turmaPesquisa", true);
        }
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Boolean getTipoMatricula() {
        return tipoMatricula;
    }

    public void setTipoMatricula(Boolean tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }
}
