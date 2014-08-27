package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_escolaridade")
@NamedQueries({
    @NamedQuery(name = "Escolaridade.pesquisaID", query = "SELECT E FROM Escolaridade AS E WHERE E.id = :pid"),
    @NamedQuery(name = "Escolaridade.findAll", query = "SELECT E FROM Escolaridade AS E ORDER BY E.descricao ASC "),
    @NamedQuery(name = "Escolaridade.findName", query = "SELECT E FROM Escolaridade AS E WHERE UPPER(E.descricao) LIKE :pdescricao ORDER BY E.descricao ASC ")
})
public class Escolaridade implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 255, nullable = false, unique = true)
    private String descricao;

    public Escolaridade() {
        this.id = -1;
        this.descricao = "";
    }

    public Escolaridade(int id, String descricao) {
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
        int hash = 3;
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
        final Escolaridade other = (Escolaridade) obj;
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
        return "Escolaridade{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
