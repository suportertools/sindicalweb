package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class LancamentoFinanceiroDBToplink extends DB implements LancamentoFinanceiroDB{
    public List<TipoDocumento> listaTipoDocumento() {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT tp " +
                    "  FROM TipoDocumento tp " +
                    " WHERE tp.id in (1,2)"
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<TipoDocumento>();
    }
}
