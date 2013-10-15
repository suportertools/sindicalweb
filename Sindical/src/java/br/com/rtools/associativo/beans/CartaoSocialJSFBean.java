package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.CategoriaDB;
import br.com.rtools.associativo.db.CategoriaDBToplink;
import br.com.rtools.associativo.db.SocioCarteirinhaDB;
import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
import br.com.rtools.impressao.Etiquetas;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.ImpressaoParaSocios;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
// import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class CartaoSocialJSFBean {

    private String indexFiltro = "0";
    private String indexOrdem = "0";
    private String descEmpresa = "";
    private List<DataObject> listaCartao = new ArrayList();
    private boolean marcarTodos = false;
    private int total = 0;
    private int qnt = 0;
    private boolean fantasia = false;
    private boolean disabledReimpressao = true;
    private String tipoEtiquetas = "termica";
    private boolean updateGrid = false;
    private boolean updateGridx = false;

    public String visualizarEtiqueta() {
        if (tipoEtiquetas.equals("nenhuma")) {
            return null;
        }

        SocioCarteirinhaDB dbs = new SocioCarteirinhaDBToplink();

        List lista = new ArrayList();
        for (int i = 0; i < listaCartao.size(); i++) {
            if ((Boolean) listaCartao.get(i).getArgumento0()) {
                lista.add((List) listaCartao.get(i).getArgumento1());
            }
        }

        List<Etiquetas> listax = new ArrayList();
        for (int i = 0; i < lista.size(); i++) {
            List l = (List) dbs.listaPesquisaEtiqueta((Integer) ((List) lista.get(i)).get(0)).get(0);
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
            if (tipoEtiquetas.equals("colunas")) {
                jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ETIQUETA_SOCIO.jasper"));
                nomeArq = "etiqueta_coluna_";
            } else {
                jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ETIQUETA_TERMICA_SOCIAL_RETRATO.jasper"));
                nomeArq = "etiqueta_termica_";
            }
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

    public String pesquisar() {
        listaCartao.clear();
        isDisabledReimpressao();
        return "cartaoSocial";
    }

    public void pesquisarx() {
        try {
            listaCartao.clear();
            isDisabledReimpressao();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/cartaoSocial.jsf");
        } catch (Exception e) {
        }
    }

    public String teste() {
        updateGrid = true;
        return null;
    }

    public String testex() {
        updateGridx = true;
        updateGrid = false;
        qnt = 0;
        return null;
    }

    public String imprimirCartao() {
        List listaaux = new ArrayList();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        for (int i = 0; i < listaCartao.size(); i++) {
            if ((Boolean) listaCartao.get(i).getArgumento0()) {
                listaaux.add((List) listaCartao.get(i).getArgumento1());
            }
        }

        if (!listaaux.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listaaux.size(); i++) {
                Socios socios = (Socios) sv.pesquisaCodigo((Integer) ((List) listaaux.get(i)).get(12), "Socios");
                SocioCarteirinha carteirinha = new SocioCarteirinha();
                if (db.pesquisaSocioCarteirinhaSocio(socios.getId()).isEmpty()) {

                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
                    if (!sv.inserirObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return null;
                    }
                    DataHoje dh = new DataHoje();

                    CategoriaDB dbCat = new CategoriaDBToplink();
                    GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());

                    socios.setNrViaCarteirinha(1);
                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
                    ((List) listaaux.get(i)).set(6, socios.getValidadeCarteirinha());
                    if (!sv.alterarObjeto(socios)) {
                        sv.desfazerTransacao();
                        return null;
                    }
                } else {
                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
                    if (!db.verificaSocioCarteirinhaExiste(socios.getId())) {
                        if (!sv.inserirObjeto(carteirinha)) {
                            sv.desfazerTransacao();
                            return null;
                        }
                    }
                    ((List) listaaux.get(i)).set(11, socios.getNrViaCarteirinha());
                }
            }


            if (ImpressaoParaSocios.imprimirCarteirinha(listaaux)) {
                sv.comitarTransacao();
            } else {
                sv.desfazerTransacao();
            }

            if (!tipoEtiquetas.equals("nenhuma")) {
                visualizarEtiqueta();
            }
        }
        return null;
    }

    public String reImprimirCartao() {
        List listaaux = new ArrayList();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        for (int i = 0; i < listaCartao.size(); i++) {
            if ((Boolean) listaCartao.get(i).getArgumento0()) {
                listaaux.add((List) listaCartao.get(i).getArgumento1());
            }
        }

        if (!listaaux.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listaaux.size(); i++) {
                Socios socios = (Socios) sv.pesquisaCodigo((Integer) ((List) listaaux.get(i)).get(12), "Socios");
                SocioCarteirinha carteirinha = new SocioCarteirinha();
                if (db.pesquisaSocioCarteirinhaSocio(socios.getId()).isEmpty()) {

                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
                    if (!sv.inserirObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return null;
                    }
                    DataHoje dh = new DataHoje();

                    CategoriaDB dbCat = new CategoriaDBToplink();
                    GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());

                    socios.setNrViaCarteirinha(1);
                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));

                    if (!sv.alterarObjeto(socios)) {
                        sv.desfazerTransacao();
                        return null;
                    }
                } else {
                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
                    if (!db.verificaSocioCarteirinhaExiste(socios.getId())) {
                        if (!sv.inserirObjeto(carteirinha)) {
                            sv.desfazerTransacao();
                            return null;
                        }
                    }
                    DataHoje dh = new DataHoje();

                    socios.setNrViaCarteirinha(socios.getNrViaCarteirinha() + 1);
                    CategoriaDB dbCat = new CategoriaDBToplink();
                    GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));

                    ((List) listaaux.get(i)).set(6, socios.getValidadeCarteirinha());
                    ((List) listaaux.get(i)).set(11, socios.getNrViaCarteirinha());
                    if (!sv.alterarObjeto(socios)) {
                        sv.desfazerTransacao();
                        return null;
                    }
                }
            }

            if (ImpressaoParaSocios.imprimirCarteirinha(listaaux)) {
                sv.comitarTransacao();
                listaCartao.clear();
            } else {
                sv.desfazerTransacao();
            }
        }
        return "cartaoSocial";
    }

    public List<DataObject> getListaCartao() {
        if (listaCartao.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            List result = db.listaFiltro(indexFiltro, descEmpresa, indexOrdem, fantasia);
            for (int i = 0; i < result.size(); i++) {
                listaCartao.add(new DataObject(marcarTodos, result.get(i)));
            }
            updateGridx = false;
        }
        return listaCartao;
    }

    public void setListaCartao(List<DataObject> listaCartao) {
        this.listaCartao = listaCartao;
    }

    public String getIndexFiltro() {
        return indexFiltro;
    }

    public void setIndexFiltro(String indexFiltro) {
        this.indexFiltro = indexFiltro;
    }

    public String getDescEmpresa() {
        return descEmpresa;
    }

    public void setDescEmpresa(String descEmpresa) {
        this.descEmpresa = descEmpresa;
    }

    public String getIndexOrdem() {
        return indexOrdem;
    }

    public void setIndexOrdem(String indexOrdem) {
        this.indexOrdem = indexOrdem;
    }

    public int getTotal() {
        total = listaCartao.size();
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void marcar() {
        for (int i = 0; i < listaCartao.size(); i++) {
            listaCartao.get(i).setArgumento0(marcarTodos);
        }
        qnt = 0;
    }

    public void marcarUM() {
        qnt = 0;
    }

    public int getQnt() {
        if (qnt == 0) {
            for (int i = 0; i < listaCartao.size(); i++) {
                if ((Boolean) listaCartao.get(i).getArgumento0()) {
                    qnt += +1;
                }
            }
        }
        return qnt;
    }

    public boolean isFantasia() {
        return fantasia;
    }

    public void setFantasia(boolean fantasia) {
        this.fantasia = fantasia;
    }

    public boolean isMarcarTodos() {
        return marcarTodos;
    }

    public void setMarcarTodos(boolean marcarTodos) {
        this.marcarTodos = marcarTodos;
    }

    public String getTipoEtiquetas() {
        return tipoEtiquetas;
    }

    public void setTipoEtiquetas(String tipoEtiquetas) {
        this.tipoEtiquetas = tipoEtiquetas;
    }

    public boolean isDisabledReimpressao() {
        if (Integer.valueOf(indexFiltro) > 2) {
            disabledReimpressao = false;
        } else {
            disabledReimpressao = true;
        }
        return disabledReimpressao;
    }

    public void setDisabledReimpressao(boolean disabledReimpressao) {
        this.disabledReimpressao = disabledReimpressao;
    }

    public boolean isUpdateGrid() {
        return updateGrid;
    }

    public void setUpdateGrid(boolean updateGrid) {
        this.updateGrid = updateGrid;
    }

    public boolean isUpdateGridx() {
        return updateGridx;
    }

    public void setUpdateGridx(boolean updateGridx) {
        this.updateGridx = updateGridx;
    }
}