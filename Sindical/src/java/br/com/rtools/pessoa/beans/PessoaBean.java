package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class PessoaBean {
    private Pessoa pessoa = new Pessoa();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String masc;
    private String maxl;
    private List<Pessoa> listaPessoa = new ArrayList();

    public PessoaBean() {
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getMasc() {
        return masc;
    }

    public void setMasc(String masc) {
        this.masc = masc;
    }

    public String salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (pessoa.getId() == -1) {
            salvarAcumuladoDB.abrirTransacao();
            if(salvarAcumuladoDB.inserirObjeto(pessoa)) {
                salvarAcumuladoDB.comitarTransacao();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(pessoa)) {
                salvarAcumuladoDB.comitarTransacao();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
        return null;
    }

    public String novo() {
        setPessoa(pessoa = new Pessoa());
        return "pessoa";
    }

    public String excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (pessoa.getId() != -1) {
            setPessoa( (Pessoa) salvarAcumuladoDB.pesquisaObjeto(pessoa.getId(), "Pessoa"));
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(pessoa)) {
                salvarAcumuladoDB.comitarTransacao();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
        setPessoa(pessoa = new Pessoa());
        return "pesquisaPessoa";
    }

    public void CarregarPessoa() {
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public String pesquisarPessoa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "agenda");
        return "pesquisaPessoa";
    }

    public String editar(Pessoa p) {
        pessoa = p;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", pessoa);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        pessoa = new Pessoa();
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        } else {
            return null;
        }
    }

    public String getColocarMascara() {
        if (porPesquisa.equals("telefone1")) {
            masc = "telefone";
        } else {
            masc = "";
        }
        return masc;
    }

    public String getColocarMaxlenght() {
        if (porPesquisa.equals("telefone1")) {
            maxl = "14";
        } else {
            maxl = "50";
        }
        return maxl;
    }

    public List<Pessoa> getListaPessoa() {
        PessoaDB pesquisa = new PessoaDBToplink();
        if (descPesquisa.equals("")) {
            listaPessoa.clear();
            return listaPessoa;
        } else {
            listaPessoa = pesquisa.pesquisarPessoa(descPesquisa, porPesquisa, comoPesquisa);
            return listaPessoa;
        }
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }
    
    public String getMascaraPesquisa(){
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }      
}
