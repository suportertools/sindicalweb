package br.com.rtools.escola.db;

import java.util.List;
import javax.persistence.EntityManager;

public interface ComponenteCurricularDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();
}
