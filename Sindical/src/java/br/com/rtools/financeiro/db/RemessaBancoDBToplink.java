package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import java.util.Vector;
import javax.persistence.Query;

public class RemessaBancoDBToplink extends DB implements RemessaBancoDB {

    public Object pesquisaRemessaBancoCobranca(int id_cobranca) {
        Object result = new Vector();
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " select max(rb.nr_lote) + 1 from fin_remessa_banco rb "
                    + " inner join fin_movimento m on (m.id = rb.id_movimento) "
                    + " inner join fin_conta_cobranca c on (m.id_conta_cobranca = c.id) "
                    + " and c.id = " + id_cobranca);
            result = (Object) qry.getSingleResult();



        } catch (Exception e) {
        }
        return ((Vector) result).get(0);
    }
}
