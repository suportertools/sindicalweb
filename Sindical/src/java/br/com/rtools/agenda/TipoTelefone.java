package br.com.rtools.agenda;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "age_tipo_telefone")
@NamedQueries({
    @NamedQuery(name = "TipoTelefone.pesquisaID", query = "SELECT ATT FROM TipoTelefone AS ATT WHERE ATT.id = :pid"),
    @NamedQuery(name = "TipoTelefone.findAll", query = "SELECT ATT FROM TipoTelefone AS ATT ORDER BY ATT.descricao ASC "),
    @NamedQuery(name = "TipoTelefone.findName", query = "SELECT ATT FROM TipoTelefone AS ATT WHERE UPPER(ATT.descricao) LIKE :pdescricao ORDER BY ATT.descricao ASC ")
})
public class TipoTelefone implements BaseEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, unique = true)
    private String descricao;

    public TipoTelefone() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoTelefone(int id, String descricao) {
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
        int hash = 5;
        hash = 61 * hash + this.id;
        hash = 61 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
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
        final TipoTelefone other = (TipoTelefone) obj;
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
        return "TipoTelefone{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
