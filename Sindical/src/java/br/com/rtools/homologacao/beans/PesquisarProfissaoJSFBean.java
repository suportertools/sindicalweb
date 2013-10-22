package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.db.ProfissaoDB;
import br.com.rtools.pessoa.db.ProfissaoDBToplink;
import java.util.ArrayList;
import java.util.List;

public class PesquisarProfissaoJSFBean {

    protected Profissao profissao = new Profissao();
    private String por;
    private String combo;
    private List<Profissao> listaProfissao;
    private int idIndexProf = -1;
    private String descricaoProfissao = "";

    public PesquisarProfissaoJSFBean() {
        profissao = new Profissao();
        por = "";
        combo = "profissao";
        listaProfissao = new ArrayList();
    }

    public List<Profissao> getListaProfissao() {
        if (!descricaoProfissao.equals("")) {
            profissao.setProfissao(descricaoProfissao);
        }
        if (listaProfissao.isEmpty() && !por.isEmpty()) {
            ProfissaoDB db = new ProfissaoDBToplink();
            listaProfissao = (List<Profissao>) db.pesquisaProfParametros(por, combo, profissao.getProfissao());
        }
        descricaoProfissao = "";
        return listaProfissao;
    }

    public void inicial() {
        listaProfissao.clear();
//        combo = "profissao";
        por = "I";
    }

    public void parcial() {
        listaProfissao.clear();
//        combo = "profissao";
        por = "P";
    }

    public void editar() {
        profissao = (Profissao) listaProfissao.get(idIndexProf);
    }
    
    public void editar(Profissao p) {
        profissao = p;
    }

    public Profissao getProfissao() {
        if (profissao.getId() == -1) {
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

    public int getIdIndexProf() {
        return idIndexProf;
    }

    public void setIdIndexProf(int idIndexProf) {
        this.idIndexProf = idIndexProf;
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
}