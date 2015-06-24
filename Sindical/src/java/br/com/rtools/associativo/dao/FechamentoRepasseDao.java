package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.FechamentoRepasse;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FechamentoRepasseDao extends DB {
    
    public List listaDataFechamentoRepasse() {
        try {
            String textqry
                    = " SELECT dt_fechamento \n"
                    + "   FROM soc_fechamento_repasse m "
                    + "  GROUP BY dt_fechamento "
                    + "  ORDER BY dt_fechamento DESC";

            Query qry = getEntityManager().createNativeQuery(textqry);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<FechamentoRepasse> listaFechamentoRepasse(String data) {
        try {
            String textqry
                    = " SELECT fr.* \n"
                    + "   FROM soc_fechamento_repasse fr \n "
                    + "  WHERE fr.dt_fechamento = '"+data+"'";

            Query qry = getEntityManager().createNativeQuery(textqry, FechamentoRepasse.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List listaMovimentoFechamentoRepasse(String dataFechamento, Integer idServico) {
        try {
            String textqry
                  //"se.ds_descricao,p.id,p.ds_nome,m.dt_vencimento,b.dt_baixa,m.nr_valor_baixa\n" +
                    = " SELECT m.* \n " +
                      "   FROM fin_movimento as m \n " +
                      "  INNER JOIN fin_baixa as b on b.id = m.id_baixa \n " +
                      "  INNER JOIN fin_servicos as se on se.id = m.id_servicos \n " +
                      "  INNER JOIN pes_pessoa as p on p.id = m.id_beneficiario \n " +
                      "  INNER JOIN soc_fechamento_repasse sf on sf.id_movimento = m.id \n " +
                      "  WHERE m.is_ativo = true " +
                      "    AND sf.dt_fechamento = '"+dataFechamento+"' \n " +
                      (idServico != null ? " AND m.id_servicos = " + idServico : "") +
                      "  ORDER BY se.ds_descricao, b.dt_baixa";

            Query qry = getEntityManager().createNativeQuery(textqry, Movimento.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public boolean inserirFechamentoRepasse() {
        try {
            getEntityManager().getTransaction().begin();
            String textQuery =
                    " INSERT INTO soc_fechamento_repasse (id_rotina, dt_inicio, dt_fechamento, id_movimento) ( \n"  +
                    "   SELECT \n " +
                    "       314 AS id_rotina, \n " +
                    "       (SELECT MAX(dt_fechamento) + 1 FROM soc_fechamento_repasse) AS dt_inicio, \n " +
                    "       CURRENT_DATE AS dt_fechamento, \n " +
                    "       m.id AS id_movimento \n " +
                    "     FROM fin_movimento AS m \n " +
                    "    INNER JOIN fin_baixa AS b ON b.id = m.id_baixa \n " +
                    "    INNER JOIN fin_servicos AS se ON se.id = m.id_servicos \n " +
                    "    INNER JOIN pes_pessoa AS p ON p.id = m.id_beneficiario \n " +
                    "    WHERE m.is_ativo = true " +
                    "      AND se.id IN (SELECT id_servicos FROM fin_servico_rotina WHERE id_rotina = 314) \n " +
                    "      AND m.id not IN (SELECT id_movimento FROM soc_fechamento_repasse) \n " +
                    " ) ";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            qry.executeUpdate();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.getMessage();
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }    
    
    public boolean excluirFechamentoRepasse(String data) {
        try {
            getEntityManager().getTransaction().begin();
            String textQuery =
                    " DELETE FROM soc_fechamento_repasse WHERE dt_fechamento = '"+data+"'";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            qry.executeUpdate();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.getMessage();
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }    
}
