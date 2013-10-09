package br.com.rtools.sistema;

import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import javax.persistence.*;

@Entity
@Table(name = "SIS_CONTADOR_ACESSOS")
@NamedQuery(name = "ContadorAcessos.pesquisaID", query = "select ca from ContadorAcessos ca where ca.id = :pid")
public class ContadorAcessos implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "NR_ACESSO")
    private int acessos;

    public ContadorAcessos() {
        this.id = -1;
        this.usuario = new Usuario();
        this.rotina = new Rotina();
        this.acessos = 0;
    }

    public ContadorAcessos(int id, Usuario usuario, Rotina rotina, int acessos) {
        this.id = id;
        this.usuario = usuario;
        this.rotina = rotina;
        this.acessos = acessos;
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

    public int getAcessos() {
        return acessos;
    }

    public void setAcessos(int acessos) {
        this.acessos = acessos;
    }
}
