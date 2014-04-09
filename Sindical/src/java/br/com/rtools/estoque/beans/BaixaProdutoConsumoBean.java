package br.com.rtools.estoque.beans;

import br.com.rtools.estoque.EstoqueSaidaConsumo;
import br.com.rtools.estoque.EstoqueTipo;
import br.com.rtools.estoque.Produto;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class BaixaProdutoConsumoBean {

    private EstoqueSaidaConsumo estoqueSaidaConsumo;
    private Produto produto;
    private EstoqueTipo selectedEstoqueTipo;
    private List<EstoqueTipo> estoqueTipos;

    @PostConstruct
    public void init() {
        estoqueSaidaConsumo = new EstoqueSaidaConsumo();
        produto = new Produto();
        selectedEstoqueTipo = new EstoqueTipo();
        estoqueTipos = new ArrayList<EstoqueTipo>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("baixaProdutoConsumoBean");
        GenericaSessao.remove("produtoPesquisa");
    }

    public void baixar() {

    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<EstoqueTipo> getEstoqueTipos() {
        if (estoqueTipos.isEmpty()) {
            Dao dao = new Dao();
            estoqueTipos = (List<EstoqueTipo>) dao.list("EstoqueTipo");
        }
        return estoqueTipos;
    }

    public void setEstoqueTipos(List<EstoqueTipo> estoqueTipos) {
        this.estoqueTipos = estoqueTipos;
    }

    public EstoqueTipo getSelectedEstoqueTipo() {
        return selectedEstoqueTipo;
    }

    public void setSelectedEstoqueTipo(EstoqueTipo selectedEstoqueTipo) {
        this.selectedEstoqueTipo = selectedEstoqueTipo;
    }

    public EstoqueSaidaConsumo getEstoqueSaidaConsumo() {
        return estoqueSaidaConsumo;
    }

    public void setEstoqueSaidaConsumo(EstoqueSaidaConsumo estoqueSaidaConsumo) {
        this.estoqueSaidaConsumo = estoqueSaidaConsumo;        
    }
}
