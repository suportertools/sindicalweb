package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.DescricaoEvento;
import br.com.rtools.associativo.GrupoEvento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class DescricaoEventoDBToplink extends DB implements DescricaoEventoDB {

    @Override
    public boolean insert(DescricaoEvento descricaoEvento) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(descricaoEvento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(DescricaoEvento descricaoEvento) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(descricaoEvento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(DescricaoEvento descricaoEvento) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(descricaoEvento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public DescricaoEvento pesquisaCodigo(int id) {
        DescricaoEvento result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("DescricaoEvento.pesquisaID");
            qry.setParameter("pid", id);
            result = (DescricaoEvento) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from DescricaoEvento r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List<GrupoEvento> listaGrupoEvento() {
        try {
            Query qry = getEntityManager().createQuery("select ge from GrupoEvento ge");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public GrupoEvento pesquisaGrupoEvento(int idGrupoEvento) {
        GrupoEvento result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("GrupoEvento.pesquisaID");
            qry.setParameter("pid", idGrupoEvento);
            result = (GrupoEvento) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
        return result;
    }

    @Override
    public List<DescricaoEvento> pesquisaDescricaoPorGrupo(int idGrupoEvento) {
        List<DescricaoEvento> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select de from DescricaoEvento de where de.grupoEvento.id = :pid");
            qry.setParameter("pid", idGrupoEvento);
            result = qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List<AEvento> listaEventoPorDescricao(int idDescEvento) {
        List<AEvento> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select e from AEvento e where e.descricaoEvento.id = :pid");
            qry.setParameter("pid", idDescEvento);
            result = qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}