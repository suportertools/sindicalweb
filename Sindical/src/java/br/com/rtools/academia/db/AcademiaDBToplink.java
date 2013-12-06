package br.com.rtools.academia.db;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AcademiaDBToplink extends DB implements AcademiaDB {

    @Override
    public AcademiaGrade existeAcademiaGrade(String horaInicio, String horaFim) {
        try {
            Query query = getEntityManager().createQuery("SELECT AG FROM AcademiaGrade AS AG WHERE AG.horaInicio = :horaInicio AND AG.horaFim = :horaFim");
            query.setParameter("horaInicio", horaInicio);
            query.setParameter("horaFim", horaFim);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AcademiaGrade) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public AcademiaServicoValor existeAcademiaServicoValor(AcademiaServicoValor asv) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servico AND ASV.periodo.id = :periodo AND ASV.academiaGrade.id = :grade");
            query.setParameter("servico", asv.getServicos().getId());
            query.setParameter("periodo", asv.getPeriodo().getId());
            query.setParameter("grade", asv.getAcademiaGrade().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AcademiaServicoValor) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    
    @Override
    public List<AcademiaServicoValor> listaAcademiaServicoValor(int idServico) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servicos ORDER BY ASV.periodo.descricao ASC, ASV.servicos.descricao ASC, ASV.academiaGrade.horaInicio ASC");
            query.setParameter("servicos", idServico);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
    
    @Override
    public List<AcademiaSemana> listaAcademiaSemana(int idAcademiaGrade) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASE FROM AcademiaSemana AS ASE WHERE ASE.academiaGrade.id = :academiaGrade");
            query.setParameter("academiaGrade", idAcademiaGrade);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
    
    @Override
    public boolean existeAcademiaSemana(int idAcademiaGrade, int idSemana) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASE FROM AcademiaSemana AS ASE WHERE ASE.academiaGrade.id = :academiaGrade AND ASE.semana.id = :semana");
            query.setParameter("academiaGrade", idAcademiaGrade);
            query.setParameter("semana", idSemana);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }            
        } catch (Exception e) {
            
        }
        return false;        
    }
    
    @Override
    public AcademiaSemana pesquisaAcademiaSemana(int idAcademiaGrade, int idSemana) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASE FROM AcademiaSemana AS ASE WHERE ASE.academiaGrade.id = :academiaGrade AND ASE.semana.id = :semana");
            query.setParameter("academiaGrade", idAcademiaGrade);
            query.setParameter("semana", idSemana);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AcademiaSemana) query.getSingleResult();
            }            
        } catch (Exception e) {
            
        }
        return null;        
    }
}
