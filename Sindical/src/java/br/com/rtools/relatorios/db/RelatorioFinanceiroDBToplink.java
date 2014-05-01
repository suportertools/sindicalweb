package br.com.rtools.relatorios.db;

import br.com.rtools.financeiro.FStatus;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class RelatorioFinanceiroDBToplink extends DB implements RelatorioFinanceiroDB{
    @Override
    public List<Vector> listaChequesRecebidos(String ids_filial, String ids_caixa, String tipo_data, String data_inicial, String data_final, int id_status){
        String text = "SELECT " +
                      "     j.ds_fantasia AS filial, " +
                      "     ch.dt_emissao AS emissao, " +
                      "     ch.dt_vencimento AS vencimento, " +
                      "     ch.ds_banco AS banco, " +
                      "     ch.ds_agencia AS agencia, " +
                      "     ch.ds_conta AS conta, " +
                      "     ch.ds_cheque AS cheque, " +
                      "     f.nr_valor AS valor, " +
                      "     b.id AS id_baixa, " +
                      "     cx.nr_caixa||'/'||cx.ds_descricao AS caixa " +
                      " FROM fin_cheque_rec AS ch " +
                      "INNER JOIN fin_forma_pagamento AS f ON f.id_cheque_rec = ch.id AND f.id_baixa = func_idBaixa_cheque_rec(ch.id) " +
                      "INNER JOIN fin_baixa AS b ON b.id = f.id_baixa " +
                      "INNER JOIN fin_caixa AS cx ON cx.id = b.id_caixa " +
                      "INNER JOIN pes_filial AS pf ON pf.id_filial = cx.id_filial " +
                      "INNER JOIN pes_juridica AS j ON j.id = pf.id_filial";
        
        String filter = "";
        String order_by = "";
        
        if (!ids_filial.isEmpty()){
            filter = filter.isEmpty() ? " WHERE pf.id_filial IN ("+ids_filial+") " : " AND pf.id_filial IN ("+ids_filial+") ";
        }
        
        if (!ids_caixa.isEmpty()){
            filter += filter.isEmpty() ? " WHERE cx.id IN ("+ids_caixa+") " : " AND cx.id IN ("+ids_caixa+") ";
        }
        
        if (!tipo_data.isEmpty() && (!data_inicial.isEmpty() || !data_final.isEmpty())){
            if (!data_inicial.isEmpty() && !data_final.isEmpty()){ // DATA INICIAL E FINAL
                filter += filter.isEmpty() ? " WHERE ch.dt_"+tipo_data+" >= '"+data_inicial+"' " + " AND ch.dt_"+tipo_data+" <= '"+data_final+"' " 
                                           : " AND ch.dt_"+tipo_data+" >= '"+data_inicial+"' " + " AND ch.dt_"+tipo_data+" <= '"+data_final+"' ";
            }else if (!data_inicial.isEmpty() && data_final.isEmpty()){ // POR DATA INICIAL
                filter += filter.isEmpty() ? " WHERE ch.dt_"+tipo_data+" >= '"+data_inicial+"' " 
                                           : " AND ch.dt_"+tipo_data+" >= '"+data_inicial+"' ";
            }else if (data_inicial.isEmpty() && !data_final.isEmpty()){ // POR DATA FINAL
                filter += filter.isEmpty() ? " WHERE ch.dt_"+tipo_data+" <= '"+data_final+"' " 
                                           : " AND ch.dt_"+tipo_data+" <= '"+data_final+"' ";
            }
        }
        
        if (id_status != 0){
            filter += filter.isEmpty() ? " WHERE ch.id_status = " + id_status : " AND ch.id_status = " + id_status;
        }
        
        try{
            Query qry = getEntityManager().createNativeQuery(text + filter + order_by);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<FStatus> listaStatusCheque(String ids) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "  select s"
                    + "  from FStatus s "
                    + " where s.id in (" + ids + ")"
                    + " order by s.descricao");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
}
