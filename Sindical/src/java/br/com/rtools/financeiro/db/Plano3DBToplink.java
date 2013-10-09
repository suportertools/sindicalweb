package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import br.com.rtools.financeiro.Plano3;
import java.util.List;
import javax.persistence.Query;

public class Plano3DBToplink extends DB implements Plano3DB {

    public boolean insert(Plano3 plano) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Plano3 plano) {
        try {
            getEntityManager().merge(plano);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Plano3 plano) {
        try {
            getEntityManager().remove(plano);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Plano3 pesquisaCodigo(int id) {
        Plano3 result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Plano3.pesquisaID");
            qry.setParameter("pid", id);
            result = (Plano3) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Plano3 p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }
}
