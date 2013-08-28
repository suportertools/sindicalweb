package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Status;
import java.util.List;
import javax.persistence.EntityManager;

public interface StatusDB {
    public EntityManager getEntityManager();
    public boolean insert(Status status);
    public boolean update(Status status);
    public boolean delete(Status status);
    public List pesquisaTodos();
    public Status pesquisaCodigo(int id);
}
