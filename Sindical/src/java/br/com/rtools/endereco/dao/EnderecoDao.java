package br.com.rtools.endereco.dao;

import br.com.rtools.endereco.*;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class EnderecoDao extends DB {

    public List<Endereco> pesquisaEnderecoCep(String cep) {
        try {
            Query qry = getEntityManager().createQuery("SELECT ENDE FROM Endereco AS ENDE WHERE ENDE.cep = :d_cep AND ENDE.ativo = true ORDER BY ENDE.descricaoEndereco.descricao");
            qry.setParameter("d_cep", cep);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaEnderecoDes(String uf, String cidade, String logradouro, String descricao, String iniParcial) {
        try {
            if (iniParcial.equals("I")) {
                descricao = descricao.trim() + "%";
            } else {
                descricao = "%" + descricao.trim() + "%";
            }
            String queryString
                    = "     SELECT E.*                                                  \n"
                    + "     FROM end_endereco E,                                        \n"
                    + "          end_cidade cid,                                        \n"
                    + "          end_logradouro logr,                                   \n"
                    + "          end_descricao_endereco des                             \n"
                    + "    WHERE E.id_cidade = cid.id                                   \n"
                    + "      AND E.id_logradouro = logr.id                              \n"
                    + "      AND E.id_descricao_endereco = des.id                       \n"
                    + "      AND UPPER(cid.ds_cidade) = UPPER('" + cidade + "')         \n"
                    + "      AND cid.ds_uf = '" + uf + "'                               \n"
                    + "      AND UPPER(logr.ds_descricao) = UPPER('" + logradouro + "')\n"
                    + "      AND TRIM(UPPER(func_translate(des.ds_descricao))) LIKE TRIM(UPPER(func_translate('" + descricao + "'))) \n"
                    + "      AND E.is_ativo = true                              \n"
                    + " ORDER BY des.ds_descricao";
            Query qry = getEntityManager().createNativeQuery(queryString, Endereco.class);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (EJBQLException e) {
            return null;
        }
        return new ArrayList();
    }

    public List pesquisaEndereco(Integer logradouro_id, Integer descricao_endereco_id, Integer bairro_id, Integer cidade_id) {
        return pesquisaEndereco(logradouro_id, descricao_endereco_id, bairro_id, cidade_id, null);
    }

    public List pesquisaEndereco(Integer logradouro_id, Integer descricao_endereco_id, Integer bairro_id, Integer cidade_id, Boolean ativo) {
        List listQuery = new ArrayList();
        try {
            String queryString
                    = "SELECT E.*               \n "
                    + "  FROM end_endereco AS E \n ";
            if (logradouro_id != null) {
                listQuery.add("E.id_logradouro = " + logradouro_id);
            }
            if (descricao_endereco_id != null) {
                listQuery.add("E.id_descricao_endereco= " + descricao_endereco_id);
            }
            if (bairro_id != null) {
                listQuery.add("E.id_bairro = " + bairro_id);
            }
            if (cidade_id != null) {
                listQuery.add("E.id_cidade = " + cidade_id);
            }
            if (ativo != null) {
                listQuery.add("E.is_ativo = " + ativo);
            }
            for (int i = 0; i < listQuery.size(); i++) {
                if (i == 0) {
                    queryString += " WHERE ";
                } else {
                    queryString += " AND ";
                }
                queryString += " " + listQuery.get(i).toString() + " \n ";
            }
            Query query = getEntityManager().createNativeQuery(queryString, Endereco.class);
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
