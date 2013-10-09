package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name = "SEG_EVENTO")
@NamedQuery(name = "Evento.pesquisaID", query = "select eve from Evento eve where eve.id=:pid")
public class Evento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false)
    private String descricao;

    public Evento() {
        this.id = -1;
        this.descricao = "";
    }

    public Evento(int id, String descricao) {
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