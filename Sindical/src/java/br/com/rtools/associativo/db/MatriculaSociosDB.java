package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaSocios;
import java.util.List;

public interface MatriculaSociosDB {

    public MatriculaSocios pesquisaPorNrMatricula(int idGpCategoria, int nrMatricula);
    
    public List listaMatriculaPorGrupoNrMatricula(int idGpCategoria, int nrMatricula);
}
