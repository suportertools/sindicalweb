package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "esc_financeiro")
@NamedQuery(name = "EFinanceiro.pesquisaID", query = "SELECT EF FROM EFinanceiro AS EF WHERE EF.id=:pid")
public class EFinanceiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_multa_cancelamento")
    private float nrMultaCancelamento;
    @JoinColumn(name = "id_multa", referencedColumnName = "id")
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
