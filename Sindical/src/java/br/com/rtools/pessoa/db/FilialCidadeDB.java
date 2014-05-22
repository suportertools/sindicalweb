package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.FilialCidade;

public interface FilialCidadeDB {

    public FilialCidade pesquisaFilialCidade(int idFilial, int idCidade);

    public FilialCidade pesquisaFilialPorCidade(int idCidade);
}
