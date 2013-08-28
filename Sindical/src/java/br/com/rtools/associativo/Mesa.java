package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="EVE_MESA")
@NamedQuery(name="Mesa.pesquisaID", query="select s from Mesa s where s.id=:pid")
public class Mesa implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_VENDA", referencedColumnName="ID",  nullable=true)
    @OneToOne
    private BVenda bVenda;
    @JoinColumn(name="ID_STATUS", referencedColumnName="ID",  nullable=true)
    @OneToOne
    private AStatus status;
    @Column(name="NR_MESAS", nullable=true)
    private int nrMesa;

    public Mesa() {
        this.id = -1;
        this.bVenda = new BVenda();
        this.status = new AStatus();
        this.nrMesa = 0;
    }

    public Mesa(int id, BVenda bVenda, AStatus status, int nrMesa) {
        this.id = id;
        this.bVenda = bVenda;
        this.status = status;
        this.nrMesa = nrMesa;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BVenda getbVenda() {
        return bVenda;
    }

    public void setbVenda(BVenda bVenda) {
        this.bVenda = bVenda;
    }

    public AStatus getStatus() {
        return status;
    }

    public void setStatus(AStatus status) {
        this.status = status;
    }

    public int getNrMesa() {
        return nrMesa;
    }

    public void setNrMesa(int nrMesa) {
        this.nrMesa = nrMesa;
    }       
}