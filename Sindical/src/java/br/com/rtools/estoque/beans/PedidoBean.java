package br.com.rtools.estoque.beans;

import br.com.rtools.estoque.EstoqueTipo;
import br.com.rtools.estoque.Pedido;
import br.com.rtools.estoque.Produto;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
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
public class PedidoBean implements Serializable {

    private Pedido pedido;
    private String mensagem;
    // [0] null
    private int[] indices;
    private List<SelectItem> listaSelectItem[];
    private List<Pedido> listaPedidos;
    private String descontoUnitarioPedido;
    private String valorUnitarioPedido;
    private String quantidadePedido;
    private boolean modalPedido;
    private float valorTotal;

    @PostConstruct
    public void init() {
        pedido = new Pedido();
        mensagem = "";
        listaSelectItem = new ArrayList[0];
        listaPedidos = new ArrayList<Pedido>();
        indices = new int[0];
        descontoUnitarioPedido = "0,00";
        valorUnitarioPedido = "0,00";
        quantidadePedido = "0";
        modalPedido = false;
        valorTotal = 0;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("pedidoBean");
    }

    public void clear() {
        GenericaSessao.remove("pedidoBean");
    }

    public void novoPedido() {
        pedido = new Pedido();
        mensagem = "";
        listaPedidos = new ArrayList<Pedido>();
        descontoUnitarioPedido = "0,00";
        valorUnitarioPedido = "0,00";
        quantidadePedido = "0";
    }

    public void openModalPedido() {
        modalPedido = true;
    }

    public void closeModalPedido() {
        modalPedido = false;
    }

    public boolean salvarPedido(SalvarAcumuladoDB sadb) {
        if (!listaPedidos.isEmpty()) {
            for (Pedido listaPedido : listaPedidos) {
                if (listaPedido.getId() == -1) {
                    if (!sadb.inserirObjeto(listaPedido)) {
                        return false;
                    }
                } else {
                    if (!sadb.alterarObjeto(listaPedido)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deletarPedido(SalvarAcumuladoDB sadb) {
        if (!listaPedidos.isEmpty()) {
            for (Pedido listaPedido : listaPedidos) {
                if (listaPedido.getId() != -1) {
                    if (!sadb.deletarObjeto(sadb.find(listaPedido))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void addItemPedido() {
        pedido.setValorUnitario(Moeda.substituiVirgulaFloat(valorUnitarioPedido));
        pedido.setDescontoUnitario(Moeda.substituiVirgulaFloat(descontoUnitarioPedido));
        pedido.setQuantidade(Integer.parseInt(quantidadePedido));
        if (pedido.getProduto().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar um produto!");
            return;
        }
        if (pedido.getQuantidade() < 1) {
            GenericaMensagem.warn("Validação", "Adicionar quantidade!");
            return;
        }
        if (pedido.getValorUnitario() < 1) {
            GenericaMensagem.warn("Validação", "Informar valor do produto!");
            return;
        }
        Dao dao = new Dao();
        dao.openTransaction();
        pedido.setEstoqueTipo((EstoqueTipo) dao.find(new EstoqueTipo(), 1));
        dao.commit();
        listaPedidos.add(pedido);
        pedido = new Pedido();
    }

    public void editarItemPedido(int index) {
        Dao dao = new Dao();
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (i == index) {
                if (listaPedidos.get(index).getId() == -1) {
                    pedido = listaPedidos.get(index);
                } else {
                    pedido = (Pedido) dao.rebind(listaPedidos.get(index));
                }
                break;
            }
        }
    }

    public void removeItemPedido(int index) {
        boolean erro = false;
        Dao dao = new Dao();
        dao.openTransaction();
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (i == index) {
                if (listaPedidos.get(i).getId() != -1) {
                    if (!dao.delete(dao.find(listaPedidos.get(i)))) {
                        dao.rollback();
                        erro = true;
                        break;
                    }
                }
                listaPedidos.remove(i);
                break;
            }
        }
        if (erro) {
            dao.rollback();
        } else {
            dao.commit();
        }
    }

    public Pedido getPedido() {
        if (GenericaSessao.exists("pesquisaProduto")) {
            pedido.setProduto((Produto) GenericaSessao.getObject("pesquisaProduto", true));
        }
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public List<SelectItem>[] getListaSelectItem() {
        return listaSelectItem;
    }

    public void setListaSelectItem(List<SelectItem>[] listaSelectItem) {
        this.listaSelectItem = listaSelectItem;
    }

    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public String getDescontoUnitarioPedido() {
        return descontoUnitarioPedido;
    }

    public void setDescontoUnitarioPedido(String descontoUnitarioPedido) {
        this.descontoUnitarioPedido = descontoUnitarioPedido;
    }

    public String getValorUnitarioPedido() {
        return valorUnitarioPedido;
    }

    public void setValorUnitarioPedido(String valorUnitarioPedido) {
        this.valorUnitarioPedido = valorUnitarioPedido;
    }

    public String getQuantidadePedido() {
        return quantidadePedido;
    }

    public void setQuantidadePedido(String quantidadePedido) {
        this.quantidadePedido = quantidadePedido;
    }

    public boolean isModalPedido() {
        return modalPedido;
    }

    public void setModalPedido(boolean modalPedido) {
        this.modalPedido = modalPedido;
    }

    public float getValorTotal() {
        float valor = 0;
        for (Pedido listaPedido : listaPedidos) {
            valor += listaPedido.getValorUnitario();
        }
        valorTotal = valor;
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }
}
