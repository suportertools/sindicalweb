package br.com.rtools.suporte.db;

import br.com.rtools.principal.DB;
import br.com.rtools.suporte.ProStatus;
import java.util.List;
import javax.persistence.Query;

public class ProStatusDBToplink extends DB implements ProStatusDB {

    public boolean insert(ProStatus proStatus) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(proStatus);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(ProStatus proStatus) {
        try {
            getEntityManager().merge(proStatus);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(ProStatus proStatus) {
        try {
            getEntityManager().remove(proStatus);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ProStatus pesquisaCodigo(int id) {
        ProStatus result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ProStatus.pesquisaID");
            qry.setParameter("pid", id);
            result = (ProStatus) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select ps from ProStatus ps order by ps.status asc");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }
}
