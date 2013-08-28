package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.*;

@Entity
@Table(name="ESC_FINANCEIRO")
@NamedQuery(name="EFinanceiro.pesquisaID", query="select ef from EFinanceiro ef where ef.id=:pid")
public class EFinanceiro implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="NR_MULTA_CANCELAMENTO")
    private float nrMultaCancelamento;
    @JoinColumn(name="ID_MULTA", referencedColumnName="ID")
    @ManyToOne
    private Servicos multa;

    public EFinanceiro() {
        this.id = -1;
        this.nrMultaCancelamento = 0;
        this.multa = new Servicos();
    }

    public EFinanceiro(int id, float nrMultaCancelamento, Servicos multa) {
        this.id = id;
        this.nrMultaCancelamento = nrMultaCancelamento;
        this.multa = multa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNrMultaCancelamento() {
        return nrMultaCancelamento;
    }

    public void setNrMultaCancelamento(float nrMultaCancelamento) {
        this.nrMultaCancelamento = nrMultaCancelamento;
    }

    public Servicos getMulta() {
        return multa;
    }

    public void setMulta(Servicos multa) {
        this.multa = multa;
    }
}
