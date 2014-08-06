package br.com.rtools.sistema.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class EmailDao extends DB {

    public List<EmailPessoa> findEmail(int idRotina, Date dateStart, Date dateFinish, String filterBy, String descricaoPesquisa, String orderBy) {
        List listQuery = new ArrayList();
        boolean isDate = false;
        boolean isDateFinish = false;
        try {
            if (idRotina > 0) {
                listQuery.add("EP.email.rotina.id = " + idRotina);
            }
            if (dateStart != null) {
                isDate = true;
                if (dateFinish != null) {
                    listQuery.add("EP.email.data BETWEEN :dateStart AND :dateFinish");
                    isDateFinish = true;
                } else {
                    listQuery.add("EP.email.data = :dateStart");
                }
            }
            if (filterBy != null && !filterBy.isEmpty()) {
                if (!descricaoPesquisa.isEmpty()) {
                    if (filterBy.equals("email")) {
                        listQuery.add(
                                " ( UPPER(EP.pessoa.email1)    LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "
                                + " OR UPPER(EP.destinatario)  LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "
                                + " OR UPPER(EP.cc)            LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "
                                + " OR UPPER(EP.bcc)           LIKE '%" + descricaoPesquisa.toUpperCase() + "%' "
                                + ") ");

                    } else if (filterBy.equals("assunto")) {
                        listQuery.add(" UPPER(EP.email.assunto) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' ");
                    } else if (filterBy.equals("pessoa")) {
                        listQuery.add(" EP.pessoa IS NOT NULL AND UPPER(EP.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' ");
                    }
                }

            }
            Usuario u = (Usuario) GenericaSessao.getObject("sessaoUsuario");
            String queryString = " SELECT EP FROM EmailPessoa AS EP WHERE EP.email.rascunho = false AND EP.email.usuario.id = " + u.getId() + " ";
            if (!listQuery.isEmpty()) {
                queryString += "  ";
                for (Object o : listQuery) {
                    queryString += " AND " + o.toString();
                }
            }
            if (orderBy != null && !orderBy.isEmpty()) {
                queryString += " ORDER BY " + orderBy + " , EP.id DESC  ";
            } else {
                queryString += " ORDER BY EP.email.data DESC, EP.email.hora DESC, EP.horaSaida DESC, EP.id DESC ";
            }
            Query query = getEntityManager().createQuery(queryString);
            if (isDate) {
                query.setParameter("dateStart", dateStart, TemporalType.DATE);
                if (isDateFinish) {
                    query.setParameter("dateFinish", dateFinish, TemporalType.DATE);
                }
            }
            query.setMaxResults(2000);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List<EmailPessoa> findRascunho() {
        try {
            Usuario u = (Usuario) GenericaSessao.getObject("sessaoUsuario");
            Query query = getEntityManager().createQuery("SELECT EP FROM EmailPessoa AS EP WHERE EP.email.rascunho = TRUE ");
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
