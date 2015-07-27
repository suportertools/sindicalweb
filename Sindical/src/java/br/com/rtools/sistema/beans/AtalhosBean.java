package br.com.rtools.sistema.beans;

import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.Atalhos;
import br.com.rtools.sistema.db.AtalhoDB;
import br.com.rtools.sistema.db.AtalhoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AtalhosBean implements Serializable {

    private List<Atalhos> listaAtalhos = new ArrayList();
    private int idRotina = 0;
    private List<SelectItem> listaRotina = new ArrayList();
    private Atalhos atalhos = new Atalhos();

    public String adicionar() {
        DaoInterface di = new Dao();
        AtalhoDB db = new AtalhoDBToplink();
        atalhos.setRotina((Rotina) di.find(new Rotina(), Integer.parseInt(listaRotina.get(idRotina).getDescription())));
        if (atalhos.getSigla().isEmpty() || db.pesquisaPorSigla(atalhos.getSigla()) != null || db.pesquisaPorRotina(atalhos.getRotina().getId()) != null) {
            return null;
        }
        atalhos.setPessoa(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getPessoa());
        di.openTransaction();
        if (di.save(atalhos)) {
            di.commit();
            listaAtalhos.clear();
        } else {
            di.rollback();
        }
        atalhos = new Atalhos();
        idRotina = 0;
        return "menuPrincipal";
    }

    public String excluir(int id) {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (di.delete((Atalhos) di.find(new Atalhos(), id))) {
            di.commit();
            listaAtalhos.clear();
        } else {
            di.rollback();
        }
        return "menuPrincipal";
    }

    public List<Atalhos> getListaAtalhos() {
        if (listaAtalhos.isEmpty() && GenericaSessao.exists("sessaoUsuario")) {
            AtalhoDB db = new AtalhoDBToplink();
            listaAtalhos = db.listaTodos(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getPessoa().getId());
            for (int i = 0; i < listaAtalhos.size(); i++) {
                if (listaAtalhos.get(i).getRotina().getRotina().length() > 16) {
                    listaAtalhos.get(i).getRotina().setRotina(listaAtalhos.get(i).getRotina().getRotina().substring(0, 13) + "...");
                }
            }
        }
        return listaAtalhos;
    }

    public void setListaAtalhos(List<Atalhos> listaAtalhos) {
        this.listaAtalhos = listaAtalhos;
    }

    public List<SelectItem> getListaRotina() {
        if (listaRotina.isEmpty()) {
            List select = new Dao().list(new Rotina(), true);
            for (int i = 0; i < select.size(); i++) {
                listaRotina.add(new SelectItem(new Integer(i),
                        (String) ((Rotina) select.get(i)).getRotina(),
                        Integer.toString(((Rotina) select.get(i)).getId())));
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
