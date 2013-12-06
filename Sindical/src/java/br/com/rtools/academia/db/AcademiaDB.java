package br.com.rtools.academia.db;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.AcademiaServicoValor;
import java.util.List;

public interface AcademiaDB {

    public AcademiaGrade existeAcademiaGrade(String horaInicio, String horaFim);

    public AcademiaServicoValor existeAcademiaServicoValor(AcademiaServicoValor asv);

    public List<AcademiaServicoValor> listaAcademiaServicoValor(int idServico);

    public List<AcademiaSemana> listaAcademiaSemana(int idAcademiaGrade);
    
    public boolean existeAcademiaSemana(int idAcademiaGrade, int idSemana);
    
    public AcademiaSemana pesquisaAcademiaSemana(int idAcademiaGrade, int idSemana);

}
