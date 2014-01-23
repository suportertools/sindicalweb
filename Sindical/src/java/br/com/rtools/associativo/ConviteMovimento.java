package br.com.rtools.associativo;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "CONV_MOVIMENTO")
@NamedQuery(name = "ConviteMovimento.pesquisaID", query = "SELECT CONM FROM ConviteMovimento CONM WHERE CONM.id = :pid")
public class ConviteMovimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_OBS", length = 300, nullable = true)
    private String observacao;
    @JoinColumn(name = "ID_SIS_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private SisPessoa sisPessoa;
    // SÓCIO
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "ID_USUARIO_INATIVACAO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Usuario usuarioInativacao;
    @JoinColumn(name = "ID_EVT", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Evt evt;
    @JoinColumn(name = "ID_DEPARTAMEMTNO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Departamento departamento;
    @JoinColumn(name = "ID_AUTORIZA_CORTESIA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private ConviteAutorizaCortesia autorizaCortesia;
    @Column(name = "IS_CORTESIA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean cortesia;
    @Column(name = "IS_ATIVO", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_VALIDADE")
    private Date dtValidade;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EMISSAO")
    private Date dtEmissao;

    public ConviteMovimento() {
        this.id = -1;
        this.observacao = "";
        this.sisPessoa = new SisPessoa();
        this.pessoa = new Pessoa();
        this.usuario = new Usuario();
        this.usuarioInativacao = new Usuario();
        this.evt = new Evt();
        this.departamento = new Departamento();
        this.autorizaCortesia = new ConviteAutorizaCortesia();
        this.cortesia = false;
        this.ativo = false;
        this.dtValidade = DataHoje.dataHoje();
        this.dtEmissao = DataHoje.dataHoje();
    }

    public ConviteMovimento(int id, String observacao, SisPessoa sisPessoa, Pessoa pessoa, Usuario usuario, Usuario usuarioInativacao, Evt evt, Departamento departamento, ConviteAutorizaCortesia autorizaCortesia, boolean cortesia, boolean ativo, String validade, String emissao) {
        this.id = id;
        this.observacao = observacao;
        this.sisPessoa = sisPessoa;
        this.pessoa = pessoa;
        this.usuario = usuario;
        this.usuarioInativacao = usuarioInativacao;
        this.evt = evt;
        this.departamento = departamento;
        this.autorizaCortesia = autorizaCortesia;
        this.cortesia = cortesia;
        this.ativo = ativo;
        this.dtValidade = DataHoje.converte(validade);
        this.dtEmissao = DataHoje.converte(emissao);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public SisPessoa getSisPessoa() {
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
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

    public Usuario getUsuarioInativacao() {
        return usuarioInativacao;
    }

    public void setUsuarioInativacao(Usuario usuarioInativacao) {
        this.usuarioInativacao = usuarioInativacao;
    }

    public Evt getEvt() {
        return evt;
    }

    public void setEvt(Evt evt) {
        this.evt = evt;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public ConviteAutorizaCortesia getAutorizaCortesia() {
        return autorizaCortesia;
    }

    public void setAutorizaCortesia(ConviteAutorizaCortesia autorizaCortesia) {
        this.autorizaCortesia = autorizaCortesia;
    }

    public boolean isCortesia() {
        return cortesia;
    }

    public void setCortesia(boolean cortesia) {
        this.cortesia = cortesia;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(Date dtValidade) {
        this.dtValidade = dtValidade;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getValidade() {
        return DataHoje.converteData(dtValidade);
    }

    public void setValidade(String validade) {
        this.dtValidade = DataHoje.converte(validade);
    }

    public String getEmissao() {
        return DataHoje.converteData(dtEmissao);
    }

    public void setEmissao(String emissao) {
        this.dtEmissao = DataHoje.converte(emissao);
    }

    @Override
    public String toString() {
        return "ConviteMovimento{" + "id=" + id + ", observacao=" + observacao + ", sisPessoa=" + sisPessoa + ", pessoa=" + pessoa + ", usuario=" + usuario + ", usuarioInativacao=" + usuarioInativacao + ", evt=" + evt + ", departamento=" + departamento + ", autorizaCortesia=" + autorizaCortesia + ", cortesia=" + cortesia + ", ativo=" + ativo + ", dtValidade=" + dtValidade + ", dtEmissao=" + dtEmissao + '}';
    }

}
