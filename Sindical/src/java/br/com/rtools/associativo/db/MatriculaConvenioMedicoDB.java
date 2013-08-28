package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaConvenioMedicoDB {
    public boolean insert(MatriculaConvenioMedico matriculaConvenioMedico);
    public boolean update(MatriculaConvenioMedico matriculaConvenioMedico);
    public boolean delete(MatriculaConvenioMedico matriculaConvenioMedico);
    public EntityManager getEntityManager();
    public MatriculaConvenioMedico pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaConvenioMedico(String desc, String por, String como);
}
