package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class RelatorioEstornoDao extends DB {

    public List<Vector> listaEstorno(String dtLancamentoi, String dtLancamentof, String dtBaixai, String dtBaixaf) {
        String text = 
                "SELECT ecl.dt_lancamento data_lancamento, \n" +
                "       ecl.dt_baixa data_baixa, \n " +
                "       pr.ds_nome responsavel, \n " +
                "       pt.ds_nome titular, \n " +
                "       pb.ds_nome beneficiario, \n " +
                "       ecl.nr_id_baixa id_baixa, \n " +
                "       pu.ds_nome usuario_estorno, \n " +
                "       puc.ds_nome usuario_caixa, \n " +
                "       c.ds_descricao caixa, \n"  +
                "       ecl.ds_motivo motivo_estorno, \n " +
                "       m.dt_vencimento vencimento, \n " +
                "       m.nr_valor valor \n " +
                "  FROM fin_estorno_caixa_lote ecl \n " +
                " INNER JOIN fin_estorno_caixa ec ON ec.id_estorno_caixa_lote = ecl.id \n " +
                " INNER JOIN fin_movimento m ON m.id = ec.id_movimento \n " +
                " INNER JOIN pes_pessoa pr ON m.id_pessoa = pr.id \n " +
                " INNER JOIN pes_pessoa pt ON m.id_pessoa = pt.id \n " +
                " INNER JOIN pes_pessoa pb ON m.id_pessoa = pb.id \n " +
                " INNER JOIN seg_usuario u ON ecl.id_usuario_estorno = u.id \n " +
                " INNER JOIN pes_pessoa pu ON pu.id = u.id_pessoa \n " +
                " INNER JOIN seg_usuario uc ON ecl.id_usuario_caixa = uc.id \n " +
                " INNER JOIN pes_pessoa puc ON puc.id = uc.id_pessoa \n " +
                "  LEFT JOIN fin_caixa c ON c.id = ecl.id_caixa";
        
        String where = "";
        
        // LANCAMENTO INICIAL E FINAL
        if (!dtLancamentoi.isEmpty() && !dtLancamentof.isEmpty()){
            where += " WHERE ecl.dt_lancamento BETWEEN '"+dtLancamentoi+"' AND '"+dtLancamentof+"' \n ";
        }else if (!dtLancamentoi.isEmpty() && dtLancamentof.isEmpty()){
            // LANCAMENTO INICIAL
            where += " WHERE ecl.dt_lancamento >= '"+dtLancamentoi+"' \n ";
        }else if (dtLancamentoi.isEmpty() && !dtLancamentof.isEmpty()){
            // LANCAMENTO FINAL
            where += " WHERE ecl.dt_lancamento <= '"+dtLancamentof+"' \n ";
        }
        
        String w = where.isEmpty() ? " WHERE " : " AND ";
        
        // BAIXA INICIAL E FINAL
        if (!dtBaixai.isEmpty() && !dtBaixaf.isEmpty()){
            where += w + " ecl.dt_baixa BETWEEN '"+dtBaixai+"' AND '"+dtBaixaf+"' \n ";
        // LANCAMENTO INICIAL
        }else if (!dtBaixai.isEmpty() && dtBaixaf.isEmpty()){
            where += w +" ecl.dt_baixa >= '"+dtBaixai+"' \n ";
        // LANCAMENTO FINAL
        }else if (dtBaixai.isEmpty() && !dtBaixaf.isEmpty()){
            where += w + " ecl.dt_baixa <= '"+dtBaixaf+"' \n ";
        }
        
        
        String order = " ORDER BY data_lancamento, id_baixa, responsavel, titular, beneficiario, vencimento ";
        
        try{
            Query qry = getEntityManager().createNativeQuery(text + where + order);
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
}
