package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.IndiceMensal;
import br.com.rtools.financeiro.Servicos;
import java.util.List;

public interface ServicosDB {

//    public boolean insert(Servicos servico);
//
//    public boolean update(Servicos servico);
//
//    public boolean delete(Servicos servico);
    // public EntityManager getEntityManager();
//    public Servicos pesquisaCodigo(int id);
//    public List pesquisaTodos();
    public List pesquisaTodos(int idRotina);

    public Servicos idServicos(Servicos des_servicos);

    public List pesquisaServicos(String desc, String por, String como, String situacao);

    public Correcao pesquisaCorrecao(int idServico);

    public IndiceMensal pesquisaIndiceMensal(int mes, int ano, int idIndice);

    public List pesquisaTodosPeloContaCobranca(int idRotina);

//    public List<Servicos> pesquisaTodosServicos();
    public List<Servicos> listaServicoSituacao(int id_rotina, String situacao);

    public List<Servicos> listaServicoSituacaoAtivo();

    public List<Servicos> listaServicosPorSubGrupoFinanceiro(Integer subgrupo);

    public List<Servicos> listaServicosPorSubGrupoFinanceiro(Integer subgrupo, Integer rotina);
}
