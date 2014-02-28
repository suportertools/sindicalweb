//package br.com.rtools.associativo.beans;
//
//import br.com.rtools.associativo.GrupoConvenio;
//import br.com.rtools.associativo.db.GrupoConvenioDB;
//import br.com.rtools.associativo.db.GrupoConvenioDBToplink;
//import java.util.List;
//import javax.faces.context.FacesContext;
//
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//
//@ManagedBean
//@SessionScoped
// public class GrupoConvenioBean {
//
//    private GrupoConvenio grupoConvenio = new GrupoConvenio();
//    private String msgConfirma;
//    private String comoPesquisa = "I";
//    private String descPesquisa = "";
//
//    public void salvar() {
//        GrupoConvenioDB db = new GrupoConvenioDBToplink();
//        if (db.pesquisaGrupoConvenioPorDescricao(getGrupoConvenio().getDescricao()).isEmpty()) {
//            if (getGrupoConvenio().getId() == -1) {
//                if (db.insert(getGrupoConvenio())) {
//                    setMsgConfirma("Grupo do convênio cadastrado com sucesso!");
//                } else {
//                    setMsgConfirma("Erro ao cadastrar grupo do convênio!");
//                }
//            } else {
//                db.getEntityManager().getTransaction().begin();
//                if (db.update(getGrupoConvenio())) {
//                    db.getEntityManager().getTransaction().commit();
//                    setMsgConfirma("Grupo do convênio atualizado com Sucesso!");
//                } else {
//                    db.getEntityManager().getTransaction().rollback();
//                    setMsgConfirma("Erro ao atualizar grupo do convênio!");
//                }
//            }
//        } else {
//            setMsgConfirma("Grupo já existe!");
//        }
//    }
//
//    public String novo() {
//        setGrupoConvenio(new GrupoConvenio());
//        setMsgConfirma("");
//        return "grupoConvenio";
//    }
//
//    public String excluir() {
//        GrupoConvenioDB db = new GrupoConvenioDBToplink();
//        if (getGrupoConvenio().getId() != -1) {
//            db.getEntityManager().getTransaction().begin();
//            setGrupoConvenio(db.pesquisaCodigo(getGrupoConvenio().getId()));
//            if (db.delete(getGrupoConvenio())) {
//                db.getEntityManager().getTransaction().commit();
//                setMsgConfirma("Grupo do convênio excluido com sucesso!");
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//                setMsgConfirma("Grupo convênio não pode ser Excluido!");
//            }
//        }
//        setGrupoConvenio(new GrupoConvenio());
//        return null;
//    }
//
//    public String editar() {
////        setGrupoConvenio((GrupoConvenio) getHtmlTable().getRowData());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("grupoConvenioPesquisa", getGrupoConvenio());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        setDescPesquisa("");
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
//            return "grupoConvenio";
//        } else {
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        }
//    }
//
//    public List getListaGrupoConvenio() {
////        Pesquisa pesquisa = new Pesquisa();
//        List result = null;
////        result = pesquisa.pesquisar("GrupoConvenio", "descricao" , getDescPesquisa(), "descricao", getComoPesquisa());
//        return result;
//    }
//
//    public void acaoPesquisaInicial() {
//        comoPesquisa = "I";
//    }
//
//    public void acaoPesquisaParcial() {
//        comoPesquisa = "P";
//    }
//
//    public void refreshForm() {
//    }
//
//    public GrupoConvenio getGrupoConvenio() {
//        return grupoConvenio;
//    }
//
//    public void setGrupoConvenio(GrupoConvenio grupoConvenio) {
//        this.grupoConvenio = grupoConvenio;
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
// }