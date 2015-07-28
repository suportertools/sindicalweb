package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.impressao.ParametroFechamentoGuias;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioFechamentoGuiasDao;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.Tabbed;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class RelatorioFechamentoGuiasBean implements Serializable {

    private Boolean[] filtro;
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private List<ParametroFechamentoGuias> parametroFechamentoGuias;
    private List<Fisica> listFisica;
    private List<Juridica> listEmpresas;
    private Date dataInicial;
    private Date dataFinal;

    /**
     * <ul>
     * <li> Indices </li>
     * <li>[0] Relatório </li>
     * <li>[1] Relatório Ordem </li>
     * <li>[2] Grupo </li>
     * <li>[3] SubGrupo </li>
     * <li>[4] Empresa </li>
     * </ul>
     */
    private Integer[] index;
    private List<SelectItem>[] listSelectItem;

    @PostConstruct
    public void init() {
        filtro = new Boolean[3];
        filtro[0] = false; // PERIODO
        filtro[1] = false; // EMPRESA
        filtro[2] = false; // BENEFICIÁRIO
        index = new Integer[5];
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        index[3] = 0;
        index[4] = 0;
        tipoRelatorio = "Avançado";
        indexAccordion = "Avançado";
        tipo = "todos";
        listSelectItem = new ArrayList[5];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        listSelectItem[3] = new ArrayList<>();
        listSelectItem[4] = new ArrayList<>();
        parametroFechamentoGuias = new ArrayList<>();
        listFisica = new ArrayList<>();
        listEmpresas = new ArrayList<>();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        new Tabbed().init();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioFechamentoGuiasBean");
        GenericaSessao.remove("tabbedBean");
    }

    public void visualizar() {
        visualizar(0);
    }

    public void visualizar(int tcase) {
        Relatorios relatorios = null;
        if (!getListRelatorios().isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(getListRelatorios().get(index[0]).getDescription()));
            if (!getListRelatorioOrdem().isEmpty()) {
                relatorios.setQryOrdem(((RelatorioOrdem) new Dao().find(new RelatorioOrdem(), Integer.parseInt(getListRelatorioOrdem().get(index[2]).getDescription()))).getQuery());
            }
        }
        if (relatorios == null) {
            return;
        }
        String order = "";
        String detalheRelatorio = "";
        if (parametroFechamentoGuias.isEmpty()) {
            String idInEmpresas = null;
            String idInBeneficiarios = null;
            String sit = null;
            if (!listEmpresas.isEmpty()) {
                for (int i = 0; i < listEmpresas.size(); i++) {
                    if (i == 0) {
                        idInEmpresas = "" + listEmpresas.get(i).getPessoa().getId();
                    } else {
                        idInEmpresas += ", " + listEmpresas.get(i).getPessoa().getId();
                    }

                }
            }
            if (!listFisica.isEmpty()) {
                for (int i = 0; i < listFisica.size(); i++) {
                    if (i == 0) {
                        idInBeneficiarios = "" + listFisica.get(i).getPessoa().getId();
                    } else {
                        idInBeneficiarios += ", " + listFisica.get(i).getPessoa().getId();
                    }

                }
            }
            List listDetalhePesquisa = new ArrayList();
            Map params = new HashMap();
            Date dtInicial = null;
            Date dtFinal = null;
            if (filtro[0]) {
                dtInicial = dataInicial;
                dtFinal = dataFinal;
            }
            List list = new RelatorioFechamentoGuiasDao().find(relatorios, idInEmpresas, idInBeneficiarios, dtInicial, dtFinal);
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
            ParametroFechamentoGuias pfg;
            params.put("detalhes_relatorio", detalheRelatorio);
            for (Object list1 : list) {
                BigDecimal valor = new BigDecimal(0);
                try {
                    valor = new BigDecimal(Float.parseFloat(AnaliseString.converteNullString(((List) list1).get(8))));
                } catch (NumberFormatException e) {

                }
                Date date = null;
                try {
                    date = (Date) ((List) list1).get(7);
                } catch (NumberFormatException e) {

                }
                pfg = new ParametroFechamentoGuias(
                        AnaliseString.converteNullString(((List) list1).get(0)),
                        Integer.parseInt(AnaliseString.converteNullString(((List) list1).get(1))),
                        Integer.parseInt(AnaliseString.converteNullString(((List) list1).get(2))),
                        AnaliseString.converteNullString(((List) list1).get(3)),
                        Integer.parseInt(AnaliseString.converteNullString(((List) list1).get(4))),
                        AnaliseString.converteNullString(((List) list1).get(5)),
                        AnaliseString.converteNullString(((List) list1).get(6)),
                        date,
                        valor
                );
                parametroFechamentoGuias.add(pfg);
            }
            Jasper.TYPE = "paisagem";
            Jasper.printReports(relatorios.getJasper(), relatorios.getNome(), parametroFechamentoGuias, params);
        }
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
        if (tipoRelatorio.equals("Resumo")) {
            clear();
        }
    }

    public void clear() {
        if (!filtro[0]) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
        }
        if (!filtro[1]) {
            listEmpresas.clear();
            index[2] = 0;
            index[3] = 0;
            index[4] = 0;
            listSelectItem[1] = new ArrayList();
            listSelectItem[3] = new ArrayList();
            listSelectItem[4] = new ArrayList();
        }
        if (!filtro[2]) {
            listFisica.clear();
        }
    }

    public void clear(Integer tCase) {
        switch (tCase) {
            case 1:
                listSelectItem[3] = new ArrayList();
                listSelectItem[4] = new ArrayList();
                index[3] = 0;
                index[4] = 0;
                getListSubGrupo();
                getListJuridica();
                break;
            case 2:
                listSelectItem[4] = new ArrayList();
                index[4] = 0;
                getListSubGrupo();
                getListJuridica();
        }

    }

    public void close(String close) {
        switch (close) {
            case "pagamento":
                filtro[0] = false;
                dataInicial = DataHoje.dataHoje();
                dataFinal = DataHoje.dataHoje();
                break;
            case "empresa":
                filtro[1] = false;
                listEmpresas.clear();
                index[2] = 0;
                index[3] = 0;
                index[4] = 0;
                listSelectItem[1] = new ArrayList();
                listSelectItem[3] = new ArrayList();
                listSelectItem[4] = new ArrayList();
                break;
            case "beneficiario":
                filtro[2] = false;
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

    /**
     * <strong>Index</strong>
     * <ul>
     * <li>[0] Tipos de Relatórios</li>
     * <li>[1] Ordem Relatório </li>
     * <li>[2] Grupo </li>
     * <li>[3] SubGrupo </li>
     * <li>[4] Empresa </li>
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
     * <li>[0] EMISSÃO</li>
     * <li>[1] EMPRESA</li>
     * <li>[2] BENEFICIÁRIO</li>
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<SelectItem> getListRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = db.pesquisaTipoRelatorio(132);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = i;
                }
                listSelectItem[0].add(new SelectItem(i,
                        list.get(i).getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListRelatorioOrdem() {
        listSelectItem[1].clear();
        if (index[0] != null) {
            if (!getListRelatorios().isEmpty()) {
                RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
                List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(Integer.parseInt(getListRelatorios().get(index[0]).getDescription()));
                if (list.isEmpty()) {
                    index[1] = null;
                }
                for (int i = 0; i < list.size(); i++) {
                    listSelectItem[1].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
                }
            }
        }
        return listSelectItem[1];
    }

    public List<SelectItem> getListGrupo() {
        if (listSelectItem[2].isEmpty()) {
            List<GrupoConvenio> list = (List<GrupoConvenio>) new Dao().list(new GrupoConvenio(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[2] = i;
                }
                listSelectItem[2].add(new SelectItem(i, (String) list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
            if (listSelectItem[2].isEmpty()) {
                listSelectItem[2] = new ArrayList();
            }
        }
        return listSelectItem[2];
    }

    public List<SelectItem> getListSubGrupo() {
        if (listSelectItem[3].isEmpty()) {
            if (!listSelectItem[2].isEmpty()) {
                SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
                List<SubGrupoConvenio> list = (List<SubGrupoConvenio>) db.listaSubGrupoConvenioPorGrupo(Integer.parseInt(listSelectItem[2].get(index[2]).getDescription()));
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        index[3] = i;
                    }
                    listSelectItem[3].add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
                }
                if (listSelectItem[3].isEmpty()) {
                    listSelectItem[3] = new ArrayList();
                }
            }

        }
        return listSelectItem[3];
    }

    public List<SelectItem> getListJuridica() {
        if (listSelectItem[4].isEmpty()) {
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            if (getListSubGrupo().isEmpty()) {
                return listSelectItem[4];
            }
            List<Juridica> list = (List<Juridica>) db.listaEmpresaConveniadaPorSubGrupo(Integer.parseInt(getListSubGrupo().get(index[3]).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[4].add(new SelectItem(i, list.get(i).getPessoa().getNome(), Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[4];
    }

    public List<Fisica> getListFisica() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            Fisica f = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
            boolean exist = false;
            for (int i = 0; i < listFisica.size(); i++) {
                if (listFisica.get(i).getId() == f.getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                listFisica.add(f);
            }
        }
        if (GenericaSessao.exists("fisicaPesquisaList")) {
            listFisica.clear();
            listFisica.addAll(GenericaSessao.getList("fisicaPesquisaList"));
            ((FisicaBean) GenericaSessao.getObject("fisicaBean")).getSelectedFisica().clear();
            GenericaSessao.remove("fisicaPesquisaList");
        }
        return listFisica;
    }

    public void setListFisica(List<Fisica> listFisica) {
        this.listFisica = listFisica;
    }

    public void removeSelected(Fisica f) {
        listFisica.remove(f);
    }

    public void removeSelected(Juridica j) {
        listEmpresas.remove(j);
    }

    public void addEmpresa() {
        boolean exist = false;
        Juridica j = (Juridica) new Dao().find(new Juridica(), Integer.parseInt(getListJuridica().get(index[4]).getDescription()));
        if (j != null) {
            for (int i = 0; i < listEmpresas.size(); i++) {
                if (listEmpresas.get(i).getId() == j.getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                listEmpresas.add(j);
            }
        }
    }

    public List<Juridica> getListEmpresas() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            Juridica j = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            boolean exist = false;
            for (int i = 0; i < listEmpresas.size(); i++) {
                if (listEmpresas.get(i).getId() == j.getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                listEmpresas.add(j);
            }
        }
        return listEmpresas;
    }

    public void setListEmpresas(List<Juridica> listEmpresas) {
        this.listEmpresas = listEmpresas;
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

    public void selecionaDataInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public List<ParametroFechamentoGuias> getParametroFechamentoGuias() {
        return parametroFechamentoGuias;
    }

    public void setParametroFechamentoGuias(List<ParametroFechamentoGuias> parametroFechamentoGuias) {
        this.parametroFechamentoGuias = parametroFechamentoGuias;
    }

    public List<SelectItem>[] getListSelectItem() {
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }
}
