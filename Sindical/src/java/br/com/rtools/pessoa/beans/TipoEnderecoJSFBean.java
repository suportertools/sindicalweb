//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.TipoEnderecoDB;
//import br.com.rtools.pessoa.db.TipoEnderecoDBToplink;
//import java.util.List;
//import javax.faces.context.FacesContext;
//
//public class TipoEnderecoJSFBean {
//
//    private TipoEndereco tipoEndereco = new TipoEndereco();
//    private String msgConfirma;
//    private String linkVoltar;
//
//    public TipoEnderecoJSFBean() {
//        //htmlTable = new HtmlDataTable();
//    }
//
//    public TipoEndereco getTipoEndereco() {
//        return tipoEndereco;
//    }
//
//    public void setTipoEndereco(TipoEndereco tipoEndereco) {
//        this.tipoEndereco = tipoEndereco;
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
//    public String salvar() {
//        TipoEnderecoDB db = new TipoEnderecoDBToplink();
//        if (tipoEndereco.getId() == -1) {
//            if (tipoEndereco.getDescricao().equals("")) {
//                msgConfirma = "Digite um Tipo de Endereço!";
//            } else {
//                if (db.idTipoEndereco(tipoEndereco) == null) {
//                    if (db.insert(tipoEndereco)) {
//                        msgConfirma = "Tipo Endereço salvo com Sucesso!";
//                    } else {
//                        msgConfirma = "Erro ao salvar Cadastro!";
//                    }
//                } else {
//                    msgConfirma = "Este Tipo de Endereço já existe no Sistema.";
//                }
//            }
//        } else {
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(tipoEndereco)) {
//                db.getEntityManager().getTransaction().commit();
//                msgConfirma = "Tipo de Endereço atualizado com Sucesso!";
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//        return null;
//    }
//
//    public String novo() {
//        tipoEndereco = new TipoEndereco();
//        return "tipoEndereco";
//    }
//
//    public String excluir() {
//        TipoEnderecoDB db = new TipoEnderecoDBToplink();
//        if (tipoEndereco.getId() != -1) {
//            db.getEntityManager().getTransaction().begin();
//            tipoEndereco = db.pesquisaCodigo(tipoEndereco.getId());
//            if (db.delete(tipoEndereco)) {
//                db.getEntityManager().getTransaction().commit();
//                msgConfirma = "Tipo de Endereço excluido com Sucesso!";
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//                msgConfirma = "Tipo de Endereço não pode ser Excluido!";
//            }
//        }
//        tipoEndereco = new TipoEndereco();
//        return null;
//    }
//
//    public String editar() {
//        //tipoEndereco = (TipoEndereco) getHtmlTable().getRowData();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        return "tipoEndereco";
//    }
//
//    public List getListaTipoEndereco() {
//        List result = null;
//        TipoEnderecoDB db = new TipoEnderecoDBToplink();
//        result = db.pesquisaTodos();
//        return result;
//    }
//
//    /*public String pesquisarTipoEndereco(){
//     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadTipoEndereco");
//     return "pesquisaTipoEndereco";
//     }*/
//    /*public String linkVoltar(){
//     if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
//     return "menuPrincipal";
//     }else
//     return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//     }*/
//
//    /*public String linkVoltarPesquisaTipoEndereco(){
//     linkVoltar = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//     if ( linkVoltar == null){
//     return "cadTipoEndereco";
//     }else
//     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
//     return linkVoltar;
//     }*/
//}
