package br.com.rtools.homologacao.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class HorariosDBToplink extends DB implements HorariosDB {

    @Override
    public List pesquisaTodosPorFilial(int idFilial, int idDiaSemana) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT H FROM Horarios H WHERE H.filial.id = :idFilial AND H.semana.id = :idDiaSemana ORDER BY H.hora");
            qry.setParameter("idFilial", idFilial);
            qry.setParameter("idDiaSemana", idDiaSemana);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
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
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
