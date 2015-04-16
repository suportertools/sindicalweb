package br.com.rtools.escola.beans;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.escola.dao.VendedorDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
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
    private List<Vendedor> listVendedores;

    @PostConstruct
    public void init() {
        vendedor = new Vendedor();
        listVendedores = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("vendedorBean");
        GenericaSessao.remove("pessoaPesquisa");
    }

    public void save() {
        if (vendedor.getPessoa().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquise uma Pessoa para ser vendedora!");
            return;
        }
        NovoLog novoLog = new NovoLog();
        VendedorDao vd = new VendedorDao();
        if (vd.existeVendedor(vendedor)) {
            GenericaMensagem.warn("Validação", "Este vendedor já existe!");
            vendedor = new Vendedor();
            return;
        }
        Dao dao = new Dao();
        dao.openTransaction();
        if (dao.save(vendedor)) {
            novoLog.save(
                    "ID " + vendedor.getId()
                    + " - Pessoa: (" + vendedor.getPessoa().getId() + ") " + vendedor.getPessoa().getNome()
            );
            dao.commit();
            GenericaMensagem.info("Sucesso", "Registro inserido");
        } else {
            dao.rollback();
            GenericaMensagem.warn("Erro", "Ao inserir registro!");
        }
        listVendedores.clear();
        vendedor = new Vendedor();
    }

    public void delete(Vendedor v) {
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        dao.openTransaction();
        if (dao.delete(v)) {
            novoLog.delete(
                    "ID " + v.getId()
                    + " - Pessoa: (" + v.getPessoa().getId() + ") " + v.getPessoa().getNome()
            );
            dao.commit();
            GenericaMensagem.info("Sucesso", "Registro excluído");
            listVendedores.clear();
            vendedor = new Vendedor();
        } else {
            dao.rollback();
            GenericaMensagem.warn("Erro", "Ao excluir registro!");
        }
    }

    public List<Vendedor> getListVendedores() {
        if (listVendedores.isEmpty()) {
            Dao dao = new Dao();
            listVendedores = (List<Vendedor>) dao.list(new Vendedor(), true);
        }
        return listVendedores;
    }

    public void setListVendedores(List<Vendedor> listVendedores) {
        this.listVendedores = listVendedores;
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
