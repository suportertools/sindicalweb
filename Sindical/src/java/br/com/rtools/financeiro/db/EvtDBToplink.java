package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class EvtDBToplink extends DB implements EvtDB {

    public boolean insert(Evt evt) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(evt);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Evt evt) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(evt);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Evt evt) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(evt);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Evt pesquisaCodigo(int id) {
        Evt result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Evt.pesquisaID");
            qry.setParameter("pid", id);
            result = (Evt) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from Evt r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public List<Movimento> pesquisaMovimentoEvt(int idEvento, int idPessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select m"
                    + "  from Movimento m,"
                    + "     BVenda v"
                    + " where v.evento.id = " + idEvento
                    + "   and v.evt.id = m.evt.id"
                    + "   and v.pessoa.id = " + idPessoa);
            return (List<Movimento>) qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
