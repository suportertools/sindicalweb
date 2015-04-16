package br.com.rtools.agenda.dao;

import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class GrupoAgendaDao extends DB {

    public GrupoAgenda idGrupoAgenda(GrupoAgenda des_grupoAgenda) {
        GrupoAgenda result = null;
        String descricao = des_grupoAgenda.getDescricao().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select grupAge from GrupoAgenda grupAge where UPPER(grupAge.descricao) = :d_grupoAgenda");
            qry.setParameter("d_grupoAgenda", descricao);
            result = (GrupoAgenda) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaGrupoAgenda(String desc, String como) {
        String textQuery = null;
        if (como.equals("P")) {

            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ga from GrupoAgenda ga    "
                    + " where UPPER(ga.descricao) like :desc";
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ga from GrupoAgenda ga    "
                    + " where UPPER(ga.descricao) like :desc";
        }

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                qry.setParameter("desc", desc);
            }
            List list = qry.getResultList();
        } catch (Exception e) {
            return new ArrayList<Object>();
        }
        return new ArrayList<Object>();
    }
}
