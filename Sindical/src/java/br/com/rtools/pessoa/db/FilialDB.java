package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Registro;
import java.util.List;

public interface FilialDB {

    public List pesquisaJuridicaFilial(int idMatriz);

    public Filial pesquisaFilialPertencente(int idMatriz, int idFilial);

    public List pesquisaFilial(String desc, String por, String como, int idmatriz);

    public List pesquisaRazao(int idMatriz);

    public Registro pesquisaRegistroPorFilial(int id);

    public List pesquisaFilialExiste(int idFilial);

    public List pesquisaPessoaConvencaoGCidade(int idPessoa, int idConvencao, int idGCidade);

    public List pesquisaPessoaPatronal(String desc, String por, String como);
}
