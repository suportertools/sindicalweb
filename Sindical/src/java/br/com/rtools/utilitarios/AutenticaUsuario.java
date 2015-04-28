package br.com.rtools.utilitarios;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    
    private String modalName;
    private String namePermission;
    private Integer event;
    private String update;
    private String action;
    private String nameClass;
    
    
    @PostConstruct
    public void init(){
        usuario = new Usuario();
        login = "";
        senha = "";
        cab = new ControleAcessoBean();
        
        modalName = null; 
        namePermission = null; 
        event = null;
        update = null;
        nameClass = null;
        action = null;
    }

    public void clear(){
        usuario = new Usuario();
        login = "";
        senha = "";
        
        modalName = null; 
        namePermission = null; 
        event = null;
        update = null;
        nameClass = null;
        action = null;
    }
    
    public AutenticaUsuario(){
        //init();
        //GenericaSessao.put("AutenticaUsuario", new AutenticaUsuario());
    }
    
    /**
     * @param modalName nome do dialog para ser aberto após a verificação de permissão (caso TRUE)
     * @param namePermission nome da permissão em seg_rotina.ds_acao
     * @param event evento da permissão em seg_evento.id 1;"Inclusão" 2;"Exclusão" 3;"Alteração" 4;"Consulta"
     * @param update componente a ser atualizado após a verificação de permissão
     * @param nameClass nome da classe para ação a ser executado após a verificação de permissão
     * @param action ação a ser executado após a verificação de permissão
     */
    public AutenticaUsuario(String modalName, String namePermission, Integer event, String update, String nameClass, String action){
        this.modalName = modalName;
        this.namePermission = namePermission;
        this.event = event;
        this.update = update;
        this.nameClass = nameClass;
        this.action = action;
        PF.openDialog("dlg_autentica_usuario");
    }
    
    /**
     * @param namePermission nome da permissão em seg_rotina.ds_acao
     * @param event evento da permissão em seg_evento.id 1;"Inclusão" 2;"Exclusão" 3;"Alteração" 4;"Consulta"
     * @param update componente a ser atualizado após a verificação de permissão
     * @param nameClass nome da classe para ação a ser executado após a verificação de permissão
     * @param action ação a ser executado após a verificação de permissão
     */
    public AutenticaUsuario(String namePermission, Integer event, String update, String nameClass, String action){
        this.namePermission = namePermission;
        this.event = event;
        this.update = update;
        this.nameClass = nameClass;
        this.action = action;
        
        PF.openDialog("dlg_autentica_usuario");
    }
    
    /**
     * @param modalName nome do dialog para ser aberto após a verificação de permissão (caso TRUE)
     * @param namePermission nome da permissão em seg_rotina.ds_acao
     * @param event evento da permissão em seg_evento.id 1;"Inclusão" 2;"Exclusão" 3;"Alteração" 4;"Consulta"
     */
    public AutenticaUsuario(String modalName, String namePermission, Integer event){
        this.modalName = modalName;
        this.namePermission = namePermission;
        this.event = event;
        
        PF.openDialog("dlg_autentica_usuario");
    }
    
    public void validarUsuario(){
        // POR CAUSA DO MODAL DE AUTENTICAÇAO ESTAR EM OUTRO FORM PRECISO PEGAR O BEAN DA SESSAO
        AutenticaUsuario au = (AutenticaUsuario) GenericaSessao.getObject("AutenticaUsuario");
        if (au.namePermission == null && au.event == null){
            GenericaMensagem.error("Atenção", "setCase inválido!");
            PF.update(":i_autentica_usuario");
            return;
        }
        
        if (valida(au.namePermission, au.event)){
            if (au.nameClass != null && au.action != null){
                Method m;  
                try {
                    Object object = (Object) GenericaSessao.getObject(au.nameClass);
                    m = object.getClass().getMethod(au.action, new Class<?>[] {});
                    Object result = m.invoke(object, new Object[] {});     
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    ex.getMessage();
                }
            }
            if (au.update != null) PF.update(au.update);
            if (au.modalName != null) PF.openDialog(au.modalName);
            PF.closeDialog("dlg_autentica_usuario");
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

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
}
