package br.com.rtools.homologacao;

import javax.persistence.*;

@Entity
@Table(name = "HOM_STATUS")
@NamedQuery(name = "Status.pesquisaID", query = "select s from Status s where s.id=:pid")
public class Status implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false, unique = true)
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
