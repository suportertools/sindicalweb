package br.com.rtools.escola.db;

import br.com.rtools.escola.MatriculaContrato;
import br.com.rtools.escola.MatriculaContratoServico;
import java.util.List;

public interface MatriculaContratoDB {

    public MatriculaContrato pesquisaCodigoServico(int id);

    public boolean existeMatriculaContrato(MatriculaContrato matriculaContrato);

    public List pesquisaTodosPorModulo(int idModulo);

    public List pesquisaTodos();

    public List<MatriculaContratoServico> pesquisaMatriculaContratoServico(int idMatriculaContrato);

    public boolean validaMatriculaContratoServico(int idMatriculaContrato, int idServico);
}
