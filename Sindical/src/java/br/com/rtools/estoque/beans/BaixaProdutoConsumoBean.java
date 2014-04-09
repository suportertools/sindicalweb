package br.com.rtools.estoque.beans;

import br.com.rtools.estoque.Estoque;
import br.com.rtools.estoque.EstoqueSaidaConsumo;
import br.com.rtools.estoque.Produto;
import br.com.rtools.estoque.dao.ProdutoDao;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
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

    private Filial filial;
    private Produto produto;
    private EstoqueSaidaConsumo estoqueSaidaConsumo;
    private List<EstoqueSaidaConsumo> listEstoqueSaidaConsumo;
    private List<Filial> filiais;
    private List<Estoque> listEstoque;
    private List<Departamento> departamentos;

    @PostConstruct
    public void init() {
        filial = new Filial();
        produto = new Produto();
        estoqueSaidaConsumo = new EstoqueSaidaConsumo();
        listEstoqueSaidaConsumo = new ArrayList<EstoqueSaidaConsumo>();
        filiais = new ArrayList<Filial>();
        listEstoque = new ArrayList<Estoque>();
        departamentos = new ArrayList<Departamento>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("baixaProdutoConsumoBean");
        GenericaSessao.remove("produtoPesquisa");
    }

    public void baixar() {

    }

    public void addItem() {
        if (filiais.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar filiais! Falar com administrador do sistema.");
            return;
        }
        if (estoqueSaidaConsumo.getQuantidade() == 0) {
            GenericaMensagem.warn("Validação", "Informar uma quantidade válida!");
            return;
        }
        for (EstoqueSaidaConsumo esc : listEstoqueSaidaConsumo) {
            if (esc.getFilialSaida().getId() == esc.getFilialSaida().getId()
                    && esc.getFilialEntrada().getId() == estoqueSaidaConsumo.getFilialEntrada().getId()
                    && esc.getProduto().getId() == estoqueSaidaConsumo.getProduto().getId()
                    && esc.getDepartamento().getId() == estoqueSaidaConsumo.getDepartamento().getId()) {
                GenericaMensagem.warn("Validação", "Item já adicionado a lista!");
                break;
            }
        }
    }

    public void removeItem(int index) {
        for (int i = 0; i < listEstoqueSaidaConsumo.size(); i++) {
            if (i == index) {
                if (listEstoqueSaidaConsumo.get(i).getId() != -1) {
                    Dao dao = new Dao();
                    if (dao.delete(index, true)) {
                        GenericaMensagem.warn("Sucesso", "Registro excluído");
                    } else {
                        GenericaMensagem.warn("Erro", "Ao excluir excluído");
                    }
                }
                listEstoqueSaidaConsumo.remove(i);
                break;
            }

        }
    }

    public EstoqueSaidaConsumo getEstoqueSaidaConsumo() {
        return estoqueSaidaConsumo;
    }

    public void setEstoqueSaidaConsumo(EstoqueSaidaConsumo estoqueSaidaConsumo) {
        this.estoqueSaidaConsumo = estoqueSaidaConsumo;
    }

    public List<Filial> getFiliais() {
        if (filiais.isEmpty()) {
            Dao dao = new Dao();
            filiais = (List<Filial>) dao.list("Filial", true);
        }
        return filiais;
    }

    public void setFiliais(List<Filial> filiais) {
        this.filiais = filiais;
    }

    public List<Departamento> getDepartamentos() {
        if (departamentos.isEmpty()) {
            Dao dao = new Dao();
            departamentos = (List<Departamento>) dao.list("Departamento", true);
        }
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public List<EstoqueSaidaConsumo> getListEstoqueSaidaConsumo() {
        return listEstoqueSaidaConsumo;
    }

    public void setListEstoqueSaidaConsumo(List<EstoqueSaidaConsumo> listEstoqueSaidaConsumo) {
        this.listEstoqueSaidaConsumo = listEstoqueSaidaConsumo;
    }

    public Filial getFilial() {
        if (filial.getId() == -1) {
            filial = MacFilial.getAcessoFilial().getFilial();
        }
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Produto getProduto() {
        if (GenericaSessao.exists("produtoPesquisa")) {
            Produto p = (Produto) GenericaSessao.getObject("produtoPesquisa");
            if (!p.equals(produto)) {
                listEstoque.clear();
                produto = p;
                ProdutoDao produtoDao = new ProdutoDao();
                listEstoque = (List<Estoque>) produtoDao.listaEstoquePorProduto(produto);
            }
        }
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<Estoque> getListEstoque() {
        return listEstoque;
    }

    public void setListEstoque(List<Estoque> listEstoque) {
        this.listEstoque = listEstoque;
    }
}
