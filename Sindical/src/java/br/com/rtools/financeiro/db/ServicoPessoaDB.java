package br.com.rtools.financeiro.db;

//import br.com.rtools.associativo.Responsavel;
import br.com.rtools.financeiro.ServicoPessoa;
import java.util.List;
import javax.persistence.EntityManager;

public interface ServicoPessoaDB {

    public EntityManager getEntityManager();

    public ServicoPessoa pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosParaGeracao(String referencia);

    public ServicoPessoa pesquisaServicoPessoaPorPessoa(int idPessoa);

    public List listByPessoa(int idPessoa);

    public List listByPessoaInativo(int idPessoa);

    public List listByCobranca(int idPessoa);

    public List pesquisaTodosParaGeracao(String referencia, int idPessoa);
//    public Responsavel buscaResponsavel(int idServicoPessoa);
}
