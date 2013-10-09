package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaAcademia;
import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaAcademiaDB {

    public boolean insert(MatriculaAcademia matriculaAcademia);

    public boolean update(MatriculaAcademia matriculaAcademia);

    public boolean delete(MatriculaAcademia matriculaAcademia);

    public EntityManager getEntityManager();

    public MatriculaAcademia pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaMatriculaAcademia(String desc, String por, String como);
}
