package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Horarios;
// import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class CancelarHorarioDBToplink extends DB implements CancelarHorarioDB {

    @Override
    public List pesquisaTodos(int idFilial) {
        try {
            Query qry = getEntityManager().createQuery("select ch from CancelarHorario ch where ch.filial.id = " + idFilial
                    + " order by ch.dtData desc, ch.hora asc");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public CancelarHorario pesquisaCancelamentoHorario(Date data, int idHorario, int idFilial) {
        CancelarHorario cancelarHorario = new CancelarHorario();
        try {
            Query qry = getEntityManager().createQuery("SELECT ch FROM CancelarHorario ch WHERE ch.dtData = :data AND ch.horarios.id = :idHorario AND ch.filial.id = :idFilial");
            qry.setParameter("data", data);
            qry.setParameter("idHorario", idHorario);
            qry.setParameter("idFilial", idFilial);
            if (!qry.getResultList().isEmpty()) {
                cancelarHorario = (CancelarHorario) qry.getSingleResult();
            }
        } catch (Exception e) {
            return cancelarHorario;
        }
        return cancelarHorario;
    }

    @Override
    public List<Horarios> listaTodosHorariosDisponiveisPorFilial(int idFilial, Date date, boolean isCancelados) {
        List result = new ArrayList();
        int diaDaSemana;
        String diaSemanaWhere = "";
        String dataWhere = "";
        if (isCancelados == false) {
            diaDaSemana = DataHoje.diaDaSemana(date);
            diaSemanaWhere = " AND h.semana.id = " + diaDaSemana;
        }
        try {
            Query qry = getEntityManager().createQuery("SELECT h FROM Horarios h WHERE " + dataWhere + " h.filial.id = :pfilial" + diaSemanaWhere +" ORDER BY H.hora ASC");
            qry.setParameter("pfilial", idFilial);
            if (!qry.getResultList().isEmpty()) {
                result = (qry.getResultList());
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    @Override
    public List<CancelarHorario> listaTodosHorariosCanceladosPorFilial(int idFilial, Date dataInicial, Date dataFinal) {
        List list = new ArrayList();
        if (dataFinal != null) {
            String queryPeriodo;
            int intDataInicial = DataHoje.converteDataParaInteger(DataHoje.converteData(dataInicial));
            int intDataFinal = DataHoje.converteDataParaInteger(DataHoje.converteData(dataFinal));
            if (intDataFinal < intDataInicial) {
                queryPeriodo = " '" + dataInicial + "' AND '" + dataInicial + "'  ";
            } else {
                queryPeriodo = " '" + dataInicial + "' AND '" + dataFinal + "'  ";
            }
            try {
                Query qry = getEntityManager().createNativeQuery(
                        "       SELECT ch.id "
                        + "       FROM hom_cancelar_horario ch "
                        + "      WHERE ch.dt_data BETWEEN " + queryPeriodo
                        + "        AND ch.id_filial = " + idFilial);
                if (!qry.getResultList().isEmpty()) {
                    list = qry.getResultList();
                    String queryListaIdPeriodo = "";
                    for (int i = 0; i < list.size(); i++) {
                        String id = ((Integer) ((List) (list).get(i)).get(0)).toString();
                        if (i == 0) {
                            queryListaIdPeriodo = id;
                        } else {
                            queryListaIdPeriodo += ", " + id;
                        }
                    }
                    list.clear();
                    Query qryResultadoPeriodo = getEntityManager().createQuery("SELECT ch FROM CancelarHorario ch WHERE ch.id IN (" + queryListaIdPeriodo + ") ORDER BY ch.dtData DESC, ch.horarios.hora ASC ");
                    if (!qryResultadoPeriodo.getResultList().isEmpty()) {
                        list = qryResultadoPeriodo.getResultList();
                    }
                }
            } catch (Exception e) {
            }

        } else {
            try {
                Query qry = getEntityManager().createQuery("SELECT ch FROM CancelarHorario ch WHERE ch.dtData = :dtData AND ch.filial.id = :idFilial ORDER BY ch.horarios.hora ASC ");
                qry.setParameter("idFilial", idFilial);
                qry.setParameter("dtData", dataInicial);
                if (!qry.getResultList().isEmpty()) {
                    list = qry.getResultList();
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    @Override
    public boolean cancelarTodosHorariosPeriodo(int idFilial, Date dateInicial, Date dateFinal) {
        try {
            Query qry = getEntityManager().createQuery("");
            getEntityManager().getTransaction().begin();
            if (qry.executeUpdate() == 1) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
        getEntityManager().getTransaction().commit();
        return true;
    }
}
