package br.com.rtools.associativo.db;

import br.com.rtools.associativo.EventoServico;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EventoServicoValorDBToplink extends DB implements EventoServicoValorDB {

    public boolean insert(EventoServicoValor eventoServicoValor) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(eventoServicoValor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(EventoServicoValor eventoServicoValor) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(eventoServicoValor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(EventoServicoValor eventoServicoValor) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(eventoServicoValor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public EventoServicoValor pesquisaCodigo(int id) {
        EventoServicoValor result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("EventoServicoValor.pesquisaID");
            qry.setParameter("pid", id);
            result = (EventoServicoValor) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from EventoServicoValor r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public EventoServicoValor pesquisaEventoServicoValor(int idEventoServico) {
        try {
            Query qry = getEntityManager().createQuery("select ev "
                    + "  from EventoServicoValor ev"
                    + " where ev.eventoServico.id = " + idEventoServico);
            return (EventoServicoValor) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return new EventoServicoValor();
        }
    }

    public List<EventoServicoValor> pesquisaServicoValorPorEvento(int idEvento) {
        List<EventoServicoValor> lista = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select r from EventoServicoValor r where r.eventoServico.aEvento.id = " + idEvento + " order by r.eventoServico.servicos.descricao, r.eventoServico.categoria.categoria");
            lista = qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            lista = new ArrayList();
        }
        return lista;
    }
}
