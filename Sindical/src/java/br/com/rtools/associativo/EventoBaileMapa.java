package br.com.rtools.associativo;

import java.io.Serializable;
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
@Table(name = "EVE_EVENTO_BAILE_MAPA")
@NamedQuery(name = "EventoBaileMapa.pesquisaID", query = "select ebm from EventoBaileMapa ebm where ebm.id = :pid")
public class EventoBaileMapa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_EVENTO_BAILE", referencedColumnName = "ID")
    @ManyToOne
    private EventoBaile eventoBaile;
    @Column(name = "NR_MESA")
    private int mesa;
    @Column(name = "DS_COMPONENTE_ID")
    private String componenteId;
    @Column(name = "DS_POSICAO")
    private String posicao;

    public EventoBaileMapa() {
        this.id = -1;
        this.eventoBaile = new EventoBaile();
        this.mesa = 0;
        this.componenteId = "";
        this.posicao = "";
    }
    
    public EventoBaileMapa(int id, EventoBaile eventoBaile, int mesa, String componenteId, String posicao) {
        this.id = id;
        this.eventoBaile = eventoBaile;
        this.mesa = mesa;
        this.componenteId = componenteId;
        this.posicao = posicao;
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

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getComponenteId() {
        return componenteId;
    }

    public void setComponenteId(String componenteId) {
        this.componenteId = componenteId;
    }
    
    
}
