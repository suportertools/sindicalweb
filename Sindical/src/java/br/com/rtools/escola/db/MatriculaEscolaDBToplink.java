package br.com.rtools.escola.db;

import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
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
        try {
            Query query = getEntityManager().createQuery(" SELECT mi FROM MatriculaIndividual mi WHERE mi.matriculaEscola.id = :matriculaEscola ");
            query.setParameter("matriculaEscola", matriculaEscola);
            MatriculaIndividual individual = (MatriculaIndividual) query.getSingleResult();
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
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    return (PessoaComplemento) query.getSingleResult();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return new PessoaComplemento();
    }

    @Override
    public boolean verificaPessoaEnderecoDocumento(String tipoPessoa, int idPessoa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT pes FROM PessoaEndereco pes WHERE pes.pessoa.id = :idPessoa and pes.pessoa.documento not like '0'");
            query.setParameter("idPessoa", idPessoa);
            if (query.getResultList().size() > 0) {
                if (tipoPessoa.equals("fisica")) {
                    Query queryFisica = getEntityManager().createNativeQuery("  "
                            + " SELECT *                                                "
                            + "   FROM pes_fisica                                       "
                            + "  WHERE func_idade(dt_nascimento, current_date) >= 18    "
                            + "    AND id_pessoa = " + idPessoa);
                    if (queryFisica.getResultList().size() <= 0) {
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
        try {
            Query query = getEntityManager().createQuery(" SELECT sv FROM ServicoValor sv WHERE sv.servicos.id = :idServico ");
            query.setParameter("idServico", idCurso);
            ServicoValor servicoValor = (ServicoValor) query.getSingleResult();
            return servicoValor;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean desfazerMovimento(MatriculaEscola me) {
        LoteDB loteDB = new LoteDBToplink();
        Lote lote = (Lote) loteDB.pesquisaLotePorEvt(me.getEvt());
        if (lote == null) {
            return false;
        }
        if (lote.getId() == -1) {
            return false;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        try {
            Query queryMovimentos = getEntityManager().createQuery("SELECT M FROM Movimento AS M WHERE M.lote.evt.id = " + me.getEvt().getId());
            List<Movimento> listMovimentos = (List<Movimento>) queryMovimentos.getResultList();
            salvarAcumuladoDB.abrirTransacao();
            for (int i = 0; i < listMovimentos.size(); i++) {
                if (!salvarAcumuladoDB.deletarObjeto((Movimento) salvarAcumuladoDB.pesquisaCodigo(listMovimentos.get(i).getId(), "Movimento"))) {
                    salvarAcumuladoDB.desfazerTransacao();
                    return false;
                }
            }
            if (lote.getId() != -1) {
                if (!salvarAcumuladoDB.deletarObjeto((Lote) salvarAcumuladoDB.pesquisaCodigo(lote.getId(), "Lote"))) {
                    salvarAcumuladoDB.desfazerTransacao();
                    return false;
                }
            }
            int idEvt = me.getEvt().getId();
            me.setEvt(null);
            if (!salvarAcumuladoDB.alterarObjeto(me)) {
                salvarAcumuladoDB.desfazerTransacao();
                return false;
            }
            if (!salvarAcumuladoDB.deletarObjeto((Evt) salvarAcumuladoDB.pesquisaCodigo(idEvt, "Evt"))) {
                salvarAcumuladoDB.desfazerTransacao();
                return false;
            }
            salvarAcumuladoDB.comitarTransacao();
            return true;
        } catch (Exception e) {
            salvarAcumuladoDB.desfazerTransacao();
            return false;
        }
    }

    @Override
    public boolean existeMatriculaTurma(MatriculaTurma mt) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MT FROM MatriculaTurma AS MT WHERE MT.turma.id = :idTurma AND MT.matriculaEscola.aluno.id = :idAluno");
            query.setParameter("idTurma", mt.getTurma().getId());
            query.setParameter("idAluno", mt.getMatriculaEscola().getAluno().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean existeMatriculaIndividual(MatriculaIndividual mi) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MI FROM MatriculaIndividual AS MI WHERE MI.curso.id = :idCurso AND MI.matriculaEscola.aluno.id = :idAluno AND MI.dataInicio = :dataInicio AND MI.dataTermino = :dataTermino");
            query.setParameter("idCurso", mi.getCurso().getId());
            query.setParameter("idAluno", mi.getMatriculaEscola().getAluno().getId());
            query.setParameter("dataInicio", mi.getDataInicio());
            query.setParameter("dataTermino", mi.getDataTermino());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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
