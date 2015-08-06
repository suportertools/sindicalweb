package br.com.rtools.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fin_estorno_caixa")
public class EstornoCaixa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_estorno_caixa_lote", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private EstornoCaixaLote estornoCaixaLote;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;

    public EstornoCaixa() {
        this.id = -1;
        this.estornoCaixaLote = new EstornoCaixaLote();
        this.movimento = new Movimento();
    }

    public EstornoCaixa(int id, EstornoCaixaLote estornoCaixaLote, Movimento movimento) {
        this.id = id;
        this.estornoCaixaLote = estornoCaixaLote;
        this.movimento = movimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EstornoCaixaLote getEstornoCaixaLote() {
        return estornoCaixaLote;
    }

    public void setEstornoCaixaLote(EstornoCaixaLote estornoCaixaLote) {
        this.estornoCaixaLote = estornoCaixaLote;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

}
