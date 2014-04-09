package br.com.rtools.estoque;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "EST_SAIDA_CONSUMO")
public class EstoqueSaidaConsumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @OneToOne
    private Produto produto;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "ID")
    @OneToOne
    private EstoqueTipo estoqueTipo;
    @JoinColumn(name = "ID_FILIAL_SAIDA", referencedColumnName = "ID")
    @OneToOne
    private Filial filialSaida;
    @JoinColumn(name = "ID_FILIAL_ENTRADA", referencedColumnName = "ID")
    @OneToOne
    private Filial filialEntrada;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID")
    @OneToOne
    private Departamento departamento;
    @Column(name = "NR_QTDE", columnDefinition = "INTEGER DEFAULT 0")
    private int quantidade;

    public EstoqueSaidaConsumo() {
        this.id = -1;
        this.produto = new Produto();
        this.estoqueTipo = new EstoqueTipo();
        this.filialSaida = new Filial();
        this.filialEntrada = new Filial();
        this.departamento = new Departamento();
        this.quantidade = 0;
    }

    public EstoqueSaidaConsumo(int id, Produto produto, EstoqueTipo estoqueTipo, Filial filialSaida, Filial filialEntrada, Departamento departamento, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.estoqueTipo = estoqueTipo;
        this.filialSaida = filialSaida;
        this.filialEntrada = filialEntrada;
        this.departamento = departamento;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Filial getFilialSaida() {
        return filialSaida;
    }

    public void setFilialSaida(Filial filialSaida) {
        this.filialSaida = filialSaida;
    }

    public Filial getFilialEntrada() {
        return filialEntrada;
    }

    public void setFilialEntrada(Filial filialEntrada) {
        this.filialEntrada = filialEntrada;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "EstoqueSaidaConsumo{" + "id=" + id + ", produto=" + produto + ", estoqueTipo=" + estoqueTipo + ", filialSaida=" + filialSaida + ", filialEntrada=" + filialEntrada + ", departamento=" + departamento + ", quantidade=" + quantidade + '}';
    }

}
