package br.com.rtools.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_grupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_plano5", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "GrupoFinanceiro.pesquisaID", query = "SELECT GF FROM GrupoFinanceiro AS GF WHERE GF.id = :pid"),
    @NamedQuery(name = "GrupoFinanceiro.findAll", query = "SELECT GF FROM GrupoFinanceiro AS GF ORDER BY GF.descricao ASC ")
})
public class GrupoFinanceiro implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100)
    private String descricao;

    public GrupoFinanceiro() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoFinanceiro(int id, String descricao) {
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

    @Override
    public String toString() {
        return "GrupoFinanceiro{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
