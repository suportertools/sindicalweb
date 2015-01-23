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
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
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
    private Boolean aguardandoResposta;
    private String[] periodo;
    private Date[] dtPeriodo;
    private Integer[] index;
    private String tipoRelatorio;
    private String indexAccordion;
    private String ano;
    private String order;

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
        dtPeriodo = new Date[2];
        dtPeriodo[0] = null;
        dtPeriodo[1] = null;
        periodo = new String[4];
        periodo[0] = "";
        periodo[1] = "";
        periodo[2] = "";
        periodo[3] = "";
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
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioComparativoArrecadacaoBean");
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
            if (filtro[0]) {
                listDetalhePesquisa.add(" Ano: " + ano);
                tipoPeriodo = 1;
                periodoString[0] = ano;
                periodoString[1] = "";
            } else if (filtro[1]) {
                tipoPeriodo = 2;
                periodoString[0] = periodo[0];
                if (!periodo[0].isEmpty() && !periodo[1].isEmpty()) {
                    listDetalhePesquisa.add(" Emissão: " + periodo[0] + " até " + periodo[1]);
                } else if (!periodo[0].isEmpty()) {
                    listDetalhePesquisa.add(" Emissão: " + periodo[0]);
                }
                periodoString[0] = periodo[1];
                periodoString[1] = "";
            } else if (filtro[2]) {
                tipoPeriodo = 3;
                periodoString[0] = periodo[2];
                periodoString[0] = periodo[3];
                if (aguardandoResposta) {
                    periodoString[0] = "";
                    periodoString[0] = "";
                    tipoPeriodo = 4;
                    listDetalhePesquisa.add(" Aguardando resposta");
                } else {
                    if (!periodo[0].isEmpty() && !periodo[1].isEmpty()) {
                        listDetalhePesquisa.add(" Data resposta: " + periodo[2] + " até " + periodo[3]);
                    } else if (!periodo[0].isEmpty()) {
                        listDetalhePesquisa.add(" Data resposta: " + periodo[2]);
                    }
                }
            }
            Dao dao = new Dao();
            if (filtro[3]) {
                if (selectedRepisStatus != null) {
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
                if (selectedCertidaoTipo != null) {
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
                if (selectedCidadeBase != null) {
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
            List list = relatorioCertificadosDao.find(relatorios, pEmpresa, tipoPeriodo, periodoString, inRepisStatus, inCertidaoTipo, inCidadeBase);
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
                dataEmissao = GenericaString.converterNullToString(((List) list1).get(5));
                if (!dataEmissao.isEmpty()) {
                    dataEmissao = DataHoje.converteData(DataHoje.converteDateSqlToDate(dataEmissao));
                }
                dataResposta = GenericaString.converterNullToString(((List) list1).get(6));
                if (!dataResposta.isEmpty()) {
                    dataResposta = DataHoje.converteData(DataHoje.converteDateSqlToDate(dataResposta));
                }
                ParametroCertificados pc
                        = new ParametroCertificados(
                                detalheRelatorio,
                                GenericaString.converterNullToString(((List) list1).get(0)), // Documento
                                GenericaString.converterNullToString(((List) list1).get(1)), // Nome
                                GenericaString.converterNullToString(((List) list1).get(2)), // Cidade
                                GenericaString.converterNullToString(((List) list1).get(3)), // Ano
                                GenericaString.converterNullToString(((List) list1).get(4)), // Status
                                dataEmissao, // Emissao
                                dataResposta, // Resposta
                                GenericaString.converterNullToString(((List) list1).get(7)) // Ano
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
        Jasper.printReports(relatorios.getJasper(), "certificados", (Collection) listParametroCertificados);
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
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
            filtro[2] = false;
            filtro[0] = false;
        }
    }

    public void limpar() {
        // Ano
        if (!filtro[0]) {
            ano = DataHoje.livre(new Date(), "YYYY");
        }
        // Emissão
        if (!filtro[1]) {
            periodo[0] = "";
            periodo[1] = "";
        }
        // Resposta
        if (!filtro[2]) {
            periodo[0] = "";
            periodo[1] = "";
            aguardandoResposta = false;
        }
        // Status
        if (!filtro[3]) {
            listRepisStatus = null;
            selectedRepisStatus = new ArrayList();
        }
        // Tipo de Certificado
        if (!filtro[4]) {
            listCertidaoTipo = null;
            selectedCertidaoTipo = new ArrayList();
        }
        // Empresa
        if (!filtro[5]) {
            juridica = new Juridica();
        }
        // Cidade Base
        if (!filtro[6]) {
            listCidadeBase = null;
            selectedCidadeBase = new ArrayList();
        }
        // Order
        if (!filtro[7]) {
            order = "";
            index[1] = null;
        }
        if (filtro[0]) {
            // Desabilita - Emissão e Resposta
            disabled[1] = true;
            disabled[2] = true;
            periodo[0] = "";
            periodo[1] = "";
            periodo[2] = "";
            periodo[3] = "";
        } else if (filtro[1]) {
            // Desabilita - Ano e Resposta
            ano = "";
            disabled[0] = true;
            disabled[2] = true;
        } else if (filtro[2]) {
            // Desabilita - Ano e Emissão
            periodo[0] = "";
            periodo[1] = "";
            disabled[0] = true;
            disabled[1] = true;
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
            case "emissao":
                filtro[1] = false;
                periodo[0] = "";
                periodo[1] = "";
                disabled[0] = false;
                disabled[1] = false;
                disabled[2] = false;
                break;
            case "resposta":
                filtro[2] = false;
                periodo[0] = "";
                periodo[1] = "";
                disabled[0] = false;
                disabled[1] = false;
                disabled[2] = false;
                aguardandoResposta = false;
                break;
            case "repisStatus":
                // Repis status
                filtro[3] = false;
                listCertidaoTipo = null;
                selectedCertidaoTipo = new ArrayList();
            case "certidaoTipo":
                // Certidão tipo
                filtro[4] = false;
                listCertidaoTipo = null;
                selectedCertidaoTipo = new ArrayList();
            case "empresa":
                // Empresa
                filtro[5] = false;
                juridica = new Juridica();
                break;
            case "cidadeBase":
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
                if(id != list.get(i).getId()) {
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

    public String[] getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String[] periodo) {
        this.periodo = periodo;
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

    public Boolean getAguardandoResposta() {
        return aguardandoResposta;
    }

    public void setAguardandoResposta(Boolean aguardandoResposta) {
        if (!this.aguardandoResposta) {
            periodo[2] = "";
            periodo[3] = "";
        }
        this.aguardandoResposta = aguardandoResposta;
    }

    public List getSelectedCidadeBase() {
        return selectedCidadeBase;
    }

    public void setSelectedCidadeBase(List selectedCidadeBase) {
        this.selectedCidadeBase = selectedCidadeBase;
    }

}
