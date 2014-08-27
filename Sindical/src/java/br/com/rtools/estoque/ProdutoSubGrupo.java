package br.com.rtools.estoque;

import br.com.rtools.utilitarios.BaseEntity;
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
@Table(name = "est_subgrupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "ProdutoSubGrupo.findAll", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG ORDER BY PSG.produtoGrupo.descricao ASC, PSG.descricao ASC "),
    @NamedQuery(name = "ProdutoSubGrupo.findName", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG WHERE UPPER(PSG.descricao) LIKE :pdescricao ORDER BY PSG.produtoGrupo.descricao ASC, PSG.descricao ASC "),
    @NamedQuery(name = "ProdutoSubGrupo.findGrupo", query = "SELECT PSG FROM ProdutoSubGrupo AS PSG WHERE PSG.produtoGrupo.id = :p1 ORDER BY PSG.produtoGrupo.descricao ASC, PSG.descricao ASC ")
})
public class ProdutoSubGrupo implements BaseEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id", nullable = false)
    @OneToOne
    private ProdutoGrupo produtoGrupo;
    @Column(name = "ds_descricao", length = 100, nullable = false)
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

    @Override
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
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.id;
        hash = 47 * hash + (this.produtoGrupo != null ? this.produtoGrupo.hashCode() : 0);
        hash = 47 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProdutoSubGrupo other = (ProdutoSubGrupo) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.produtoGrupo != other.produtoGrupo && (this.produtoGrupo == null || !this.produtoGrupo.equals(other.produtoGrupo))) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProdutoSubGrupo{" + "id=" + id + ", produtoGrupo=" + produtoGrupo + ", descricao=" + descricao + '}';
    }
}
