package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Midia;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MidiaDBToplink extends DB implements MidiaDB{
    public boolean insert(Midia midia) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(midia);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Midia midia) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(midia);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Midia midia) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(midia);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Midia pesquisaCodigo(int id) {
        Midia result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Midia.pesquisaID");
            qry.setParameter("pid", id);
            result = (Midia) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select m from Midia m");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

}
