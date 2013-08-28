package br.com.rtools.escola.db;

import br.com.rtools.escola.ComponenteCurricular;
import java.util.List;
import javax.persistence.EntityManager;

public interface ComponenteCurricularDB {
    public boolean insert(ComponenteCurricular componenteCurricular);
    public boolean update(ComponenteCurricular componenteCurricular);
    public boolean delete(ComponenteCurricular componenteCurricular);
    public ComponenteCurricular pesquisaCodigo(int id);
    public List pesquisaTodos();
    public EntityManager getEntityManager();
    public ComponenteCurricular idComponenteCurricular(ComponenteCurricular des_ComponenteCurricular);
}
