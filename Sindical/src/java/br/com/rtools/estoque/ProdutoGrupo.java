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
@Table(name = "est_grupo")
@NamedQueries({
    @NamedQuery(name = "ProdutoGrupo.findAll", query = "SELECT PG FROM ProdutoGrupo AS PG ORDER BY PG.descricao ASC "),
    @NamedQuery(name = "ProdutoGrupo.findName", query = "SELECT PG FROM ProdutoGrupo AS PG WHERE UPPER(PG.descricao) LIKE :pdescricao ORDER BY PG.descricao ASC ")
})
public class ProdutoGrupo implements BaseEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = false, unique = true)
    private String descricao;

    public ProdutoGrupo() {
        this.id = -1;
        this.descricao = "";
    }

    public ProdutoGrupo(int id, String descricao) {
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
        hash = 53 * hash + this.id;
        hash = 53 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
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
        final ProdutoGrupo other = (ProdutoGrupo) obj;
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
        return "ProdutoGrupo{" + "id=" + id + ", descricao=" + descricao + '}';
    }
}
