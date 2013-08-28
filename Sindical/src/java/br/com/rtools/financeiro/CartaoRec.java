package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_CARTAO_REC")
@NamedQuery(name="CartaoRec.pesquisaID", query="select cr from CartaoRec cr where cr.id=:pid")
public class CartaoRec implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    public CartaoRec() {
        this.id = -1;
    }

    public CartaoRec(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}