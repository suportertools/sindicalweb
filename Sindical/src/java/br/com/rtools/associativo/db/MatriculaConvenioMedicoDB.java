package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaConvenioMedicoDB {

    public EntityManager getEntityManager();

    public List pesquisaConvenioMedico(String desc, String por, String como, Boolean ativo);
    
    public List<MatriculaConvenioMedico> listaConvenioPessoa(int id_pessoa, int id_servico);
}
