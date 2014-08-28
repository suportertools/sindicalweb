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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_centro_custo_contabil",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_descricao", "nr_codigo"})
)
@NamedQueries({
    @NamedQuery(name = "CentroCustoContabil.pesquisaID", query = "SELECT CC FROM CentroCustoContabil AS CC WHERE CC.id = :pid"),
    @NamedQuery(name = "CentroCustoContabil.findAll", query = "SELECT CC FROM CentroCustoContabil AS CC ORDER BY CC.descricao ASC, CC.codigo ASC")
})

public class CentroCustoContabil implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_codigo")
    private int codigo;
    @Column(name = "ds_descricao")
    private String descricao;

    public CentroCustoContabil() {
        this.id = -1;
        this.codigo = 0;
        this.descricao = "";
    }

    public CentroCustoContabil(int id, int codigo, String descricao) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
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

    @Override
    public String toString() {
        return "CentroCustoContabil{" + "id=" + id + ", codigo=" + codigo + ", descricao=" + descricao + '}';
    }
}
