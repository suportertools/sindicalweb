package br.com.rtools.homologacao;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "hom_demissao")
@NamedQueries({
    @NamedQuery(name = "Demissao.pesquisaID", query = "SELECT D FROM Demissao AS D WHERE D.id = :pid"),
    @NamedQuery(name = "Demissao.findAll", query = "SELECT D FROM Demissao AS D ORDER BY D.descricao ASC "),
    @NamedQuery(name = "Demissao.findName", query = "SELECT D FROM Demissao AS D WHERE UPPER(D.descricao) LIKE :pdescricao ORDER BY D.descricao ASC ")
})
public class Demissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false)
    private String descricao;

    public Demissao() {
        this.id = -1;
        this.descricao = "";
    }

    public Demissao(int id, String descricao) {
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
