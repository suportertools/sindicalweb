package br.com.rtools.escola.db;

import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TurmaDBToplink extends DB implements TurmaDB {

    @Override
    public List<TurmaProfessor> listaTurmaProfessor(int idTurma) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT TP FROM TurmaProfessor TP WHERE TP.turma.id = " + idTurma + " ORDER BY TP.componenteCurricular.descricao ASC, TP.professor.professor.nome ASC ");
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public boolean existeTurma(Turma turma) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT T FROM Turma AS T WHERE T.dtInicio = :dataInicio AND T.dtTermino = :dataTermino AND T.horaInicio = :hInicio AND T.horaTermino = :hTermino  AND T.cursos.id = :idCurso AND T.filial.id = :idFilial ");
            qry.setParameter("dataInicio", turma.getDtInicio());
            qry.setParameter("dataTermino", turma.getDtTermino());
            qry.setParameter("hInicio", turma.getHoraInicio());
            qry.setParameter("hTermino", turma.getHoraTermino());
            qry.setParameter("idCurso", turma.getCursos().getId());
            qry.setParameter("idFilial", turma.getFilial().getId());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

    @Override
    public boolean existeTurmaProfessor(TurmaProfessor turmaProfessor) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT TP FROM TurmaProfessor AS TP WHERE TP.turma.id = :idTurma AND TP.componenteCurricular.id = :idComponenteCurricular AND TP.professor.id = :idProfessor ");
            qry.setParameter("idTurma", turmaProfessor.getTurma().getId());
            qry.setParameter("idProfessor", turmaProfessor.getProfessor().getId());
            qry.setParameter("idComponenteCurricular", turmaProfessor.getComponenteCurricular().getId());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
