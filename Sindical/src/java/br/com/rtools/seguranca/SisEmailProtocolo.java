package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_email_protocolo")
@NamedQueries({
    @NamedQuery(name = "SisEmailProtocolo.findAll", query = "SELECT SEP FROM SisEmailProtocolo SEP ORDER BY SEP.descricao ASC "),
    @NamedQuery(name = "SisEmailProtocolo.findName", query = "SELECT SEP FROM SisEmailProtocolo SEP WHERE UPPER(SEP.descricao) LIKE :pdescricao ORDER BY SEP.descricao ASC ")
})
public class SisEmailProtocolo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public SisEmailProtocolo() {
        this.id = -1;
        this.descricao = "";
    }

    public SisEmailProtocolo(int id, String descricao) {
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
