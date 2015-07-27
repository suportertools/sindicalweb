package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class PesquisaLogDao extends DB {

    public List listRotinasLogs() {
        try {
            Query query = getEntityManager().createQuery("SELECT L.rotina FROM Log AS L GROUP BY L.rotina ORDER BY L.rotina.rotina ASC");
            List list = query.getResultList();
            if (list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaLogs(String dataInicial, String dataFinal, String horaInicial, String horaFinal, int idUsuario, int idRotina, String idInEvento, String descricao) {
        List listWhere = new ArrayList();
        if (dataInicial != null) {
            listWhere.add(" L.dtData BETWEEN :dtInicial AND :dtFinal ");
            if (!horaInicial.isEmpty() && !horaFinal.isEmpty()) {
                listWhere.add(" L.hora BETWEEN '" + horaInicial + "' AND '" + horaFinal + "' ");
            } else if (!horaInicial.isEmpty()) {
                listWhere.add(" L.hora LIKE '" + horaInicial + "' ");
            }
        }
        if (idUsuario != 0) {
            listWhere.add(" L.usuario.id = " + idUsuario);
        }
        if (idRotina != 0) {
            listWhere.add(" L.rotina.id = " + idRotina);
        }
        if (idInEvento != null) {
            listWhere.add(" L.evento.id IN( " + idInEvento + ") ");
        }
        if (!descricao.isEmpty()) {
            listWhere.add(" UPPER(L.conteudoOriginal) LIKE '%" + descricao.toUpperCase() + "%' OR UPPER(L.conteudoAlterado) LIKE '%" + descricao.toUpperCase() + "%' ");
        }
        try {
            String where = "";
            if (!listWhere.isEmpty()) {
                for (int i = 0; i < listWhere.size(); i++) {
                    if (i == 0) {
                        where += " AND " + listWhere.get(i).toString() + " ";
                    } else {
                        where += " AND " + listWhere.get(i).toString() + " ";
                    }
                }
            }
            String queryString = "SELECT L FROM Log AS L WHERE L.rotina IS NOT NULL AND L.usuario IS NOT NULL " + where + " ORDER BY L.dtData DESC, L.hora DESC, L.rotina.rotina DESC ";
            Query query = getEntityManager().createQuery(queryString);
            if (dataInicial != null) {
                query.setParameter("dtInicial", DataHoje.converte(dataInicial), TemporalType.DATE);
                query.setParameter("dtFinal", DataHoje.converte(dataFinal), TemporalType.DATE);
            }
            query.setMaxResults(200);
            List list = query.getResultList();
            if (list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
