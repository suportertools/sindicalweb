package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Midia;
import java.util.List;
import javax.persistence.EntityManager;

public interface MidiaDB {

    public boolean insert(Midia midia);

    public boolean update(Midia midia);

    public boolean delete(Midia midia);

    public EntityManager getEntityManager();

    public Midia pesquisaCodigo(int id);

    public List pesquisaTodos();
}
