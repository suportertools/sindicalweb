package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Plano4;
import java.util.List;
import javax.persistence.EntityManager;

public interface Plano4DB {

    public boolean insert(Plano4 plano4);

    public boolean update(Plano4 plano4);

    public boolean delete(Plano4 plano4);

    public EntityManager getEntityManager();

    public Plano4 pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodasStrings();
}
