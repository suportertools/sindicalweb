package br.com.rtools.escola.db;

import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TurmaDBToplink extends  DB implements TurmaDB{
    public boolean insert(Turma turma) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(turma);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Turma turma) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(turma);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Turma turma) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(turma);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Turma pesquisaCodigo(int id) {
        Turma result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Turma.pesquisaID");
            qry.setParameter("pid", id);
            result = (Turma) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select t from Turma t order by t.cursos.descricao");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

    public List<TurmaProfessor> listaTurmaProfessor(int idTurma){
        try{
            Query qry = getEntityManager().createQuery(
                     " select tp " +
                     "   from TurmaProfessor tp" +
                     "  where tp.turma.id = " + idTurma);
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
}


