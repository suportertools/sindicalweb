package br.com.rtools.associativo;

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
@Table(name = "eve_mesa")
@NamedQuery(name = "Mesa.pesquisaID", query = "select s from Mesa s where s.id=:pid")
public class Mesa implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_venda", referencedColumnName = "id")
    @ManyToOne
    private BVenda bVenda;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @ManyToOne
    private AStatus status;
    @JoinColumn(name = "id_evento_baile_mapa")
    @ManyToOne
    private EventoBaileMapa eventoBaileMapa;

    public Mesa() {
        this.id = -1;
        this.bVenda = new BVenda();
        this.status = new AStatus();
        this.eventoBaileMapa = new EventoBaileMapa();
    }

    public Mesa(int id, BVenda bVenda, AStatus status, EventoBaileMapa eventoBaileMapa) {
        this.id = id;
        this.bVenda = bVenda;
        this.status = status;
        this.eventoBaileMapa = eventoBaileMapa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public EventoBaileMapa getEventoBaileMapa() {
        return eventoBaileMapa;
    }

    public void setEventoBaileMapa(EventoBaileMapa eventoBaileMapa) {
        this.eventoBaileMapa = eventoBaileMapa;
    }

}