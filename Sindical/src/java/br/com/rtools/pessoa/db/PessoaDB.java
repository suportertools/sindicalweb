package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.JuridicaReceita;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.PessoaSemCadastro;
import java.util.List;
import javax.persistence.EntityManager;

public interface PessoaDB {

    public boolean insert(Pessoa pessoa);

    public boolean update(Pessoa pessoa);

    public boolean delete(Pessoa pessoa);

    public EntityManager getEntityManager();

    public Pessoa pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosSemLogin();

    public boolean atualizarPessoaFisica(Fisica pessoaFisica);

    public Pessoa ultimoId();

    public List pesquisarPessoa(String desc, String por, String como);

    public List pessoasPermitidas(int idGrupo, int idConvencao);

    public Pessoa pessoaPermitida(int idPessoa);

    public List pessoasSemMovimento(List idPessoa, String idRef, int idTipoServ, int idServicos);

    public boolean pessoaSemMovimento(int idPessoa, String idRef, int idTipoServ, int idServicos);

    public Pessoa pessoaDocumento(String documento);

    public JuridicaReceita pesquisaJuridicaReceita(String documento);

    public PessoaComplemento pesquisaPessoaComplementoPorPessoa(int idPessoa);
    
    public PessoaSemCadastro pesquisaPessoaSemCadastro(String documento);
}
