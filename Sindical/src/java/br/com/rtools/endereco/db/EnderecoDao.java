package br.com.rtools.endereco.db;

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
                descricao = descricao.toLowerCase().toUpperCase() + "%";
            } else {
                descricao = "%" + descricao.toLowerCase().toUpperCase() + "%";
            }
            String queryString
                    = "     SELECT ende.*                                         \n"
                    + "     FROM end_endereco ende,                             \n"
                    + "          end_cidade cid,                                \n"
                    + "          end_logradouro logr,                           \n"
                    + "          end_descricao_endereco des                     \n"
                    + "    WHERE ende.id_cidade = cid.id                        \n"
                    + "      AND ende.id_logradouro = logr.id                   \n"
                    + "      AND ende.id_descricao_endereco = des.id            \n"
                    + "      AND cid.ds_cidade = '" + cidade + "'               \n"
                    + "      AND cid.ds_uf = '" + uf + "'                       \n"
                    + "      AND logr.ds_descricao = '" + logradouro + "'       \n"
                    + "      AND UPPER(func_translate(des.ds_descricao)) LIKE '" + AnaliseString.removerAcentos(descricao) + "' \n"
                    + "      AND ende.is_ativo = true                           \n"
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

    public List pesquisaEndereco(Integer idDescricao, Integer idCidade, Integer idBairro, Integer idLogradouro) {
        try {
            Query qry = getEntityManager().createQuery("select ende "
                    + "  from Endereco ende "
                    + " where ende.descricaoEndereco.id = :idDesc "
                    + "   and ende.cidade.id = :idCid "
                    + "   and ende.bairro.id = :idBai "
                    + "   and ende.logradouro.id = :idLog");
            qry.setParameter("idDesc", idDescricao);
            qry.setParameter("idCid", idCidade);
            qry.setParameter("idBai", idBairro);
            qry.setParameter("idLog", idLogradouro);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
