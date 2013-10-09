package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class MatriculaSociosDBToplink extends DB implements MatriculaSociosDB {

    public boolean insert(MatriculaSocios matriculaSocios) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(matriculaSocios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(MatriculaSocios matriculaSocios) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(matriculaSocios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(MatriculaSocios matriculaSocios) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(matriculaSocios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public MatriculaSocios pesquisaCodigo(int id) {
        MatriculaSocios result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MatriculaSocios.pesquisaID");
            qry.setParameter("pid", id);
            result = (MatriculaSocios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public MatriculaSocios pesquisaPorNrMatricula(int idGpCategoria, int nrMatricula) {
        MatriculaSocios result = null;
        try {
            Query qry = getEntityManager().createQuery("select m from MatriculaSocios s"
                    + " where m.grupoCategoria.id = " + idGpCategoria
                    + "   and m.nrMatricula = " + nrMatricula);
            result = (MatriculaSocios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select ms from MatriculaSocios ms");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
