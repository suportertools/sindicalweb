package br.com.rtools.escola.db;

import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import java.util.List;

public interface TurmaDB {

    public List<TurmaProfessor> listaTurmaProfessor(int idTurma);

    public boolean existeTurmaProfessor(TurmaProfessor turmaProfessor);

    public boolean existeTurma(Turma turma);

    public List listaTurmaAtiva();

    public List listaTurmaAtivaPorFilial(int idFilial);
    
    public List listaTurmaAtivaPorFilialServico(int idFilial, int idServico);

}
