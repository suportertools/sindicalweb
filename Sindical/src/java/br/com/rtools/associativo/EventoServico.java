package br.com.rtools.associativo;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Servicos;
import javax.persistence.Column;
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
@Table(name = "EVE_EVENTO_SERVICO")
@NamedQuery(name = "EventoServico.pesquisaID", query = "select es from EventoServico es where es.id=:pid")
public class EventoServico implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private AEvento aEvento;
    @JoinColumn(name = "ID_SERVICOS", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Servicos servicos;
    @Column(name = "IS_MESA", nullable = true)
    private boolean mesa;
    @Column(name = "IS_INDIVIDUAL", nullable = false)
    private boolean individual;
    @Column(name = "DS_DESCRICAO")
    private String descricao;

    public EventoServico() {
        this.id = -1;
        this.aEvento = new AEvento();
        this.servicos = new Servicos();
        this.mesa = false;
        this.individual = true;
        this.descricao = "";
    }

    public EventoServico(int id, AEvento aEvento, Servicos servicos, boolean isMesa, boolean isIndividual, String descricao) {
        this.id = id;
        this.aEvento = aEvento;
        this.servicos = servicos;
        this.mesa = isMesa;
        this.individual = isIndividual;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AEvento getaEvento() {
        return aEvento;
    }

    public void setaEvento(AEvento aEvento) {
        this.aEvento = aEvento;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public boolean isMesa() {
        return mesa;
    }

    public void setMesa(boolean mesa) {
        this.mesa = mesa;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
