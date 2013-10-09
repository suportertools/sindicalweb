package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.EnvioEmails;
import java.util.List;
import javax.persistence.EntityManager;

public interface EnvioEmailsDB {

    public boolean insert(EnvioEmails envioEmails);

    public boolean update(EnvioEmails envioEmails);

    public boolean delete(EnvioEmails envioEmails);

    public EntityManager getEntityManager();

    public EnvioEmails pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<EnvioEmails> pesquisaTodosPorPessoa(int idPessoa);
}
