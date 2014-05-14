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
@Table(name = "FIN_GRUPO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PLANO5", "DS_DESCRICAO"})
)
@NamedQuery(name = "GrupoFinanceiro.pesquisaID", query = "SELECT GF FROM GrupoFinanceiro AS GF WHERE GF.id = :pid")
public class GrupoFinanceiro implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PLANO5", referencedColumnName = "ID")
    @ManyToOne
    private Plano5 plano5;
    @Column(name = "DS_DESCRICAO", length = 100)
    private String descricao;

    public GrupoFinanceiro() {
        this.id = -1;
        this.plano5 = new Plano5();
        this.descricao = "";
    }

    public GrupoFinanceiro(int id, Plano5 plano5, String descricao) {
        this.id = id;
        this.plano5 = plano5;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "GrupoFinanceiro{" + "id=" + id + ", plano5=" + plano5 + ", descricao=" + descricao + '}';
    }

}
