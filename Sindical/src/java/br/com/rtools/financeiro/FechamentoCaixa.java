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
@Table(name = "FIN_FECHAMENTO_CAIXA")
@NamedQuery(name = "FechamentoCaixa.pesquisaID", query = "select fc from FechamentoCaixa fc where fc.id = :pid")
public class FechamentoCaixa implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DATA")
    private Date dtData;
    @Column(name = "DS_HORA")
    private String hora;
    @Column(name = "NR_VALOR_INFORMADO")
    private float valorInformado;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario usuario;
    @Column(name = "NR_VALOR_FECHAMENTO")
    private float valorFechamento;

    public FechamentoCaixa() {
        this.id = -1;
        this.setData(DataHoje.data());
        this.hora = DataHoje.horaMinuto();
        this.valorInformado = 0;
        this.usuario = new Usuario();
        this.valorFechamento = 0;
    }
    
    public FechamentoCaixa(int id, Date dtData, String hora, float valorInformado, Caixa caixa, Usuario usuario, Filial filial, float valorFechamento) {
        this.id = id;
        this.dtData = dtData;
        this.hora = hora;
        this.valorInformado = valorInformado;
        this.usuario = usuario;
        this.valorFechamento = valorFechamento;
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
}
