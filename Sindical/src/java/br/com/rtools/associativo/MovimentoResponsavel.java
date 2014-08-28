package br.com.rtools.associativo;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "soc_movimento_responsavel")
@NamedQuery(name = "MovimentoResponsavel.pesquisaID", query = "select m from MovimentoResponsavel m where m.id=:pid")
public class MovimentoResponsavel implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_titular", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa titular;
    @JoinColumn(name = "id_beneficiario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa beneficiario;

    public MovimentoResponsavel() {
        this.id = -1;
        this.movimento = new Movimento();
        this.titular = new Pessoa();
        this.beneficiario = new Pessoa();
    }

    public MovimentoResponsavel(int id, Movimento movimento, Pessoa titular, Pessoa beneficiario) {
        this.id = id;
        this.movimento = movimento;
        this.titular = titular;
        this.beneficiario = beneficiario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Pessoa getTitular() {
        return titular;
    }

    public void setTitular(Pessoa titular) {
        this.titular = titular;
    }

    public Pessoa getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Pessoa beneficiario) {
        this.beneficiario = beneficiario;
    }
}
