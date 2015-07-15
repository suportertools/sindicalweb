package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.PessoaEmpresa;
import java.util.List;
import javax.persistence.EntityManager;

public interface PessoaEmpresaDB {

    public boolean insert(PessoaEmpresa pessoaEmpresa);

    public boolean update(PessoaEmpresa pessoaEmpresa);

    public boolean delete(PessoaEmpresa pessoaEmpresa);

    public EntityManager getEntityManager();

    public PessoaEmpresa pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List listaPessoaEmpresaPorFisica(int id);

    public List listaPessoaEmpresaPorFisicaDemissao(int id);
    
    public List<PessoaEmpresa> listaPessoaEmpresaPorFisicaEmpresaDemissao(int id, int id_juridica);
    
    public PessoaEmpresa pesquisaPessoaEmpresaPorFisica(int id);

    public List listaPessoaEmpresaTodos(int id);

    public PessoaEmpresa pesquisaPessoaEmpresaPorPessoa(int idPessoa);
    
    public List<PessoaEmpresa> listaPessoaEmpresaPorJuridica(int id_juridica);
}
