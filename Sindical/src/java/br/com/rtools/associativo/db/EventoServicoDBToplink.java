package br.com.rtools.associativo.db;

import br.com.rtools.associativo.EventoServico;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class EventoServicoDBToplink extends DB implements EventoServicoDB {

    public boolean insert(EventoServico eventoServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(eventoServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(EventoServico eventoServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(eventoServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(EventoServico eventoServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(eventoServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public EventoServico pesquisaCodigo(int id) {
        EventoServico result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("EventoServico.pesquisaID");
            qry.setParameter("pid", id);
            result = (EventoServico) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from EventoServico r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public List listaEventoServico(int idAEvento) {
        try {
            Query qry = getEntityManager().createQuery("select es "
                    + "  from EventoServico es"
                    + " where es.aEvento.id = " + idAEvento);
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public EventoServico pesquisaPorEventoEServico(int idAEvento, int idServico) {
        try {
            Query qry = getEntityManager().createQuery("select es "
                    + "  from EventoServico es"
                    + " where es.aEvento.id = " + idAEvento
                    + "   and es.servicos.id = " + idServico);
            return (EventoServico) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
