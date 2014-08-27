package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name = "seg_nivel")
@NamedQueries({
    @NamedQuery(name = "Nivel.pesquisaID", query = "SELECT NIV FROM Nivel NIV WHERE NIV.id = :pid"),
    @NamedQuery(name = "Nivel.findAll", query = "SELECT NIV FROM Nivel NIV ORDER BY NIV.descricao ASC "),
    @NamedQuery(name = "Nivel.findName", query = "SELECT NIV FROM Nivel NIV WHERE UPPER(NIV.descricao) LIKE :pdescricao ORDER BY NIV.descricao ASC ")
})
public class Nivel implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false)
    private String descricao;

    public Nivel() {
        this.id = -1;
        this.descricao = "";
    }

    public Nivel(int id, String descricao) {
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
}
