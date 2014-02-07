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
@Table(name = "FIN_CARTAO")
@NamedQuery(name = "Cartao.pesquisaID", query = "select c from Cartao c where c.id = :pid")
public class Cartao implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO")
    private String descricao;
    @Column(name = "NR_DIAS")
    private int dias;    
    @Column(name = "NR_TAXA")
    private float taxa;    
    @Column(name = "DS_DEBITO_CREDITO")
    private String debitoCredito;
    @JoinColumn(name = "ID_PLANO5", referencedColumnName = "ID")
    @ManyToOne
    private Plano5 plano5;    

    public Cartao() {
        this.id = -1;
        this.descricao = "";
        this.dias = 0;
        this.taxa = 0;
        this.debitoCredito = "";
        this.plano5 = new Plano5();
    }
    
    public Cartao(int id, String descricao, int dias, float taxa, String debitoCredito, Plano5 plano5) {
        this.id = id;
        this.descricao = descricao;
        this.dias = dias;
        this.taxa = taxa;
        this.debitoCredito = debitoCredito;
        this.plano5 = plano5;
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

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public float getTaxa() {
        return taxa;
    }

    public void setTaxa(float taxa) {
        this.taxa = taxa;
    }

    public String getDebitoCredito() {
        return debitoCredito;
    }

    public void setDebitoCredito(String debitoCredito) {
        this.debitoCredito = debitoCredito;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }
    
}
