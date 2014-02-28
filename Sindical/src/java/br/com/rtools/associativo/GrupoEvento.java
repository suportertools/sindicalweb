package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "EVE_GRUPO_EVENTO")
@NamedQueries({
    @NamedQuery(name = "GrupoEvento.pesquisaID", query = "SELECT GE FROM GrupoEvento AS GE WHERE GE.id = :pid"),
    @NamedQuery(name = "GrupoEvento.findAll", query = "SELECT GE FROM GrupoEvento AS GE ORDER BY GE.descricao ASC "),
    @NamedQuery(name = "GrupoEvento.findName", query = "SELECT GE FROM GrupoEvento AS GE WHERE UPPER(GE.descricao) LIKE :pdescricao ORDER BY GE.descricao ASC ")
})
public class GrupoEvento implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true, unique = true)
    private String descricao;

    public GrupoEvento() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoEvento(int id, String descricao) {
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

    @Override
    public String toString() {
        return "GrupoEvento{" + "id=" + id + ", descricao=" + descricao + '}';
    }
}
