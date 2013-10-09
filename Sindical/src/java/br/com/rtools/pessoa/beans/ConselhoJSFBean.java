package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Conselho;
import br.com.rtools.pessoa.db.ConselhoDB;
import br.com.rtools.pessoa.db.ConselhoDBToplink;
import java.util.List;
import javax.faces.context.FacesContext;

public class ConselhoJSFBean {

    private Conselho conselho = new Conselho();
    private String msgConfirma;
    private String linkVoltar;

    public ConselhoJSFBean() {
        //htmlTable = new HtmlDataTable();
    }

    public Conselho getConselho() {
        return conselho;
    }

    public void setConselho(Conselho conselho) {
        this.conselho = conselho;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        ConselhoDB db = new ConselhoDBToplink();
        if (conselho.getId() == -1) {
            if (conselho.getConselho().equals("")) {
                msgConfirma = "Digite o nome de um conselho por favor!";
            } else {
                if (db.idConselho(conselho) == null) {
                    db.insert(conselho);
                    msgConfirma = "Conselho salvo com Sucesso!";
                } else {
                    msgConfirma = "Este conselho já existe no Sistema.";
                }
            }
        } else {
            db.getEntityManager().getTransaction().begin();
            if (db.update(conselho)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Conselho atualizado com Sucesso!";
            } else {
                db.getEntityManager().getTransaction().rollback();
            }

        }
        return null;
    }

    public String novo() {
        conselho = new Conselho();//zera objeto
        return "cadConselho";
    }

    public String excluir() {
        ConselhoDB db = new ConselhoDBToplink();
        if (conselho.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            conselho = db.pesquisaCodigo(conselho.getId());
            if (db.delete(conselho)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Conselho Excluido com Sucesso!";
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Conselho não pode ser Excluido!";
            }
        }
        conselho = new Conselho(); //zera objeto
        return null;
    }

    public String editar() {
        //conselho = (Conselho) getHtmlTable().getRowData(); 
        return "cadConselho";
    }

    public List getListaConselho() {
        List result = null;
        ConselhoDB db = new ConselhoDBToplink();
        result = db.pesquisaTodos();
        return result;
    }

    public String pesquisarConselho() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadConselho");
        return "pesquisaConselho";
    }

    public String linkVoltar() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "menuPrincipal";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String linkVoltarPesquisaConselho() {
        linkVoltar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (linkVoltar == null) {
            return "cadConselho";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
        }
        return linkVoltar;
    }
}