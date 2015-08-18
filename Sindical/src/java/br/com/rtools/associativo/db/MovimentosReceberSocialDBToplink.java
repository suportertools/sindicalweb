package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class MovimentosReceberSocialDBToplink extends DB implements MovimentosReceberSocialDB {

    /**
     * <ul>
     * <li> <strong> RESULT </strong> </li> 
     * <li> 00 - SERVIÇO -> DESCRIÇÃO; </li>
     * <li> 01 - TIPO SERVIÇO -> DESCRIÇÃO;</li>
     * <li> 02 - MOVIMENTO -> REFERÊNCIA;</li>
     * <li> 03 - VALOR;</li>
     * <li> 04 - VALOR MULTA;</li>
     * <li> 05 - VALOR MULTA COM ACRÉSCIMO;</li>
     * <li> 06 - DESCONTO;</li>
     * <li> 07 - VALOR MULTA COM CORREÇÃO;</li>
     * <li> 08 - DATA BAIXA;</li>
     * <li> 09 - VALOR BAIXA;</li>
     * <li> 10 - E/S;</li>
     * <li> 11 - RESPONSÁVEL -> NOME;</li>
     * <li> 12 - BENEFICIÁRIO -> NOME;</li>
     * <li> 13 - RESPONSÁVEL -> ID;</li>
     * <li> 14 - MOVIMENTO -> ID;</li>
     * <li> 15 - LOTE -> ID;</li>
     * <li> 16 - LOTE -> LANÇAMENTO;</li>
     * <li> 17 - MOVIMENTO -> DOCUMENTO;</li>
     * <li> 18 - DIAS EM ATRASO;</li>
     * <li> 19 - MULTA;</li>
     * <li> 20 - JUROS;</li>
     * <li> 21 - CORREÇÃO;</li>
     * <li> 22 - CAIXA -> NOME;</li>
     * <li> 23 - LOTE BAIXA -> ID;</li>
     * <li> 24 - MOVIMENTO -> DOCUMENTO;</li>
     * <li> 25 - TITULAR -> NOME;</li>
     * <li> 26 - BENEFECIÁRIO -> ID;</li>
     * </ul>
     *
     * @param id_pessoa (Beneficiário do movimento)
     * @param id_responsavel (Responsável pelo movimento)
     * @param por_status (Status)
     * @param referencia (Referência)
     * @param tipoPessoa (Se é física ou jurídica)
     * @param lote_baixa (Id da baixa)
     * @return
     */
    @Override
    public List pesquisaListaMovimentos(String id_pessoa, String id_responsavel, String por_status, String referencia, String tipoPessoa, String lote_baixa) {
        try {
            if (id_pessoa.isEmpty()) {
                return new ArrayList();
            }
            String textqry = " "
                    + "    SELECT se.ds_descricao           AS servico,                             \n" // 00 - SERVIÇO -> DESCRIÇÃO
                    + "           tp.ds_descricao           AS tipo,                                \n" // 01 - TIPO SERVIÇO -> DESCRIÇÃO
                    + "           m.ds_referencia,                                                  \n" // 02 - MOVIMENTO -> REFERÊNCIA
                    + "           m.dt_vencimento           AS vencimento,                          \n" // 03 - VALOR
                    + "           func_valor(m.id)          AS valor,                               \n" // 04 - VALOR MULTA
                    + "           func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) AS acrescimo,   \n" // VALOR MULTA COM ACRÉSCIMO
                    + "           m.nr_desconto             AS desconto,                            \n" // 06 - DESCONTO        
                    + "           func_valor(m.id)+func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) AS vl_calculado, \n" // 07 - VALOR MULTA COM CORREÇÃO
                    + "           bx.dt_baixa,                                                      \n" // 08 - DATA BAIXA
                    + "           nr_valor_baixa            AS valor_pago,                          \n" // 09 - VALOR BAIXA
                    + "           m.ds_es                   AS es,                                  \n" // 10 - E/S
                    + "           p.ds_nome                 AS responsavel,                         \n" // 11 - RESPONSÁVEL -> NOME
                    + "           b.ds_nome                 AS beneficiario,                        \n" // 12 - BENEFICIÁRIO -> NOME
                    + "           p.id                      AS id_responsavel,                      \n" // 13 - RESPONSÁVEL -> ID
                    + "           m.id                      AS id_movimento,                        \n" // 14 - MOVIMENTO -> ID
                    + "           m.id_lote                 AS lote,                                \n" // 15 - LOTE -> ID
                    + "           l.dt_lancamento           AS criacao,                             \n" // 16 - LOTE -> LANÇAMENTO
                    + "           m.ds_documento            AS boleto,                              \n" // 17 - MOVIMENTO -> DOCUMENTO
                    + "           func_intervalo_dias(m.dt_vencimento,CURRENT_DATE) AS dias_atraso, \n" // 18 - DIAS EM ATRASO
                    + "           func_multa_ass(m.id)      AS multa,                               \n" // 19 - MULTA
                    + "           func_juros_ass(m.id)      AS juros,                               \n" // 20 - JUROS
                    + "           func_correcao_ass(m.id)   AS correcao,                            \n" // 21 - CORREÇÃO
                    + "           us.ds_nome                AS caixa,                               \n" // 22 - CAIXA -> NOME
                    + "           m.id_baixa                AS lote_baixa,                          \n" // 23 - LOTE BAIXA -> ID
                    + "           l.ds_documento            AS documento,                           \n" // 24 - MOVIMENTO -> DOCUMENTO
                    + "           t.ds_nome                 AS titular,                             \n" // 25 - TITULAR -> NOME
                    + "           b.id AS id_beneficiario                                           \n" // 26 - BENEFECIÁRIO -> ID
                    + "      FROM fin_movimento     AS m                                            \n"
                    + "INNER JOIN fin_lote          AS l  ON l.id = m.id_lote           \n"
                    + "INNER JOIN pes_pessoa        AS p  ON p.id = m.id_pessoa         \n"
                    + "INNER JOIN pes_pessoa        AS b  ON b.id = m.id_beneficiario   \n"
                    + " LEFT JOIN pes_pessoa        AS t  ON t.id = m.id_titular        \n"
                    + "INNER JOIN fin_servicos      AS se ON se.id = m.id_servicos      \n"
                    + "INNER JOIN fin_tipo_servico  AS tp ON tp.id = m.id_tipo_servico  \n"
                    + " LEFT JOIN fin_baixa         AS bx ON bx.id = m.id_baixa         \n"
                    + " LEFT JOIN seg_usuario       AS u  ON u.id = bx.id_usuario       \n"
                    + " LEFT JOIN pes_pessoa        AS us ON us.id = u.id_pessoa        \n"
                    + " LEFT JOIN pes_juridica      AS j  ON j.id_pessoa = m.id_pessoa  \n";

            String order_by = "";
            String where = "";
            String ands = "";

            switch (por_status) {
                case "todos":
                    ands = where + " WHERE (m.id_pessoa in (" + id_responsavel + ") OR (m.id_beneficiario IN (" + id_pessoa + ") AND j.id IS NULL)) \n  "
                            + "        AND m.is_ativo = true \n"
                            + "        AND m.id_servicos NOT IN(SELECT sr.id_servicos FROM fin_servico_rotina AS sr WHERE id_rotina = 4) \n";
                    order_by = (tipoPessoa.equals("fisica")) ? " ORDER BY m.dt_vencimento asc, p.ds_nome, t.ds_nome, b.ds_nome, se.ds_descricao \n"
                            : "";
                    break;
                case "abertos":
                    ands = where + " WHERE (m.id_pessoa IN (" + id_responsavel + ") OR (m.id_beneficiario IN (" + id_pessoa + ") AND j.id IS NULL)) \n "
                            + "        AND m.id_baixa IS NULL   \n"
                            + "        AND m.is_ativo = true    \n"
                            + "        AND m.id_servicos NOT IN(SELECT sr.id_servicos FROM fin_servico_rotina AS sr WHERE id_rotina = 4) \n";
                    order_by = " ORDER BY m.dt_vencimento ASC, p.ds_nome, t.ds_nome, b.ds_nome, se.ds_descricao \n";
                    break;
                case "quitados":
                    ands = where + " WHERE (m.id_pessoa IN (" + id_responsavel + ") OR (m.id_beneficiario IN (" + id_pessoa + ") AND j.id IS NULL)) "
                            + "        AND m.id_baixa IS NOT NULL   \n"
                            + "        AND m.is_ativo = true        \n"
                            + "        AND m.id_servicos NOT IN(SELECT sr.id_servicos FROM fin_servico_rotina AS sr WHERE id_rotina = 4) \n";
                    order_by = (tipoPessoa.equals("fisica")) ? " ORDER BY bx.dt_baixa ASC, m.dt_vencimento, p.ds_nome, se.ds_descricao \n"
                            : "";
                    break;
                case "atrasados":
                    ands = where + " WHERE (m.id_pessoa IN (" + id_responsavel + ") OR (m.id_beneficiario IN (" + id_pessoa + ") AND j.id IS NULL)) "
                            + "        AND m.id_baixa IS NULL               \n"
                            + "        AND m.is_ativo = true                \n"
                            + "        AND m.dt_vencimento < current_date   \n"
                            + "        AND m.id_servicos NOT IN(SELECT sr.id_servicos FROM fin_servico_rotina AS sr WHERE id_rotina = 4) \n";
                    order_by = (tipoPessoa.equals("fisica")) ? " ORDER BY m.dt_vencimento, p.ds_nome, t.ds_nome, se.ds_descricao \n"
                            : "";
                    break;
                case "vencer":
                    ands = where + " WHERE (m.id_pessoa IN (" + id_responsavel + ") OR (m.id_beneficiario IN (" + id_pessoa + ") AND j.id IS NULL)) "
                            + "        AND m.id_baixa IS NULL               \n"
                            + "        AND m.is_ativo = true                \n"
                            + "        AND m.dt_vencimento > current_date   \n"
                            + "        AND m.id_servicos NOT IN(SELECT sr.id_servicos FROM fin_servico_rotina AS sr WHERE id_rotina = 4) \n";
                    order_by = (tipoPessoa.equals("fisica")) ? " ORDER BY m.dt_vencimento, p.ds_nome, t.ds_nome, se.ds_descricao \n"
                            : "";
                    break;
            }

            if (!referencia.isEmpty()) {
                ands += " AND m.ds_referencia = '" + referencia + "' \n";
            }

            if (!lote_baixa.isEmpty()) {
                ands += " AND m.id_baixa = " + lote_baixa + " \n";
            }

            textqry += ands + order_by;
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public List dadosSocio(int id_lote) {
        try {

            String textqry = " select "
                    + "     p.ds_nome as titular,"
                    + "     m.nr_matricula as matricula,"
                    + "     c.ds_categoria as categoria,"
                    + "     g.ds_grupo_categoria as grupo,"
                    + "  case "
                    + "  when m.dt_inativo is null then 'Matricula ATIVA' "
                    + "	 when m.dt_inativo is not null then 'Matricula INATIVA' "
                    + "   end "
                    + "  from fin_lote as l "
                    + " inner join matr_socios as m on m.id=l.id_matricula_socios "
                    + " inner join pes_pessoa as p on p.id=m.id_titular "
                    + " inner join soc_categoria as c on c.id=m.id_categoria "
                    + " inner join soc_grupo_categoria as g on g.id=c.id_grupo_categoria "
                    + " where l.id = " + id_lote;

            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public Pessoa pesquisaPessoaPorBoleto(String boleto, int id_conta_cobranca) {
        Pessoa pessoa = null;
        String textqry
                = " SELECT pes.* "
                + "   FROM pes_pessoa pes "
                + "  INNER JOIN fin_movimento mov ON pes.id = mov.id_pessoa "
                + "  INNER JOIN fin_boleto bol ON mov.nr_ctr_boleto = bol.nr_ctr_boleto "
                + "    AND mov.is_ativo is true "
                + "    AND bol.id_conta_cobranca = " + id_conta_cobranca
                + "    AND mov.ds_documento = '" + boleto + "'";
        try {
            Query qry = getEntityManager().createNativeQuery(textqry, Pessoa.class);
            qry.setMaxResults(1);
            pessoa = (Pessoa) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return pessoa;
    }

    @Override
    public float[] pesquisaValorAcrescimo(int id_movimento) {
        float[] valor = new float[2];
        String textqry
                = " SELECT func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) as acrescimo, "
                + "        func_valor(m.id)+func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) as valor "
                + "   FROM fin_movimento m "
                + "  WHERE m.id = " + id_movimento;
        try {
            Query qry = getEntityManager().createNativeQuery(textqry);
            valor[0] = Moeda.converteUS$(Double.toString((Double) ((Vector) qry.getSingleResult()).get(0)));
            valor[1] = Moeda.converteUS$(Double.toString((Double) ((Vector) qry.getSingleResult()).get(1)));
        } catch (Exception e) {
            e.getMessage();
        }
        return valor;
    }

    @Override
    public List<Vector> listaBoletosAbertosAgrupado(int id_pessoa, boolean atrasados) {
        String textqry
                = " SELECT b.id, b.nr_ctr_boleto, b.ds_boleto, sum(m.nr_valor), b.dt_vencimento, b.dt_vencimento_original, b.ds_mensagem \n"
                + "  FROM fin_boleto b \n"
                + " INNER JOIN fin_movimento m ON m.nr_ctr_boleto = b.nr_ctr_boleto \n"
                + " WHERE m.id_pessoa = " + id_pessoa + " \n"
                + "   AND m.is_ativo = true \n"
                + ((atrasados) ? "" : "   AND b.dt_vencimento >= CURRENT_DATE \n")
                + "   AND (m.ds_documento IS NOT NULL AND m.ds_documento <> '') \n"
                + "   AND (m.nr_ctr_boleto IS NOT NULL AND m.nr_ctr_boleto <> '') \n"
                + "   AND b.dt_vencimento IS NOT NULL \n"
                + "   AND m.id_servicos NOT IN (SELECT id_servicos FROM fin_servico_rotina WHERE id_rotina = 4) \n"
                + " GROUP BY b.id, b.nr_ctr_boleto, b.ds_boleto, b.dt_vencimento, b.dt_vencimento_original  \n"
                + ((atrasados) ? " ORDER BY b.dt_vencimento DESC" : " ORDER BY b.dt_vencimento");

        Query qry = getEntityManager().createNativeQuery(textqry);
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public List<Movimento> listaMovimentosAbertosAnexarAgrupado(int id_pessoa) {
        String textqry
                = " SELECT m.* \n"
                + "  FROM fin_movimento m \n"
                + "  LEFT JOIN fin_boleto b ON m.nr_ctr_boleto = b.nr_ctr_boleto \n"
                + " INNER JOIN pes_pessoa pt ON pt.id = m.id_titular \n"
                + " INNER JOIN pes_pessoa pb ON pb.id = m.id_beneficiario \n"
                + " WHERE m.id_pessoa = " + id_pessoa + " \n"
                + "   AND m.is_ativo = true \n"
                + "   AND (m.ds_documento IS NULL OR m.ds_documento = '') \n"
                + "   AND (m.nr_ctr_boleto IS NULL OR m.nr_ctr_boleto = '') \n"
                + "   -- AND b.dt_vencimento >= CURRENT_DATE QUANDO ATUALIZAR OS CAMPOS NULOS \n"
                + //"   AND m.dt_vencimento >= CURRENT_DATE -- ATÉ ATUALIZAR OS CAMPOS NULOS \n" +
                "   AND m.id_baixa IS NULL \n"
                + "   AND m.id_servicos NOT IN (SELECT id_servicos FROM fin_servico_rotina WHERE id_rotina = 4) \n"
                + " ORDER BY m.dt_vencimento DESC, pt.ds_nome, pb.ds_nome";

        Query qry = getEntityManager().createNativeQuery(textqry, Movimento.class);
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public List<Movimento> listaMovimentosPorNrCtrBoleto(String nr_ctr_boleto) {
//        String textqry
//            = " SELECT m " +
//                "  FROM Movimento m " +
//                " WHERE m.nrCtrBoleto = '"+nr_ctr_boleto+"'" +
//                " ORDER BY m.titular.nome, m.beneficiario.nome, m.dtVencimento";
        String textqry
                = " SELECT m.* "
                + "  FROM fin_movimento m "
                + " INNER JOIN fin_boleto b ON b.nr_ctr_boleto = m.nr_ctr_boleto "
                + " INNER JOIN pes_pessoa pt ON pt.id = m.id_titular \n"
                + " INNER JOIN pes_pessoa pb ON pb.id = m.id_beneficiario \n"
                + " WHERE m.is_ativo = true "
                + "   AND m.nr_ctr_boleto = '" + nr_ctr_boleto + "'"
                + " ORDER BY pt.ds_nome, pb.ds_nome, m.dt_vencimento";

        Query qry = getEntityManager().createNativeQuery(textqry, Movimento.class);
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public Pessoa responsavelBoleto(String nr_ctr_boleto) {
        String textqry
                = " SELECT m.id_pessoa \n "
                + "  FROM fin_boleto b \n "
                + " INNER JOIN fin_movimento m ON m.nr_ctr_boleto = b.nr_ctr_boleto \n "
                + " WHERE b.nr_ctr_boleto = '" + nr_ctr_boleto + "' \n "
                + " GROUP BY m.id_pessoa";

        Query qry = getEntityManager().createNativeQuery(textqry);
        List<Vector> result = qry.getResultList();

        try {
            return (Pessoa) new Dao().find(new Pessoa(), result.get(0).get(0));
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public List<TransferenciaCaixa> transferenciaCaixa(Integer id_fechamento_caixa_saida) {
        String textqry
                = " SELECT tc "
                + "  FROM TransferenciaCaixa tc "
                + " WHERE tc.fechamentoSaida.id = " + id_fechamento_caixa_saida;

        Query qry = getEntityManager().createQuery(textqry);

        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

}
