package br.com.rtools.escola.db;

import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaEscolaDBToplink extends DB implements MatriculaEscolaDB {

    @Override
    public MatriculaEscola pesquisaCodigo(int id) {
        MatriculaEscola result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MatriculaEscola.pesquisaID");
            qry.setParameter("pid", id);
            result = (MatriculaEscola) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select v from MatriculaEscola v");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaTodosEscStatus() {
        try {
            Query qry = getEntityManager().createQuery("select e from EscStatus e");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    public List pesquisaTodosProfessores() {
        try {
            Query qry = getEntityManager().createQuery("select p from EscProfessor p");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List<MatriculaEscola> pesquisaMatriculaEscola(String porPesquisa, String descricaoCurso, String descricaoAluno, String comoPesquisa) {
        try {
            List<MatriculaEscola> list;
            String complementoQuery = "";
            Query query;
            if (porPesquisa.equals("matriculaIndividual")) {
                if (!descricaoCurso.equals("")) {
                    complementoQuery = " AND UPPER(mi.curso.descricao) LIKE :descricaoCurso ";
                }
                query = getEntityManager().createQuery(" SELECT mi.matriculaEscola FROM MatriculaIndividual mi WHERE UPPER(mi.matriculaEscola.aluno.nome) LIKE :descricaoAluno AND mi.matriculaEscola.habilitado = true " + complementoQuery);
            } else {
                if (!descricaoCurso.equals("")) {
                    complementoQuery = " AND UPPER(mt.turma.cursos.descricao) LIKE :descricaoCurso ";
                }
                query = getEntityManager().createQuery(" SELECT mt.matriculaEscola FROM MatriculaTurma mt WHERE UPPER(mt.matriculaEscola.aluno.nome) LIKE :descricaoAluno AND mt.matriculaEscola.habilitado = true " + complementoQuery);
            }
            if (comoPesquisa.equals("Inicial")) {
                query.setParameter("descricaoAluno", "" + descricaoAluno.toUpperCase() + "%");
                if (!descricaoCurso.equals("")) {
                    query.setParameter("descricaoCurso", "" + descricaoCurso.toUpperCase() + "%");
                }
                if (!descricaoCurso.equals("")) {
                    query.setParameter("descricaoCurso", "" + descricaoCurso.toUpperCase() + "%");
                }
            } else if (comoPesquisa.equals("Parcial")) {
                query.setParameter("descricaoAluno", "%" + descricaoAluno.toUpperCase() + "%");
                if (!descricaoCurso.equals("")) {
                    query.setParameter("descricaoCurso", "%" + descricaoCurso.toUpperCase() + "%");
                }
            }
            list = query.getResultList();
            return list;
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public MatriculaIndividual pesquisaCodigoMIndividual(int matriculaEscola) {
        MatriculaIndividual individual = new MatriculaIndividual();
        try {
            Query query = getEntityManager().createQuery(" SELECT mi FROM MatriculaIndividual mi WHERE mi.matriculaEscola.id = :matriculaEscola ");
            query.setParameter("matriculaEscola", matriculaEscola);
            individual = (MatriculaIndividual) query.getSingleResult();
            return individual;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public MatriculaTurma pesquisaCodigoMTurma(int matriculaEscola) {
        try {
            Query query = getEntityManager().createQuery(" SELECT mt FROM MatriculaTurma mt WHERE mt.matriculaEscola.id = :matriculaEscola ");
            query.setParameter("matriculaEscola", matriculaEscola);
            return (MatriculaTurma) query.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public PessoaComplemento pesquisaDataRefPessoaComplemto(int idPessoa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT pes FROM PessoaComplemento pes WHERE pes.pessoa.id = :idPessoa ");
            query.setParameter("idPessoa", idPessoa);
            return (PessoaComplemento) query.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public boolean verificaPessoaEnderecoDocumento(String tipoPessoa, int idPessoa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT pes FROM PessoaEndereco pes WHERE pes.pessoa.id = :idPessoa and pes.pessoa.documento not like '0'");
            query.setParameter("idPessoa", idPessoa);
            if(query.getResultList().size() > 0){
                if(tipoPessoa.equals("fisica")){
                    Query queryFisica = getEntityManager().createNativeQuery("  "
                    + " SELECT *                                                "
                    + "   FROM pes_fisica                                       " 
                    + "  WHERE func_idade(dt_nascimento, current_date) >= 18    "
                    + "    AND id_pessoa = "+idPessoa);
                    if(queryFisica.getResultList().size() <= 0){
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return false;
    }
    
    public ServicoValor pesquisaValorTaxa(int idCurso) {
        ServicoValor servicoValor = new ServicoValor();
        try {
            Query query = getEntityManager().createQuery(" SELECT sv FROM ServicoValor sv WHERE sv.servicos.id = :idServico ");
            query.setParameter("idServico", idCurso);
            servicoValor = (ServicoValor) query.getSingleResult();
            return servicoValor;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public boolean desfazerMovimento(int idLote, int idMatriculaEscola) {
        try {
            getEntityManager().getTransaction().begin();
            List list = new ArrayList();
            list.add(" DELETE FROM Movimento m WHERE m.lote.id = "+idLote );
            list.add(" UPDATE MatriculaEscola me SET me.lote = null WHERE me.id = "+ idMatriculaEscola );
            list.add(" DELETE FROM Lote l WHERE l.id = "+idLote );
            for(int i = 0; i < list.size(); i++){
                Query query = getEntityManager().createQuery(list.get(i).toString());
                query.executeUpdate();
            }
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            e.getMessage();
            return false;
        }
    }
    
//    public ServicoValor pesquisaServicoPorPessoa(int idPessoa){
//        ServicoValor servicoValor = new ServicoValor();
//        try{
//            Query query = getEntityManager().createNativeQuery(""
//                    + "     SELECT fin.*
//                    + "       FROM fin_servico_valor fin 
//                    + " INNER JOIN pes_fisica vw ON( vw.id_pessoa = :idPessoa)
//"where func_idade(dt_nascimento, current_date) >= nr_idade_ini and func_idade(dt_nascimento, current_date) <= nr_idade_fim and id_servico = 102"
//                    + "");
//        }catch(Exception e){
//            e.getMessage();
//        }
//        
//    
//        return
//    }

}
