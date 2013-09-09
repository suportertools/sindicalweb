

package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.IndiceMensal;
import br.com.rtools.financeiro.Servicos;
import java.util.List;
import javax.persistence.EntityManager;


public interface ServicosDB {
    public boolean insert(Servicos servico);
    public boolean update(Servicos servico);
    public boolean delete(Servicos servico);
    public EntityManager getEntityManager();
    public Servicos pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaTodos(int idRotina);
    public Servicos idServicos(Servicos des_servicos);
    public List pesquisaServicos (String desc, String por, String como);
    public Correcao pesquisaCorrecao(int idServico);
    public IndiceMensal pesquisaIndiceMensal(int mes, int ano, int idIndice);
    public List pesquisaTodosPeloContaCobranca(int idRotina);
    public List<Servicos> pesquisaTodosServicos();
    
}


