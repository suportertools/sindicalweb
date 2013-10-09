package br.com.rtools.associativo;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Usuario;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EVE_VENDA")
@NamedQuery(name = "BVenda.pesquisaID", query = "select s from BVenda s where s.id=:pid")
public class BVenda implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = true)
    @OneToOne
    private AEvento evento;
    @JoinColumn(name = "ID_EVT", referencedColumnName = "ID", nullable = true)
    @OneToOne
    private Evt evt;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @JoinColumn(name = "ID_RESPONSAVEL", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Pessoa responsavel;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Usuario usuario;
    @Column(name = "DS_OBS", nullable = true)
    private String obs;

    public BVenda() {
        this.id = -1;
        this.evento = new AEvento();
        this.evt = null;
        this.pessoa = new Pessoa();
        this.responsavel = new Pessoa();
        this.usuario = new Usuario();
        this.obs = "";
    }

    public BVenda(int id, AEvento evento, Evt evt, Pessoa pessoa, Pessoa responsavel, Usuario usuario, String obs) {
        this.id = id;
        this.evento = evento;
        this.evt = evt;
        this.pessoa = pessoa;
        this.responsavel = responsavel;
        this.usuario = usuario;
        this.obs = obs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AEvento getEvento() {
        return evento;
    }

    public void setEvento(AEvento evento) {
        this.evento = evento;
    }

    public Evt getEvt() {
        return evt;
    }

    public void setEvt(Evt evt) {
        this.evt = evt;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
