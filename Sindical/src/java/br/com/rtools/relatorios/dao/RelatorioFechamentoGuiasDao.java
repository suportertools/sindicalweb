package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class RelatorioFechamentoGuiasDao extends DB {

    private String order = "";

    public List find(Relatorios relatorio, String inEmpresas, String inBeneficiarios, Date pagamentoInicial, Date pagamentoFinal) {
        List listWhere = new ArrayList();
        String queryString = ""
                + "      SELECT C.ds_nome,                       "
                + "             G.id             AS guia,        "
                + "             M.id_baixa,                      "
                + "             SE.ds_descricao  AS servico,     "
                + "             M.nr_quantidade  AS qtde,        "
                + "             PU.ds_nome       AS atendente,   "
                + "             P.ds_nome        AS beneficiario,"
                + "             B.dt_baixa       AS datapagto,   "
                + "             M.nr_valor_baixa AS valor        "
                + "        FROM fin_movimento   AS M             "
                + "  INNER JOIN fin_baixa       AS B  ON B.id  = M.id_baixa         "
                + "  INNER JOIN fin_servicos    AS SE ON SE.id = M.id_servicos      "
                + "  INNER JOIN fin_guia        AS G  ON G.id_lote = M.id_lote      "
                + "  INNER JOIN pes_pessoa      AS C  ON C.id = G.id_convenio       "
                + "  INNER JOIN pes_pessoa      AS P  ON P.id = M.id_beneficiario   "
                + "  INNER JOIN seg_usuario     AS U  ON U.id = B.id_usuario        "
                + "  INNER JOIN pes_pessoa      AS PU ON PU.id = U.id_pessoa        ";

        if (inEmpresas != null) {
            listWhere.add(" C.id IN (" + inEmpresas + ")");
        }
        if (inBeneficiarios != null) {
            listWhere.add(" P.id IN (" + inBeneficiarios + ")");
        }
        if (pagamentoInicial != null && pagamentoFinal != null) {
            listWhere.add(" B.dt_baixa BETWEEN '" + DataHoje.converteData(pagamentoInicial) + "' AND '" + DataHoje.converteData(pagamentoFinal) + "'");
        } else if (pagamentoInicial != null) {
            listWhere.add(" B.dt_baixa >= '" + DataHoje.converteData(pagamentoInicial) + "'");
        }
        if (!listWhere.isEmpty()) {
            queryString += " WHERE ";
            for (int i = 0; i < listWhere.size(); i++) {
                if (i > 0) {
                    queryString += " AND ";
                }
                queryString += listWhere.get(i).toString();
            }
        }
        if (relatorio.getQryOrdem().isEmpty()) {
            queryString += " ORDER BY C.ds_nome, B.id, B.dt_baixa ";
        } else {
            queryString += " ORDER BY " + relatorio.getQryOrdem();
        }
        try {
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
