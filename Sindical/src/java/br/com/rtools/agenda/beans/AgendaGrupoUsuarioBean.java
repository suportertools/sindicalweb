package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.AgendaGrupoUsuario;
import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AgendaGrupoUsuarioBean implements Serializable {

    private Usuario usuario;
    private List<AgendaGrupoUsuario> agendaGrupoUsuarios;
    private List<GrupoAgenda> listGrupoAgenda;
    private GrupoAgenda grupoAgenda;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        agendaGrupoUsuarios = new ArrayList<AgendaGrupoUsuario>();
        listGrupoAgenda = new ArrayList<GrupoAgenda>();
        grupoAgenda = new GrupoAgenda();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("agendaGrupoUsuarioBean");
        GenericaSessao.remove("usuarioPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("agendaGrupoUsuarioBean");
    }

    public void save() {
        if (usuario.getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar usuário!");
            return;
        }
        AgendaGrupoUsuario agp = new AgendaGrupoUsuario();
        if (listGrupoAgenda.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar grupos!");
            return;
        }
        DaoInterface dao = new Dao();
        agp.setGrupoAgenda(grupoAgenda);
        agp.setUsuario(usuario);
        for (int i = 0; i < agendaGrupoUsuarios.size(); i++) {
            if (agp.getGrupoAgenda().getId() == agendaGrupoUsuarios.get(i).getGrupoAgenda().getId()
                    && agp.getUsuario().getId() == agendaGrupoUsuarios.get(i).getUsuario().getId()) {
                GenericaMensagem.warn("Validação", "Grupo e usuário ja cadastrado!");
                return;
            }
        }
        NovoLog novoLog = new NovoLog();
        if (dao.save(agp, true)) {
            novoLog.save("ID: " + agp.getId() + " - Grupo Agenda: " + agp.getGrupoAgenda().getDescricao() + " - Usuário: (" + agp.getUsuario().getId() + ") " + agp.getUsuario().getLogin());
            GenericaMensagem.info("Sucesso", "Registro adicionado");
            GenericaSessao.remove("agendaTelefoneBean");
            agendaGrupoUsuarios.clear();
        } else {
            GenericaMensagem.warn("Erro", "Ao adicionar!");
        }
    }

    public void remove(AgendaGrupoUsuario agp) {
        DaoInterface di = new Dao();
        if (di.delete(agp, true)) {
            NovoLog novoLog = new NovoLog();
            novoLog.delete("ID: " + agp.getId() + " - Grupo Agenda: " + agp.getGrupoAgenda().getDescricao() + " - Usuário: (" + agp.getUsuario().getId() + ") " + agp.getUsuario().getLogin());
            GenericaMensagem.info("Sucesso", "Registro removido");
            GenericaSessao.remove("agendaTelefoneBean");
            agendaGrupoUsuarios.clear();
        } else {
            GenericaMensagem.warn("Erro", "Ao remover!");
        }
    }

    public List<AgendaGrupoUsuario> getAgendaGrupoUsuarios() {
        if (agendaGrupoUsuarios.isEmpty()) {
            if (!getListGrupoAgenda().isEmpty()) {
                DaoInterface di = new Dao();
                if (grupoAgenda != null) {
                    if (grupoAgenda.getId() != -1) {
                        agendaGrupoUsuarios = (List<AgendaGrupoUsuario>) di.listQuery("AgendaGrupoUsuario", "findGrupoAgenda", new Object[]{grupoAgenda.getId()});
                    }
                }
            }
        }
        return agendaGrupoUsuarios;
    }

    public void setAgendaGrupoUsuarios(List<AgendaGrupoUsuario> agendaGrupoUsuarios) {
        this.agendaGrupoUsuarios = agendaGrupoUsuarios;
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("usuarioPesquisa")) {
            usuario = (Usuario) GenericaSessao.getObject("usuarioPesquisa", true);
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<GrupoAgenda> getListGrupoAgenda() {
        if (listGrupoAgenda.isEmpty()) {
            DaoInterface di = new Dao();
            listGrupoAgenda = di.list(new GrupoAgenda(), true);
            if(listGrupoAgenda.isEmpty()) {
                grupoAgenda = listGrupoAgenda.get(0);
            }
        }
        return listGrupoAgenda;
    }

    public void setListGrupoAgenda(List<GrupoAgenda> listGrupoAgenda) {
        this.listGrupoAgenda = listGrupoAgenda;
    }

    public GrupoAgenda getGrupoAgenda() {
        return grupoAgenda;
    }

    public void setGrupoAgenda(GrupoAgenda grupoAgenda) {
        this.grupoAgenda = grupoAgenda;
    }
}
