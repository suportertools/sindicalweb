package br.com.rtools.pessoa.db;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class EnviarArquivosDBToplink extends DB implements EnviarArquivosDB {

    @Override
    public Juridica pesquisaCodigo(int id) {
        Juridica result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Juridica.pesquisaID");
            qry.setParameter("pid", id);
            result = (Juridica) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaContabilidades() {
        String textQuery = "";
        try {
            textQuery = "     SELECT jc.id,                                           "
                    + "            p.ds_nome as nome,                               "
                    + "            p.ds_telefone1 as telefone,                      "
                    + "            count(*) qtde,                                   "
                    + "            p.ds_email1 as email                             "
                    + "       FROM arr_contribuintes_vw as c                        "
                    + " INNER JOIN pes_juridica as jc on jc.id = c.id_contabilidade "
                    + " INNER JOIN pes_pessoa as p on p.id = jc.id_pessoa           "
                    + "      WHERE c.dt_inativacao is null                          "
                    + "        AND length(rtrim(p.ds_email1)) > 0                   "
                    + "   GROUP BY jc.id,                                           "
                    + "            p.ds_nome,                                       "
                    + "            p.ds_telefone1,                                  "
                    + "            ds_email1                                        "
                    + "   ORDER BY p.ds_nome                                        ";

            Query qry = getEntityManager().createNativeQuery(textQuery);
            return qry.getResultList();

        } catch (EJBQLException e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaContribuintes(String listaConvencao, String listaGrupoCidade, String listaCnae) {

        String caso = "";
        String inStringCnae = "";

        if (!listaConvencao.isEmpty() && caso.equals("")) {
            caso = "3";
            inStringCnae += " AND c.id_convencao IN (" + listaConvencao + ") ";
        }
        if (!listaGrupoCidade.isEmpty() && caso.equals("")) {
            caso = "2";
            inStringCnae += " AND c.id_grupo_cidade IN(" + listaGrupoCidade + ") ";
        }
        if (!listaCnae.isEmpty()) {
            caso = "1";
            inStringCnae += " AND j.id_cnae IN(" + listaCnae + ") ";
        }
        String textQuery = "";
        String textQuery1;
        try {

            textQuery1 = "     SELECT c.id_juridica,                            "
                    + "            p.ds_nome as nome,                           "
                    + "            p.ds_telefone1 as telefone,                  "
                    + "            p.ds_email1 as email                         "
                    + "       FROM arr_contribuintes_vw as c                    "
                    + " INNER JOIN pes_pessoa as p on p.id = c.id_pessoa        "
                    + " INNER JOIN pes_juridica AS j on j.id = c.id_juridica    "
                    + " INNER JOIN pes_cnae as cn ON cn.id = j.id_cnae          "
                    + "      WHERE c.dt_inativacao is null                      "
                    + "        AND length(rtrim(p.ds_email1)) > 0               ";

            if (caso.equals("1")) {
                textQuery += textQuery1 + "  " + inStringCnae + " ";
            } else if (caso.equals("2")) {
                textQuery += textQuery1 + " " + inStringCnae + " ";
            } else if (caso.equals("3")) {
                textQuery += textQuery1 + " " + inStringCnae + " ";
            } else {
                textQuery += textQuery1;
            }

            textQuery += inStringCnae + "   ORDER BY p.ds_nome  ;";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List<Convencao> listaConvencao() {
        String textQuery;
        try {

            textQuery = "   SELECT c.id_convencao                 "
                    + "     FROM arr_contribuintes_vw AS c      "
                    + " GROUP BY c.id_convencao                 "
                    + " ORDER BY c.id_convencao                 ";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                if (!list.isEmpty()) {
                    String idConvencao = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            idConvencao = ((List) list.get(i)).get(0).toString();
                        } else {
                            idConvencao += ", " + ((List) list.get(i)).get(0).toString();
                        }
                    }
                    Query qryConvencoes = getEntityManager().createQuery(" SELECT con FROM Convencao AS con WHERE con.id IN (" + idConvencao + ")");
                    List list1 = qryConvencoes.getResultList();
                    if (!list1.isEmpty()) {
                        return list1;
                    }
                }
                return list;
            }
        } catch (EJBQLException e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List<GrupoCidade> listaGrupoCidadePorConvencao(String listaConvencao) {
        String textQuery;
        String filtroPorConvencao = "";
        if (!listaConvencao.isEmpty()) {
            filtroPorConvencao = " WHERE c.id_convencao in (" + listaConvencao + ") ";
        }
        try {

            textQuery = "   SELECT c.id_grupo_cidade          "
                    + "     FROM arr_contribuintes_vw AS c  "
                    + filtroPorConvencao
                    + " GROUP BY c.id_grupo_cidade          "
                    + " ORDER BY c.id_grupo_cidade          ";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                String idGrupoCidade = "";
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        idGrupoCidade = ((List) list.get(i)).get(0).toString();
                    } else {
                        idGrupoCidade += ", " + ((List) list.get(i)).get(0).toString();
                    }
                }
                Query qryGrupoCidade = getEntityManager().createQuery(" SELECT gc FROM GrupoCidade AS gc WHERE gc.id IN(" + idGrupoCidade + ")");
                List list1 = qryGrupoCidade.getResultList();
                if (!list1.isEmpty()) {
                    return list1;
                }
            }
        } catch (EJBQLException e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List<Cnae> listaCnaePorConvencao(String listaConvencao) {
        String textQuery = "";
        String filtroPorConvencao = "";
        if (!listaConvencao.isEmpty()) {

            filtroPorConvencao = " WHERE c.id_convencao in (" + listaConvencao + ")   ";
        }
        try {
            textQuery = "     SELECT cn.id                                      "
                    + "       FROM arr_contribuintes_vw AS c                  "
                    + " INNER JOIN pes_juridica AS j on j.id = c.id_juridica  "
                    + " INNER JOIN pes_cnae AS cn on cn.id = j.id_cnae        "
                    + filtroPorConvencao
                    + "   GROUP BY cn.id                                      "
                    + "   ORDER BY cn.id                                      ";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                String idCnaeConvencao = "";
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        idCnaeConvencao = ((List) list.get(i)).get(0).toString();
                    } else {
                        idCnaeConvencao += ", " + ((List) list.get(i)).get(0).toString();
                    }
                }
                Query qryGrupoCidade = getEntityManager().createQuery(" SELECT C FROM Cnae AS C WHERE C.id IN(" + idCnaeConvencao + ") ");
                List list1 = qryGrupoCidade.getResultList();
                if (!list1.isEmpty()) {
                    return list1;
                }
            }
        } catch (EJBQLException e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}