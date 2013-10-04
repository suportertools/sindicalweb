package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_STATUS")
@NamedQuery(name = "FStatus.pesquisaID", query = "select s from FStatus s where s.id=:pid")
public class FStatus implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, unique = true, nullable = false)
    private String descricao;

    public FStatus() {
        this.id = -1;
        this.descricao = "";
    }

    public FStatus(int id, String descricao) {
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