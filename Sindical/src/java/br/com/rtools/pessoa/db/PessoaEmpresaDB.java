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
    public PessoaEmpresa pesquisaPessoaEmpresaPorFisica(int id);
    public List listaPessoaEmpresaTodos(int id);
}
