package br.com.rtools.sistema;

import javax.persistence.*;

@Entity
@Table(name = "sis_semana")
@NamedQuery(name = "Semana.pesquisaID", query = "SELECT Sem FROM Semana Sem WHERE Sem.id = :pid")
public class Semana implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 15, unique = true)
    private String descricao;

    public Semana() {
        this.id = -1;
        this.descricao = "";
    }

    public Semana(int id, String descricao) {
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
