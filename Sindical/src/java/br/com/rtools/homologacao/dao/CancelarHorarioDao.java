package br.com.rtools.homologacao.dao;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class CancelarHorarioDao extends DB {

    public List pesquisaTodos(Integer idFilial) {
        try {
            Query qry = getEntityManager().createQuery("SELECT CH FROM CancelarHorario AS CH WHERE CH.filial.id = :filial ORDER BY CH.dtData DESC, CH.horarios.hora ASC");
            qry.setParameter("filial", idFilial);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public CancelarHorario pesquisaCancelamentoHorario(Date data, Integer idHorario, Integer idFilial) {
        return pesquisaCancelamentoHorarioSemana(data, idHorario, idFilial, null);
    }

    public CancelarHorario pesquisaCancelamentoHorarioSemana(Date data, Integer idHorario, Integer idFilial, Integer idSemana) {
        CancelarHorario cancelarHorario = new CancelarHorario();
        try {
            Query query;
            if (idSemana != null) {
                if (data == null) {
                    query = getEntityManager().createQuery("SELECT CH FROM CancelarHorario AS CH WHERE CH.horarios.id = :horario AND CH.filial.id = :filial AND CH.horarios.semana.id = :semana");
                } else {
                    query = getEntityManager().createQuery("SELECT CH FROM CancelarHorario AS CH WHERE  CH.dtData = :data AND CH.horarios.id = :horario AND CH.filial.id = :filial AND CH.horarios.semana.id = :semana");
                    query.setParameter("data", data);
                }
                query.setParameter("semana", idSemana);
                query.setMaxResults(1);
            } else {
                query = getEntityManager().createQuery("SELECT CH FROM CancelarHorario AS CH WHERE CH.dtData = :data AND CH.horarios.id = :horario AND CH.filial.id = :filial");
                query.setParameter("data", data);
            }
            query.setParameter("horario", idHorario);
            query.setParameter("filial", idFilial);
            if (!query.getResultList().isEmpty()) {
                cancelarHorario = (CancelarHorario) query.getSingleResult();
            }
        } catch (Exception e) {
            return cancelarHorario;
        }
        return cancelarHorario;
    }

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal) {
        return listaTodosHorariosCancelados(idFilial, dataInicial, dataFinal, null, null);
    }

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal, String horario) {
        return listaTodosHorariosCancelados(idFilial, dataInicial, dataFinal, null, horario);

    }

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal, Integer idSemana) {
        return listaTodosHorariosCancelados(idFilial, dataInicial, dataFinal, idSemana, null);
    }

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal, Integer idSemana, String horario) {
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
                String queryString
                        = "     SELECT CH.id                                        "
                        + "       FROM hom_cancelar_horario CH                      "
                        + " INNER JOIN hom_horarios AS H ON H.id = CH.id_horarios   "
                        + "      WHERE CH.dt_data BETWEEN " + queryPeriodo
                        + "        AND CH.id_filial = " + idFilial;
                if (idSemana != null) {
                    queryString += " AND H.id_semana = " + idSemana;
                }
                if (horario != null) {
                    queryString += " AND H.ds_hora = '" + horario + "'";
                }
                Query qry = getEntityManager().createNativeQuery(queryString);
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
                    queryString = "SELECT ch FROM CancelarHorario ch WHERE ch.id IN (" + queryListaIdPeriodo + ") ORDER BY ch.dtData DESC, ch.horarios.hora ASC";
                    Query qryResultadoPeriodo = getEntityManager().createQuery(queryString);
                    if (!qryResultadoPeriodo.getResultList().isEmpty()) {
                        list = qryResultadoPeriodo.getResultList();
                    }
                }
            } catch (Exception e) {
            }

        } else {
            try {
                Query qry = getEntityManager().createQuery("SELECT ch FROM CancelarHorario ch WHERE ch.dtData = :dtData AND ch.filial.id = :idFilial ORDER BY ch.dtData DESC, ch.horarios.hora ASC ");
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
}
