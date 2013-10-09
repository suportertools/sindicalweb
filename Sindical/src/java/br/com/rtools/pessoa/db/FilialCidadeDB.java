package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.FilialCidade;
import java.util.List;

public interface FilialCidadeDB {

    public boolean insert(FilialCidade filialCidade);

    public boolean update(FilialCidade filialCidade);

    public boolean delete(FilialCidade filialCidade);

    public FilialCidade pesquisaCodigo(int id);

    public List pesquisaTodos();

    public FilialCidade pesquisaFilialCidade(int idFilial, int idCidade);

    public FilialCidade pesquisaFilialPorCidade(int idCidade);
}
