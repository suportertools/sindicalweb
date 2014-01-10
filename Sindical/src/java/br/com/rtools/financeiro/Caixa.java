package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
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
@Table(name = "FIN_CAIXA")
@NamedQuery(name = "Caixa.pesquisaID", query = "select c from Caixa c where c.id = :pid")
public class Caixa implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NR_CAIXA")
    private int caixa;
    @Column(name = "DS_DESCRICAO", length = 100)
    private String descricao;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID")
    @ManyToOne
    private Filial filial;

    public Caixa() {
        this.id = -1;
        this.caixa = 0;
        this.descricao = "";
        this.filial = new Filial();
    }
    
    public Caixa(int id, int caixa, String descricao, Filial filial) {
        this.id = id;
        this.caixa = caixa;
        this.descricao = descricao;
        this.filial = filial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaixa() {
        return caixa;
    }

    public void setCaixa(int caixa) {
        this.caixa = caixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
