package br.com.rtools.escola.dao;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class VendedorDao extends DB {

    public boolean existeVendedor(Vendedor vendedor) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT V FROM Vendedor V WHERE V.pessoa.id = :idPessoa ");
            qry.setParameter("idPessoa", vendedor.getPessoa().getId());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
