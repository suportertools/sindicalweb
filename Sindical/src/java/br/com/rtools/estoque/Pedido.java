package br.com.rtools.estoque;

import br.com.rtools.financeiro.Lote;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "est_pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_quantidade", columnDefinition = "integer default 0")
    private int quantidade;
    @Column(name = "nr_valor_unitario", columnDefinition = "double precision default 0")
    private float valorUnitario;
    @Column(name = "nr_desconto_unitario", columnDefinition = "double precision default 0")
    private float descontoUnitario;
    @JoinColumn(name = "id_lote", referencedColumnName = "id")
    @OneToOne
    private Lote lote;
    @JoinColumn(name = "id_produto", referencedColumnName = "id")
    @OneToOne
    private Produto produto;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id", columnDefinition = "integer default 1")
    @OneToOne
    private EstoqueTipo estoqueTipo;

    public Pedido() {
        this.id = -1;
        this.quantidade = 0;
        this.valorUnitario = 0;
        this.descontoUnitario = 0;
        this.lote = new Lote();
        this.produto = new Produto();
        this.estoqueTipo = new EstoqueTipo();
    }

    public Pedido(int id, int quantidade, float valorUnitario, float descontoUnitario, Lote lote, Produto produto, EstoqueTipo estoqueTipo) {
        this.id = id;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.descontoUnitario = descontoUnitario;
        this.lote = lote;
        this.produto = produto;
        this.estoqueTipo = estoqueTipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(float valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public float getDescontoUnitario() {
        return descontoUnitario;
    }

    public void setDescontoUnitario(float descontoUnitario) {
        this.descontoUnitario = descontoUnitario;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public EstoqueTipo getEstoqueTipo() {
        return estoqueTipo;
    }

    public void setEstoqueTipo(EstoqueTipo estoqueTipo) {
        this.estoqueTipo = estoqueTipo;
    }

    public String getValorUnitarioString() {
        return Moeda.converteR$Float(valorUnitario);
    }

    public void setValorUnitarioString(String valorUnitarioString) {
        this.valorUnitario = Moeda.substituiVirgulaFloat(valorUnitarioString);
    }

    public String getDescontoUnitarioString() {
        return Moeda.converteR$Float(descontoUnitario);
    }

    public void setDescontoUnitarioString(String descontoUnitarioString) {
        this.descontoUnitario = Moeda.substituiVirgulaFloat(descontoUnitarioString);
    }

    @Override
    public String toString() {
        return "Pedido{" + "id=" + id + ", quantidade=" + quantidade + ", valorUnitario=" + valorUnitario + ", descontoUnitario=" + descontoUnitario + ", lote=" + lote + ", produto=" + produto + ", estoqueTipo=" + estoqueTipo + '}';
    }

}
