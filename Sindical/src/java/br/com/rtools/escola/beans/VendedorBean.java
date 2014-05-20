package br.com.rtools.escola.beans;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.escola.db.VendedorDB;
import br.com.rtools.escola.db.VendedorDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class VendedorBean implements Serializable {

    private Vendedor vendedor;
    private String message;
    private List<Vendedor> listVendedores;

    @PostConstruct
    public void init() {
        vendedor = new Vendedor();
        message = "";
        listVendedores = new ArrayList<Vendedor>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("vendedorBean");
        GenericaSessao.remove("pessoaPesquisa");
    }

    public void save() {
        if (vendedor.getPessoa().getId() == -1) {
            message = "Pesquise uma Pessoa para ser vendedora!";
            return;
        }
        NovoLog novoLog = new NovoLog();
        VendedorDB vendedorDB = new VendedorDBToplink();
        if (vendedorDB.existeVendedor(vendedor)) {
            message = "Este vendedor já existe!";
            vendedor = new Vendedor();
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        if (di.save(vendedor)) {
            novoLog.save(
                    "ID " + vendedor.getId()
                    + " - Pessoa: (" + vendedor.getPessoa().getId() + ") " + vendedor.getPessoa().getNome()
            );
            di.commit();
            message = "Registro inserido com sucesso!";
        } else {
            di.rollback();
            message = "Erro ao inserir registro!";
        }
        listVendedores.clear();
        vendedor = new Vendedor();
    }

    public void delete(Vendedor v) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (di.delete(v)) {
            novoLog.delete(
                    "ID " + v.getId()
                    + " - Pessoa: (" + v.getPessoa().getId() + ") " + v.getPessoa().getNome()
            );
            di.commit();
            message = "Registro excluído com sucesso";
            listVendedores.clear();
            vendedor = new Vendedor();
        } else {
            di.rollback();
            message = "Erro ao excluir registro!";
        }
    }

    public List<Vendedor> getListVendedores() {
        if (listVendedores.isEmpty()) {
            DaoInterface di = new Dao();
            listVendedores = (List<Vendedor>) di.list(new Vendedor(), true);
        }
        return listVendedores;
    }

    public void setListVendedores(List<Vendedor> listVendedores) {
        this.listVendedores = listVendedores;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Vendedor getVendedor() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            vendedor.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}
