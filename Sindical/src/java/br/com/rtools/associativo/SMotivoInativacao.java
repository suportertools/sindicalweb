package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "soc_motivo_inativacao")
@NamedQuery(name = "SMotivoInativacao.pesquisaID", query = "select mi from SMotivoInativacao mi where mi.id=:pid")
public class SMotivoInativacao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 150, unique = true)
    private String descricao;

    public SMotivoInativacao() {
        this.id = -1;
        this.descricao = "";
    }

    public SMotivoInativacao(int id, String descricao) {
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
