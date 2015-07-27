package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.associativo.ConfiguracaoSocial;
import br.com.rtools.associativo.beans.ConfiguracaoSocialBean;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.principal.DBExternal;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.sistema.ContadorAcessos;
import br.com.rtools.sistema.db.AtalhoDB;
import br.com.rtools.sistema.db.AtalhoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.db.FunctionsDB;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
    private boolean habilitaLog = false;

    public void atualizaDemissionaSocios() {
        FunctionsDB db = new FunctionsDao();
        ConfiguracaoSocialBean csb = new ConfiguracaoSocialBean();
        csb.init();
        ConfiguracaoSocial cs = csb.getConfiguracaoSocial();
        if (cs.isInativaDemissionado() && DataHoje.maiorData(DataHoje.dataHoje(), cs.getDataInativacaoDemissionado()) && cs.getGrupoCategoriaInativaDemissionado() != null) {
            db.demissionaSocios(cs.getGrupoCategoriaInativaDemissionado().getId(), cs.getDiasInativaDemissionado());
            Dao di = new Dao();
            cs = (ConfiguracaoSocial) di.find(cs);
            di.openTransaction();
            cs.setDataInativacaoDemissionado(DataHoje.dataHoje());
            di.update(cs);
            di.commit();
        }
    }

    public boolean block() throws Exception {
        String nomeCliente = null;
        if (GenericaSessao.exists("sessaoCliente")) {
            nomeCliente = (String) GenericaSessao.getString("sessaoCliente");
        }
        if (nomeCliente == null) {
            return true;
        }
        if (nomeCliente.equals("Rtools") || nomeCliente.equals("Sindical") || nomeCliente.equals("ComercioLimeira")) {
            return true;
        }
        ResultSet rs;
        PreparedStatement ps;
        DBExternal dBExternal = new DBExternal();
        try {
            ps = dBExternal.getConnection().prepareStatement(
                    "   SELECT *                    "
                    + "   FROM sis_configuracao     "
                    + "  WHERE ds_identifica =     '" + nomeCliente + "'"
                    + "  LIMIT 1                    "
            );
            //ps.setString(1, nomeCliente);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return true;
            }

            Boolean ativo = rs.getBoolean("is_ativo");

            if (ativo) {
                return true;
            }

//            while (rs.next()) {
//                
//                Boolean ativo = Boolean.parseBoolean(rs.getString("is_ativo"));
//                if (ativo) {
//                    return true;
//                }
//            }
        } catch (Exception e) {
            e.getMessage();
            return true;
        }
        return false;
    }

    public String validacao() throws Exception {
        if (!block()) {
            GenericaMensagem.warn("Sistema. Entre em contato com nosso suporte técnico. (16) 3964-6117", "Entre em contato com nosso suporte técnico.");
            return null;
        }
        String pagina = null;
        NovoLog log = new NovoLog();
        if (macFilial != null) {
            Object objs[] = new Object[2];
            objs[0] = macFilial.getFilial();
            objs[1] = macFilial.getDepartamento();
            GenericaSessao.put("acessoFilial", macFilial);
            filial = "Filial: ( " + macFilial.getFilial().getFilial().getPessoa().getNome() + " / " + macFilial.getDepartamento().getDescricao() + " )";
            if (macFilial.getMesa() > 0) {
                filial += " - Guiche: " + macFilial.getMesa();
            }
            if (macFilial.getDescricao() != null && !macFilial.getDescricao().isEmpty()) {
                filial += " - " + macFilial.getDescricao();
            }
            if (macFilial.getCaixa() != null) {
                if (macFilial.getCaixa().getCaixa() > 0) {
                    filial += " - Caixa: " + macFilial.getCaixa().getCaixa();
                }
            }
        } else {
            GenericaSessao.put("acessoFilial", null);
            filial = "";
        }
        GenericaSessao.put("indicaAcesso", "local");
        UsuarioDB db = new UsuarioDBToplink();
        String user = usuario.getLogin(), senh = usuario.getSenha();
        if (usuario.getLogin().equals("") || usuario.getLogin().equals("Usuario")) {
            msgErro = "@ Informar nome do usuário!";
            GenericaMensagem.warn("Validação", msgErro);
            return pagina;
        }
        if (usuario.getSenha().equals("") || usuario.getSenha().equals("Senha")) {
            msgErro = "@ Informar senha!";
            GenericaMensagem.warn("Validação", msgErro);
            return pagina;
        }
        usuario = db.ValidaUsuario(usuario.getLogin(), usuario.getSenha());
        if (usuario != null) {
            AtalhoDB dba = new AtalhoDBToplink();
            if (dba.listaAcessosUsuario(usuario.getId()).isEmpty()) {
                Diretorio.criar("");
                Diretorio.criar("Relatorios");
                Diretorio.criar("Imagens/Fotos");
                Diretorio.criar("Imagens/LogoPatronal");
                Diretorio.criar("Imagens/Mapas");
                Diretorio.criar("Arquivos/contrato");
                Diretorio.criar("Arquivos/convencao");
                Diretorio.criar("Arquivos/downloads/boletos");
                Diretorio.criar("Arquivos/downloads/carteirinhas");
                Diretorio.criar("Arquivos/downloads/etiquetas");
                Diretorio.criar("Arquivos/downloads/fichas");
                Diretorio.criar("Arquivos/downloads/protocolo");
                Diretorio.criar("Arquivos/downloads/relatorios");
                Diretorio.criar("Arquivos/downloads/remessa");
                Diretorio.criar("Arquivos/downloads/repis");
                Diretorio.criar("Arquivos/notificacao");
                Diretorio.criar("Arquivos/retorno/pendentes"); // EXCLUIR DEPOIS DA DATA 01/11/2014 EM FASE DE TESTES
                Diretorio.criar("Arquivos/senhas");
            }

            pagina = "menuPrincipal";
            GenericaSessao.put("sessaoUsuario", usuario);
            GenericaSessao.put("usuarioLogin", usuario.getLogin());
            GenericaSessao.put("userName", usuario.getLogin());
            GenericaSessao.put("linkClicado", true);
            GenericaSessao.put("acessoCadastro", false);
            login = ((Usuario) GenericaSessao.getObject("sessaoUsuario")).getPessoa().getNome() + " - "
                    + ((Usuario) GenericaSessao.getObject("sessaoUsuario")).getPessoa().getTipoDocumento().getDescricao() + ": "
                    + ((Usuario) GenericaSessao.getObject("sessaoUsuario")).getPessoa().getDocumento();
            //log.novo("Usuário logou", "Usuário:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "";
            atualizaDemissionaSocios();
        } else {
            //log.live("Login de acesso tentativa de acesso usr:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "@ Usuário e/ou Senha inválidas! Tente novamente.";
            GenericaMensagem.warn("Validação", msgErro);
        }
        return pagina;
    }

    public String getValidacaoIndex() throws IOException {
        if (GenericaSessao.exists("sessaoCliente")) {
            GenericaSessao.remove("conexao");
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String requestCliente;
        if (request.getParameter("cliente") != null && request.getParameter("cliente") != FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente")) {
            requestCliente = request.getParameter("cliente");
        } else {
            requestCliente = "Sindical";
        }
        if (!requestCliente.equals("")) {
            if (GenericaSessao.exists("sessaoCliente")) {
                GenericaSessao.remove("sessaoCliente");
            }
            if (GenericaSessao.exists("acessoFilial")) {
                GenericaSessao.remove("acessoFilial");
            }
            GenericaSessao.put("sessaoCliente", requestCliente);
        } else {
            if (GenericaSessao.exists("sessaoCliente")) {
                GenericaSessao.remove("sessaoCliente");
            }
            if (GenericaSessao.exists("acessoFilial")) {
                GenericaSessao.remove("acessoFilial");
            }
        }
        return null;
    }

    public String getValidaIndex() {
        if (GenericaSessao.exists("sessaoCliente")) {
            GenericaSessao.remove("conexao");
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
            if (GenericaSessao.exists("sessaoCliente")) {
                GenericaSessao.remove("sessaoCliente");
            }
            if (GenericaSessao.exists("acessoFilial")) {
                GenericaSessao.remove("acessoFilial");
            }
            GenericaSessao.put("sessaoCliente", requestCliente);
        } else {
            if (GenericaSessao.exists("sessaoCliente")) {
                GenericaSessao.remove("sessaoCliente");
            }
            if (GenericaSessao.exists("acessoFilial")) {
                GenericaSessao.remove("acessoFilial");
            }
        }
        return null;
    }

    public void refreshForm(String retorno) throws IOException {
    }

    public String voltarDoAcessoNegado() {
        linkVoltar = GenericaSessao.getString("urlRetorno");
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
        if (usuario == null) {
            usuario = new Usuario();
        }
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
            MacFilialDao macFilialDao = new MacFilialDao();
            setMacFilial(macFilialDao.pesquisaMac(filialDep));

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
        if (GenericaSessao.exists("sessaoUsuario")) {
            Usuario usu = ((Usuario) GenericaSessao.getObject("sessaoUsuario"));
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
        if (GenericaSessao.exists("idModulo")) {
            GenericaSessao.remove("idModulo");
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
                if (usuarioSuporteTecnico.getEmail().isEmpty()) {
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

    public boolean isHabilitaLog() {
        return habilitaLog;
    }

    public void setHabilitaLog(boolean habilitaLog) {
        GenericaSessao.put("habilitaLog", habilitaLog);
        this.habilitaLog = habilitaLog;
    }

}

//            if (!nomeCliente.equals("Rtools") && !nomeCliente.equals("Sindical")) {
//                DBExternal dbe = new DBExternal();
//                if (dbe.getConnection() != null) {
//                    try {
//                        String string = "SELECT * FROM sis_configuracao WHERE ds_identifica = '" + nomeCliente + "'";
//                        ResultSet resultSet = dbe.getStatment().executeQuery(string);
//                        String id = "";
//                        String ativo = "";
//                        while (resultSet.next()) {
//                            id = resultSet.getString("id");
//                            ativo = resultSet.getString("is_ativo");
//                            if (ativo.equals("f")) {
//                                resultSet.close();
//                                dbe.getStatment().close();
//                                msgErro = "@ Entre em contato com nossa equipe (16) 3964.6117";
//                                GenericaMensagem.warn(msgErro, "");
//                                return null;
//                            }
//                        }
//                        if (!id.equals("")) {
//                            string = "UPDATE sis_configuracao SET nr_acesso = (nr_acesso+1) WHERE id = " + id;
//                            int result = dbe.getStatment().executeUpdate(string);
//                            if (result != 1) {
//                                dbe.getStatment().close();
//                                msgErro = "@ Erro ao atualizar contador!";
//                                GenericaMensagem.warn(msgErro, "");
//                                return null;
//                            }
//                        }
//                    } catch (SQLException exception) {
//                        dbe.closeStatment();
//                        msgErro = "@ Erro!";
//                        GenericaMensagem.warn(msgErro, "");
//                        return null;
//                    }
//                    dbe.getStatment().close();
//                }
//            }
