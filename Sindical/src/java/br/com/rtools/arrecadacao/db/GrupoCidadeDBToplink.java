package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class GrupoCidadeDBToplink extends DB implements GrupoCidadeDB {

    public boolean insert(GrupoCidade grupoCidade) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(grupoCidade);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(GrupoCidade grupoCidade) {
        try {
            getEntityManager().merge(grupoCidade);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(GrupoCidade grupoCidade) {
        try {
            getEntityManager().remove(grupoCidade);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public GrupoCidade pesquisaCodigo(int id) {
        GrupoCidade result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("GrupoCidade.pesquisaID");
            qry.setParameter("pid", id);
            result = (GrupoCidade) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from GrupoCidade cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List<ConvencaoCidade> pesquisaGrupoPorConvencao(int idConvencao) {
        try {
            Query qry = getEntityManager().createQuery("select cc from ConvencaoCidade cc"
                    + " where cc.convencao.id = :pid");
            qry.setParameter("pid", idConvencao);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public GrupoCidade idGrupoCidade(GrupoCidade des_grupoCidade) {
        GrupoCidade result = null;
        String descricao = des_grupoCidade.getDescricao().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select con from GrupoCidade con where UPPER(con.descricao) = :d_grupoCidade");
            qry.setParameter("d_grupoCidade", descricao);
            result = (GrupoCidade) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
