package br.com.rtools.agenda.db;

import br.com.rtools.agenda.Agenda;
import br.com.rtools.agenda.AgendaTelefone;
import java.util.List;
import javax.persistence.EntityManager;

public interface AgendaTelefoneDB {

    public EntityManager getEntityManager();

    public List pesquisaAgenda(String ddd, String descricaoPesquisa, String porPesquisa, String comoPesquisa, int idGrupoAgenda);
    
    public List pesquisaAgendaTelefone(String ddd, String descricaoPesquisa, String porPesquisa, String comoPesquisa, int idGrupoAgenda);

    public List listaAgendaTelefone(int idAgenda);

    public List pesquisaTodos();

    public Agenda agendaExiste(Agenda agenda);

    public AgendaTelefone agendaTelefoneExiste(AgendaTelefone agendaTelefone);

    public List DDDAgrupado();
}
