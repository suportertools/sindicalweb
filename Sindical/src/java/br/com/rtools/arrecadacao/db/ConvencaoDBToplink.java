package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ConvencaoDBToplink extends DB implements ConvencaoDB {

    @Override
    public boolean insert(Convencao convencao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(convencao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Convencao convencao) {
        try {
            getEntityManager().merge(convencao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Convencao convencao) {
        try {
            getEntityManager().remove(convencao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Convencao pesquisaCodigo(int id) {
        Convencao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Convencao.pesquisaID");
            qry.setParameter("pid", id);
            result = (Convencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from Convencao cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Convencao pesquisaConvencaoDesc(String descricao) {
        Convencao result = null;
        try {
            Query qry = getEntityManager().createQuery("select con from Convencao con where con.descricao like :d_convencao");
            qry.setParameter("d_convencao", descricao);
            result = (Convencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
