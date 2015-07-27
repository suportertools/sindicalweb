package br.com.rtools.seguranca.beans;

import br.com.rtools.seguranca.Log;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.db.PesquisaLogDB;
import br.com.rtools.seguranca.db.PesquisaLogDao;
import br.com.rtools.seguranca.db.RotinaDao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class PesquisaLogBean implements Serializable {

    private List<SelectItem>[] listSelectItem;
    private List<Log> listLogs;
    private Boolean[] filtro;
    private Boolean[] filtroEvento;
    private Boolean blockUsuario;
    private Boolean blockRotina;
    private Date[] data;
    private String[] hora;
    private Integer[] index;
    private String tipo;
    private String indexAccordion;
    private String porPesquisa;
    private String descPesquisa;
    private Usuario usuario;
    private Log log;

    @PostConstruct
    public void init() {
        filtro = new Boolean[5];
        filtro[0] = false;
        filtro[1] = false;
        filtro[2] = false;
        filtro[3] = false;
        filtro[4] = false;
        filtroEvento = new Boolean[4];
        filtro[0] = false;
        filtro[1] = false;
        filtro[2] = false;
        filtro[3] = false;
        blockUsuario = false;
        blockRotina = false;
        listLogs = new ArrayList<>();
        listSelectItem = new ArrayList[1];
        listSelectItem[0] = new ArrayList<>();
        data = new Date[2];
        data[0] = DataHoje.dataHoje();
        data[1] = DataHoje.dataHoje();
        hora = new String[2];
        hora[0] = "";
        hora[1] = "";
        index = new Integer[1];
        index[0] = 0;
        tipo = "Avançado";
        indexAccordion = "Avançado";
        porPesquisa = "";
        descPesquisa = "";
        usuario = new Usuario();
        log = new Log();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("pesquisaLogBean");
        GenericaSessao.remove("usuarioPesquisa");
        GenericaSessao.remove("pessoaPesquisa");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("removeFiltro");
    }

    public List<SelectItem> getListRotinas() {
        if (listSelectItem[0].isEmpty()) {
            PesquisaLogDao pld = new PesquisaLogDao();
            List<Rotina> list = (List<Rotina>) pld.listRotinasLogs();
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getRotina(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public void typeChange(TabChangeEvent event) {
        tipo = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
    }

    public void selectedDataInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.data[0] = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.data[1] = DataHoje.converte(format.format(event.getObject()));
    }

    public void clear() {
        if (!filtro[0]) {
            listSelectItem = new ArrayList[1];
            listSelectItem[0] = new ArrayList<>();
        }
        if (!filtro[1]) {
            data[0] = DataHoje.dataHoje();
            data[1] = DataHoje.dataHoje();
            hora[0] = "";
            hora[1] = "";
        }
        if (!filtro[2]) {
            usuario = new Usuario();
        }
        if (!filtro[3]) {
            filtroEvento[0] = false;
            filtroEvento[1] = false;
            filtroEvento[2] = false;
            filtroEvento[3] = false;
        }
        if (!filtro[4]) {
            porPesquisa = "";
            descPesquisa = "";
        }
    }

    public void close(String close) {
        if (close.equals("periodo")) {
            filtro[0] = false;
            data[0] = DataHoje.dataHoje();
            data[1] = DataHoje.dataHoje();
            hora[0] = "";
            hora[1] = "";
        } else if (close.equals("usuario")) {
            filtro[1] = false;
            usuario = new Usuario();
        } else if (close.equals("rotina")) {
            filtro[2] = false;
            listSelectItem = new ArrayList[1];
            listSelectItem[0] = new ArrayList<>();
        } else if (close.equals("evento")) {
            filtro[3] = false;
            filtroEvento[0] = false;
            filtroEvento[1] = false;
            filtroEvento[2] = false;
            filtroEvento[3] = false;
        } else if (close.equals("descricao")) {
            filtro[4] = false;
            descPesquisa = "";
            porPesquisa = "";
        }
        PF.update("form_logs:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
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
     * <li>[0] List[SelectItem] Evento</li>
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

    public void removeFiltro() {
        GenericaSessao.put("removeFiltro", true);
    }

    /**
     * <strong>Filtros</strong>
     * <ul>
     * <li>[0] Periodo</li>
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

    public String inIdEventos() {
        List listIds = new ArrayList();
        String ids = "";
        if (filtroEvento[0]) {
            listIds.add("1");
        }
        if (filtroEvento[1]) {
            listIds.add("2");
        }
        if (filtroEvento[2]) {
            listIds.add("3");
        }
        if (filtroEvento[3]) {
            listIds.add("4");
        }
        for (int i = 0; i < listIds.size(); i++) {
            if (i == 0) {
                ids += listIds.get(i).toString();
            } else {
                ids += "," + listIds.get(i).toString();
            }
        }
        return ids;
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("usuarioPesquisa")) {
            usuario = (Usuario) GenericaSessao.getObject("usuarioPesquisa");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * <strong>Filtros Evento</strong>
     * <ul>
     * <li>[0](1)Inclusão</li>
     * <li>[1](2)Exclusão</li>
     * <li>[2](3)Alteração</li>
     * <li>[3](4)Consulta</li>
     * </ul>
     *
     * @return boolean
     */
    public Boolean[] getFiltroEvento() {
        return filtroEvento;
    }

    public void setFiltroEvento(Boolean[] filtroEvento) {
        this.filtroEvento = filtroEvento;
    }

    public List<Log> getListLogs() {
        if (listLogs.isEmpty()) {
            String dtInicial = null;
            String dtFinal = null;
            String hrInicial = null;
            String hrFinal = null;
            int idR = 0;
            int idU = 0;
            String idInEventos = null;
            if (filtro[0]) {
                dtInicial = DataHoje.converteData(data[0]);
                dtFinal = DataHoje.converteData(data[1]);
                hrInicial = hora[0];
                hrFinal = hora[1];
            }
            if (filtro[1]) {
                if (usuario.getId() != -1) {
                    idU = usuario.getId();
                }
            }
            if (filtro[2]) {
                if (!getListRotinas().isEmpty()) {
                    idR = Integer.parseInt(getListRotinas().get(index[0]).getDescription());
                }
            }
            if (filtro[3]) {
                if (!inIdEventos().isEmpty()) {
                    idInEventos = inIdEventos();
                }
            }
            if (filtro[4]) {

            }
            PesquisaLogDao pld = new PesquisaLogDao();
            listLogs = (List<Log>) pld.pesquisaLogs(dtInicial, dtFinal, hrInicial, hrFinal, idU, idR, idInEventos, descPesquisa);
        }
        return listLogs;
    }

    public void setListLogs(List<Log> listLogs) {
        this.listLogs = listLogs;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    /**
     * <strong>Data</strong>
     * <ul>
     * <li>[0]Inicial</li>
     * <li>[0]Final</li>
     * </ul>
     *
     * @return Date
     */
    public Date[] getData() {
        return data;
    }

    public void setData(Date[] data) {
        this.data = data;
    }

    /**
     * <strong>Hora</strong>
     * <ul>
     * <li>[0]Inicial</li>
     * <li>[0]Final</li>
     * </ul>
     *
     * @return Date
     */
    public String[] getHora() {
        return hora;
    }

    public void setHora(String[] hora) {
        this.hora = hora;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void details(Log l) {
        log = new Log();
        log = l;
    }

    public void listenerBlockUsuario() {
        GenericaSessao.remove("pesquisaLogBean");
        PesquisaLogBean pesquisaLogBean = new PesquisaLogBean();
        pesquisaLogBean.init();
        HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String pagina = ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).converteURL(paginaRequerida.getRequestURI());
        // PesquisaLogBean pesquisaLogBean = new PesquisaLogBean();
        RotinaDao rotinaDao = new RotinaDao();
        Rotina r = rotinaDao.pesquisaRotinaPorPagina(pagina);
        if (r == null) {
            r = rotinaDao.pesquisaRotinaPorAcao(pagina);
        }
        getListLogs().clear();
        Integer[] integer = new Integer[]{0};
        getListRotinas().clear();
        getListRotinas();
        if (getListRotinas().isEmpty()) {
            GenericaMensagem.info("Sistema", "Nenhum Log encontrado");
        }
        if (r != null) {
            for (int i = 0; i < listSelectItem[0].size(); i++) {
                if (Integer.parseInt(listSelectItem[0].get(i).getDescription()) == r.getId()) {
                    integer[0] = i;
                    break;
                }
            }
        }
        if (integer[0] == 0) {
            pesquisaLogBean.setBlockRotina(false);
            GenericaMensagem.info("Sistema", "Nenhum Log encontrado para esta rotina!");
        } else {
            pesquisaLogBean.setBlockRotina(true);
        }
        // GenericaSessao.put("pesquisaLogBean", pesquisaLogBean);
        filtro[1] = true;
        filtro[2] = true;
        pesquisaLogBean.setUsuario((Usuario) GenericaSessao.getObject("sessaoUsuario"));
        pesquisaLogBean.setBlockUsuario(true);
        pesquisaLogBean.setIndex(integer);
        pesquisaLogBean.setFiltro(filtro);
        //pesquisaLogBean.setIndex(integer);
        pesquisaLogBean.getListRotinas();
        pesquisaLogBean.getListLogs();
        pesquisaLogBean.getFiltroEvento();
        GenericaSessao.put("pesquisaLogBean", pesquisaLogBean);

    }

    public Boolean getBlockUsuario() {
        return blockUsuario;
    }

    public void setBlockUsuario(Boolean blockUsuario) {
        this.blockUsuario = blockUsuario;
    }

    public Boolean getBlockRotina() {
        return blockRotina;
    }

    public void setBlockRotina(Boolean blockRotina) {
        this.blockRotina = blockRotina;
    }
}
