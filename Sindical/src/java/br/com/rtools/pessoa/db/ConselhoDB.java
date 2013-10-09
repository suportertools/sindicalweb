package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Conselho;
import java.util.List;
import javax.persistence.EntityManager;

public interface ConselhoDB {

    public boolean insert(Conselho conselho);

    public boolean update(Conselho conselho);

    public boolean delete(Conselho conselho);

    public EntityManager getEntityManager();

    public Conselho pesquisaCodigo(int id);

    public List pesquisaTodos();

    public Conselho idConselho(Conselho des_conselho);
}
