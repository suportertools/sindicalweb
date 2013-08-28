package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Demissao;
import java.util.List;
import javax.persistence.EntityManager;

public interface DemissaoDB {
    public EntityManager getEntityManager();
    public boolean insert(Demissao demissao);
    public boolean update(Demissao demissao);
    public boolean delete(Demissao demissao);
    public List pesquisaTodos();
    public Demissao pesquisaCodigo(int id);
}
