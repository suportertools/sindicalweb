package br.com.rtools.financeiro;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "FIN_CARTAO_REC")
@NamedQuery(name = "CartaoRec.pesquisaID", query = "select cr from CartaoRec cr where cr.id = :pid")
public class CartaoRec implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_STATUS", referencedColumnName = "ID")
    @ManyToOne
    private FStatus status;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_LIQUIDACAO")
    private Date dtLiquidacao;

    public CartaoRec(int id, FStatus status, Date dtLiquidacao) {
        this.id = id;
        this.status = status;
        this.dtLiquidacao = dtLiquidacao;
    }
    
    public CartaoRec() {
        this.id = -1;
    }

    public CartaoRec(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FStatus getStatus() {
        return status;
    }

    public void setStatus(FStatus status) {
        this.status = status;
    }

    public Date getDtLiquidacao() {
        return dtLiquidacao;
    }

    public void setDtLiquidacao(Date dtLiquidacao) {
        this.dtLiquidacao = dtLiquidacao;
    }
}