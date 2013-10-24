//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.db.TipoDocumentoDB;
import br.com.rtools.pessoa.db.TipoDocumentoDBToplink;
//import java.util.List;
//import javax.faces.context.FacesContext;
//
//public class TipoDocumentoJSFBean {
//
//    private TipoDocumento tipoDocumento = new TipoDocumento();
//    private String msgConfirma = "";
//    private int idBean;
//
//    public int getIdBean() {
//        return idBean;
//    }
//
//    public void setIdBean(int idBean) {
//        this.idBean = idBean;
//    }
//
//    public TipoDocumento getTipoDocumento() {
//        return tipoDocumento;
//    }
//
//    public void setTipoDocumento(TipoDocumento tipoDocumento) {
//        this.tipoDocumento = tipoDocumento;
//    }
//
//    public String salvar() {
//        TipoDocumentoDB db = new TipoDocumentoDBToplink();
//        if (tipoDocumento.getId() == -1) {
//            if (db.insert(tipoDocumento)) {
//                msgConfirma = "Tipo de Documento Salvo com Sucesso!";
//            } else {
//                msgConfirma = "Erro ao Salvar Tipo de Documento";
//            }
//        } else {
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(tipoDocumento)) {
//                db.getEntityManager().getTransaction().commit();
//                msgConfirma = "Tipo de Documento atualizado com sucesso!";
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//                msgConfirma = "Tipo de Documento não pode ser atualizado!";
//            }
//        }
//        return null;
//    }
//
//    public String novo() {
//        tipoDocumento = new TipoDocumento();//zera objeto
//        return "tipoDocumento";
//    }
//
//    public String excluir() {
//        TipoDocumentoDB db = new TipoDocumentoDBToplink();
//        if (tipoDocumento.getId() != -1) {
//            db.getEntityManager().getTransaction().begin();
//            tipoDocumento = db.pesquisaCodigo(tipoDocumento.getId());
//            if (db.delete(tipoDocumento)) {
//                db.getEntityManager().getTransaction().commit();
//                msgConfirma = "Tipo de Documento excluido com sucesso!";
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//                msgConfirma = "Tipo de Documento não pode excluido!";
//            }
//        }
//        tipoDocumento = new TipoDocumento(); //zera objeto
//        return null;
//    }
//
//    public String editar() {
//        //tipoDocumento = (TipoDocumento) getHtmlTable().getRowData(); //recupera o objeto referente a linha da tabela clicada pelo usuario
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        return "tipoDocumento";
//    }
//
//    public List getListaTipoDocumento() {
//        List result = null;
//        TipoDocumentoDB db = new TipoDocumentoDBToplink();
//        result = db.pesquisaTodos();
//        return result;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//
//    /*
//     public String CarregarIdentificador(){
//     idBean = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get( "paramDoc" ));
//     if (idBean == 0)
//     return "cadTipoDocumento";
//     else return null;
//     }*/
//
//    /*public String linkVoltar(){
//     if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
//     return "menuPrincipal";
//     }else
//     return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//     }*/
//}
