package br.com.rtools.suporte;

import javax.persistence.*;

@Entity
@Table(name = "PRO_STATUS")
public class ProStatus implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 20)
    private String descricao;

    public ProStatus(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public ProStatus() {
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
