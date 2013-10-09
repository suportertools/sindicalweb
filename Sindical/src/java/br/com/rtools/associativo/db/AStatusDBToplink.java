package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AStatus;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class AStatusDBToplink extends DB implements AStatusDB {

    public boolean insert(AStatus aStatus) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(aStatus);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(AStatus aStatus) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(aStatus);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(AStatus aStatus) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(aStatus);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public AStatus pesquisaCodigo(int id) {
        AStatus result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("AStatus.pesquisaID");
            qry.setParameter("pid", id);
            result = (AStatus) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from AStatus r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public List pesquisaTodosOrdenadosPorID() {
        try {
            Query qry = getEntityManager().createQuery("select r from AStatus r order by r.id");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
