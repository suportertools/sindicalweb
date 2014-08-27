package br.com.rtools.homologacao;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "hom_status")
@NamedQueries({
    @NamedQuery(name = "Status.pesquisaID", query = "SELECT S FROM Status AS S WHERE S.id = :pid"),
    @NamedQuery(name = "Status.findAll",    query = "SELECT S FROM Status AS S ORDER BY S.descricao ASC "),
    @NamedQuery(name = "Status.findName",   query = "SELECT S FROM Status AS S WHERE UPPER(S.descricao) LIKE :pdescricao ORDER BY S.descricao ASC ")
})
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public Status() {
        this.id = -1;
        this.descricao = "";
    }

    public Status(int id, String descricao) {
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
        try {
            if (descricao.isEmpty()) {
                throw new Exception("OI");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
