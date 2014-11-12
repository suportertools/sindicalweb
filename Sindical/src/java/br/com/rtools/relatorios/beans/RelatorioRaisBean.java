package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.dao.RaisDao;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.impressao.ParametroRaisRelatorio;
import br.com.rtools.pessoa.ClassificacaoEconomica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.Raca;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.File;
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
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioRaisBean implements Serializable {

    private SisPessoa sisPessoa;
    private Juridica empresa;
    private List selectedCidades;
    private List selectedProfissoes;
    private List selectedFaixaSalarial;
    private List selectedRaca;
    private List selectedClassificaoEconomica;
    private List<SelectItem>[] listSelectItem;
    private List<ParametroRaisRelatorio> parametroRaisRelatorio;
    private Map<String, Integer> listCidades;
    private Map<String, Integer> listProfissoes;
    private Map<String, Integer> listRaca;
    private Map<String, Integer> listClassificacaoEconomica;
    private String[] faixaSalarial;
    private Boolean[] filtro;
    private Date dataInicial;
    private Date dataFinal;
    private Integer[] index;
    private String tipoRelatorio;
    private String indexAccordion;
    private String porPesquisa;
    private String descPorPesquisa;
    private String order;
    private String anoBase;
    private String sexo;

    @PostConstruct
    public void init() {
        anoBase = "";
        filtro = new Boolean[11];
        filtro[0] = false; // ANO BASE
        filtro[1] = false; // PERÍODO EMISSÃO
        filtro[2] = false; // EMPRESA
        filtro[3] = false; // EMPREGADO
        filtro[4] = false; // PROFISSÃO
        filtro[5] = false; // FAIXA SALARIAL
        filtro[6] = false; // RAÇA
        filtro[7] = false; // CLASSIFICAÇÃO ECONÕMICA
        filtro[8] = false; // CIDADE (EMPRESA)
        filtro[9] = false; // SEXO
        filtro[10] = false; // ORDENAÇÃO
        listCidades = null;
        listProfissoes = null;
        faixaSalarial = new String[2];
        faixaSalarial[0] = null;
        faixaSalarial[1] = null;
        listRaca = null;
        listClassificacaoEconomica = null;
        selectedCidades = new ArrayList<>();
        selectedProfissoes = new ArrayList<>();
        selectedFaixaSalarial = new ArrayList<>();
        selectedRaca = new ArrayList<>();
        selectedClassificaoEconomica = new ArrayList<>();
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        parametroRaisRelatorio = new ArrayList<>();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        index = new Integer[2];
        index[0] = 0;
        index[1] = 0;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        porPesquisa = "";
        descPorPesquisa = "";
        order = "";
        sisPessoa = new SisPessoa();
        empresa = new Juridica();
        sexo = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioRaisBean");
        GenericaSessao.remove("sisPessoaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void visualizar() {
        Relatorios relatorios = null;
        if (!getListaTipoRelatorios().isEmpty()) {
            RelatorioGenericoDB rgdb = new RelatorioGenericoDBToplink();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
        }
        if (relatorios == null) {
            return;
        }
        String detalheRelatorio = "";
        if (parametroRaisRelatorio.isEmpty()) {
            RaisDao raisDao = new RaisDao();
            Integer idEmpresa = null;
            Integer idSisPessoa = null;
            String pIStringI = "";
            String pFStringI = "";
            String referencia = "";
            String dReferencia = "";
            String inIdProfissoes = inIdProfissoes();
            String inIdRaca = inIdRaca();
            String inIdClassificaoEconomica = inIdClassificaoEconomica();
            String inIdCidades = inIdCidades();
            List listDetalhePesquisa = new ArrayList();
            if (filtro[0]) {
                listDetalhePesquisa.add(" Ano Base: " + anoBase);
            }
            if (filtro[1]) {
                pIStringI = DataHoje.converteData(dataInicial);
                pFStringI = DataHoje.converteData(dataFinal);
                listDetalhePesquisa.add(" Período de Emissão entre " + pIStringI + " e " + pFStringI);
            }
            if (!dReferencia.isEmpty()) {
                listDetalhePesquisa.add(" Período convenção: " + dReferencia + "");
            }
            if (sisPessoa.getId() != -1) {
                idSisPessoa = sisPessoa.getId();
                listDetalhePesquisa.add(" Pessoa CPF: " + sisPessoa.getDocumento() + " - " + sisPessoa.getNome());
            }
            if (empresa.getId() != -1) {
                idEmpresa = empresa.getId();
                listDetalhePesquisa.add(" Pessoa Jurídica por " + porPesquisa + ". CNPJ: " + empresa.getPessoa().getDocumento() + " - " + empresa.getPessoa().getNome());
            }
            String orderString = "";
            if (order != null) {
                if (order.equals("0")) {
                    orderString = "SP.ds_nome ASC, R.nr_ano_base ASC, R.dt_emissao ASC ";
                } else if (orderString.equals("1")) {
                    orderString = " PJ.ds_nome ASC, R.nr_ano_base ASC, R.dt_emissao ASC ";
                } else if (orderString.equals("2")) {
                    orderString = " R.dt_emissao ASC, SP.ds_nome ASC ";
                } else if (orderString.equals("3")) {
                    orderString = " R.dt_emissao ASC, PJ.ds_nome ASC ";
                }
            } else {
                orderString = "";
            }
            List list = raisDao.filtroRelatorio(relatorios, anoBase, pIStringI, pFStringI, idEmpresa, idSisPessoa, inIdProfissoes, faixaSalarial[0], faixaSalarial[1], inIdRaca, inIdClassificaoEconomica, inIdCidades, sexo, orderString);
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
                        detalheRelatorio += "" + listDetalhePesquisa.get(i).toString();
                    } else {
                        detalheRelatorio += "; " + listDetalhePesquisa.get(i).toString();
                    }
                }
            }
            String dt = "";
            String dte = "";
            String dtd = "";
            String dta = "";
            String alvara = "";
            String filiado = "";
            for (Object list1 : list) {
                dt = GenericaString.converterNullToString(((List) list1).get(30));
                dte = GenericaString.converterNullToString(((List) list1).get(4));
                dta = GenericaString.converterNullToString(((List) list1).get(13));
                dtd = GenericaString.converterNullToString(((List) list1).get(11));
                if (!dt.isEmpty()) {
                    dt = DataHoje.converteData(DataHoje.converteDateSqlToDate(dt));
                }
                if (!dte.isEmpty()) {
                    dte = DataHoje.converteData(DataHoje.converteDateSqlToDate(dte));
                }
                if (!dta.isEmpty()) {
                    dta = DataHoje.converteData(DataHoje.converteDateSqlToDate(dta));
                }
                if (!dtd.isEmpty()) {
                    dtd = DataHoje.converteData(DataHoje.converteDateSqlToDate(dtd));
                }
                ParametroRaisRelatorio pr
                        = new ParametroRaisRelatorio(
                                detalheRelatorio,
                                AnaliseString.converteNullString(((List) list1).get(0)),
                                AnaliseString.converteNullString(((List) list1).get(1)),
                                dte,
                                AnaliseString.converteNullString(((List) list1).get(3)),
                                AnaliseString.converteNullString(((List) list1).get(5)),
                                AnaliseString.converteNullString(((List) list1).get(6)),
                                AnaliseString.converteNullString(((List) list1).get(7)),
                                AnaliseString.converteNullString(((List) list1).get(8)),
                                AnaliseString.converteNullString(((List) list1).get(9)),
                                AnaliseString.converteNullString(((List) list1).get(10)),
                                dtd,
                                AnaliseString.converteNullString(((List) list1).get(12)),
                                dta,
                                AnaliseString.converteNullString(((List) list1).get(14)),
                                AnaliseString.converteNullString(((List) list1).get(15)),
                                AnaliseString.converteNullString(((List) list1).get(16)),
                                AnaliseString.converteNullString(((List) list1).get(17)),
                                AnaliseString.converteNullString(((List) list1).get(18)),
                                AnaliseString.converteNullString(((List) list1).get(19)),
                                AnaliseString.converteNullString(((List) list1).get(20)),
                                AnaliseString.converteNullString(((List) list1).get(21)),
                                AnaliseString.converteNullString(((List) list1).get(22)),
                                AnaliseString.converteNullString(((List) list1).get(23)),
                                AnaliseString.converteNullString(((List) list1).get(24)),
                                AnaliseString.converteNullString(((List) list1).get(25)),
                                AnaliseString.converteNullString(((List) list1).get(26)),
                                AnaliseString.converteNullString(((List) list1).get(27)),
                                AnaliseString.converteNullString(((List) list1).get(28)),
                                AnaliseString.converteNullString(((List) list1).get(29)),
                                AnaliseString.converteNullString(dt),
                                AnaliseString.converteNullString(((List) list1).get(31))
                        );
                parametroRaisRelatorio.add(pr);
            }

        }
        if (!Diretorio.criar("Arquivos/downloads/relatorios/rais")) {
            GenericaMensagem.info("Sistema", "Erro ao criar diretório!");
            return;
        }
        try {
            JasperReport jasper;
            FacesContext faces = FacesContext.getCurrentInstance();
            if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + relatorios.getJasper())).exists()) {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + relatorios.getJasper())));
            } else {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper())));
            }
            try {
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource((Collection) parametroRaisRelatorio);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                String nomeDownload = "relatorio_rais_sintetico_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/rais/");
                salvaArquivos.salvaNaPasta(pathPasta);
                Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                download.baixar();
                download.remover();
            } catch (JRException erro) {
                GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
            GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(271);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
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
            limpar();
            filtro[2] = false;
            filtro[0] = false;
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

    public void limpar() {
        if (!filtro[0]) {
            anoBase = "";
        }
        if (!filtro[1]) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
        }
        if (!filtro[2]) {
            empresa = new Juridica();
        }
        if (!filtro[3]) {
            sisPessoa = new SisPessoa();
        }
        if (!filtro[4]) {
            selectedProfissoes = null;
        }
        if (!filtro[5]) {
            faixaSalarial = new String[2];
            faixaSalarial[0] = null;
            faixaSalarial[1] = null;
        }
        if (!filtro[6]) {
            selectedRaca = null;
        }
        if (!filtro[7]) {
            selectedClassificaoEconomica = null;
        }
        if (!filtro[8]) {
            selectedCidades = null;
        }
        if (!filtro[9]) {
            sexo = "";
        }
        if (!filtro[10]) {
            order = "";
        }
    }

    public void close(String close) {
        switch (close) {
            case "anoBase":
                anoBase = "";
                filtro[0] = false;
                break;
            case "periodoEmissao":
                filtro[1] = false;
                dataInicial = DataHoje.dataHoje();
                dataFinal = DataHoje.dataHoje();
                break;
            case "empresa":
                empresa = new Juridica();
                filtro[2] = false;
                break;
            case "empregado":
                sisPessoa = new SisPessoa();
                filtro[3] = false;
                break;
            case "profissao":
                listProfissoes = null;
                selectedProfissoes = null;
                filtro[4] = false;
                break;
            case "faixaSalarial":
                faixaSalarial = new String[2];
                faixaSalarial[0] = null;
                faixaSalarial[1] = null;
                filtro[5] = false;
                break;
            case "raca":
                listRaca = null;
                selectedRaca = null;
                filtro[6] = false;
                break;
            case "classificaoEconomica":
                listClassificacaoEconomica = null;
                selectedClassificaoEconomica = null;
                filtro[7] = false;
                break;
            case "cidadeEmpresa":
                selectedCidades = null;
                listCidades = null;
                filtro[8] = false;
                break;
            case "sexo":
                sexo = "";
                filtro[9] = false;
                break;
            case "order":
                order = "";
                filtro[10] = false;
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
     * <li>[0] ANO BASE</li>
     * <li>[1] PERÍODO EMISSÃO</li>
     * <li>[2] EMPRESA</li>
     * <li>[3] EMPREGADO</li>
     * <li>[4] PROFISSÃO</li>
     * <li>[5] FAIXA SALARIAL</li>
     * <li>[6] RAÇA</li>
     * <li>[7] CLASSIFICAÇÃO ECONÕMICA</li>
     * <li>[8] CIDADE (EMPRESA)</li>
     * <li>[9] SEXO</li>
     * <li>[10] ORDENAÇÃO </li>
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

    public SisPessoa getSisPessoa() {
        if (GenericaSessao.exists("sisPessoaPesquisa")) {
            GenericaSessao.remove("sisPessoaBean");
            sisPessoa = (SisPessoa) GenericaSessao.getObject("sisPessoaPesquisa", true);
        }
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
    }

    public Juridica getEmpresa() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            GenericaSessao.remove("juridicaBean");
            empresa = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
        }
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }

    public List getSelectedCidades() {
        return selectedCidades;
    }

    public void setSelectedCidades(List selectedCidades) {
        this.selectedCidades = selectedCidades;
    }

    public List getSelectedProfissoes() {
        return selectedProfissoes;
    }

    public void setSelectedProfissoes(List selectedProfissoes) {
        this.selectedProfissoes = selectedProfissoes;
    }

    public List getSelectedFaixaSalarial() {
        return selectedFaixaSalarial;
    }

    public void setSelectedFaixaSalarial(List selectedFaixaSalarial) {
        this.selectedFaixaSalarial = selectedFaixaSalarial;
    }

    public List getSelectedRaca() {
        return selectedRaca;
    }

    public void setSelectedRaca(List selectedRaca) {
        this.selectedRaca = selectedRaca;
    }

    public List getSelectedClassificaoEconomica() {
        return selectedClassificaoEconomica;
    }

    public void setSelectedClassificaoEconomica(List selectedClassificaoEconomica) {
        this.selectedClassificaoEconomica = selectedClassificaoEconomica;
    }

    public List<ParametroRaisRelatorio> getParametroRaisRelatorio() {
        return parametroRaisRelatorio;
    }

    public void setParametroRaisRelatorio(List<ParametroRaisRelatorio> parametroRaisRelatorio) {
        this.parametroRaisRelatorio = parametroRaisRelatorio;
    }

    public Map<String, Integer> getListCidades() {
        if (listCidades == null) {
            listCidades = new HashMap<>();
            RaisDao raisDao = new RaisDao();
            List<Cidade> list = (List<Cidade>) raisDao.pesquisaTodasCidades();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    listCidades.put(list.get(i).getCidade(), list.get(i).getId());
                }
            }
        }
        return listCidades;
    }

    public void setListCidades(Map<String, Integer> listCidades) {
        this.listCidades = listCidades;
    }

    public Map<String, Integer> getListProfissoes() {
        if (listProfissoes == null) {
            listProfissoes = new HashMap<>();
            RaisDao raisDao = new RaisDao();
            List<Profissao> list = (List<Profissao>) raisDao.pesquisaTodasProfissoes();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    listProfissoes.put(list.get(i).getProfissao(), list.get(i).getId());
                }
            }
        }
        return listProfissoes;
    }

    public void setListProfissoes(Map<String, Integer> listProfissoes) {
        this.listProfissoes = listProfissoes;
    }

    public Map<String, Integer> getListRaca() {
        if (listRaca == null) {
            listRaca = new HashMap<>();
            Dao dao = new Dao();
            List<Raca> list = (List<Raca>) dao.list(new Raca());
            for (int i = 0; i < list.size(); i++) {
                listRaca.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listRaca;
    }

    public void setListRaca(Map<String, Integer> listRaca) {
        this.listRaca = listRaca;
    }

    public Map<String, Integer> getListClassificacaoEconomica() {
        if (listClassificacaoEconomica == null) {
            listClassificacaoEconomica = new HashMap<>();
            Dao dao = new Dao();
            List<ClassificacaoEconomica> list = (List<ClassificacaoEconomica>) dao.list(new ClassificacaoEconomica(), true);
            for (int i = 0; i < list.size(); i++) {
                listClassificacaoEconomica.put(list.get(i).getDescricao() + " - Faixa: " + list.get(i).getSalarioMinimoInicial() + " - " + list.get(i).getSalarioMinimoFinal(), list.get(i).getId());
            }
        }
        return listClassificacaoEconomica;
    }

    public void setListClassificacaoEconomica(Map<String, Integer> listClassificacaoEconomica) {
        this.listClassificacaoEconomica = listClassificacaoEconomica;
    }

    public String inIdCidades() {
        String ids = null;
        if (selectedCidades != null) {
            for (int i = 0; i < selectedCidades.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedCidades.get(i);
                } else {
                    ids += "," + selectedCidades.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdProfissoes() {
        String ids = null;
        if (selectedProfissoes != null) {
            for (int i = 0; i < selectedProfissoes.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedProfissoes.get(i);
                } else {
                    ids += "," + selectedProfissoes.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdRaca() {
        String ids = null;
        if (selectedRaca != null) {
            for (int i = 0; i < selectedRaca.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedRaca.get(i);
                } else {
                    ids += "," + selectedRaca.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdClassificaoEconomica() {
        String ids = null;
        if (selectedClassificaoEconomica != null) {
            for (int i = 0; i < selectedClassificaoEconomica.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedClassificaoEconomica.get(i);
                } else {
                    ids += "," + selectedClassificaoEconomica.get(i);
                }
            }
        }
        return ids;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String[] getFaixaSalarial() {
        return faixaSalarial;
    }

    public void setFaixaSalarial(String[] faixaSalarial) {
        this.faixaSalarial = faixaSalarial;
    }

    public String getAnoBase() {
        return anoBase;
    }

    public void setAnoBase(String anoBase) {
        this.anoBase = anoBase;
    }
}
