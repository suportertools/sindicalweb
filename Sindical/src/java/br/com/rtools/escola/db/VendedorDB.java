package br.com.rtools.escola.db;

import br.com.rtools.escola.Vendedor;
import java.util.List;
import javax.persistence.EntityManager;

public interface VendedorDB {

    public EntityManager getEntityManager();

    public Vendedor pesquisaCodigo(int id);

    public List pesquisaTodos();

    public boolean existeVendedor(Vendedor vendedor);
}
