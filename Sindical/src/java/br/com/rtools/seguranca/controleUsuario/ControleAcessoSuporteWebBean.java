package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ControleAcessoSuporteWebBean {

    private Pessoa pessoa = new Pessoa();
    private Usuario usuario = new Usuario();
    private String login = "";
    private String status = "";
    private String msgNovaSenha = "";
    private String verificaSenha = "";
    private String msgLoginInvalido = "";

    public void refreshForm() {
    }

    public String refreshFormStr() {
        return "indexSuporteWeb";
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

    public String validacao() throws IOException {
        String pagina = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("indicaAcesso", "suporteWeb");
        UsuarioDB db = new UsuarioDBToplink();
        usuario = (db.ValidaUsuarioSuporteWeb(usuario.getLogin(), usuario.getSenha()));
        if ((getUsuario() != null)) {
            pagina = "menuPrincipalSuporteWeb";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessaoUsuario", getUsuario());
            login = usuario.getPessoa().getNome() + " - "
                    + usuario.getPessoa().getTipoDocumento().getDescricao() + ": "
                    + usuario.getPessoa().getDocumento();
            usuario = new Usuario();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        } else {
            if (getUsuario() == null) {
                msgLoginInvalido = "Usuário ou/e senha inválidos!";
            } else {
                msgLoginInvalido = "Usuário não existe!";
            }
            setUsuario(new Usuario());
        }
        return pagina;
    }

    public String getLinkSite() {
        return "indexSuporte";
    }

    public String getMsgNovaSenha() {
        return msgNovaSenha;
    }

    public void setMsgNovaSenha(String msgNovaSenha) {
        this.msgNovaSenha = msgNovaSenha;
    }

    public String getVerificaSenha() {
        return verificaSenha;
    }

    public void setVerificaSenha(String verificaSenha) {
        this.verificaSenha = verificaSenha;
    }

    public String getMsgLoginInvalido() {
        return msgLoginInvalido;
    }

    public void setMsgLoginInvalido(String msgLoginInvalido) {
        this.msgLoginInvalido = msgLoginInvalido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
