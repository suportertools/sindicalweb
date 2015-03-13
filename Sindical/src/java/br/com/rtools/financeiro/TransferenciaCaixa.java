package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "fin_transferencia_caixa")
@NamedQuery(name = "TransferenciaCaixa.pesquisaID", query = "select tc from TransferenciaCaixa tc where tc.id = :pid")
public class TransferenciaCaixa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_caixa_saida", referencedColumnName = "id")
    @ManyToOne
    private Caixa caixaSaida;
    @Column(name = "nr_valor")
    private float valor;
    @JoinColumn(name = "id_caixa_entrada", referencedColumnName = "id")
    @ManyToOne
    private Caixa caixaEntrada;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @ManyToOne
    private FStatus status;
    @JoinColumn(name = "id_fechamento_entrada", referencedColumnName = "id")
    @ManyToOne
    private FechamentoCaixa fechamentoEntrada;
    @JoinColumn(name = "id_fechamento_saida", referencedColumnName = "id")
    @ManyToOne
    private FechamentoCaixa fechamentoSaida;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    
    public TransferenciaCaixa() {
        this.id = -1;
        this.caixaSaida = new Caixa();
        this.valor = 0;
        this.caixaEntrada = new Caixa();
        this.dtLancamento = DataHoje.dataHoje();
        this.status = new FStatus();
        this.fechamentoEntrada = new FechamentoCaixa();
        this.fechamentoSaida = new FechamentoCaixa();
        this.usuario = null;
    }

    public TransferenciaCaixa(int id, Caixa caixaSaida, float valor, Caixa caixaEntrada, Date dtLancamento, FStatus status, FechamentoCaixa fechamentoEntrada, FechamentoCaixa fechamentoSaida, Usuario usuario) {
        this.id = id;
        this.caixaSaida = caixaSaida;
        this.valor = valor;
        this.caixaEntrada = caixaEntrada;
        this.dtLancamento = dtLancamento;
        this.status = status;
        this.fechamentoEntrada = fechamentoEntrada;
        this.fechamentoSaida = fechamentoSaida;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Caixa getCaixaSaida() {
        return caixaSaida;
    }

    public void setCaixaSaida(Caixa caixaSaida) {
        this.caixaSaida = caixaSaida;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
    
    
    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valorString) {
        this.valor = Moeda.converteUS$(valorString);
    }        

    public Caixa getCaixaEntrada() {
        return caixaEntrada;
    }

    public void setCaixaEntrada(Caixa caixaEntrada) {
        this.caixaEntrada = caixaEntrada;
    }

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public FStatus getStatus() {
        return status;
    }

    public void setStatus(FStatus status) {
        this.status = status;
    }

    public FechamentoCaixa getFechamentoEntrada() {
        return fechamentoEntrada;
    }

    public void setFechamentoEntrada(FechamentoCaixa fechamentoEntrada) {
        this.fechamentoEntrada = fechamentoEntrada;
    }

    public FechamentoCaixa getFechamentoSaida() {
        return fechamentoSaida;
    }

    public void setFechamentoSaida(FechamentoCaixa fechamentoSaida) {
        this.fechamentoSaida = fechamentoSaida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
