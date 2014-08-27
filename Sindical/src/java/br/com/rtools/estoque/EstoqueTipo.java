package br.com.rtools.estoque;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "est_tipo")
@NamedQueries({
    @NamedQuery(name = "EstoqueTipo.findAll", query = "SELECT ET FROM EstoqueTipo AS ET ORDER BY ET.descricao ASC "),
    @NamedQuery(name = "EstoqueTipo.findName", query = "SELECT ET FROM EstoqueTipo AS ET WHERE UPPER(ET.descricao) LIKE :pdescricao ORDER BY ET.descricao ASC ")
})
public class EstoqueTipo implements BaseEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = false, unique = true)
    private String descricao;

    public EstoqueTipo() {
        this.id = -1;
        this.descricao = "";
    }

    public EstoqueTipo(int id, String descricao) {
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
    public String toString() {
        return "EstoqueTipo{" + "id=" + id + ", descricao=" + descricao + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
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
        final EstoqueTipo other = (EstoqueTipo) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

}
