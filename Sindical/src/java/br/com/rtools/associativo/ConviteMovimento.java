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
@Table(name = "conv_movimento")
@NamedQuery(name = "ConviteMovimento.pesquisaID", query = "SELECT CONM FROM ConviteMovimento CONM WHERE CONM.id = :pid")
public class ConviteMovimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_obs", length = 300)
    private String observacao;
    @JoinColumn(name = "id_sis_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private SisPessoa sisPessoa;
    // SÓCIO
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_usuario_inativacao", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioInativacao;
    @JoinColumn(name = "id_evt", referencedColumnName = "id")
    @ManyToOne
    private Evt evt;
    @JoinColumn(name = "id_departamemtno", referencedColumnName = "id")
    @ManyToOne
    private Departamento departamento;
    @JoinColumn(name = "id_autoriza_cortesia", referencedColumnName = "id")
    @ManyToOne
    private ConviteAutorizaCortesia autorizaCortesia;
    @Column(name = "is_cortesia", columnDefinition = "boolean default false")
    private boolean cortesia;
    @Column(name = "is_ativo", columnDefinition = "boolean default true")
    private boolean ativo;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_validade")
    private Date dtValidade;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id")
    @ManyToOne
    private ConviteServico conviteServico;
    @Column(name = "ds_controle_cortesia", length = 200)
    private String controleCortesia;
    
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
        this.ativo = true;
        this.dtValidade = DataHoje.dataHoje();
        this.dtEmissao = DataHoje.dataHoje();
        this.conviteServico = new ConviteServico();
        this.controleCortesia = "";
    }

    public ConviteMovimento(int id, String observacao, SisPessoa sisPessoa, Pessoa pessoa, Usuario usuario, Usuario usuarioInativacao, Evt evt, Departamento departamento, ConviteAutorizaCortesia autorizaCortesia, boolean cortesia, boolean ativo, String validade, String emissao, ConviteServico conviteServico, String controleCortesia) {
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
        this.conviteServico = conviteServico;
        this.controleCortesia = controleCortesia;
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

    public ConviteServico getConviteServico() {
        return conviteServico;
    }

    public void setConviteServico(ConviteServico conviteServico) {
        this.conviteServico = conviteServico;
    }

    @Override
    public String toString() {
        return "ConviteMovimento{" + "id=" + id + ", observacao=" + observacao + ", sisPessoa=" + sisPessoa + ", pessoa=" + pessoa + ", usuario=" + usuario + ", usuarioInativacao=" + usuarioInativacao + ", evt=" + evt + ", departamento=" + departamento + ", autorizaCortesia=" + autorizaCortesia + ", cortesia=" + cortesia + ", ativo=" + ativo + ", dtValidade=" + dtValidade + ", dtEmissao=" + dtEmissao + ", conviteServico=" + conviteServico + '}';
    }

    public String getControleCortesia() {
        return controleCortesia;
    }

    public void setControleCortesia(String controleCortesia) {
        this.controleCortesia = controleCortesia;
    }
}
