package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_CARTAO_PAG")
@NamedQuery(name="CartaoPag.pesquisaID", query="select cp from CartaoPag cp where cp.id=:pid")
public class CartaoPag implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    public CartaoPag() {
        this.id = -1;
    }

    public CartaoPag(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}