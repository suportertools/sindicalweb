package br.com.rtools.homologacao.db;

import br.com.rtools.principal.DB;

/**
 * Use HorariosDao
 *
 * @author rtools2
 * @deprecated use HorariosDao
 */
@Deprecated
public class HorariosDBToplink extends DB {

//    @Override
//    public List pesquisaTodosPorFilial(int idFilial, int idDiaSemana) {
//        try {
//            Query qry = getEntityManager().createQuery(" SELECT H FROM Horarios H WHERE H.filial.id = :idFilial AND H.semana.id = :idDiaSemana ORDER BY H.hora");
//            qry.setParameter("idFilial", idFilial);
//            qry.setParameter("idDiaSemana", idDiaSemana);
//            List list = qry.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//        }
//        return new ArrayList();
//    }
//
//    @Override
//    public List pesquisaPorHorarioFilial(int idFilial, String horario, int idSemana) {
//        try {
//            Query qry = getEntityManager().createQuery(
//                    " SELECT h FROM Horarios h "
//                    + "  WHERE h.hora = '" + horario + "'"
//                    + "    AND h.semana.id = '" + idSemana + "'"
//                    + "    AND h.filial.id = " + idFilial);
//            List list = qry.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//        }
//        return new ArrayList();
//    }
//
//    @Override
//    public List listaHorariosAgrupadosPorFilialSemana(Integer idFilial, Integer idSemana) {
//        try {
//            Query query;
//            if (idSemana == null) {
//                query = getEntityManager().createQuery(" SELECT H.hora FROM Horarios AS H WHERE H.filial.id = :filial GROUP BY H.hora ORDER BY H.hora ");
//                query.setParameter("filial", idFilial);
//            } else {
//                query = getEntityManager().createQuery(" SELECT H.hora FROM Horarios AS H WHERE H.filial.id = :filial AND H.semana.id = :semana GROUP BY H.hora ORDER BY H.hora ");
//                query.setParameter("filial", idFilial);
//                query.setParameter("semana", idSemana);
//            }
//            List list = query.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//        return new ArrayList();
//    }
//
//    @Override
//    public List pesquisaPorHorarioFilial(Integer idFilial, String horario) {
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "   SELECT H FROM Horarios h "
//                    + "  WHERE H.hora = '" + horario + "'"
//                    + "    AND H.filial.id = " + idFilial);
//            List list = qry.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//        }
//        return new ArrayList();
//    }
}
