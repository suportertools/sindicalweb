package br.com.rtools.associativo;

import br.com.rtools.financeiro.Movimento;
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
@Table(name = "soc_historico_emissao_guias")
@NamedQuery(name = "HistoricoEmissaoGuias.pesquisaID", query = "select heg from HistoricoEmissaoGuias heg where heg.id = :pid")
public class HistoricoEmissaoGuias implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @Column(name = "is_baixado")
    private boolean baixado;

    public HistoricoEmissaoGuias() {
        this.id = -1;
        this.setEmissao(DataHoje.data());
        this.movimento = new Movimento();
        this.usuario = new Usuario();
        this.baixado = false;
    }

    public HistoricoEmissaoGuias(int id, String emissao, Movimento movimento, Usuario usuario, boolean baixado) {
        this.id = id;
        this.setEmissao(emissao);
        this.movimento = movimento;
        this.usuario = usuario;
        this.baixado = baixado;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }
    
    public String getEmissao() {
        if (dtEmissao != null) {
            return DataHoje.converteData(dtEmissao);
        } else {
            return "";
        }
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty())) {
            this.dtEmissao = DataHoje.converte(emissao);
        }
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

    public boolean isBaixado() {
        return baixado;
    }

    public void setBaixado(boolean baixado) {
        this.baixado = baixado;
    }
}
