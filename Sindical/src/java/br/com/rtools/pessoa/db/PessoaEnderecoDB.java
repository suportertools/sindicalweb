package br.com.rtools.pessoa.db;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.PessoaEndereco;
import java.util.List;
import javax.persistence.EntityManager;

public interface PessoaEnderecoDB {

    public boolean insert(PessoaEndereco pessoaEndereco);

    public boolean update(PessoaEndereco pessoaEndereco);

    public boolean delete(PessoaEndereco pessoaEndereco);

    public EntityManager getEntityManager();

    public PessoaEndereco pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaEndPorPessoa(int id);

    public PessoaEndereco pesquisaEndPorPessoaTipo(int idPessoa, int idTipoEndereco);

    public Endereco enderecoReceita(String cep, String[] descricao, String[] bairro);
}
