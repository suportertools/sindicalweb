package br.com.rtools.escola.dao;

import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RescisaoContratoDao extends DB{
    
    public List<Movimento> listaMovimentoMatricula(Integer idPessoa, Integer idServico) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT m FROM Movimento m WHERE m.pessoa.id = " + idPessoa + " AND m.servicos.id = "+idServico+ " AND m.ativo = TRUE");
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
    
    public List<Movimento> listaMovimentoPagos(Integer evt) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " SELECT m.* \n " +
                    "   FROM fin_lote l \n " +
                    "  INNER JOIN fin_movimento m ON m.id_lote = l.id \n " +
                    "  WHERE m.dt_vencimento - (CAST(EXTRACT(day FROM m.dt_vencimento) AS int) + 1) >= CURRENT_DATE - (CAST(EXTRACT(day FROM CURRENT_DATE) AS int)+1) \n " +
                    "    AND l.id_evt = "+ evt +
                    "    AND m.id_baixa IS NOT NULL \n " +
                    "    AND m.is_ativo = TRUE ", Movimento.class
            );
            
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
    
    public List<Lote> listaLoteEVT(Integer evt) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT l FROM Lote l WHERE l.evt.id = " + evt
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
    
    public List<Movimento> listaMovimentoEVT(Integer evt) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT m FROM Movimento m WHERE m.lote.evt.id = " + evt + " and m.ativo = TRUE and m.baixa IS NULL"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
