package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ServicoCategoria;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ServicoCategoriaDBToplink extends DB implements ServicoCategoriaDB {

    @Override
    public boolean insert(ServicoCategoria servicoCategoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(servicoCategoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(ServicoCategoria servicoCategoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(servicoCategoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(ServicoCategoria servicoCategoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(servicoCategoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public ServicoCategoria pesquisaCodigo(int id) {
        ServicoCategoria result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ServicoCategoria.pesquisaID");
            qry.setParameter("pid", id);
            result = (ServicoCategoria) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select sc from ServicoCategoria sc");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaServCatPorId(int idCategoria) {
        try {
            Query qry = getEntityManager().createQuery("select sc "
                    + "  from ServicoCategoria sc"
                    + " where sc.categoria.id = " + idCategoria
                    + " order by sc.parentesco.id");
            return (qry.getResultList());
        } catch (EJBQLException e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public ServicoCategoria pesquisaPorParECat(int idParentesco, int idCategoria) {
        ServicoCategoria result = null;
        try {
            Query qry = getEntityManager().createQuery("select sc from ServicoCategoria sc "
                    + " where sc.categoria.id = " + idCategoria + " "
                    + "   and sc.parentesco.id = " + idParentesco);
            result = (ServicoCategoria) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}
