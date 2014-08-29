package br.com.rtools.arrecadacao;

import br.com.rtools.endereco.Cidade;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_grupo_cidades")
@NamedQueries({
    @NamedQuery(name = "GrupoCidades.pesquisaID", query = "SELECT GCS FROM GrupoCidades AS GCS WHERE GCS.id = :pid"),
    @NamedQuery(name = "GrupoCidades.findAll", query = "SELECT GCS FROM GrupoCidades AS GCS ORDER BY GCS.grupoCidade.descricao ASC, GCS.cidade.cidade ASC, GCS.cidade.uf ASC "),
    @NamedQuery(name = "GrupoCidades.findName", query = "SELECT GCS FROM GrupoCidades AS GCS WHERE UPPER(GCS.grupoCidade.descricao) LIKE :pdescricao ORDER BY GCS.grupoCidade.descricao ASC, GCS.cidade.cidade ASC, GCS.cidade.uf ASC ")
})
public class GrupoCidades implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cidade cidade;
    @JoinColumn(name = "id_grupo_cidade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoCidade grupoCidade;

    public GrupoCidades() {
        this.id = -1;
        this.cidade = new Cidade();
        this.grupoCidade = new GrupoCidade();
    }

    public GrupoCidades(int id, Cidade cidade, GrupoCidade grupoCidade) {
        this.id = id;
        this.cidade = cidade;
        this.grupoCidade = grupoCidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }
}
