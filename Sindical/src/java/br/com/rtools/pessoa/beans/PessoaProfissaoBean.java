package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.PessoaProfissao;
import br.com.rtools.pessoa.db.PessoaProfissaoDB;
import br.com.rtools.pessoa.db.PessoaProfissaoDBToplink;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class PessoaProfissaoBean implements Serializable {

    private PessoaProfissao pessoaProfissao = new PessoaProfissao();

    public PessoaProfissaoBean() {
        //htmlTable = new HtmlDataTable();
    }

    public PessoaProfissao getPessoaProfissao() {
        return pessoaProfissao;
    }

    public void setPessoaProfissao(PessoaProfissao pessoaProfissao) {
        this.pessoaProfissao = pessoaProfissao;
    }

    public String salvar() {
        PessoaProfissaoDB db = new PessoaProfissaoDBToplink();
        if (pessoaProfissao.getId() == -1) {
            db.insert(pessoaProfissao);
        } else {
            db.getEntityManager().getTransaction().begin();
            if (db.update(pessoaProfissao)) {
                db.getEntityManager().getTransaction().commit();
            } else {
                db.getEntityManager().getTransaction().rollback();
            }
        }
        return null;
    }

    public String novo() {
        pessoaProfissao = new PessoaProfissao();
        return "cadPessoaProfissao";
    }

    public String excluir() {
        PessoaProfissaoDB db = new PessoaProfissaoDBToplink();
        if (pessoaProfissao.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            pessoaProfissao = db.pesquisaCodigo(pessoaProfissao.getId());
            if (db.delete(pessoaProfissao)) {
                db.getEntityManager().getTransaction().commit();
            } else {
                db.getEntityManager().getTransaction().rollback();
            }
        }
        pessoaProfissao = new PessoaProfissao();
        return "pesquisaPessoaProfissao";
    }

    public String editar() {
        //pessoaProfissao = (PessoaProfissao) getHtmlTable().getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "pessoaProfissao";
    }

    public List getListaPessoaProfissao() {
        List result = null;
        PessoaProfissaoDB db = new PessoaProfissaoDBToplink();
        result = db.pesquisaTodos();
        return result;
    }

    public int getPegaPessoaProfissao() {
        return pessoaProfissao.getId();
    }

    public void CarregarPessoa() {
    }
}
