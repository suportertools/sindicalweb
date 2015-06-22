package br.com.rtools.escola.dao;

import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RescisaoContratoDao extends DB{
    
    public List<Movimento> listaMovimentoMatricula(Integer idPessoa, Integer idServico) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT m FROM Movimento m WHERE m.pessoa.id = " + idPessoa + " AND m.servicos.id = "+idServico);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
    
    public List<Movimento> listaMovimentoPagos(Integer evt) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "select m.* \n " +
                    " from fin_lote as l \n " +
                    "inner join fin_movimento as m on m.id_lote=l.id \n " +
                    "where  \n " +
                    "m.dt_vencimento-(cast(extract(day from m.dt_vencimento) as int)+1) >= \n " +
                    "current_date-(cast(extract(day from current_date) as int)+1) \n " +
                    "and l.id_evt="+evt+" and m.id_baixa is not null", Movimento.class
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
