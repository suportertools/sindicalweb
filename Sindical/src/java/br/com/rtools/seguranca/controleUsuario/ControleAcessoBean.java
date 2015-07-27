package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.financeiro.db.ContaBancoDB;
import br.com.rtools.financeiro.db.ContaBancoDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.PermissaoDB;
import br.com.rtools.seguranca.db.PermissaoDao;
import br.com.rtools.seguranca.db.PermissaoUsuarioDao;
import br.com.rtools.seguranca.db.RotinaDao;
import br.com.rtools.seguranca.db.UsuarioAcessoDao;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.sistema.ContadorAcessos;
import br.com.rtools.sistema.db.AtalhoDB;
import br.com.rtools.sistema.db.AtalhoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class ControleAcessoBean implements Serializable {

    // static final long serialVersionUID = 7220145288109489651L;
    private String login = "";
    private Pessoa loginContribuinte = null;
    private int idModulo = 0;
    private String urlDestino;
    private HttpServletRequest paginaRequerida;
    private String showRendered = "true";
    private String msgConfirma = "";
    private int tipo = -1;
    private Modulo modulo = new Modulo();
    private Rotina rotina = new Rotina();
    private Evento evento = new Evento();
    private int idIndexPermissao = -1;
    private boolean bloqueiaMenu = false;

    public String getShowRendered() {
        return showRendered;
    }

    public void setShowRendered(String showRendered) {
        this.showRendered = showRendered;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getValidacao() throws IOException {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlDestino = paginaRequerida.getRequestURI();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso") == null) {
            redirectAcessoNegado();
            return null;
        }
        if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso")).equals("local")) {
            if (urlDestino.equals("/Sindical/usuarioPerfil.jsf")) {
                return null;
            }
            if ((!verificarUsuario()) || !verificarPermissaoUsuario()) {
                redirectAcessoNegado();
                return null;
            } else {
                controleInterno(urlDestino);
                return null;
            }
        } else if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso")).equals("web")) {
            if ((!verificarUsuarioAcessoWeb()) || !verificarTipoPagina()) {
                redirectAcessoNegado();
            } else {
                controleInterno(urlDestino);
                return null;
            }
        }
        return null;
    }

    public String controleInterno(String urlCaminho) throws IOException {
        String retorno = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlControleInterno");
        if (retorno == null) {
            retorno = "";
        } else if (retorno.equals(converteURL(urlCaminho))) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("linkClicado") == null) {
            redirectAcessoNegado();
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("linkClicado");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlControleInterno", converteURL(urlCaminho));
        }
        return null;
    }

    public void redirectAcessoNegado() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null || FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "acessoNegado");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/acessoNegado.jsf");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "sessaoExpirou");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/sessaoExpirou.jsf");
        }
    }

    public boolean verificarUsuarioAcessoWeb() {
        UsuarioDB db = new UsuarioDBToplink();
        Pessoa contri = null;
        Pessoa conta = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            loginContribuinte = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
            contri = db.ValidaUsuarioContribuinteWeb(loginContribuinte.getId());
            conta = db.ValidaUsuarioContabilidadeWeb(loginContribuinte.getId());
        } else {
            loginContribuinte = null;
        }

        if ((loginContribuinte == null)) {
            return false;
        } else {
            if ((contri != null) && (conta != null)) {
                tipo = 0; //OS DOIS
            } else if (contri != null) {
                tipo = 1; //CONTRIBUINTES
            } else if (conta != null) {
                tipo = 2; //CONTABILIDADE
            } else {
                tipo = 3; //PATRONAL
            }
            return true;
        }
    }

    public boolean verificarTipoPagina() {
        String conv = converteURL(urlDestino);
        if (tipo == 0) {
            return true;
        } else if (conv.equals("menuPrincipalAcessoWeb") || conv.equals("webConfiguracoes")) {
            return true;
        } else if ((tipo == 1) && (conv.equals("webContribuinte") || conv.equals("webAgendamentoContribuinte") || conv.equals("webSolicitaREPIS"))) {
            return true;
        } else if ((tipo == 2) && (conv.equals("webContabilidade") || conv.equals("webAgendamentoContabilidade") || conv.equals("webSolicitaREPIS"))) {
            return true;
        } else if ((tipo == 3) && (conv.equals("webLiberacaoREPIS") || conv.equals("webRelatorioCertificados"))) {
            return true;
        }
        return false;
    }

    public boolean verificarPermissaoUsuario() {
        boolean retorno = false;
        PermissaoUsuarioDao db = new PermissaoUsuarioDao();
        RotinaDao rotinaDao = new RotinaDao();
        if (!urlDestino.equals("/Sindical/menuPrincipal.jsf")
                && ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() != 1) {

            //PESQUISA DE MODULOS-------------------------------------------------------------------------------------------
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null) {
                idModulo = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
                if (idModulo != 0) {
                    modulo = (Modulo) new Dao().find(new Modulo(), idModulo);
                }
            }

            //PESQUISA DE ROTINAS-------------------------------------------------------------------------------------------
            rotina = rotinaDao.pesquisaRotinaPermissao(urlDestino);

            if (rotina == null) {
                rotina = new Rotina();
            }

            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaSimples") != null) {
                String[] lista = (String[]) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaSimples");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("chamadaPaginaSimples");
                Rotina r = rotinaDao.pesquisaRotinaPermissaoPorClasse(lista[0]);
                if (r != null) {
                    AtalhoDB dba = new AtalhoDBToplink();
                    rotina = new Rotina();
                    rotina = r;

                    ContadorAcessos cont = dba.pesquisaContadorAcessos(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId(), rotina.getId());
                    if (cont == null) {
                        cont = new ContadorAcessos();
                        cont.setRotina(rotina);
                        cont.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
                    }

                    cont.setAcessos(cont.getAcessos() + 1);
                    Dao dao = new Dao();
                    dao.openTransaction();

                    if (cont.getId() == -1) {
                        if (dao.save(cont)) {
                            dao.commit();
                        } else {
                            dao.rollback();
                        }
                    } else {
                        if (dao.update(cont)) {
                            dao.commit();
                        } else {
                            dao.rollback();
                        }
                    }
                }

            }

            if (rotina.getId() == -1) {
                return false;
            }

            //PESQUISA DE EVENTOS-------------------------------------------------------------------------------------------
            evento = (Evento) new Dao().find(new Evento(), 4);

            if (evento == null) {
                evento = new Evento();
            }

            // PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------            
            if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
                Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
                Permissao permissao;
                if (modulo.getId() != -1) {
                    permissao = db.pesquisaPermissao(modulo.getId(), rotina.getId(), evento.getId());
                } else {
                    permissao = db.pesquisaPermissao(9, rotina.getId(), evento.getId());
                }

                if (permissao.getId() != -1) {
                    List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = false;
                        } else {
                            retorno = true;
                            break;
                        }
                    }
                    //if (retorno) {
                    UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                    if (usuarioAcesso.getId() != -1) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = true;
                        } else {
                            retorno = false;
                        }
                    }
                    //}
                } else {
                    retorno = false;
                }
            } else {
                return retorno;
            }
        } else {
            if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() != 1) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo");
            }
            modulo = new Modulo();
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoSalvar() {
        Object object = (Object) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("object");
        if (object == null) {
            return true;
        }
        Class classe = object.getClass();
        Integer id = -1;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            // id = (Integer) metodo.invoke(object, null);
            id = (Integer) metodo.invoke(object, (Object[]) null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //System.out.println(erro);
        }

        Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        boolean retorno = true;
        if (user != null) {
            int idEvento;
            if (user.getId() != 1) {
                PermissaoUsuarioDao db = new PermissaoUsuarioDao();
                Permissao permissao;
                idModulo = modulo.getId();
                if (id == -1) {
                    if (modulo.getId() != -1) {
                        idEvento = 1;
                    } else {
                        idModulo = 9;
                        idEvento = 1;
                    }

                } else {
                    if (modulo.getId() != -1) {
                        idEvento = 3;
                    } else {
                        idModulo = 9;
                        idEvento = 3;
                    }
                }
                permissao = db.pesquisaPermissao(idModulo, rotina.getId(), idEvento);
                if (permissao.getId() != -1) {
                    List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());

                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = true;
                        } else {
                            retorno = false;
                            break;
                        }
                    }
//                    if (!retorno) {
                    UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                    if (usuarioAcesso.getId() != -1) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = false;
                        } else {
                            retorno = true;
                        }
                    }
//                    }
                } else {
                    retorno = true;
                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean getSalvar(Object object, int idMod) throws IllegalArgumentException {
        if (object == null) {
            return false;
        }
        if (idMod == 0) {
            idMod = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo"));
        }
        Class classe = object.getClass();
        Integer id = -1;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            // id = (Integer) metodo.invoke(object, null);
            id = (Integer) metodo.invoke(object, (Object[]) null);
        } catch (NoSuchMethodException e) {
            //System.out.println(erro);
        } catch (IllegalAccessException e) {
            //System.out.println(erro);
        } catch (InvocationTargetException e) {
            //System.out.println(erro);
        }
        Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        Dao dao = new Dao();
        Modulo m = (Modulo) dao.find(new Modulo(), idMod);
        boolean retorno = false;
        if (user != null) {
            int idEvento;
            if (user.getId() != 1) {
                PermissaoUsuarioDao db = new PermissaoUsuarioDao();
                Permissao permissao;
                idModulo = m.getId();
                if (id == -1) {
                    if (m.getId() != -1) {
                        idEvento = 1;
                    } else {
                        idModulo = 9;
                        idEvento = 1;
                    }

                } else {
                    if (m.getId() != -1) {
                        idEvento = 3;
                    } else {
                        idModulo = 9;
                        idEvento = 3;
                    }
                }
                permissao = db.pesquisaPermissao(idModulo, rotina.getId(), idEvento);
                if (permissao.getId() != -1) {
                    List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());

                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = true;
                        } else {
                            retorno = false;
                            break;
                        }
                    }
                    UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                    if (usuarioAcesso.getId() != -1) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = false;
                        } else {
                            retorno = true;
                        }
                    }
                } else {
                    retorno = true;
                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean getBotaoExcluir() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = true;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");

            if (user.getId() == 1) {
                return false;
            }

            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), rotina.getId(), 2);
            } else {
                permissao = db.pesquisaPermissao(9, rotina.getId(), 2);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (!retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getExcluir(int idMod) {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        if (idMod == 0) {
            idMod = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo"));
        }
        Dao dao = new Dao();
        Modulo m = (Modulo) dao.find(new Modulo(), idMod);
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");

            if (user.getId() == 1) {
                return false;
            }

            if (m.getId() != -1) {
                permissao = db.pesquisaPermissao(m.getId(), rotina.getId(), 2);
            } else {
                permissao = db.pesquisaPermissao(9, rotina.getId(), 2);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoAlterar() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");

            if (user.getId() == 1) {
                return false;
            }

            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), rotina.getId(), 3);
            } else {
                permissao = db.pesquisaPermissao(9, rotina.getId(), 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (!retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoEstornarBoleto() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }

            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 134, 2);
            } else {
                permissao = db.pesquisaPermissao(9, 134, 2);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (!retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getListaExtratoTela() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return true;
            }
            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 153, 4);
            } else {
                permissao = db.pesquisaPermissao(9, 153, 4);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = false;
                    } else {
                        retorno = true;
                        break;
                    }
                }
//                if (retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = true;
                    } else {
                        retorno = false;
                    }
                }
//                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean getTotalValorExtratoTela() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return true;
            }
            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 165, 4);
            } else {
                permissao = db.pesquisaPermissao(9, 165, 4);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = false;
                    } else {
                        retorno = true;
                        break;
                    }
                }
//                if (retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = true;
                    } else {
                        retorno = false;
                    }
                }
//                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean getBotaoExcluirAcordo() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }
            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 135, 2);
            } else {
                permissao = db.pesquisaPermissao(9, 135, 2);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (!retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoAlterarDataVenctoExtrato() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }
            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 136, 3);
            } else {
                permissao = db.pesquisaPermissao(9, 136, 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoDescontoSocial() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }
            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 120, 3);
            } else {
                permissao = db.pesquisaPermissao(9, 120, 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoAlterarValorCobrancaMensal() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }
            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 254, 3);
            } else {
                permissao = db.pesquisaPermissao(9, 254, 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (retorno) {
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoEstornarMensalidades() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }

            permissao = db.pesquisaPermissao(2, 285, 3);

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (PermissaoUsuario permissaoUsuario : permissaoUsuarios) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuario.getDepartamento().getId(), permissaoUsuario.getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoEstornarMensalidadesOutrosUsuarios() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }

            permissao = db.pesquisaPermissao(2, 286, 3);

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (PermissaoUsuario permissaoUsuario : permissaoUsuarios) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuario.getDepartamento().getId(), permissaoUsuario.getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoAlterarMovimento() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }

            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 190, 3);
            } else {
                permissao = db.pesquisaPermissao(2, 190, 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (PermissaoUsuario permissaoUsuario : permissaoUsuarios) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuario.getDepartamento().getId(), permissaoUsuario.getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoBaixaBanco() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }

            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 287, 3);
            } else {
                permissao = db.pesquisaPermissao(2, 287, 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (PermissaoUsuario permissaoUsuario : permissaoUsuarios) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuario.getDepartamento().getId(), permissaoUsuario.getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoFecharCaixaOutroUsuario() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            Permissao permissao;
            PermissaoUsuarioDao db = new PermissaoUsuarioDao();
            Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            if (user.getId() == 1) {
                return false;
            }

            if (modulo.getId() != -1) {
                permissao = db.pesquisaPermissao(modulo.getId(), 288, 3);
            } else {
                permissao = db.pesquisaPermissao(1, 288, 3);
            }

            if (permissao.getId() != -1) {
                List<PermissaoUsuario> permissaoUsuarios = db.listaPermissaoUsuario(user.getId());
                for (PermissaoUsuario permissaoUsuario : permissaoUsuarios) {
                    PermissaoDepartamento permissaoDepartamento = db.pesquisaPermissaoDepartamento(permissaoUsuario.getDepartamento().getId(), permissaoUsuario.getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean verificarUsuario() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            login = ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getLogin();
        } else {
            login = "";
        }
        if ((login == null) || (login.equals(""))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean verificarContaBanco() {
        ContaBancoDB db = new ContaBancoDBToplink();
        List contas = db.pesquisaTodos();
        if (!contas.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String converteURL(String url) {
        String result;
        int iniURL, fimURL;
        iniURL = url.lastIndexOf("/");
        fimURL = url.lastIndexOf(".");
        result = url.substring(iniURL + 1, fimURL);
        return result;
    }

    public String Teste() {
        setShowRendered("false");
        return null;
    }

    public int getIdIndexPermissao() {
        return idIndexPermissao;
    }

    public void setIdIndexPermissao(int idIndexPermissao) {
        this.idIndexPermissao = idIndexPermissao;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(int idEvento) {
        return verificaPermissao(null, 0, 0, idEvento);

    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param pageName Nome da página da rotina
     * @param idEvento
     * @return
     */
    public boolean verificarPermissao(String pageName, Integer idEvento) {
        return verificaPermissao(pageName, idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param usuario Class
     * @param pageName Nome da página da rotina
     * @param idEvento
     * @return
     */
    public boolean verificarPermissao(Usuario usuario, String pageName, Integer idEvento) {
        RotinaDao rotinaDao = new RotinaDao();
        String pagina = pageName;
        Rotina r = rotinaDao.pesquisaRotinaPorPagina(pagina);
        if (r == null) {
            r = rotinaDao.pesquisaRotinaPorAcao(pagina);
        }
        if (r == null) {
            return false;
        }
        return verificaPermissao(usuario, 0, r.getId(), idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param pageName
     * @param idEvento
     * @return
     */
    public Boolean permissaoValida(String pageName, Integer idEvento) {
        return verificaPermissao(pageName, idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param pageName
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(String pageName, Integer idEvento) {
        Usuario user = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        if (user.getId() == 1) {
            return false;
        }
        RotinaDao rotinaDao = new RotinaDao();
        String pagina = pageName;
        Rotina r = rotinaDao.pesquisaRotinaPorPagina(pagina);
        if (r == null) {
            r = rotinaDao.pesquisaRotinaPorAcao(pagina);
        }
        if (r == null) {
            return false;
        }
        return verificaPermissao(null, 0, r.getId(), idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param idRotina
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(int idRotina, int idEvento) {
        return verificaPermissao(null, 0, idRotina, idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param idModulo
     * @param idRotina
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(Usuario usuario, int idModulo, int idRotina, int idEvento) {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = true;
        if (usuario == null) {
            if (GenericaSessao.exists("sessaoUsuario")) {
                Permissao permissao;
                Usuario user = (Usuario) GenericaSessao.getObject("sessaoUsuario");
                if (user.getId() == 1) {
                    return false;
                }
                if (idModulo == 0) {
                    idModulo = modulo.getId();
                }
                if (idRotina == 0 || idRotina == -1) {
                    idRotina = rotina.getId();
                }
                PermissaoUsuarioDao permissaoUsuarioDB = new PermissaoUsuarioDao();
                PermissaoDao permissaoDao = new PermissaoDao();
                if (idModulo != -1) {
                    permissao = permissaoDao.pesquisaPermissaoModuloRotinaEvento(idModulo, idRotina, idEvento);
                } else {
                    permissao = permissaoDao.pesquisaPermissaoModuloRotinaEvento(2, idRotina, idEvento);
                }
                if (permissao != null) {
                    List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDB.listaPermissaoUsuario(user.getId());
                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = permissaoUsuarioDB.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = true;
                        } else {
                            retorno = false;
                            break;
                        }
                    }
                    UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(user.getId(), permissao.getId());
                    if (usuarioAcesso.getId() != -1) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = false;
                        } else {
                            retorno = true;
                        }
                    }
                } else {
                    retorno = true;
                }
            } else {
                retorno = true;
            }
        } else {
            Permissao permissao;
            if (usuario.getId() == 1) {
                return false;
            }
            if (idModulo == 0) {
                idModulo = modulo.getId();
            }
            if (idRotina == 0 || idRotina == -1) {
                idRotina = rotina.getId();
            }

            PermissaoUsuarioDao permissaoUsuarioDB = new PermissaoUsuarioDao();
            PermissaoDao permissaoDao = new PermissaoDao();
            if (idModulo != -1) {
                permissao = permissaoDao.pesquisaPermissaoModuloRotinaEvento(idModulo, idRotina, idEvento);
            } else {
                permissao = permissaoDao.pesquisaPermissaoModuloRotinaEvento(2, idRotina, idEvento);
            }
            if (permissao != null) {
                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDB.listaPermissaoUsuario(usuario.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = permissaoUsuarioDB.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcesso usuarioAcesso = new UsuarioAcessoDao().pesquisaUsuarioAcesso(usuario.getId(), permissao.getId());
                if (usuarioAcesso.getId() != -1) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }

        }
        return retorno;
    }
}
