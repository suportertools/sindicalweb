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

@Entity
@Table(name = "FIN_SUBGRUPO")
@NamedQuery(name = "SubGrupoFinanceiro.pesquisaID", query = "select sgf from SubGrupoFinanceiro sgf where sgf.id = :pid")
public class SubGrupoFinanceiro implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID")
    @ManyToOne
    private GrupoFinanceiro grupoFinanceiro;
    @Column(name = "DS_DESCRICAO", length = 100)
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
}
