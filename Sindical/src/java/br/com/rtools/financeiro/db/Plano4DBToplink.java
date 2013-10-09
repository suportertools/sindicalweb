package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import br.com.rtools.financeiro.Plano4;
import java.util.List;
import javax.persistence.Query;

public class Plano4DBToplink extends DB implements Plano4DB {

    public boolean insert(Plano4 plano4) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano4);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Plano4 plano4) {
        try {
            getEntityManager().merge(plano4);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Plano4 plano4) {
        try {
            getEntityManager().remove(plano4);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Plano4 pesquisaCodigo(int id) {
        Plano4 result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Plano4.pesquisaID");
            qry.setParameter("pid", id);
            result = (Plano4) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Plano4 p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaTodasStrings() {
        try {
            Query qry = getEntityManager().createQuery("select p.conta from Plano4 p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }
}
