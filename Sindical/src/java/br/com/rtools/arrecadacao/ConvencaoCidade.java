package br.com.rtools.arrecadacao;

import javax.persistence.*;

@Entity
@Table(name = "ARR_CONVENCAO_CIDADE")
@NamedQuery(name = "ConvencaoCidade.pesquisaID", query = "select c from ConvencaoCidade c where c.id=:pid")
public class ConvencaoCidade implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_GRUPO_CIDADE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private GrupoCidade grupoCidade;
    @JoinColumn(name = "ID_CONVENCAO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Convencao convencao;
    @Column(name = "DS_CAMINHO", length = 100)
    private String caminho;

    public ConvencaoCidade() {
        this.id = -1;
        this.grupoCidade = new GrupoCidade();
        this.convencao = new Convencao();
        this.caminho = "";
    }

    public ConvencaoCidade(int id, GrupoCidade grupoCidade, Convencao convencao, String caminho) {
        this.id = id;
        this.grupoCidade = grupoCidade;
        this.convencao = convencao;
        this.caminho = caminho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
