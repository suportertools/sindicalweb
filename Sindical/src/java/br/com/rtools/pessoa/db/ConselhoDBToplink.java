package br.com.rtools.pessoa.db;

import br.com.rtools.principal.DB;
import br.com.rtools.pessoa.Conselho;
import java.util.List;
import javax.persistence.Query;

public class ConselhoDBToplink extends DB implements ConselhoDB {

    public boolean insert(Conselho conselho) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(conselho);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Conselho conselho) {
        try {
            getEntityManager().merge(conselho);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Conselho conselho) {
        try {
            getEntityManager().remove(conselho);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Conselho pesquisaCodigo(int id) {
        Conselho result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Conselho.pesquisaID");
            qry.setParameter("pid", id);
            result = (Conselho) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select con from Conselho con ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public Conselho idConselho(Conselho des_conselho) {
        Conselho result = null;
        String descricao = des_conselho.getConselho().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select con from Conselho con where UPPER(con.conselho) = :d_conselho");
            qry.setParameter("d_conselho", descricao);
            result = (Conselho) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
