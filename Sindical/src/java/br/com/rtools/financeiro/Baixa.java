package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_baixa")
@NamedQuery(name = "Baixa.pesquisaID", query = "select l from Baixa l where l.id=:pid")
public class Baixa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_baixa")
    private Date dtBaixa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_importacao")
    private Date dtImportacao;
    @Column(name = "nr_sequencia_baixa")
    private int sequenciaBaixa;
    @Column(name = "ds_documento_baixa")
    private String documentoBaixa;
    @JoinColumn(name = "id_caixa", referencedColumnName = "id")
    @ManyToOne
    private Caixa caixa;
    @JoinColumn(name = "id_fechamento_caixa", referencedColumnName = "id")
    @ManyToOne
    private FechamentoCaixa fechamentoCaixa;
    @JoinColumn(name = "id_usuario_desconto", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioDesconto;
    @Column(name = "nr_troco")
    private float troco;

    public Baixa() {
        this.id = -1;
        this.usuario = new Usuario();
        this.setBaixa("");
        this.setImportacao("");
        this.sequenciaBaixa = 0;
        this.documentoBaixa = "";
        this.caixa = new Caixa();
        this.fechamentoCaixa = new FechamentoCaixa();
        this.usuarioDesconto = null;
        this.troco = 0;
    }

    public Baixa(int id,
            Usuario usuario,
            String baixa,
            String importacao,
            int sequenciaBaixa,
            String documentoBaixa,
            Caixa caixa,
            FechamentoCaixa fechamentoCaixa,
            Usuario usuarioDesconto,
            float troco) {
        this.id = id;
        this.usuario = usuario;
        this.setBaixa(baixa);
        this.setImportacao(importacao);
        this.fechamentoCaixa = fechamentoCaixa;
        this.sequenciaBaixa = sequenciaBaixa;
        this.documentoBaixa = documentoBaixa;
        this.caixa = caixa;
        this.usuarioDesconto = usuarioDesconto;
        this.troco = troco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDtImportacao() {
        return dtImportacao;
    }

    public void setDtImportacao(Date dtImportacao) {
        this.dtImportacao = dtImportacao;
    }

    public String getImportacao() {
        return DataHoje.converteData(dtImportacao);
    }

    public void setImportacao(String dtImportacao) {
        this.dtImportacao = DataHoje.converte(dtImportacao);
    }

    public String getBaixa() {
        return DataHoje.converteData(dtBaixa);
    }

    public void setBaixa(String baixa) {
        this.dtBaixa = DataHoje.converte(baixa);
    }

    public Date getDtBaixa() {
        return dtBaixa;
    }

    public void setDtBaixa(Date dtBaixa) {
        this.dtBaixa = dtBaixa;
    }

    public int getSequenciaBaixa() {
        return sequenciaBaixa;
    }

    public void setSequenciaBaixa(int sequenciaBaixa) {
        this.sequenciaBaixa = sequenciaBaixa;
    }

    public String getDocumentoBaixa() {
        return documentoBaixa;
    }

    public void setDocumentoBaixa(String documentoBaixa) {
        this.documentoBaixa = documentoBaixa;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public FechamentoCaixa getFechamentoCaixa() {
        return fechamentoCaixa;
    }

    public void setFechamentoCaixa(FechamentoCaixa fechamentoCaixa) {
        this.fechamentoCaixa = fechamentoCaixa;
    }

    public Usuario getUsuarioDesconto() {
        return usuarioDesconto;
    }

    public void setUsuarioDesconto(Usuario usuarioDesconto) {
        this.usuarioDesconto = usuarioDesconto;
    }

    public float getTroco() {
        return troco;
    }

    public void setTroco(float troco) {
        this.troco = troco;
    }
}
