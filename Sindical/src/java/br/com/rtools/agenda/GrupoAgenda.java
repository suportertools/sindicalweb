package br.com.rtools.agenda;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "age_grupo_agenda")
@NamedQueries({
    @NamedQuery(name = "GrupoAgenda.pesquisaID", query = "SELECT GRA FROM GrupoAgenda GRA WHERE GRA.id = :pid"),
    @NamedQuery(name = "GrupoAgenda.findAll", query = "SELECT GRA FROM GrupoAgenda GRA ORDER BY GRA.descricao ASC "),
    @NamedQuery(name = "GrupoAgenda.findName", query = "SELECT GRA FROM GrupoAgenda GRA WHERE UPPER(GRA.descricao) LIKE :pdescricao ORDER BY GRA.descricao ASC ")
})
public class GrupoAgenda implements BaseEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = false, unique = true)
    private String descricao;

    public GrupoAgenda() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoAgenda(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
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
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.id;
        hash = 23 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoAgenda other = (GrupoAgenda) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GrupoAgenda{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
