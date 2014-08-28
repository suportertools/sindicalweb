package br.com.rtools.financeiro;

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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_conta_operacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_plano5", "id_operacao", "ds_es"})
)
@NamedQuery(name = "ContaOperacao.pesquisaID", query = "SELECT CO FROM ContaOperacao AS CO WHERE CO.id = :pid")
public class ContaOperacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "id_plano5", referencedColumnName = "id")
    @ManyToOne
    private Plano5 plano5;
    @JoinColumn(name = "id_centro_custo_contabil_sub", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private CentroCustoContabilSub centroCustoContabilSub;
    @JoinColumn(name = "id_operacao", referencedColumnName = "id")
    @ManyToOne
    private Operacao operacao;
    @Column(name = "ds_es")
    private String es;
    @Column(name = "is_conta_fixa", columnDefinition = "boolean default false")
    private boolean contaFixa;

    public ContaOperacao() {
        this.id = -1;
        this.plano5 = new Plano5();
        this.centroCustoContabilSub = new CentroCustoContabilSub();
        this.operacao = new Operacao();
        this.es = "";
        this.contaFixa = false;
    }

    public ContaOperacao(int id, Plano5 plano5, CentroCustoContabilSub centroCustoContabilSub, Operacao operacao, String es, boolean contaFixa) {
        this.id = id;
        this.plano5 = plano5;
        this.centroCustoContabilSub = centroCustoContabilSub;
        this.operacao = operacao;
        this.es = es;
        this.contaFixa = contaFixa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public CentroCustoContabilSub getCentroCustoContabilSub() {
        return centroCustoContabilSub;
    }

    public void setCentroCustoContabilSub(CentroCustoContabilSub centroCustoContabilSub) {
        this.centroCustoContabilSub = centroCustoContabilSub;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public boolean isContaFixa() {
        return contaFixa;
    }

    public void setContaFixa(boolean contaFixa) {
        this.contaFixa = contaFixa;
    }

    @Override
    public String toString() {
        return "ContaOperacao{" + "id=" + id + ", plano5.id=" + plano5.getId() + ", centroCustoContabilSub.id=" + centroCustoContabilSub.getId() + ", operacao.id=" + operacao.getId() + ", es=" + es + ", contaFixa=" + contaFixa + '}';
    }

}
