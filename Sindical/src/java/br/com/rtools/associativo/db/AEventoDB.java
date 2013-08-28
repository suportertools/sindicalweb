package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.EventoBaile;
import java.util.List;
import javax.persistence.EntityManager;

public interface AEventoDB {
    public boolean insert(AEvento aEvento);
    public boolean update(AEvento aEvento);
    public boolean delete(AEvento aEvento);
    public EntityManager getEntityManager();
    public AEvento pesquisaCodigo(int id);
    public List pesquisaTodos();
    public AEvento pesquisaEvento(EventoBaile eventoBaile);
}
