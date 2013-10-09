package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ContribuintesInativos;
import br.com.rtools.principal.DB;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ContribuintesInativosDBToplink extends DB implements ContribuintesInativosDB {

    @Override
    public boolean insert(ContribuintesInativos contribuintesInativos) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(contribuintesInativos);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(ContribuintesInativos contribuintesInativos) {
        try {
            getEntityManager().merge(contribuintesInativos);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(ContribuintesInativos contribuintesInativos) {
        try {
            getEntityManager().remove(contribuintesInativos);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ContribuintesInativos pesquisaCodigo(int id) {
        ContribuintesInativos result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ContribuintesInativos.pesquisaID");
            qry.setParameter("pid", id);
            result = (ContribuintesInativos) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from ContribuintesInativos cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ContribuintesInativos pesquisaContribuintesInativos(int id) {
        ContribuintesInativos result = new ContribuintesInativos();
        try {
//            Query qry = getEntityManager().createQuery(
//                    " select ci " +
//                    "  from ContribuintesInativos ci " +
//                    " where ci.juridica.id = :pid " +
//                    "  and   ci.dtAtivacao is null");
//            qry.setParameter("pid", id);
//            //result = (ContribuintesInativos) qry.getSingleResult();
//            result = (ContribuintesInativos) qry.getSingleResult();
            Query qry = getEntityManager().createNativeQuery("select id from arr_contribuintes_inativos where id_juridica = " + id + " and dt_ativacao is null");
            List vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                result = pesquisaCodigo((Integer) ((Vector) vetor.get(0)).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContribuintesInativos();
        }
        return result;
    }

    @Override
    public List listaContribuintesInativos(int id) {
        List result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select ci"
                    + "  from ContribuintesInativos ci "
                    + " where ci.juridica.id = :pid ");// +
            //" and   ci.dtAtivacao is null");
            qry.setParameter("pid", id);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
}