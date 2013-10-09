package br.com.rtools.escola.db;

import br.com.rtools.escola.EFinanceiro;
import java.util.List;
import javax.persistence.EntityManager;

public interface EFinanceiroDB {

    public boolean insert(EFinanceiro eFinanceiro);

    public boolean update(EFinanceiro eFinanceiro);

    public boolean delete(EFinanceiro eFinanceiro);

    public EntityManager getEntityManager();

    public EFinanceiro pesquisaCodigo(int id);

    public List pesquisaTodos();
}
