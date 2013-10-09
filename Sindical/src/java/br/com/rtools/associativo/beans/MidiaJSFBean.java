package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Midia;
import br.com.rtools.associativo.db.MidiaDB;
import br.com.rtools.associativo.db.MidiaDBToplink;
import java.util.List;
import javax.faces.context.FacesContext;

public class MidiaJSFBean {

    private Midia midia = new Midia();
    private String msgConfirma = "";

    public String salvar() {
        MidiaDB db = new MidiaDBToplink();
        if (midia.getId() == -1) {
            if (db.insert(midia)) {
                msgConfirma = "Mídia salva com sucesso!";
            } else {
                msgConfirma = "Erro ao salvar mídia!";
            }
        } else {
            if (db.update(midia)) {
                msgConfirma = "Mídia atualizada com sucesso!";
            } else {
                msgConfirma = "Erro ao atualizar mídia!";
            }
        }
        return null;
    }

    public String excluir() {
        MidiaDB db = new MidiaDBToplink();
        if (midia.getId() == -1) {
            msgConfirma = "Não existe Mídia para ser excluída!";
        } else {
            if (db.delete(midia)) {
                msgConfirma = "Mídia excluída com sucesso!";
            } else {
                msgConfirma = "Erro ao excluír mídia!";
            }
        }
        return null;
    }

    public String editar() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        midia = (Midia)getHtmlTable().getRowData();
        return "midia";
    }

    public String novo() {
        midia = new Midia();
        msgConfirma = "";
        return "midia";
    }

    public List getListaMidias() {
        MidiaDB db = new MidiaDBToplink();
        List result = db.pesquisaTodos();
        return result;
    }

    public Midia getMidia() {
        return midia;
    }

    public void setMidia(Midia midia) {
        this.midia = midia;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}
