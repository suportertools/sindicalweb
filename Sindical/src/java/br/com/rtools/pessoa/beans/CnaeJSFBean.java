//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.pessoa.Cnae;
//import br.com.rtools.pessoa.db.CnaeDB;
//import br.com.rtools.pessoa.db.CnaeDBToplink;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.context.FacesContext;
//
//public class CnaeJSFBean implements Serializable {
//
//    private Cnae cnae = new Cnae();
//    private String msgConfirma;
//    private int idIndex = -1;
//    private List<Cnae> listaCnae = new ArrayList();
//    private boolean limpar = false;
//
//    public String novo() {
//        cnae = new Cnae();
//        listaCnae.clear();
//        return null;
//    }
//
//    public String limpar() {
//        if (isLimpar() == true) {
//            novo();
//        }
//        return "cnae";
//    }
//
//    public Cnae getCnae() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado") != null) {
//            cnae = (Cnae) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado");
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
//        }
//        return cnae;
//    }
//
//    public void setCnae(Cnae cnae) {
//        this.cnae = cnae;
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
//        if (getCnae().getCnae().isEmpty()) {
//            setMsgConfirma("Digite um Cnae!");
//            return null;
//        }
//        if (getCnae().getNumero().isEmpty()) {
//            setMsgConfirma("Digite o Número do Cnae!");
//            return null;
//        }
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        if (getCnae().getId() == -1) {
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.inserirObjeto(cnae)) {
//                salvarAcumuladoDB.comitarTransacao();
//                setMsgConfirma("Cnae salvo com sucesso.");
//                setLimpar(false);
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                setMsgConfirma("Erro ao salvar Cnae!");
//            }
//        } else {
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.alterarObjeto(getCnae())) {
//                salvarAcumuladoDB.comitarTransacao();
//                setMsgConfirma("Cnae atualizada com sucesso.");
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                setMsgConfirma("Erro ao atualizar Cnae!");
//            }
//        }
//        return null;
//    }
//
//    public String excluir() {
//        if (getCnae().getId() != -1) {
//            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//            setCnae((Cnae) salvarAcumuladoDB.pesquisaCodigo(getCnae().getId(), "Cnae"));
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.deletarObjeto(cnae)) {
//                salvarAcumuladoDB.comitarTransacao();
//                cnae = new Cnae();
//                listaCnae.clear();
//                setMsgConfirma("Cadastro excluído com sucesso!");
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                setMsgConfirma("Erro! Cadastro não foi excluído.");
//            }
//        } else {
//            setMsgConfirma("Não há registro para excluir.");
//        }
//        return null;
//    }
//
//    public String editar(Cnae c) {
//        cnae = c;
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cnaePesquisado", getCnae());
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        setCnae(new Cnae());
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
//            return "cnae";
//        } else {
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        }
//    }
//
//    public List<Cnae> getListaCnae() {
//        CnaeDB db = new CnaeDBToplink();
//        listaCnae = db.pesquisaTodos();
//        return listaCnae;
//    }
//
//    public void setListaCnae(List<Cnae> listaCnae) {
//        this.listaCnae = listaCnae;
//    }
//
//    public int getIdIndex() {
//        return idIndex;
//    }
//
//    public void setIdIndex(int idIndex) {
//        this.idIndex = idIndex;
//    }
//
//    public boolean isLimpar() {
//        return limpar;
//    }
//
//    public void setLimpar(boolean limpar) {
//        this.limpar = limpar;
//    }
//}