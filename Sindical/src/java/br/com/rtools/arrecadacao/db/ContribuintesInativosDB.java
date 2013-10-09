package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ContribuintesInativos;
import java.util.List;
import javax.persistence.EntityManager;

public interface ContribuintesInativosDB {

    public boolean insert(ContribuintesInativos contribuintesInativos);

    public boolean update(ContribuintesInativos contribuintesInativos);

    public boolean delete(ContribuintesInativos contribuintesInativos);

    public EntityManager getEntityManager();

    public ContribuintesInativos pesquisaCodigo(int id);

    public List pesquisaTodos();

    public ContribuintesInativos pesquisaContribuintesInativos(int id);

    public List listaContribuintesInativos(int id);
}
