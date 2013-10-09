package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Plano3;
import java.util.List;
import javax.persistence.EntityManager;

public interface Plano3DB {

    public boolean insert(Plano3 plano);

    public boolean update(Plano3 plano);

    public boolean delete(Plano3 plano);

    public EntityManager getEntityManager();

    public Plano3 pesquisaCodigo(int id);

    public List pesquisaTodos();
}
