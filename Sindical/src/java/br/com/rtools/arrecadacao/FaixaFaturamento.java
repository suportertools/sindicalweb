package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_faixa_faturamento")
@NamedQuery(name = "FaixaFaturamento.pesquisaID", query = "select c from FaixaFaturamento c where c.id = :pid")
public class FaixaFaturamento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @Column(name = "nr_min", nullable = true)
    private double min;
    @Column(name = "nr_max", nullable = true)
    private double max;
    @Column(name = "nr_contribuicao", nullable = true)
    private float contribuicao;
    @Column(name = "ds_ref_inicial", length = 7, nullable = true)
    private String referenciaInicial;
    @Column(name = "ds_ref_final", length = 7, nullable = true)
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
