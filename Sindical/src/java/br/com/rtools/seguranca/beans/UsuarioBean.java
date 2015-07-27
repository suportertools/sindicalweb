package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;

@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

    private Usuario usuario;
    private List<PermissaoUsuario> listaPermissaoUsuario;
    private List<Usuario> listaUsuario;
    private List<UsuarioAcesso> listaUsuarioAcesso;
    private List<SelectItem> listaModulos;
    private List<SelectItem> listaRotinas;
    private List<SelectItem> listaEventos;
    private List<SelectItem> listaDepartamentos;
    private List<SelectItem> listaNiveis;
    private String confirmaSenha;
    private String descricaoPesquisa;
    private String mensagem;
    private String senhaNova;
    private String senhaAntiga;
    private String userLogado;
    private boolean adicionado;
    private boolean disSenha;
    private boolean disNovaSenha;
    private boolean disStrSenha;
    private boolean filtrarPorModulo;
    private boolean filtrarPorRotina;
    private boolean filtrarPorEvento;
    private boolean filtrarUsuarioAtivo;
    private int idDepartamento;
    private int idEvento;
    private int idModulo;
    private int idNivel;
    private int idRotina;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        listaPermissaoUsuario = new ArrayList();
        listaUsuario = new ArrayList();
        listaUsuarioAcesso = new ArrayList();
        listaModulos = new ArrayList<SelectItem>();
        listaRotinas = new ArrayList<SelectItem>();
        listaEventos = new ArrayList<SelectItem>();
        listaDepartamentos = new ArrayList<SelectItem>();
        listaNiveis = new ArrayList<SelectItem>();
        confirmaSenha = "";
        descricaoPesquisa = "";
        mensagem = "";
        senhaNova = "";
        senhaAntiga = "";
        userLogado = "";
        adicionado = false;
        disSenha = false;
        disNovaSenha = false;
        disStrSenha = true;
        filtrarPorModulo = false;
        filtrarPorRotina = false;
        filtrarPorEvento = false;
        filtrarUsuarioAtivo = true;
        idDepartamento = 0;
        idEvento = 0;
        idModulo = 0;
        idNivel = 0;
        idRotina = 0;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("usuarioBean");
        GenericaSessao.remove("usuarioPesquisa");
        GenericaSessao.remove("pessoaPesquisa");
        GenericaSessao.remove("uploadBean");
        GenericaSessao.remove("photoCamBean");
        clear(1);
        clear(2);
    }

    public void clear() {
        GenericaSessao.remove("usuarioBean");
    }

    public void clear(Integer tCase) {
        if (tCase == 1) {
            try {
                FileUtils.deleteDirectory(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "/Cliente/" + getCliente() + "/temp/" + "foto/" + new SegurancaUtilitariosBean().getSessaoUsuario().getId()));
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/" + -1 + ".png"));
                if (f.exists()) {
                    f.delete();
                }
            } catch (IOException ex) {
                Logger.getLogger(FisicaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (tCase == 2) {
            GenericaSessao.remove("cropperBean");
            GenericaSessao.remove("uploadBean");
            GenericaSessao.remove("photoCamBean");
        }
    }

    public void save() {
        DaoInterface di = new Dao();
        di.openTransaction();

        UsuarioDB db = new UsuarioDBToplink();

        if (usuario.getLogin().equals("")) {
            mensagem = "Campo login não pode ser nulo!";
            return;
        }

        if (usuario.getSenha().equals("")) {
            mensagem = "Campo senha não pode ser nulo!";
            return;
        }
        if (usuario.getSenha().length() > 6) {
            mensagem = "A senha deve ter no máximo 6 Caracteres!";
            return;
        }
        NovoLog novoLog = new NovoLog();
        if (usuario.getId() == -1) {
            if (usuario.getPessoa().getId() == -1) {
                mensagem = "Pesquise um nome de Usuário disponível!";
                return;
            }

            if (!usuario.getSenha().equals(confirmaSenha)) {
                mensagem = "Senhas não correspondem!";
                return;
            }
            if (db.pesquisaLogin(usuario.getLogin(), usuario.getPessoa().getId()).isEmpty()) {
                if (di.save(usuario)) {
                    di.commit();
                    mensagem = "Login e senha salvos com Sucesso!";
                    novoLog.save("ID: " + usuario.getId() + " - Pessoa: " + usuario.getPessoa().getId() + " - " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin() + " - Ativo: " + usuario.getAtivo());
                } else {
                    di.rollback();
                    mensagem = "Erro ao Salvar Login e Senha!";
                }
            } else {
                di.rollback();
                mensagem = "Este login já existe no Sistema.";
            }
        } else {
            Usuario user = (Usuario) di.find(usuario);
            if (disNovaSenha) {
                if (user.getSenha().equals(getSenhaAntiga()) && !usuario.getSenha().equals("")) {
                } else {
                    usuario.setSenha(user.getSenha());
                    mensagem = "Senha Incompativel!";
                }
            } else {
                usuario.setSenha(user.getSenha());
            }
            Usuario usu = (Usuario) di.find(usuario);
            String beforeUpdate = "ID: " + usu.getId() + " - Pessoa: (" + usu.getPessoa().getId() + ") " + usu.getPessoa().getNome() + " - Login: " + usu.getLogin() + " - Ativo: " + usu.getAtivo();
            if (di.update(usuario)) {
                di.commit();
                mensagem = "Login e senha salvos com Sucesso!";
                novoLog.update(beforeUpdate, "ID: " + usuario.getId() + " - Pessoa: (" + usuario.getPessoa().getId() + ") " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin() + " - Ativo: " + usuario.getAtivo());
            } else {
                di.rollback();
                mensagem = "Erro ao atualizar Usuario!";
            }
        }
    }

    public String addUsuarioPermissao() {
        if (usuario.getId() == -1) {
            return null;
        }
        PermissaoUsuario pu = new PermissaoUsuario();
        DaoInterface di = new Dao();
        pu.setDepartamento((Departamento) di.find(new Departamento(), Integer.valueOf(listaDepartamentos.get(idDepartamento).getDescription())));
        pu.setNivel((Nivel) di.find(new Nivel(), Integer.valueOf(listaNiveis.get(idNivel).getDescription())));
        pu.setUsuario(usuario);
        PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
        if (permissaoUsuarioDao.existePermissaoUsuario(pu)) {
            GenericaMensagem.warn("Sistema", "Permissão já existe");
            return null;
        }
        di.openTransaction();
        if (di.save(pu)) {
            di.commit();
            NovoLog novoLog = new NovoLog();
            novoLog.save("Permissão Usuário - ID: " + pu.getId() + " - Usuário (" + pu.getUsuario().getId() + ") " + pu.getUsuario().getLogin() + " - Departamento (" + pu.getDepartamento().getId() + ") " + pu.getDepartamento().getDescricao() + " - Nível (" + pu.getNivel().getId() + ") " + pu.getNivel().getDescricao());
            GenericaMensagem.info("Sucesso", "Permissão adicionada");
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Erro ao adicionar essa permissão!");
        }
        idDepartamento = 0;
        idNivel = 0;
        return null;
    }

    public String removePermissaoUsuario(PermissaoUsuario pu) {
        if (pu.getId() != -1) {
            DaoInterface di = new Dao();
            pu = (PermissaoUsuario) di.find(pu);
            di.openTransaction();
            if (di.delete(pu)) {
                di.commit();
                NovoLog novoLog = new NovoLog();
                novoLog.delete("Permissão Usuário - ID: " + pu.getId() + " - Usuário (" + pu.getUsuario().getId() + ") " + pu.getUsuario().getLogin() + " - Departamento (" + pu.getDepartamento().getId() + ") " + pu.getDepartamento().getDescricao() + " - Nível (" + pu.getNivel().getId() + ") " + pu.getNivel().getDescricao());
                GenericaMensagem.info("Sucesso", "Permissão removida");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao Excluír permissão!");
                return null;
            }
        }
        return null;
    }

    public boolean removePermissoes(DaoInterface di) {
        PermissaoUsuarioDao db = new PermissaoUsuarioDao();
        UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
        for (int i = 0; i < listaPermissaoUsuario.size(); i++) {
            PermissaoUsuario perUsuario = listaPermissaoUsuario.get(i);
            perUsuario = db.pesquisaPermissaoUsuario(perUsuario.getUsuario().getId(), perUsuario.getDepartamento().getId(), perUsuario.getNivel().getId());

            List<PermissaoDepartamento> lista = db.pesquisaPDepartamento(perUsuario.getDepartamento().getId(), perUsuario.getNivel().getId());
            for (int w = 0; w < lista.size(); w++) {
                UsuarioAcesso ua = usuarioAcessoDao.pesquisaUsuarioAcesso(perUsuario.getUsuario().getId(), lista.get(w).getPermissao().getId());
                if (!di.delete(ua)) {
                    di.rollback();
                    return false;
                }
            }
            if (!di.delete(perUsuario)) {
                di.rollback();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public void delete() {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (usuario.getId() != -1) {
            if (!removePermissoes(di)) {
                mensagem = "Erro ao excluir permissões!";
                return;
            }

            if (!di.delete(usuario)) {
                di.rollback();
                mensagem = "Login não pode ser excluido!";
            } else {
                di.commit();
                mensagem = "Login excluido com sucesso!";
                NovoLog novoLog = new NovoLog();
                novoLog.delete("ID: " + usuario.getId() + " - Pessoa: " + usuario.getPessoa().getId() + " - " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin());
                destroy();
            }
        }
    }

    public String edit(Usuario usu) {
        usuario = usu;
        clear(1);
        clear(2);
        GenericaSessao.put("pessoaPesquisa", usuario.getPessoa());
        GenericaSessao.put("usuarioPesquisa", usuario);
        GenericaSessao.put("linkClicado", true);
        return (String) GenericaSessao.getString("urlRetorno");
    }

    public List<Usuario> getListaUsuario() {
        if (listaUsuario.isEmpty()) {
            if (descricaoPesquisa.isEmpty()) {
                DaoInterface di = new Dao();
                listaUsuario = di.list(new Usuario(), true);
            } else {
                UsuarioDB db = new UsuarioDBToplink();
                listaUsuario = db.pesquisaTodosPorDescricao(descricaoPesquisa);
            }
            List<Usuario> list = new ArrayList<Usuario>();
            if (filtrarUsuarioAtivo) {
                for (int i = 0; i < listaUsuario.size(); i++) {
                    if (listaUsuario.get(i).getAtivo()) {
                        list.add(listaUsuario.get(i));
                    }
                }
                listaUsuario.clear();
                listaUsuario = list;
            }
        }
        return listaUsuario;
    }

    public void limparListaUsuario() {
        listaUsuario.clear();
    }

    public List<SelectItem> getListaNiveis() {
        if (listaNiveis.isEmpty()) {
            DaoInterface di = new Dao();
            List niveis = di.list(new Nivel(), true);
            if (!niveis.isEmpty()) {
                for (int i = 0; i < niveis.size(); i++) {
                    listaNiveis.add(new SelectItem(i,
                            ((Nivel) niveis.get(i)).getDescricao(),
                            Integer.toString(((Nivel) niveis.get(i)).getId()))
                    );
                }
            }
        }
        return listaNiveis;
    }

    public List<SelectItem> getListaDepartamentos() {
        if (listaDepartamentos.isEmpty()) {
            DaoInterface di = new Dao();
            List departamentos = di.list(new Departamento(), true);
            for (int i = 0; i < departamentos.size(); i++) {
                listaDepartamentos.add(new SelectItem(i,
                        ((Departamento) departamentos.get(i)).getDescricao(),
                        Integer.toString(((Departamento) departamentos.get(i)).getId()))
                );
            }
        }
        return listaDepartamentos;
    }

    public void sairSistema() throws IOException {
        String retorno = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            retorno = (String) GenericaSessao.getString("sessaoCliente") + "/";
            if (GenericaSessao.exists("acessoFilial")) {
                MacFilial macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
                retorno += "?filial=" + macFilial.getMac();
            }

        }
        /*String sair;
         if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null){
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuario");
         sair = "index";
         }else sair = "";
         return sair;*/
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(retorno);
    }

    public String sairSuporteWeb() {
        /*String sair;
         if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null){
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuario");
         sair = "index";
         }else sair = "";
         return sair;*/
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        return "indexSuporte";
    }

    public boolean isDisSenha() {
        if (usuario.getId() == -1) {
            disSenha = true;
        } else {
            disSenha = false;
        }
        return disSenha;
    }

    public void setDisSenha(boolean disSenha) {
        this.disSenha = disSenha;
    }

    public boolean isDisStrSenha() {
        if (usuario.getId() == -1) {
            disStrSenha = false;
        } else {
            disStrSenha = true;
        }
        return disStrSenha;
    }

    public void setDisStrSenha(boolean disStrSenha) {
        this.disStrSenha = disStrSenha;
    }

    public void habilitaNovaSenha() {
        if (!disNovaSenha) {
            disNovaSenha = true;
        } else {
            disNovaSenha = false;
        }
    }

    public boolean isDisNovaSenha() {
        return disNovaSenha;
    }

    public void setDisNovaSenha(boolean disNovaSenha) {
        this.disNovaSenha = disNovaSenha;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            usuario.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
            GenericaSessao.remove("usuarioPesquisa");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUserLogado() {
        return userLogado;
    }

    public void setUserLogado(String userLogado) {
        this.userLogado = userLogado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public List<PermissaoUsuario> getListaPermissaoUsuario() {
        PermissaoUsuarioDao db = new PermissaoUsuarioDao();
        if (usuario.getId() != -1 && !adicionado) {
            listaPermissaoUsuario = db.pesquisaListaPermissaoPorUsuario(usuario.getId());
        }
        return listaPermissaoUsuario;
    }

    public void setListaPermissaoUsuario(List<PermissaoUsuario> listaPermissaoUsuario) {
        this.listaPermissaoUsuario = listaPermissaoUsuario;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public List<SelectItem> getListaModulos() {
        if (listaModulos.isEmpty()) {
            PermissaoDao permissaoDao = new PermissaoDao();
            List<Modulo> modulos = permissaoDao.listaModuloPermissaoAgrupado();
            for (int i = 0; i < modulos.size(); i++) {
                listaModulos.add(new SelectItem(i,
                        modulos.get(i).getDescricao(),
                        Integer.toString(modulos.get(i).getId())));
            }
            listaRotinas.clear();
        }
        return listaModulos;
    }

    public void setListaModulos(List<SelectItem> listaModulos) {
        this.listaModulos = listaModulos;
    }

    public List<SelectItem> getListaRotinas() {
        if (listaRotinas.isEmpty() && !listaModulos.isEmpty()) {
            int idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
            PermissaoDao permissaoDao = new PermissaoDao();
            List<Rotina> rotinas = permissaoDao.listaRotinaPermissaoAgrupado(idM);
            idRotina = 0;
            for (int i = 0; i < rotinas.size(); i++) {
                listaRotinas.add(
                        new SelectItem(
                                i,
                                rotinas.get(i).getRotina(),
                                Integer.toString(rotinas.get(i).getId())));
            }
            listaEventos.clear();
        }
        return listaRotinas;
    }

    public void setListaRotinas(List<SelectItem> listaRotinas) {
        this.listaRotinas = listaRotinas;
    }

    public List<SelectItem> getListaEventos() {
        if (listaEventos.isEmpty() && !listaRotinas.isEmpty() && !listaModulos.isEmpty()) {
            PermissaoDao permissaoDao = new PermissaoDao();
            int idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
            int idR = Integer.parseInt(listaRotinas.get(idRotina).getDescription());
            List<Evento> eventos = permissaoDao.listaEventoPermissaoAgrupado(idM, idR);
            listaEventos.clear();
            for (int i = 0; i < eventos.size(); i++) {
                listaEventos.add(new SelectItem(i, eventos.get(i).getDescricao(), Integer.toString(eventos.get(i).getId())));
            }
        }
        return listaEventos;
    }

    public void setListaEventos(List<SelectItem> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public void addUsuarioAcesso() {
        PermissaoDao permissaoDao = new PermissaoDao();
        UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
        int idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
        int idR = Integer.parseInt(listaRotinas.get(idRotina).getDescription());
        int idE = Integer.parseInt(listaEventos.get(idEvento).getDescription());
        if (usuario.getId() != -1) {
            if (((UsuarioAcesso) (usuarioAcessoDao.pesquisaUsuarioAcessoModuloRotinaEvento(usuario.getId(), idM, idR, idE))).getId() == -1) {
                Permissao permissao = permissaoDao.pesquisaPermissaoModuloRotinaEvento(idM, idR, idE);
                UsuarioAcesso usuarioAcesso = new UsuarioAcesso();
                DaoInterface di = new Dao();
                usuarioAcesso.setUsuario(usuario);
                usuarioAcesso.setPermissao(permissao);
                di.openTransaction();
                usuarioAcesso.setPermite(true);
                if (di.save(usuarioAcesso)) {
                    di.commit();
                    NovoLog novoLog = new NovoLog();
                    novoLog.save("Usuário Acesso - ID: " + usuarioAcesso.getId() + " - Usuário (" + usuarioAcesso.getUsuario().getId() + ") " + usuarioAcesso.getUsuario().getLogin() + " - Permissão (" + usuarioAcesso.getPermissao().getId() + ") [Módulo: " + usuarioAcesso.getPermissao().getModulo().getDescricao() + " - Rotina: " + usuarioAcesso.getPermissao().getRotina().getRotina() + " - Evento: " + usuarioAcesso.getPermissao().getEvento().getDescricao() + "]");
                    GenericaMensagem.info("Sucesso", "Permissão adicionada");
                } else {
                    di.rollback();
                    GenericaMensagem.warn("Erro", "Erro ao adicionar permissão!");
                }
            } else {
                GenericaMensagem.warn("Sistema", "Permissão já existe!");
            }
        }
        listaUsuarioAcesso.clear();
    }

    public List<UsuarioAcesso> getListaUsuarioAcesso() {
        listaUsuarioAcesso.clear();
        if (usuario.getId() != -1) {
            PermissaoDao permissaoDao = new PermissaoDao();
            int idM = 0;
            int idR = 0;
            int idE = 0;
            if (filtrarPorModulo) {
                idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
            }
            if (filtrarPorRotina) {
                idR = Integer.parseInt(listaRotinas.get(idRotina).getDescription());
            }
            if (filtrarPorEvento) {
                idE = Integer.parseInt(listaEventos.get(idEvento).getDescription());
            }
            listaUsuarioAcesso = permissaoDao.listaUsuarioAcesso(usuario.getId(), idM, idR, idE);
            if (filtrarPorModulo || filtrarPorRotina || filtrarPorEvento) {
            }
        }
        return listaUsuarioAcesso;
    }

    public void limparListaUsuarioAcesso() {
        limparListaUsuarioAcessox();
        listaModulos.clear();
        getListaModulos();
        getListaRotinas();
        getListaEventos();
    }

    public void limparListaUsuarioAcessox() {
        listaUsuarioAcesso.clear();
    }

    public void setListaUsuarioAcesso(List<UsuarioAcesso> listaUsuarioAcesso) {
        this.listaUsuarioAcesso = listaUsuarioAcesso;
    }

    public void updateUsuarioAcesso(UsuarioAcesso ua) {
        if (ua.getId() == -1) {
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        NovoLog novoLog = new NovoLog();
        String beforeUpdate = "Usuário Acesso - ID: " + ua.getId() + " - Usuário (" + ua.getUsuario().getId() + ") " + ua.getUsuario().getLogin() + " - Permissão (" + ua.getPermissao().getId() + ") [Módulo: " + ua.getPermissao().getModulo().getDescricao() + " - Rotina: " + ua.getPermissao().getRotina().getRotina() + " - Evento: " + ua.getPermissao().getEvento().getDescricao() + "] - Permite:" + ua.isPermite();
        if (ua.isPermite()) {
            ua.setPermite(false);
        } else {
            ua.setPermite(true);
        }
        if (di.update(ua)) {
            novoLog.update(beforeUpdate, "Usuário Acesso - ID: " + ua.getId() + " - Usuário (" + ua.getUsuario().getId() + ") " + ua.getUsuario().getLogin() + " - Permissão (" + ua.getPermissao().getId() + ") [Módulo: " + ua.getPermissao().getModulo().getDescricao() + " - Rotina: " + ua.getPermissao().getRotina().getRotina() + " - Evento: " + ua.getPermissao().getEvento().getDescricao() + "] - Permite:" + ua.isPermite());
            di.commit();
            GenericaMensagem.info("Sucesso", "Permissão de acesso atualizada");
            listaUsuarioAcesso.clear();
        } else {
            GenericaMensagem.warn("Erro", "Falha ao atualizar essa permisão!");
            di.rollback();
        }
    }

    public void removeUsuarioAcesso(UsuarioAcesso ua) {
        if (ua.getId() == -1) {
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        if (di.delete(ua)) {
            di.commit();
            NovoLog novoLog = new NovoLog();
            novoLog.delete("Usuário Acesso - ID: " + ua.getId() + " - Usuário (" + ua.getUsuario().getId() + ") " + ua.getUsuario().getLogin() + " - Permissão (" + ua.getPermissao().getId() + ") [Módulo: " + ua.getPermissao().getModulo().getDescricao() + " - Rotina: " + ua.getPermissao().getRotina().getRotina() + " - Evento: " + ua.getPermissao().getEvento().getDescricao() + "]");
            listaUsuarioAcesso.clear();
            GenericaMensagem.info("Sucesso", "Permissão removida");
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Erro ao remover permissão!");
        }

    }

    public boolean isFiltrarPorModulo() {
        return filtrarPorModulo;
    }

    public void setFiltrarPorModulo(boolean filtrarPorModulo) {
        this.filtrarPorModulo = filtrarPorModulo;
    }

    public boolean isFiltrarPorRotina() {
        return filtrarPorRotina;
    }

    public void setFiltrarPorRotina(boolean filtrarPorRotina) {
        this.filtrarPorRotina = filtrarPorRotina;
    }

    public boolean isFiltrarPorEvento() {
        return filtrarPorEvento;
    }

    public void setFiltrarPorEvento(boolean filtrarPorEvento) {
        this.filtrarPorEvento = filtrarPorEvento;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getUsuarioPerfil() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return null;
    }

    public void salvarSenhaUsuarioPerfil() {
        if (usuario.getId() != -1) {
            if (usuario.getPessoa().getId() == 1) {
                mensagem = "Não é possível alterar a senha do administrador!";
                GenericaMensagem.warn("Validação", mensagem);
                return;
            }
            if (getSenhaNova().equals("")) {
                mensagem = "Campo senha não pode ser nulo!";
                GenericaMensagem.warn("Validação", mensagem);
                return;
            }
            if (getSenhaNova().length() > 6) {
                mensagem = "A senha deve ter no máximo 6 Caracteres!";
                GenericaMensagem.warn("Validação", mensagem);
                return;
            }
            DaoInterface di = new Dao();
            Usuario user = (Usuario) di.find(new Usuario(), usuario.getId());
            if (!user.getSenha().equals(senhaAntiga)) {
                mensagem = "Senha antiga incompativel!";
                GenericaMensagem.warn("Erro", mensagem);
                return;
            }
            usuario.setSenha(senhaNova);
            di.openTransaction();
            if (di.update(usuario)) {
                di.commit();
                setSenhaNova("");
                setSenhaAntiga("");
                mensagem = "Senha alterada";
                GenericaMensagem.info("Sucesso", mensagem);
                NovoLog novoLog = new NovoLog();
                novoLog.update("", "ID: " + usuario.getId() + " - Pessoa: " + usuario.getPessoa().getId() + " - " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin() + " - Ativo: " + usuario.getAtivo());
            } else {
                di.rollback();
                mensagem = "Não foi possível atualizar essa senha!";
            }
        }
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public boolean isFiltrarUsuarioAtivo() {
        return filtrarUsuarioAtivo;
    }

    public void setFiltrarUsuarioAtivo(boolean filtrarUsuarioAtivo) {
        this.filtrarUsuarioAtivo = filtrarUsuarioAtivo;
    }

    public String getCliente() {
        if (GenericaSessao.exists("sessaoCliente")) {
            return GenericaSessao.getString("sessaoCliente");
        }
        return "";
    }

    public void password() {

    }

    public void send() {
        Dao dao = new Dao();
        Mail mail = new Mail();
        mail.setEmail(
                new Email(
                        -1,
                        DataHoje.dataHoje(),
                        DataHoje.livre(new Date(), "HH:mm"),
                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                        (Rotina) dao.find(new Rotina(), 68),
                        null,
                        "Lembrete de Senha usuário: " + usuario.getLogin() + " - Cliente: " + GenericaSessao.getString("sessaoCliente"),
                        usuario.getLogin() + " - " + usuario.getSenha(),
                        false,
                        false
                )
        );
        List<EmailPessoa> emailPessoas = new ArrayList<>();
        EmailPessoa emailPessoa = new EmailPessoa();
        emailPessoa.setDestinatario("suporte@rtools.com.br");
        emailPessoa.setPessoa(null);
        emailPessoa.setRecebimento(null);
        emailPessoas.add(emailPessoa);
        mail.setEmailPessoas(emailPessoas);
        String[] string = mail.send();
        if (string[0].isEmpty()) {
            GenericaMensagem.warn("Validação", "Erro ao enviar mensagem!" + string[0]);
        } else {
            GenericaMensagem.info("Sucesso", "Email enviado com sucesso!");
        }
    }
}
