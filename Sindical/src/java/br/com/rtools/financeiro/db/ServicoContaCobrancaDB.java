package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ServicoContaCobranca;
import java.util.List;
import javax.persistence.EntityManager;

public interface ServicoContaCobrancaDB {

    public boolean insert(ServicoContaCobranca servicoContaCobranca);

    public boolean update(ServicoContaCobranca servicoContaCobranca);

    public boolean delete(ServicoContaCobranca servicoContaCobranca);

    public EntityManager getEntityManager();

    public ServicoContaCobranca pesquisaCodigo(int id);

    public List pesquisaTodos();
    
    public List pesquisaTodosTipoUm();
    
    public List pesquisaTodosFiltrado();

    public List pesquisaTodosFiltradoAtivo();

    public List pesquisaServPorIdServIdTipoServ(int servico, int tipoServico);

    public ServicoContaCobranca pesquisaServPorIdServIdTipoServIdContCobranca(int servico, int tipoServico, int contaCobranca);

    public List pesquisaServPorIdServ(int servico);

    public List listaContaCobrancaAtivoArrecadacao();
    
    public List listaContaCobrancaAtivoAssociativo();
}
