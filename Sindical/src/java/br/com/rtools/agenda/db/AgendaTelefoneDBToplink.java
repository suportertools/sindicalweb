package br.com.rtools.agenda.db;

import br.com.rtools.agenda.Agenda;
import br.com.rtools.agenda.AgendaFavorito;
import br.com.rtools.agenda.AgendaTelefone;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AgendaTelefoneDBToplink extends DB implements AgendaTelefoneDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT age FROM Agenda age ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
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

    @Override
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

    @Override
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
                query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " AGET.agenda.grupoAgenda.id = " + idGrupoAgenda + " ORDER BY AGET.agenda.id DESC ";
                queryFiltroGrupoAgendaA = " AND AGET.agenda.grupoAgenda.id = " + idGrupoAgenda + " ";
            }
            if (!isFavoritos) {
                if (porPesquisa.equals("pessoa")) {
                    if (comoPesquisa.equals("Inicial")) {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.agenda.pessoa.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGET.agenda.nome ASC ";
                    } else {
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " UPPER(AGET.agenda.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' " + queryFiltroGrupoAgendaA + " ORDER BY AGET.agenda.nome ASC ";
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
                        query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE " + idGAsAgendaTAnd + " AGET.agenda.id IN(" + listaId + ")";
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
                        subQuery = " SELECT AGET FROM Agenda AS AGE WHERE AGE.grupoAgenda.id = " + idGrupoAgenda + " ORDER BY AGE.id DESC ";
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

    @Override
    public Agenda agendaExiste(Agenda agenda) {
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

    @Override
    public AgendaTelefone agendaTelefoneExiste(AgendaTelefone agendaTelefone) {
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

    @Override
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

    @Override
    public AgendaFavorito favorito(int idAgenda, int idUsuario) {
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

    @Override
    public List listaFavoritoPorAgenda(int idAgenda) {
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

    @Override
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

    @Override
    public List listaGrupoAgendaPorUsuario(int idUsuario) {
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

    @Override
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
                Query query1 = getEntityManager().createQuery("SELECT AC FROM AgendaContato AS AC WHERE AC.id IN ("+inIds+") ORDER BY AC.nascimento ASC, AC.contato ASC");
                return query1.getResultList();
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
