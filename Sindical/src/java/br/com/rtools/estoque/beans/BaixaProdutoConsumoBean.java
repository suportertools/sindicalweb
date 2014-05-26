package br.com.rtools.estoque.beans;

import br.com.rtools.estoque.Estoque;
import br.com.rtools.estoque.EstoqueSaidaConsumo;
import br.com.rtools.estoque.EstoqueTipo;
import br.com.rtools.estoque.Produto;
import br.com.rtools.estoque.dao.ProdutoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class BaixaProdutoConsumoBean implements Serializable {

    private Departamento selectedDepartamento;
    private Estoque estoque;
    private Filial filial;
    private Filial selectedFilialDestino;
    private Produto produto;
    private EstoqueSaidaConsumo estoqueSaidaConsumo;
    private List<Departamento> listDepartamentos;
    private List<EstoqueSaidaConsumo>[] listEstoqueSaidaConsumo;
    private List<Filial> listFiliais;
    private String message;
    private int quantidadeEstoque;
    private int nrEstoque;
    private String color;

    @PostConstruct
    public void init() {
        selectedDepartamento = new Departamento();
        estoque = new Estoque();
        filial = MacFilial.getAcessoFilial().getFilial();
        selectedFilialDestino = new Filial();
        produto = new Produto();
        estoqueSaidaConsumo = new EstoqueSaidaConsumo();
        listDepartamentos = new ArrayList<Departamento>();
        listEstoqueSaidaConsumo = new ArrayList[2];
        listEstoqueSaidaConsumo[0] = new ArrayList<EstoqueSaidaConsumo>();
        listEstoqueSaidaConsumo[1] = new ArrayList<EstoqueSaidaConsumo>();
        listFiliais = new ArrayList<Filial>();
        message = "";
        quantidadeEstoque = 0;
        nrEstoque = 0;
        color = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("baixaProdutoConsumoBean");
        GenericaSessao.remove("produtoBean");
        GenericaSessao.remove("produtoPesquisa");
    }

    public void baixar() {
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        novoLog.startList();
        dao.openTransaction();
        boolean err = false;
        for (EstoqueSaidaConsumo esc : listEstoqueSaidaConsumo[0]) {
            if (!dao.save(esc)) {
                err = true;
            }
            novoLog.save(
                    "ID: " + esc.getId()
                    + " - Departamento: (" + esc.getId() + ") " + esc.getDepartamento().getDescricao()
                    + " - Filial Saída: " + esc.getFilialSaida().getId()
                    + " - Filial Entrada: " + esc.getFilialEntrada().getId()
                    + " - Quantidade: " + esc.getQuantidade()
                    + " - Produto: (" + esc.getProduto().getId() + ") " + esc.getProduto().getDescricao()
            );
        }
        // update est_estoque set nr_estoque=nr_estoque-(qtde da saida) where id_tipo=1 and id_filial=(filial saida) and id_produto = (produto selecionado)
        if (err) {
            novoLog.cancelList();
            dao.rollback();
            message = "Erro ao baixar produtos do estoque!";
        } else {
            String beforeEstoque = "" + estoque.getEstoque();
            estoque.setEstoque(quantidadeEstoque);
            if (dao.update(estoque)) {
                novoLog.save(
                        "Estoque - ID: " + estoque.getId()
                        + " - Filial: (" + estoque.getFilial().getId() + ") " + estoque.getFilial().getFilial().getPessoa().getId()
                        + " - Produto: (" + estoque.getProduto().getId() + ") " + estoque.getProduto().getDescricao()
                        + " - Estoque Antes: " + beforeEstoque
                        + " - Estoque Atual: " + estoque.getEstoque()
                );
                dao.commit();
                novoLog.saveList();
                message = "Baixa realizada com sucesso!";
                listEstoqueSaidaConsumo[0].clear();
                listEstoqueSaidaConsumo[1].clear();
            } else {
                novoLog.cancelList();
                dao.rollback();
                message = "Erro ao baixar produtos do estoque!";
            }
        }
    }

    public void addItem() {
        if (filial.getId() == -1) {
            GenericaMensagem.error("Validação", "Acessar o sistema com Filial!");
            return;
        }
        estoqueSaidaConsumo.setFilialSaida(filial);
        if (listFiliais.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar Filiais! Falar com administrador do sistema.");
            return;
        }
        if (produto.getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar um produto!");
            return;
        }
        if (!estoque.isAtivo()) {
            GenericaMensagem.warn("Validação", "Produto está inátivado!");
            return;
        }
        estoqueSaidaConsumo.setProduto(produto);
        estoqueSaidaConsumo.setDepartamento(selectedDepartamento);
        estoqueSaidaConsumo.setFilialEntrada(selectedFilialDestino);
        Dao dao = new Dao();
        dao.openTransaction();
        estoqueSaidaConsumo.setEstoqueTipo((EstoqueTipo) dao.find(new EstoqueTipo(), 1));
        dao.commit();
        for (EstoqueSaidaConsumo esc : listEstoqueSaidaConsumo[0]) {
            if (esc.getFilialSaida().getId() == esc.getFilialSaida().getId()
                    && esc.getFilialEntrada().getId() == estoqueSaidaConsumo.getFilialEntrada().getId()
                    && esc.getProduto().getId() == estoqueSaidaConsumo.getProduto().getId()
                    && esc.getDepartamento().getId() == estoqueSaidaConsumo.getDepartamento().getId()) {
                GenericaMensagem.warn("Validação", "Item já adicionado a lista!");
                return;
            }
        }
        if (estoqueSaidaConsumo.getQuantidade() == 0) {
            GenericaMensagem.warn("Validação", "Informar uma quantidade válida!");
            return;
        }
        listEstoqueSaidaConsumo[0].add(estoqueSaidaConsumo);
        estoqueSaidaConsumo = new EstoqueSaidaConsumo();
    }

    public void removeItem(int index) {
        for (int i = 0; i < listEstoqueSaidaConsumo[0].size(); i++) {
            if (i == index) {
                listEstoqueSaidaConsumo[0].remove(i);
                removeQuantidadeEstoque();
                break;
            }
        }
    }

    public void removeItem(EstoqueSaidaConsumo esc) {
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        novoLog.startList();
        dao.openTransaction();
        if (esc.getLancamento().equals(DataHoje.data())) {
            if (dao.delete(esc)) {
                novoLog.delete(
                        "ID: " + esc.getId()
                        + " - Departamento: (" + esc.getId() + ") " + esc.getDepartamento().getDescricao()
                        + " - Filial Saída: " + esc.getFilialSaida().getId()
                        + " - Filial Entrada: " + esc.getFilialEntrada().getId()
                        + " - Quantidade: " + esc.getQuantidade()
                        + " - Produto: (" + esc.getProduto().getId() + ") " + esc.getProduto().getDescricao()
                );
                String beforeEstoque = "" + estoque.getEstoque();
                estoque.setEstoque(estoque.getEstoque() + esc.getQuantidade());
                if (!dao.update(estoque)) {
                    dao.rollback();
                    GenericaMensagem.warn("Erro", "Ao excluir excluído");
                    novoLog.cancelList();
                    return;
                }
                novoLog.update("Mesmo do atualizado", 
                        "Estoque - ID: " + estoque.getId()
                        + " - Filial: (" + estoque.getFilial().getId() + ") " + estoque.getFilial().getFilial().getPessoa().getId()
                        + " - Produto: (" + estoque.getProduto().getId() + ") " + estoque.getProduto().getDescricao()
                        + " - Estoque Antes: " + beforeEstoque
                        + " - Estoque Atual: " + estoque.getEstoque()
                );                
                GenericaMensagem.warn("Sucesso", "Registro excluído");
                dao.commit();
                novoLog.saveList();
                ProdutoDao produtoDao = new ProdutoDao();
                estoque = (Estoque) produtoDao.listaEstoquePorProdutoFilial(produto, filial);
                quantidadeEstoque = estoque.getEstoque();
                nrEstoque = estoque.getEstoque();
            } else {
                novoLog.cancelList();
                dao.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir excluído");
            }
            listEstoqueSaidaConsumo[1].clear();
            removeQuantidadeEstoque();
        }
        novoLog.cancelList();
    }

    public EstoqueSaidaConsumo getEstoqueSaidaConsumo() {
        return estoqueSaidaConsumo;
    }

    public void setEstoqueSaidaConsumo(EstoqueSaidaConsumo estoqueSaidaConsumo) {
        this.estoqueSaidaConsumo = estoqueSaidaConsumo;
    }

    public List<Filial> getListFiliais() {
        if (listFiliais.isEmpty()) {
            Dao dao = new Dao();
            listFiliais = (List<Filial>) dao.list("Filial", true);
        }
        return listFiliais;
    }

    public void setListFiliais(List<Filial> listFiliais) {
        this.listFiliais = listFiliais;
    }

    public List<Departamento> getListDepartamentos() {
        if (listDepartamentos.isEmpty()) {
            Dao dao = new Dao();
            listDepartamentos = (List<Departamento>) dao.listQuery("FilialDepartamento", "findDepartamentoPorFilial", new Object[]{filial.getId()});
        }
        return listDepartamentos;
    }

    public void setListDepartamentos(List<Departamento> listDepartamentos) {
        this.listDepartamentos = listDepartamentos;
    }

    public List<EstoqueSaidaConsumo> getListEstoqueSaidaConsumoAdd() {
        if (listEstoqueSaidaConsumo[0].isEmpty()) {
            listEstoqueSaidaConsumo[0] = new ArrayList();
        }
        return listEstoqueSaidaConsumo[0];
    }

    public void setListEstoqueSaidaConsumoAdd(List<EstoqueSaidaConsumo> listEstoqueSaidaConsumo) {
        this.listEstoqueSaidaConsumo[0] = listEstoqueSaidaConsumo;
    }

    public List<EstoqueSaidaConsumo> getListEstoqueSaidaConsumo() {
        if (listEstoqueSaidaConsumo[1].isEmpty()) {
            ProdutoDao produtoDao = new ProdutoDao();
            listEstoqueSaidaConsumo[1].addAll(produtoDao.listaEstoqueSaidaConsumoProdutoTipo(estoque.getProduto().getId(), 1, "DESC", "ASC", "ASC", "ASC", "ASC"));
        }
        return listEstoqueSaidaConsumo[1];
    }

    public void setListEstoqueSaidaConsumo(List<EstoqueSaidaConsumo> listEstoqueSaidaConsumo) {
        this.listEstoqueSaidaConsumo[1] = listEstoqueSaidaConsumo;
    }

    public Filial getFilial() {
        if (!listFiliais.isEmpty()) {
            if (filial.getId() == -1) {
                filial = listFiliais.get(0);
            }
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
                estoqueSaidaConsumo = new EstoqueSaidaConsumo();
                estoque = new Estoque();
                produto = p;
                ProdutoDao produtoDao = new ProdutoDao();
                estoque = (Estoque) produtoDao.listaEstoquePorProdutoFilial(produto, filial);
                if (estoque != null) {
                    quantidadeEstoque = estoque.getEstoque();
                    nrEstoque = estoque.getEstoque();
                } else {
                    estoque = new Estoque();
                    quantidadeEstoque = 0;
                    nrEstoque = 0;
                }
                removeQuantidadeEstoque();
            }
        }
        return produto;
    }

    public int quantidadeRestanteFilial() {
        int estoqueUsado = 0;
        for (int i = 0; i < getListEstoqueSaidaConsumo().size(); i++) {
            estoqueUsado += listEstoqueSaidaConsumo[0].get(i).getQuantidade();
        }
        int estoqueAtual = estoque.getEstoque() - estoqueUsado;
        if (estoqueAtual >= 0) {
            return estoqueAtual;
        }
        return estoqueAtual;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Departamento getSelectedDepartamento() {
        return selectedDepartamento;
    }

    public void setSelectedDepartamento(Departamento selectedDepartamento) {
        this.selectedDepartamento = selectedDepartamento;
    }

    public Filial getSelectedFilialDestino() {
        return selectedFilialDestino;
    }

    public void setSelectedFilialDestino(Filial selectedFilialDestino) {
        this.selectedFilialDestino = selectedFilialDestino;
    }

    public void removeQuantidadeEstoque() {
        if (estoque.getId() != -1) {
            int quantidadeAdd = 0;
            int quantidadeEst = 0;
            if (!listEstoqueSaidaConsumo[0].isEmpty()) {
                for (EstoqueSaidaConsumo lesc : listEstoqueSaidaConsumo[0]) {
                    quantidadeAdd += lesc.getQuantidade();
                }
            }
            if (estoqueSaidaConsumo.getQuantidade() < 0) {
                return;
            }
            quantidadeEst = estoque.getEstoque() - estoqueSaidaConsumo.getQuantidade() - quantidadeAdd;
            color = "";
            if (quantidadeEst < estoque.getEstoqueMinimo()) {
                color = "color: red; border: 1px dashed red!important;";
                GenericaMensagem.warn("Validação", "Limite do estoque mínimo excedido. Cadastrar mais produtos no estoque desta filial.");
            } else {
                int percent = quantidadeEst;
                if (percent < 10) {
                    color = "color: orange; border: 1px solid yellow!important;";
                } else {
                    color = "color: green; border: 1px solid green!important;";
                }
            }
            if (quantidadeEst >= 0) {
                quantidadeEstoque = quantidadeEst;
            }
            PF.update("form_baixa_produto:");
        }
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getNrEstoque() {
        return nrEstoque;
    }

    public void setNrEstoque(int nrEstoque) {
        this.nrEstoque = nrEstoque;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
