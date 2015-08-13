package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Parentesco;
import br.com.rtools.associativo.db.ParentescoDB;
import br.com.rtools.associativo.db.ParentescoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ParentescoBean {

    private Parentesco parentesco = new Parentesco();
    private String msgConfirma = "";
    private int idIndex = -1;
    private List<Parentesco> listaParentesco = new ArrayList();
    private ArrayList listaImagem = new ArrayList();
    private boolean limpar = false;

    public String salvar() {
        ParentescoDB db = new ParentescoDao();
        NovoLog novoLog = new NovoLog();
        if (getParentesco().getParentesco().equals("") || getParentesco().getParentesco() == null) {
            setMsgConfirma("Digite um nome para o parentesco!");
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (getParentesco().getId() == -1) {
            if (db.insert(getParentesco())) {
                setLimpar(false);
                setMsgConfirma("Parentesco salvo com sucesso.");
                GenericaMensagem.info("Sucesso", msgConfirma);
                novoLog.save(
                        "ID: " + parentesco.getId()
                        + " - Parentesco: " + parentesco.getParentesco()
                        + " - Sexo: " + parentesco.getSexo()
                        + " - Com Validade?: " + parentesco.isValidade()
                        + " - Validade: " + parentesco.getNrValidade()
                        + " - Ativo: " + parentesco.isAtivo()
                );
                parentesco = new Parentesco();
            } else {
                setMsgConfirma("Erro ao salvar parentesco!");
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            DaoInterface di = new Dao();
            Parentesco p = (Parentesco) di.find(parentesco);
            String beforeUpdate = 
                "ID: " + p.getId()
                + " - Parentesco: " + p.getParentesco()
                + " - Sexo: " + p.getSexo()
                + " - Com Validade?: " + p.isValidade()
                + " - Validade: " + p.getNrValidade()
                + " - Ativo: " + p.isAtivo()
            ;                
            if (db.update(getParentesco())) {
                novoLog.update(beforeUpdate,
                        "ID: " + parentesco.getId()
                        + " - Parentesco: " + parentesco.getParentesco()
                        + " - Sexo: " + parentesco.getSexo()
                        + " - Com Validade?: " + parentesco.isValidade()
                        + " - Validade: " + parentesco.getNrValidade()
                        + " - Ativo: " + parentesco.isAtivo()
                );                
                setMsgConfirma("Parentesco atualizado com sucesso.");
                GenericaMensagem.info("Sucesso", msgConfirma);
                parentesco = new Parentesco();
            } else {
                setMsgConfirma("Erro ao atualizar parentesco!");
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        }
        return null;
    }

    public String excluir() {
        ParentescoDB db = new ParentescoDao();
        NovoLog novoLog = new NovoLog();
        if (parentesco.getId() == -1) {
            msgConfirma = "Selecione um parentesco para ser excluído!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (db.delete(db.pesquisaCodigo(parentesco.getId()))) {
            novoLog.delete(
                    "ID: " + parentesco.getId()
                    + " - Parentesco: " + parentesco.getParentesco()
                    + " - Sexo: " + parentesco.getSexo()
                    + " - Com Validade?: " + parentesco.isValidade()
                    + " - Validade: " + parentesco.getNrValidade()
                    + " - Ativo: " + parentesco.isAtivo()
            );            
            setLimpar(true);
            msgConfirma = "Parentesco deletado com sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
        } else {
            setMsgConfirma("Erro ao deletar parentesco!");
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        parentesco = new Parentesco();
        return null;
    }

    public String editar(Parentesco p) {
        parentesco = p; //(Parentesco) getListaParentesco().get(getIdIndex());
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
        ParentescoDB db = new ParentescoDao();
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
