package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_desconto_empregado")
@NamedQuery(name = "DescontoEmpregado.pesquisaID", query = "select c from DescontoEmpregado c where c.id = :pid")
public class DescontoEmpregado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @Column(name = "nr_percentual", nullable = true)
    private float percentual;
    @Column(name = "ds_ref_inicial", length = 7, nullable = true)
    private String referenciaInicial;
    @Column(name = "ds_ref_final", length = 7, nullable = true)
    private String referenciaFinal;
    @JoinColumn(name = "id_grupo_cidade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoCidade grupoCidade;
    @JoinColumn(name = "id_convencao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Convencao convencao;
    @Column(name = "nr_valor_por_empregado", nullable = false)
    private float valorEmpregado;

    public DescontoEmpregado() {
        this.id = -1;
        this.servicos = new Servicos();
        this.percentual = 0;
        this.referenciaInicial = "";
        this.referenciaFinal = "";
        this.grupoCidade = new GrupoCidade();
        this.convencao = new Convencao();
        this.valorEmpregado = 0;
    }

    public DescontoEmpregado(int id, Servicos servicos, float percentual, String referenciaInicial, String referenciaFinal, GrupoCidade grupoCidade, Convencao convencao, float valorEmpregado) {
        this.id = id;
        this.servicos = servicos;
        this.percentual = percentual;
        this.referenciaInicial = referenciaInicial;
        this.referenciaFinal = referenciaFinal;
        this.grupoCidade = grupoCidade;
        this.convencao = convencao;
        this.valorEmpregado = valorEmpregado;
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

    public float getPercentual() {
        return percentual;
    }

    public void setPercentual(float percentual) {
        this.percentual = percentual;
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }

    public Convencao getConvencao() {
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }

    public float getValorEmpregado() {
        return valorEmpregado;
    }

    public void setValorEmpregado(float valorEmpregado) {
        this.valorEmpregado = valorEmpregado;
    }
}
