package br.com.rtools.sistema.beans;

import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.sistema.Atalhos;
import br.com.rtools.sistema.db.AtalhoDB;
import br.com.rtools.sistema.db.AtalhoDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class AtalhosJSFBean {
    private List<Atalhos> listaAtalhos = new ArrayList();
    private int idRotina = 0;
    private List<SelectItem> listaRotina = new ArrayList();
    private Atalhos atalhos = new Atalhos();
    
    public String adicionar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        AtalhoDB db = new AtalhoDBToplink();
        RotinaDB dbr = new RotinaDBToplink();
        
        atalhos.setRotina( dbr.pesquisaCodigo( Integer.parseInt(listaRotina.get(idRotina).getDescription()) ));
        if (atalhos.getSigla().isEmpty() || db.pesquisaPorSigla(atalhos.getSigla()) != null || db.pesquisaPorRotina(atalhos.getRotina().getId()) != null)
            return null;
        
        atalhos.setPessoa( ((Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getPessoa() );
        
        sv.abrirTransacao();
        if (sv.inserirObjeto(atalhos)){
            sv.comitarTransacao();
            listaAtalhos.clear();
        }else
            sv.desfazerTransacao();
        
        atalhos = new Atalhos();
        idRotina = 0;
        return "menuPrincipal";
    }
    
    public String excluir(int id){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (sv.deletarObjeto((Atalhos)sv.pesquisaCodigo(id, "Atalhos"))){
            sv.comitarTransacao();
            listaAtalhos.clear();
        }else
            sv.desfazerTransacao();
        return "menuPrincipal";
    }
        
    public List<Atalhos> getListaAtalhos() {
        if(listaAtalhos.isEmpty() && FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null){
            AtalhoDB db = new AtalhoDBToplink();
            listaAtalhos = db.listaTodos(((Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getPessoa().getId());
            for (int i = 0; i < listaAtalhos.size(); i++){
                if (listaAtalhos.get(i).getRotina().getRotina().length() > 16){
                    listaAtalhos.get(i).getRotina().setRotina(listaAtalhos.get(i).getRotina().getRotina().substring(0, 13)+"...");
                }
            }
        }
        return listaAtalhos;
    }

    public void setListaAtalhos(List<Atalhos> listaAtalhos) {
        this.listaAtalhos = listaAtalhos;
    }

    public List<SelectItem> getListaRotina(){
        if (listaRotina.isEmpty()){
            RotinaDB db = new RotinaDBToplink();
            List select = db.pesquisaTodosOrdenado();
            for (int i = 0; i < select.size(); i++){
                listaRotina.add(new SelectItem( new Integer(i),
                                (String) ((Rotina) select.get(i)).getRotina(),
                                Integer.toString(((Rotina) select.get(i)).getId()) ));
            }
        }
        return listaRotina;
    }    
    
    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public Atalhos getAtalhos() {
        return atalhos;
    }

    public void setAtalhos(Atalhos atalhos) {
        this.atalhos = atalhos;
    }
}
