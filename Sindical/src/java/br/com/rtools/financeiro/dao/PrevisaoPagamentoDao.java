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
            Query querypp = getEntityManager().createQuery(" SELECT PP FROM PrevisaoPagamento AS PP WHERE PP.movimento.dtVencimento BETWEEN :p1 AND :p2 ");
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

}
