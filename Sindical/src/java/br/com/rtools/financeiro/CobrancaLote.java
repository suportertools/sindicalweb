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
@Table(name="FIN_COBRANCA_LOTE")
@NamedQuery(name="CobrancaLote.pesquisaID", query="select c from CobrancaLote c where c.id = :pid")
public class CobrancaLote implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_EMISSAO")
    private Date dtEmissao;
    @JoinColumn(name="ID_USUARIO", referencedColumnName="ID")
    @ManyToOne
    private Usuario usuario;
    @Column(name="DS_MENSAGEM", length = 8000)
    private String mensagem;

    public CobrancaLote() {
        this.id = -1;
        this.setEmissao(null);
        this.usuario = new Usuario();
        this.mensagem = "";
    }
    
    public CobrancaLote(int id, String emissao, Usuario usuario, String mensagem) {
        this.id = id;
        this.setEmissao(emissao);
        this.usuario = usuario;
        this.mensagem = mensagem;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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
}
