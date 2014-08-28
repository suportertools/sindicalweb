package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "soc_midia")
@NamedQueries({
    @NamedQuery(name = "Midia.pesquisaID", query = "SELECT m FROM Midia AS m WHERE m.id = :pid"),
    @NamedQuery(name = "Midia.findAll", query = "SELECT m FROM Midia AS m ORDER BY m.descricao ASC "),
    @NamedQuery(name = "Midia.findName", query = "SELECT m FROM Midia AS m WHERE UPPER(m.descricao) LIKE :pdescricao ORDER BY m.descricao ASC ")
})
public class Midia implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", nullable = true, unique = true)
    private String descricao;

    public Midia(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Midia() {
        this.id = -1;
        this.descricao = "";
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
