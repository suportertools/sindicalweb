package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Caravana;
import java.util.List;
import javax.persistence.EntityManager;

public interface CaravanaDB {

    public EntityManager getEntityManager();

    public Caravana pesquisaCodigo(int id);

    public List pesquisaTodos();
}
