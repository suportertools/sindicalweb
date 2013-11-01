package br.com.rtools.agenda;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AGE_GRUPO_AGENDA")
@NamedQueries({
    @NamedQuery(name = "GrupoAgenda.pesquisaID",    query = "SELECT GRA FROM GrupoAgenda GRA WHERE GRA.id = :pid"),
    @NamedQuery(name = "GrupoAgenda.findAll",       query = "SELECT GRA FROM GrupoAgenda GRA ORDER BY GRA.descricao ASC "),
    @NamedQuery(name = "GrupoAgenda.findName",      query = "SELECT GRA FROM GrupoAgenda GRA WHERE UPPER(GRA.descricao) LIKE :pdescricao ORDER BY GRA.descricao ASC ")
})
public class GrupoAgenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false, unique = true)
    private String descricao;

    public GrupoAgenda() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoAgenda(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
