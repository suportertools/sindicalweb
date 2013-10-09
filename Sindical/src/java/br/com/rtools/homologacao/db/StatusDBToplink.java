package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Status;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class StatusDBToplink extends DB implements StatusDB {

    @Override
    public boolean insert(Status status) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(status);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Status status) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(status);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Status status) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(status);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select s from Status s");
            if (!qry.getResultList().isEmpty()) {
                if (!qry.getResultList().isEmpty()) {
                    return (qry.getResultList());
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public Status pesquisaCodigo(int id) {
        Status result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Status.pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = (Status) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return result;
    }
}
