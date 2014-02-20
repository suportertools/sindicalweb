package br.com.rtools.seguranca;

import br.com.rtools.pessoa.Pessoa;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SEG_USUARIO")
@NamedQueries({
    @NamedQuery(name = "Usuario.pesquisaID", query = "SELECT U FROM Usuario U WHERE U.id = :pid "),
    @NamedQuery(name = "Usuario.findAll", query = "SELECT U FROM Usuario U ORDER BY U.pessoa.nome ASC, U.login ASC ")
})
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Column(name = "DS_LOGIN", length = 15, nullable = false)
    private String login;
    @Column(name = "DS_SENHA", length = 6, nullable = false)
    private String senha;
    @Column(name = "IS_ATIVO")
    private boolean ativo;

    public Usuario() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.login = "";
        this.senha = "";
        this.ativo = false;
    }

    public Usuario(int id, Pessoa pessoa, String login, String senha, boolean ativo) {
        this.id = id;
        this.pessoa = pessoa;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", pessoa=" + pessoa + ", login=" + login + ", ativo=" + ativo + '}';
    }
}
