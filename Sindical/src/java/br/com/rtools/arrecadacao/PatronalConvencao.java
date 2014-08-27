package br.com.rtools.arrecadacao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_patronal_convencao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_convencao", "id_grupo_cidade"})
)
@NamedQueries({
    @NamedQuery(name = "PatronalConvencao.findPorPessoaPatronal", query = "SELECT APC FROM PatronalConvencao AS APC WHERE APC.patronal.pessoa.id = :p1 ORDER BY APC.convencao.descricao ASC, APC.grupoCidade.descricao ASC")
})
public class PatronalConvencao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_patronal", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Patronal patronal;
    @JoinColumn(name = "id_convencao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Convencao convencao;
    @JoinColumn(name = "id_grupo_cidade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoCidade grupoCidade;

    public PatronalConvencao() {
        this.id = -1;
        this.patronal = new Patronal();
        this.convencao = new Convencao();
        this.grupoCidade = new GrupoCidade();
    }

    public PatronalConvencao(int id, Patronal patronal, Convencao convencao, GrupoCidade grupoCidade) {
        this.id = id;
        this.patronal = patronal;
        this.convencao = convencao;
        this.grupoCidade = grupoCidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patronal getPatronal() {
        return patronal;
    }

    public void setPatronal(Patronal patronal) {
        this.patronal = patronal;
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
}
