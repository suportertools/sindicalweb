
package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.*;

@Entity
@Table(name="ARR_DESCONTO_EMPREGADO")
@NamedQuery(name="DescontoEmpregado.pesquisaID", query="select c from DescontoEmpregado c where c.id = :pid")
public class DescontoEmpregado implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_SERVICOS", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Servicos servicos;
    @Column(name="NR_PERCENTUAL", nullable=true)
    private float percentual;
    @Column(name="DS_REF_INICIAL", length=7 , nullable=true)
    private String referenciaInicial;
    @Column(name="DS_REF_FINAL",  length=7 , nullable=true)
    private String referenciaFinal;
    @JoinColumn(name="ID_GRUPO_CIDADE", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private GrupoCidade grupoCidade;
    @JoinColumn(name="ID_CONVENCAO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Convencao convencao;
    @Column(name="NR_VALOR_POR_EMPREGADO", nullable=false)
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