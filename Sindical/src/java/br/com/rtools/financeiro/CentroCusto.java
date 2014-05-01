package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import javax.persistence.*;

@Entity
@Table(name = "FIN_CENTRO_CUSTO")
@NamedQuery(name = "CentroCusto.pesquisaID", query = "select cc from CentroCusto cc where cc.id = :pid")
public class CentroCusto implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_CENTRO_CUSTO_CONTABIL", referencedColumnName = "ID")
    @ManyToOne
    private CentroCustoContabil centroCustoContabil;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID")
    @ManyToOne
    private Filial filial;

    public CentroCusto() {
        this.id = -1;
        this.descricao = "";
        this.centroCustoContabil = new CentroCustoContabil();
        this.filial = new Filial();
    }
    
    public CentroCusto(int id, String descricao, CentroCustoContabil centroCustoContabil, Filial filial) {
        this.id = id;
        this.descricao = descricao;
        this.centroCustoContabil = centroCustoContabil;
        this.filial = filial;
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

    public CentroCustoContabil getCentroCustoContabil() {
        return centroCustoContabil;
    }

    public void setCentroCustoContabil(CentroCustoContabil centroCustoContabil) {
        this.centroCustoContabil = centroCustoContabil;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
    
    
}
