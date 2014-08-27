package br.com.rtools.arrecadacao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_motivo_inativacao")
@NamedQuery(name = "MotivoInativacao.pesquisaID", query = "select m from MotivoInativacao m where m.id=:pid")
public class MotivoInativacao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = true, unique = true)
    private String descricao;

    public MotivoInativacao() {
        this.id = -1;
        this.descricao = "";
    }

    public MotivoInativacao(int id, String descricao) {
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
