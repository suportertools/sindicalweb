package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.OposicaoPessoa;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.relatorios.Relatorios;
import java.util.List;
import java.util.Vector;

public interface OposicaoDB {

    public ConvencaoPeriodo pesquisaConvencaoPeriodo(int id_convencao, int id_grupo);

    public List<ConvencaoPeriodo> listaConvencaoPeriodo();

    public List<Oposicao> listaTodos();

    public Oposicao pesquisaOposicao(int id_fisica, int id_juridica);

    public PessoaEmpresa pesquisaPessoaFisicaEmpresa(String cpf, String rg);

    public OposicaoPessoa pesquisaOposicaoPessoa(String cpf, String rg);

    public List<Vector> pesquisaPessoaConvencaoGrupoCidade(int id);

    public List<Oposicao> pesquisaOposicao(String descricaoPesquisa, String tipoPesquisa, String comoPesquisa);

    public boolean validaOposicao(Oposicao oposicao);

    public List filtroRelatorio(int idEmpresa, Integer idFuncionario, String emissaoInicial, String emissaoFinal, String convencaoPeriodo, Relatorios r, String inCnaes);

    public List<ConvencaoPeriodo> listaConvencaoPeriodoPorOposicao();

    public List<Cnae> listaCnaesPorOposicaoJuridica(String inIdsCnaeConvencao);
}
