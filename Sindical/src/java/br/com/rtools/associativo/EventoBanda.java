package br.com.rtools.associativo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EVE_EVENTO_BANDA")
@NamedQuery(name = "EventoBanda.pesquisaID", query = "select s from EventoBanda s where s.id=:pid")
public class EventoBanda implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_BANDA", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Banda banda;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = true)
    @OneToOne
    private AEvento evento;

    public EventoBanda() {
        this.id = -1;
        this.banda = new Banda();
        this.evento = new AEvento();
    }

    public EventoBanda(int id, Banda banda, AEvento evento) {
        this.id = id;
        this.banda = banda;
        this.evento = evento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Banda getBanda() {
        return banda;
    }

    public void setBanda(Banda banda) {
        this.banda = banda;
    }

    public AEvento getEvento() {
        return evento;
    }

    public void setEvento(AEvento evento) {
        this.evento = evento;
    }
}