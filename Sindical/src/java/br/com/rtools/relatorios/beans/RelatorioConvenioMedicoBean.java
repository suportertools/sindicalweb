package br.com.rtools.relatorios.beans;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.dao.SubgrupoFinanceiroDao;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.impressao.ParametroConvenioMedico;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioConvenioMedicoDao;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
public class RelatorioConvenioMedicoBean implements Serializable {

    private Boolean[] filtro;
    private Integer[] index;
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private List<SelectItem> listRelatorio;
    private List<SelectItem> listRelatorioOrdem;
    private List<SelectItem> listSubgrupo;
    private List<SelectItem> listServicos;
    private Float faixaValorInicial;
    private Float faixaValorFinal;
    private List<ParametroConvenioMedico> parametroConvenioMedico;
    private String situacao;

    @PostConstruct
    public void init() {
        filtro = new Boolean[5];
        filtro[0] = false; // SUB GRUPO
        filtro[1] = false; // SERVIÇOS
        filtro[2] = false; // ATIVO
        filtro[3] = false; // VALOR
        filtro[4] = false; // SITUAÇÃO DO SERVIÇO
        index = new Integer[4];
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        index[3] = 0;
        tipoRelatorio = "Resumo";
        indexAccordion = "Resumo";
        tipo = "todos";
        listRelatorio = new ArrayList();
        listRelatorioOrdem = new ArrayList();
        listSubgrupo = new ArrayList<>();
        listServicos = new ArrayList<>();
        faixaValorInicial = new Float(0);
        faixaValorFinal = new Float(0);
        parametroConvenioMedico = new ArrayList<>();
        situacao = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioConvenioMedicoBean");
    }

    public void visualizar() {
        visualizar(0);
    }

    public void visualizar(int tcase) {
        Relatorios relatorios = null;
        if (!getListRelatorios().isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(getListRelatorios().get(index[0]).getDescription()));
        }
        if (relatorios == null) {
            return;
        }
        String order = "";
        String detalheRelatorio = "";
        if (parametroConvenioMedico.isEmpty()) {
            Integer idSg = null;
            Integer idSv = null;
            String sit = null;
            if (!listSubgrupo.isEmpty()) {
                idSg = Integer.parseInt(getListSubgrupo().get(index[2]).getDescription());
            }
            if (!listServicos.isEmpty()) {
                idSv = Integer.parseInt(getListServicos().get(index[3]).getDescription());
            }
            List listDetalhePesquisa = new ArrayList();
            Map params = new HashMap();
            if (filtro[4]) {
                sit = situacao;
            }
            List list = new RelatorioConvenioMedicoDao().find(relatorios, idSg, idSv, filtro[2], faixaValorInicial, faixaValorFinal, sit);
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
            ParametroConvenioMedico pcm;
            params.put("detalhes_relatorio", detalheRelatorio);
            for (Object list1 : list) {
                Integer idade = 0;
                try {
                    idade = Integer.parseInt(AnaliseString.converteNullString(((List) list1).get(3)));
                } catch (NumberFormatException e) {

                }
                BigDecimal valor = new BigDecimal(0);
                try {
                    valor = new BigDecimal(Float.parseFloat(AnaliseString.converteNullString(((List) list1).get(6))));
                } catch (NumberFormatException e) {

                }
                pcm = new ParametroConvenioMedico(
                        AnaliseString.converteNullString(((List) list1).get(0)),
                        AnaliseString.converteNullString(((List) list1).get(1)),
                        AnaliseString.converteNullString(((List) list1).get(2)),
                        idade,
                        AnaliseString.converteNullString(((List) list1).get(4)),
                        AnaliseString.converteNullString(((List) list1).get(5)),
                        valor
                );
                parametroConvenioMedico.add(pcm);
            }
            Jasper.TYPE = "paisagem";
            Jasper.printReports(relatorios.getJasper(), "convenio_medico", parametroConvenioMedico, params);
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
            index[2] = 0;
            index[3] = 0;
            listSubgrupo.clear();
            listServicos.clear();
        }
        if (!filtro[1]) {
            index[3] = 0;
            listServicos.clear();
        }
        if (!filtro[2]) {

        }
        if (!filtro[3]) {
            faixaValorInicial = new Float(0);
            faixaValorFinal = new Float(0);
        }
        if (!filtro[4]) {
            situacao = "A";
        }
    }

    public void close(String close) {
        switch (close) {
            case "subgrupo":
                filtro[0] = false;
                index[2] = 0;
                index[3] = 0;
                listSubgrupo.clear();
                listServicos.clear();
                break;
            case "servicos":
                filtro[1] = false;
                index[3] = 0;
                listServicos.clear();
                break;
            case "isentos":
                filtro[2] = false;
                break;
            case "valor":
                filtro[3] = false;
                faixaValorInicial = new Float(0);
                faixaValorFinal = new Float(0);
                break;
            case "situacao":
                filtro[4] = false;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<SelectItem> getListRelatorios() {
        if (listRelatorio.isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = db.pesquisaTipoRelatorio(121);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = i;
                }
                listRelatorio.add(new SelectItem(i,
                        list.get(i).getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listRelatorio;
    }

    public List<SelectItem> getListRelatorioOrdem() {
        listRelatorioOrdem.clear();
        if (index[0] != null) {
            if (!getListRelatorios().isEmpty()) {
                RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
                List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(Integer.parseInt(getListRelatorios().get(index[0]).getDescription()));
                if (list.isEmpty()) {
                    index[1] = null;
                }
                for (int i = 0; i < list.size(); i++) {
                    listRelatorioOrdem.add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
                }
            }
        }
        return listRelatorioOrdem;
    }

    public List<SelectItem> getListSubgrupo() {
        if (listSubgrupo.isEmpty()) {
            SubgrupoFinanceiroDao subgrupoFinanceiroDao = new SubgrupoFinanceiroDao();
            List<SubGrupoFinanceiro> list = subgrupoFinanceiroDao.listaSubgrupoFinanceiroPorRotina(121);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[2] = i;
                }
                listSubgrupo.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listSubgrupo;
    }

    public void setListSubgrupo(List<SelectItem> listSubgrupo) {
        this.listSubgrupo = listSubgrupo;
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            ServicosDB servicosDB = new ServicosDBToplink();
            List<Servicos> list;
            if (listSubgrupo.isEmpty()) {
                String s = "";
                if (filtro[4]) {
                    s = "A";
                }
                list = servicosDB.listaServicoSituacao(121, s);
            } else {
                list = servicosDB.listaServicosPorSubGrupoFinanceiro(Integer.parseInt(listSubgrupo.get(index[2]).getDescription()));
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[3] = i;
                }
                listServicos.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listServicos;
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listServicos = listServicos;
    }

    public List<ParametroConvenioMedico> getParametroConvenioMedico() {
        return parametroConvenioMedico;
    }

    public void setParametroConvenioMedico(List<ParametroConvenioMedico> parametroConvenioMedico) {
        this.parametroConvenioMedico = parametroConvenioMedico;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Float getFaixaValorInicial() {
        return faixaValorInicial;
    }

    public void setFaixaValorInicial(Float faixaValorInicial) {
        this.faixaValorInicial = faixaValorInicial;
    }

    public Float getFaixaValorFinal() {
        return faixaValorFinal;
    }

    public void setFaixaValorFinal(Float faixaValorFinal) {
        this.faixaValorFinal = faixaValorFinal;
    }

    public String getFaixaValorInicialString() {
        return Moeda.converteR$Float(faixaValorInicial);
    }

    public void setFaixaValorInicialString(String faixaValorInicialString) {
        this.faixaValorInicial = Moeda.converteUS$(faixaValorInicialString);
    }

    public String getFaixaValorFinalString() {
        return Moeda.converteR$Float(faixaValorFinal);
    }

    public void setFaixaValorFinalString(String faixaValorFinalString) {
        this.faixaValorFinal = Moeda.converteUS$(faixaValorFinalString);
    }
}
