package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ServicoRotina;
import java.util.List;
import javax.persistence.EntityManager;

public interface ServicoRotinaDB {
    public boolean insert(ServicoRotina servicoRotina);
    public boolean update(ServicoRotina servicoRotina);
    public boolean delete(ServicoRotina servicoRotina);
    public EntityManager getEntityManager();
    public ServicoRotina pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaServicoRotinaPorServico(int idServico);
    public List pesquisaTodasRotinasSemServicoOrdenado(int idServico);
    public List pesquisaTodosServicosComRotinas(int idRotina);
}
