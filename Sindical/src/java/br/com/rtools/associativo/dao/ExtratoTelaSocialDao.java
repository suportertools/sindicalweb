package br.com.rtools.associativo.dao;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class ExtratoTelaSocialDao extends DB{
    
    public List<Vector> listaMovimentosSocial(
            String porPesquisa, String ordenacao, String tipoDataPesquisa, String dataInicial, String dataFinal, String dataRefInicial, String dataRefFinal, String boletoInicial, String boletoFinal, String tipoPessoa, Integer id_pessoa, Integer id_servico, Integer id_tipo_servico
    ){
        
        String text
                = " SELECT m.id, \n " // 00
                + "        pr.ds_documento, \n " // 01
                + "        pr.ds_nome, \n " // 02
                + "        pt.ds_nome, \n " // 03
                + "        pb.ds_nome, \n " // 04
                + "        bo.ds_boleto as boleto, \n " // 05
                + "        s.ds_descricao, \n " // 06
                + "        ts.ds_descricao, \n " // 07
                + "        m.ds_referencia, \n " // 08
                + "        m.dt_vencimento, \n " // 09
                + "        m.nr_valor, \n " // 10
                + "        b.dt_baixa, \n " // 11
                + "        m.nr_valor_baixa, \n " // 12
                + "        m.nr_taxa, \n " // 13
                + "        m.id_baixa, \n " // 14
                + "        bo.id, \n " // 15
                + "        pt.ds_documento, \n " // 16
                + "        pb.ds_documento, \n " // 17
                + "        bo.dt_vencimento, \n " // 18
                + "        bo.dt_vencimento_original, \n " // 19
                + "        b.dt_importacao \n " // 20
                + "   FROM fin_movimento m \n "
                + "  INNER JOIN pes_pessoa pr ON pr.id = m.id_pessoa -- RESPONSAVEL \n "
                + "  INNER JOIN pes_pessoa pt ON pt.id = m.id_titular -- TITULAR \n "
                + "  INNER JOIN pes_pessoa pb ON pb.id = m.id_beneficiario -- BENEFICIARIO \n "
                + "  INNER JOIN fin_servicos s ON s.id = m.id_servicos \n "
                + "  INNER JOIN fin_tipo_servico ts ON ts.id = m.id_tipo_servico \n "
                + "   LEFT JOIN fin_baixa b ON b.id = m.id_baixa \n "
                + "   LEFT JOIN fin_boleto bo ON bo.nr_ctr_boleto = m.nr_ctr_boleto \n "
                // PEGAR MOVIMENTOS QUE FORAM EXCLUIDOS ----------------------------------
//                + "   LEFT JOIN fin_movimento_inativo mi ON m.id = mi.id_movimento \n "
//                + "   LEFT JOIN seg_usuario u ON u.id = mi.id_usuario \n "
//                + "   LEFT JOIN pes_pessoa pu ON pu.id = u.id_pessoa \n "
                // PEGAR MOVIMENTOS QUE FORAM EXCLUIDOS ----------------------------------
                + "  WHERE m.is_ativo = true AND s.id NOT IN (SELECT sr.id_servicos FROM fin_servico_rotina sr WHERE sr.id_rotina = 4) \n ";
        
        String and = "", order = "";
        
        switch(porPesquisa){
            case "todos":
                break;
                
            case "recebidas":
                and += " AND m.id_baixa IS NOT NULL \n ";
                break;
                
            case "naoRecebidas":
                and += " AND m.id_baixa IS NULL \n ";
                break;
                
            case "atrasadas":
                and += " AND m.id_baixa IS NULL AND m.dt_vencimento < CURRENT_DATE \n ";
                break;
        }
        
        if (id_pessoa != null && id_pessoa != -1){
            switch(tipoPessoa){
                case "nenhum":
                    //and += " AND m.id_pessoa = "+id_pessoa;
                    break;

                case "responsavel":
                    and += " AND m.id_pessoa = "+id_pessoa;
                    break;

                case "titular":
                    and += " AND m.id_titular = "+id_pessoa;
                    break;

                case "beneficiario":
                    and += " AND m.id_beneficiario = "+id_pessoa;
                    break;
            }
        }
        
        switch (tipoDataPesquisa) {
            case "recebimento":
                if (!dataInicial.isEmpty() && dataFinal.isEmpty()) {
                    and += " and b.dt_baixa >= '" + dataInicial + "'";
                } else if (!dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                    and += " and b.dt_baixa >= '" + dataInicial + "' and b.dt_baixa <= '" + dataFinal + "'";
                } else if (dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                    and += "' and b.dt_baixa <= '" + dataFinal + "'";
                }
                break;
            case "importacao":
                if (!dataInicial.isEmpty() && dataFinal.isEmpty()) {
                    and += " and b.dt_importacao >= '" + dataInicial+"'";
                } else if (!dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                    and += " and b.dt_importacao >= '" + dataInicial + "' and b.dt_importacao <= '" + dataFinal + "'";
                } else if (dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                    and += "' and b.dt_importacao <= '" + dataFinal + "'";
                }
                break;
            case "vencimento":
                if (!dataInicial.isEmpty() && dataFinal.isEmpty()) {
                    and += " and m.dt_vencimento >= '" + dataInicial + "'";
                } else if (!dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                    and += " and m.dt_vencimento >= '" + dataInicial + "' and m.dt_vencimento <= '" + dataFinal + "'";
                } else if (dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                    and += "' and m.dt_vencimento <= '" + dataFinal + "'";
                }
                break;
            case "referencia":
                if (!dataRefInicial.isEmpty() && dataRefFinal.isEmpty()) {
                    String ini = dataRefInicial.substring(3, 7) + dataRefInicial.substring(0, 2);
                    and += " and substring(m.ds_referencia, 4, 8)|| substring(m.ds_referencia, 0, 3) >= '" + ini + "'";
                } else if (!dataRefInicial.isEmpty() && !dataRefFinal.isEmpty()) {
                    String ini = dataRefInicial.substring(3, 7) + dataRefInicial.substring(0, 2);
                    String fin = dataRefFinal.substring(3, 7) + dataRefFinal.substring(0, 2);
                    and += " and substring(m.ds_referencia, 4, 8)|| substring(m.ds_referencia, 0, 3) >= '" + ini + "' and substring(m.ds_referencia, 4, 8)|| substring(m.ds_referencia, 0, 3) <= '" + fin + "'";
                } else if (dataRefInicial.isEmpty() && !dataRefFinal.isEmpty()) {
                    String fin = dataRefFinal.substring(3, 7) + dataRefFinal.substring(0, 2);
                    and += "' substring(m.ds_referencia, 4, 8)|| substring(m.ds_referencia, 0, 3) <= '" + fin + "'";
                }
                break;
        }
        

        if (!boletoInicial.isEmpty() && boletoFinal.isEmpty()) {
            and += " and bo.ds_boleto >= '" + boletoInicial + "'";
        } else if (!boletoInicial.isEmpty() && !boletoFinal.isEmpty()) {
            and += " and bo.ds_boleto >= '" + boletoInicial + "'"
                    + " and bo.ds_boleto <= '" + boletoFinal + "'";
        } else if (boletoInicial.isEmpty() && !boletoFinal.isEmpty()) {
            and += " and bo.ds_boleto <= '" + boletoFinal + "'";
        }
        
        if (id_servico != 0){
            and += " AND m.id_servicos = " + id_servico;
        }
        
        if (id_tipo_servico != 0){
            and += " AND m.id_tipo_servico = " + id_tipo_servico;
        }

        switch(ordenacao){
            case "referencia":
                order += " ORDER BY substring(m.ds_referencia, 4, 8)|| substring(m.ds_referencia, 0, 3) DESC \n";
                break;
                
            case "vencimento":
                order += " ORDER BY m.dt_vencimento DESC \n";
                break;
                
            case "quitacao":
                order += " ORDER BY b.dt_baixa DESC \n";
                break;
                
            case "importacao":
                order += " ORDER BY b.dt_importacao DESC \n";
                break;
                
            case "boleto":
                order += " ORDER BY bo.ds_boleto DESC \n";
                break;
        }
        
        
        try {
            Query qry = getEntityManager().createNativeQuery(
                    text + and + order + " LIMIT 15000 "
            );
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<Servicos> listaServicosAssociativo(){
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT s FROM Servicos s where s.id NOT IN (SELECT sr.servicos.id FROM ServicoRotina sr WHERE sr.rotina.id = 4) ORDER BY s.descricao"
            );
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
}
