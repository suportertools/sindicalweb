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
@Table(name = "eve_evento_banda")
@NamedQuery(name = "EventoBanda.pesquisaID", query = "select s from EventoBanda s where s.id=:pid")
public class EventoBanda implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_banda", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Banda banda;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = true)
    @ManyToOne
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