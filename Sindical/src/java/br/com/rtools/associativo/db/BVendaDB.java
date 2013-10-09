package br.com.rtools.associativo.db;

import br.com.rtools.associativo.BVenda;
import java.util.List;
import javax.persistence.EntityManager;

public interface BVendaDB {

    public boolean insert(BVenda bVenda);

    public boolean update(BVenda bVenda);

    public boolean delete(BVenda bVenda);

    public EntityManager getEntityManager();

    public BVenda pesquisaCodigo(int id);

    public List pesquisaTodos();

    public void excluirMesa(int idEvento);

    public BVenda pesquisaVendaPorEvt(int idEvt);
}
