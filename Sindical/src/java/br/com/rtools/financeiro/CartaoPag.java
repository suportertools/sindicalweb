package br.com.rtools.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_cartao_pag")
@NamedQuery(name = "CartaoPag.pesquisaID", query = "select cp from CartaoPag cp where cp.id=:pid")
public class CartaoPag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
