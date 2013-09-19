package br.com.rtools.escola.db;

import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.pessoa.PessoaComplemento;
import java.util.List;
import javax.persistence.EntityManager;

public interface MatriculaEscolaDB {
    public MatriculaEscola pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaTodosEscStatus();
    public EntityManager getEntityManager();
    public List<MatriculaEscola> pesquisaMatriculaEscola(String porPesquisa, String descricaoCurso, String descricaoAluno, String comoPesquisa);
    public MatriculaIndividual pesquisaCodigoMIndividual(int matriculaEscola);
    public MatriculaTurma pesquisaCodigoMTurma(int matriculaEscola);
    public PessoaComplemento pesquisaDataRefPessoaComplemto(int idPessoa);
    public boolean verificaPessoaEnderecoDocumento(String tipoPessoa, int idPessoa);
    public boolean desfazerMovimento(int idLote, int idMatriculaEscola);
//    public List<GMatriculaEscolaPesquisa> pesquisaMatriculaEscola(boolean isIndividual, String curso, String nome, boolean isInicial);
//    public List<GMatriculaEscola> retornaPesquisaMatriculaEscola(int id);
}
