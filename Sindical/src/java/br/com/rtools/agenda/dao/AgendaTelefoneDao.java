package br.com.rtools.agenda.dao;

import br.com.rtools.agenda.Agenda;
import br.com.rtools.agenda.AgendaFavorito;
import br.com.rtools.agenda.AgendaTelefone;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AgendaTelefoneDao extends DB {

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT age FROM Agenda age ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List<AgendaTelefone> listaAgendaTelefone(int idAgenda) {
        List list = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(" SELECT AGET FROM AgendaTelefone AS AGET WHERE AGET.agenda.id = :idAgenda ORDER BY AGET.contato ASC, AGET.tipoTelefone.descricao ASC, AGET.telefone ASC ");
            qry.setParameter("idAgenda", idAgenda);
            list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public List pesquisaAgenda(String ddd, String descricaoPesquisa, String porPesquisa, String comoPesquisa, int idGrupoAgenda) {
        List list = new ArrayList();
        String queryFiltroGrupoAgendaA = "";
        String queryFiltroGrupoAgendaB = "";
        String query = " SELECT AGE FROM Agenda AS AGE ORDER BY AGE.id DESC ";
        if (idGrupoAgenda > 0) {
            query = " SELECT AGE FROM Agenda AS AGE WHERE AGE.grupoAgenda.id = " + idGrupoAgenda + " ORDER BY ID DESC ";
            queryFiltroGrupoAgendaA = " AND AGE.grupoAgenda.id = " + idGrupoAgenda + " ";
            queryFiltroGrupoAgendaB = " AND AGET.agenda.grupoAgenda.id = " + idGrupoAgenda + " ";
        }
        try {
            if (porPesquisa.equals("pessoa")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.pessoa.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                } else {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                }
            } else if (porPesquisa.equals("nome")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                } else {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                }
            } else if (porPesquisa.equals("contato")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGET.agenda FROM AgendaTelefone AS AGET WHERE UPPER(AGET.contato) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaB + " ORDER BY AGET.contato ASC ";
                } else {
                    query = " SELECT AGET.agenda FROM AgendaTelefone AS AGET WHERE UPPER(AGET.contato) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaB + "  ORDER BY AGET.contato ASC  ";
                }
            } else if (porPesquisa.equals("telefone")) {
                String dddString = "";
                if (!ddd.equals("")) {
                    dddString = " AGET.ddd = '" + ddd + "' AND ";
                }
                query = " SELECT AGET.agenda FROM AgendaTelefone AS AGET WHERE " + dddString + " AGET.telefone = '" + descricaoPesquisa + "'" + queryFiltroGrupoAgendaB;
            } else if (porPesquisa.equals("endereco")) {
                String queryEndereco = ""
                        + "     SELECT age.id FROM age_agenda age"
                        + " INNER JOIN endereco_vw ende ON ende.id = age.id_endereco                                                                                                         "
                        + "WHERE UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || bairro || ', ' || cidade || ', ' || uf)    LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        + "   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade || ', ' || uf)                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        + "   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade )                                   LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        + "   OR UPPER(ende.logradouro || ' ' || ende.endereco)                                                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        + "   OR UPPER(ende.endereco)                                                                                LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        + "   OR UPPER(ende.cidade)                                                                                  LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        + "   OR UPPER(ende.cep)                                                                                     = '" + descricaoPesquisa.toUpperCase() + "'";

                Query qryEndereco = getEntityManager().createNativeQuery(queryEndereco);
                List listEndereco = qryEndereco.getResultList();
                String listaId = "";
                if (!listEndereco.isEmpty()) {
                    for (int i = 0; i < listEndereco.size(); i++) {
                        if (i == 0) {
                            listaId = ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                        } else {
                            listaId += ", " + ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                        }
                    }
                    query = " SELECT AGE FROM Agenda AS AGE WHERE AGE.id IN(" + listaId + ")";
                }
            }
            Query qry = getEntityManager().createQuery(query);
            qry.setMaxResults(250);
            list = qry.getResultList();
            if (!list.isEmpty()) {
                return qry.getResultList();
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public List pesquisaAgendaTelefone(String ddd, String descricaoPesquisa, String porPesquisa, String comoPesquisa, int idGrupoAgenda, boolean isFavoritos, int idUsuario) {
        List list = new ArrayList();
        try {
            String idGAs = "";
            String idGAsAgenda = "";
            String idGAsAgendaAnd = "";
            String idGAsAgendaT = "";
            String idGAsAgendaTAnd = "";
            if (idUsuario != 0) {
                String queryString = ""
                        + " SELECT id FROM age_grupo_agenda                             "
                        + "  WHERE id IN (SELECT id_grupo_agenda                        "
                        + "                 FROM age_grupo_usuario                      "
                        + "                WHERE id_usuario = " + idUsuario + ")            "
                        + "  UNION SELECT id                                            "
                        + "          FROM age_grupo_agenda                              "
                        + "         WHERE id NOT IN (SELECT id_grupo_agenda             "
                        + "                            FROM age_grupo_usuario           "
                        + "                           WHERE id_usuario != " + idUsuario + ")";
                Query queryNativeGA = getEntityManager().createNativeQuery(queryString);
                List listNativeGA = queryNativeGA.getResultList();
                for (int i = 0; i < listNativeGA.size(); i++) {
                    if (i == 0) {
                        idGAs = "" + ((List) (listNativeGA.get(i))).get(0).toString();
                    } else {
                        idGAs += ", " + ((List) (listNativeGA.get(i))).get(0).toString();
                    }
                }
                if (!listNativeGA.isEmpty()) {
                    idGAsAgenda = " ";
                    idGAsAgendaT = " AGET.agenda.grupoAgenda.id IN (" + idGAs + ") ";
                    idGAsAgendaAnd = " AGE.grupoAgenda.id IN (" + idGAs + ") AND ";
                    idGAsAgendaTAnd = " AGET.agenda.grupoAgenda.id IN (" + idGAs + ") AND ";
                }
            }
            String queryFiltroGrupoAgendaA = "";
            String queryFiltroGrupoAgendaB = "";
            String query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaT + " ORDER BY AGET.agenda.id DESC ";
            if (idGrupoAgenda > 0) {
                query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " AGET.agenda.grupoAgenda.id = " + idGrupoAgenda + " GROUP BY AGET.agenda ORDER BY AGET.agenda.id DESC ";
                queryFiltroGrupoAgendaA = " AND AGET.agenda.grupoAgenda.id = " + idGrupoAgenda + " ";
            }
            if (!isFavoritos) {
                if (porPesquisa.equals("pessoa")) {
                    if (comoPesquisa.equals("Inicial")) {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.agenda.pessoa.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " GROUP BY AGET.agenda ORDER BY AGET.agenda.nome ASC ";
                    } else {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.agenda.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " GROUP BY AGET.agenda ORDER BY AGET.agenda.nome ASC ";
                    }
                } else if (porPesquisa.equals("nome")) {
                    if (comoPesquisa.equals("Inicial")) {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.agenda.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGET.agenda.nome ASC ";
                    } else {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.agenda.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGET.agenda.nome ASC ";
                    }
                } else if (porPesquisa.equals("contato")) {
                    if (comoPesquisa.equals("Inicial")) {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.contato) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaB + " ORDER BY AGET.contato ASC ";
                    } else {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.contato) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaB + "  ORDER BY AGET.contato ASC  ";
                    }
                } else if (porPesquisa.equals("telefone")) {
                    String dddString = "";
                    if (!ddd.equals("")) {
                        dddString = " AGET.ddd = '" + ddd + "' AND ";
                    }
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " " + dddString + " AGET.telefone = '" + descricaoPesquisa.toUpperCase() + "'" + queryFiltroGrupoAgendaB;
                } else if (porPesquisa.equals("endereco")) {
                    String queryEndereco = ""
                            + "     SELECT age.id FROM age_agenda age"
                            + " INNER JOIN endereco_vw ende ON ende.id = age.id_endereco                                                                                                         "
                            + "WHERE UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || bairro || ', ' || cidade || ', ' || uf)    LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                            + "   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade || ', ' || uf)                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                            + "   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade )                                   LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                            + "   OR UPPER(ende.logradouro || ' ' || ende.endereco)                                                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                            + "   OR UPPER(ende.endereco)                                                                                LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                            + "   OR UPPER(ende.cidade)                                                                                  LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                            + "   OR UPPER(ende.cep)                                                                                     = '" + descricaoPesquisa.toUpperCase() + "'";
                    Query qryEndereco = getEntityManager().createNativeQuery(queryEndereco);
                    List listEndereco = qryEndereco.getResultList();
                    String listaId = "";
                    if (!listEndereco.isEmpty()) {
                        for (int i = 0; i < listEndereco.size(); i++) {
                            if (i == 0) {
                                listaId = ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                            } else {
                                listaId += ", " + ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                            }
                        }
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " AGET.agenda.id IN(" + listaId + ") GROUP BY AGET.agenda";
                    }
                }
                Query qry = getEntityManager().createQuery(query);
                qry.setMaxResults(250);
                list = qry.getResultList();
                if (!list.isEmpty()) {
                    return qry.getResultList();
                } else {
                    String subQuery = "";
                    String subQueryFiltroGrupoAgendaA = "";
                    if (idGrupoAgenda > 0) {
                        subQuery = " SELECT AGET FROM Agenda AS AGE WHERE AGE.grupoAgenda.id = " + idGrupoAgenda + " GROUP BY AGET.agenda ORDER BY AGE.id DESC ";
                        subQueryFiltroGrupoAgendaA = " AND AGE.grupoAgenda.id = " + idGrupoAgenda + " ";
                    }
                    if (porPesquisa.equals("pessoa")) {
                        if (comoPesquisa.equals("Inicial")) {
                            subQuery = " SELECT AGE FROM Agenda AS AGE WHERE " + idGAsAgendaAnd + " UPPER(AGE.pessoa.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + subQueryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                        } else {
                            subQuery = " SELECT AGE FROM Agenda AS AGE WHERE  " + idGAsAgendaAnd + " UPPER(AGE.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + subQueryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                        }
                    } else if (porPesquisa.equals("nome")) {
                        if (comoPesquisa.equals("Inicial")) {
                            subQuery = " SELECT AGE FROM Agenda AS AGE WHERE  " + idGAsAgendaAnd + " UPPER(AGE.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + subQueryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                        } else {
                            subQuery = " SELECT AGE FROM Agenda AS AGE WHERE  " + idGAsAgendaAnd + " UPPER(AGE.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + subQueryFiltroGrupoAgendaA + " ORDER BY AGE.nome ASC ";
                        }
                    }
                    Query subQry = getEntityManager().createQuery(subQuery);
                    List<Agenda> subList = subQry.getResultList();
                    if (!subList.isEmpty()) {
                        for (Agenda agenda : subList) {
                            list.add(new AgendaTelefone(-1, agenda, null, "", "", "", ""));
                        }
                        return list;
                    }
                }
            } else {
                query = " SELECT AF.agenda FROM AgendaFavorito AS AF WHERE AF.agenda.grupoAgenda.id IN (" + idGAs + ") AND AF.usuario.id = :p1 ORDER BY AF.agenda.nome ";
                Query qry = getEntityManager().createQuery(query);
                qry.setParameter("p1", idUsuario);
                list = qry.getResultList();
                List<AgendaTelefone> agendaTelefones = new ArrayList<AgendaTelefone>();
                AgendaTelefone agendaTelefone = new AgendaTelefone();
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        agendaTelefone.setAgenda((Agenda) list.get(i));
                        agendaTelefones.add(agendaTelefone);
                        agendaTelefone = new AgendaTelefone();
                    }
                    return agendaTelefones;
                }
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public List pesquisaAgendaTelefonex(String ddd, String descricaoPesquisa, String porPesquisa, String comoPesquisa, int idGrupoAgenda, boolean isFavoritos, int idUsuario) {
        if (!descricaoPesquisa.isEmpty() && !porPesquisa.equals("telefone")) {
            descricaoPesquisa = AnaliseString.removerAcentos(descricaoPesquisa);
            descricaoPesquisa = descricaoPesquisa.toUpperCase();
        }
        String inArray = "";
        List list = new ArrayList();
        String inGrupoPermitido = "";
        String queryString;
        String subQueryString;
        List inList;
        List<AgendaTelefone> agendaTelefones = new ArrayList<AgendaTelefone>();
        AgendaTelefone agendaTelefone = new AgendaTelefone();
        Query query;

        // Verifica se o usuário logado tem permissão para ver o grupo
        if (idUsuario != 0) {
            queryString = ""
                    + " SELECT id FROM age_grupo_agenda                             "
                    + "  WHERE id IN (SELECT id_grupo_agenda                        "
                    + "                 FROM age_grupo_usuario                      "
                    + "                WHERE id_usuario = " + idUsuario + ")            "
                    + "  UNION SELECT id                                            "
                    + "          FROM age_grupo_agenda                              "
                    + "         WHERE id NOT IN (SELECT id_grupo_agenda             "
                    + "                            FROM age_grupo_usuario           "
                    + "                           WHERE id_usuario != " + idUsuario + ")";
            Query queryNativeGA = getEntityManager().createNativeQuery(queryString);
            List listNativeGA = queryNativeGA.getResultList();
            for (int i = 0; i < listNativeGA.size(); i++) {
                if (i == 0) {
                    inGrupoPermitido = "" + ((List) (listNativeGA.get(i))).get(0).toString();
                } else {
                    inGrupoPermitido += ", " + ((List) (listNativeGA.get(i))).get(0).toString();
                }
            }
        }

        // Traz os contatos favoritos do usuário da sessão
        if (isFavoritos) {
            try {
                queryString = " SELECT AF.agenda FROM AgendaFavorito AS AF WHERE AF.agenda.grupoAgenda.id IN (" + inGrupoPermitido + ") AND AF.usuario.id = :usuario ORDER BY AF.agenda.nome ";
                query = getEntityManager().createQuery(queryString);
                query.setParameter("usuario", idUsuario);
                list = query.getResultList();
                if (!list.isEmpty()) {
                    for (Object o : list) {
                        agendaTelefone.setAgenda((Agenda) o);
                        agendaTelefones.add(agendaTelefone);
                        agendaTelefone = new AgendaTelefone();
                    }
                    return agendaTelefones;
                }
            } catch (Exception e) {

            }
            return new ArrayList();
        }
        // Adiciona os grupos que o usuário pode visualizar na consulta
        if (!inGrupoPermitido.isEmpty()) {
            inGrupoPermitido = " AGE.id_grupo_agenda IN (" + inGrupoPermitido + ") AND ";
        }
        String queryGrupoAgenda = "";
        if (idGrupoAgenda > 0) {
            queryGrupoAgenda = " AND AGE.id_grupo_agenda = " + idGrupoAgenda + " ";
            // Se a descriçção for vázia não entrará mas consultas, pulara diretamente para os grupos da agenda
            if (descricaoPesquisa.isEmpty()) {
                porPesquisa = "";
            }
        }
        int indice = 2;
        String limiQuery = " LIMIT 250";
        if (descricaoPesquisa.length() == 1) {
            limiQuery = " LIMIT 50";
        } else if (descricaoPesquisa.length() == 2) {
            limiQuery = " LIMIT 80";
        } else if (descricaoPesquisa.length() == 3) {
            limiQuery = " LIMIT 100";
        }
        queryString = "  SELECT DISTINCT ON(AGET.id_agenda) AGET.id_agenda, AGE.ds_nome, AGET.id            "
                + "        FROM age_telefone AS AGET                            "
                + "  INNER JOIN age_agenda AS AGE ON AGE.id = AGET.id_agenda    "
                + "   LEFT JOIN pes_pessoa AS P ON P.id = AGE.id_pessoa         "
                + "       WHERE " + inGrupoPermitido;
        if (porPesquisa.equals("pessoa")) {
            if (comoPesquisa.equals("Inicial")) {
                queryString += " UPPER(TRANSLATE(P.ds_nome)) LIKE '" + descricaoPesquisa + "%' " + queryGrupoAgenda;
            } else {
                queryString += " UPPER(TRANSLATE(P.ds_nome)) LIKE '%" + descricaoPesquisa + "%' " + queryGrupoAgenda;
            }
        } else if (porPesquisa.equals("nome")) {
            if (comoPesquisa.equals("Inicial")) {
                queryString += " UPPER(TRANSLATE(AGE.ds_nome)) LIKE '" + descricaoPesquisa + "%' " + queryGrupoAgenda;
            } else {
                queryString += " UPPER(TRANSLATE(AGE.ds_nome)) LIKE '%" + descricaoPesquisa + "%' " + queryGrupoAgenda;
            }
        } else if (porPesquisa.equals("contato")) {
            if (comoPesquisa.equals("Inicial")) {
                queryString += " UPPER(TRANSLATE(AGET.ds_contato)) LIKE '" + descricaoPesquisa + "%' " + queryGrupoAgenda;
            } else {
                queryString += " UPPER(TRANSLATE(AGET.ds_contato)) LIKE '%" + descricaoPesquisa + "%' " + queryGrupoAgenda;
            }
        } else if (porPesquisa.equals("telefone")) {
            String dddString = "";
            if (!ddd.equals("")) {
                dddString = " AGET.ds_ddd = '" + ddd + "' AND ";
            }
            queryString += dddString + " AGET.ds_telefone = '" + descricaoPesquisa + "'";
        } else if (porPesquisa.equals("endereco")) {
            indice = 2;
            queryString = ""
                    + "     SELECT DISTINCT ON(AGET.id_agenda) AGET.id_agenda, AGE.ds_nome, AGET.id                                                                                                                                 "
                    + "       FROM age_agenda AS AGE                                                                                                                                                                                "
                    + "  LEFT JOIN age_telefone AS AGET ON AGET.id_agenda = AGE.id                                                                                                                                                  "
                    + " INNER JOIN endereco_vw ende ON ende.id = age.id_endereco                                                                                                                                                    "
                    + "WHERE UPPER(TRANSLATE(ende.logradouro) || ' ' || TRANSLATE(ende.endereco) || ', ' || TRANSLATE(bairro) || ', ' || TRANSLATE(cidade) || ', ' || TRANSLATE(uf))    LIKE UPPER('%" + descricaoPesquisa + "%')   "
                    + "   OR UPPER(TRANSLATE(ende.logradouro) || ' ' || TRANSLATE(ende.endereco) || ', ' || TRANSLATE(cidade) || ', ' || TRANSLATE(uf))                      LIKE UPPER('%" + descricaoPesquisa + "%')              "
                    + "   OR UPPER(TRANSLATE(ende.logradouro) || ' ' || TRANSLATE(ende.endereco) || ', ' || TRANSLATE(cidade))                                   LIKE UPPER('%" + descricaoPesquisa + "%')                          "
                    + "   OR UPPER(TRANSLATE(ende.logradouro) || ' ' || TRANSLATE(ende.endereco))                                                      LIKE UPPER('%" + descricaoPesquisa + "%')                                    "
                    + "   OR UPPER(TRANSLATE(ende.endereco))                                                                                LIKE UPPER('%" + descricaoPesquisa + "%')                                               "
                    + "   OR UPPER(TRANSLATE(ende.cidade))                                                                                  LIKE UPPER('%" + descricaoPesquisa + "%')                                               "
                    + "   OR UPPER(ende.cep)                                                                                     = '" + descricaoPesquisa + "'";
        } else {
            queryString += " AGE.id_grupo_agenda = " + idGrupoAgenda + " GROUP BY AGET.id_agenda ORDER BY AGET.id_agenda DESC ";
        }
        queryString += " GROUP BY AGET.id_agenda, AGE.ds_nome, AGET.id ";
        queryString += limiQuery;
        try {
            query = getEntityManager().createNativeQuery(queryString);
            inList = query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }

        for (int i = 0; i < inList.size(); i++) {
            if (i == 0) {
                try {
                    inArray = ((Integer) ((List) inList.get(i)).get(indice)).toString();
                } catch (Exception e) {
                }
            } else {
                try {
                    inArray += ", " + ((Integer) ((List) inList.get(i)).get(indice)).toString();
                } catch (Exception e) {
                }
            }
        }

        try {
            queryString = ""
                    + "           SELECT AGT.*                                      "
                    + "             FROM age_telefone AS AGT                        "
                    + "        LEFT JOIN age_agenda AS AG ON AG.id = AGT.id_agenda  ";
            if (indice == 0) {
                if (!inArray.isEmpty()) {
                    queryString += " WHERE AGT.id_agenda IN (" + inArray + ") ";
                }
            } else if (indice == 2) {
                if (!inArray.isEmpty()) {
                    queryString += " WHERE AGT.id IN (" + inArray + ") ";
                }
            }
            queryString += " ORDER BY AGT.id_agenda DESC ";
            Query q = getEntityManager().createNativeQuery(queryString, AgendaTelefone.class);
            List l = q.getResultList();
            if (!l.isEmpty()) {
                return l;
            }
        } catch (Exception e) {
            e.getMessage();
        }

        // Se nenhum resultado for encotrado (quando não houver vínculo entre agenda e agenda telefone) tenta uma ultima vez realizando uma pesquisa somente na tabela da agenda
        if (list.isEmpty() && !porPesquisa.equals("endereco")) {
            subQueryString = "SELECT AGE.* "
                    + "                FROM age_agenda AS AGE "
                    + "           LEFT JOIN pes_pessoa AS P ON P.id = AGE.id_pessoa ";
            String subQueryStringGrupoAgenda = "";
            if (idGrupoAgenda > 0) {
                // subQueryString += " AND AGE.id_grupo_agenda = " + idGrupoAgenda + " ORDER BY AGE.id DESC ";
                subQueryStringGrupoAgenda = " AND AGE.id_grupo_agenda = " + idGrupoAgenda + " ";
            }
            if (porPesquisa.equals("pessoa")) {
                if (comoPesquisa.equals("Inicial")) {
                    subQueryString += " WHERE " + inGrupoPermitido + " UPPER(TRANSLATE(P.ds_nome)) LIKE '" + descricaoPesquisa + "%' " + subQueryStringGrupoAgenda + " ORDER BY AGE.ds_nome ASC ";
                } else {
                    subQueryString += " WHERE " + inGrupoPermitido + " UPPER(TRANSLATE(P.ds_nome)) LIKE '%" + descricaoPesquisa + "%' " + subQueryStringGrupoAgenda + " ORDER BY AGE.ds_nome ASC ";
                }
            } else if (porPesquisa.equals("nome")) {
                if (comoPesquisa.equals("Inicial")) {
                    subQueryString += " WHERE  " + inGrupoPermitido + " UPPER(TRANSLATE(AGE.ds_nome)) LIKE '" + descricaoPesquisa + "%' " + subQueryStringGrupoAgenda + " ORDER BY AGE.ds_nome ASC ";
                } else {
                    subQueryString += " WHERE  " + inGrupoPermitido + " UPPER(TRANSLATE(AGE.ds_nome)) LIKE '%" + descricaoPesquisa + "%' " + subQueryStringGrupoAgenda + " ORDER BY AGE.ds_nome ASC ";
                }
            }
            try {
                query = getEntityManager().createNativeQuery(subQueryString, Agenda.class);
                List<Agenda> subList = query.getResultList();
                if (!subList.isEmpty()) {
                    for (Agenda agenda : subList) {
                        list.add(new AgendaTelefone(-1, agenda, null, "", "", "", ""));
                    }
                    return list;
                }
            } catch (Exception e) {

            }
            return new ArrayList();
        }

        return new ArrayList();
    }

    public Agenda agendaExiste(Agenda agenda
    ) {
        String queryString = "";
        try {
            if (agenda.getPessoa() != null && agenda.getEndereco() != null && !agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE AGE.pessoa.id = " + agenda.getPessoa().getId() + " AND AGE.endereco.id = " + agenda.getEndereco().getId() + " AND UPPER(AGE.nome) = '" + agenda.getNome().toUpperCase() + "'";
            } else if (agenda.getPessoa() != null && !agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE AGE.pessoa.id = " + agenda.getPessoa().getId() + " AND UPPER(AGE.nome )= '" + agenda.getNome().toUpperCase() + "'";
            } else if (agenda.getEndereco() != null && !agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE AGE.endereco.id = " + agenda.getEndereco().getId() + " AND UPPER(AGE.nome) = '" + agenda.getNome().toUpperCase() + "'";
            } else if (!agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.nome) = '" + agenda.getNome().toUpperCase() + "'";
            }
            if (!queryString.equals("")) {
                Query query = getEntityManager().createQuery(queryString);
                if (!query.getResultList().isEmpty()) {
                    return (Agenda) query.getSingleResult();
                }
            }
        } catch (Exception e) {
            return new Agenda();
        }
        return new Agenda();
    }

    public AgendaTelefone agendaTelefoneExiste(AgendaTelefone agendaTelefone
    ) {
        try {
            Query query = getEntityManager().createQuery(" SELECT AGET FROM AgendaTelefone AS AGET WHERE AGET.telefone = :strTelefone AND AGET.agenda.id = :idAgenda");
            query.setParameter("strTelefone", agendaTelefone.getTelefone());
            query.setParameter("idAgenda", agendaTelefone.getAgenda().getId());
            if (!query.getResultList().isEmpty()) {
                return (AgendaTelefone) query.getSingleResult();
            }
        } catch (Exception e) {
            return new AgendaTelefone();
        }
        return new AgendaTelefone();
    }

    public List DDDAgrupado() {
        try {
            String queryString = " SELECT ds_ddd FROM age_telefone GROUP BY ds_ddd ORDER BY ds_ddd ";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public AgendaFavorito favorito(int idAgenda, int idUsuario
    ) {
        try {
            Query query = getEntityManager().createQuery("SELECT AF FROM AgendaFavorito AS AF WHERE AF.agenda.id = :p1 AND AF.usuario.id = :p2");
            query.setParameter("p1", idAgenda);
            query.setParameter("p2", idUsuario);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AgendaFavorito) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    public List listaFavoritoPorAgenda(int idAgenda
    ) {
        try {
            Query query = getEntityManager().createQuery("SELECT AF FROM AgendaFavorito AS AF WHERE AF.agenda.id = :p1 ");
            query.setParameter("p1", idAgenda);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List listaGrupoAgendaPorGrupoUsuario() {
        try {
            Query query = getEntityManager().createQuery("SELECT AGU.grupoAgenda FROM AgendaGrupoUsuario AS AGU GROUP BY AGU.grupoAgenda");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List listaGrupoAgendaPorUsuario(int idUsuario
    ) {
        try {
            String queryString = ""
                    + " SELECT id FROM age_grupo_agenda                             "
                    + "  WHERE id IN (SELECT id_grupo_agenda                        "
                    + "                 FROM age_grupo_usuario                      "
                    + "                WHERE id_usuario = " + idUsuario + ")            "
                    + "  UNION SELECT id                                            "
                    + "          FROM age_grupo_agenda                              "
                    + "         WHERE id NOT IN (SELECT id_grupo_agenda             "
                    + "                            FROM age_grupo_usuario           "
                    + "                           WHERE id_usuario != " + idUsuario + ")";
            Query queryNative = getEntityManager().createNativeQuery(queryString);
            List listNative = queryNative.getResultList();
            String ids = "";
            for (int i = 0; i < listNative.size(); i++) {
                if (i == 0) {
                    ids = "" + ((List) (listNative.get(i))).get(0).toString();
                } else {
                    ids += ", " + ((List) (listNative.get(i))).get(0).toString();
                }
            }
            if (!listNative.isEmpty()) {
                Query query = getEntityManager().createQuery("SELECT GA FROM GrupoAgenda AS GA WHERE GA.id IN (" + ids + ") ");
                List list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaAniversariantesPorPeriodo() {
        try {
            Query query = getEntityManager().createNativeQuery("SELECT id FROM age_contato WHERE is_notifica_aniversario = TRUE AND TO_CHAR(dt_nascimento, 'MM') = TO_CHAR(current_date, 'MM')");
            List list = query.getResultList();
            String inIds = "";
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        inIds = "" + ((List) list.get(i)).get(0).toString();
                    } else {
                        inIds += ", " + ((List) list.get(i)).get(0).toString();
                    }
                }
                Query query1 = getEntityManager().createQuery("SELECT AC FROM AgendaContato AS AC WHERE AC.id IN (" + inIds + ") ORDER BY AC.nascimento ASC, AC.contato ASC");
                return query1.getResultList();
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
