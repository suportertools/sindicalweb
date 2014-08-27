package br.com.rtools.sistema;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_periodo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_descricao", "nr_dias"})
)
@NamedQueries({
    @NamedQuery(name = "Periodo.pesquisaID", query = "SELECT P FROM Periodo AS P WHERE P.id = :pid"),
    @NamedQuery(name = "Periodo.findAll", query = "SELECT P FROM Periodo AS P ORDER BY P.dias ASC")
})
public class Periodo implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20, unique = true)
    private String descricao;
    @Column(name = "nr_dias")
    private int dias;

    public Periodo() {
        this.id = -1;
        this.descricao = "";
        this.dias = 0;
    }

    public Periodo(int id, String descricao, int dias) {
        this.id = id;
        this.descricao = descricao;
        this.dias = dias;
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

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        hash = 17 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
        hash = 17 * hash + this.dias;
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
        final Periodo other = (Periodo) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        if (this.dias != other.dias) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Periodo{" + "id=" + id + ", descricao=" + descricao + ", dias=" + dias + '}';
    }

}
