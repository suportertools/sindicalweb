package br.com.rtools.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fin_cobranca")
@NamedQuery(name = "Cobranca.pesquisaID", query = "select c from Cobranca c where c.id = :pid")
public class Cobranca implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_lote", referencedColumnName = "id")
    @ManyToOne
    private CobrancaLote lote;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;

    public Cobranca() {
        this.id = -1;
        this.lote = new CobrancaLote();
        this.movimento = new Movimento();
    }

    public Cobranca(int id, CobrancaLote lote, Movimento movimento) {
        this.id = id;
        this.lote = lote;
        this.movimento = movimento;
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

    public CobrancaLote getLote() {
        return lote;
    }

    public void setLote(CobrancaLote lote) {
        this.lote = lote;
    }
}
