package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaSocios;
import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaSociosDB {

    public boolean insert(MatriculaSocios matriculaSocios);

    public boolean update(MatriculaSocios matriculaSocios);

    public boolean delete(MatriculaSocios matriculaSocios);

    public EntityManager getEntityManager();

    public MatriculaSocios pesquisaCodigo(int id);

    public List pesquisaTodos();

    public MatriculaSocios pesquisaPorNrMatricula(int idGpCategoria, int nrMatricula);
}
