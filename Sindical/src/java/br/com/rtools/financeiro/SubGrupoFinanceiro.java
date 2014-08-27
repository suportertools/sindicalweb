package br.com.rtools.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_subgrupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo", "ds_descricao"})
)
@NamedQuery(name = "SubGrupoFinanceiro.pesquisaID", query = "SELECT SGF FROM SubGrupoFinanceiro AS SGF WHERE SGF.id = :pid")
public class SubGrupoFinanceiro implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    @ManyToOne
    private GrupoFinanceiro grupoFinanceiro;
    @Column(name = "ds_descricao", length = 100)
    private String descricao;

    public SubGrupoFinanceiro() {
        this.id = -1;
        this.grupoFinanceiro = new GrupoFinanceiro();
        this.descricao = "";
    }

    public SubGrupoFinanceiro(int id, GrupoFinanceiro grupoFinanceiro, String descricao) {
        this.id = id;
        this.grupoFinanceiro = grupoFinanceiro;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GrupoFinanceiro getGrupoFinanceiro() {
        return grupoFinanceiro;
    }

    public void setGrupoFinanceiro(GrupoFinanceiro grupoFinanceiro) {
        this.grupoFinanceiro = grupoFinanceiro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "SubGrupoFinanceiro{" + "id=" + id + ", grupoFinanceiro=" + grupoFinanceiro + ", descricao=" + descricao + '}';
    }

}
