package br.com.rtools.estoque;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "est_saida_consumo")
public class EstoqueSaidaConsumo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_produto", referencedColumnName = "id")
    @OneToOne
    private Produto produto;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id")
    @OneToOne
    private EstoqueTipo estoqueTipo;
    @JoinColumn(name = "id_filial_saida", referencedColumnName = "id")
    @OneToOne
    private Filial filialSaida;
    @JoinColumn(name = "id_filial_entrada", referencedColumnName = "id")
    @OneToOne
    private Filial filialEntrada;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @OneToOne
    private Departamento departamento;
    @Column(name = "nr_qtde", columnDefinition = "integer default 0")
    private int quantidade;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;

    public EstoqueSaidaConsumo() {
        this.id = -1;
        this.produto = new Produto();
        this.estoqueTipo = new EstoqueTipo();
        this.filialSaida = new Filial();
        this.filialEntrada = new Filial();
        this.departamento = new Departamento();
        this.quantidade = 0;
        this.dtLancamento = new Date();
    }

    public EstoqueSaidaConsumo(int id, Produto produto, EstoqueTipo estoqueTipo, Filial filialSaida, Filial filialEntrada, Departamento departamento, int quantidade, String dtLancamento) {
        this.id = id;
        this.produto = produto;
        this.estoqueTipo = estoqueTipo;
        this.filialSaida = filialSaida;
        this.filialEntrada = filialEntrada;
        this.departamento = departamento;
        this.quantidade = quantidade;
        this.dtLancamento = DataHoje.converte(dtLancamento);
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

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public String getLancamento() {
        return DataHoje.converteData(dtLancamento);
    }

    public void setDtLancamento(String dtLancamento) {
        this.dtLancamento = DataHoje.converte(dtLancamento);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.id;
        hash = 79 * hash + (this.produto != null ? this.produto.hashCode() : 0);
        hash = 79 * hash + (this.estoqueTipo != null ? this.estoqueTipo.hashCode() : 0);
        hash = 79 * hash + (this.filialSaida != null ? this.filialSaida.hashCode() : 0);
        hash = 79 * hash + (this.filialEntrada != null ? this.filialEntrada.hashCode() : 0);
        hash = 79 * hash + (this.departamento != null ? this.departamento.hashCode() : 0);
        hash = 79 * hash + this.quantidade;
        hash = 79 * hash + (this.dtLancamento != null ? this.dtLancamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EstoqueSaidaConsumo other = (EstoqueSaidaConsumo) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.produto != other.produto && (this.produto == null || !this.produto.equals(other.produto))) {
            return false;
        }
        if (this.estoqueTipo != other.estoqueTipo && (this.estoqueTipo == null || !this.estoqueTipo.equals(other.estoqueTipo))) {
            return false;
        }
        if (this.filialSaida != other.filialSaida && (this.filialSaida == null || !this.filialSaida.equals(other.filialSaida))) {
            return false;
        }
        if (this.filialEntrada != other.filialEntrada && (this.filialEntrada == null || !this.filialEntrada.equals(other.filialEntrada))) {
            return false;
        }
        if (this.departamento != other.departamento && (this.departamento == null || !this.departamento.equals(other.departamento))) {
            return false;
        }
        if (this.quantidade != other.quantidade) {
            return false;
        }
        if (this.dtLancamento != other.dtLancamento && (this.dtLancamento == null || !this.dtLancamento.equals(other.dtLancamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EstoqueSaidaConsumo{" + "id=" + id + ", produto=" + produto + ", estoqueTipo=" + estoqueTipo + ", filialSaida=" + filialSaida + ", filialEntrada=" + filialEntrada + ", departamento=" + departamento + ", quantidade=" + quantidade + ", dtLancamento=" + dtLancamento + '}';
    }

}
