package br.com.rtools.academia.db;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.associativo.MatriculaAcademia;
import java.util.List;

public interface AcademiaDB {

    public AcademiaGrade existeAcademiaGrade(String horaInicio, String horaFim);

    public AcademiaServicoValor existeAcademiaServicoValor(AcademiaServicoValor asv);

    public List<AcademiaServicoValor> listaAcademiaServicoValor(int idServico);

    public List<AcademiaSemana> listaAcademiaSemana(int id_servico_valor);
    
    public List<AcademiaSemana> listaAcademiaSemana();
    
    public boolean existeAcademiaSemana(int idAcademiaGrade, int idSemana);
    
    public AcademiaSemana pesquisaAcademiaSemana(int idAcademiaGrade, int idSemana);
    
    public boolean desfazerMovimento(MatriculaAcademia ma);
    
    public List<AcademiaServicoValor> listaServicoValorPorRotina();
    
    public List<AcademiaServicoValor> listaAcademiaServicoValorPorServico(int idServico);
    
    public List<MatriculaAcademia> pesquisaMatriculaAcademia(String tipo, String por, String como, String descricao);

}
