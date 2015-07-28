package br.com.rtools.escola.dao;

import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TurmaDao extends DB {

    public List<TurmaProfessor> listaTurmaProfessor(int idTurma) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT TP FROM TurmaProfessor TP WHERE TP.turma.id = " + idTurma + " ORDER BY TP.componenteCurricular.descricao ASC, TP.professor.professor.nome ASC ");
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    public boolean existeTurma(Turma turma) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT T FROM Turma AS T WHERE T.dtInicio = :dataInicio AND T.dtTermino = :dataTermino AND T.horaInicio = :hInicio AND T.horaTermino = :hTermino  AND T.cursos.id = :idCurso AND T.filial.id = :idFilial AND T.sala = :sala ");
            qry.setParameter("dataInicio", turma.getDtInicio());
            qry.setParameter("dataTermino", turma.getDtTermino());
            qry.setParameter("hInicio", turma.getHoraInicio());
            qry.setParameter("hTermino", turma.getHoraTermino());
            qry.setParameter("sala", turma.getSala());
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

    public List listaTurmaAtiva() {
        return listaTurmaAtivaPorFilialServico(-1, -1);
    }

    public List listaTurmaAtivaPorFilial(int idFilial) {
        return listaTurmaAtivaPorFilialServico(idFilial, -1);
    }

    public List listaTurmaAtivaPorFilialServico(int idFilial, int idServico) {
        try {
            String queryFilial = idFilial > 0 ? " AND T.filial.id = " + idFilial : "";
            String queryServico = idServico > 0 ? " AND T.cursos.id = " + idServico : "";
            Query qry = getEntityManager().createQuery("SELECT T FROM Turma AS T WHERE T.dtTermino >= CURRENT_DATE " + queryFilial + " " + queryServico + " ORDER BY T.cursos.descricao ASC, T.sala ASC, T.descricao ASC, T.dtInicio DESC, T.horaInicio ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findbyFilial(Integer filial_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT T FROM Turma AS T WHERE T.dtTermino >= CURRENT_DATE AND T.filial.id = :filial_id ORDER BY T.cursos.descricao ASC, T.sala ASC, T.descricao ASC, T.dtInicio DESC, T.horaInicio ASC ");
            query.setParameter("filial_id", filial_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
