package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Parentesco;
import java.util.List;
import javax.persistence.EntityManager;

public interface ParentescoDB {

    public boolean insert(Parentesco parentesco);

    public boolean update(Parentesco parentesco);

    public boolean delete(Parentesco parentesco);

    public EntityManager getEntityManager();

    public Parentesco pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosSemTitular();

    public List pesquisaTodosSemTitularPorSexo(String sexo);
}
