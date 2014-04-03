package br.com.rtools.estoque;

import br.com.rtools.financeiro.Lote;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "EST_PEDIDO_LOTE")
public class PedidoLote implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_LOTE", referencedColumnName = "ID")
    @OneToOne
    private Lote lote;

    public PedidoLote() {
        this.id = -1;
        this.lote = new Lote();
    }

    public PedidoLote(int id, Lote lote) {
        this.id = id;
        this.lote = lote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    @Override
    public String toString() {
        return "PedidoLote{" + "id=" + id + ", lote=" + lote + '}';
    }
}
