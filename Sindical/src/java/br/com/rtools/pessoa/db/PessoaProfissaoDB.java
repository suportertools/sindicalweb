package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.PessoaProfissao;
import java.util.List;
import javax.persistence.EntityManager;

public interface PessoaProfissaoDB {

    public boolean insert(PessoaProfissao pessoaProfissao);

    public boolean update(PessoaProfissao pessoaProfissao);

    public boolean delete(PessoaProfissao pessoaProfissao);

    public EntityManager getEntityManager();

    public PessoaProfissao pesquisaCodigo(int id);

    public List pesquisaTodos();

    public PessoaProfissao pesquisaProfPorFisica(int id);
}
