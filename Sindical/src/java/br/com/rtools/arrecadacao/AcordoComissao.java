package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_acordo_comissao")
@NamedQuery(name = "AcordoComissao.pesquisaID", query = "select c from AcordoComissao c where c.id = :pid")
public class AcordoComissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_conta_cobranca", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private ContaCobranca contaCobranca;
    @Column(name = "nr_num_documento", length = 100, nullable = true)
    private String numero;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inicio")
    private Date dtInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_fechamento")
    private Date dtFechamento;
    @JoinColumn(name = "id_acordo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Acordo acordo;

    public AcordoComissao() {
        this.id = -1;
        this.contaCobranca = new ContaCobranca();
        this.numero = "";
        this.dtInicio = null;
        this.dtFechamento = null;
        this.acordo = new Acordo();
    }

    public AcordoComissao(int id, ContaCobranca contaCobranca, String numero, Date dtInicio, Date dtFechamento, Acordo acordo) {
        this.id = id;
        this.contaCobranca = contaCobranca;
        this.numero = numero;
        this.dtInicio = dtInicio;
        this.dtFechamento = dtFechamento;
        this.acordo = acordo;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFechamento() {
        return dtFechamento;
    }

    public void setDtFechamento(Date dtFechamento) {
        this.dtFechamento = dtFechamento;
    }

    public Acordo getAcordo() {
        return acordo;
    }

    public void setAcordo(Acordo acordo) {
        this.acordo = acordo;
    }

    public String getFechamento() {
        return DataHoje.converteData(dtFechamento);
    }

    public void setFechamento(String data) {
        setDtFechamento(DataHoje.converte(data));
    }

    public String getInicio() {
        return DataHoje.converteData(dtInicio);
    }

    public void setInicio(String data) {
        setDtInicio(DataHoje.converte(data));
    }
}
