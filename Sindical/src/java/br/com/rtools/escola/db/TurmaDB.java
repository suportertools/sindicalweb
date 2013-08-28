package br.com.rtools.escola.db;

import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import java.util.List;

public interface TurmaDB {
    public boolean insert(Turma turma);
    public boolean update(Turma turma);
    public boolean delete(Turma turma);
    public Turma pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List<TurmaProfessor> listaTurmaProfessor(int idTurma);
}