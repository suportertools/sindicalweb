package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ServicoValor;
import java.util.Date;
import java.util.List;

public interface ServicoValorDB {

    public List pesquisaServicoValor(int idServico);

    public ServicoValor pesquisaServicoValorPorPessoaFaixaEtaria(int idServico, int idPessoa);

    public float pesquisaMaiorResponsavel(int idPessoa);

    public float funcaoValorServico(int idPessoa, int idServico, Date date, int tipo);
}
