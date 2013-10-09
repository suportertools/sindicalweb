package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name = "SEG_USUARIO_ACESSO")
@NamedQuery(name = "UsuarioAcesso.pesquisaID", query = "SELECT ua FROM UsuarioAcesso ua WHERE ua.id=:pid")
public class UsuarioAcesso implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Permissao permissao;
    @Column(name = "IS_PERMITE")
    private boolean permite;

    public UsuarioAcesso() {
        this.id = -1;
        this.usuario = new Usuario();
        this.permissao = new Permissao();
        this.permite = false;
    }

    public UsuarioAcesso(int id, Usuario usuario, Permissao permissao, boolean permite) {
        this.id = id;
        this.usuario = usuario;
        this.permissao = permissao;
        this.permite = permite;
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

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public boolean isPermite() {
        return permite;
    }

    public void setPermite(boolean permite) {
        this.permite = permite;
    }
}
