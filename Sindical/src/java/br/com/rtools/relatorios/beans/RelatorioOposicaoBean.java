package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.db.OposicaoDB;
import br.com.rtools.arrecadacao.db.OposicaoDBToplink;
import br.com.rtools.impressao.ParametroOposicao;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioOposicaoBean implements Serializable {

    private Oposicao oposicao;
    private List<Oposicao> oposicaos;
    private boolean porPeridoEmissao;
    private boolean porPeridoConvencao;
    private boolean porOposicao;
    private String tipoRelatorio;
    private String indexAccordion;
    private Date dataInicial;
    private Date dataFinal;
    private List<SelectItem>[] listSelectItem;
    private List<ParametroOposicao> parametroOposicao;
    private List<ConvencaoPeriodo> listConvencaoPeriodos;
    private int[] index;
    private String porPesquisa;
    private String descPorPesquisa;

    @PostConstruct
    public void init() {
        oposicao = new Oposicao();
        oposicaos = new ArrayList<Oposicao>();
        porPeridoEmissao = false;
        porPeridoConvencao = false;
        porOposicao = false;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<SelectItem>();
        listSelectItem[1] = new ArrayList<SelectItem>();
        parametroOposicao = new ArrayList<ParametroOposicao>();
        listConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
        index = new int[2];
        index[0] = 0;
        index[1] = 0;
        porPesquisa = "";
        descPorPesquisa = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioOposicaoBean");
        GenericaSessao.remove("oposicaoPesquisa");
        GenericaSessao.remove("oposicaoBean");
        GenericaSessao.remove("oposicaoPesquisaPor");
        GenericaSessao.remove("removeFiltro");
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
        if (parametroOposicao.isEmpty()) {
            OposicaoDB oposicaoDB = new OposicaoDBToplink();
            int pPessoaOposicaoI = 0;
            int pEmpresaI = 0;
            String pIStringI = "";
            String pFStringI = "";
            String referencia = "";
            List listDetalhePesquisa = new ArrayList();
            if (porPesquisa.equals("rgs") || porPesquisa.equals("nome") || porPesquisa.equals("cpf")) {
                if (oposicao.getOposicaoPessoa().getId() != -1) {
                    pPessoaOposicaoI = oposicao.getOposicaoPessoa().getId();
                    listDetalhePesquisa.add(" Pessoa Oposição por " + porPesquisa + " ");
                }
            }
            if (porPesquisa.equals("cnpj") || porPesquisa.equals("empresa")) {
                if (oposicao.getOposicaoPessoa().getId() != -1) {
                    pEmpresaI = oposicao.getJuridica().getPessoa().getId();
                    listDetalhePesquisa.add(" Pessoa Jurídica por " + porPesquisa + " ");
                }
            }
            if (porPeridoEmissao) {
                pIStringI = DataHoje.converteData(dataInicial);
                pFStringI = DataHoje.converteData(dataFinal);
                listDetalhePesquisa.add(" Período de Emissão entre " + pIStringI + " e " + pFStringI);
            }
            if (porPeridoConvencao) {
                for (int i = 0; i < listConvencaoPeriodos.size(); i++) {
                    if (i == 0) {
                        referencia = "" + listConvencaoPeriodos.get(i).getId();
                    } else {
                        referencia += "," + listConvencaoPeriodos.get(i).getId();
                    }
                }
            }
            List list = oposicaoDB.filtroRelatorio(pEmpresaI, pPessoaOposicaoI, pIStringI, pFStringI, referencia, relatorios);
            if (listDetalhePesquisa.isEmpty()) {
                detalheRelatorio += "Pesquisar todos registros!";
            } else {
                detalheRelatorio += "Pesquisar registros por: ";
                for (int i = 0; i < listDetalhePesquisa.size(); i++) {
                    if (i == 0) {
                        detalheRelatorio += listDetalhePesquisa.get(i).toString();
                    } else {
                        detalheRelatorio += "," + listDetalhePesquisa.get(i).toString();
                    }
                }
            }
            String dt = "";
            String dat = "";
            for (Object list1 : list) {
                dt = GenericaString.converterNullToString(((List) list1).get(0));
                ParametroOposicao po
                        = new ParametroOposicao(
                                detalheRelatorio,
                                dt, // Emissao
                                GenericaString.converterNullToString(((List) list1).get(1)), // Tipo Documento
                                GenericaString.converterNullToString(((List) list1).get(2)), // Documento
                                GenericaString.converterNullToString(((List) list1).get(3)), // Empresa
                                GenericaString.converterNullToString(((List) list1).get(4)).toUpperCase(), // Funcioário
                                GenericaString.converterNullToString(((List) list1).get(5)), // Sexo
                                GenericaString.converterNullToString(((List) list1).get(6)), // CPF
                                GenericaString.converterNullToString(((List) list1).get(7)), // RG
                                GenericaString.converterNullToString(((List) list1).get(8)), // Referência Inicial
                                GenericaString.converterNullToString(((List) list1).get(9)) // Referência Final
                        );
                parametroOposicao.add(po);
            }

        }
        if (parametroOposicao.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        if (!Diretorio.criar("Arquivos/downloads/relatorios/oposicao")) {
            GenericaMensagem.info("Sistema", "Erro ao criar diretório!");
            return;
        }
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            try {
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource((Collection) parametroOposicao);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                String nomeDownload = "relatorio_oposicao_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/oposicao/");
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
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(163);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListaReferencias() {
        if (listSelectItem[1].isEmpty()) {
            OposicaoDB oposicaoDB = new OposicaoDBToplink();
            List<ConvencaoPeriodo> list = (List<ConvencaoPeriodo>) oposicaoDB.listaConvencaoPeriodoPorOposicao();
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getReferenciaFinal() + " - " + list.get(i).getReferenciaInicial() + " - Grupo Cidade: " +list.get(i).getGrupoCidade().getDescricao()  + " - Convenção: " +list.get(i).getConvencao().getDescricao(), "" + list.get(i).getId()));
            }
            if (listSelectItem[1].isEmpty()) {
                listSelectItem[1] = new ArrayList<SelectItem>();
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
            limpar();
            porOposicao = false;
            porPeridoEmissao = false;
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
        if (!porPeridoEmissao) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
        }
        if (!porPeridoConvencao) {
            listSelectItem[0].clear();
            listConvencaoPeriodos.clear();
        }
        if (!porOposicao) {
            oposicao = new Oposicao();
            porPesquisa = "";
        }
    }

    public void close(String close) {
        if (close.equals("oposicao")) {
            porOposicao = false;
            oposicao = new Oposicao();
            porPesquisa = "";
        } else if (close.equals("periodoEmissao")) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
            porPeridoEmissao = false;
        } else if (close.equals("periodoConvencao")) {
            listSelectItem[1].clear();
            porPeridoConvencao = false;
            listConvencaoPeriodos.clear();
        }
        RequestContext.getCurrentInstance().update("form_relatorio:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

    public Oposicao getOposicao() {
        if (GenericaSessao.exists("oposicaoPesquisa")) {
            oposicao = (Oposicao) GenericaSessao.getObject("oposicaoPesquisa", true);
            if (GenericaSessao.exists("oposicaoPesquisaPor")) {
                porPesquisa = GenericaSessao.getString("oposicaoPesquisaPor", true);
                if (porPesquisa.equals("todos") || porPesquisa.equals("data") || porPesquisa.equals("observacao")) {
                    oposicao = new Oposicao();
                    porPesquisa = "";
                }
                GenericaSessao.remove("removeFiltro");
            }
        }
        return oposicao;
    }

    public void setOposicao(Oposicao oposicao) {
        this.oposicao = oposicao;
    }

    public boolean isPorPeridoEmissao() {
        return porPeridoEmissao;
    }

    public void setPorPeridoEmissao(boolean porPeridoEmissao) {
        this.porPeridoEmissao = porPeridoEmissao;
    }

    public boolean isPorOposicao() {
        return porOposicao;
    }

    public void setPorOposicao(boolean porOposicao) {
        this.porOposicao = porOposicao;
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

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public boolean isPorPeridoConvencao() {
        return porPeridoConvencao;
    }

    public void setPorPeridoConvencao(boolean porPeridoConvencao) {
        this.porPeridoConvencao = porPeridoConvencao;
    }

    public List<ParametroOposicao> getParametroOposicao() {
        return parametroOposicao;
    }

    public void setParametroOposicao(List<ParametroOposicao> parametroOposicao) {
        this.parametroOposicao = parametroOposicao;
    }

    public List<Oposicao> getOposicaos() {
        return oposicaos;
    }

    public void setOposicaos(List<Oposicao> oposicaos) {
        this.oposicaos = oposicaos;
    }

    public String getDescPorPesquisa() {
        String tp = porPesquisa;
        if (porPesquisa.equals("todos")) {
            descPorPesquisa = "NENHUM FILTO SELECIONADO.";
        } else if (porPesquisa.equals("rgs") || porPesquisa.equals("cpf") || porPesquisa.equals("nome")) {
            if (porPesquisa.equals("rgs")) {
                tp = "RG";
            }
            descPorPesquisa = "PESQUISA FUNCIONÁRIO POR: " + tp.toUpperCase();
        } else if (porPesquisa.equals("cnpj") || porPesquisa.equals("empresa")) {
            descPorPesquisa = "PESQUISA EMPRESA POR: " + tp.toUpperCase();
        }
        return descPorPesquisa;
    }

    public void setDescPorPesquisa(String descPorPesquisa) {
        this.descPorPesquisa = descPorPesquisa;
    }

    public void removeFiltro() {
        GenericaSessao.put("removeFiltro", true);
    }

    public void addReferencia() {
        Dao dao = new Dao();
        int id = Integer.parseInt(getListaReferencias().get(index[1]).getDescription());
        for (ConvencaoPeriodo lcp : listConvencaoPeriodos) {
            if (id == lcp.getId()) {
                return;
            }
        }
        listConvencaoPeriodos.add((ConvencaoPeriodo) dao.find(new ConvencaoPeriodo(), Integer.parseInt(getListaReferencias().get(index[1]).getDescription())));
    }

    public void removeReferencia(int index) {
        for (int i = 0; i < listConvencaoPeriodos.size(); i++) {
            if (index == i) {
                listConvencaoPeriodos.remove(i);
                return;
            }
        }
    }

    public List<ConvencaoPeriodo> getListConvencaoPeriodos() {
        return listConvencaoPeriodos;
    }

    public void setListConvencaoPeriodos(List<ConvencaoPeriodo> listConvencaoPeriodos) {
        this.listConvencaoPeriodos = listConvencaoPeriodos;
    }
}
