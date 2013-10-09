package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "ARR_PATRONAL")
@NamedQuery(name = "Patronal.pesquisaID", query = "select p from Patronal p where p.id = :pid")
public class Patronal implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "ID_CONVENCAO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Convencao convencao;
    @JoinColumn(name = "ID_GRUPO_CIDADE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private GrupoCidade grupoCidade;
    @Column(name = "DS_BASE_TERRITORIAL", length = 2000, nullable = true)
    private String baseTerritorial;

    public Patronal() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.convencao = new Convencao();
        this.grupoCidade = new GrupoCidade();
        this.baseTerritorial = "";
    }

    public Patronal(int id, Pessoa pessoa, Convencao convencao, GrupoCidade grupoCidade, String baseTerritorial) {
        this.id = id;
        this.pessoa = pessoa;
        this.convencao = convencao;
        this.grupoCidade = grupoCidade;
        this.baseTerritorial = baseTerritorial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
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

    public String getBaseTerritorial() {
        return baseTerritorial;
    }

    public void setBaseTerritorial(String baseTerritorial) {
        this.baseTerritorial = baseTerritorial;
    }
}