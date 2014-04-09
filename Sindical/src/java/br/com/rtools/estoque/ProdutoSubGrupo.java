package br.com.rtools.estoque;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "EST_SUBGRUPO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_GRUPO", "DS_DESCRICAO"})
)
@NamedQueries({
    @NamedQuery(name = "ProdutoSubGrupo.findAll", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG ORDER BY PSG.produtoGrupo.descricao ASC, PSG.descricao ASC "),
    @NamedQuery(name = "ProdutoSubGrupo.findName", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG WHERE UPPER(PSG.descricao) LIKE :pdescricao ORDER BY PSG.produtoGrupo.descricao ASC, PSG.descricao ASC ")
})
public class ProdutoSubGrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private ProdutoGrupo produtoGrupo;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false)
    private String descricao;

    public ProdutoSubGrupo() {
        this.id = -1;
        this.produtoGrupo = new ProdutoGrupo();
        this.descricao = "";
    }

    public ProdutoSubGrupo(int id, ProdutoGrupo produtoGrupo, String descricao) {
        this.produtoGrupo = produtoGrupo;
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProdutoGrupo getProdutoGrupo() {
        return produtoGrupo;
    }

    public void setProdutoGrupo(ProdutoGrupo produtoGrupo) {
        this.produtoGrupo = produtoGrupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "ProdutoSubGrupo{" + "id=" + id + ", descricao=" + descricao + '}';
    }
}
