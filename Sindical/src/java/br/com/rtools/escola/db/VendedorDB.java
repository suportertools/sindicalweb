package br.com.rtools.escola.db;

import br.com.rtools.escola.Vendedor;
import java.util.List;
import javax.persistence.EntityManager;

public interface VendedorDB {
    public boolean insert(Vendedor vendedor);
    public boolean update(Vendedor vendedor);
    public boolean delete(Vendedor vendedor);
    public EntityManager getEntityManager();
    public Vendedor pesquisaCodigo(int id);
    public List pesquisaTodos();
}
