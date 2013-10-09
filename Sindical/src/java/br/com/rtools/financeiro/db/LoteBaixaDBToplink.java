package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import br.com.rtools.financeiro.Baixa;
import java.util.List;
import javax.persistence.Query;

public class LoteBaixaDBToplink extends DB implements LoteBaixaDB {

    public Baixa pesquisaCodigo(int id) {
        Baixa result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("LoteBaixa.pesquisaID");
            qry.setParameter("pid", id);
            result = (Baixa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from LoteBaixa p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public Baixa pesquisaLoteBaixaPorNumeroBoleto(String numero, int idContaCobranca) {
        try {
            Query qry = getEntityManager().createQuery(
                    " select m.loteBaixa "
                    + "   from Movimento m"
                    + "  where m.numero = :numero"
                    + "    and m.contaCobranca.id = :idContaCobranca");

            qry.setParameter("numero", numero);
            qry.setParameter("idContaCobranca", idContaCobranca);
            return (Baixa) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}