package br.com.rtools.escola.db;

import br.com.rtools.escola.EscolaAutorizados;
import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.PessoaComplemento;
import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaEscolaDB {

    public EntityManager getEntityManager();

    public List<MatriculaEscola> pesquisaMatriculaEscola(String tipoMatricula, String descricaoCurso, String descricao, String comoPesquisa, String porPesquisa, int filtroStatus, Filial filial);

    public MatriculaIndividual pesquisaCodigoMIndividual(int matriculaEscola);

    public MatriculaTurma pesquisaCodigoMTurma(int matriculaEscola);

    public PessoaComplemento pesquisaDataRefPessoaComplemto(int idPessoa);

    public boolean verificaPessoaEnderecoDocumento(String tipoPessoa, int idPessoa);

    public boolean desfazerMovimento(MatriculaEscola me);

    public boolean existeMatriculaTurma(MatriculaTurma mt);

    public boolean existeMatriculaIndividual(MatriculaIndividual mi);

    public boolean existeVagasDisponivel(MatriculaTurma mt);

    public List<EscolaAutorizados> listaPessoasAutorizas(int idMatricula);

    public List<MatriculaTurma> pesquisaMatriculaEscolaPorTurma(int idTurma);

    public List<Servicos> listServicosPorMatriculaIndividual();

}
