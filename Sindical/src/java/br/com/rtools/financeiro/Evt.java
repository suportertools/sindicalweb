package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_EVT")
@NamedQuery(name = "Evt.pesquisaID", query = "select e from Evt e where e.id=:pid")
public class Evt implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", nullable = true)
    private String descricao;

    public Evt() {
        this.id = -1;
        this.descricao = "";
    }

    public Evt(int id) {
        this.id = id;
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