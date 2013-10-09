package br.com.rtools.associativo.db;

import br.com.rtools.associativo.EventoServicoValor;
import java.util.List;
import javax.persistence.EntityManager;

public interface EventoServicoValorDB {

    public boolean insert(EventoServicoValor eventoServicoValor);

    public boolean update(EventoServicoValor eventoServicoValor);

    public boolean delete(EventoServicoValor eventoServicoValor);

    public EntityManager getEntityManager();

    public EventoServicoValor pesquisaCodigo(int id);

    public List pesquisaTodos();

    public EventoServicoValor pesquisaEventoServicoValor(int idEventoServico);

    public List<EventoServicoValor> pesquisaServicoValorPorEvento(int idEvento);
}
