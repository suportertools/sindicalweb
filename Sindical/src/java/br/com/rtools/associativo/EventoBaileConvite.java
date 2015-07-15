package br.com.rtools.associativo;

import br.com.rtools.financeiro.Movimento;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "eve_evento_baile_convite")
public class EventoBaileConvite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_evento_baile", referencedColumnName = "id")
    @ManyToOne
    private EventoBaile eventoBaile;
    @Column(name = "nr_convite")
    private Integer convite;
    @JoinColumn(name = "id_venda", referencedColumnName = "id")
    @ManyToOne
    private BVenda bVenda;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @ManyToOne
    private AStatus status;    
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;    

    public EventoBaileConvite() {
        this.id = -1;
        this.eventoBaile = new EventoBaile();
        this.convite = 0;
        this.bVenda = new BVenda();
        this.status = new AStatus();
        this.movimento = null;
    }
    
    public EventoBaileConvite(int id, EventoBaile eventoBaile, int convite, BVenda bVenda, AStatus status, Movimento movimento) {
        this.id = id;
        this.eventoBaile = eventoBaile;
        this.convite = convite;
        this.bVenda = bVenda;
        this.status = status;
        this.movimento = movimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventoBaile getEventoBaile() {
        return eventoBaile;
    }

    public void setEventoBaile(EventoBaile eventoBaile) {
        this.eventoBaile = eventoBaile;
    }

    public int getConvite() {
        return convite;
    }

    public void setConvite(Integer convite) {
        this.convite = convite;
    }
    
    public BVenda getbVenda() {
        return bVenda;
    }

    public void setbVenda(BVenda bVenda) {
        this.bVenda = bVenda;
    }

    public AStatus getStatus() {
        return status;
    }

    public void setStatus(AStatus status) {
        this.status = status;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }
}
