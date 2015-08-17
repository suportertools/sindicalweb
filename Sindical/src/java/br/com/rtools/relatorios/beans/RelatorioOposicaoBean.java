package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.dao.OposicaoDao;
import br.com.rtools.impressao.ParametroOposicao;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.EnviarArquivosDB;
import br.com.rtools.pessoa.db.EnviarArquivosDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
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
public class RelatorioOposicaoBean implements Serializable {

    private Oposicao oposicao;
    private List selectedConvencao;
    private List selectedGrupoCidades;
    private List selectedCnae;
    private List<SelectItem>[] listSelectItem;
    private List<ParametroOposicao> parametroOposicao;
    private List<ConvencaoPeriodo> listConvencaoPeriodos;
    private List<Oposicao> oposicaos;
    private Map<String, Integer> listConvencoes;
    private Map<String, Integer> listGrupoCidades;
    private Map<String, Integer> listCnaes;
    private Boolean[] filtro;
    private Date dataInicial;
    private Date dataFinal;
    private Integer[] index;
    private String tipoRelatorio;
    private String indexAccordion;
    private String porPesquisa;
    private String descPorPesquisa;
    private String order;
    private String status;

    @PostConstruct
    public void init() {
        oposicao = new Oposicao();
        filtro = new Boolean[6];
        filtro[0] = false;
        filtro[1] = false;
        filtro[2] = false;
        filtro[3] = false;
        filtro[4] = false;
        filtro[5] = false;
        selectedConvencao = new ArrayList<>();
        selectedGrupoCidades = new ArrayList<>();
        selectedCnae = new ArrayList<>();
        oposicaos = new ArrayList<>();
        listCnaes = null;
        listConvencoes = null;
        listGrupoCidades = null;
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        parametroOposicao = new ArrayList<>();
        listConvencaoPeriodos = new ArrayList<>();
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
        status = "todos";
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
            RelatorioDao rgdb = new RelatorioDao();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
        }
        if (relatorios == null) {
            return;
        }
        String detalheRelatorio = "";
        if (parametroOposicao.isEmpty()) {
            OposicaoDao oposicaoDao = new OposicaoDao();
            int pPessoaOposicaoI = 0;
            int pEmpresaI = 0;
            String pIStringI = "";
            String pFStringI = "";
            String referencia = "";
            String dReferencia = "";
            List listDetalhePesquisa = new ArrayList();
            if (filtro[0]) {
                pIStringI = DataHoje.converteData(dataInicial);
                pFStringI = DataHoje.converteData(dataFinal);
                listDetalhePesquisa.add(" Período de Emissão entre " + pIStringI + " e " + pFStringI);
            }
            if (filtro[1]) {
                for (int i = 0; i < listConvencaoPeriodos.size(); i++) {
                    if (i == 0) {
                        dReferencia = "[" + listConvencaoPeriodos.get(i).getReferenciaFinal() + " - " + listConvencaoPeriodos.get(i).getReferenciaInicial() + " - Conv: " + listConvencaoPeriodos.get(i).getConvencao().getDescricao() + " - G. Cidade: " + listConvencaoPeriodos.get(i).getGrupoCidade().getDescricao() + "]";
                        referencia = "" + Integer.toString(listConvencaoPeriodos.get(i).getId());
                    } else {
                        referencia += "," + Integer.toString(listConvencaoPeriodos.get(i).getId());
                        dReferencia += ", [" + listConvencaoPeriodos.get(i).getReferenciaFinal() + " - " + listConvencaoPeriodos.get(i).getReferenciaInicial() + " - Conv: " + listConvencaoPeriodos.get(i).getConvencao().getDescricao() + " - G. Cidade: " + listConvencaoPeriodos.get(i).getGrupoCidade().getDescricao() + "]";
                    }
                }
            }
            
            if (filtro[5]){
                listDetalhePesquisa.add(" Status da Oposição: " + status.toUpperCase() + "");
            }
            
            String cnaesList = "";
            String inCnaes = null;
            if (!dReferencia.isEmpty()) {
                listDetalhePesquisa.add(" Período convenção: " + dReferencia + "");
            }
            if (porPesquisa.equals("rgs") || porPesquisa.equals("nome") || porPesquisa.equals("cpf")) {
                if (oposicao.getOposicaoPessoa().getId() != -1) {
                    pPessoaOposicaoI = oposicao.getOposicaoPessoa().getId();
                    listDetalhePesquisa.add(" Pessoa Oposição por " + porPesquisa + ". Documento nº" + oposicao.getOposicaoPessoa().getCpf() + " - " + oposicao.getOposicaoPessoa().getNome());
                }
            }
            if (porPesquisa.equals("cnpj") || porPesquisa.equals("empresa")) {
                if (oposicao.getOposicaoPessoa().getId() != -1) {
                    //pEmpresaI = oposicao.getJuridica().getPessoa().getId();
                    pEmpresaI = oposicao.getJuridica().getId();
                    listDetalhePesquisa.add(" Pessoa Jurídica por " + porPesquisa + ". CNPJ: " + oposicao.getJuridica().getPessoa().getDocumento() + " - " + oposicao.getJuridica().getPessoa().getNome());
                }
            }
            if (selectedCnae != null) {
                for (int i = 0; i < selectedCnae.size(); i++) {
                    if (i == 0) {
                        inCnaes = "" + selectedCnae.get(i);
                        //cnaesList += selectedCnae.get(i).getCnae();
                    } else {
                        inCnaes += "," + selectedCnae.get(i);
                        //cnaesList += ", " + selectedCnae(i).getCnae();
                    }
                }
                if (!cnaesList.isEmpty()) {
                    listDetalhePesquisa.add(" Cnaes: " + cnaesList + "; ");
                }
            }
            List list = oposicaoDao.filtroRelatorio(pEmpresaI, pPessoaOposicaoI, pIStringI, pFStringI, referencia, relatorios, inCnaes, status, order);
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
            String dt;
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
                                GenericaString.converterNullToString(((List) list1).get(9)), // Referência Final
                                GenericaString.converterNullToString(((List) list1).get(10)), // Logradouro
                                GenericaString.converterNullToString(((List) list1).get(11)), // Endereço
                                GenericaString.converterNullToString(((List) list1).get(12)), // Número
                                GenericaString.converterNullToString(((List) list1).get(13)), // Complemento
                                GenericaString.converterNullToString(((List) list1).get(14)), // Bairro
                                GenericaString.converterNullToString(((List) list1).get(15)), // Cidade
                                GenericaString.converterNullToString(((List) list1).get(16)), // UF
                                GenericaString.converterNullToString(((List) list1).get(17)) // CEP
                        );
                parametroOposicao.add(po);
            }

        }
        if (parametroOposicao.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        Jasper.printReports(relatorios.getJasper(), "oposicao", (Collection) parametroOposicao);
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(239);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListaReferencias() {
        if (listSelectItem[1].isEmpty()) {
            OposicaoDao oposicaoDao = new OposicaoDao();
            List<ConvencaoPeriodo> list = (List<ConvencaoPeriodo>) oposicaoDao.listaConvencaoPeriodoPorOposicao();
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getReferenciaInicial() + " - " + list.get(i).getReferenciaFinal() + " - Grupo Cidade: " + list.get(i).getGrupoCidade().getDescricao() + " - Convenção: " + list.get(i).getConvencao().getDescricao(), "" + list.get(i).getId()));
            }
            if (listSelectItem[1].isEmpty()) {
                listSelectItem[1] = new ArrayList<>();
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
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
        }
        if (!filtro[1]) {
            listSelectItem[0].clear();
            listConvencaoPeriodos.clear();
        }
        if (!filtro[2]) {
            oposicao = new Oposicao();
            porPesquisa = "";
        }
        if (!filtro[3]) {
            listCnaes = null;
            listConvencoes = null;
            listGrupoCidades = null;
            listSelectItem = new ArrayList[2];
            listSelectItem[0] = new ArrayList<>();
            listSelectItem[1] = new ArrayList<>();
        }
        if (!filtro[4]) {
            order = "";
        }
        if (!filtro[5]) {
            status = "todos";
        }
    }

    public void close(String close) {
        if (close.equals("periodoEmissao")) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
            filtro[0] = false;
        } else if (close.equals("periodoConvencao")) {
            listSelectItem[1].clear();
            filtro[1] = false;
            listConvencaoPeriodos.clear();
        } else if (close.equals("oposicao")) {
            filtro[2] = false;
            oposicao = new Oposicao();
            porPesquisa = "";
        } else if (close.equals("cnae")) {
            filtro[3] = false;
            listCnaes = null;
            listConvencoes = null;
            listGrupoCidades = null;
            listSelectItem = new ArrayList[2];
            listSelectItem[0] = new ArrayList<>();
            listSelectItem[1] = new ArrayList<>();
        } else if (close.equals("order")) {
            order = "";
            filtro[4] = false;
        }else if (close.equals("status")) {
            status = "todos";
            filtro[5] = false;
        }
        PF.update("form_relatorio:id_panel");
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

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
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
        switch (porPesquisa) {
            case "todos":
                descPorPesquisa = "NENHUM FILTO SELECIONADO.";
                break;
            case "rgs":
            case "cpf":
            case "nome":
                if (porPesquisa.equals("rgs")) {
                    tp = "RG";
                }   descPorPesquisa = "PESQUISA FUNCIONÁRIO POR: " + tp.toUpperCase();
                break;
            case "cnpj":
            case "empresa":
                descPorPesquisa = "PESQUISA EMPRESA POR: " + tp.toUpperCase();
                break;
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
        Integer id = Integer.parseInt(getListaReferencias().get(index[1]).getDescription());
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

    /**
     * <strong>Filtros</strong>
     * <ul>
     * <li>[0] Periodo Emissão</li>
     * <li>[1] Periodo Convenção</li>
     * <li>[2] Periodo Pesquisas Oposição</li>
     * <li>[3] Cnae</li>
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

    public Map<String, Integer> getListCnaes() {
        listCnaes = null;
        if (!selectedConvencao.isEmpty()) {
            listCnaes = new HashMap<>();
            String ids = inIdConvencao();
            if (!ids.isEmpty()) {
                OposicaoDao odb = new OposicaoDao();
                List<Cnae> list = (List<Cnae>) odb.listaCnaesPorOposicaoJuridica(ids);
                for (int i = 0; i < list.size(); i++) {
                    listCnaes.put(list.get(i).getCnae() + " - " + list.get(i).getNumero(), list.get(i).getId());
                }
            }
        } else {
            selectedCnae = new ArrayList();
        }
        return listCnaes;
    }

    public void setListCnaes(Map<String, Integer> listCnaes) {
        this.listCnaes = listCnaes;
    }

    public Map<String, Integer> getListConvencaos() {
        if (listConvencoes == null) {
            listConvencoes = new HashMap<>();
            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
            List<Convencao> list = enviarArquivosDB.listaConvencao();
            for (int i = 0; i < list.size(); i++) {
                listConvencoes.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listConvencoes;
    }

    public void setListConvencaos(Map<String, Integer> listConvencoes) {
        this.listConvencoes = listConvencoes;
    }

    public Map<String, Integer> getListGrupoCidades() {
        listGrupoCidades = null;
        if (!selectedConvencao.isEmpty()) {
            listGrupoCidades = new HashMap<>();
            String ids = inIdConvencao();
            if (!ids.isEmpty()) {
                EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
                List<GrupoCidade> list = enviarArquivosDB.listaGrupoCidadePorConvencao(ids);
                for (int i = 0; i < list.size(); i++) {
                    listGrupoCidades.put(list.get(i).getDescricao(), list.get(i).getId());
                }
            }
        } else {
            selectedGrupoCidades = new ArrayList();
        }
        return listGrupoCidades;
    }

    public void setListGrupoCidades(HashMap<String, Integer> listGrupoCidades) {
        this.listGrupoCidades = listGrupoCidades;
    }

    public String inIdConvencao() {
        String ids = "";
        if (selectedConvencao != null) {
            for (int i = 0; i < selectedConvencao.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedConvencao.get(i);
                } else {
                    ids += "," + selectedConvencao.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdGruposCidade() {
        String ids = "";
        if (selectedGrupoCidades != null) {
            for (int i = 0; i < selectedGrupoCidades.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedGrupoCidades.get(i);
                } else {
                    ids += "," + selectedGrupoCidades.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdCnaes() {
        String ids = "";
        if (selectedCnae != null) {
            for (int i = 0; i < selectedCnae.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedCnae.get(i);
                } else {
                    ids += "," + selectedCnae.get(i);
                }
            }

        }
        return ids;
    }

    public List getSelectedConvencao() {
        return selectedConvencao;
    }

    public void setSelectedConvencao(List selectedConvencao) {
        this.selectedConvencao = selectedConvencao;
    }

    public List getSelectedGrupoCidades() {
        return selectedGrupoCidades;
    }

    public void setSelectedGrupoCidades(List selectedGrupoCidades) {
        this.selectedGrupoCidades = selectedGrupoCidades;
    }

    public List getSelectedCnae() {
        return selectedCnae;
    }

    public void setSelectedCnae(List selectedCnae) {
        this.selectedCnae = selectedCnae;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
