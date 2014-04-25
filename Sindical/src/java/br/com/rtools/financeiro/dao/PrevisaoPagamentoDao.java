package br.com.rtools.financeiro.dao;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.PrevisaoPagamento;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class PrevisaoPagamentoDao extends DB {

//            String queryStringA = ""
//                    + "     SELECT m.id "
//                    + "       FROM fin_movimento            AS m                                                "
//                    + " INNER JOIN fin_lote                 AS l  ON l.id               = m.id_lote             "
//                    + " INNER JOIN pes_pessoa               AS p  ON p.id               = m.id_pessoa           "
//                    + " INNER JOIN fin_plano5               AS p5 ON p5.id              = m.id_plano5           "
//                    + "  LEFT JOIN fin_previsao_pagamento   AS pr ON pr.id_movimento    = m.id                  "
//                    + "  LEFT JOIN fin_tipo_pagamento       AS t  ON t.id               = pr.id_tipo_pagamento  "
//                    + "  LEFT JOIN fin_conta_banco          AS cb ON cb.id              = pr.id_conta_banco     "
//                    + "  LEFT JOIN fin_banco                AS b  ON b.id               = cb.id_banco           "
//                    + "      WHERE m.ds_es = 'S'                                                                "
//                    + "        AND m.id_baixa IS NULL                                                           "
//                    + "        AND m.dt_vencimento >='" + p1 + "'                                               "
//                    + "        AND m.dt_vencimento <='" + p2 + "'                                               ";
//            Query queryA = getEntityManager().createNativeQuery(queryStringA);
//            List listA = queryA.getResultList();
//            if (!listA.isEmpty()) {
//                String inIds = "";
//                for (int i = 0; i < listA.size(); i++) {
//                    if (i == 0) {
//                        inIds = "" + ((List) listA.get(i)).get(0).toString();
//                    } else {
//                        inIds += ", " + ((List) listA.get(i)).get(0).toString();
//                    }
//                }
    public List<PrevisaoPagamento> listaPrevisaoPagamento(String p1, String p2) {
        try {
            List<PrevisaoPagamento> listaPrevisaoPagamentos = new ArrayList<PrevisaoPagamento>();
            Query queryMovimentos = getEntityManager().createQuery(" SELECT M FROM Movimento AS M WHERE M.baixa IS NULL AND M.es = 'S' AND M.dtVencimento BETWEEN :p1 AND :p2 AND M.id NOT IN ( SELECT PP.movimento.id FROM PrevisaoPagamento AS PP )");
            queryMovimentos.setParameter("p1", DataHoje.converte(p1), TemporalType.DATE);
            queryMovimentos.setParameter("p2", DataHoje.converte(p2), TemporalType.DATE);
            List<Movimento> listMovimentos = (List<Movimento>) queryMovimentos.getResultList();
            PrevisaoPagamento pp = new PrevisaoPagamento();
            for (Movimento m : listMovimentos) {
                pp.setMovimento(m);
                listaPrevisaoPagamentos.add(pp);
                pp = new PrevisaoPagamento();
            }
            Query querypp = getEntityManager().createQuery(" SELECT PP FROM PrevisaoPagamento AS PP WHERE PP.movimento.dtVencimento BETWEEN :p1 AND :p2  ORDER BY PP.tipoPagamento.descricao ASC, PP.movimento.dtVencimento DESC, PP.movimento.pessoa.nome ASC ");
            querypp.setParameter("p1", DataHoje.converte(p1), TemporalType.DATE);
            querypp.setParameter("p2", DataHoje.converte(p2), TemporalType.DATE);
            listaPrevisaoPagamentos.addAll((List<PrevisaoPagamento>) querypp.getResultList());
            if (!listaPrevisaoPagamentos.isEmpty()) {
                return listaPrevisaoPagamentos;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    /**
     * Uso: Relatório Previsão de Pagamentos
     *
     * @param p1
     * @param p2
     * @param relatorio - default true
     * @return
     */
    public List listaPrevisaoPagamento(String p1, String p2, boolean relatorio) {
        try {
            String queryString = ""
                    + "        SELECT                                                                                                       "
            /* 0 */ + "               to_char(L.dt_emissao, 'DD/MM/YYYY')               AS fin_lote_dt_emissao,                             "
            /* 1 */ + "               L.nr_valor 					AS fin_lote_nr_valor,                               "
            /* 2 */ + "               P.ds_nome 					AS pes_ds_nome,                                     "
            /* 3 */ + "               P5.ds_conta || ' - ' || P5.ds_numero 		AS fin_plano_5,                                     "
            /* 4 */ + "               L.ds_documento 	 				AS fin_lote_ds_documento,                           "
            /* 5 */ + "               to_char(M.dt_vencimento, 'DD/MM/YYYY') 		AS fin_movimento_dt_vencimento,                     "
            /* 6 */ + "               M.nr_valor + (M.nr_juros + M.nr_multa + M.nr_correcao) - M.nr_desconto AS nr_valor_devido,            "
            /* 7 */ + "               TP.ds_descricao 					AS fin_tipo_documento_ds_descricao,                  "
            /* 8 */ + "               TP.id                                             AS fin_tipo_documento_id                            "
                    + "          FROM fin_previsao_pagamento 				AS PP                                               "
                    + "    INNER JOIN fin_movimento 					AS M  ON M.id  = PP.id_movimento                    "
                    + "    INNER JOIN fin_lote 						AS L  ON L.id  = M.id_lote                          "
                    + "     LEFT JOIN fin_conta_banco 					AS CB ON CB.id = PP.id_conta_banco                  "
                    + "    INNER JOIN fin_tipo_pagamento 				AS TP ON TP.id = PP.id_tipo_pagamento               "
                    + "    INNER JOIN fin_plano5 					AS P5 ON P5.id = M.id_plano5                        "
                    + "    INNER JOIN pes_pessoa 					AS P  ON P.id  = M.id_pessoa                        "
                    + "         WHERE M.dt_vencimento BETWEEN '"+p1+"' AND '"+p2+"'                                                         "     
                    + "      ORDER BY TP.ds_descricao ASC,                                                                                  "
                    + "               M.dt_vencimento DESC,                                                                                 "
                    + "               P.ds_nome ASC                                                                                         ";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
