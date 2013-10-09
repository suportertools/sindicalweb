package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.*;

@Entity
@Table(name = "ARR_FAIXA_FATURAMENTO")
@NamedQuery(name = "FaixaFaturamento.pesquisaID", query = "select c from FaixaFaturamento c where c.id = :pid")
public class FaixaFaturamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_SERVICOS", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @Column(name = "NR_MIN", nullable = true)
    private double min;
    @Column(name = "NR_MAX", nullable = true)
    private double max;
    @Column(name = "NR_CONTRIBUICAO", nullable = true)
    private float contribuicao;
    @Column(name = "DS_REF_INICIAL", length = 7, nullable = true)
    private String referenciaInicial;
    @Column(name = "DS_REF_FINAL", length = 7, nullable = true)
    private String referenciaFinal;

    public FaixaFaturamento() {
        this.id = -1;
        this.servicos = new Servicos();
        this.min = 0;
        this.max = 0;
        this.contribuicao = 0;
        this.referenciaInicial = "";
        this.referenciaFinal = "";
    }

    public FaixaFaturamento(int id, Servicos servicos, double min, double max, float contribuicao, String referenciaInicial, String referenciaFinal) {
        this.id = id;
        this.servicos = servicos;
        this.min = min;
        this.max = max;
        this.contribuicao = contribuicao;
        this.referenciaInicial = referenciaInicial;
        this.referenciaFinal = referenciaFinal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public float getContribuicao() {
        return contribuicao;
    }

    public void setContribuicao(float contribuicao) {
        this.contribuicao = contribuicao;
    }

    public String getReferenciaInicial() {
        return referenciaInicial;
    }

    public void setReferenciaInicial(String referenciaInicial) {
        this.referenciaInicial = referenciaInicial;
    }

    public String getReferenciaFinal() {
        return referenciaFinal;
    }

    public void setReferenciaFinal(String referenciaFinal) {
        this.referenciaFinal = referenciaFinal;
    }
}