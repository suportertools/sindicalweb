//package br.com.rtools.financeiro.beans;
//
//import br.com.rtools.financeiro.ServicoValor;
//import java.io.Serializable;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//
//@ManagedBean
//@SessionScoped
//public class ServicoValorBean implements Serializable {
//
//    private ServicoValor servicoValor = new ServicoValor();
//    private String comoPesquisa = "T";
//    private String descPesquisa = "";
//
//    public String getComoPesquisa() {
//        return comoPesquisa;
//    }
//
//    public void setComoPesquisa(String comoPesquisa) {
//        this.comoPesquisa = comoPesquisa;
//    }
//
//    public String getDescPesquisa() {
//        return descPesquisa;
//    }
//
//    public void setDescPesquisa(String descPesquisa) {
//        this.descPesquisa = descPesquisa;
//    }
//
//    public ServicoValorBean() {
////        htmlTable = new HtmlDataTable();
//    }
//
//    public ServicoValor getServicoValor() {
//        return servicoValor;
//    }
//
//    public void setServicoValor(ServicoValor servicoValor) {
//        this.servicoValor = servicoValor;
//    }
//
////    public String salvar(){
////        ServicoValorDB db = new ServicoValorDBToplink();
////        if (servicoValor.getId()==-1){
////            db.insert(servicoValor);
////        }
////        else{
////            db.getEntityManager().getTransaction().begin();
////            if (db.update(servicoValor))
////            {db.getEntityManager().getTransaction().commit();}
////            else
////            {db.getEntityManager().getTransaction().rollback();}
////        }
////        return null;
////    }
////
////   public String novo(){
////       servicoValor = new ServicoValor();
////       return "cadServicoValor";
////   }
////
////   public String excluir(){
////        ServicoValorDB db = new ServicoValorDBToplink();
////        if (servicoValor.getId()!=-1){
////            db.getEntityManager().getTransaction().begin();
////            servicoValor = db.pesquisaCodigo(servicoValor.getId());
////            if (db.delete(servicoValor))
////            {db.getEntityManager().getTransaction().commit();}
////            else
////            {db.getEntityManager().getTransaction().rollback();}
////        }
////       servicoValor = new ServicoValor();
////       return "pesquisaServicoValor";
////   }
//    public List getListaServicoValor() {
////       Pesquisa pesqisa = new Pesquisa();
//        List result = null;
////       result = pesquisa.pesquisar("ServicoValor", "servicoValor" , descPesquisa, "servicos", comoPesquisa);
//        return result;
//    }
//
//    public void refreshForm() {
//    }
//    /*
//     public Logradouro getLogradouroPesquisa(){
//     try{
//     Logradouro c = (Logradouro) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("logradouroPesquisa");
//     if (c == null){
//     c = new Logradouro();
//     }
//     this.endereco.setLogradouro(c);
//     return c;
//     }catch(Exception e){
//     Logradouro c = new Logradouro();
//     this.endereco.setLogradouro(c);
//     return c;
//     }
//     }    */
//
//    public String pesquisarServicoValor() {
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadEndereco");
//        return "pesquisaServicoValor";
//    }
//
//    public String editar() {
////       servicoValor = (ServicoValor) getHtmlTable().getRowData();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoValorPesquisa", servicoValor);
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
//            return "cadServicoValor";
//        } else {
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        }
//    }
//}
