package br.com.rtools.escola;

import javax.persistence.*;

@Entity
@Table(name = "esc_status")
@NamedQueries({
    @NamedQuery(name = "EscStatus.pesquisaID", query = "SELECT ES FROM EscStatus AS ES WHERE ES.id = :pid"),
    @NamedQuery(name = "EscStatus.findAll", query = "SELECT ES FROM EscStatus AS ES ORDER BY ES.descricao ASC ")
})
public class EscStatus implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", nullable = true)
    private String descricao;

    public EscStatus() {
        id = -1;
        descricao = "";
    }

    public EscStatus(int id, String descricao) {
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
