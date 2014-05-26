//package br.com.rtools.financeiro.beans;
//
//import br.com.rtools.financeiro.Indice;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//
//@ManagedBean
//@SessionScoped
//public class IndiceBean implements Serializable {
//
//    private Indice indice = new Indice();
//    private String msgConfirma = "";
//
//    public String salvar() {
////        IndiceDB db = new IndiceDBToplink();
////        if (indice.getId() == -1){
////            if (indice.getDescricao().equals("")){
////                msgConfirma = "Digite um Índice por favor!";
////            }else{
////                if (db.insert(indice))
////                   msgConfirma = "Índice salvo com Sucesso!";
////                else
////                   msgConfirma = "Erro ao Salvar indice!";
////            }
////        }
////        else{
////            db.getEntityManager().getTransaction().begin();
////            if (db.update(indice))
////            {db.getEntityManager().getTransaction().commit();}
////            else
////            {db.getEntityManager().getTransaction().rollback();}
////            msgConfirma = "Índice atualizado com Sucesso!";
////        }
//        return null;
//    }
//
//    public String editar() {
////       indice = (Indice) getHtmlTable().getRowData();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
//            return "indice";
//        } else {
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        }
//    }
//
//    public String novo() {
//        indice = new Indice();
//        return "indice";
//    }
//
//    public String excluir() {
////        IndiceDB db = new IndiceDBToplink();
////        if (indice.getId() != -1){
////            db.getEntityManager().getTransaction().begin();
////            indice = db.pesquisaCodigo(indice.getId());
////            if (db.delete(indice)){
////                db.getEntityManager().getTransaction().commit();
////                msgConfirma = "Índice excluido com Sucesso!";
////            }else{
////                db.getEntityManager().getTransaction().rollback();
////                msgConfirma = "Índice não pode ser excluido!";
////            }
////        }
////        indice = new Indice();
//        return null;
//    }
//
//    public List getListaIndices() {
////        IndiceDB db = new IndiceDBToplink();
//        List result = new ArrayList();
////        result = db.pesquisaTodos();
//        return result;
//    }
//
//    public Indice getIndice() {
//        return indice;
//    }
//
//    public void setIndice(Indice indice) {
//        this.indice = indice;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//}
