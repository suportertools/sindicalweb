package br.com.rtools.arrecadacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "arr_convencao_periodo")
@NamedQuery(name = "ConvencaoPeriodo.pesquisaID", query = "select convp from ConvencaoPeriodo convp where convp.id=:pid")
public class ConvencaoPeriodo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_convencao", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Convencao convencao;
    @JoinColumn(name = "id_grupo_cidade", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private GrupoCidade grupoCidade;
    @Column(name = "ds_referencia_inicial", length = 7)
    private String referenciaInicial;
    @Column(name = "ds_referencia_final", length = 7)
    private String referenciaFinal;

    public ConvencaoPeriodo(int id, Convencao convencao, GrupoCidade grupoCidade, String referenciaInicial, String referenciaFinal) {
        this.id = id;
        this.convencao = convencao;
        this.grupoCidade = grupoCidade;
        this.referenciaInicial = referenciaInicial;
        this.referenciaFinal = referenciaFinal;
    }

    public ConvencaoPeriodo() {
        this.id = -1;
        this.convencao = new Convencao();
        this.grupoCidade = new GrupoCidade();
        this.referenciaInicial = "";
        this.referenciaFinal = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Convencao getConvencao() {
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
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
