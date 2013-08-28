package br.com.rtools.escola.db;

import br.com.rtools.escola.Professor;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ProfessorDBToplink extends DB implements ProfessorDB{
    public boolean insert(Professor professor) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(professor);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Professor professor) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(professor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Professor professor) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(professor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Professor pesquisaCodigo(int id) {
        Professor result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Professor.pesquisaID");
            qry.setParameter("pid", id);
            result = (Professor) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select p from Professor p order by p.professor.nome");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
}
