package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class RelatorioMovimentosDBToplink extends DB implements RelatorioMovimentosDB {

    @Override
    public List listaMovimentos(Relatorios relatorios, String condicao, int idServico, int idTipoServico, int idJuridica, boolean data,
            String tipoData, Date dtInicial, Date dtFinal, String dtRefInicial, String dtRefFinal,
            String ordem, boolean chkPesEmpresa, String porPesquisa, String filtroEmpresa,
            int idConvencao, int idGrupoCidade, String idsCidades, String idsEsc, String inCnaes) {
        List result;
        
        String textQuery = ""
                + "SELECT mov.id                       AS idMov,                \n "
                + "       mov.ds_documento             AS numeroDocumento,      \n "
                + "       se.ds_descricao              AS servico,              \n "
                + "       ts.ds_descricao              AS tipoServico,          \n "
                + "       mov.ds_referencia            AS referencia,           \n "
                + "       mov.dt_vencimento            AS vencimento,           \n "
                + "       mov.nr_valor                 AS valor,                \n "
                + "       se.id                        AS idServico,            \n "
                + "       ts.id                        AS idTipoServico,        \n "
                + "       pes.id                       AS idPessoa,             \n "
                + "       pes.ds_nome                  AS nomePessoa,           \n "
                + "       pes_endereco.ds_descricao    AS enderecoPessoa,       \n "
                + "       pes_logradouro.ds_descricao  AS logradouroPessoa,     \n "
                + "       pes_pend.ds_numero           AS numeroPessoa,         \n "
                + "       pes_pend.ds_complemento      AS complementoPessoa,    \n "
                + "       pes_bairro.ds_descricao      AS bairroPessoa,         \n "
                + "       pes_end.ds_cep               AS cepPessoa,            \n "
                + "       pes_cidade.ds_cidade         AS cidadePessoa,         \n "
                + "       pes_cidade.ds_uf             AS ufCidade,             \n "
                + "       pes.ds_telefone1             AS telefonePessoa,       \n "
                + "       pes.ds_email1                AS emailPessoa,          \n "
                + "       pdoc.ds_descricao            AS tipoDocPessoa,        \n "
                + "       pes.ds_documento             AS documentoPessoa,      \n "
                + "       cnae.id                      AS idCnae,               \n "
                + "       cnae.ds_numero               AS numeroCnae,           \n "
                + "       cnae.ds_cnae                 AS nomeCnae,             \n "
                + "       jur.id_contabilidade         AS idContabil,           \n "
                + "       pesc.ds_nome                 AS nomeContabil,         \n "
                + "       esc_endereco.ds_descricao    AS enderecoContabil,     \n "
                + "       esc_logradouro.ds_descricao  AS logradouroContabil,   \n "
                + "       esc_pend.ds_numero           AS numeroContabil,       \n "
                + "       esc_pend.ds_complemento      AS complementoContabil,  \n "
                + "       esc_bairro.ds_descricao      AS bairroContabil,       \n "
                + "       esc_end.ds_cep               AS cepContabil,          \n "
                + "       esc_cidade.ds_cidade         AS cidadeContabil,       \n "
                + "       esc_cidade.ds_uf             AS ufCidade,             \n "
                + "       pesc.ds_telefone1            AS telefoneContabil,     \n "
                + "       pesc.ds_email1               AS emailContabil,        \n "
                + "       lot.id                       AS idLote,               \n "
                + "       lot.dt_baixa                 AS quitacaoLote,         \n "
                + "       lot.dt_importacao            AS importacaoLote,       \n "
                + "       jur.id                       AS idJuridica,           \n "
                + "       upes.ds_nome                 AS usuario,              \n "
                + "       mov.nr_taxa                  AS taxa,                 \n "
                + "       mov.nr_multa                 AS multa,                \n "
                + "       mov.nr_juros                 AS juros,                \n "
                + "       mov.nr_correcao              AS correcao,             \n "
                + "       mov.nr_valor_baixa           AS valor_baixa,          \n "
                + "       cc.nr_repasse                AS vl_repasse            \n "
                + "  FROM fin_movimento                AS mov                   \n "
                + " INNER JOIN pes_pessoa              AS pes              ON pes.id               = mov.id_pessoa                              \n "
                + " INNER JOIN pes_tipo_documento      AS pdoc             ON pdoc.id              = pes.id_tipo_documento                      \n "
                + " INNER JOIN fin_servicos            AS se               ON se.id                = mov.id_servicos                            \n "
                + " INNER JOIN fin_servico_rotina      AS ser              ON ser.id_servicos      = se.id AND ser.id_rotina = 4                \n "
                + " INNER JOIN fin_tipo_servico        AS ts               ON ts.id                = mov.id_tipo_servico                        \n "
                + "  LEFT JOIN pes_juridica            AS jur              ON jur.id_pessoa        = pes.id                                     \n "
                + "  LEFT JOIN arr_contribuintes_vw    AS C                ON C.id_juridica        = jur.id                                     \n ";
        if (condicao.equals("inativos") || condicao.equals("naoContribuintes")) {
            textQuery += " LEFT JOIN arr_contribuintes_inativos_agrupados_vw AS CI ON CI.id_juridica = C.id_juridica \n ";
        }
        textQuery += " "
                //+ "  LEFT JOIN fin_baixa               AS lot              ON lot.id               = mov.id_baixa                                                                       \n "
                + "  LEFT JOIN pes_juridica            AS esc              ON esc.id               = jur.id_contabilidade                                                               \n "
                + "  LEFT JOIN pes_pessoa              AS pesc             ON pesc.id              = esc.id_pessoa                                                                      \n "
                + "  LEFT JOIN pes_tipo_documento      AS escdoc           ON escdoc.id            = pes.id_tipo_documento                                                              \n "
                + "  LEFT JOIN pes_cnae                AS cnae             ON cnae.id              = jur.id_cnae                                                                        \n "
                + "  LEFT JOIN pes_pessoa_endereco     AS pes_pend         ON pes_pend.id_pessoa   = pes.id AND (pes_pend.id_tipo_endereco = 2 OR pes_pend.id_tipo_endereco IS NULL)    \n "
                + "  LEFT JOIN end_endereco            AS pes_end          ON pes_end.id           = pes_pend.id_endereco                                                               \n "
                + "  LEFT JOIN end_logradouro          AS pes_logradouro   ON pes_logradouro.id    = pes_end.id_logradouro                                                              \n "
                + "  LEFT JOIN end_descricao_endereco  AS pes_endereco     ON pes_endereco.id      = pes_end.id_descricao_endereco                                                      \n "
                + "  LEFT JOIN end_bairro              AS pes_bairro       ON pes_bairro.id        = pes_end.id_bairro                                                                  \n "
                + "  LEFT JOIN end_cidade              AS pes_cidade       ON pes_cidade.id        = pes_end.id_cidade                                                                  \n "
                + "  LEFT JOIN pes_pessoa_endereco     AS esc_pend         ON esc_pend.id_pessoa   = pesc.id AND (esc_pend.id_tipo_endereco = 2 OR esc_pend.id_tipo_endereco IS NULL)   \n "
                + "  LEFT JOIN end_endereco            AS esc_end          ON esc_end.id           = esc_pend.id_endereco                                                               \n "
                + "  LEFT JOIN end_logradouro          AS esc_logradouro   ON esc_logradouro.id    = esc_end.id_logradouro                                                              \n "
                + "  LEFT JOIN end_descricao_endereco  AS esc_endereco     ON esc_endereco.id      = esc_end.id_descricao_endereco                                                      \n "
                + "  LEFT JOIN end_bairro              AS esc_bairro       ON esc_bairro.id        = esc_end.id_bairro                                                                  \n "
                + "  LEFT JOIN end_cidade              AS esc_cidade       ON esc_cidade.id        = esc_end.id_cidade                                                                  \n "
                + "  LEFT JOIN fin_baixa               AS lot              ON lot.id               = mov.id_baixa                                                                       \n "
                + "  LEFT JOIN seg_usuario             AS us               ON us.id                = lot.id_usuario                                                                     \n "
                + "  LEFT JOIN pes_pessoa              AS upes             ON upes.id              = us.id_pessoa                                                                       \n "
                + "  LEFT JOIN fin_boleto              AS bol              ON bol.nr_ctr_boleto    = mov.nr_ctr_boleto                                                                  \n "
                + "  LEFT JOIN fin_conta_cobranca      AS cc               ON cc.id                = bol.id_conta_cobranca                                                              \n ";

        // CONDICAO -----------------------------------------------------
        switch (condicao) {
            case "todos":
                textQuery += " WHERE mov.is_ativo = true \n ";
                break;
            case "ativos":
                textQuery += " WHERE mov.is_ativo = true AND C.id_juridica IS NOT NULL AND C.dt_inativacao IS NULL \n ";
                break;
            case "inativos":
                textQuery += " WHERE mov.is_ativo = true AND CI.id_juridica IS NOT NULL \n ";
                // 03/11/2014 - Chamado 234 - RUNRUN + "   AND jur.id NOT IN (SELECT c.id_juridica FROM arr_contribuintes_vw c) "
                // + "   AND jur.id NOT IN (SELECT c.id_juridica FROM arr_contribuintes_vw C WHERE dt_inativacao IS NULL) "
                //+ "   AND jur.id IN (SELECT ci.id_juridica FROM arr_contribuintes_inativos ci GROUP BY ci.id_juridica) "
                // + " AND C.dt_inativacao IS NOT NULL ";
            case "naoContribuintes":
                textQuery += " WHERE mov.is_ativo = true AND CI.id_juridica IS NULL AND C.id_juridica IS NULL \n ";
                break;
        }

        // CONTRIBUICAO DE RELATORIO---------------------------------------------
        if (idServico != 0) {
            textQuery += " AND mov.id_servicos = " + idServico + " \n ";
        }

        // TIPO SERVICO DO RELATORIO-----------------------------------------------
        if (idTipoServico != 0) {
            textQuery += " AND mov.id_tipo_servico = " + idTipoServico + " \n ";
        }

        // PESSOA DO RELATORIO-----------------------------------------------------
        if (idJuridica != 0) {
            if (filtroEmpresa.equals("empresa")) {
                textQuery += " AND jur.id = " + idJuridica + " \n ";
            } else {
                textQuery += " AND esc.id = " + idJuridica + " \n ";
            }
        }

        // FILTRAR POR ESCRITÓRIOS ------------------------------------------------        
        if (!idsEsc.isEmpty()) {
            switch (idsEsc) {
                case "sem":
                    textQuery += " AND jur.id_contabilidade IS NULL \n ";
                    break;
                case "com":
                    textQuery += " AND jur.id_contabilidade IS NOT NULL \n ";
                    break;
                default:
                    textQuery += " AND esc.id IN ( " + idsEsc + " ) \n ";
                    break;
            }
        }

        // FILTRO MOVIMENTO ---------------------------------------------------------
        switch (porPesquisa) {
            case "todas":
                break;
            case "recebidas":
                textQuery += " AND mov.id_baixa IS NOT NULL \n ";
                break;
            case "naorecebidas":
                textQuery += " AND mov.id_baixa IS NULL \n ";
                break;
            case "atrasadas":
                textQuery += " AND mov.id_baixa IS NULL AND mov.dt_vencimento < '" + DataHoje.data() + "' \n ";
                break;
            case "atrasadas_quitadas":
                textQuery += " AND mov.id_baixa > 0 AND lot.dt_baixa > mov.dt_vencimento \n ";
                break;
        }

        // DATA DO RELATORIO ---------------------------------------------------------
        if (data) {
            if (dtInicial != null && dtFinal != null) {
                switch (tipoData) {
                    case "importacao":
                        textQuery += " AND mov.id_baixa = lot.id \n "
                                + " AND lot.dt_importacao >= '" + DataHoje.converteData(dtInicial) + "' \n "
                                + " AND lot.dt_importacao <= '" + DataHoje.converteData(dtFinal) + "' \n ";
                        break;
                    case "recebimento":
                        textQuery += " AND mov.id_baixa = lot.id \n "
                                + " AND lot.dt_baixa >= '" + DataHoje.converteData(dtInicial) + "' \n "
                                + " AND lot.dt_baixa <= '" + DataHoje.converteData(dtFinal) + "' \n ";
                        break;
                    case "vencimento":
                        textQuery += " AND mov.dt_vencimento >= '" + DataHoje.converteData(dtInicial) + "' \n "
                                + " AND mov.dt_vencimento <= '" + DataHoje.converteData(dtFinal) + "' \n ";
                        break;
                }
            } else if (!dtRefInicial.equals("") && !dtRefFinal.equals("")) {
                String ini = dtRefInicial.substring(3, 7) + dtRefInicial.substring(0, 2);
                String fin = dtRefFinal.substring(3, 7) + dtRefFinal.substring(0, 2);
                textQuery += " AND concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) >=  \'" + ini + "\' \n "
                        + " AND concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) <=  \'" + fin + "\' \n ";
            }
        }

        // CONVENCAO DO RELATORIO ------------------------------------------------------------------------------------
        if (inCnaes.isEmpty()) {
            if (idConvencao != 0) {
                textQuery += " AND jur.id_cnae in (SELECT id_cnae from arr_cnae_convencao WHERE id_convencao = " + idConvencao + ") \n ";
            }
        } else {
            textQuery += " AND jur.id_cnae in (" + inCnaes + ") \n ";
        }

        // GRUPO CIDADE DO RELATORIO -----------------------------------------------------------------------------------
        if (idGrupoCidade != 0) {
            textQuery += " AND pes_cidade.id in (SELECT id_cidade from arr_grupo_cidades WHERE id_grupo_cidade = " + idGrupoCidade + ") \n ";
        }

        // IDS CIDADES DA BASE -----------------------------------------------------------------------------------
        if (!idsCidades.isEmpty()) {
            textQuery += " AND pes_cidade.id in (" + idsCidades + ") \n ";
        }

        String ordem2;
        if (relatorios.getQryOrdem() == null || relatorios.getQryOrdem().isEmpty()) {
            ordem2 = " ORDER BY ";
        } else {
            ordem2 = " ORDER BY " + relatorios.getQryOrdem() + ", ";
        }

        // ORDEM DO RELATORIO --------------------------------------------------------
        if (chkPesEmpresa) {
            textQuery += ordem2 + " pes.ds_nome, ";
            switch (ordem) {
                case "vencimento":
                    textQuery += " mov.dt_vencimento \n ";
                    break;
                case "quitacao":
                    textQuery += " lot.dt_baixa \n ";
                    break;
                case "importacao":
                    textQuery += " lot.dt_importacao \n ";
                    break;
                case "referencia":
                    textQuery += " concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) \n ";
                    break;
            }
        } else {
            textQuery += ordem2 + " pes.ds_nome, ";
            switch (ordem) {
                case "vencimento":
                    textQuery += " mov.dt_vencimento \n ";
                    break;
                case "quitacao":
                    textQuery += " lot.dt_baixa \n ";
                    break;
                case "importacao":
                    textQuery += " lot.dt_importacao \n ";
                    break;
                case "referencia":
                    textQuery += " concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) \n ";
                    break;
            }
        }

        try {
            Query qry = getEntityManager().createNativeQuery(textQuery);
            result = qry.getResultList();
        } catch (Exception e) {
            result = new ArrayList();
        }
        return result;
    }
}
