package br.com.rtools.associativo;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.pessoa.Pessoa;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Table(name = "car_venda")
@NamedQuery(name = "CVenda.pesquisaID", query = "select cv from CVenda cv where cv.id=:pid")
public class CVenda implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id")
    @OneToOne
    private Pessoa responsavel;
    @JoinColumn(name = "id_aevento", referencedColumnName = "id")
    @ManyToOne
    private AEvento aEvento;
    @JoinColumn(name = "id_evt", referencedColumnName = "id")
    @ManyToOne
    private Evt evt;
    @Column(name = "nr_quarto")
    private int quarto;
    @Column(name = "ds_observacao")
    private String observacao;

    public CVenda(int id, Pessoa responsavel, AEvento aEvento, Evt evt, int quarto, String observacao) {
        this.id = id;
        this.responsavel = responsavel;
        this.aEvento = aEvento;
        this.evt = evt;
        this.quarto = quarto;
        this.observacao = observacao;
    }

    public CVenda() {
        this.id = -1;
        this.responsavel = new Pessoa();
        this.aEvento = new AEvento();
        this.evt = new Evt();
        this.quarto = 0;
        this.observacao = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public AEvento getaEvento() {
        return aEvento;
    }

    public void setaEvento(AEvento aEvento) {
        this.aEvento = aEvento;
    }

    public Evt getEvt() {
        return evt;
    }

    public void setEvt(Evt evt) {
        this.evt = evt;
    }

    public int getQuarto() {
        return quarto;
    }

    public void setQuarto(int quarto) {
        this.quarto = quarto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
