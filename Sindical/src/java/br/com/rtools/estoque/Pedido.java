package br.com.rtools.estoque;

import br.com.rtools.financeiro.Lote;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "EST_PEDIDO")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NR_QUANTIDADE", columnDefinition = "INTEGER DEFAULT 0")
    private int estoque;
    @Column(name = "NR_VALOR_UNITARIO", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private float valorUnitario;
    @Column(name = "NR_DESCONTO_UNITARIO", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private float descontoUnitario;
    @JoinColumn(name = "ID_LOTE", referencedColumnName = "ID")
    @OneToOne
    private Lote lote;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @OneToOne
    private Produto produto;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "ID", columnDefinition = "INTEGER DEFAULT 1")
    @OneToOne
    private EstoqueTipo estoqueTipo;

    public Pedido() {
        this.id = -1;
        this.estoque = 0;
        this.valorUnitario = 0;
        this.descontoUnitario = 0;
        this.lote = new Lote();
        this.produto = new Produto();
        this.estoqueTipo = new EstoqueTipo();
    }

    public Pedido(int id, int estoque, float valorUnitario, float descontoUnitario, Lote lote, Produto produto, EstoqueTipo estoqueTipo) {
        this.id = id;
        this.estoque = estoque;
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

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
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

    @Override
    public String toString() {
        return "Pedido{" + "id=" + id + ", estoque=" + estoque + ", valorUnitario=" + valorUnitario + ", descontoUnitario=" + descontoUnitario + ", lote=" + lote + ", produto=" + produto + ", estoqueTipo=" + estoqueTipo + '}';
    }

}
