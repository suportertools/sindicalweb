package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.CertidaoTipo;
import br.com.rtools.arrecadacao.RepisStatus;
import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.impressao.ParametroCertificados;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.beans.JuridicaBean;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioCertificadosDao;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.PF;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioCertificadosBean implements Serializable {

    private Juridica juridica;
    private List selectedRepisStatus;
    private List selectedCertidaoTipo;
    private List selectedCidadeBase;
    private List<ParametroCertificados> listParametroCertificados;
    private List<SelectItem>[] listSelectItem;
    private Map<String, Integer> listRepisStatus;
    private Map<String, Integer> listCertidaoTipo;
    private Map<String, Integer> listCidadeBase;
    private Boolean[] filtro;
    private Boolean[] disabled;
    private boolean aguardandoResposta;
    private String periodoInicial;
    private String periodoFinal;
    private String respostaInicial;
    private String respostaFinal;
    private Integer[] index;
    private String tipoRelatorio;
    private String indexAccordion;
    private String ano;
    private String order;
    private Relatorios relatorios;

    @PostConstruct
    public void init() {
        juridica = new Juridica();
        filtro = new Boolean[8];
        filtro[0] = false;
        filtro[1] = false;
        filtro[2] = false;
        filtro[3] = false;
        filtro[4] = false;
        filtro[5] = false;
        filtro[6] = false;
        filtro[7] = false;
        disabled = new Boolean[3];
        disabled[0] = false;
        disabled[1] = false;
        disabled[2] = false;
        aguardandoResposta = false;
        ano = DataHoje.livre(new Date(), "YYYY");
        periodoInicial = "";
        periodoFinal = "";
        respostaInicial = "";
        respostaFinal = "";
        selectedRepisStatus = new ArrayList<>();
        selectedCertidaoTipo = new ArrayList<>();
        selectedCidadeBase = new ArrayList<>();
        listRepisStatus = null;
        listCertidaoTipo = null;
        listCidadeBase = null;
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listParametroCertificados = new ArrayList<>();
        index = new Integer[2];
        index[0] = null;
        index[1] = null;
        tipoRelatorio = "Avançado";
        indexAccordion = "Avançado";
        order = "";
        relatorios = new Relatorios();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioCertidicadosBean");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void visualizar() {
        Relatorios relatoriosx = null;
        if (!getListaTipoRelatorios().isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            relatoriosx = rgdb.pesquisaRelatorios(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
        }
        if (relatoriosx == null) {
            return;
        }
        String detalheRelatorio = "";
        listParametroCertificados.clear();
        if (listParametroCertificados.isEmpty()) {
            RelatorioCertificadosDao relatorioCertificadosDao = new RelatorioCertificadosDao();
            Integer pEmpresa = null;
            List listDetalhePesquisa = new ArrayList();
            if (juridica.getId() != -1) {
                pEmpresa = juridica.getId();
                listDetalhePesquisa.add(" Empresa - CNPJ: " + juridica.getPessoa().getDocumento() + " - " + juridica.getPessoa().getNome());
            }
            if (!order.isEmpty()) {
                relatorioCertificadosDao.setOrder(order);
            }
            String inRepisStatus = inIdRepisStatus();
            String inCertidaoTipo = inIdCertidaoTipo();
            String inCidadeBase = inIdCidadeBase();
            Integer tipoPeriodo = null;
            String periodoString[] = new String[]{"", ""};
            if (periodoInicial == null) {
                periodoInicial = "";
            }
            if (periodoFinal == null) {
                periodoFinal = "";
            }
            if (respostaInicial == null) {
                respostaInicial = "";
            }
            if (respostaFinal == null) {
                respostaFinal = "";
            }
            if (filtro[0]) {
                listDetalhePesquisa.add(" Ano: " + ano);
                tipoPeriodo = 1;
                periodoString[0] = ano;
                periodoString[1] = "";
            } else if (filtro[1]) {
                tipoPeriodo = 2;
                periodoString[0] = periodoInicial;
                if (!periodoInicial.isEmpty() && !periodoFinal.isEmpty()) {
                    listDetalhePesquisa.add(" Emissão: " + periodoInicial + " até " + periodoFinal);
                } else if (!periodoInicial.isEmpty()) {
                    listDetalhePesquisa.add(" Emissão: " + periodoInicial);
                }
                periodoString[0] = periodoInicial;
                periodoString[1] = periodoFinal;
            } else if (filtro[2]) {
                tipoPeriodo = 3;
                periodoString[0] = respostaInicial;
                periodoString[1] = respostaFinal;
                if (aguardandoResposta) {
                    periodoString[0] = "";
                    periodoString[1] = "";
                    tipoPeriodo = 4;
                    listDetalhePesquisa.add(" Aguardando resposta");
                } else {
                    if (!respostaInicial.isEmpty() && !respostaFinal.isEmpty()) {
                        listDetalhePesquisa.add(" Data resposta: " + respostaInicial + " até " + respostaFinal);
                    } else if (!respostaInicial.isEmpty()) {
                        listDetalhePesquisa.add(" Data resposta: " + respostaInicial);
                    }
                }
            }
            Dao dao = new Dao();
            if (filtro[3]) {
                if (selectedRepisStatus != null && !selectedRepisStatus.isEmpty()) {
                    String repisStatusString = "";
                    Integer id = 0;
                    for (int i = 0; i < selectedRepisStatus.size(); i++) {
                        id = Integer.parseInt(selectedRepisStatus.get(i).toString());
                        if (repisStatusString.isEmpty()) {
                            repisStatusString = ((RepisStatus) dao.find(new RepisStatus(), id)).getDescricao();
                        } else {
                            repisStatusString += ", " + ((RepisStatus) dao.find(new RepisStatus(), id)).getDescricao();
                        }
                    }
                    listDetalhePesquisa.add(" Status: " + repisStatusString);
                }
            }
            if (filtro[4]) {
                if (selectedCertidaoTipo != null && !selectedCertidaoTipo.isEmpty()) {
                    String certidaoTipoString = "";
                    Integer id = 0;
                    for (int i = 0; i < selectedCertidaoTipo.size(); i++) {
                        id = Integer.parseInt(selectedCertidaoTipo.get(i).toString());
                        if (certidaoTipoString.isEmpty()) {
                            certidaoTipoString = ((CertidaoTipo) dao.find(new CertidaoTipo(), id)).getDescricao();
                        } else {
                            certidaoTipoString += ", " + ((CertidaoTipo) dao.find(new CertidaoTipo(), id)).getDescricao();
                        }
                    }
                    listDetalhePesquisa.add(" Tipos de certidão: " + certidaoTipoString);
                }
            }
            if (filtro[6]) {
                if (selectedCidadeBase != null && !selectedCidadeBase.isEmpty()) {
                    String cidadeString = "";
                    Integer id = 0;
                    for (int i = 0; i < selectedCidadeBase.size(); i++) {
                        id = Integer.parseInt(selectedCidadeBase.get(i).toString());
                        if (cidadeString.isEmpty()) {
                            cidadeString = ((Cidade) dao.find(new Cidade(), id)).getCidade();
                        } else {
                            cidadeString += ", " + ((Cidade) dao.find(new Cidade(), id)).getCidade();
                        }
                    }
                    listDetalhePesquisa.add(" Cidade: " + cidadeString);
                }
            }
            if (filtro[7]) {
                if (index[1] != null) {
                    RelatorioOrdem relatorioOrdem = (RelatorioOrdem) dao.find(new RelatorioOrdem(), Integer.parseInt(getListaRelatorioOrdem().get(index[1]).getDescription()));
                    if (relatorioOrdem != null) {
                        relatorioCertificadosDao.setOrder(relatorioOrdem.getQuery());
                    }

                }
            }
            List list = relatorioCertificadosDao.find(relatoriosx, pEmpresa, tipoPeriodo, periodoString, inRepisStatus, inCertidaoTipo, inCidadeBase);
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
            String dataEmissao = "";
            String dataResposta = "";
            for (Object list1 : list) {
                dataEmissao = GenericaString.converterNullToString(((List) list1).get(4));
                if (!dataEmissao.isEmpty()) {
                    dataEmissao = DataHoje.converteData(DataHoje.converteDateSqlToDate(dataEmissao));
                }
                dataResposta = GenericaString.converterNullToString(((List) list1).get(5));
                if (!dataResposta.isEmpty()) {
                    dataResposta = DataHoje.converteData(DataHoje.converteDateSqlToDate(dataResposta));
                }
                ParametroCertificados pc
                        = new ParametroCertificados(
                                detalheRelatorio,
                                GenericaString.converterNullToString(((List) list1).get(0)), // Documento
                                GenericaString.converterNullToString(((List) list1).get(1)), // Nome
                                GenericaString.converterNullToString(((List) list1).get(3)), // Status
                                GenericaString.converterNullToString(((List) list1).get(2)), // Tipo
                                dataEmissao, // Emissao
                                dataResposta, // Resposta
                                GenericaString.converterNullToString(((List) list1).get(6)), // Ano
                                GenericaString.converterNullToString(((List) list1).get(7)), // Solicitante
                                GenericaString.converterNullToString(((List) list1).get(8)), // Email
                                GenericaString.converterNullToString(((List) list1).get(9)), // Telefone
                                GenericaString.converterNullToString(((List) list1).get(10)), // Logradouro
                                GenericaString.converterNullToString(((List) list1).get(11)), // Descrição Endereço
                                GenericaString.converterNullToString(((List) list1).get(12)), // Número
                                GenericaString.converterNullToString(((List) list1).get(13)), // Complemento
                                GenericaString.converterNullToString(((List) list1).get(14)), // Bairro
                                GenericaString.converterNullToString(((List) list1).get(15)), // Cidade
                                GenericaString.converterNullToString(((List) list1).get(16)), // UF
                                GenericaString.converterNullToString(((List) list1).get(17)) // CEP
                        );
                listParametroCertificados.add(pc);
            }

        }
        if (listParametroCertificados.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        Jasper.IS_HEADER = true;
        Jasper.TYPE = "paisagem";
        if (relatoriosx.getExcel()) {
            Jasper.EXCEL_FIELDS = relatoriosx.getCamposExcel();
        } else {
            Jasper.EXCEL_FIELDS = "";
        }
        Jasper.printReports(relatoriosx.getJasper(), "certificados", (Collection) listParametroCertificados);
    }

    public void export() throws FileNotFoundException, IOException {
        listParametroCertificados.clear();
        RelatorioCertificadosDao relatorioCertificadosDao = new RelatorioCertificadosDao();
        String inRepisStatus = inIdRepisStatus();
        String inCertidaoTipo = inIdCertidaoTipo();
        String inCidadeBase = inIdCidadeBase();
        Integer tipoPeriodo = null;
        String periodoString[] = new String[]{"", ""};
        if (filtro[0]) {
            tipoPeriodo = 1;
            periodoString[0] = ano;
            periodoString[1] = "";
        } else if (filtro[1]) {
            tipoPeriodo = 2;
            periodoString[0] = periodoInicial;
            periodoString[0] = periodoFinal;
            periodoString[1] = "";
        } else if (filtro[2]) {
            tipoPeriodo = 3;
            periodoString[0] = respostaInicial;
            periodoString[0] = respostaFinal;
            if (aguardandoResposta) {
                periodoString[0] = "";
                periodoString[0] = "";
                tipoPeriodo = 4;
            }
        }
        List list = relatorioCertificadosDao.find(tipoPeriodo, periodoString, inRepisStatus, inCertidaoTipo, inCidadeBase);
        for (Object list1 : list) {
            ParametroCertificados pc
                    = new ParametroCertificados(
                            GenericaString.converterNullToString(((List) list1).get(0)), // Nome
                            GenericaString.converterNullToString(((List) list1).get(1)), // Logradouro
                            GenericaString.converterNullToString(((List) list1).get(2)), // Descrição Endereço
                            GenericaString.converterNullToString(((List) list1).get(3)), // Número
                            GenericaString.converterNullToString(((List) list1).get(4)), // Complemento
                            GenericaString.converterNullToString(((List) list1).get(5)), // Bairro
                            GenericaString.converterNullToString(((List) list1).get(6)), // Cidade
                            GenericaString.converterNullToString(((List) list1).get(7)), // UF
                            GenericaString.converterNullToString(((List) list1).get(8)) // CEP
                    );
            listParametroCertificados.add(pc);
        }

        Diretorio.criar("downloads/outros");
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/outros/");
        String path = caminho;
        File flDes = new File(caminho); // 0 DIA, 1 MES, 2 ANO

        flDes.mkdir();
        caminho += "/endereco" + DataHoje.hora().replace(":", "") + ".txt";
        FileWriter writer;
        try (FileOutputStream file = new FileOutputStream(caminho)) {
            writer = new FileWriter(caminho);
        }
        try (BufferedWriter buffWriter = new BufferedWriter(writer)) {
            String header = "Razão;Endereço;Nº;Bairro;Cep;Cidade;UF";
            buffWriter.write(header);
            buffWriter.newLine();
            String content;
            for (int i = 0; i < listParametroCertificados.size(); i++) {
                content = listParametroCertificados.get(i).getNome() + ";"
                        + listParametroCertificados.get(i).getLogradouro() + " " + listParametroCertificados.get(i).getEndereco() + ";"
                        + listParametroCertificados.get(i).getNumero() + ";"
                        + listParametroCertificados.get(i).getBairro() + ";"
                        + listParametroCertificados.get(i).getCep() + ";"
                        + listParametroCertificados.get(i).getCidade() + ";"
                        + listParametroCertificados.get(i).getUf();
                buffWriter.write(content);
                buffWriter.newLine();
                content = "";
            }
            buffWriter.flush();
        }
        Download download = new Download("/endereco" + DataHoje.hora().replace(":", "") + ".txt", path, "text/plain", context);
        download.baixar();
        download.remover();
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(280);
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

    public List<SelectItem> getListaRelatorioOrdem() {
        listSelectItem[1].clear();
        if (index[0] != null) {
            RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
            List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(Integer.parseInt(getListaTipoRelatorios().get(index[0]).getDescription()));
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
        if (tipoRelatorio.equals("Avançado")) {
            limpar();
        }
    }

    public void limpar() {
        // Ano
        if (!filtro[0]) {
            ano = DataHoje.livre(new Date(), "YYYY");
        }
        // Emissão
        if (!filtro[1]) {
            periodoInicial = "";
            periodoFinal = "";
        }
        // Resposta
        if (!filtro[2]) {
            respostaInicial = "";
            respostaFinal = "";
            aguardandoResposta = false;
        }
        // Status
        if (!filtro[3]) {
            selectedRepisStatus = new ArrayList();
            listRepisStatus = null;
        }
        // Tipo de Certificado
        if (!filtro[4]) {
            selectedCertidaoTipo = new ArrayList();
            listCertidaoTipo = null;
        }
        // Empresa
        if (!filtro[5]) {
            juridica = new Juridica();
        }
        // Cidade Base
        if (!filtro[6]) {
            selectedCidadeBase = new ArrayList();
            listCidadeBase = null;
        }
        // Order
        if (!filtro[7]) {
            order = "";
            index[1] = null;
        }
        if (filtro[0]) {
            if (!filtro[1] && !filtro[2]) {
                // Desabilita - Emissão e Resposta
                disabled[1] = true;
                disabled[2] = true;
                periodoInicial = "";
                periodoFinal = "";
                respostaInicial = "";
                respostaFinal = "";
            }
        } else if (filtro[1]) {
            if (!filtro[0] && !filtro[2]) {
                // Desabilita - Ano e Resposta
                ano = "";
                disabled[0] = true;
                disabled[2] = true;
            }
        } else if (filtro[2]) {
            if (!filtro[0] && !filtro[1]) {
                // Desabilita - Ano e Emissão
                periodoInicial = "";
                periodoFinal = "";
                disabled[0] = true;
                disabled[1] = true;
            }
        } else {
            disabled[0] = false;
            disabled[1] = false;
            disabled[2] = false;
        }
    }

    public void close(String close) {
        switch (close) {
            case "ano":
                filtro[0] = false;
                ano = "";
                disabled[0] = false;
                disabled[1] = false;
                disabled[2] = false;
                break;
            case "periodo_emissao":
                filtro[1] = false;
                periodoInicial = "";
                periodoFinal = "";
                disabled[0] = false;
                disabled[1] = false;
                disabled[2] = false;
                break;
            case "periodo_resposta":
                filtro[2] = false;
                respostaInicial = "";
                respostaFinal = "";
                disabled[0] = false;
                disabled[1] = false;
                disabled[2] = false;
                aguardandoResposta = false;
                break;
            case "repis_status":
                // Repis status
                filtro[3] = false;
                listRepisStatus = null;
                selectedRepisStatus = new ArrayList();
            case "certidao_tipo":
                // Certidão tipo
                filtro[4] = false;
                listCertidaoTipo = null;
                selectedCertidaoTipo = new ArrayList();
            case "empresa":
                // Empresa
                filtro[5] = false;
                juridica = new Juridica();
                break;
            case "cidade_base":
                // Cidade base
                filtro[6] = false;
                listCidadeBase = null;
                selectedCidadeBase = new ArrayList();
                break;
            case "order":
                order = "";
                filtro[7] = false;
                index[1] = null;
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

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
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
     * <li>[0] Ano </li>
     * <li>[1] Emissão </li>
     * <li>[2] Resposta </li>
     * <li>[3] Repis Status </li>
     * <li>[4] Certidão Tipo </li>
     * <li>[5] Empresa </li>
     * <li>[6] Cidade Base </li>
     * <li>[7] Ordenação </li>
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

    public Map<String, Integer> getListRepisStatus() {
        if (listRepisStatus == null || listRepisStatus.isEmpty()) {
            listRepisStatus = new LinkedHashMap<>();
            Dao dao = new Dao();
            List<RepisStatus> list = (List<RepisStatus>) dao.list(new RepisStatus());
            for (int i = 0; i < list.size(); i++) {
                listRepisStatus.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listRepisStatus;
    }

    public void setListRepisStatus(Map<String, Integer> listRepisStatus) {
        this.listRepisStatus = listRepisStatus;
    }

    public Map<String, Integer> getListCertidaoTipo() {
        if (listCertidaoTipo == null || listCertidaoTipo.isEmpty()) {
            listCertidaoTipo = new LinkedHashMap<>();
            Dao dao = new Dao();
            List<CertidaoTipo> list = (List<CertidaoTipo>) dao.list(new CertidaoTipo());
            for (int i = 0; i < list.size(); i++) {
                listCertidaoTipo.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listCertidaoTipo;
    }

    public void setListCertidaoTipo(Map<String, Integer> listCertidaoTipo) {
        this.listCertidaoTipo = listCertidaoTipo;
    }

    public Map<String, Integer> getListCidadeBase() {
        if (listCidadeBase == null || listCidadeBase.isEmpty()) {
            listCidadeBase = new LinkedHashMap<>();
            GrupoCidadesDB grupoCidadesDB = new GrupoCidadesDBToplink();
            List<Cidade> list = grupoCidadesDB.pesquisaCidadesBase();
            int id = -1;
            for (int i = 0; i < list.size(); i++) {
                if (id != list.get(i).getId()) {
                    id = list.get(i).getId();
                }
                listCidadeBase.put(list.get(i).getCidade().toUpperCase(), list.get(i).getId());
            }
        }
        return listCidadeBase;
    }

    public void setListCidadeBase(Map<String, Integer> listCidadesBase) {
        this.listCidadeBase = listCidadesBase;
    }

    public String inIdRepisStatus() {
        String ids = "";
        if (selectedRepisStatus != null) {
            for (int i = 0; i < selectedRepisStatus.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedRepisStatus.get(i);
                } else {
                    ids += "," + selectedRepisStatus.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdCertidaoTipo() {
        String ids = "";
        if (selectedCertidaoTipo != null) {
            for (int i = 0; i < selectedCertidaoTipo.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedCertidaoTipo.get(i);
                } else {
                    ids += "," + selectedCertidaoTipo.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdCidadeBase() {
        String ids = "";
        if (selectedCidadeBase != null) {
            for (int i = 0; i < selectedCidadeBase.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedCidadeBase.get(i);
                } else {
                    ids += "," + selectedCidadeBase.get(i);
                }
            }

        }
        return ids;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void defineTipo(String tipo) {
        JuridicaBean juridicaBean = new JuridicaBean();
        GenericaSessao.put("tipo", tipo);
        switch (tipo) {
            case "empresa":
                juridicaBean.pesquisaTodosEAtivos();
                break;
            case "contabilidade":
                juridicaBean.pesquisaSomenteContabilidades();
                break;
        }
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public List getSelectedRepisStatus() {
        return selectedRepisStatus;
    }

    public void setSelectedRepisStatus(List selectedRepisStatus) {
        this.selectedRepisStatus = selectedRepisStatus;
    }

    public List getSelectedCertidaoTipo() {
        return selectedCertidaoTipo;
    }

    public void setSelectedCertidaoTipo(List selectedCertidaoTipo) {
        this.selectedCertidaoTipo = selectedCertidaoTipo;
    }

    public List<ParametroCertificados> getListParametroCertificados() {
        return listParametroCertificados;
    }

    public void setListParametroCertificados(List<ParametroCertificados> listParametroCertificados) {
        this.listParametroCertificados = listParametroCertificados;
    }

    public Boolean[] getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean[] disabled) {
        this.disabled = disabled;
    }

    public boolean isAguardandoResposta() {
        return aguardandoResposta;
    }

    public void setAguardandoResposta(boolean aguardandoResposta) {
        this.aguardandoResposta = aguardandoResposta;
    }

    public List getSelectedCidadeBase() {
        return selectedCidadeBase;
    }

    public void setSelectedCidadeBase(List selectedCidadeBase) {
        this.selectedCidadeBase = selectedCidadeBase;
    }

    public String getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(String periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public String getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(String periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getRespostaInicial() {
        return respostaInicial;
    }

    public void setRespostaInicial(String respostaInicial) {
        this.respostaInicial = respostaInicial;
    }

    public String getRespostaFinal() {
        return respostaFinal;
    }

    public void setRespostaFinal(String respostaFinal) {
        this.respostaFinal = respostaFinal;
    }

    public void listener(Integer tCase) {
        if (tCase == 1) {
            if (!this.aguardandoResposta) {
                this.respostaInicial = "";
                this.respostaFinal = "";
            }
        }

    }

    public Relatorios getRelatorios() {
        if(index[0] != null) {
            try {
                if (relatorios.getId() != Integer.parseInt(getListaTipoRelatorios().get(index[0]).getDescription())) {
                    Jasper.EXPORT_TO_EXCEL = false;
                }
                relatorios = (Relatorios) new Dao().find(new Relatorios(), Integer.parseInt(getListaTipoRelatorios().get(index[0]).getDescription()));
            } catch (Exception e) {
                relatorios = new Relatorios();
                Jasper.EXPORT_TO_EXCEL = false;
            }            
        }
        return relatorios;
    }

}
