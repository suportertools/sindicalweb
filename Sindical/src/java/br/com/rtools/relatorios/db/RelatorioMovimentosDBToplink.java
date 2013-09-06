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
        String textQuery = "select mov.id               as idMov," +
                           "       mov.ds_documento     as numeroDocumento," +
                           "       se.ds_descricao      as servico," +
                           "       ts.ds_descricao      as tipoServico," +
                           "       mov.ds_referencia    as referencia," +
                           "       mov.dt_vencimento    as vencimento," +
                           "       mov.nr_valor         as valor, " +
                           "       se.id                as idServico, " +
                           "       ts.id                as idTipoServico," +
                           "       pes.id               as idPessoa, " +
                           "       pes.ds_nome          as nomePessoa, " +
                           "       pes_endereco.ds_descricao as enderecoPessoa," +
                           "       pes_logradouro.ds_descricao as logradouroPessoa," +
                           "       pes_pend.ds_numero   as numeroPessoa, " +
                           "       pes_pend.ds_complemento as complementoPessoa, " +
                           "       pes_bairro           as bairroPessoa," +
                           "       pes_end.ds_cep       as cepPessoa," +
                           "       pes_cidade.ds_cidade as cidadePessoa," +
                           "       pes_cidade.ds_uf     as ufCidade," +
                           "       pes.ds_telefone1     as telefonePessoa," +
                           "       pes.ds_email1        as emailPessoa," +
                           "       pdoc.ds_descricao    as tipoDocPessoa," +
                           "       pes.ds_documento     as documentoPessoa," +
                           "       cnae.id              as idCnae," +
                           "       cnae.ds_numero       as numeroCnae," +
                           "       cnae.ds_cnae         as nomeCnae," +
                           "       jur.id_contabilidade as idContabil," +
                           "       pesc.ds_nome         as nomeContabil," +
                           "       esc_endereco.ds_descricao as enderecoContabil," +
                           "       esc_logradouro.ds_descricao as logradouroContabil," +
                           "       esc_pend.ds_numero   as numeroContabil," +
                           "       esc_pend.ds_complemento as complementoContabil," +
                           "       esc_bairro.ds_descricao as bairroContabil," +
                           "       esc_end.ds_cep       as cepContabil," +
                           "       esc_cidade.ds_cidade as cidadeContabil," +
                           "       esc_cidade.ds_uf     as ufCidade," +
                           "       pesc.ds_telefone1    as telefoneContabil," +
                           "       pesc.ds_email1       as emailContabil," +
                           "       lot.id               as idLote," +
                           "       lot.dt_baixa         as quitacaoLote," +
                           "       lot.dt_importacao    as importacaoLote," +
                           "       jur.id               as idJuridica," +
                           "       upes.ds_nome         as usuario," +
                           "       mov.nr_taxa          as taxa," +
                           "       mov.nr_multa         as multa," +
                           "       mov.nr_juros         as juros," +
                           "       mov.nr_correcao      as correcao," +
                           "       mov.nr_valor_baixa   as valor_baixa, " +
                           "       cc.nr_repasse        as vl_repasse " +
                           "  from fin_movimento               as mov " +
                           " inner join pes_pessoa         as pes   on pes.id  = mov.id_pessoa " +
                           " inner join pes_tipo_documento as pdoc  on pdoc.id = pes.id_tipo_documento " +
                           " inner join fin_servicos       as se    on se.id = mov.id_servicos " +
                           " inner join fin_servico_rotina as ser on ser.id_servicos=se.id and ser.id_rotina=4 " +
                           " inner join fin_tipo_servico   as ts   on ts.id = mov.id_tipo_servico " +   
                           "  left join fin_baixa           as lot   on lot.id  = mov.id_baixa " +
                           "  left join pes_juridica       as jur     on jur.id_pessoa = pes.id " +
                           "  left join pes_juridica       as esc     on esc.id = jur.id_contabilidade " +
                           "  left join pes_pessoa         as pesc    on pesc.id = esc.id_pessoa " +
                           "  left join pes_tipo_documento as escdoc  on escdoc.id = pes.id_tipo_documento " +
                           "  left join pes_cnae as cnae  on cnae.id = jur.id_cnae " +
                           "  left join pes_pessoa_endereco    as pes_pend       on pes_pend.id_pessoa = pes.id " +
                           "  left join end_endereco           as pes_end        on pes_end.id         = pes_pend.id_endereco " +
                           "  left join end_logradouro         as pes_logradouro on pes_logradouro.id  = pes_end.id_logradouro " +
                           "  left join end_descricao_endereco as pes_endereco   on pes_endereco.id    = pes_end.id_descricao_endereco " +
                           "  left join end_bairro             as pes_bairro     on pes_bairro.id      = pes_end.id_bairro " +
                           "  left join end_cidade             as pes_cidade     on pes_cidade.id      = pes_end.id_cidade " +
                           "  left join pes_pessoa_endereco    as esc_pend       on esc_pend.id_pessoa = pes.id " +
                           "  left join end_endereco           as esc_end        on esc_end.id         = esc_pend.id_endereco " +
                           "  left join end_logradouro         as esc_logradouro on esc_logradouro.id  = esc_end.id_logradouro " +
                           "  left join end_descricao_endereco as esc_endereco   on esc_endereco.id    = esc_end.id_descricao_endereco " +
                           "  left join end_bairro             as esc_bairro     on esc_bairro.id      = esc_end.id_bairro " +
                           "  left join end_cidade             as esc_cidade     on esc_cidade.id      = esc_end.id_cidade " +
                           "  left join seg_usuario    as us   on us.id=lot.id_usuario " +
                           "  left join pes_pessoa     as upes on upes.id=us.id_pessoa " +
                           " inner join fin_boleto as bol on bol.nr_ctr_boleto = mov.nr_ctr_boleto "+
                           " inner join fin_conta_cobranca as cc on cc.id = bol.id_conta_cobranca ";

        
        // CONDICAO -----------------------------------------------------
        if (condicao.equals("todos")){
           textQuery = textQuery + " where  mov.is_ativo = true and (pes_pend.id_tipo_endereco=2 or pes_pend.id_tipo_endereco is null) and (esc_pend.id_tipo_endereco=2 or esc_pend.id_tipo_endereco is null) ";
        }else if (condicao.equals("ativos")){
           textQuery = textQuery + " where  mov.is_ativo = true and (pes_pend.id_tipo_endereco=2 or pes_pend.id_tipo_endereco is null) and (esc_pend.id_tipo_endereco=2 or esc_pend.id_tipo_endereco is null) " +
                                   "    and jur.id in (select c.id_juridica from arr_contribuintes_vw c) ";
        }else if (condicao.equals("inativos")){
           textQuery = textQuery + " where  mov.is_ativo = true and (pes_pend.id_tipo_endereco=2 or pes_pend.id_tipo_endereco is null) and (esc_pend.id_tipo_endereco=2 or esc_pend.id_tipo_endereco is null) " +
                                   "    and jur.id not in (select c.id_juridica from arr_contribuintes_vw c) " +
                                   "    and jur.id in (select ci.id_juridica from arr_contribuintes_inativos ci group by ci.id_juridica) ";
        }

        // CONTRIBUICAO DE RELATORIO---------------------------------------------
        if (idServico != 0){
           textQuery = textQuery + " and mov.id_servicos = "+ idServico;
        }

        // TIPO SERVICO DO RELATORIO-----------------------------------------------
        if (idTipoServico != 0){
           textQuery = textQuery + " and mov.id_tipo_servico = "+ idTipoServico;
        }

        // PESSOA DO RELATORIO-----------------------------------------------------
        if (idJuridica != 0){
            if(filtroEmpresa.equals("empresa")){
                textQuery = textQuery + " and jur.id = " + idJuridica;
            }else{
                textQuery = textQuery + " and esc.id = " + idJuridica;
            }
        }
        
        // FILTRAR POR ESCRITÓRIOS ------------------------------------------------        
        if (!idsEsc.isEmpty()){
            if (!idsEsc.equals("sem"))
                textQuery = textQuery + " and esc.id in ( " + idsEsc +" )";
            else
                textQuery = textQuery + " and jur.id_contabilidade is null";
        }        

        // FILTRO MOVIMENTO ---------------------------------------------------------
        if (porPesquisa.equals("todas")){
            //textQuery = textQuery + " and mov.is_ativo = true";
        }else if (porPesquisa.equals("recebidas")){
            textQuery = textQuery + " and mov.id_baixa is not null";
        }else if (porPesquisa.equals("naorecebidas")){
            textQuery = textQuery + " and mov.id_baixa is null";
        }else if (porPesquisa.equals("atrasadas")){
            textQuery = textQuery + " and mov.id_baixa is null" +
                                    " and mov.dt_vencimento < '" + DataHoje.data()+"'";
        }

        // DATA DO RELATORIO ---------------------------------------------------------
        if (data){
            if (dtInicial != null && dtFinal != null){
                if (tipoData.equals("importacao")){
                    textQuery = textQuery + " and mov.id_baixa = lot.id" +
                                            " and lot.dt_importacao >= '"+DataHoje.converteData(dtInicial)+"'" +
                                            " and lot.dt_importacao <= '"+DataHoje.converteData(dtFinal)+"'";
                }else if (tipoData.equals("recebimento")){
                    textQuery = textQuery + " and mov.id_baixa = lot.id" +
                                            " and lot.dt_baixa >= '" +DataHoje.converteData(dtInicial)+"'" +
                                            " and lot.dt_baixa <= '"+DataHoje.converteData(dtFinal)+"'";
                }else if (tipoData.equals("vencimento")){
                    textQuery = textQuery + " and mov.dt_vencimento >= '" +DataHoje.converteData(dtInicial)+"'" +
                                            " and mov.dt_vencimento <= '"+DataHoje.converteData(dtFinal)+"'";
                }
            }else if (!dtRefInicial.equals("") && !dtRefFinal.equals("")){
                String ini = dtRefInicial.substring(3, 7) + dtRefInicial.substring(0, 2);
                String fin = dtRefFinal.substring(3, 7)   + dtRefFinal.substring(0, 2);
                textQuery = textQuery + " and concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) >=  \'"+ini+ "\' " +
                                        " and concatenar(substring(mov.ds_referencia, 4, 8), substring(mov.ds_referencia, 0, 3)) <=  \'"+fin+ "\' ";
            }
        }

        // CONVENCAO DO RELATORIO ------------------------------------------------------------------------------------
        if(idConvencao != 0){
            textQuery = textQuery + " and jur.id_cnae in (select id_cnae from arr_cnae_convencao where id_convencao = "+idConvencao+")";
        }

        // GRUPO CIDADE DO RELATORIO -----------------------------------------------------------------------------------
        if(idGrupoCidade != 0){
            textQuery = textQuery + " and pes_cidade.id in (select id_cidade from arr_grupo_cidades where id_grupo_cidade = "+idGrupoCidade+")";
        }

        // IDS CIDADES DA BASE -----------------------------------------------------------------------------------
        if(!idsCidades.isEmpty()){
            textQuery = textQuery + " and pes_cidade.id in ("+idsCidades+")";
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