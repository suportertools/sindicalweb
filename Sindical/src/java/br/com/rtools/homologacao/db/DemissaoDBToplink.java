package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Demissao;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class DemissaoDBToplink extends DB implements DemissaoDB {

    public boolean insert(Demissao demissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(demissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Demissao demissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(demissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Demissao demissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(demissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select d from Demissao d");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Demissao pesquisaCodigo(int id) {
        Demissao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Demissao.pesquisaID");
            qry.setParameter("pid", id);
            result = (Demissao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
