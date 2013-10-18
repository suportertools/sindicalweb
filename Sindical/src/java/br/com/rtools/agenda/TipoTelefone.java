package br.com.rtools.agenda;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AGE_TIPO_TELEFONE")
@NamedQueries({
    @NamedQuery(name = "TipoTelefone.pesquisaID", query = "SELECT ATT FROM TipoTelefone AS ATT WHERE ATT.id = :pid"),
    @NamedQuery(name = "TipoTelefone.findAll",    query = "SELECT ATT FROM TipoTelefone AS ATT ORDER BY ATT.descricao ASC "),
    @NamedQuery(name = "TipoTelefone.findName",   query = "SELECT ATT FROM TipoTelefone AS ATT WHERE UPPER(ATT.descricao) LIKE :pdescricao ORDER BY ATT.descricao ASC ")
})
public class TipoTelefone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, unique = true)
    private String descricao;

    public TipoTelefone() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoTelefone(int id, String descricao) {
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
