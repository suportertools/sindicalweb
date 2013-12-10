package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.HistoricoCarteirinha;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
// import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class CartaoSocialBean implements Serializable {
    /** NOVO **/
    private String descricao = "";
    private List<Vector> listaCarteirinha = new ArrayList();
    private List<Vector> listaSelecionado = new ArrayList();
    private List listaHistorico = new ArrayList();
    private String por = "";
    private String porLabel = "";
    private String indexOrdem = "0";
    
    public void historicoCarteirinha(){
        if (listaSelecionado.size() > 0){
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            listaHistorico.clear();
            for (int i = 0; i < listaSelecionado.size(); i++){
                List<HistoricoCarteirinha> listah = db.listaHistoricoCarteirinha( (Integer) listaSelecionado.get(i).get(12) );
                for (int w = 0; w < listah.size(); w++){
                    listaHistorico.add( new DataObject(listah.get(w), "", listaSelecionado.get(i).get(5)+" - "+listaSelecionado.get(i).get(7)) );
                }
            }
        }
    }
    
    public void naoImpressoEmpresa(){
        por = "niEmpresa";
        porLabel = "Pesquisa por Não Impressos / EMPRESAS";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", descricao, indexOrdem);
    }
    
    public void naoImpressoCNPJ(){
        por = "niCNPJ";
        porLabel = "Pesquisa por Não Impressos / CNPJ";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niCNPJ", descricao, indexOrdem);
    }
    
    public void impressoEmpresa(){
        por = "iEmpresa";
        porLabel = "Pesquisa por Impressos / EMPRESAS";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", descricao, indexOrdem);
    }    
    
    public void impressoCNPJ(){
        por = "iCNPJ";
        porLabel = "Pesquisa por Impressos / CNPJ";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCNPJ", descricao, indexOrdem);
    }    
    
    public void impressoDias(){
        por = "iDias";
        porLabel = "Pesquisa por Impressos / ÚLTIMOS 30 DIAS";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iDias", descricao, indexOrdem);
    }    
    
    public void sociosNome(){
        por = "iNome";
        porLabel = "Pesquisa por Sócios / NOME";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iNome", descricao, indexOrdem);
    }
    
    public void sociosCodigo(){
        por = "iCodigo";
        porLabel = "Pesquisa por Sócios / MATRÍCULA";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCodigo", descricao, indexOrdem);
    }
    
    public void sociosCPF(){
        por = "iCPF";
        porLabel = "Pesquisa por Sócios / CPF";
        
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCPF", descricao, indexOrdem);
    }
    
    public void pesquisar(){
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        if (por.equals("niEmpresa")){
            listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", descricao, indexOrdem);
        }
        
        if (por.equals("niCNPJ")){
            listaCarteirinha = db.pesquisaCarteirinha("niCNPJ", descricao, indexOrdem);
        }
        
        if (por.equals("iEmpresa")) {
            listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", descricao, indexOrdem);
        }
        
        if (por.equals("iCNPJ")){
            listaCarteirinha = db.pesquisaCarteirinha("iCNPJ", descricao, indexOrdem);
        }
        
        if (por.equals("iDias")){
            listaCarteirinha = db.pesquisaCarteirinha("iDias", descricao, indexOrdem);
        }
        
        if (por.equals("iNome")){
            listaCarteirinha = db.pesquisaCarteirinha("iNome", descricao, indexOrdem);
        }
        
        if (por.equals("iCodigo")){
            listaCarteirinha = db.pesquisaCarteirinha("iCodigo", descricao, indexOrdem);
        }
        
        if (por.equals("iCPF")){
            listaCarteirinha = db.pesquisaCarteirinha("iCPF", descricao, indexOrdem);
        }
    }
    

    public void imprimirCarteirinha() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (!listaSelecionado.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            sv.abrirTransacao();
            DataHoje dh = new DataHoje();
            for (int i = 0; i < listaSelecionado.size(); i++) {
                Socios socios = (Socios) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(12), "Socios");
                SocioCarteirinha carteirinha = new SocioCarteirinha();
                CategoriaDB dbCat = new CategoriaDBToplink();
                GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
                if (db.pesquisaSocioCarteirinhaSocio(socios.getId()).isEmpty()) {

                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
                    if (!sv.inserirObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return;
                    }
                    
                    socios.setNrViaCarteirinha(1);
                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
                    // ATUALIZA VALIDADE NA GRID
                    // ((List) listaaux.get(i)).set(6, socios.getValidadeCarteirinha());
                    listaSelecionado.get(i).set(6, socios.getValidadeCarteirinha());
                    if (!sv.alterarObjeto(socios)) {
                        sv.desfazerTransacao();
                        return;
                    }
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();
                    
                    hc.setSocios(socios);
                    hc.setDescricao("Primeira Impressão de Carteirinha");
                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                    
                } else {
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();
                    
                    hc.setSocios(socios);
                    hc.setDescricao("Impressão de Carteirinha");
                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
//                    carteirinha.setEmissao(DataHoje.data());
//                    carteirinha.setSocios(socios);
//                    if (!db.verificaSocioCarteirinhaExiste(socios.getId())) {
//                        if (!sv.inserirObjeto(carteirinha)) {
//                            sv.desfazerTransacao();
//                            return;
//                        }
//                    }
//                    
//                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
                    // ATUALIZA VALIDADE NA GRID
                    //((List) listaaux.get(i)).set(11, socios.getNrViaCarteirinha());
                    //((List) listaaux.get(i)).set(6, socios.getValidadeCarteirinha());
                    if (!sv.alterarObjeto(socios)) {
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
    

    public void reImprimirCarteirinha() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (!listaSelecionado.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            sv.abrirTransacao();
            DataHoje dh = new DataHoje();
            for (int i = 0; i < listaSelecionado.size(); i++) {
                Socios socios = (Socios) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(12), "Socios");
                SocioCarteirinha carteirinha = new SocioCarteirinha();
                CategoriaDB dbCat = new CategoriaDBToplink();
                GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
                if (db.pesquisaSocioCarteirinhaSocio(socios.getId()).isEmpty()) {

                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
                    if (!sv.inserirObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return;
                    }
                    
                    socios.setNrViaCarteirinha(1);
                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
                    listaSelecionado.get(i).set(6, socios.getValidadeCarteirinha());
                    // ATUALIZA VALIDADE NA GRID
                    // ((List) listaaux.get(i)).set(6, socios.getValidadeCarteirinha());
                    if (!sv.alterarObjeto(socios)) {
                        sv.desfazerTransacao();
                        return;
                    }
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();
                    
                    hc.setSocios(socios);
                    hc.setDescricao("Primeira Reimpressão de Carteirinha 2º Via");
                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                } else {
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();
                    hc.setSocios(socios);
                    hc.setDescricao("Reimpressão de Carteirinha 2º Via");
                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                    
                    
                    carteirinha.setEmissao(DataHoje.data());
                    carteirinha.setSocios(socios);
//                    if (!db.verificaSocioCarteirinhaExiste(socios.getId())) {
//                        if (!sv.inserirObjeto(carteirinha)) {
//                            sv.desfazerTransacao();
//                            return;
//                        }
//                    }
                    
                    socios.setNrViaCarteirinha(socios.getNrViaCarteirinha() + 1);
                    socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
                    
                    // ATUALIZA VALIDADE NA GRID
                    listaSelecionado.get(i).set(11, socios.getNrViaCarteirinha());
                    listaSelecionado.get(i).set(6, socios.getValidadeCarteirinha());
                    
                    //((List) listaaux.get(i)).set(11, socios.getNrViaCarteirinha());
                    //((List) listaaux.get(i)).set(6, socios.getValidadeCarteirinha());
                    if (!sv.alterarObjeto(socios)) {
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
           
            jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ETIQUETA_SOCIO.jasper"));
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
            
            jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ETIQUETA_TERMICA_SOCIAL_RETRATO.jasper"));
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
    
    /** ---- **/
    
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
        this.listaSelecionado = listaSelecionado;
    }

    public List getListaHistorico() {
        return listaHistorico;
    }

    public void setListaHistorico(List listaHistorico) {
        this.listaHistorico = listaHistorico;
    }
}