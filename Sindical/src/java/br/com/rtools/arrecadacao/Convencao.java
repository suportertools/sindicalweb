package br.com.rtools.arrecadacao;

import javax.persistence.*;

@Entity
@Table(name = "ARR_CONVENCAO")
@NamedQuery(name = "Convencao.pesquisaID", query = "select c from Convencao c where c.id=:pid")
public class Convencao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true, unique = true)
    private String descricao;

    public Convencao() {
        this.id = -1;
        this.descricao = "";
    }

    public Convencao(int id, String descricao) {
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
