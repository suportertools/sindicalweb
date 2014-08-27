package br.com.rtools.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_tipo_remuneracao")
public class TipoRemuneracao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20, unique = true)
    private String descricao;

    public TipoRemuneracao() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoRemuneracao(int id, String descricao) {
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
