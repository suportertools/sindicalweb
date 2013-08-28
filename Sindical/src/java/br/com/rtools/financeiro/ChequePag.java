package br.com.rtools.financeiro;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="FIN_CHEQUE_PAG")
@NamedQuery(name="ChequePag.pesquisaID", query="select cp from ChequePag cp where cp.id=:pid")
public class ChequePag implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="DS_CHEQUE", length=50)
    private String cheque;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_EMISSAO")
    private Date dtEmissao;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_VENCIMENTO")
    private Date dtVencimento;

    @JoinColumn(name="ID_PLANO5", referencedColumnName="ID")
    @OneToOne
    private Plano5 plano5;

    @JoinColumn(name="ID_STATUS", referencedColumnName="ID")
    @OneToOne
    private FStatus status;

    public ChequePag() {
        this.id = -1;
        this.cheque = "";
        this.setEmissao(null);
        this.setVencimento(null);
        this.plano5 = new Plano5();
        this.status = new FStatus();
    }

    public ChequePag(int id, String cheque, Date dtEmissao, Date dtVencimento, Plano5 plano5, FStatus status) {
        this.id = id;
        this.cheque = cheque;
        this.dtEmissao = dtEmissao;
        this.dtVencimento = dtVencimento;
        this.plano5 = plano5;
        this.status = status;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getEmissao() {
        return DataHoje.converteData(dtEmissao);
    }

    public void setEmissao(String emissao) {
        this.dtEmissao = DataHoje.converte(emissao);
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getVencimento() {
        return DataHoje.converteData(dtVencimento);
    }

    public void setVencimento(String vencimento) {
        this.dtVencimento = DataHoje.converte(vencimento);
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public FStatus getStatus() {
        return status;
    }

    public void setStatus(FStatus status) {
        this.status = status;
    }
}