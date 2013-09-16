package br.com.rtools.escola.db;

import br.com.rtools.escola.Professor;
import java.util.List;
import javax.persistence.EntityManager;

public interface ProfessorDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public boolean existeProfessor(Professor professor);
}
