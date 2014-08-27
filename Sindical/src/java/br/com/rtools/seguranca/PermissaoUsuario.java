package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name = "seg_permissao_usuario")
@NamedQuery(name = "PermissaoUsuario.pesquisaID", query = "select pu from PermissaoUsuario pu where pu.id=:pid")
public class PermissaoUsuario implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Usuario usuario;
    @JoinColumn(name = "id_nivel", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Nivel nivel;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Departamento departamento;

    public PermissaoUsuario() {
        this.id = -1;
        this.usuario = new Usuario();
        this.nivel = new Nivel();
        this.departamento = new Departamento();
    }

    public PermissaoUsuario(int id, Usuario usuario, Nivel nivel, Departamento departamento) {
        this.id = id;
        this.usuario = usuario;
        this.nivel = nivel;
        this.departamento = departamento;
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

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
