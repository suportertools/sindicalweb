package br.com.rtools.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "FIN_CENTRO_CUSTO_CONTABIL")
@NamedQuery(name = "CentroCustoContabil.pesquisaID", query = "select cc from CentroCustoContabil cc where cc.id = :pid")
public class CentroCustoContabil implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NR_CODIGO")
    private int codigo;
    @Column(name = "DS_DESCRICAO")
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
}
