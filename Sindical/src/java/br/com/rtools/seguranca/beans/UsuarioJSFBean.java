package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

public class UsuarioJSFBean {

    private Usuario usuario = new Usuario();
    private List<PermissaoUsuario> listaPermissaoUsuario = new ArrayList();
    private List<SelectItem> listaModulos = new ArrayList<SelectItem>();
    private List<SelectItem> listaRotinas = new ArrayList<SelectItem>();
    private List<SelectItem> listaEventos = new ArrayList<SelectItem>();
    private List<SelectItem> listaDepartamentos = new ArrayList<SelectItem>();
    private List<SelectItem> listaNiveis = new ArrayList<SelectItem>();
    private List<Usuario> listaUsuario = new ArrayList();
    private List<UsuarioAcesso> listaUsuarioAcesso = new ArrayList();
    private String userLogado;
    private String msgConfirma;
    private String msgPermissao;
    private String msgUsuarioAcesso = "";
    private String senhaAntiga = "";
    private String confirmaSenha = "";
    private boolean disSenha = false;
    private boolean disNovaSenha = false;
    private boolean disStrSenha = true;
    private boolean adicionado = false;
    private int idIndex = -1;
    private int idIndexPermissao = -1;
    private int idIndexUsuarioAcesso = -1;
    private int idDepartamento;
    private int idNivel;
    private int idModulo = 0;
    private int idRotina = 0;
    private int idEvento = 0;
    private boolean filtrarPorModulo = false;
    private boolean filtrarPorRotina = false;
    private boolean filtrarPorEvento = false;
    private String descricaoPesquisa = "";

    public UsuarioJSFBean() {
        listaUsuario = new ArrayList<Usuario>();
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        UsuarioDB db = new UsuarioDBToplink();

        if (usuario.getLogin().equals("")) {
            msgConfirma = "Campo login não pode ser nulo!";
            return null;
        }

        if (usuario.getSenha().equals("")) {
            msgConfirma = "Campo senha não pode ser nulo!";
            return null;
        }
        if (usuario.getSenha().length() > 6) {
            msgConfirma = "A senha deve ter no máximo 6 Caracteres!";
            return null;
        }

        if (usuario.getId() == -1) {
            if (usuario.getPessoa().getId() == -1) {
                msgConfirma = "Pesquise um nome de Usuário disponível!";
                return null;
            }

            if (!usuario.getSenha().equals(confirmaSenha)) {
                msgConfirma = "Senhas não correspondem!";
                return null;
            }

            if (db.pesquisaLogin(usuario.getLogin(), usuario.getPessoa().getId()).isEmpty()) {
                if (sv.inserirObjeto(usuario)) {
                    sv.comitarTransacao();
                    msgConfirma = "Login e senha salvos com Sucesso!";
                    return null;
                } else {                    
                    msgConfirma = "Erro ao Salvar Login e Senha!";
                    sv.desfazerTransacao();
                    return null;
                }
            } else {
                msgConfirma = "Este login já existe no Sistema.";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            Usuario user = (Usuario) db.pesquisaCodigo(usuario.getId());
            if (disNovaSenha) {
                if (user.getSenha().equals(senhaAntiga) && !usuario.getSenha().equals("")) {
                } else {
                    usuario.setSenha(user.getSenha());
                    msgConfirma = "Senha Incompativel!";
                }
            } else {
                usuario.setSenha(user.getSenha());
            }
            if (sv.alterarObjeto(usuario)) {
                sv.comitarTransacao();
                msgConfirma = "Login e senha salvos com Sucesso!";
                return null;
            } else {
                msgConfirma = "Erro ao atualizar Usuario!";
                sv.desfazerTransacao();
                return null;
            }
        }
    }

    public String adicionarUsuarioPermissao() {
        if (usuario.getId() == -1) {
            return null;
        }
        PermissaoUsuario pu = new PermissaoUsuario();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        pu.setDepartamento( (Departamento) salvarAcumuladoDB.pesquisaCodigo(Integer.valueOf(listaDepartamentos.get(idDepartamento).getDescription()), "Departamento"));
        pu.setNivel( (Nivel) salvarAcumuladoDB.pesquisaCodigo(Integer.valueOf(listaNiveis.get(idNivel).getDescription()), "Nivel"));
        pu.setUsuario(usuario);
        PermissaoUsuarioDB permissaoUsuarioDB = new PermissaoUsuarioDBToplink();        
        if (permissaoUsuarioDB.existePermissaoUsuario(pu)) {
            // msgPermissao = "Permissão já Existente!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Permissão já existe"));
            return null;
        }
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(pu)) {
            salvarAcumuladoDB.comitarTransacao();
            // msgPermissao = "Permissão adicionada com sucesso.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão adicionada"));
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            // msgPermissao = "Erro ao adicionar essa permissão!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao adicionar essa permissão!"));
        }
        idDepartamento = 0;
        idNivel = 0;
        return null;
    }

    public String removerPermissaoUsuario(PermissaoUsuario pu) {
        msgPermissao = "";
        if (pu.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();        
            pu = (PermissaoUsuario) salvarAcumuladoDB.pesquisaCodigo(pu.getId(), "PermissaoUsuario");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(pu)) {
                salvarAcumuladoDB.comitarTransacao();
                // msgPermissao = "Permissão removida com sucesso.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão removida"));
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                // msgPermissao = "Erro ao Excluír permissão!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao Excluír permissão!"));
                return null;
            }
        }
        return null;
    }

    public boolean excluirPermissoes(SalvarAcumuladoDB sv) {
        PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
        List<PermissaoDepartamento> lista = new ArrayList();
        UsuarioAcesso ua = new UsuarioAcesso();
        for (int i = 0; i < listaPermissaoUsuario.size(); i++) {
            PermissaoUsuario perUsuario = listaPermissaoUsuario.get(i);
            perUsuario = db.pesquisaPermissaoUsuario(perUsuario.getUsuario().getId(), perUsuario.getDepartamento().getId(), perUsuario.getNivel().getId());

            lista = db.pesquisaPDepartamento(perUsuario.getDepartamento().getId(), perUsuario.getNivel().getId());
            for (int w = 0; w < lista.size(); w++) {
                ua = db.pesquisaUsuarioAcesso(perUsuario.getUsuario().getId(), lista.get(w).getPermissao().getId());

                if (!sv.deletarObjeto(sv.pesquisaCodigo(ua.getId(), "UsuarioAcesso"))) {
                    sv.desfazerTransacao();
                    return false;
                }
            }

            if (!sv.deletarObjeto((PermissaoUsuario) sv.pesquisaCodigo(perUsuario.getId(), "PermissaoUsuario"))) {
                sv.desfazerTransacao();
                return false;
            } else {
                return true;
            }
            //perUsuario = new PermissaoUsuario();
        }
        return true;
    }

    public String novo() {
        usuario = new Usuario();
        disNovaSenha = false;
        msgPermissao = "";
        idDepartamento = 0;
        idNivel = 0;
        idIndex = -1;
        idIndexPermissao = -1;
        adicionado = false;
        confirmaSenha = "";
        listaUsuario.clear();
        listaPermissaoUsuario.clear();
        listaUsuarioAcesso.clear();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        return null;
    }

    public void novoGenerico() {
        usuario = new Usuario();
        disNovaSenha = false;
        msgPermissao = "";
        idDepartamento = 0;
        idNivel = 0;        
        idIndex = -1;
        idIndexPermissao = -1;
        adicionado = false;
        confirmaSenha = "";
        listaUsuarioAcesso.clear();
        listaPermissaoUsuario.clear();
        listaUsuario.clear();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (usuario.getId() != -1) {
            if (!excluirPermissoes(sv)) {
                msgConfirma = "Erro ao excluir Permissões!";
                return null;
            }

            usuario = (Usuario) sv.pesquisaCodigo(usuario.getId(), "Usuario");
            if (!sv.deletarObjeto(usuario)) {
                msgConfirma = "Login não pode ser  Excluido!";
                sv.desfazerTransacao();
            } else {
                msgConfirma = "Login excluido com Sucesso!";
                sv.comitarTransacao();
            }
        }
        novoGenerico();
        return null;
    }

    public String editar(Usuario usu) {
        usuario = usu;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", usuario.getPessoa());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);        
        return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public List<Usuario> getListaUsuario() {
        if (listaUsuario.isEmpty()) {
            UsuarioDB db = new UsuarioDBToplink();
            if (descricaoPesquisa.isEmpty()) {
                listaUsuario = db.pesquisaTodos();
            } else {
                listaUsuario = db.pesquisaTodosPorDescricao(descricaoPesquisa);
            }
        }
        return listaUsuario;
    }
    
    public void limparListaUsuario(){
        listaUsuario.clear();
    }

    public List<SelectItem> getListaNiveis() {
        if (listaNiveis.isEmpty()) {
            PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
            List niveis = db.pesquisaTodosNiveis();
            if (!niveis.isEmpty()) {
                for (int i = 0; i < niveis.size(); i++) {
                    listaNiveis.add(new SelectItem(new Integer(i),
                            ((Nivel) niveis.get(i)).getDescricao(),
                            Integer.toString(((Nivel) niveis.get(i)).getId())));
                }
            }
        }
        return listaNiveis;
    }

    public List<SelectItem> getListaDepartamentos() {
        if (listaDepartamentos.isEmpty()) {
            PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
            List departamentos = db.pesquisaTodosDepOrdenado();
            for (int i = 0; i < departamentos.size(); i++) {
                listaDepartamentos.add(new SelectItem(new Integer(i),
                        ((Departamento) departamentos.get(i)).getDescricao(),
                        Integer.toString(((Departamento) departamentos.get(i)).getId())));
            }
        }
        return listaDepartamentos;
    }

    public void refreshForm() {
    }

    public void sairSistema() throws IOException {
        String retorno = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
            retorno = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") + "/";
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
                SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
                MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
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

    public String habilitaNovaSenha() {
        if (!disNovaSenha) {
            disNovaSenha = true;
        } else {
            disNovaSenha = false;
        }
        return null;
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
        if ((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            usuario.setPessoa((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getMsgPermissao() {
        return msgPermissao;
    }

    public void setMsgPermissao(String msgPermissao) {
        this.msgPermissao = msgPermissao;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<PermissaoUsuario> getListaPermissaoUsuario() {
        PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
        if (usuario.getId() != -1 && !adicionado) {
            listaPermissaoUsuario = db.pesquisaListaPermissaoPorUsuario(usuario.getId());
        }
        return listaPermissaoUsuario;
    }

    public void setListaPermissaoUsuario(List<PermissaoUsuario> listaPermissaoUsuario) {
        this.listaPermissaoUsuario = listaPermissaoUsuario;
    }

    public int getIdIndexPermissao() {
        return idIndexPermissao;
    }

    public void setIdIndexPermissao(int idIndexPermissao) {
        this.idIndexPermissao = idIndexPermissao;
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
        if(listaModulos.isEmpty()){
            PermissaoDB db = new PermissaoDBToplink();
            List<Modulo> modulos = db.listaModuloPermissaoAgrupado();
            for (int i = 0; i < modulos.size(); i++) {
                listaModulos.add(new SelectItem(new Integer(i),
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
        if (listaRotinas.isEmpty() && !listaModulos.isEmpty()){
            int idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
            PermissaoDB db = new PermissaoDBToplink();
            List<Rotina> rotinas = db.listaRotinaPermissaoAgrupado(idM);
            idRotina = 0;
            for (int i = 0; i < rotinas.size(); i++) {
                listaRotinas.add(
                        new SelectItem(
                        new Integer(i),
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
        if (listaEventos.isEmpty() && !listaRotinas.isEmpty() && !listaModulos.isEmpty()){
            PermissaoDB db = new PermissaoDBToplink();
            int idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
            int idR = Integer.parseInt(listaRotinas.get(idRotina).getDescription());
            List<Evento> eventos = db.listaEventoPermissaoAgrupado(idM, idR);
            listaEventos.clear();
            for (int i = 0; i < eventos.size(); i++) {
                listaEventos.add(
                        new SelectItem(
                        new Integer(i),
                        eventos.get(i).getDescricao(),
                        Integer.toString(eventos.get(i).getId())));
            }
        }
        return listaEventos;
    }

    public void setListaEventos(List<SelectItem> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public void adicionarUsuarioAcesso() {
        msgUsuarioAcesso = "";
        PermissaoDB db = new PermissaoDBToplink();
        int idM = Integer.parseInt(listaModulos.get(idModulo).getDescription());
        int idR = Integer.parseInt(listaRotinas.get(idRotina).getDescription());
        int idE = Integer.parseInt(listaEventos.get(idEvento).getDescription());
        if (usuario.getId() != -1) {
            if (((UsuarioAcesso) (db.pesquisaUsuarioAcessoModuloRotinaEvento(usuario.getId(), idM, idR, idE))).getId() == -1) {
                Permissao permissao = db.pesquisaPermissaoModuloRotinaEvento(idM, idR, idE);
                UsuarioAcesso usuarioAcesso = new UsuarioAcesso();
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                usuarioAcesso.setUsuario(usuario);
                usuarioAcesso.setPermissao(permissao);
                salvarAcumuladoDB.abrirTransacao();
                usuarioAcesso.setPermite(true);
                if (salvarAcumuladoDB.inserirObjeto(usuarioAcesso)) {
                    salvarAcumuladoDB.comitarTransacao();
//                    NovoLog log = new NovoLog();
//                    log.novo(userLogado, userLogado);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão adicionada"));
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                    // msgUsuarioAcesso = "Erro ao adicionar Permissão!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao adicionar permissão!"));
                }
            } else {
                // msgUsuarioAcesso = "Permissão já existe!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Permissão já existe!"));
            }
        }
        listaUsuarioAcesso.clear();
    }

    public List<UsuarioAcesso> getListaUsuarioAcesso() {
        listaUsuarioAcesso.clear();
        if (usuario.getId() != -1) {
            PermissaoDB db = new PermissaoDBToplink();
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
            listaUsuarioAcesso = db.listaUsuarioAcesso(usuario.getId(), idM, idR, idE);
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

    public int getIdIndexUsuarioAcesso() {
        return idIndexUsuarioAcesso;
    }

    public int getIdIndexUsuarioAcessoToId() {
        if (!listaUsuarioAcesso.isEmpty()) {
            return listaUsuarioAcesso.get(idIndex).getId();
        }
        return -1;
    }

    public void setIdIndexUsuarioAcesso(int idIndexUsuarioAcesso) {
        this.idIndexUsuarioAcesso = idIndexUsuarioAcesso;
    }

    public String permiteUsuarioAcesso(UsuarioAcesso ua) {
        if (ua.getId() == -1) {
            return null;
        }
//        if (ua.isPermite()) {
//            ua.setPermite(false);
//        } else {
//            ua.setPermite(true);            
//        }
        msgUsuarioAcesso = "";
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.alterarObjeto(ua)) {
            salvarAcumuladoDB.comitarTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão de acesso atualizada"));
            listaUsuarioAcesso.clear();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Falha ao atualizar essa permisão!"));
            salvarAcumuladoDB.desfazerTransacao();
        }
//        for (int i = 0; i < listaUsuarioAcesso.size(); i++) {
//            if (listaUsuarioAcesso.get(i).getId() == ua.getId()) {
//                listaUsuarioAcesso.get(i).setPermite(ua.isPermite());
//                break;
//            }
//        }
        return null;
    }

    public String btnExcluirUsuarioAcesso(UsuarioAcesso ua) {
        if (ua.getId() == -1) {
            return null;
        }
        msgUsuarioAcesso = "";
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        UsuarioAcesso usuarioAcesso = (UsuarioAcesso) salvarAcumuladoDB.pesquisaCodigo(ua.getId(), "UsuarioAcesso");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(usuarioAcesso)) {
            salvarAcumuladoDB.comitarTransacao();
            listaUsuarioAcesso.clear();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão removida"));
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            // msgUsuarioAcesso = "Erro ao remover permissão!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao remover permissão!"));
        }
        return null;

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

    public String getMsgUsuarioAcesso() {
        return msgUsuarioAcesso;
    }

    public void setMsgUsuarioAcesso(String msgUsuarioAcesso) {
        this.msgUsuarioAcesso = msgUsuarioAcesso;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }
}