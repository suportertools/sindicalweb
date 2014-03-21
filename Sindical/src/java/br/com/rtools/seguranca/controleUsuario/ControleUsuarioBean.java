package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.sistema.ContadorAcessos;
import br.com.rtools.sistema.db.AtalhoDB;
import br.com.rtools.sistema.db.AtalhoDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class ControleUsuarioBean implements Serializable {

    private Usuario usuario = new Usuario();
    private Usuario usuarioSuporteTecnico = new Usuario();
    private String login = "";
    private String linkVoltar;
    private int fimURL;
    private int iniURL;
    private String paginaDestino;
    private String alerta;
    private String emailSuporteTecnico;
    private static String cliente;
    private static String bloqueiaMenu;
    private String filial;
    private String filialDep = "";
    private String msgErro = "";
    private MacFilial macFilial = null;
    private List<ContadorAcessos> listaContador = new ArrayList();
    private List<String> images = new ArrayList<String>();

    public String validacao() throws Exception {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) { 
//            String nomeCliente = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente");        
//            if (!nomeCliente.equals("Rtools") && !nomeCliente.equals("Sindical")) {
//                DB db = new DB();
//                if(db.getStatment() != null) {
//                    try {
//                        String string = "SELECT * FROM sis_configuracao WHERE ds_identifica = '"+nomeCliente+"'";
//                        ResultSet resultSet = db.getStatment().executeQuery(string);
//                        String id = "";
//                        String ativo = "";
//                        while(resultSet.next())  {
//                            id = resultSet.getString("id");
//                            ativo = resultSet.getString("is_ativo");
//                            if (ativo.equals("f")) {
//                                resultSet.close();
//                                db.getStatment().close();
//                                msgErro = "@ Entre em contato com nossa equipe (16) 3964.6117";
//                                return "pagina";
//                            } 
//                        }
//                        if (!id.equals("")) {
//                            string = "UPDATE sis_configuracao SET nr_acesso = (nr_acesso+1) WHERE id = "+id;
//                            int result = db.getStatment().executeUpdate(string);
//                            if (result != 1) {
//                                db.getStatment().close();
//                                msgErro = "@ Erro ao atualizar contador!";
//                            }
//                        }
//                    } catch (SQLException exception) {
//                        db.closeStatment();
//                        msgErro = "@ Erro!";
//                        return "pagina";
//                    }
//                    db.getStatment().close();
//                }
//            }
//        }
        NovoLog log = new NovoLog();
        if (macFilial != null) {
            Object objs[] = new Object[2];
            objs[0] = macFilial.getFilial();
            objs[1] = macFilial.getDepartamento();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoFilial", macFilial);
            filial = "Filial: ( " + macFilial.getFilial().getFilial().getPessoa().getNome() + " / " + macFilial.getDepartamento().getDescricao() + " )";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoFilial", null);
            filial = "";
        }
        String pagina = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("indicaAcesso", "local");
        UsuarioDB db = new UsuarioDBToplink();
        String user = usuario.getLogin(), senh = usuario.getSenha();
        if (usuario.getLogin().equals("") || usuario.getLogin().equals("Usuario")) {
            msgErro = "@ Informar nome do usuário!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", msgErro));
            return pagina;
        }
        if (usuario.getSenha().equals("") || usuario.getSenha().equals("Senha")) {
            msgErro = "@ Informar senha!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", msgErro));
            return pagina;
        }
        usuario = db.ValidaUsuario(usuario.getLogin(), usuario.getSenha());
        if (usuario != null) {
            pagina = "menuPrincipal";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessaoUsuario", usuario);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogin", usuario.getLogin());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", usuario.getLogin());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
            login = ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getPessoa().getNome() + " - "
                    + ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getPessoa().getTipoDocumento().getDescricao() + ": "
                    + ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getPessoa().getDocumento();
            log.novo("login de acesso", "usuario logou!");
            usuario = new Usuario();
            msgErro = "";
        } else {
            log.novo("login de acesso", "tentativa de acesso usr:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "@ Usuário e/ou Senha inválidas! Tente novamente.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", msgErro));
        }
        return pagina;
    }

    public String getValidacaoIndex() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("conexao");
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String requestCliente;
        if (request.getParameter("cliente") != null && request.getParameter("cliente") != FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente")) {
            requestCliente = request.getParameter("cliente");
        } else {
            requestCliente = "Sindical";
        }
        if (!requestCliente.equals("")) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoCliente");
            }
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("acessoFilial");
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessaoCliente", requestCliente);
        } else {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoCliente");
            }
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("acessoFilial");
            }
        }
        return null;
    }

    public String getValidaIndex() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("conexao");
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String requestCliente;
        if (request.getParameter("cliente") != null && request.getParameter("cliente") != FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente")) {
            FacesContext conext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
            session.invalidate();
            requestCliente = request.getParameter("cliente");
        } else {
            requestCliente = "Sindical";
        }
        if (!requestCliente.equals("")) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoCliente");
            }
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("acessoFilial");
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessaoCliente", requestCliente);
        } else {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoCliente");
            }
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("acessoFilial");
            }
        }
        return null;
    }

    public void refreshForm(String retorno) throws IOException {
    }

    public String voltarDoAcessoNegado() {
        linkVoltar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (linkVoltar == null) {
            return "index";
        } else {
            return converteURL();
        }
    }

    public String converteURL() {
        String url = linkVoltar;
        iniURL = url.lastIndexOf("/");
        fimURL = url.lastIndexOf(".");
        if (iniURL != -1 && fimURL != -1) {
            paginaDestino = url.substring(iniURL + 1, fimURL);
        } else {
            paginaDestino = url;
        }
        return paginaDestino;
    }

    public void carregar() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlIndex", request.getQueryString());
        filialDep = "oi";
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

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getFilialDep() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //  filialDep = request.getRequestURL().toString();
        // filialDep = requestFilial.getQueryString();
        filialDep = request.getParameter("filial");
        if (filialDep != null) {
            MacFilialDB db = new MacFilialDBToplink();
            setMacFilial(db.pesquisaMac(filialDep));

            if (getMacFilial() != null) {
                filialDep = getMacFilial().getFilial().getFilial().getPessoa().getNome();
            } else {
                filialDep = "Filial sem Registro";
            }
        }
        return filialDep;
    }

    public void setFilialDep(String filialDep) {
        this.filialDep = filialDep;
    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public List<ContadorAcessos> getListaContador() {
        if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")) != null) {
            Usuario usu = ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
//            RotinaDB db = new RotinaDBToplink();
            AtalhoDB dba = new AtalhoDBToplink();
            listaContador.clear();
            //listaRotina = db.pesquisaAcessosOrdem();
            listaContador = dba.listaAcessosUsuario(usu.getId());
        }
        return listaContador;
    }

    public void setListaContador(List<ContadorAcessos> listaContador) {
        this.listaContador = listaContador;
    }

    public static String getCliente() {
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
        }
        return cliente;
    }

    public String getClienteString() {
        String novoCliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            novoCliente = GenericaSessao.getString("sessaoCliente");
        }
        return novoCliente;
    }

    public boolean isBoqueiaMenu() {
        String nomeCliente = getClienteString();
        if (nomeCliente.equals("Rtools") || nomeCliente.equals("Sindical")) {
            return true;
        }
        return false;
    }

    public static void setBloqueiaMenu(String aBloqueiaMenu) {
        bloqueiaMenu = aBloqueiaMenu;
    }

    public void removeSessaoModuloMenuPrincipal() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null) {
            GenericaSessao.remove("idModulo");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo");
        }
    }

    public List<String> getImages() {
        if (images.isEmpty()) {
            images = new ArrayList<String>();
            images.add("1.jpg");
            images.add("2.jpg");
            images.add("3.jpg");
            images.add("4.jpg");
            images.add("5.jpg");
        }
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Usuario getUsuarioSuporteTecnico() {
        if (usuarioSuporteTecnico.getId() == -1) {
            if (GenericaSessao.exists("sessaoUsuario")) {
                usuarioSuporteTecnico = (Usuario) GenericaSessao.getObject("sessaoUsuario");
                if (usuarioSuporteTecnico.getEmail().equals("")) {
                    if (!usuarioSuporteTecnico.getPessoa().getEmail1().equals("")) {
                        usuarioSuporteTecnico.setEmail(usuarioSuporteTecnico.getPessoa().getEmail1());
                    } else {                        
                        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
                        Usuario u = (Usuario) dB.find(new Usuario(), 1);
                        usuarioSuporteTecnico.setEmail(u.getPessoa().getEmail1());
                    }
                }
            }
        }
        return usuarioSuporteTecnico;
    }

    public void setUsuarioSuporteTecnico(Usuario usuarioSuporteTecnico) {
        this.usuarioSuporteTecnico = usuarioSuporteTecnico;
    }
}
