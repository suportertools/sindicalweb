package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Profissao;
import java.util.List;
import javax.persistence.EntityManager;

public interface ProfissaoDB {

    public boolean insert(Profissao profissao);

    public boolean update(Profissao profissao);

    public boolean delete(Profissao profissao);

    public EntityManager getEntityManager();

    public Profissao pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<String> pesquisaProfissao(String des_tipo);

    public Profissao idProfissao(Profissao des_prof);

    public List pesquisaProfParametros(String por, String combo, String desc);
}
