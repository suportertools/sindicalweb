package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AutorizaImpressaoCartao;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.HistoricoCarteirinha;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.CategoriaDB;
import br.com.rtools.associativo.db.CategoriaDBToplink;
import br.com.rtools.associativo.db.SocioCarteirinhaDB;
import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.impressao.Etiquetas;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.ImpressaoParaSocios;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
// import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;

@ManagedBean
@SessionScoped
public class CartaoSocialBean implements Serializable {

    /**
     * NOVO *
     */
    private String descricao = "";
    private List<Vector> listaCarteirinha = new ArrayList();
    private List<Vector> filteredCarteirinha = new ArrayList();
    private List<Vector> listaSelecionado = new ArrayList();
    private List listaHistorico = new ArrayList();
    private String por = "";
    private String porLabel = "";
    private String indexOrdem = "0";
    private Integer page;
    private Boolean toggle = false;
    private Integer firstIndex = 0;
    private Integer lastIndex = 0;

    public CartaoSocialBean() {
        this.naoImpressoTodos();
    }

    public void historicoCarteirinha() {
        if (listaSelecionado.size() > 0) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();

            listaHistorico.clear();
            for (int i = 0; i < listaSelecionado.size(); i++) {
                List<HistoricoCarteirinha> listah = db.listaHistoricoCarteirinha((Integer) listaSelecionado.get(i).get(0));
                for (HistoricoCarteirinha listah1 : listah) {

                    AutorizaImpressaoCartao ai = db.pesquisaAutorizaPorHistorico(listah1.getId());

                    listaHistorico.add(new DataObject(listah1, "", listaSelecionado.get(i).get(5) + " - " + listaSelecionado.get(i).get(7), ai));

                }
            }
        }
    }

    public void naoImpressoTodos() {
        por = "niEmpresaTodos";
        porLabel = "Lista de TODOS NÃO IMPRESSOS";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", "", indexOrdem);
    }

    public void naoImpressoEmpresa() {
        por = "niEmpresa";
        porLabel = "Pesquisa por Não Impressos / EMPRESAS";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", descricao, indexOrdem);
    }

    public void naoImpressoCNPJ() {
        por = "niCNPJ";
        porLabel = "Pesquisa por Não Impressos / CNPJ";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niCNPJ", descricao, indexOrdem);
    }

    public void impressoTodos() {
        por = "iEmpresaTodos";
        porLabel = "Lista de TODOS IMPRESSOS";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", "", indexOrdem);
    }

    public void impressoEmpresa() {
        por = "iEmpresa";
        porLabel = "Pesquisa por Impressos / EMPRESAS";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", descricao, indexOrdem);
    }

    public void impressoCNPJ() {
        por = "iCNPJ";
        porLabel = "Pesquisa por Impressos / CNPJ";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCNPJ", descricao, indexOrdem);
    }

    public void impressoDias() {
        por = "iDias";
        porLabel = "Pesquisa por Impressos / ÚLTIMOS 30 DIAS";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iDias", descricao, indexOrdem);
    }

    public void pessoaNome() {
        por = "iNome";
        porLabel = "Pesquisa por Pessoa / NOME";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iNome", descricao, indexOrdem);
    }

    public void sociosMatricula() {
        por = "iMatricula";
        porLabel = "Pesquisa por Sócio / MATRÍCULA";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iMatricula", descricao, indexOrdem);
    }

    public void pessoaID() {
        por = "iID";
        porLabel = "Pesquisa por Pessoa / Código";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        if (!descricao.isEmpty()) {
            listaCarteirinha = db.pesquisaCarteirinha("iID", descricao, indexOrdem);
        } else {
            listaCarteirinha = new ArrayList<Vector>();
        }
    }

    public void pessoaCPF() {
        por = "iCPF";
        porLabel = "Pesquisa por Pessoa / CPF";

        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCPF", descricao, indexOrdem);
    }

    public void pesquisar() {
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        if (por.equals("niEmpresa")) {
            listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", descricao, indexOrdem);
        }

        if (por.equals("niCNPJ")) {
            listaCarteirinha = db.pesquisaCarteirinha("niCNPJ", descricao, indexOrdem);
        }

        if (por.equals("iEmpresa")) {
            listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", descricao, indexOrdem);
        }

        if (por.equals("iCNPJ")) {
            listaCarteirinha = db.pesquisaCarteirinha("iCNPJ", descricao, indexOrdem);
        }

        if (por.equals("iDias")) {
            listaCarteirinha = db.pesquisaCarteirinha("iDias", descricao, indexOrdem);
        }

        if (por.equals("iNome")) {
            listaCarteirinha = db.pesquisaCarteirinha("iNome", descricao, indexOrdem);
        }

        if (por.equals("iID")) {
            listaCarteirinha = db.pesquisaCarteirinha("iID", descricao, indexOrdem);
        }

        if (por.equals("iMatricula")) {
            listaCarteirinha = db.pesquisaCarteirinha("iMatricula", descricao, indexOrdem);
        }

        if (por.equals("iCPF")) {
            listaCarteirinha = db.pesquisaCarteirinha("iCPF", descricao, indexOrdem);
        }
    }

    public void imprimirCarteirinha() {
        imprimirCarteirinha(null);
    }

    public void imprimirCarteirinha(Vector vector) {
        Dao dao = new Dao();
        List<Vector> list = new ArrayList();
        if (!listaSelecionado.isEmpty() && vector == null) {
            list = listaSelecionado;
        } else if (vector != null) {
            list.add(vector);
        }
        if (!list.isEmpty()) {
            dao.openTransaction();
            SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
            for (int i = 0; i < list.size(); i++) {
                Pessoa pessoa = (Pessoa) dao.find(new Pessoa(), (Integer) ((List) list.get(i)).get(0));
                SocioCarteirinha carteirinha = (SocioCarteirinha) dao.find(new SocioCarteirinha(), (Integer) ((List) list.get(i)).get(19));

                //ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinha(-1, 170);
                //ModeloCarteirinha modeloc = (ModeloCarteirinha) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(19), "ModeloCarteirinha");
                //carteirinha = dbc.pesquisaCarteirinhaPessoa(pessoa.getId(), modeloc.getId());
                if (carteirinha.getDtEmissao() == null) {

                    carteirinha.setEmissao(DataHoje.data());
                    if (!dao.update(carteirinha)) {
                        dao.rollback();
                        return;
                    }
                    list.get(i).set(6, carteirinha.getValidadeCarteirinha());
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("Primeira Impressão de Carteirinha");

                    if (list.get(i).get(17) != null) {
                        Movimento m = (Movimento) dao.find(new Movimento(), Integer.valueOf(list.get(i).get(17).toString()));
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!dao.save(hc)) {
                        dao.rollback();
                        return;
                    }

                    //AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), modeloc.getId());
                    AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), carteirinha.getModeloCarteirinha().getId());

                    if (ai != null) {
                        ai.setHistoricoCarteirinha(hc);
                        if (!dao.update(ai)) {
                            dao.rollback();
                            return;
                        }
                    }

                } else {
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("Impressão de Carteirinha");

                    if (list.get(i).get(17) != null) {
                        Movimento m = (Movimento) dao.find(new Movimento(), Integer.valueOf(list.get(i).get(17).toString()));
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!dao.save(hc)) {
                        dao.rollback();
                        return;
                    }

                    //AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), modeloc.getId());
                    AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), carteirinha.getModeloCarteirinha().getId());

                    if (ai != null) {
                        ai.setHistoricoCarteirinha(hc);
                        if (!dao.update(ai)) {
                            dao.rollback();
                            return;
                        }
                    }
                }
            }

            if (ImpressaoParaSocios.imprimirCarteirinha(list)) {
                dao.commit();
            } else {
                dao.rollback();
            }
        }
    }

    public void reImprimirCarteirinha() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (!listaSelecionado.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            CategoriaDB dbCat = new CategoriaDBToplink();
            DataHoje dh = new DataHoje();
            SociosDB dbs = new SociosDBToplink();

            sv.abrirTransacao();

            for (int i = 0; i < listaSelecionado.size(); i++) {
                Pessoa pessoa = (Pessoa) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(0), "Pessoa");
                Socios socios = dbs.pesquisaSocioPorPessoa(pessoa.getId());
                SocioCarteirinha carteirinha = (SocioCarteirinha) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(19), "SocioCarteirinha");

                if (socios.getId() != -1 && socios.getMatriculaSocios().getId() != -1) {
                    GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(Integer.valueOf(socios.getMatriculaSocios().getCategoria().getId()));
                    Date validadeCarteirinha = DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data()));
                    carteirinha.setDtValidadeCarteirinha(validadeCarteirinha);
                } else {
                    carteirinha.setDtValidadeCarteirinha(null);
                }

                carteirinha.setVia(carteirinha.getVia() + 1);
                listaSelecionado.get(i).set(6, carteirinha.getValidadeCarteirinha());

                if (carteirinha.getDtEmissao() == null) {
                    carteirinha.setEmissao(DataHoje.data());

                    if (!sv.alterarObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return;
                    }

                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("Primeira ReImpressão de Carteirinha 2º Via");

                    if (listaSelecionado.get(i).get(17) != null) {
                        Movimento m = (Movimento) sv.pesquisaCodigo(Integer.valueOf(listaSelecionado.get(i).get(17).toString()), "Movimento");
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                } else {
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    carteirinha.setVia(carteirinha.getVia() + 1);

                    if (!sv.alterarObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return;
                    }

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("ReImpressão de Carteirinha 2º Via");

                    if (listaSelecionado.get(i).get(17) != null) {
                        Movimento m = (Movimento) sv.pesquisaCodigo(Integer.valueOf(listaSelecionado.get(i).get(17).toString()), "Movimento");
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                }
            }

            if (ImpressaoParaSocios.imprimirCarteirinha(listaSelecionado)) {
                sv.comitarTransacao();
            } else {
                sv.desfazerTransacao();
            }
        }
    }

    public String imprimirEtiqueta() {
        SocioCarteirinhaDB dbs = new SocioCarteirinhaDBToplink();

        List<Etiquetas> listax = new ArrayList();
        for (int i = 0; i < listaSelecionado.size(); i++) {
            List l = (List) dbs.listaPesquisaEtiqueta((Integer) ((List) listaSelecionado.get(i)).get(0)).get(0);
            listax.add(new Etiquetas(String.valueOf(l.get(0)),
                    String.valueOf(l.get(1)),
                    String.valueOf(l.get(2)),
                    String.valueOf(l.get(3)),
                    String.valueOf(l.get(4)),
                    String.valueOf(l.get(5)),
                    String.valueOf(l.get(6)),
                    String.valueOf(l.get(7)),
                    String.valueOf(l.get(8))));
        }

        if (listax.isEmpty()) {
            return null;
        }

        try {
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
            JasperReport jasper;
            String nomeArq;

            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ETIQUETA_SOCIO.jasper"));
            jasper = (JasperReport) JRLoader.loadObject(fl);
            nomeArq = "etiqueta_coluna_";

            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = nomeArq + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/etiquetas");
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String imprimirEtiquetaTermica() {
        SocioCarteirinhaDB dbs = new SocioCarteirinhaDBToplink();

        List<Etiquetas> listax = new ArrayList();
        for (int i = 0; i < listaSelecionado.size(); i++) {
            List l = (List) dbs.listaPesquisaEtiqueta((Integer) ((List) listaSelecionado.get(i)).get(0)).get(0);
            listax.add(new Etiquetas(String.valueOf(l.get(0)),
                    String.valueOf(l.get(1)),
                    String.valueOf(l.get(2)),
                    String.valueOf(l.get(3)),
                    String.valueOf(l.get(4)),
                    String.valueOf(l.get(5)),
                    String.valueOf(l.get(6)),
                    String.valueOf(l.get(7)),
                    String.valueOf(l.get(8))));
        }

        if (listax.isEmpty()) {
            return null;
        }

        try {
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
            JasperReport jasper;
            String nomeArq;

            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ETIQUETA_TERMICA_SOCIAL_RETRATO.jasper"));
            jasper = (JasperReport) JRLoader.loadObject(fl);
            nomeArq = "etiqueta_termica_";

            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = nomeArq + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/etiquetas");
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * ---- *
     */
    public String getIndexOrdem() {
        return indexOrdem;
    }

    public void setIndexOrdem(String indexOrdem) {
        this.indexOrdem = indexOrdem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Vector> getListaCarteirinha() {
        return listaCarteirinha;
    }

    public void setListaCarteirinha(List<Vector> listaCarteirinha) {
        this.listaCarteirinha = listaCarteirinha;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getPorLabel() {
        return porLabel;
    }

    public void setPorLabel(String porLabel) {
        this.porLabel = porLabel;
    }

    public List<Vector> getListaSelecionado() {
        return listaSelecionado;
    }

    public void setListaSelecionado(List<Vector> listaSelecionado) {
//        if (toggle != null || toggle) {
//            this.listaSelecionado = listaSelecionado;
//            toggle = true;
//        }
    }

    public List getListaHistorico() {
        return listaHistorico;
    }

    public void setListaHistorico(List listaHistorico) {
        this.listaHistorico = listaHistorico;
    }

    public List<Vector> getFilteredCarteirinha() {
        return filteredCarteirinha;
    }

    public void setFilteredCarteirinha(List<Vector> filteredCarteirinha) {
        this.filteredCarteirinha = filteredCarteirinha;
    }

    public void selectedPage(PageEvent event) {
        page = event.getPage();
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void onRowSelect(SelectEvent event) {
        listaSelecionado.add(((Vector) event.getObject()));
    }

    public void onRowUnselect(UnselectEvent event) {
        for (int i = 0; i < listaSelecionado.size(); i++) {
            if (((Vector) listaSelecionado.get(i)).get(0) == ((Vector) event.getObject()).get(0)) {
                listaSelecionado.remove(i);
                break;
            }
        }
    }

    public String clear() {
        listaSelecionado.clear();
        page = null;
        return "cartaoSocial";
    }

    public void listernetFilter(FilterEvent filterEvent) {
        toggle = true;
    }

    public void selectedAll(Boolean all) {
        if (!listaSelecionado.isEmpty() && listaSelecionado.size() == listaCarteirinha.size()) {
            listaSelecionado.clear();
        } else {
            listaSelecionado.clear();
            listaSelecionado.addAll(listaCarteirinha);
        }
    }

    public void selectedAll() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        toggle = false;
        selectedAll(false);
        UIComponent uIComponent = PF.findComponent("i_btn_toggle");
        PF.update("formCartaoSocial:i_panel_grid");
    }

    public Boolean getToggle() {
        return toggle;
    }

    public void setToggle(Boolean toggle) {
        this.toggle = toggle;
    }

    public Integer getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(Integer firstIndex) {
        if (this.firstIndex > listaCarteirinha.size()) {
            this.firstIndex = 0;
        }
        this.firstIndex = firstIndex;
    }

    public Integer getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(Integer lastIndex) {
        if (firstIndex > 0) {
            if (this.lastIndex == 0) {
                this.lastIndex = this.firstIndex;
            } else {
                this.lastIndex = lastIndex;
            }
        } else {
            if (this.lastIndex < this.firstIndex) {
                this.lastIndex = this.firstIndex;
            }
            if (this.firstIndex > listaCarteirinha.size()) {
                this.lastIndex = listaCarteirinha.size();
            }
            if (this.lastIndex == 0) {
                this.lastIndex = this.firstIndex;
            }
        }
    }

    public String loadSelecteds() {
        if (firstIndex == 0 && lastIndex == 0) {
            return null;
        }
        listaSelecionado.clear();
        for (int i = firstIndex - 1; i < lastIndex; i++) {
            listaSelecionado.add(listaCarteirinha.get(i));
        }
        return "cartaoSocial";
    }

}
