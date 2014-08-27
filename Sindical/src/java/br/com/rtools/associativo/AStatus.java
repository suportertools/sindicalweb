package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "eve_status")
@NamedQuery(name = "AStatus.pesquisaID", query = "select s from AStatus s where s.id=:pid")
public class AStatus implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", nullable = true, unique = true)
    private String descricao;

    public AStatus() {
        this.id = -1;
        this.descricao = "";
    }

    public AStatus(int id, String descricao) {
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