package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.FilialCidade;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class FilialCidadeDBToplink extends DB implements FilialCidadeDB {

    @Override
    public boolean insert(FilialCidade filialCidade) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(filialCidade);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(FilialCidade filialCidade) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(filialCidade);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(FilialCidade filialCidade) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(filialCidade);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public FilialCidade pesquisaCodigo(int id) {
        FilialCidade result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("FilialCidade.pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = (FilialCidade) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public FilialCidade pesquisaFilialCidade(int idFilial, int idCidade) {
        FilialCidade result = new FilialCidade();
        try {
            Query qry = getEntityManager().createQuery("select fc from FilialCidade fc"
                    + " where fc.cidade.id = " + idCidade
                    + "   and fc.filial.id = " + idFilial);
            if (!qry.getResultList().isEmpty()) {
                result = (FilialCidade) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public FilialCidade pesquisaFilialPorCidade(int idCidade) {
        FilialCidade result = new FilialCidade();
        try {
            Query qry = getEntityManager().createQuery("select fc from FilialCidade fc"
                    + " where fc.cidade.id = " + idCidade);
            if (!qry.getResultList().isEmpty()) {
                result = (FilialCidade) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select fc from FilialCidade fc");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
