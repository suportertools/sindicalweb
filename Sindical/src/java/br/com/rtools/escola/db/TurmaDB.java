package br.com.rtools.escola.db;

import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import java.util.List;

public interface TurmaDB {

    public List pesquisaTodos();

    public List<TurmaProfessor> listaTurmaProfessor(int idTurma);

    public boolean existeTurmaProfessor(TurmaProfessor turmaProfessor);

    public boolean existeTurma(Turma turma);
}