package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name = "SEG_DEPARTAMENTO")
@NamedQueries({
    @NamedQuery(name = "Departamento.pesquisaID", query = "SELECT DEP FROM Departamento DEP WHERE DEP.id = :pid"),
    @NamedQuery(name = "Departamento.findAll", query = "SELECT DEP FROM Departamento DEP ORDER BY DEP.descricao ASC "),
    @NamedQuery(name = "Departamento.findName", query = "SELECT DEP FROM Departamento DEP WHERE UPPER(DEP.descricao) LIKE :pdescricao ORDER BY DEP.descricao ASC ")
})
public class Departamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false)
    private String descricao;

    public Departamento() {
        this.id = -1;
        this.descricao = "";
    }

    public Departamento(int id, String descricao) {
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
        return "Departamento{" + "id=" + id + ", descricao=" + descricao + '}';
    }
}
