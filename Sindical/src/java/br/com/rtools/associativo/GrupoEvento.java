package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "EVE_GRUPO_EVENTO")
@NamedQuery(name = "GrupoEvento.pesquisaID", query = "select ge from GrupoEvento ge where ge.id=:pid")
public class GrupoEvento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true, unique = true)
    private String descricao;

    public GrupoEvento() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoEvento(int id, String descricao) {
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
