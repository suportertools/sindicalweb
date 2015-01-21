package br.com.rtools.relatorios.dao;

import br.com.rtools.arrecadacao.RepisMovimento;
import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioCertificadosDao extends DB {

    private String order = "";

    /**
     *
     * @param relatorios
     * @param empresa
     * @param tipo
     * @param referencia
     * @param inRepisStatus
     * @param inCertidoesTipo
     * @return
     */
    public List find(Relatorios relatorios, Integer empresa, Integer tipo, String referencia[], String inRepisStatus, String inCertidoesTipo, String inCidadeBase) {
        try {
            String queryString;
            List listQuery = new ArrayList();
            queryString = ""
                    + "      SELECT P.ds_documento,                                                                 " // 0
                    + "             P.ds_nome,                                                                      " // 1
                    + "             C.ds_cidade,                                                                    " // 2
                    + "             CT.ds_descricao,                                                                " // 3
                    + "             RS.ds_descricao,                                                                " // 4
                    + "             RM.dt_emissao,                                                                  " // 5
                    + "             RM.dt_resposta,                                                                 " // 6
                    + "             RM.nr_ano                                                                       " // 7
                    + "        FROM arr_repis_movimento     AS RM                                                   "
                    + "  INNER JOIN pes_pessoa              AS P    ON P.id     = RM.id_pessoa                      "
                    + "  INNER JOIN pes_pessoa_endereco     AS PE   ON PE.id    = P.id                              "
                    + "  INNER JOIN end_endereco            AS ENDE ON ENDE.id  = PE.id_endereco                    "
                    + "  INNER JOIN end_cidade              AS C    ON C.id     = ENDE.id_cidade                    "
                    + "  INNER JOIN arr_repis_status        AS RS   ON RS.id    = RM.id_repis_status                "
                    + "  INNER JOIN arr_certidao_tipo       AS CT   ON CT.id    = RM.id_certidao_tipo               "
                    + "";
            if (tipo != null) {
                listQuery.add("PE.id_tipo_endereco = 2");
                if (tipo == 1) {
                    listQuery.add("RM.nr_ano = " + referencia[0]);
                } else if (tipo == 2) {
                    if (referencia[1].isEmpty()) {
                        listQuery.add("RM.dt_emissao = '" + DataHoje.converteData(referencia[0]) + "'");
                    } else {
                        listQuery.add("RM.dt_emissao BETWEEN '" + DataHoje.converteData(referencia[0]) + "' AND '" + DataHoje.converteData(referencia[1]) + "'");
                    }
                } else if (tipo == 3) {
                    if (referencia[1].isEmpty()) {
                        listQuery.add("RM.dt_resposta = '" + DataHoje.converteData(referencia[0]) + "'");
                    } else {
                        listQuery.add("RM.dt_resposta BETWEEN '" + DataHoje.converteData(referencia[0]) + "' AND '" + DataHoje.converteData(referencia[1]) + "'");
                    }
                } else if (tipo == 4) {
                    listQuery.add("RM.dt_resposta IS NULL");
                }
            }
            if (!inRepisStatus.isEmpty()) {
                listQuery.add("RM.id_repis_status IN(" + inRepisStatus + ")");
            }
            if (!inCertidoesTipo.isEmpty()) {
                listQuery.add("RM.id_certidao_tipo IN(" + inCertidoesTipo + ")");
            }
            if (!inCidadeBase.isEmpty()) {
                listQuery.add("C.id IN(" + inCidadeBase + ")");
            }
            if (empresa != null) {
                listQuery.add("RM.id_pessoa = " + empresa);
            }
            for (int i = 0; i < listQuery.size(); i++) {
                if (i == 0) {
                    queryString += " WHERE ";
                } else {
                    queryString += " AND ";
                }
                queryString += " " + listQuery.get(i).toString();
            }
            if (!relatorios.getQryOrdem().isEmpty()) {
                queryString += " ORDER BY " + relatorios.getQry();
            } else {
                if (order.isEmpty()) {
                    queryString += " ORDER BY P.ds_nome ASC";
                } else {
                    queryString += " ORDER BY " + order;
                }
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
