package br.com.rtools.financeiro.db;

import java.util.List;
import javax.persistence.EntityManager;

public interface ServicoRotinaDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public List pesquisaServicoRotinaPorServico(int idServico);

    public List pesquisaTodasRotinasSemServicoOrdenado(int idServico);

    public List pesquisaTodosServicosComRotinas(int idRotina);

    public boolean existeServicoRotina(int idServico, int idRotina);
}
