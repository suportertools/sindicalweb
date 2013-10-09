package br.com.rtools.associativo.db;

import br.com.rtools.associativo.BVenda;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class BVendaDBToplink extends DB implements BVendaDB {

    public boolean insert(BVenda bVenda) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(bVenda);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(BVenda bVenda) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(bVenda);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(BVenda bVenda) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(bVenda);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public BVenda pesquisaCodigo(int id) {
        BVenda result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("BVenda.pesquisaID");
            qry.setParameter("pid", id);
            result = (BVenda) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from BVenda r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public BVenda pesquisaVendaPorEvt(int idEvt) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select b from BVenda b where b.evt.id = " + idEvt);
            return (BVenda) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void excluirMesa(int idVenda) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " delete from eve_mesa m where m.id_venda =  " + idVenda + " ; "
                    + " delete from eve_venda where id = " + idVenda + " ; ");
            qry.executeUpdate();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
