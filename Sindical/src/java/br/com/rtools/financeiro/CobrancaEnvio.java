package br.com.rtools.financeiro;

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
@Table(name = "fin_cobranca_envio")
@NamedQuery(name = "CobrancaEnvio.pesquisaID", query = "select c from CobrancaEnvio c where c.id = :pid")
public class CobrancaEnvio implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_lote", referencedColumnName = "id")
    @ManyToOne
    private CobrancaLote lote;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @Column(name = "ds_hora", length = 5)
    private String hora;
    @JoinColumn(name = "id_cobranca_tipo", referencedColumnName = "id")
    @ManyToOne
    private CobrancaTipo tipoCobranca;

    public CobrancaEnvio() {
        this.id = -1;
        this.lote = new CobrancaLote();
        this.usuario = new Usuario();
        this.setEmissao(null);
        this.hora = "";
        this.tipoCobranca = new CobrancaTipo();
    }

    public CobrancaEnvio(int id, CobrancaLote lote, Usuario usuario, String emissao, String hora, CobrancaTipo tipoCobranca) {
        this.id = id;
        this.lote = lote;
        this.usuario = usuario;
        this.setEmissao(hora);
        this.hora = hora;
        this.tipoCobranca = tipoCobranca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CobrancaLote getLote() {
        return lote;
    }

    public void setLote(CobrancaLote lote) {
        this.lote = lote;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public CobrancaTipo getTipoCobranca() {
        return tipoCobranca;
    }

    public void setTipoCobranca(CobrancaTipo tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
    }

    public String getEmissao() {
        return DataHoje.converteData(dtEmissao);
    }

    public void setEmissao(String emissao) {
        this.dtEmissao = DataHoje.converte(emissao);
    }
}
