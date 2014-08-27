package br.com.rtools.arrecadacao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_grupo_cidade")
@NamedQueries({
    @NamedQuery(name = "GrupoCidade.pesquisaID", query = "SELECT C FROM GrupoCidade AS C WHERE C.id = :pid"),
    @NamedQuery(name = "GrupoCidade.findAll", query = "SELECT C FROM GrupoCidade AS C ORDER BY C.descricao ASC "),
    @NamedQuery(name = "GrupoCidade.findName", query = "SELECT C FROM GrupoCidade AS C WHERE UPPER(C.descricao) LIKE :pdescricao ORDER BY C.descricao ASC ")
})
public class GrupoCidade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = true, unique = true)
    private String descricao;

    public GrupoCidade() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoCidade(int id, String descricao) {
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
