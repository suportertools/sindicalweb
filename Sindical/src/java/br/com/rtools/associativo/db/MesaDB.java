package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Mesa;
import br.com.rtools.financeiro.Movimento;
import java.util.List;
import javax.persistence.EntityManager;

public interface MesaDB {
    public boolean insert(Mesa mesa);
    public boolean update(Mesa mesa);
    public boolean delete(Mesa mesa);
    public EntityManager getEntityManager();
    public Mesa pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List<Mesa> pesquisaTodosPorEvento(int idEvento);
    public Mesa pesquisaVendaPorEvt(int idVenda);
    public Movimento pesquisaMovimentoPorVenda(int idVenda);

}
