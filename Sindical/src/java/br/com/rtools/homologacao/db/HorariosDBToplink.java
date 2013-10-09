package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Horarios;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class HorariosDBToplink extends DB implements HorariosDB {

    @Override
    public boolean insert(Horarios horarios) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(horarios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Horarios horarios) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(horarios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Horarios horarios) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(horarios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select h from Horarios h order by h.hora");
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaTodosPorFilial(int idFilial, int idDiaSemana) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT H FROM Horarios H WHERE H.filial.id = :idFilial AND H.semana.id = :idDiaSemana ORDER BY H.hora");
            qry.setParameter("idFilial", idFilial);
            qry.setParameter("idDiaSemana", idDiaSemana);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaPorHorario(String horario) {
        try {
            Query qry = getEntityManager().createQuery("select h from Horarios h"
                    + " where h.hora = '" + horario + "'");
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (EJBQLException e) {
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaPorHorarioFilial(int idFilial, String horario, int idSemana) {
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT h FROM Horarios h "
                    + "  WHERE h.hora = '" + horario + "'"
                    + "    AND h.semana.id = '" + idSemana + "'"
                    + "    AND h.filial.id = " + idFilial);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (EJBQLException e) {
        }
        return new ArrayList();
    }

    @Override
    public Horarios pesquisaCodigo(int id) {
        Horarios result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Horarios.pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = (Horarios) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return result;
    }
}
