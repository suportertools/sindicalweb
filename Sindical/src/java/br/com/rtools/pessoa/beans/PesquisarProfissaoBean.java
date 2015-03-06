package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.db.ProfissaoDB;
import br.com.rtools.pessoa.db.ProfissaoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import org.richfaces.component.SortOrder;

public class PesquisarProfissaoBean implements Serializable {

    protected Profissao profissao = new Profissao();
    private String por;
    private String combo;
    private List<Profissao> listaProfissao;
    private String descricaoProfissao = "";
    //private SortOrder profissaoOrder = SortOrder.unsorted;

    public PesquisarProfissaoBean() {
        profissao = new Profissao();
        por = "";
        combo = "profissao";
        listaProfissao = new ArrayList();
    }
    
    public void sortByProfissao() {
        //profissaoOrder = SortOrder.unsorted;
    }  

    public List<Profissao> getListaProfissao() {
        if (listaProfissao.isEmpty() && !por.isEmpty()) {
            ProfissaoDB db = new ProfissaoDBToplink();
            if (!descricaoProfissao.isEmpty()) 
                listaProfissao = (List<Profissao>) db.pesquisaProfParametros(por, combo, descricaoProfissao);
            else
                listaProfissao = (List<Profissao>) db.pesquisaProfParametros(por, combo, profissao.getProfissao());
        }
        return listaProfissao;
    }

    public void inicial() {
        listaProfissao.clear();
        por = "I";
    }

    public void parcial() {
        listaProfissao.clear();
        por = "P";
    }

    public void editar(Profissao p) {
        profissao = new Profissao();
        profissao = p;
        //profissao.setProfissao(p.getProfissao());
    }

    public Profissao getProfissao() {
        if (profissao == null || profissao.getId() == -1) {
            profissao = new Profissao();
        }
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public void setListaProfissao(List<Profissao> listaProfissao) {
        this.listaProfissao = listaProfissao;
    }

    public String getDescricaoProfissao() {
        return descricaoProfissao;
    }

    public void setDescricaoProfissao(String descricaoProfissao) {
        this.descricaoProfissao = descricaoProfissao;
    }
//
//    public SortOrder getProfissaoOrder() {
//        return profissaoOrder;
//    }
//
//    public void setProfissaoOrder(SortOrder profissaoOrder) {
//        this.profissaoOrder = profissaoOrder;
//    }
}