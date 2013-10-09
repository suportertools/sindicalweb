package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Parentesco;
import br.com.rtools.associativo.db.ParentescoDB;
import br.com.rtools.associativo.db.ParentescoDBToplink;
import java.util.ArrayList;
import java.util.List;

public class ParentescoJSFBean {

    private Parentesco parentesco = new Parentesco();
    private String msgConfirma = "";
    private int idIndex = -1;
    private List<Parentesco> listaParentesco = new ArrayList();
    private ArrayList listaImagem = new ArrayList();
    private boolean limpar = false;

    public String salvar() {
        ParentescoDB db = new ParentescoDBToplink();
        if (getParentesco().getParentesco().equals("") || getParentesco().getParentesco() == null) {
            setMsgConfirma("Digite um nome para o parentesco!");
            return null;
        }
        if (getParentesco().getId() == -1) {
            if (db.insert(getParentesco())) {
                setLimpar(false);
                setMsgConfirma("Parentesco salvo com sucesso.");
            } else {
                setMsgConfirma("Erro ao salvar parentesco!");
            }
        } else {
            if (db.update(getParentesco())) {
                setMsgConfirma("Parentesco atualizado com sucesso.");
            } else {
                setMsgConfirma("Erro ao atualizar parentesco!");
            }
        }
        return null;
    }

    public String excluir() {
        ParentescoDB db = new ParentescoDBToplink();
        if (parentesco.getId() == -1) {
            msgConfirma = "Selecione um parentesco para ser excluído!";
            return null;
        }
        if (db.delete(db.pesquisaCodigo(parentesco.getId()))) {
            setLimpar(true);
            msgConfirma = "Parentesco deletado com sucesso!";
        } else {
            setMsgConfirma("Erro ao deletar parentesco!");
        }
        return null;
    }

    public String editar() {
        parentesco = (Parentesco) getListaParentesco().get(getIdIndex());
        return "parentesco";
    }

    public String novo() {
        parentesco = new Parentesco();
        listaParentesco.clear();
        return "parentesco";
    }

    public String limpar() {
        if (limpar == true) {
            novo();
        }
        return "parentesco";
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setGrupoCategoria(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<Parentesco> getListaParentesco() {
        ParentescoDB db = new ParentescoDBToplink();
        listaParentesco = db.pesquisaTodos();
        for (int i = 0; i < listaParentesco.size(); i++) {
            if (listaParentesco.get(i).isAtivo() == true) {
                getListaImagem().add("iconTrue.gif");
            } else {
                getListaImagem().add("iconFalse.gif");
            }
        }
        return listaParentesco;
    }

    public void setListaParentesco(List<Parentesco> listaParentesco) {
        this.setListaParentesco(listaParentesco);
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public ArrayList getListaImagem() {
        return listaImagem;
    }

    public void setListaImagem(ArrayList listaImagem) {
        this.listaImagem = listaImagem;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}