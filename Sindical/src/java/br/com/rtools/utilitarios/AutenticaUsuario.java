package br.com.rtools.utilitarios;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AutenticaUsuario {
    private Usuario usuario;
    private String login;
    private String senha;
    private ControleAcessoBean cab;
    
    
    @PostConstruct
    public void init(){
        usuario = new Usuario();
        login = "";
        senha = "";
        cab = new ControleAcessoBean();
    }

    public void clear(){
        usuario = new Usuario();
        login = "";
        senha = "";
    }
    
    public void validarUsuario(String modalName, String namePermission, Integer evento){
        if (valida(namePermission, evento)){
            PF.openDialog(modalName);
        }else{
            PF.update(":i_autentica_usuario");
        }
    }
    
    public void validarUsuario(String namePermission, Integer evento){
        if (valida(namePermission, evento)){
            
        }else{
            PF.update(":i_autentica_usuario");
        }
    }
    
    public boolean valida(String namePermission, Integer evento){
        if (login.isEmpty()){
            GenericaMensagem.warn("Atenção", "Digite seu Login!");
            return false;
        }
        
        if (senha.isEmpty()){
            GenericaMensagem.warn("Atenção", "Digite sua Senha!");
            return false;
        }
        
        UsuarioDB db = new UsuarioDBToplink();
        usuario = db.ValidaUsuario(login, senha);
        if (usuario != null){
            // se TRUE NÃO tem permissão
            if (!cab.verificarPermissao(usuario, namePermission, evento)){
                GenericaSessao.put("usuarioAutenticado", usuario);
                return true;
            }else{
                GenericaMensagem.error("Atenção", "Usuário sem permissão para essa ação!");
                return false;
            }
        }else{
            GenericaMensagem.error("Atenção", "Não foi possível autenticar usuário!");
            return false;
        }
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
}
