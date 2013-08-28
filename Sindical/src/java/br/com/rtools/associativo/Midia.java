package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name="SOC_MIDIA")
@NamedQuery(name="Midia.pesquisaID", query="select m from Midia m where m.id=:pid")
public class Midia implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", nullable = true, unique = true)
    private String descricao;

    public Midia(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Midia() {
        this.id = -1;
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
