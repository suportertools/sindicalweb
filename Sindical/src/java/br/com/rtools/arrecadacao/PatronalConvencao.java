package br.com.rtools.arrecadacao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARR_PATRONAL_CONVENCAO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_CONVENCAO", "ID_GRUPO_CIDADE"})
)
@NamedQueries({
    @NamedQuery(name = "PatronalConvencao.findPorPessoaPatronal", query = "SELECT APC FROM PatronalConvencao AS APC WHERE APC.patronal.pessoa.id = :p1 ORDER BY APC.convencao.descricao ASC, APC.grupoCidade.descricao ASC")
})
public class PatronalConvencao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PATRONAL", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Patronal patronal;
    @JoinColumn(name = "ID_CONVENCAO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Convencao convencao;
    @JoinColumn(name = "ID_GRUPO_CIDADE", referencedColumnName = "ID", nullable = false)
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
