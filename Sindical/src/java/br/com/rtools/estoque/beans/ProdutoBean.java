package br.com.rtools.estoque.beans;

import br.com.rtools.estoque.Estoque;
import br.com.rtools.estoque.EstoqueTipo;
import br.com.rtools.estoque.Produto;
import br.com.rtools.estoque.ProdutoGrupo;
import br.com.rtools.estoque.ProdutoSubGrupo;
import br.com.rtools.estoque.ProdutoUnidade;
import br.com.rtools.estoque.dao.ProdutoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.sistema.Cor;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ProdutoBean implements Serializable {

    private Produto produto;
    private Produto produtoPesquisa;
    private Estoque estoque;
    private ProdutoGrupo produtoGrupo;
    private ProdutoSubGrupo produtoSubGrupo;
    private ProdutoUnidade produtoUnidade;
    private Cor cor;
    private String mensagem;
    private String comoPesquisa;
    // [0] ProdutoGrupo - [1] ProdutoSubGrupo - [2] ProdutoUnidade - [3] Cor
    private int[] indices;
    private List<SelectItem> listaSelectItem[];
    private List<Produto> listaProdutos;
    private List<Estoque> listaEstoque;
    private String custoMedio;

    @PostConstruct
    public void init() {
        produto = new Produto();
        produtoPesquisa = new Produto();
        estoque = new Estoque();
        produtoGrupo = new ProdutoGrupo();
        produtoSubGrupo = new ProdutoSubGrupo();
        produtoUnidade = new ProdutoUnidade();
        cor = new Cor();
        mensagem = "";
        comoPesquisa = "";
        listaSelectItem = new ArrayList[6];
        listaSelectItem[0] = new ArrayList<SelectItem>();
        listaSelectItem[1] = new ArrayList<SelectItem>();
        listaSelectItem[2] = new ArrayList<SelectItem>();
        listaSelectItem[3] = new ArrayList<SelectItem>();
        listaSelectItem[4] = new ArrayList<SelectItem>();
        listaSelectItem[5] = new ArrayList<SelectItem>();
        listaProdutos = new ArrayList<Produto>();
        listaEstoque = new ArrayList<Estoque>();
        indices = new int[6];
        indices[0] = 0;
        indices[1] = 0;
        indices[2] = 0;
        indices[3] = 0;
        indices[4] = 0;
        indices[5] = 0;
        custoMedio = "0";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("produtoBean");
    }

    public void clear() {
        GenericaSessao.remove("produtoBean");
    }

    public void save() {
        if (produto.getDescricao().isEmpty()) {
            mensagem = "Informar a descrição do produto!";
            return;
        }
        if (listaSelectItem[0].isEmpty()) {
            mensagem = "Cadastrar grupo de produtos!";
            return;
        }
        Dao dao = new Dao();
        dao.openTransaction();
        produto.setProdutoGrupo((ProdutoGrupo) dao.find(new ProdutoGrupo(), Integer.parseInt(listaSelectItem[0].get(indices[0]).getDescription())));
        if (listaSelectItem[1].isEmpty()) {
            produto.setProdutoSubGrupo(null);
        } else {
            produto.setProdutoSubGrupo((ProdutoSubGrupo) dao.find(new ProdutoSubGrupo(), Integer.parseInt(listaSelectItem[1].get(indices[1]).getDescription())));
        }
        if (listaSelectItem[1].isEmpty()) {
            mensagem = "Cadastrar unidades!";
            return;
        } else {
            produto.setProdutoUnidade((ProdutoUnidade) dao.find(new ProdutoUnidade(), Integer.parseInt(listaSelectItem[2].get(indices[2]).getDescription())));
        }
        if (listaSelectItem[1].isEmpty()) {
            mensagem = "Cadastrar cores!";
            return;
        } else {
            produto.setCor((Cor) dao.find(new Cor(), Integer.parseInt(listaSelectItem[3].get(indices[3]).getDescription())));
        }
        if (produto.getId() == -1) {
            if (dao.save(produto)) {
                dao.commit();
                NovoLog novoLog = new NovoLog();
                novoLog.novo("Salvar", produto.toString());
                mensagem = "Produto cadastrado com sucesso.";
            } else {
                dao.rollback();
                mensagem = "Erro ao adicionar registro!";
            }
        } else {
            Produto produtoAntes = (Produto) dao.find(produto);
            if (dao.update(produto)) {
                if (!produto.equals(produtoAntes)) {
                    NovoLog novoLog = new NovoLog();
                    novoLog.novo("Alterar", "De" + produtoAntes.toString() + " para " + produto.toString());
                }
                dao.commit();
                mensagem = "Produto atualizado com sucesso.";
            } else {
                mensagem = "Erro ao atualizado produto!";
                dao.rollback();
            }
        }
    }

    public String edit(Produto p) {
        Dao dao = new Dao();
        produto = (Produto) dao.rebind(p);
        for (int i = 0; i < getListaGrupos().size(); i++) {
            if (produto.getProdutoGrupo().getId() == Integer.parseInt(getListaGrupos().get(i).getDescription())) {
                indices[0] = i;
                break;
            }
        }
        for (int i = 0; i < getListaSubGrupos().size(); i++) {
            if (produto.getProdutoSubGrupo().getId() == Integer.parseInt(getListaSubGrupos().get(i).getDescription())) {
                indices[1] = i;
                break;
            }
        }
        for (int i = 0; i < getListaUnidades().size(); i++) {
            if (produto.getProdutoUnidade().getId() == Integer.parseInt(getListaUnidades().get(i).getDescription())) {
                indices[2] = i;
                break;
            }
        }
        for (int i = 0; i < getListaCores().size(); i++) {
            if (produto.getCor().getId() == Integer.parseInt(getListaCores().get(i).getDescription())) {
                indices[3] = i;
                break;
            }
        }
        GenericaSessao.put("linkClicado", true);
        if (!GenericaSessao.exists("urlRetorno")) {
            return "produto";
        } else {
            GenericaSessao.put("pesquisaProduto", produto);
            return GenericaSessao.getString("urlRetorno");
        }
    }

    public void delete() {
        if (produto.getId() != -1) {
            Dao dao = new Dao();
            dao.openTransaction();
            for (Estoque listaEstoque1 : listaEstoque) {
                if (!dao.delete(dao.find(listaEstoque1))) {
                    dao.rollback();
                    mensagem = "Erro ao excluir produtos do estoque!";
                    return;
                }
            }
            if (dao.delete(dao.find(produto))) {
                NovoLog novoLog = new NovoLog();
                novoLog.novo("Excluir", produto.toString());
                dao.commit();
                clear();
                mensagem = "Produto excluído com sucesso.";
            } else {
                dao.rollback();
                mensagem = "Erro ao excluir produto!";
            }
        }
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<SelectItem> getListaGrupos() {
        if (listaSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<ProdutoGrupo> list = (List<ProdutoGrupo>) dao.list(new ProdutoGrupo(), true);
            for (int i = 0; i < list.size(); i++) {
                listaSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listaSelectItem[0].isEmpty()) {
            return listaSelectItem[0];
        } else {
            return new ArrayList<SelectItem>();
        }
    }

    public List<SelectItem> getListaSubGrupos() {
        if (!listaSelectItem[0].isEmpty()) {
            if (listaSelectItem[1].isEmpty()) {
                Dao dao = new Dao();                
                List<ProdutoSubGrupo> list = (List<ProdutoSubGrupo>) dao.listQuery(new ProdutoSubGrupo(), "findGrupo", new Object[]{getListaGrupos().get(indices[0]).getDescription()});
                for (int i = 0; i < list.size(); i++) {
                    listaSelectItem[1].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                }
            }
        }
        if (!listaSelectItem[1].isEmpty()) {
            return listaSelectItem[1];
        } else {
            return new ArrayList<SelectItem>();
        }
    }

    public List<SelectItem> getListaUnidades() {
        if (listaSelectItem[2].isEmpty()) {
            Dao dao = new Dao();
            List<ProdutoUnidade> list = (List<ProdutoUnidade>) dao.list(new ProdutoUnidade(), true);
            for (int i = 0; i < list.size(); i++) {
                listaSelectItem[2].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listaSelectItem[2].isEmpty()) {
            return listaSelectItem[2];
        } else {
            return new ArrayList<SelectItem>();
        }
    }

    public List<SelectItem> getListaCores() {
        if (listaSelectItem[3].isEmpty()) {
            Dao dao = new Dao();
            List<Cor> list = (List<Cor>) dao.list(new Cor(), true);
            for (int i = 0; i < list.size(); i++) {
                listaSelectItem[3].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listaSelectItem[3].isEmpty()) {
            return listaSelectItem[3];
        } else {
            return new ArrayList<SelectItem>();
        }
    }

    public List<SelectItem> getListaTipo() {
        if (listaSelectItem[4].isEmpty()) {
            Dao dao = new Dao();
            List<EstoqueTipo> list = (List<EstoqueTipo>) dao.list(new EstoqueTipo(), true);
            for (int i = 0; i < list.size(); i++) {
                listaSelectItem[4].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listaSelectItem[4].isEmpty()) {
            return listaSelectItem[4];
        } else {
            return new ArrayList<SelectItem>();
        }
    }

    public List<SelectItem> getListaFilial() {
        if (listaSelectItem[5].isEmpty()) {
            Dao dao = new Dao();
            List<Filial> list = (List<Filial>) dao.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listaSelectItem[5].add(new SelectItem(i, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        if (!listaSelectItem[5].isEmpty()) {
            return listaSelectItem[5];
        } else {
            return new ArrayList<SelectItem>();
        }
    }

    public void addProdutoEstoque() {
        if (listaSelectItem[5].isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar filial!");
            return;
        }
        if (listaSelectItem[4].isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar tipos de estoque!");
            return;
        }
        if (estoque.getEstoque() < 1) {
            GenericaMensagem.warn("Validação", "Informar quantidade!");
            return;
        }
        if (estoque.getEstoqueMinimo() < 1) {
            GenericaMensagem.warn("Validação", "Informar mínimo!");
            return;
        }
        if (estoque.getEstoqueMaximo() < 1) {
            GenericaMensagem.warn("Validação", "Informar estoque máximo!");
            return;
        }
        if (estoque.getEstoqueMaximo() < estoque.getEstoqueMinimo()) {
            GenericaMensagem.warn("Validação", "Estoque máximo deve ser maior que estoque mínimo!");
            return;
        }
        estoque.setCustoMedio(Moeda.converteUS$(custoMedio));
        Dao dao = new Dao();
        if (estoque.getId() == -1) {
            estoque.setEstoqueTipo((EstoqueTipo) dao.find(new EstoqueTipo(), Integer.parseInt(listaSelectItem[4].get(indices[4]).getDescription())));
            estoque.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(listaSelectItem[5].get(indices[5]).getDescription())));
            estoque.setProduto(produto);
            ProdutoDao produtoDao = new ProdutoDao();
            if (produtoDao.existeProdutoEstoqueFilialTipo(estoque)) {
                GenericaMensagem.warn("Validação", "Produto e tipo já cadastrado para esta filial!!");
                return;
            }
            dao.openTransaction();
            if (dao.save(estoque)) {
                dao.commit();
                GenericaMensagem.info("Sucesso", "Registro inserido som sucesso");
            } else {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Ao inserir registro!");
            }
        } else {
            dao.openTransaction();
            if (dao.update(estoque)) {
                dao.commit();
                GenericaMensagem.info("Sucesso", "Registro atualizado som sucesso");
            } else {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizado registro!");
            }
        }
        estoque = new Estoque();
        listaEstoque.clear();
        custoMedio = "";
    }

    public void editEstoque(Estoque e) {
        Dao dao = new Dao();
        estoque = (Estoque) dao.rebind(e);
        for (int i = 0; i < getListaFilial().size(); i++) {
            if (estoque.getFilial().getId() == Integer.parseInt(getListaFilial().get(i).getDescription())) {
                indices[5] = i;
                break;
            }
        }
        for (int i = 0; i < getListaTipo().size(); i++) {
            if (estoque.getEstoqueTipo().getId() == Integer.parseInt(getListaTipo().get(i).getDescription())) {
                indices[4] = i;
                break;
            }
        }
        custoMedio = Moeda.converteR$Float(estoque.getCustoMedio());
    }

    public void deleteEstoque(Estoque e) {
        if (e.getId() != -1) {
            Dao dao = new Dao();
            if (dao.delete((Estoque) dao.find(e), true)) {
                GenericaMensagem.info("Sucesso", "Registro removido");
            } else {
                GenericaMensagem.warn("Erro", "Ao remover registro!");
            }
        }
        listaEstoque.clear();
    }

    public void saveProdutoGrupo() {
        if (produtoGrupo.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar descrição!");
            return;
        }
        ProdutoDao produtoDao = new ProdutoDao();
        if (produtoDao.existeCor(produtoGrupo.getDescricao())) {
            GenericaMensagem.warn("Validação", "Produto Grupo já cadastrado!");
            return;
        }
        saveSubItens(produtoGrupo);
        produtoGrupo = new ProdutoGrupo();
        listaSelectItem[0].clear();
    }

    public void saveProdutoSubGrupo() {
        if (produtoSubGrupo.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar descrição!");
            return;
        }
        ProdutoDao produtoDao = new ProdutoDao();
        Dao dao = new Dao();
        produtoSubGrupo.setProdutoGrupo((ProdutoGrupo) dao.find(new ProdutoGrupo(), Integer.parseInt(getListaGrupos().get(indices[0]).getDescription())));
        if (produtoDao.existeProdutoSubGrupo(produtoSubGrupo.getDescricao())) {
            GenericaMensagem.warn("Validação", "Produto SubGrupo já cadastrado!");
            return;
        }
        if (dao.save(produtoSubGrupo, true)) {
            GenericaMensagem.info("Sucesso", "Registro inserido som sucesso");
        } else {
            GenericaMensagem.warn("Erro", "Ao inserir registro!");
        }
        produtoSubGrupo = new ProdutoSubGrupo();
        listaSelectItem[1].clear();
    }

    public void saveProdutoUnidade() {
        if (produtoUnidade.getDescricao().isEmpty()) {
            GenericaMensagem.info("Validação", "Informar descrição!");
            return;
        }
        ProdutoDao produtoDao = new ProdutoDao();
        if (produtoDao.existeProdutoUnidade(produtoUnidade.getDescricao())) {
            GenericaMensagem.warn("Validação", "Unidade já cadastrada!");
            return;
        }
        saveSubItens(produtoUnidade);
        produtoUnidade = new ProdutoUnidade();
        listaSelectItem[2].clear();
    }

    public void saveCor() {
        if (cor.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar descrição!");
            return;
        }
        ProdutoDao produtoDao = new ProdutoDao();
        if (produtoDao.existeCor(cor.getDescricao())) {
            GenericaMensagem.warn("Validação", "Cor já cadastrada!");
            return;
        }
        saveSubItens(cor);
        cor = new Cor();
        listaSelectItem[3].clear();
    }

    public void saveSubItens(Object object) {
        Dao dao = new Dao();
        if (dao.save(object, true)) {
            GenericaMensagem.info("Sucesso", "Registro inserido som sucesso");
        } else {
            GenericaMensagem.warn("Erro", "Ao inserir registro!");
        }
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public ProdutoGrupo getProdutoGrupo() {
        return produtoGrupo;
    }

    public void setProdutoGrupo(ProdutoGrupo produtoGrupo) {
        this.produtoGrupo = produtoGrupo;
    }

    public ProdutoSubGrupo getProdutoSubGrupo() {
        return produtoSubGrupo;
    }

    public void setProdutoSubGrupo(ProdutoSubGrupo produtoSubGrupo) {
        this.produtoSubGrupo = produtoSubGrupo;
    }

    public ProdutoUnidade getProdutoUnidade() {
        return produtoUnidade;
    }

    public void setProdutoUnidade(ProdutoUnidade produtoUnidade) {
        this.produtoUnidade = produtoUnidade;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<Produto> getListaProdutos() {
        if (listaProdutos.isEmpty()) {
            ProdutoDao produtoDao = new ProdutoDao();
            listaProdutos = (List<Produto>) produtoDao.pesquisaProduto(produtoPesquisa, 0, comoPesquisa);
        }
        return listaProdutos;
    }

    public void setListaProdutos(List<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public Produto getProdutoPesquisa() {
        return produtoPesquisa;
    }

    public void setProdutoPesquisa(Produto produtoPesquisa) {
        this.produtoPesquisa = produtoPesquisa;
    }

    public void acaoPesquisaInicial() {
        listaProdutos.clear();
        comoPesquisa = "Inicial";
    }

    public void acaoPesquisaParcial() {
        listaProdutos.clear();
        comoPesquisa = "Parcial";
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public List<Estoque> getListaEstoque() {
        if (listaEstoque.isEmpty()) {
            if (produto.getId() != -1) {
                ProdutoDao produtoDao = new ProdutoDao();
                listaEstoque = (List<Estoque>) produtoDao.listaEstoquePorProduto(produto);
            }
        }
        return listaEstoque;
    }

    public void setListaEstoque(List<Estoque> listaEstoque) {
        this.listaEstoque = listaEstoque;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public String getCustoMedio() {
        return custoMedio;
    }

    public void setCustoMedio(String custoMedio) {
        this.custoMedio = custoMedio;
    }
}
