package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class CnaeJSFBean {

    private Cnae cnae = new Cnae();
    private String msgConfirma;
    private int idIndex = -1;
    private List<Cnae> listaCnae = new ArrayList();
    private boolean limpar = false;

    public String novo() {
        setCnae(new Cnae());
        getListaCnae().clear();
        return "cnae";
    }

    public String limpar() {
        if (isLimpar() == true) {
            novo();
        }
        return "cnae";
    }

    public Cnae getCnae() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado") != null) {
            cnae = (Cnae) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
        }
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        CnaeDB db = new CnaeDBToplink();
        if (getCnae().getCnae().isEmpty()) {
            setMsgConfirma("Digite um Cnae!");
            return null;
        }

        if (getCnae().getNumero().isEmpty()) {
            setMsgConfirma("Digite o Número do Cnae!");
            return null;
        }
//
//        if(db.idCnae(getCnae()) != null){
//            setMsgConfirma("Este Cnae já existe no Sistema!");
//            return null;
//        }

        if (getCnae().getId() == -1) {
            if (db.insert(getCnae())) {
                setMsgConfirma("Cnae salvo com sucesso.");
                setLimpar(false);
            } else {
                setMsgConfirma("Erro ao salvar Cnae!");
            }
        } else {
            if (db.update(getCnae())) {
                setMsgConfirma("Cnae atualizada com sucesso.");
            } else {
                setMsgConfirma("Erro ao atualizar Cnae!");
            }
        }
        return null;
    }

    public String excluir() {
        CnaeDB db = new CnaeDBToplink();
        if (getCnae().getId() != -1) {
            setCnae(db.pesquisaCodigo(getCnae().getId()));
            if (db.delete(cnae)) {
                setLimpar(true);
                setMsgConfirma("Cadastro excluído com sucesso!");
            } else {
                setMsgConfirma("Erro! Cadastro não foi excluído.");
            }
        } else {
            setMsgConfirma("Não há registro para excluir.");
        }
        return null;
    }

    public String editar() {
        setCnae((Cnae) getListaCnae().get(getIdIndex()));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cnaePesquisado", getCnae());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        setCnae(new Cnae());
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
            return "cnae";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public List<Cnae> getListaCnae() {
        CnaeDB db = new CnaeDBToplink();
        listaCnae = db.pesquisaTodos();
        return listaCnae;
    }

    public void setListaCnae(List<Cnae> listaCnae) {
        this.listaCnae = listaCnae;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}