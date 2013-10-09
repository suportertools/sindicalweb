package br.com.rtools.homologacao;

import javax.persistence.*;

@Entity
@Table(name = "HOM_DEMISSAO")
@NamedQuery(name = "Demissao.pesquisaID", query = "select d from Demissao d where d.id=:pid")
public class Demissao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false)
    private String descricao;

    public Demissao() {
        this.id = -1;
        this.descricao = "";
    }

    public Demissao(int id, String descricao) {
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
