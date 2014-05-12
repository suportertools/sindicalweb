package br.com.rtools.financeiro;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "FIN_CENTRO_CUSTO_CONTABIL_SUB",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_CENTRO_CUSTO_CONTABIL", "DS_DESCRICAO", "NR_CODIGO"})
)
@NamedQueries({
    @NamedQuery(name = "CentroCustoContabilSub.pesquisaID", query = "SELECT CCCS FROM CentroCustoContabilSub CCCS WHERE CCCS.id = :pid"),
    @NamedQuery(name = "CentroCustoContabilSub.findByCCC", query = "SELECT CCCS FROM CentroCustoContabilSub AS CCCS WHERE CCCS.centroCustoContabil.id IN(:p1) ORDER BY CCCS.centroCustoContabil.descricao ASC, CCCS.centroCustoContabil.codigo ASC, CCCS.descricao ASC, CCCS.codigo ASC")
})
public class CentroCustoContabilSub implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NR_CODIGO")
    private int codigo;
    @Column(name = "DS_DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_CENTRO_CUSTO_CONTABIL", referencedColumnName = "ID")
    @ManyToOne
    private CentroCustoContabil centroCustoContabil;

    public CentroCustoContabilSub() {
        this.id = -1;
        this.codigo = 0;
        this.descricao = "";
        this.centroCustoContabil = new CentroCustoContabil();
    }

    public CentroCustoContabilSub(int id, int codigo, String descricao, CentroCustoContabil centroCustoContabil) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.centroCustoContabil = centroCustoContabil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CentroCustoContabil getCentroCustoContabil() {
        return centroCustoContabil;
    }

    public void setCentroCustoContabil(CentroCustoContabil centroCustoContabil) {
        this.centroCustoContabil = centroCustoContabil;
    }

    @Override
    public String toString() {
        return "CentroCustoContabilSub{" + "id=" + id + ", codigo=" + codigo + ", descricao=" + descricao + ", centroCustoContabil=" + centroCustoContabil + '}';
    }
}
