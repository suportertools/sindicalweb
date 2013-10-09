package br.com.rtools.associativo.db;

import br.com.rtools.associativo.EventoBanda;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EventoBandaDBToplink extends DB implements EventoBandaDB {

    @Override
    public boolean insert(EventoBanda eventoBanda) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(eventoBanda);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(EventoBanda eventoBanda) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(eventoBanda);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(EventoBanda eventoBanda) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(eventoBanda);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public EventoBanda pesquisaCodigo(int id) {
        EventoBanda result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("EventoBanda.pesquisaID");
            qry.setParameter("pid", id);
            result = (EventoBanda) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from EventoBanda r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List<EventoBanda> pesquisaBandasDoEvento(int idEvento) {
        List<EventoBanda> lista = new ArrayList<EventoBanda>();
        try {
            Query qry = getEntityManager().createQuery(
                    "select ev "
                    + "  from EventoBanda ev"
                    + " where ev.evento.id = " + idEvento);
            lista = qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            lista = new ArrayList<EventoBanda>();
        }
        return lista;
    }
}
