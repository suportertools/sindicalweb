package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Suspencao;
import java.util.List;
import javax.persistence.EntityManager;

public interface SuspencaoDB {
    public boolean insert(Suspencao suspencao);
    public boolean update(Suspencao suspencao);
    public boolean delete(Suspencao suspencao);
    public EntityManager getEntityManager();
    public Suspencao pesquisaCodigo(int id);
    public List pesquisaTodos();
}
