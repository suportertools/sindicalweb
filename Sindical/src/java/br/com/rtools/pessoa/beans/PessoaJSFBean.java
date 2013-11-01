//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.context.FacesContext;
//
//public class PessoaJSFBean {
//
//    private Pessoa pessoa = new Pessoa();
//    private String descPesquisa = "";
//    private String porPesquisa = "nome";
//    private String comoPesquisa = "";
//    private String masc;
//    private String maxl;
//    private int idIndex = -1;
//    private List<Pessoa> listaPessoa = new ArrayList();
//
//    public PessoaJSFBean() {
//        idIndex = -1;
//        descPesquisa = "";
//        porPesquisa = "nome";
//        comoPesquisa = "";
//    }
//
//    public Pessoa getPessoa() {
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public String getDescPesquisa() {
//        return descPesquisa;
//    }
//
//    public void setDescPesquisa(String descPesquisa) {
//        this.descPesquisa = descPesquisa;
//    }
//
//    public String getPorPesquisa() {
//        return porPesquisa;
//    }
//
//    public void setPorPesquisa(String porPesquisa) {
//        this.porPesquisa = porPesquisa;
//    }
//
//    public String getComoPesquisa() {
//        return comoPesquisa;
//    }
//
//    public void setComoPesquisa(String comoPesquisa) {
//        this.comoPesquisa = comoPesquisa;
//    }
//
//    public String getMasc() {
//        return masc;
//    }
//
//    public void setMasc(String masc) {
//        this.masc = masc;
//    }
//
//    public String salvar() {
//        PessoaDB db = new PessoaDBToplink();
//        if (pessoa.getId() == -1) {
//            db.insert(pessoa);
//        } else {
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(pessoa)) {
//                db.getEntityManager().getTransaction().commit();
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//        return null;
//    }
//
//    public String novo() {
//        setPessoa(pessoa = new Pessoa());
//        return "pessoa";
//    }
//
//    public String excluir() {
//        PessoaDB db = new PessoaDBToplink();
//        if (pessoa.getId() != -1) {
//            db.getEntityManager().getTransaction().begin();
//            setPessoa(db.pesquisaCodigo(pessoa.getId()));
//            if (db.delete(pessoa)) {
//                db.getEntityManager().getTransaction().commit();
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//        setPessoa(pessoa = new Pessoa());
//        return "pesquisaPessoa";
//    }
//
//    public void CarregarPessoa() {
//    }
//
//    public void refreshForm() {
//    }
//
//    public void acaoPesquisaInicial() {
//        comoPesquisa = "I";
//    }
//
//    public void acaoPesquisaParcial() {
//        comoPesquisa = "P";
//    }
//
//    public String pesquisarPessoa() {
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "agenda");
//        return "pesquisaPessoa";
//    }
//
//    public String editar() {
//        pessoa = (Pessoa) listaPessoa.get(idIndex);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", pessoa);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        pessoa = new Pessoa();
//        descPesquisa = "";
//        porPesquisa = "nome";
//        comoPesquisa = "";
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        } else {
//            return null;
//        }
//    }
//
//    public String getColocarMascara() {
//        if (porPesquisa.equals("telefone1")) {
//            masc = "telefone";
//        } else {
//            masc = "";
//        }
//        return masc;
//    }
//
//    public String getColocarMaxlenght() {
//        if (porPesquisa.equals("telefone1")) {
//            maxl = "14";
//        } else {
//            maxl = "50";
//        }
//        return maxl;
//    }
//
//    public int getIdIndex() {
//        return idIndex;
//    }
//
//    public void setIdIndex(int idIndex) {
//        this.idIndex = idIndex;
//    }
//
//    public List<Pessoa> getListaPessoa() {
//        PessoaDB pesquisa = new PessoaDBToplink();
//        if (descPesquisa.equals("")) {
//            listaPessoa = new ArrayList();
//            return listaPessoa;
//        } else {
//            listaPessoa = pesquisa.pesquisarPessoa(descPesquisa, porPesquisa, comoPesquisa);
//            return listaPessoa;
//        }
//    }
//
//    public void setListaPessoa(List<Pessoa> listaPessoa) {
//        this.listaPessoa = listaPessoa;
//    }
//}
