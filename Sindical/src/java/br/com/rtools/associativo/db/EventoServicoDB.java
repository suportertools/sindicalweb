package br.com.rtools.associativo.db;

import br.com.rtools.associativo.EventoServico;
import java.util.List;
import javax.persistence.EntityManager;

public interface EventoServicoDB {

    public boolean insert(EventoServico eventoServico);

    public boolean update(EventoServico eventoServico);

    public boolean delete(EventoServico eventoServico);

    public EntityManager getEntityManager();

    public EventoServico pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List listaEventoServico(int idAEvento);

    public EventoServico pesquisaPorEventoEServico(int idAEvento, int idServico);
}
