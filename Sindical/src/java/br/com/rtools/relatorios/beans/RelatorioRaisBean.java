package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.dao.RaisDao;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.impressao.ParametroRaisNaoEnviadasRelatorio;
import br.com.rtools.impressao.ParametroRaisRelatorio;
import br.com.rtools.pessoa.ClassificacaoEconomica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.Raca;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.sistema.beans.UploadFilesBean;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.MemoryFile;
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
    private Juridica escritorio;
    private List selectedCidades;
    private List selectedProfissoes;
    private List selectedFaixaSalarial;
    private List selectedRaca;
    private List selectedClassificaoEconomica;
    private List<SelectItem>[] listSelectItem;
    private List<ParametroRaisRelatorio> parametroRaisRelatorio;
    private List<ParametroRaisNaoEnviadasRelatorio> parametroRaisNaoEnviadasRelatorio;
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
    private String tipo;
    private String indexAccordion;
    private String porPesquisa;
    private String descPorPesquisa;
    private String order;
    private String anoBase;
    private String sexo;
    private String assunto;
    private String mensagem;
    private Boolean raisEnviadas;
    private ParametroRaisNaoEnviadasRelatorio[] selected;

    @PostConstruct
    public void init() {
        anoBase = "";
        filtro = new Boolean[14];
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
        filtro[11] = false; // ESCRITÓRIO
        filtro[12] = false; // --
        filtro[13] = false; // COM EMAILS
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
        escritorio = new Juridica();
        sexo = "";
        raisEnviadas = false;
        parametroRaisNaoEnviadasRelatorio = new ArrayList<>();
        selected = null;
        tipo = "todos";
        assunto = "RAIS";
        mensagem = "";
        SegurancaUtilitariosBean sub = new SegurancaUtilitariosBean();
        UploadFilesBean uploadFilesBean = new UploadFilesBean("Arquivos/anexos/pendentes/rais");
        GenericaSessao.put("uploadFilesBean", uploadFilesBean);
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioRaisBean");
        GenericaSessao.remove("sisPessoaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("tipoPesquisaPessoaJuridica");
        GenericaSessao.remove("uploadFilesBean");
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
            Integer idEscritorio = null;
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
                if (anoBase.isEmpty()) {
                    GenericaMensagem.warn("Validação", "Informar o ano base");
                    return;
                }
            }
            if (filtro[8]) {
                if (inIdCidades.isEmpty()) {
                    GenericaMensagem.warn("Validação", "Selecionar pelo menos uma cidade!");
                    return;
                }
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
            } else {
                if (filtro[2]) {
                    idEmpresa = -1;
                    listDetalhePesquisa.add(" Pessoa Empresa por " + porPesquisa + ". Todas.");
                }
            }
            boolean escritorios = false;
            if (escritorio.getId() != -1) {
                idEscritorio = escritorio.getId();
                escritorios = true;
                listDetalhePesquisa.add(" Pessoa Escritório por " + porPesquisa + ". CNPJ: " + escritorio.getPessoa().getDocumento() + " - " + escritorio.getPessoa().getNome());
            } else {
                if (filtro[11]) {
                    escritorios = true;
                    idEscritorio = -1;
                    listDetalhePesquisa.add(" Pessoa Escritório por " + porPesquisa + ". Todos.");
                }
            }
            String orderString = "";
            if (order != null) {
                if (order.equals("0")) {
                    orderString = " SP.ds_nome ASC, R.nr_ano_base ASC, R.dt_emissao ASC ";
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
            List list;
            if (raisEnviadas) {
                list = raisDao.filtroRelatorio(relatorios, anoBase, pIStringI, pFStringI, idEmpresa, idSisPessoa, inIdProfissoes, faixaSalarial[0], faixaSalarial[1], inIdRaca, inIdClassificaoEconomica, inIdCidades, sexo, orderString);
            } else {
                list = raisDao.filtroRelatorioNaoEnviadas(relatorios, anoBase, idEmpresa, idEscritorio, inIdCidades, "", tipo, escritorios);
            }
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
            ParametroRaisRelatorio pr;
            ParametroRaisNaoEnviadasRelatorio prne;
            for (Object list1 : list) {
                if (raisEnviadas) {
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
                    pr = new ParametroRaisRelatorio(
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
                } else {
                    if (escritorios) {
                        Integer quantidade = 0;
                        try {
                            quantidade = Integer.parseInt(AnaliseString.converteNullString(((List) list1).get(4)));
                        } catch (Exception e) {
                            quantidade = 0;
                        }
                        prne = new ParametroRaisNaoEnviadasRelatorio(
                                AnaliseString.converteNullString(((List) list1).get(0)),
                                AnaliseString.converteNullString(((List) list1).get(1)),
                                AnaliseString.converteNullString(((List) list1).get(2)),
                                AnaliseString.converteNullString(((List) list1).get(3)),
                                quantidade
                        );

                    } else {
                        prne = new ParametroRaisNaoEnviadasRelatorio(
                                detalheRelatorio,
                                AnaliseString.converteNullString(((List) list1).get(0)),
                                AnaliseString.converteNullString(((List) list1).get(1)),
                                AnaliseString.converteNullString(((List) list1).get(2)),
                                AnaliseString.converteNullString(((List) list1).get(3)),
                                AnaliseString.converteNullString(((List) list1).get(4)),
                                AnaliseString.converteNullString(((List) list1).get(5)),
                                AnaliseString.converteNullString(((List) list1).get(6)),
                                AnaliseString.converteNullString(((List) list1).get(7)),
                                AnaliseString.converteNullString(((List) list1).get(8)),
                                AnaliseString.converteNullString(((List) list1).get(9)),
                                AnaliseString.converteNullString(((List) list1).get(10)),
                                AnaliseString.converteNullString(((List) list1).get(11)),
                                AnaliseString.converteNullString(((List) list1).get(12)),
                                AnaliseString.converteNullString(((List) list1).get(13)),
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
                                0
                        );
                    }
                    parametroRaisNaoEnviadasRelatorio.add(prne);
                }
            }
            if (raisEnviadas) {
                imprimir((Collection) parametroRaisRelatorio, relatorios);
            }
        }
    }

    public void imprimir() {
        if (!parametroRaisNaoEnviadasRelatorio.isEmpty()) {
            Relatorios r = null;
            if (!getListaTipoRelatorios().isEmpty()) {
                RelatorioGenericoDB rgdb = new RelatorioGenericoDBToplink();
                r = rgdb.pesquisaRelatorios(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
            }
            if (r == null) {
                return;
            }
            imprimir((Collection) parametroRaisNaoEnviadasRelatorio, r);
            // parametroRaisNaoEnviadasRelatorio.clear();
        }
    }

    public void imprimir(Collection c, Relatorios r) {
        if (!Diretorio.criar("Arquivos/downloads/relatorios/rais")) {
            GenericaMensagem.info("Sistema", "Erro ao criar diretório!");
        }
        try {
            JasperReport jasper;
            FacesContext faces = FacesContext.getCurrentInstance();
            if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + r.getJasper())).exists()) {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + r.getJasper())));
            } else {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(r.getJasper())));
            }
            try {
                JRBeanCollectionDataSource dtSource = null;
                if (raisEnviadas) {
                    dtSource = new JRBeanCollectionDataSource(c);
                } else {
                    dtSource = new JRBeanCollectionDataSource(c);
                }
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                String nomeDownload = "relatorio_rais_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
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
            List<Relatorios> list;
            if (getRaisEnviadas()) {
                list = (List<Relatorios>) db.pesquisaTipoRelatorio(273);
            } else {
                list = (List<Relatorios>) db.pesquisaTipoRelatorio(274);
            }
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
            clear();
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

    public void switchFilter() {
        if (this.filtro[2]) {
            this.filtro[11] = false;
            escritorio = new Juridica();
        } else if (this.filtro[11]) {
            this.filtro[2] = false;
            empresa = new Juridica();
        }
        clear();
    }

    public void clear() {
        parametroRaisNaoEnviadasRelatorio.clear();
        selected = null;
        if (!filtro[0]) {
            anoBase = "";
        }
        if (!filtro[1]) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
        }
        if (!filtro[2]) {
            empresa = new Juridica();
            filtro[13] = false;
            tipo = "todos";
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
        if (!filtro[11]) {
            filtro[13] = false;
            escritorio = new Juridica();
            tipo = "todos";
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
                tipo = "todos";
                break;
            case "escritorio":
                escritorio = new Juridica();
                filtro[11] = false;
                filtro[13] = false;
                tipo = "todos";
                break;
            case "empregado":
                filtro[13] = false;
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
        if (GenericaSessao.exists("juridicaPesquisa") && GenericaSessao.exists("tipoPesquisaPessoaJuridica")) {
            if (GenericaSessao.getString("tipoPesquisaPessoaJuridica").equals("contribuintes")) {
                GenericaSessao.remove("juridicaBean");
                GenericaSessao.remove("tipoPesquisaPessoaJuridica");
                empresa = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            }
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

    public void defineTCase(Boolean raisEnviadas) {
        GenericaSessao.put("raisEnviadas", raisEnviadas);
    }

    public Boolean getRaisEnviadas() {
        if (GenericaSessao.exists("raisEnviadas")) {
            raisEnviadas = GenericaSessao.getBoolean("raisEnviadas", true);
        }
        return raisEnviadas;
    }

    public void setRaisEnviadas(Boolean raisEnviadas) {
        this.raisEnviadas = raisEnviadas;
    }

    public Juridica getEscritorio() {
        if (GenericaSessao.exists("juridicaPesquisa") && GenericaSessao.exists("tipoPesquisaPessoaJuridica")) {
            if (GenericaSessao.getString("tipoPesquisaPessoaJuridica").equals("escritorios")) {
                GenericaSessao.remove("juridicaBean");
                GenericaSessao.remove("tipoPesquisaPessoaJuridica");
                escritorio = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            }
        }
        return escritorio;
    }

    public void setEscritorio(Juridica escritorio) {
        this.escritorio = escritorio;
    }

    public List<ParametroRaisNaoEnviadasRelatorio> getParametroRaisNaoEnviadasRelatorio() {
        return parametroRaisNaoEnviadasRelatorio;
    }

    public void setParametroRaisNaoEnviadasRelatorio(List<ParametroRaisNaoEnviadasRelatorio> parametroRaisNaoEnviadasRelatorio) {
        this.parametroRaisNaoEnviadasRelatorio = parametroRaisNaoEnviadasRelatorio;
    }

    public ParametroRaisNaoEnviadasRelatorio[] getSelected() {
        return selected;
    }

    public void setSelected(ParametroRaisNaoEnviadasRelatorio[] selected) {
        this.selected = selected;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void send() {
        Mail mail = new Mail();
        List<MemoryFile> listFiles;
        List<File> files = new ArrayList<>();
        if (GenericaSessao.exists("uploadFilesBean")) {
            listFiles = ((UploadFilesBean) GenericaSessao.getObject("uploadFilesBean")).getListFiles();
            for (int i = 0; i < listFiles.size(); i++) {
                files.add((File) listFiles.get(i).getFile());
            }
        }
        if (filtro[11]) {
            RaisDao raisDao = new RaisDao();
            String empresasString = "";
            List<EmailPessoa> listEmailPessoa = new ArrayList<>();
            EmailPessoa emailPessoa = new EmailPessoa();
            Email email = new Email();
            email.setAssunto("Empresas sem RAIS");
            Dao dao = new Dao();
            email.setRotina((Rotina) dao.find(new Rotina(), 274));
            mail.setEmail(email);
            Juridica juridica = new Juridica();
            String telefoneString = "";
            String emailString = "";
            for (int i = 0; i < selected.length; i++) {
                List listEmpresas = raisDao.filtroRelatorioNaoEnviadas(new Relatorios(), anoBase, null, Integer.parseInt(selected[i].getEscritorio_id()), null, "", "", false);
                for (int j = 0; j < listEmpresas.size(); j++) {
                    telefoneString = AnaliseString.converteNullString(((List) listEmpresas.get(j)).get(4));
                    emailString = AnaliseString.converteNullString(((List) listEmpresas.get(j)).get(12));
                    empresasString += " - " + ((List) listEmpresas.get(j)).get(0).toString() + " - " + ((List) listEmpresas.get(j)).get(1).toString() + " - Telefone: " + telefoneString + " - Email: " + emailString + ";<br />";
                }
                juridica = (Juridica) dao.find(new Juridica(), Integer.parseInt(selected[i].getEscritorio_id()));
                emailPessoa = new EmailPessoa();
                emailPessoa.setPessoa(juridica.getPessoa());
                listEmailPessoa.add(emailPessoa);
                juridica = new Juridica();
            }
            email.setMensagem(mensagem + "<br /><br />" + empresasString);
            mail.setEmailPessoas(listEmailPessoa);
        } else if (filtro[2]) {
            RaisDao raisDao = new RaisDao();
            String empresasString = "";
            List<EmailPessoa> listEmailPessoa = new ArrayList<>();
            EmailPessoa emailPessoa = new EmailPessoa();
            Email email = new Email();
            email.setAssunto(assunto);
            Dao dao = new Dao();
            email.setRotina((Rotina) dao.find(new Rotina(), 274));
            email.setMensagem(mensagem);
            mail.setEmail(email);
            Juridica juridica = new Juridica();
            for (int i = 0; i < selected.length; i++) {
                juridica = (Juridica) dao.find(new Juridica(), Integer.parseInt(selected[i].getId()));
                emailPessoa = new EmailPessoa();
                emailPessoa.setPessoa(juridica.getPessoa());
                listEmailPessoa.add(emailPessoa);
                juridica = new Juridica();
            }
            mail.setEmailPessoas(listEmailPessoa);
            String[] err = mail.send();
            if (!err[0].isEmpty()) {

            } else if (!err[1].isEmpty()) {

            }
        }
        if (!files.isEmpty()) {
            mail.setFiles(files);
        }
        String[] err = mail.send();
        if (!err[0].isEmpty()) {
            GenericaMensagem.info("Sucesso", "Mensagem enviada com sucesso!");
        } else if (!err[1].isEmpty()) {
            GenericaMensagem.warn("Erro", "Ao enviar mensagem." + err[1]);

        }
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
