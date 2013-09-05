package br.com.rtools.agenda.db;

import br.com.rtools.principal.DB;
import br.com.rtools.agenda.Agenda;
import br.com.rtools.agenda.AgendaTelefone;
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
            query = " SELECT AGE FROM Agenda AS AGE WHERE AGE.grupoAgenda.id = "+idGrupoAgenda+" ORDER BY ID DESC ";
            queryFiltroGrupoAgendaA = " AND AGE.grupoAgenda.id = "+idGrupoAgenda+" ";
            queryFiltroGrupoAgendaB = " AND AGET.agenda.grupoAgenda.id = "+idGrupoAgenda+" ";
        }
        try {
            if (porPesquisa.equals("pessoa")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.pessoa.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGE.nome ASC ";
                } else {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGE.nome ASC ";
                }
            } else if (porPesquisa.equals("nome")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGE.nome ASC ";
                } else {
                    query = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGE.nome ASC ";
                }
            } else if (porPesquisa.equals("contato")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGET.agenda FROM AgendaTelefone AS AGET WHERE UPPER(AGET.contato) LIKE '" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaB+" ORDER BY AGET.contato ASC ";
                } else {
                    query = " SELECT AGET.agenda FROM AgendaTelefone AS AGET WHERE UPPER(AGET.contato) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaB+"  ORDER BY AGET.contato ASC  ";
                }
            } else if (porPesquisa.equals("telefoneSP")) {
                String dddString = "";
                if (!ddd.equals("")) {
                    dddString  = " AGET.ddd = '" + ddd + "' AND ";
                }
                query = " SELECT AGET.agenda FROM AgendaTelefone AS AGET WHERE "+dddString+" AGET.telefone = '" + descricaoPesquisa.toUpperCase() + "'"+ queryFiltroGrupoAgendaB;
            } else if (porPesquisa.equals("endereco")) {
                String queryEndereco = ""
                        +"     SELECT age.id FROM age_agenda age"
                        +" INNER JOIN endereco_vw ende ON ende.id = age.id_endereco                                                                                                         "
                        +"WHERE UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || bairro || ', ' || cidade || ', ' || uf)    LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade || ', ' || uf)                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade )                                   LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.logradouro || ' ' || ende.endereco)                                                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.endereco)                                                                                LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.cidade)                                                                                  LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.cep)                                                                                     = '" + descricaoPesquisa.toUpperCase() +              "'";
                
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
                    query = " SELECT AGE FROM Agenda AS AGE WHERE AGE.id IN("+listaId+")";
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
    public List pesquisaAgendaTelefone(String ddd, String descricaoPesquisa, String porPesquisa, String comoPesquisa, int idGrupoAgenda) {
        List list = new ArrayList();
        String queryFiltroGrupoAgendaA = "";
        String queryFiltroGrupoAgendaB = "";
        String query = " SELECT AGET FROM AgendaTelefone AS AGET ORDER BY AGET.agenda.id DESC ";
        if (idGrupoAgenda > 0) {
            query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE AGET.agenda.grupoAgenda.id = "+idGrupoAgenda+" ORDER BY AGET.agenda.id DESC ";
            queryFiltroGrupoAgendaA = " AND AGET.grupoAgenda.id = "+idGrupoAgenda+" ";
            queryFiltroGrupoAgendaB = " AND AGET.agenda.grupoAgenda.id = "+idGrupoAgenda+" ";
        }
        try {
            if (porPesquisa.equals("pessoa")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE UPPER(AGET.agenda.pessoa.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGET.agenda.nome ASC ";
                } else {
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE UPPER(AGET.agenda.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGET.agenda.nome ASC ";
                }
            } else if (porPesquisa.equals("nome")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE UPPER(AGET.agenda.nome) LIKE '" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGET.agenda.nome ASC ";
                } else {
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE UPPER(AGET.agenda.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaA+" ORDER BY AGET.agenda.nome ASC ";
                }
            } else if (porPesquisa.equals("contato")) {
                if (comoPesquisa.equals("Inicial")) {
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE UPPER(AGET.agenda.contato) LIKE '" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaB+" ORDER BY AGET.agenda.contato ASC ";
                } else {
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE UPPER(AGET.agenda.contato) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "+queryFiltroGrupoAgendaB+"  ORDER BY AGET.agenda.contato ASC  ";
                }
            } else if (porPesquisa.equals("telefoneSP")) {
                String dddString = "";
                if (!ddd.equals("")) {
                    dddString  = " AGET.ddd = '" + ddd + "' AND ";
                }
                query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE "+dddString+" AGET.telefone = '" + descricaoPesquisa.toUpperCase() + "'"+ queryFiltroGrupoAgendaB;
            } else if (porPesquisa.equals("endereco")) {
                String queryEndereco = ""
                        +"     SELECT age.id FROM age_agenda age"
                        +" INNER JOIN endereco_vw ende ON ende.id = age.id_endereco                                                                                                         "
                        +"WHERE UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || bairro || ', ' || cidade || ', ' || uf)    LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade || ', ' || uf)                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.logradouro || ' ' || ende.endereco || ', ' || cidade )                                   LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.logradouro || ' ' || ende.endereco)                                                      LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.endereco)                                                                                LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.cidade)                                                                                  LIKE UPPER('%" + descricaoPesquisa.toUpperCase() + "%') "
                        +"   OR UPPER(ende.cep)                                                                                     = '" + descricaoPesquisa.toUpperCase() +              "'";
                
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
                    query = " SELECT AGET FROM AgendaTelefone AS AGET WHERE AGET.agenda.id IN("+listaId+")";
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
    public Agenda agendaExiste(Agenda agenda) {
        String queryString = "";
        try {
            if (agenda.getPessoa() != null && agenda.getEndereco()  != null && !agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE AGE.pessoa.id = "+agenda.getPessoa().getId()+" AND AGE.endereco.id = "+agenda.getEndereco().getId()+" AND UPPER(AGE.nome) = '"+agenda.getNome().toUpperCase()+"'";
            } else if (agenda.getPessoa() != null && !agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE AGE.pessoa.id = "+agenda.getPessoa().getId()+" AND UPPER(AGE.nome )= '"+agenda.getNome().toUpperCase()+"'";
            } else if (agenda.getEndereco() != null && !agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE AGE.endereco.id = "+agenda.getEndereco().getId()+" AND UPPER(AGE.nome) = '"+agenda.getNome().toUpperCase()+"'";
            } else if (!agenda.getNome().equals("")) {
                queryString = " SELECT AGE FROM Agenda AS AGE WHERE UPPER(AGE.nome) = '"+agenda.getNome().toUpperCase()+"'";
            }
            if(!queryString.equals("")) {
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
}
