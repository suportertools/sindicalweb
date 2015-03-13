package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
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
@Table(name = "fin_fechamento_caixa")
@NamedQuery(name = "FechamentoCaixa.pesquisaID", query = "select fc from FechamentoCaixa fc where fc.id = :pid")
public class FechamentoCaixa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "ds_hora")
    private String hora;
    @Column(name = "nr_valor_informado")
    private float valorInformado;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @Column(name = "nr_valor_fechamento")
    private float valorFechamento;
    @Column(name = "nr_saldo_atual")
    private float saldoAtual;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_fechamento_geral")
    private Date dtFechamentoGeral;    

    public FechamentoCaixa() {
        this.id = -1;
        this.setData(DataHoje.data());
        this.hora = DataHoje.horaMinuto();
        this.valorInformado = 0;
        this.usuario = new Usuario();
        this.valorFechamento = 0;
        this.saldoAtual = 0;
        this.dtFechamentoGeral = null;
    }

    public FechamentoCaixa(int id, Date dtData, String hora, float valorInformado, Caixa caixa, Usuario usuario, Filial filial, float valorFechamento, float saldoAtual, Date dtFechamentoGeral) {
        this.id = id;
        this.dtData = dtData;
        this.hora = hora;
        this.valorInformado = valorInformado;
        this.usuario = usuario;
        this.valorFechamento = valorFechamento;
        this.saldoAtual = saldoAtual;
        this.dtFechamentoGeral = dtFechamentoGeral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public float getValorInformado() {
        return valorInformado;
    }

    public void setValorInformado(float valorInformado) {
        this.valorInformado = valorInformado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getData() {
        if (dtData != null) {
            return DataHoje.converteData(dtData);
        } else {
            return "";
        }
    }

    public void setData(String data) {
        if (!(data.isEmpty())) {
            this.dtData = DataHoje.converte(data);
        }
    }

    public float getValorFechamento() {
        return valorFechamento;
    }

    public void setValorFechamento(float valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    public float getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(float saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public Date getDtFechamentoGeral() {
        return dtFechamentoGeral;
    }

    public void setDtFechamentoGeral(Date dtFechamentoGeral) {
        this.dtFechamentoGeral = dtFechamentoGeral;
    }
    
    public String getDtFechamentoGeralString() {
        return DataHoje.converteData(dtFechamentoGeral);
    }

    public void setDtFechamentoGeralString(String dtFechamentoGeralString) {
        this.dtFechamentoGeral = DataHoje.converte(dtFechamentoGeralString);
    }
}
