package br.com.rtools.estoque;

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
@Table(name = "EST_TIPO")
@NamedQueries({
    @NamedQuery(name = "EstoqueTipo.findAll", query = "SELECT ET FROM EstoqueTipo AS ET ORDER BY ET.descricao ASC "),
    @NamedQuery(name = "EstoqueTipo.findName", query = "SELECT ET FROM EstoqueTipo AS ET WHERE UPPER(ET.descricao) LIKE :pdescricao ORDER BY ET.descricao ASC ")
})
public class EstoqueTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false, unique = true)
    private String descricao;

    public EstoqueTipo() {
        this.id = -1;
        this.descricao = "";
    }

    public EstoqueTipo(int id, String descricao) {
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
        return "EstoqueTipo{" + "id=" + id + ", descricao=" + descricao + '}';
    }
}
