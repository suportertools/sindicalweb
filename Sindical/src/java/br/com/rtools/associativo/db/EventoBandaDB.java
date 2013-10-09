package br.com.rtools.associativo.db;

import br.com.rtools.associativo.EventoBanda;
import java.util.List;
import javax.persistence.EntityManager;

public interface EventoBandaDB {

    public boolean insert(EventoBanda eventoBanda);

    public boolean update(EventoBanda eventoBanda);

    public boolean delete(EventoBanda eventoBanda);

    public EntityManager getEntityManager();

    public EventoBanda pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<EventoBanda> pesquisaBandasDoEvento(int idEvento);
}
