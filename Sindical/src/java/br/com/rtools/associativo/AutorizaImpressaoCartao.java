package br.com.rtools.associativo;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
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
@Table(name = "soc_autoriza_impressao_cartao")
@NamedQuery(name = "AutorizaImpressaoCartao.pesquisaID", query = "select a from AutorizaImpressaoCartao a where a.id = :pid")
public class AutorizaImpressaoCartao implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_historico_carteirinha", referencedColumnName = "id")
    @ManyToOne
    private HistoricoCarteirinha historicoCarteirinha;
    @JoinColumn(name = "id_modelo_carteirinha", referencedColumnName = "id")
    @ManyToOne
    private ModeloCarteirinha modeloCarteirinha;
    @Column(name = "is_foto")
    private boolean foto;

    public AutorizaImpressaoCartao() {
        this.id = -1;
        this.setEmissao(DataHoje.data());
        this.pessoa = new Pessoa();
        this.usuario = new Usuario();
        this.historicoCarteirinha = null;
        this.modeloCarteirinha = new ModeloCarteirinha();
        this.foto = false;
   }

    public AutorizaImpressaoCartao(int id, String emissao, Pessoa pessoa, Usuario usuario, HistoricoCarteirinha historicoCarteirinha, ModeloCarteirinha modeloCarteirinha, boolean foto) {
        this.id = id;
        this.setEmissao(emissao);
        this.pessoa = pessoa;
        this.usuario = usuario;
        this.historicoCarteirinha = historicoCarteirinha;
        this.modeloCarteirinha = modeloCarteirinha;
        this.foto = foto;
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public HistoricoCarteirinha getHistoricoCarteirinha() {
        return historicoCarteirinha;
    }

    public void setHistoricoCarteirinha(HistoricoCarteirinha historicoCarteirinha) {
        this.historicoCarteirinha = historicoCarteirinha;
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

    public ModeloCarteirinha getModeloCarteirinha() {
        return modeloCarteirinha;
    }

    public void setModeloCarteirinha(ModeloCarteirinha modeloCarteirinha) {
        this.modeloCarteirinha = modeloCarteirinha;
    }

    public boolean isFoto() {
        return foto;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
    }
    
}
