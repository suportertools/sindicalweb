package br.com.rtools.escola.db;

import br.com.rtools.escola.Professor;
import java.util.List;
import javax.persistence.EntityManager;

public interface ProfessorDB {
    public boolean insert(Professor professor);
    public boolean update(Professor professor);
    public boolean delete(Professor professor);
    public EntityManager getEntityManager();
    public Professor pesquisaCodigo(int id);
    public List pesquisaTodos();
}
