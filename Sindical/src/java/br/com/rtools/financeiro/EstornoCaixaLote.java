package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Usuario;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "fin_estorno_caixa_lote")
public class EstornoCaixaLote implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_baixa")
    private Date dtBaixa;
    @JoinColumn(name = "id_usuario_estorno", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuarioEstorno;
    @JoinColumn(name = "id_usuario_caixa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuarioCaixa;    
    @JoinColumn(name = "id_caixa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Caixa caixa;  
    @Column(name = "ds_motivo")
    private String motivo;
    @Column(name = "nr_id_baixa")
    private int nrIdBaixa;

    public EstornoCaixaLote() {
        this.id = -1;
        this.dtLancamento = null;
        this.dtBaixa = null;
        this.usuarioEstorno = new Usuario();
        this.usuarioCaixa = new Usuario();
        this.caixa = new Caixa();
        this.motivo = "";
        this.nrIdBaixa = -1;
    }

    public EstornoCaixaLote(int id, Date dtLancamento, Date dtBaixa, Usuario usuarioEstorno, Usuario usuarioCaixa, Caixa caixa, String motivo, int nrIdBaixa) {
        this.id = id;
        this.dtLancamento = dtLancamento;
        this.dtBaixa = dtBaixa;
        this.usuarioEstorno = usuarioEstorno;
        this.usuarioCaixa = usuarioCaixa;
        this.caixa = caixa;
        this.motivo = motivo;
        this.nrIdBaixa = nrIdBaixa;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public Date getDtBaixa() {
        return dtBaixa;
    }

    public void setDtBaixa(Date dtBaixa) {
        this.dtBaixa = dtBaixa;
    }

    public Usuario getUsuarioEstorno() {
        return usuarioEstorno;
    }

    public void setUsuarioEstorno(Usuario usuarioEstorno) {
        this.usuarioEstorno = usuarioEstorno;
    }

    public Usuario getUsuarioCaixa() {
        return usuarioCaixa;
    }

    public void setUsuarioCaixa(Usuario usuarioCaixa) {
        this.usuarioCaixa = usuarioCaixa;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getNrIdBaixa() {
        return nrIdBaixa;
    }

    public void setNrIdBaixa(int nrIdBaixa) {
        this.nrIdBaixa = nrIdBaixa;
    }
    
    
}
