package br.com.rtools.associativo.db;

import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaConvenioMedicoDB {

    public EntityManager getEntityManager();

    public List pesquisaConvenioMedico(String desc, String por, String como, Boolean ativo);
}
