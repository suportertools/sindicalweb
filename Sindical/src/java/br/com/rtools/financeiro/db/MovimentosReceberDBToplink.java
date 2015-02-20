package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MovimentosReceberDBToplink extends DB implements MovimentosReceberDB {

    @Override
    public List pesquisaListaMovimentos(int id_juridica) {
        try {
            String textoQuery = ""
                    + "     SELECT m.ds_documento Boleto,                       "
                    + "            se.ds_descricao          AS Servico,         "
                    + "            tp.ds_descricao          AS Tipo,            "
                    + "            m.ds_referencia          AS Referencia,      "
                    + "            m.dt_vencimento          AS Vencimento,      "
                    + "            func_valor_folha(m.id)   AS Valor_Mov,       "
                    //+ "            m.nr_valor               AS Valor_Mov,       "
                    + "            f.nr_valor               AS Valor_Folha,     "
                    + "            func_multa(m.id)         AS Multa,           "
                    + "            func_juros(m.id)         AS Juros,           "
                    + "            func_correcao(m.id)      AS Correcao,        "
                    + "            null                     AS Desconto,        "
                    + "            func_valor_folha(m.id) + func_multa(m.id) + func_juros(m.id) + func_correcao(m.id)   AS Valor_calculado, "
                    + "            func_intervalo_meses(CURRENT_DATE,dt_vencimento)                                     AS Meses_em_Atraso, "
                    + "            CURRENT_DATE-dt_vencimento                                                           AS Dias_em_atraso,  "
                    + "            i.ds_descricao indice,"
                    + "            m.id AS id"
                    + "       FROM fin_movimento                    AS m"
                    + " INNER JOIN fin_servicos                     AS se ON se.id = m.id_servicos"
                    + " INNER JOIN fin_tipo_servico                 AS tp ON tp.id = m.id_tipo_servico"
                    + " INNER JOIN pes_juridica                     AS j ON j.id_pessoa = m.id_pessoa"
                    + "  LEFT JOIN arr_faturamento_folha_empresa    AS f ON f.id_juridica = j.id AND f.ds_referencia = m.ds_referencia AND f.id_tipo_servico = m.id_tipo_servico "
                    + "  LEFT JOIN fin_correcao                     AS cr ON cr.id_servicos = m.id_servicos AND "
                    + "                                                 (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) AND "
                    + "                                                 (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))         "
                    + "  LEFT JOIN fin_indice                       AS i ON i.id = cr.id_indice"
                    + "      WHERE m.id_pessoa = " + id_juridica
                    + "        AND m.is_ativo IS TRUE "
                    + "        AND m.id_baixa IS NULL "
                    + "        AND m.id_servicos IN (SELECT sr.id_servicos FROM fin_servico_rotina sr WHERE sr.id_rotina = 4)"
                    + "   ORDER BY m.dt_vencimento";

            Query qry = getEntityManager().createNativeQuery(textoQuery);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaListaMovimentosDesconto(int id_juridica, float desconto, float total) {
        try {
            String textoQuery = "select m.ds_documento Boleto, "
                    + "       se.ds_descricao as Servico, "
                    + "       tp.ds_descricao as Tipo, "
                    + "       m.ds_referencia as Referencia,"
                    + "       m.dt_vencimento as Vencimento, "
                    + "       func_valor_folha(m.id) as Valor_Mov,"
                    + "       f.nr_valor as Valor_Folha,"
                    + "       func_multa(m.id) as Multa,"
                    + "       func_juros(m.id) as Juros,"
                    + "       func_correcao(m.id) as Correcao,"
                    + "       func_desconto(m.id, " + desconto + ", " + total + ") as Desconto,"
                    + "       func_valor_folha(m.id) + func_multa(m.id) + func_juros(m.id) + func_correcao(m.id) as Valor_calculado,"
                    + "       func_intervalo_meses(CURRENT_DATE,dt_vencimento) as Meses_em_Atraso,"
                    + "       CURRENT_DATE-dt_vencimento as Dias_em_atraso,"
                    + "       i.ds_descricao indice,"
                    + "       m.id as id"
                    + "  from fin_movimento as m"
                    + " inner join fin_servicos as se on se.id=m.id_servicos"
                    + " inner join fin_tipo_servico as tp on tp.id=m.id_tipo_servico"
                    + " inner join pes_juridica as j on j.id_pessoa=m.id_pessoa"
                    + "  left join arr_faturamento_folha_empresa as f on f.id_juridica=j.id and f.ds_referencia=m.ds_referencia and f.id_tipo_servico=m.id_tipo_servico"
                    + "  left join fin_correcao as cr on cr.id_servicos=m.id_servicos and "
                    + " (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and "
                    + " (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))"
                    + "  left join fin_indice as i on i.id=cr.id_indice"
                    + " where m.id_pessoa = " + id_juridica
                    + "   and m.is_ativo is true "
                    + "   and m.id_baixa is null "
                    + " order by m.dt_vencimento";

            Query qry = getEntityManager().createNativeQuery(textoQuery);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
