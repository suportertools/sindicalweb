package br.com.rtools.escola.db;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class VendedorDBToplink extends DB implements VendedorDB {

    @Override
    public Vendedor pesquisaCodigo(int id) {
        Vendedor result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Vendedor.pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                return (Vendedor) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT V FROM Vendedor AS V ORDER BY V.pessoa.nome ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
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
