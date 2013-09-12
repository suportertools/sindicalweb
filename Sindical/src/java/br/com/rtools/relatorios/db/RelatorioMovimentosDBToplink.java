package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class RelatorioMovimentosDBToplink extends DB implements RelatorioMovimentosDB{

    @Override
    public List listaMovimentos(Relatorios relatorios, String condicao,int idServico,int idTipoServico, int idJuridica, boolean data,
                                String tipoData, Date dtInicial, Date dtFinal, String dtRefInicial,String dtRefFinal,
                                String ordem, boolean chkPesEmpresa, String porPesquisa, String filtroEmpresa,
                                int idConvencao, int idGrupoCidade, String idsCidades, String idsEsc){
        List result = new ArrayList();
        String textQuery = "SELECT mov.id                       AS idMov," +
                           "       mov.ds_documento             AS numeroDocumento," +
                           "       se.ds_descricao              AS servico," +
                           "       ts.ds_descricao              AS tipoServico," +
                           "       mov.ds_referencia            AS referencia," +
                           "       mov.dt_vencimento            AS vencimento," +
                           "       mov.nr_valor                 AS valor, " +
                           "       se.id                        AS idServico, " +
                           "       ts.id                        AS idTipoServico," +
                           "       pes.id                       AS idPessoa, " +
                           "       pes.ds_nome                  AS nomePessoa, " +
                           "       pes_endereco.ds_descricao    AS enderecoPessoa," +
                           "       pes_logradouro.ds_descricao  AS logradouroPessoa," +
                           "       pes_pend.ds_numero           AS numeroPessoa, " +
                           "       pes_pend.ds_complemento      AS complementoPessoa, " +
                           "       pes_bairro                   AS bairroPessoa," +
                           "       pes_end.ds_cep               AS cepPessoa," +
                           "       pes_cidade.ds_cidade         AS cidadePessoa," +
                           "       pes_cidade.ds_uf             AS ufCidade," +
                           "       pes.ds_telefone1             AS telefonePessoa," +
                           "       pes.ds_email1                AS emailPessoa," +
                           "       pdoc.ds_descricao            AS tipoDocPessoa," +
                           "       pes.ds_documento             AS documentoPessoa," +
                           "       cnae.id                      AS idCnae," +
                           "       cnae.ds_numero               AS numeroCnae," +
                           "       cnae.ds_cnae                 AS nomeCnae," +
                           "       jur.id_contabilidade         AS idContabil," +
                           "       pesc.ds_nome                 AS nomeContabil," +
                           "       esc_endereco.ds_descricao    AS enderecoContabil," +
                           "       esc_logradouro.ds_descricao  AS logradouroContabil," +
                           "       esc_pend.ds_numero           AS numeroContabil," +
                           "       esc_pend.ds_complemento      AS complementoContabil," +
                           "       esc_bairro.ds_descricao      AS bairroContabil," +
                           "       esc_end.ds_cep               AS cepContabil," +
                           "       esc_cidade.ds_cidade         AS cidadeContabil," +
                           "       esc_cidade.ds_uf             AS ufCidade," +
                           "       pesc.ds_telefone1            AS telefoneContabil," +
                           "       pesc.ds_email1               AS emailContabil," +
                           "       lot.id                       AS idLote," +
                           "       lot.dt_baixa                 AS quitacaoLote," +
                           "       lot.dt_importacao            AS importacaoLote," +
                           "       jur.id                       AS idJuridica," +
                           "       upes.ds_nome                 AS usuario," +
                           "       mov.nr_taxa                  AS taxa," +
                           "       mov.nr_multa                 AS multa," +
                           "       mov.nr_juros                 AS juros," +
                           "       mov.nr_correcao              AS correcao," +
                           "       mov.nr_valor_baixa           AS valor_baixa, " +
                           "       cc.nr_repasse                AS vl_repasse " +
                           "  FROM fin_movimento                AS mov " +
                           " INNER JOIN pes_pessoa              AS pes              ON pes.id               = mov.id_pessoa " +
                           " INNER JOIN pes_tipo_documento      AS pdoc             ON pdoc.id              = pes.id_tipo_documento " +
                           " INNER JOIN fin_servicos            AS se               ON se.id                = mov.id_servicos " +
                           " INNER JOIN fin_servico_rotina      AS ser              ON ser.id_servicos      = se.id AND ser.id_rotina=4 " +
                           " INNER JOIN fin_tipo_servico        AS ts               ON ts.id                = mov.id_tipo_servico " +   
                           "  LEFT JOIN fin_baixa               AS lot              ON lot.id               = mov.id_baixa " +
                           "  LEFT JOIN pes_juridica            AS jur              ON jur.id_pessoa        = pes.id " +
                           "  LEFT JOIN pes_juridica            AS esc              ON esc.id               = jur.id_contabilidade " +
                           "  LEFT JOIN pes_pessoa              AS pesc             ON pesc.id              = esc.id_pessoa " +
                           "  LEFT JOIN pes_tipo_documento      AS escdoc           ON escdoc.id            = pes.id_tipo_documento " +
                           "  LEFT JOIN pes_cnae                AS cnae             ON cnae.id              = jur.id_cnae " +
                           "  LEFT JOIN pes_pessoa_endereco     AS pes_pend         ON pes_pend.id_pessoa   = pes.id " +
                           "  LEFT JOIN end_endereco            AS pes_end          ON pes_end.id           = pes_pend.id_endereco " +
                           "  LEFT JOIN end_logradouro          AS pes_logradouro   ON pes_logradouro.id    = pes_end.id_logradouro " +
                           "  LEFT JOIN end_descricao_endereco  AS pes_endereco     ON pes_endereco.id      = pes_end.id_descricao_endereco " +
                           "  LEFT JOIN end_bairro              AS pes_bairro       ON pes_bairro.id        = pes_end.id_bairro " +
                           "  LEFT JOIN end_cidade              AS pes_cidade       ON pes_cidade.id        = pes_end.id_cidade " +
                           "  LEFT JOIN pes_pessoa_endereco     AS esc_pend         ON esc_pend.id_pessoa   = pesc.id " +
                           "  LEFT JOIN end_endereco            AS esc_end          ON esc_end.id           = esc_pend.id_endereco " +
                           "  LEFT JOIN end_logradouro          AS esc_logradouro   ON esc_logradouro.id    = esc_end.id_logradouro " +
                           "  LEFT JOIN end_descricao_endereco  AS esc_endereco     ON esc_endereco.id      = esc_end.id_descricao_endereco " +
                           "  LEFT JOIN end_bairro              AS esc_bairro       ON esc_bairro.id        = esc_end.id_bairro " +
                           "  LEFT JOIN end_cidade              AS esc_cidade       ON esc_cidade.id        = esc_end.id_cidade " +
                           "  LEFT JOIN seg_usuario             AS us               ON us.id                = lot.id_usuario " +
                           "  LEFT JOIN pes_pessoa              AS upes             ON upes.id              = us.id_pessoa " +
                           " INNER JOIN fin_boleto              AS bol              ON bol.nr_ctr_boleto    = mov.nr_ctr_boleto "+
                           " INNER JOIN fin_conta_cobranca      AS cc               ON cc.id                = bol.id_conta_cobranca ";

        
        // CONDICAO -----------------------------------------------------
        if (condicao.equals("todos")){
           textQuery = textQuery + " WHERE mov.is_ativo = true AND (pes_pend.id_tipo_endereco = 2 OR pes_pend.id_tipo_endereco IS NULL) AND (esc_pend.id_tipo_endereco = 2 OR esc_pend.id_tipo_endereco IS NULL) ";
        }else if (condicao.equals("ativos")){
           textQuery = textQuery + " WHERE mov.is_ativo = true AND (pes_pend.id_tipo_endereco = 2 OR pes_pend.id_tipo_endereco IS NULL) AND (esc_pend.id_tipo_endereco = 2 OR esc_pend.id_tipo_endereco IS NULL) " +
                                   "   AND jur.id IN (select c.id_juridica FROM arr_contribuintes_vw c WHERE c.id_motivo IS NULL) ";
        }else if (condicao.equals("inativos")){
           textQuery = textQuery + " WHERE mov.is_ativo = true AND (pes_pend.id_tipo_endereco = 2 OR pes_pend.id_tipo_endereco IS NULL) AND (esc_pend.id_tipo_endereco = 2 OR esc_pend.id_tipo_endereco IS NULL) " +
                                   "   AND jur.id NOT IN (SELECT c.id_juridica FROM arr_contribuintes_vw c) " +
                                   "   AND jur.id IN (SELECT ci.id_juridica FROM arr_contribuintes_inativos ci GROUP BY ci.id_juridica) ";
        }

        // CONTRIBUICAO DE RELATORIO---------------------------------------------
        if (idServico != 0){
           textQuery = textQuery + " AND mov.id_servicos = "+ idServico;
        }

        // TIPO SERVICO DO RELATORIO-----------------------------------------------
        if (idTipoServico != 0){
           textQuery = textQuery + " AND mov.id_tipo_servico = "+ idTipoServico;
        }

        // PESSOA DO RELATORIO-----------------------------------------------------
        if (idJuridica != 0){
            if(filtroEmpresa.equals("empresa")){
                textQuery = textQuery + " AND jur.id = " + idJuridica;
            }else{
                textQuery = textQuery + " AND esc.id = " + idJuridica;
            }
        }
        
        // FILTRAR POR ESCRITÓRIOS ------------------------------------------------        
        if (!idsEsc.isEmpty()){
            if (!idsEsc.equals("sem"))
                textQuery = textQuery + " AND esc.id IN ( " + idsEsc +" )";
            else
                textQuery = textQuery + " AND jur.id_contabilidade IS NULL";
        }        

        // FILTRO MOVIMENTO ---------------------------------------------------------
        if (porPesquisa.equals("todas")){
            //textQuery = textQuery + " AND mov.is_ativo = true";
        }else if (porPesquisa.equals("recebidas")){
            textQuery = textQuery + " AND mov.id_baixa IS NOT NULL";
        }else if (porPesquisa.equals("naorecebidas")){
            textQuery = textQuery + " AND mov.id_baixa IS NULL";
        }else if (porPesquisa.equals("atrasadas")){
            textQuery = textQuery + " AND mov.id_baixa IS NULL" +
                                    " AND mov.dt_vencimento < '" + DataHoje.data()+"'";
        }

        // DATA DO RELATORIO ---------------------------------------------------------
        if (data){
            if (dtInicial != null && dtFinal != null){
                if (tipoData.equals("importacao")){
                    textQuery = textQuery + " AND mov.id_baixa = lot.id" +
                                            " AND lot.dt_importacao >= '"+DataHoje.converteData(dtInicial)+"'" +
                                            " AND lot.dt_importacao <= '"+DataHoje.converteData(dtFinal)+"'";
                }else if (tipoData.equals("recebimento")){
                    textQuery = textQuery + " AND mov.id_baixa = lot.id" +
                                            " AND lot.dt_baixa >= '" +DataHoje.converteData(dtInicial)+"'" +
                                            " AND lot.dt_baixa <= '"+DataHoje.converteData(dtFinal)+"'";
                }else if (tipoData.equals("vencimento")){
                    textQuery = textQuery + " AND mov.dt_vencimento >= '" +DataHoje.converteData(dtInicial)+"'" +
                                            " AND mov.dt_vencimento <= '"+DataHoje.converteData(dtFinal)+"'";
                }
            }else if (!dtRefInicial.equals("") && !dtRefFinal.equals("")){
                String ini = dtRefInicial.substring(3, 7) + dtRefInicial.substring(0, 2);
                String fin = dtRefFinal.substring(3, 7)   + dtRefFinal.substring(0, 2);
                textQuery = textQuery + " AND concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) >=  \'"+ini+ "\' " +
                                        " AND concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) <=  \'"+fin+ "\' ";
            }
        }

        // CONVENCAO DO RELATORIO ------------------------------------------------------------------------------------
        if(idConvencao != 0){
            textQuery = textQuery + " AND jur.id_cnae in (SELECT id_cnae from arr_cnae_convencao WHERE id_convencao = "+idConvencao+")";
        }

        // GRUPO CIDADE DO RELATORIO -----------------------------------------------------------------------------------
        if(idGrupoCidade != 0){
            textQuery = textQuery + " AND pes_cidade.id in (SELECT id_cidade from arr_grupo_cidades WHERE id_grupo_cidade = "+idGrupoCidade+")";
        }

        // IDS CIDADES DA BASE -----------------------------------------------------------------------------------
        if(!idsCidades.isEmpty()){
            textQuery = textQuery + " AND pes_cidade.id in ("+idsCidades+")";
        }

        String ordem2 = "";
        if (relatorios.getQryOrdem() == null || relatorios.getQryOrdem().isEmpty()){
            ordem2 = " order by ";
        }else{
            ordem2 = " order by " + relatorios.getQryOrdem()+ ", ";
        }
        
        // ORDEM DO RELATORIO --------------------------------------------------------
        if (chkPesEmpresa){
            textQuery = textQuery + ordem2 + " pes.ds_nome, ";
            if (ordem.equals("vencimento"))
                textQuery = textQuery + " mov.dt_vencimento ";
            else if (ordem.equals("quitacao"))
                textQuery = textQuery + " lot.dt_baixa ";
            else if (ordem.equals("importacao"))
                textQuery = textQuery + " lot.dt_importacao ";
            else if (ordem.equals("referencia"))
                textQuery = textQuery + " concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3))";
        }else{
            if (ordem.equals("vencimento"))
                textQuery = textQuery + ordem2+ " mov.dt_vencimento ";
            else if (ordem.equals("quitacao"))
                textQuery = textQuery + ordem2+ " lot.dt_baixa ";
            else if (ordem.equals("importacao"))
                textQuery = textQuery + ordem2+ " lot.dt_importacao ";
            else if (ordem.equals("referencia"))
                textQuery = textQuery + ordem2+ " concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3))";

        }

        try{
            Query qry = getEntityManager().createNativeQuery(textQuery);
            result = qry.getResultList();
        }catch(EJBQLException e ){
        }
        return result;
    }
}
