package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Plano2;
import java.util.List;
import javax.persistence.EntityManager;

public interface Plano2DB {

    public boolean insert(Plano2 plano);

    public boolean update(Plano2 plano);

    public boolean delete(Plano2 plano);

    public EntityManager getEntityManager();

    public Plano2 pesquisaCodigo(int id);

    public List pesquisaTodos();
}
