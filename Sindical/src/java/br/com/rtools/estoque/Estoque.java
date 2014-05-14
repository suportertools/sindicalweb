package br.com.rtools.estoque;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "EST_ESTOQUE",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PRODUTO", "ID_FILIAL", "ID_TIPO"})
)
public class Estoque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NR_ESTOQUE", columnDefinition = "INTEGER DEFAULT 0")
    private int estoque;
    @Column(name = "NR_ESTOQUE_MINIMO", columnDefinition = "INTEGER DEFAULT 1")
    private int estoqueMinimo;
    @Column(name = "NR_ESTOQUE_MAXIMO", columnDefinition = "INTEGER DEFAULT 1")
    private int estoqueMaximo;
    @Column(name = "NR_CUSTO_MEDIO", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private float custoMedio;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @OneToOne
    private Produto produto;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID")
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "ID")
    @OneToOne
    private EstoqueTipo estoqueTipo;
    @JoinColumn(name = "IS_ATIVO", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;

    public Estoque() {
        this.id = -1;
        this.estoque = 0;
        this.estoqueMinimo = 0;
        this.estoqueMaximo = 0;
        this.custoMedio = 0;
        this.produto = new Produto();
        this.filial = new Filial();
        this.estoqueTipo = new EstoqueTipo();
        this.ativo = true;
    }

    public Estoque(int id, int estoque, int estoqueMinimo, int estoqueMaximo, float custoMedio, Produto produto, Filial filial, EstoqueTipo estoqueTipo, boolean ativo) {
        this.id = id;
        this.estoque = estoque;
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueMaximo = estoqueMaximo;
        this.custoMedio = custoMedio;
        this.produto = produto;
        this.filial = filial;
        this.estoqueTipo = estoqueTipo;
        this.ativo = ativo;
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

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(int estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public float getCustoMedio() {
        return custoMedio;
    }

    public void setCustoMedio(float custoMedio) {
        this.custoMedio = custoMedio;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public EstoqueTipo getEstoqueTipo() {
        return estoqueTipo;
    }

    public void setEstoqueTipo(EstoqueTipo estoqueTipo) {
        this.estoqueTipo = estoqueTipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCustoMedioString() {
        return Moeda.converteR$Float(custoMedio);
    }

    public void setCustoMedioString(String custoMedioString) {
        this.custoMedio = Moeda.converteUS$(custoMedioString);
    }

    @Override
    public String toString() {
        return "Estoque{" + "id=" + id + ", estoque=" + estoque + ", estoqueMinimo=" + estoqueMinimo + ", estoqueMaximo=" + estoqueMaximo + ", custoMedio=" + custoMedio + ", produto=" + produto + ", filial=" + filial + ", estoqueTipo=" + estoqueTipo + ", ativo=" + ativo + '}';
    }
}
