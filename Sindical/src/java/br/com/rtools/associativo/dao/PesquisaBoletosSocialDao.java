package br.com.rtools.associativo.dao;

import br.com.rtools.financeiro.MovimentoBoleto;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PesquisaBoletosSocialDao extends DB {
        
    public List<MovimentoBoleto> listaMovimentoBoleto(String boleto) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT mb.* \n " +
                    "  FROM fin_movimento_boleto mb \n " +
                    " INNER JOIN fin_boleto b ON mb.id_boleto = b.id \n " +
                    " INNER JOIN fin_movimento m ON m.id = mb.id_movimento \n " +
                    " WHERE m.is_ativo = true \n " +
                    "   AND b.ds_boleto = '"+boleto+"' \n " +
                    " ORDER BY m.dt_vencimento DESC", MovimentoBoleto.class
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
