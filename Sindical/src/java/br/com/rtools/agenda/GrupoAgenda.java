package br.com.rtools.agenda;

import javax.persistence.*;

@Entity
@Table(name = "AGE_GRUPO_AGENDA")
@NamedQuery(name = "GrupoAgenda.pesquisaID", query = "select a from GrupoAgenda a where a.id=:pid")
public class GrupoAgenda implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false, unique = true)
    private String descricao;

    public GrupoAgenda() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoAgenda(int id, String descricao) {
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
