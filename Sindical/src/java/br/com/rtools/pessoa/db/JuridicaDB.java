package br.com.rtools.pessoa.db;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Empregados;
import br.com.rtools.arrecadacao.MotivoInativacao;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import java.util.List;
import javax.persistence.EntityManager;

public interface JuridicaDB {

//    public boolean insert(Juridica juridica);
//    public boolean update(Juridica Juridica);
//    public boolean delete(Juridica Juridica);
    public EntityManager getEntityManager();

    public Juridica pesquisaCodigo(int id);

//    public List pesquisaTodos();
    public List pesquisaPessoa(String desc, String por, String como);

    public List pesquisaPessoa(String desc, String por, String como, boolean isContabilidades, boolean isAtivas);

    public List<PessoaEndereco> pesquisarPessoaEnderecoJuridica(int id);

    public CnaeConvencao pesquisaCnaeParaContribuicao(int id);

    public List listaMotivoInativacao();

    public MotivoInativacao pesquisaCodigoMotivoInativacao(int id);

    public Juridica pesquisaJuridicaPorPessoa(int id);

    public List pesquisaJuridicaPorDoc(String doc);

    public List pesquisaPesEndEmpresaComContabil(int idJurCon);

    public List pesquisaJuridicaComEmail();

    public List<Juridica> pesquisaJuridicaParaRetorno(String documento);

    public int quantidadeEmpresas(int idContabilidade);

    public List listaJuridicaContribuinte(int id_juridica);

    public List<Juridica> listaContabilidadePertencente(int id_juridica);

    public List listaJuridicaContribuinteID();

    public List pesquisaContabilidade();

    public int[] listaInadimplencia(int id_juridica);

    /**
     *
     * @param pessoa
     * @param motivo (FECHOU ...)
     * @return
     */
    public boolean empresaInativa(Pessoa pessoa, String motivo);

    public boolean empresaInativa(Integer pessoa);

    public Empregados pesquisaEmpregados(int id_juridica);
    
    public Juridica pesquisaContabilidadePorEmail(String email);
}
