package br.com.rtools.escola.dao;

import br.com.rtools.escola.Professor;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ProfessorDao extends DB {

    public boolean existProfessor(Professor professor) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT P FROM Professor AS P WHERE P.professor.id = :idPessoa ");
            List list = qry.getResultList();
            qry.setParameter("idPessoa", professor.getProfessor().getId());
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
