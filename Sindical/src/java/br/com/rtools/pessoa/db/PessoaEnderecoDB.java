package br.com.rtools.pessoa.db;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.PessoaEndereco;
import java.util.List;
import javax.persistence.EntityManager;

public interface PessoaEnderecoDB {

    public EntityManager getEntityManager();

    public List pesquisaEndPorPessoa(int id);

    public PessoaEndereco pesquisaEndPorPessoaTipo(int idPessoa, int idTipoEndereco);

    public Endereco enderecoReceita(String cep, String[] descricao, String[] bairro);
    
    public List<PessoaEndereco> listaEnderecoContabilidadeDaEmpresa(Integer id_empresa, Integer id_tipo_endereco);
}
