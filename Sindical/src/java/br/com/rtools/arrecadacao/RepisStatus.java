package br.com.rtools.arrecadacao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARR_REPIS_STATUS")
@NamedQuery(name = "RepisStatus.pesquisaID", query = "select r from RepisStatus r where r.id=:pid")
public class RepisStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = true, unique = true)
    private String descricao;

    public RepisStatus() {
        this.id = -1;
        this.descricao = "";
    }

    public RepisStatus(int id, String descricao) {
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
