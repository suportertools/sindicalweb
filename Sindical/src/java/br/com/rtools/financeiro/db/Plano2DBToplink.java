package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import br.com.rtools.financeiro.Plano2;
import java.util.List;
import javax.persistence.Query;

public class Plano2DBToplink extends DB implements Plano2DB {

    public boolean insert(Plano2 plano2) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano2);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Plano2 plano2) {
        try {
            getEntityManager().merge(plano2);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Plano2 plano2) {
        try {
            getEntityManager().remove(plano2);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Plano2 pesquisaCodigo(int id) {
        Plano2 result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Plano2.pesquisaID");
            qry.setParameter("pid", id);
            result = (Plano2) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Plano2 p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }
}
