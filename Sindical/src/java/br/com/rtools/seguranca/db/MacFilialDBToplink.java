package br.com.rtools.seguranca.db;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.MacFilial;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MacFilialDBToplink extends DB implements MacFilialDB {

    public boolean insert(MacFilial macFilial) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(macFilial);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(MacFilial macFilial) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(macFilial);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(MacFilial macFilial) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(macFilial);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public MacFilial pesquisaCodigo(int id) {
        MacFilial result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MacFilial.pesquisaID");
            qry.setParameter("pid", id);
            result = (MacFilial) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select mf from MacFilial mf ");
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public MacFilial pesquisaMac(String mac) {
        try {
            Query qry = getEntityManager().createQuery("select mf from MacFilial mf where mf.mac like '" + mac + "'");
            if (!qry.getResultList().isEmpty()) {
                return (MacFilial) qry.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
