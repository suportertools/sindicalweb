package br.com.rtools.escola.db;

import br.com.rtools.escola.Professor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ProfessorDBToplink extends DB implements ProfessorDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("SELECT P FROM Professor AS P ORDER BY P.professor.nome");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public boolean existeProfessor(Professor professor) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT P FROM Professor AS P WHERE P.professor.id :idPessoa ");
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
