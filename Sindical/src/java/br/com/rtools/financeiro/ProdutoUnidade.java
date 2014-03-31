package br.com.rtools.financeiro;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "FIN_PRODUTO_UNIDADE")
@NamedQueries({
    @NamedQuery(name = "ProdutoUnidade.findAll", query = "SELECT PU FROM ProdutoUnidade AS PU ORDER BY PU.descricao ASC "),
    @NamedQuery(name = "ProdutoUnidade.findName", query = "SELECT PU FROM ProdutoUnidade AS PU WHERE UPPER(PU.descricao) LIKE :pdescricao ORDER BY PU.descricao ASC ")
})
public class ProdutoUnidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false, unique = true)
    private String descricao;

    public ProdutoUnidade() {
        this.id = -1;
        this.descricao = "";
    }

    public ProdutoUnidade(int id, String descricao) {
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
