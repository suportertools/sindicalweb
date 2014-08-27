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
@Table(name = "fin_impressao")
@NamedQuery(name = "Impressao.pesquisaID", query = "select i from Impressao i where i.id = :pid")
public class Impressao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_impressao", nullable = false)
    private Date dtImpressao;
    @Column(name = "ds_hora", nullable = false)
    private String hora;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento")
    private Date dtVencimento;

    public Impressao(int id, Movimento movimento, Usuario usuario, Date dtImpressao, String hora, Date dtVencimento) {
        this.id = id;
        this.movimento = movimento;
        this.usuario = usuario;
        this.dtImpressao = dtImpressao;
        this.hora = hora;
        this.dtVencimento = dtVencimento;
    }

    public Impressao() {
        this.id = -1;
        this.movimento = new Movimento();
        this.usuario = new Usuario();
        this.dtImpressao = DataHoje.dataHoje();
        this.hora = DataHoje.hora();
        this.dtVencimento = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDtImpressao() {
        return dtImpressao;
    }

    public void setDtImpressao(Date dtImpressao) {
        this.dtImpressao = dtImpressao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getImpressao() {
        return DataHoje.converteData(dtImpressao);
    }

    public String getVencimento() {
        return DataHoje.converteData(dtVencimento);
    }

}
