package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.AgendaGrupoUsuario;
import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AgendaGrupoUsuarioBean {

    private Usuario usuario;
    private List<AgendaGrupoUsuario> agendaGrupoUsuarios;
    private Integer[] indice;
    private List<SelectItem>[] listSelectItens;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        agendaGrupoUsuarios = new ArrayList<AgendaGrupoUsuario>();
        indice = new Integer[1];
        indice[0] = 0;
        listSelectItens = new ArrayList[1];
        listSelectItens[0] = new ArrayList<SelectItem>();
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
        if(usuario.getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar usuário!");
            return;
        }
        AgendaGrupoUsuario agp = new AgendaGrupoUsuario();
        if (listSelectItens[0].isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar grupos!");
            return;
        }
        DaoInterface dao = new Dao();
        agp.setGrupoAgenda((GrupoAgenda) dao.find(new GrupoAgenda(), Integer.parseInt(getListGrupoAgenda().get(indice[0]).getDescription())));
        agp.setUsuario(usuario);
        for (int i = 0; i < agendaGrupoUsuarios.size(); i++) {
            if (agp.getGrupoAgenda().getId() == agendaGrupoUsuarios.get(i).getGrupoAgenda().getId()
                    && agp.getUsuario().getId() == agendaGrupoUsuarios.get(i).getUsuario().getId()) {
                GenericaMensagem.warn("Validação", "Grupo e usuário ja cadastrado!");
                return;
            }
        }
        if (dao.save(agp, true)) {
            GenericaMensagem.info("Sucesso", "Registro adicionado");
            agendaGrupoUsuarios.clear();
        } else {
            GenericaMensagem.warn("Erro", "Ao adicionar!");
        }
    }

    public void remove(AgendaGrupoUsuario agp) {
        DaoInterface di = new Dao();
        if (di.delete(agp, true)) {
            GenericaMensagem.info("Sucesso", "Registro removido");
            agendaGrupoUsuarios.clear();
        } else {
            GenericaMensagem.warn("Erro", "Ao remover!");
        }
    }

    public List<AgendaGrupoUsuario> getAgendaGrupoUsuarios() {
        if (agendaGrupoUsuarios.isEmpty()) {
            if (!getListGrupoAgenda().isEmpty()) {
                DaoInterface di = new Dao();
                int id = Integer.parseInt(getListGrupoAgenda().get(indice[0]).getDescription());
                agendaGrupoUsuarios = (List<AgendaGrupoUsuario>) di.listQuery("AgendaGrupoUsuario", "findGrupoAgenda", new Object[]{id});
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

    public List<SelectItem> getListGrupoAgenda() {
        if (listSelectItens[0].isEmpty()) {
            DaoInterface dao = new Dao();
            List<GrupoAgenda> list = (List<GrupoAgenda>) dao.list("GrupoAgenda", true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItens[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (listSelectItens[0].isEmpty()) {
            listSelectItens[0] = new ArrayList<SelectItem>();
        }
        return listSelectItens[0];
    }

    public Integer[] getIndice() {
        return indice;
    }

    public void setIndice(Integer[] indice) {
        this.indice = indice;
    }
}
