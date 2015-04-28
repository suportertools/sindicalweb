package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioConvenioMedicoDao extends DB {

    private String order = "";

    public List find(Relatorios relatorio, Integer subgrupo, Integer convenio, Boolean isentos, Float faixaValorInicial, Float faixaValorFinal, String situacao) {
        List listWhere = new ArrayList();
        String queryString = ""
                + "     SELECT SB.ds_descricao                          AS grupo,                           " // 0 - SubGrupo
                + "            SE.ds_descricao                          AS servico,                         " // 1 - Serviço
                + "            P.ds_nome                                AS nome,                            " // 2 - Nome
                + "            func_idade(F.dt_nascimento,current_date) AS idade,                           " // 3 - Idade
                + "            S.categoria,                                                                 " // 4 - Categoria
                + "            SP.ds_ref_vigoracao                      AS vigoração,                       " // 5 - Vigoração
                + "            func_valor_servico(P.id, se.id, current_date, 0, id_categoria)   AS valor    " // 6 - Valor
                + "       FROM matr_convenio_medico AS M                                                    "
                + " INNER JOIN fin_servico_pessoa AS SP ON SP.id        = M.id_servico_pessoa   "
                + " INNER JOIN pes_pessoa AS P          ON P.id         = SP.id_pessoa          "
                + " INNER JOIN pes_fisica AS F          ON F.id_pessoa  = P.id                  "
                + " INNER JOIN fin_servicos AS SE       ON SE.id        = SP.id_servico         "
                + " INNER JOIN fin_subgrupo AS SB       ON SB.id        = SE.id_subgrupo        "
                + "  LEFT JOIN soc_socios_vw AS S       ON S.codsocio   = P.id                  "
                + "      WHERE M.dt_inativo IS NULL                                             ";
        if (situacao != null) {
            listWhere.add(" SE.ds_situacao = '" + situacao + "'");
        }
        if (subgrupo != null) {
            listWhere.add(" SE.id_subgrupo = " + subgrupo);
        }
        if (convenio != null) {
            listWhere.add(" SE.id = " + convenio);
        }
        if (isentos) {
            listWhere.add(" SP.ds_ref_vigoracao > '" + DataHoje.dataReferencia(DataHoje.data()) + "'");
        }
        if (faixaValorInicial >= 0 && faixaValorFinal > 0) {
            listWhere.add(" func_valor_servico(P.id, se.id, current_date, 0, id_categoria) BETWEEN " + faixaValorInicial + " AND " + faixaValorFinal);
        } else if (faixaValorInicial > 0) {
            listWhere.add(" func_valor_servico(P.id, se.id, current_date, 0, id_categoria) >= " + faixaValorInicial);
        }
        if (!listWhere.isEmpty()) {
            for (int i = 0; i < listWhere.size(); i++) {
                queryString += " AND ";
                queryString += listWhere.get(i).toString();
            }
        }
        queryString += " ORDER BY SB.ds_descricao, "
                + "               SE.ds_descricao, "
                + "               P.ds_nome        ";

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
