package br.com.rtools.seguranca.beans;

import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class RotinaJSFBean {
    private Rotina rotina = new Rotina();
    private String msgConfirma;
    private int idIndex = -1;
    private List<Rotina> listaRotina = new ArrayList();
    
    public RotinaJSFBean(){
//        htmlTable = new HtmlDataTable();
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar(){
        RotinaDB db = new RotinaDBToplink();
        if (rotina.getId()==-1){
            if (rotina.getRotina().equals("")){
                msgConfirma = "Digite uma Rotina!";
            }else{
                if (db.idRotina(rotina) == null){
                   db.insert(rotina);
                   msgConfirma = "Rotina salvo com Sucesso!";
                }
                else
                   msgConfirma = "Esta Rotina já existe no Sistema.";
            }
        }
        else{
            db.getEntityManager().getTransaction().begin();
            if (db.update(rotina))
            {db.getEntityManager().getTransaction().commit();
            msgConfirma = "Rotina atualizado com Sucesso!";}
            else
            {db.getEntityManager().getTransaction().rollback();}
        }
        return null;
    }

   public String novo(){
       rotina = new Rotina();
       return "rotina";
   }

   public String excluir(){
        RotinaDB db = new RotinaDBToplink();
        if (rotina.getId()!=-1){
            db.getEntityManager().getTransaction().begin();
            rotina = db.pesquisaCodigo(rotina.getId());
            if (db.delete(rotina))
            {db.getEntityManager().getTransaction().commit();
            msgConfirma = "Rotina Excluida com Sucesso!";}
            else
            {db.getEntityManager().getTransaction().rollback();
            msgConfirma = "Esta rotina não pode ser excluida!";}
        }
       rotina = new Rotina();
       return null;
   }

   public String editar(){
       rotina = (Rotina) listaRotina.get(idIndex);
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rotinaPesquisa", rotina);
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
       if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null)
           return "rotina";
       else
           return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
   }
   
   public List<Rotina> getListaRotina(){
       RotinaDB db = new RotinaDBToplink();
       listaRotina = db.pesquisaTodosOrdenado();
       return listaRotina;
   }

    public void setListaRotina(List<Rotina> listaRotina) {
        this.listaRotina = listaRotina;
    }
    
    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
    
    public String rotinaAtiva(boolean ativo) {
        if (ativo) {
            return "Ativo";
        }
        return "Inativo";
    }    
}
