package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ServicoValor;
import java.util.List;

public interface ServicoValorDB {

    public List pesquisaServicoValor(int idServico);

    public ServicoValor pesquisaServicoValorPorPessoaFaixaEtaria(int idServico, int idPessoa);
    
    public ServicoValor pesquisaServicoValorPorIdade(int idServico, int idade);

    public float pesquisaMaiorResponsavel(int idPessoa);
}
