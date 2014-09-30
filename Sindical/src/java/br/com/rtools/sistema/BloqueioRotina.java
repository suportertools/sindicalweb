package br.com.rtools.sistema;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "sis_bloqueio_rotina")
public class BloqueioRotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id")
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_bloqueio", nullable = true)
    private Date bloqueio;

    public BloqueioRotina() {
        this.id = -1;
        this.rotina = new Rotina();
        this.usuario = new Usuario();
        this.pessoa = new Pessoa();
        this.bloqueio = null;
    }

    public BloqueioRotina(int id, Rotina rotina, Usuario usuario, Pessoa pessoa, Date bloqueio) {
        this.id = id;
        this.rotina = rotina;
        this.usuario = usuario;
        this.pessoa = pessoa;
        this.bloqueio = bloqueio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(Date bloqueio) {
        this.bloqueio = bloqueio;
    }
}
