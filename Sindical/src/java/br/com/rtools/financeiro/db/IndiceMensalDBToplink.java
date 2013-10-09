package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.IndiceMensal;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class IndiceMensalDBToplink extends DB implements IndiceMensalDB {

    @Override
    public boolean insert(IndiceMensal indiceMensal) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(indiceMensal);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(IndiceMensal indiceMensal) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(indiceMensal);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(IndiceMensal indiceMensal) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(indiceMensal);
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
            Query qry = getEntityManager().createQuery("select im from IndiceMensal im");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaIndMensalExistente(int idIndice, int ano, int mes) {
        List result;
        Query qry = getEntityManager().createQuery("select im from IndiceMensal im "
                + " where im.indice.id = " + idIndice
                + "   and im.ano = " + ano
                + "   and im.mes = " + mes);
        result = qry.getResultList();
        if (result.isEmpty()) {
            return new ArrayList();
        } else {
            return result;
        }
    }

    @Override
    public List pesquisaTodosIndices() {
        try {
            Query qry = getEntityManager().createQuery("select i from Indice i order by i.descricao");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaIndiceMensalPorIDIndice(int idIndice) {
        try {
            Query qry = getEntityManager().createQuery("select im from IndiceMensal im where im.indice.id = " + idIndice
                    + " order by im.ano desc, im.mes asc");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public IndiceMensal pesquisaCodigo(int id) {
        IndiceMensal result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("IndiceMensal.pesquisaID");
            qry.setParameter("pid", id);
            result = (IndiceMensal) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Indice pesquisaCodigoIndice(int id) {
        Indice result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Indice.pesquisaID");
            qry.setParameter("pid", id);
            result = (Indice) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
