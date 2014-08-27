package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_indicador_alvara")
@NamedQueries({
    @NamedQuery(name = "IndicadorAlvara.pesquisaID", query = "SELECT IA FROM IndicadorAlvara AS IA WHERE IA.id = :pid"),
    @NamedQuery(name = "IndicadorAlvara.findAll", query = "SELECT IA FROM IndicadorAlvara AS IA ORDER BY IA.descricao ASC "),
    @NamedQuery(name = "IndicadorAlvara.findName", query = "SELECT IA FROM IndicadorAlvara AS IA WHERE UPPER(IA.descricao) LIKE :pdescricao ORDER BY IA.descricao ASC ")
})
public class IndicadorAlvara implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 150, nullable = false, unique = true)
    private String descricao;

    public IndicadorAlvara() {
        this.id = -1;
        this.descricao = "";
    }

    public IndicadorAlvara(int id, String descricao) {
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
        hash = 97 * hash + this.id;
        hash = 97 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
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
        final IndicadorAlvara other = (IndicadorAlvara) obj;
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
        return "IndicadorAlvara{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
