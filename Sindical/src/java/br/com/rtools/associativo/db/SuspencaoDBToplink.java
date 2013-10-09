package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Suspencao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class SuspencaoDBToplink extends DB implements SuspencaoDB {

    public boolean insert(Suspencao suspencao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(suspencao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Suspencao suspencao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(suspencao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Suspencao suspencao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(suspencao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Suspencao pesquisaCodigo(int id) {
        Suspencao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Suspencao.pesquisaID");
            qry.setParameter("pid", id);
            result = (Suspencao) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select s from Suspencao s");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
