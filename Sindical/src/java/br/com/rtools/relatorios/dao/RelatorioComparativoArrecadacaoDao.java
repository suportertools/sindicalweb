package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioComparativoArrecadacaoDao extends DB {

    private String order = "";

    /**
     *
     * @param relatorios
     * @param empresa
     * @param contabilidade
     * @param servico
     * @param tipoServico
     * @param referencia1
     * @param tipo1
     * @param referencia2
     * @param tipo2
     * @param inConvencao
     * @param inGrupoCidade
     * @param inCnaes
     * @param inCidadeBase
     * @param percentual
     * @return
     */
    public List find(Relatorios relatorios, Integer empresa, Integer contabilidade, Integer servico, Integer tipoServico, String referencia1, String tipo1, String referencia2, String tipo2, String inConvencao, String inGrupoCidade, String inCnaes, String inCidadeBase, Integer percentual) {
        try {
            String queryString;
            queryString = ""
                    + "     SELECT p.ds_documento                      AS cnpj,                                                                             "
                    + "            P.ds_nome                           AS empresa,                                                                          "
                    + "            S.ds_descricao                      AS contribuicao,                                                                     "
                    + "            M1.ds_referencia                    AS referencia1,                                                                      "
                    + "            func_nulldouble(M1.nr_valor_baixa)  AS valor1,                                                                           "
                    + "            M2.ds_referencia                    AS referencia2,                                                                      "
                    + "            func_nulldouble(M2.nr_valor_baixa)  AS valor2,                                                                           "
                    + "      to_char(                                                                                                                       "
                    + "      (CASE WHEN func_nulldouble(M2.nr_valor_baixa) > 0 AND func_nulldouble(M1.nr_valor_baixa) = 0 THEN   100                        "
                    + "            WHEN func_nulldouble(M2.nr_valor_baixa) = 0 AND func_nulldouble(M1.nr_valor_baixa) > 0 THEN - 100                        "
                    + "            WHEN func_nulldouble(M2.nr_valor_baixa) = 0 AND func_nulldouble(M1.nr_valor_baixa) = 0 THEN     0                        "
                    + "            WHEN func_nulldouble(M2.nr_valor_baixa) > 0 AND func_nulldouble(M1.nr_valor_baixa) > 0 THEN                              "
                    + "                (func_nulldouble(M2.nr_valor_baixa) - func_nulldouble(M1.nr_valor_baixa)) / func_nulldouble(M2.nr_valor_baixa) * 100 "
                    + "       END), 'FM999999999.00') AS percentual                                                                                         "
                    + "       FROM pes_pessoa               AS P                                                                                            "
                    + " INNER JOIN arr_contribuintes_vw     AS C  ON C.id_pessoa  = P.id AND C.dt_inativacao IS NULL                                        "
                    + " INNER JOIN pes_juridica             AS J  ON J.id_pessoa  = P.id                                                                    "
                    + " INNER JOIN pes_pessoa_endereco      AS PE ON PE.id_pessoa = P.id AND PE.id_tipo_endereco = 2                                        "
                    + " INNER JOIN end_endereco             AS ENDE ON ENDE.id    = PE.id_endereco                                                          "
                    + "  LEFT JOIN fin_movimento            AS M1 ON                                                                                        "
                    + "            M1.id_pessoa             = P.id                                                                                              "
                    + "            AND M1.id_servicos       = " + servico + "                                                                                      "
                    + "            AND M1.id_tipo_servico   = 1                                                                                               "
                    + "            AND M1.ds_referencia     = '" + referencia1 + "'                                                                             "
                    + "            AND M1.is_ativo          = true                                                                                                   "
                    + "  LEFT JOIN fin_movimento            AS M2 ON                                                                                        "
                    + "            M2.id_pessoa             = P.id                                                                                                      "
                    + "            AND M2.id_servicos       = " + servico + "                                                                                      "
                    + "            AND M2.id_tipo_servico   = 1                                                                                               "
                    + "            AND M2.ds_referencia     = '" + referencia2 + "'                                                                             "
                    + "            AND M2.is_ativo          = true                                                                                                   "
                    + "  LEFT JOIN fin_servicos             AS S  ON S.id         = " + servico + "                                                         "
                    + "      WHERE M1.is_ativo = true                                                                                                           "
                    + "            " + tipo(1, tipo1)
                    + "            " + tipo(2, tipo2)
                    + "            AND P.id > 0 ";
                        
            if (!inConvencao.isEmpty()) {
                queryString += " AND C.id_convencao IN(" + inConvencao + ")";
            }
            if (!inGrupoCidade.isEmpty()) {
                queryString += " AND C.id_grupo_cidade IN(" + inGrupoCidade + ")";
            }
            if (!inCnaes.isEmpty()) {
                queryString += " AND J.id_cnae IN(" + inCnaes + ")";
            }
            if (!inCidadeBase.isEmpty()) {
                queryString += " AND ENDE.id_cidade IN(" + inCidadeBase + ")";
            }
            if (contabilidade != null) {
                queryString += " AND C.id_contabilidade = " + contabilidade;
            }
            if (empresa != null) {
                queryString += " AND C.id_juridica = " + empresa;
            }
            if (percentual != null) {
                if (percentual > 0) {
                    queryString += " "
                            + " AND (                                                                                               "
                            + "         (                                                                                           "
                            + "             ( func_nulldouble(M2.nr_valor_baixa) > 0 AND func_nulldouble(M1.nr_valor_baixa) > 0     "
                            + "                 AND ( func_nulldouble(M2.nr_valor_baixa) - func_nulldouble(M1.nr_valor_baixa)       "
                            + "                 ) / func_nulldouble(M2.nr_valor_baixa) * 100 > " + percentual
                            + "             )                                                                                       "
                            + "         ) OR func_nulldouble (                                                                      "
                            + "             M2.nr_valor_baixa                                                                       "
                            + "         ) > 0 AND func_nulldouble(M1.nr_valor_baixa) = 0                                            "
                            + " )";
                } else if (percentual < 0) {
                    queryString += ""
                            + " AND (                                                                                               "
                            + "         (                                                                                           "
                            + "             ( func_nulldouble(M2.nr_valor_baixa) > 0 AND func_nulldouble(M1.nr_valor_baixa) > 0     "
                            + "                 AND ( func_nulldouble(M2.nr_valor_baixa) - func_nulldouble(M1.nr_valor_baixa)       "
                            + "                 ) / func_nulldouble(M2.nr_valor_baixa) * 100 < " + percentual
                            + "             )                                                                                       "
                            + "         ) OR func_nulldouble(M2.nr_valor_baixa) = 0 AND func_nulldouble(M1.nr_valor_baixa) > 0      "
                            + " )";
                } else if (percentual == 0) {
                    queryString += " AND func_nulldouble(M2.nr_valor_baixa) = 0 AND func_nulldouble(M1.nr_valor_baixa) = 0 ";
                }
            }
            if (!relatorios.getQry().isEmpty()) {
                queryString += " " + relatorios.getQry();
            }
            if (!relatorios.getQryOrdem().isEmpty()) {
                queryString += " ORDER BY " + relatorios.getQry();
            } else {
                queryString += " ORDER BY P.ds_nome ASC ";
            }
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

    public String tipo(Integer tCase, String tipo) {
        switch (tipo) {
            case "baixado":
                return " AND M" + tCase + ".id_baixa IS NOT NULL ";
            case "nao_baixado":
                return " AND M" + tCase + ".id_baixa IS NULL ";
            default:
                return "";
        }
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
