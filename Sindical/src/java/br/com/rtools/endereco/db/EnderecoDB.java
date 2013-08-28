
package br.com.rtools.endereco.db;

import br.com.rtools.endereco.*;
import br.com.rtools.pessoa.PessoaEndereco;
import java.util.List;
import javax.persistence.EntityManager;


public interface EnderecoDB {

    public boolean insert(Endereco endereco);

    public boolean update(Endereco endereco);
    public boolean delete(Endereco endereco);    
    public EntityManager getEntityManager();    
    public Endereco pesquisaCodigo (int id);
    public List pesquisaEnderecoCep(String cep);
    public List pesquisaTodos();
    public List pesquisaEnderecoDes(String uf, String cidade, String logradouro, String descricao, String iniParcial);
    public PessoaEndereco pesquisaPessoaEndCobranca(int idPessoa);
    public List pesquisaEndereco(int idDescricao, int idCidade, int idBairro, int idLogradouro);
    public List<Logradouro> pesquisaTodosOrdenado();    
    public DescricaoEndereco pesquisaDescricaoEndereco(int id);
    public Logradouro pesquisaLogradouro(int id);
    public Bairro pesquisaBairro(int id);
    public Cidade pesquisaCidade(int id);   
}

