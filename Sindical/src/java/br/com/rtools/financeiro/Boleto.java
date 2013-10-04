package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_BOLETO")
@NamedQuery(name="Boleto.pesquisaID", query="select b from Boleto b where b.id=:pid")
public class Boleto implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name="ID_CONTA_COBRANCA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private ContaCobranca contaCobranca;

    @Column(name="NR_BOLETO")
    private long nrBoleto;

    @Column(name="DS_BOLETO", length=50)
    private String boletoComposto;

    @Column(name="NR_CTR_BOLETO", length=20)
    private String nrCtrBoleto;
    
    @Column(name="IS_ATIVO", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;

    public Boleto() {
        this.id = -1;
        this.contaCobranca = new ContaCobranca();
        this.nrBoleto = 0;
        this.boletoComposto = "";
        this.nrCtrBoleto = "";
        this.ativo = true;
    }

    public Boleto(int id, ContaCobranca contaCobranca, int nrBoleto, String boletoComposto, String nrCtrBoleto, boolean ativo) {
        this.id = id;
        this.contaCobranca = contaCobranca;
        this.nrBoleto = nrBoleto;
        this.boletoComposto = boletoComposto;
        this.nrCtrBoleto = nrCtrBoleto;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public long getNrBoleto() {
        return nrBoleto;
    }

    public void setNrBoleto(int nrBoleto) {
        this.nrBoleto = nrBoleto;
    }

    public String getBoletoComposto() {
        return boletoComposto;
    }

    public void setBoletoComposto(String boletoComposto) {
        this.boletoComposto = boletoComposto;
    }

    public String getNrCtrBoleto() {
        return nrCtrBoleto;
    }

    public void setNrCtrBoleto(String nrCtrBoleto) {
        this.nrCtrBoleto = nrCtrBoleto;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}