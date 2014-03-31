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
@Table(name = "FIN_PRODUTO_SUBGRUPO")
@NamedQueries({
    @NamedQuery(name = "ProdutoSubGrupo.findAll", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG ORDER BY PSG.descricao ASC "),
    @NamedQuery(name = "ProdutoSubGrupo.findName", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG WHERE UPPER(PSG.descricao) LIKE :pdescricao ORDER BY PSG.descricao ASC ")
})
public class ProdutoSubGrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false, unique = true)
    private String descricao;

    public ProdutoSubGrupo() {
        this.id = -1;
        this.descricao = "";
    }

    public ProdutoSubGrupo(int id, String descricao) {
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
