package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AStatus;
import java.util.List;
import javax.persistence.EntityManager;

public interface AStatusDB {

    public boolean insert(AStatus aStatus);

    public boolean update(AStatus aStatus);

    public boolean delete(AStatus aStatus);

    public EntityManager getEntityManager();

    public AStatus pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosOrdenadosPorID();
}
