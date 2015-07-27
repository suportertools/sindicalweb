package br.com.rtools.escola.dao;

import br.com.rtools.escola.EscolaAutorizados;
import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaEscolaDao extends DB {

    public List<MatriculaEscola> pesquisaMatriculaEscola(String tipoMatricula, String descricaoCurso, String descricao, String comoPesquisa, String porPesquisa, int filtroStatus, Filial filial) {
        try {
            String text = "", inner = "", filter = "";

            text = "SELECT me.* \n "
                    + "  FROM matr_escola me \n "
                    + " INNER JOIN fin_servico_pessoa sp ON sp.id = me.id_servico_pessoa \n "
                    + " INNER JOIN fin_servicos s ON s.id = sp.id_servico \n "
                    + " INNER JOIN esc_status es ON es.id = me.id_status \n ";
            if (filtroStatus != 0 && filtroStatus != 5) {
                filter += " WHERE es.id = " + filtroStatus + " \n ";
            } else {
                filter += " WHERE es.id IN (SELECT id FROM esc_status) \n ";
            }

            if (tipoMatricula.equals("Individual")) {
                inner += " INNER JOIN esc_matr_individual mi ON mi.id_matr_escola = me.id \n ";
            } else {
                inner += " INNER JOIN esc_matr_turma mt ON mt.id_matr_escola = me.id \n ";
            }

            if (porPesquisa.equals("aluno")) {
                inner += " INNER JOIN pes_pessoa p ON p.id = sp.id_pessoa \n ";
            } else {
                inner += " INNER JOIN pes_pessoa p ON p.id = sp.id_cobranca \n ";
            }

            if (!descricaoCurso.isEmpty()) {
                descricaoCurso = AnaliseString.normalizeLower(descricaoCurso);
                descricaoCurso = (comoPesquisa.equals("Inicial") ? descricaoCurso + "%" : "%" + descricaoCurso + "%");
                filter += " AND LOWER(FUNC_TRANSLATE(s.ds_descricao)) LIKE '" + descricaoCurso + "' \n ";
            }

            if (!descricao.isEmpty()) {
                descricao = AnaliseString.normalizeLower(descricao);
                descricao = (comoPesquisa.equals("Inicial") ? descricao + "%" : "%" + descricao + "%");
                filter += " AND LOWER(FUNC_TRANSLATE(p.ds_nome)) LIKE '" + descricao + "' \n ";
            }

            if (filial != null) {
                inner += "  INNER JOIN pes_filial f ON f.id = me.id_filial AND f.id = " + filial.getId() + " \n ";
            }
            Query qry = getEntityManager().createNativeQuery(text + inner + filter, MatriculaEscola.class);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public MatriculaIndividual pesquisaCodigoMIndividual(int matriculaEscola) {
        try {
            Query query = getEntityManager().createQuery(" SELECT mi FROM MatriculaIndividual mi WHERE mi.matriculaEscola.id = :matriculaEscola ");
            query.setParameter("matriculaEscola", matriculaEscola);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (MatriculaIndividual) query.getSingleResult();
            }
        } catch (Exception e) {
            return new MatriculaIndividual();
        }
        return new MatriculaIndividual();
    }

    public MatriculaTurma pesquisaCodigoMTurma(int matriculaEscola) {
        try {
            Query query = getEntityManager().createQuery(" SELECT mt FROM MatriculaTurma mt WHERE mt.matriculaEscola.id = :matriculaEscola ");
            query.setParameter("matriculaEscola", matriculaEscola);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (MatriculaTurma) query.getSingleResult();
            }
        } catch (Exception e) {
            return new MatriculaTurma();
        }
        return new MatriculaTurma();
    }

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

    public String desfazerMovimento(MatriculaEscola me) {
        LoteDB loteDB = new LoteDBToplink();
        Lote lote = (Lote) loteDB.pesquisaLotePorEvt(me.getServicoPessoa().getEvt());

        if (lote == null) {
            return null;
        }

        if (lote.getId() == -1) {
            return null;
        }

        //SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        Dao dao = new Dao();
        try {
            Query queryMovimentos = getEntityManager().createQuery("SELECT M FROM Movimento AS M WHERE M.lote.evt.id = " + me.getServicoPessoa().getEvt().getId() + " AND M.baixa IS NOT NULL AND M.ativo = TRUE");
            List<Movimento> listMovimentos = (List<Movimento>) queryMovimentos.getResultList();

            if (!listMovimentos.isEmpty()) {
                return "Movimentos Baixados não podem ser Excluídos!";
            }

            queryMovimentos = getEntityManager().createQuery("SELECT M FROM Movimento AS M WHERE M.lote.evt.id = " + me.getServicoPessoa().getEvt().getId() + " AND M.baixa IS NULL AND M.ativo = TRUE");
            listMovimentos = (List<Movimento>) queryMovimentos.getResultList();

            dao.openTransaction();
            for (Movimento listMovimento : listMovimentos) {
                if (!dao.delete(listMovimento)) {
                    dao.rollback();
                    return "Não foi possível excluir Movimento!";
                }
            }
            if (lote.getId() != -1) {
                if (!dao.delete(lote)) {
                    dao.rollback();
                    return "Não foi possível excluir lote!";
                }
            }
            Evt evt = (Evt) dao.find(new Evt(), me.getServicoPessoa().getEvt().getId());
            me.getServicoPessoa().setEvt(null);
            if (!dao.update(me.getServicoPessoa())) {
                dao.rollback();
                return "Não foi possível excluir Matrícula";
            }

            if (!dao.delete(evt)) {
                dao.rollback();
                return "Não foi possível excluir EVT";
            }

            dao.commit();

//            
            //Dao dao = new Dao();
//            db.abrirTransacao();
//            if (!db.deletarObjeto((Evt) salvarAcumuladoDB.pesquisaCodigo(idEvt, "Evt"))) {
//                db.desfazerTransacao();
//                return "Não foi possível excluir EVT";
//            }
//            db.comitarTransacao();
            return null;
        } catch (Exception e) {
            dao.rollback();
            return "No contexto da exclusão!";
        }
    }

    public boolean existeMatriculaTurma(MatriculaTurma mt) {
        try {
            Query query = getEntityManager().createQuery("SELECT MT FROM MatriculaTurma AS MT WHERE MT.turma.id = :idTurma AND MT.matriculaEscola.servicoPessoa.pessoa.id = :idAluno AND MT.matriculaEscola.escStatus.id <> 3 AND MT.matriculaEscola.servicoPessoa.ativo = TRUE");
            query.setParameter("idTurma", mt.getTurma().getId());
            query.setParameter("idAluno", mt.getMatriculaEscola().getServicoPessoa().getPessoa().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean existeVagasDisponivel(MatriculaTurma mt) {
        int quantidadeVagas = mt.getTurma().getQuantidade();
        try {
            Query query = getEntityManager().createQuery(" SELECT COUNT(MT) FROM MatriculaTurma AS MT WHERE MT.turma.id = :idTurma");
            query.setParameter("idTurma", mt.getTurma().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                Long nrVagas = (Long) query.getSingleResult();
                if (quantidadeVagas == nrVagas) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean existeMatriculaIndividual(MatriculaIndividual mi) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MI FROM MatriculaIndividual AS MI WHERE MI.curso.id = :idCurso AND MI.matriculaEscola.servicoPessoa.pessoa.id = :idAluno AND MI.dataInicio = :dataInicio AND MI.dataTermino = :dataTermino AND Mi.matriculaEscola.escStatus.id <> 3 AND MI.matriculaEscola.servicoPessoa.ativo = TRUE");
            query.setParameter("idCurso", mi.getCurso().getId());
            query.setParameter("idAluno", mi.getMatriculaEscola().getServicoPessoa().getId());
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

    public List<EscolaAutorizados> listaPessoasAutorizas(int idMatricula) {
        try {
            Query query = getEntityManager().createQuery(" SELECT EA FROM EscolaAutorizados AS EA WHERE EA.matriculaEscola.id = :matricula");
            query.setParameter("matricula", idMatricula);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List<MatriculaTurma> pesquisaMatriculaEscolaPorTurma(int idTurma) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MT FROM MatriculaTurma AS MT WHERE MT.turma.id = :idTurma");
            query.setParameter("idTurma", idTurma);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List<Servicos> listServicosPorMatriculaIndividual() {
        try {
            Query query = getEntityManager().createQuery(" SELECT MI.curso FROM MatriculaIndividual AS MI GROUP BY MI.curso ORDER BY MI.curso.descricao ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List<ServicoValor> listServicoValorPorServicoIdade(Integer id_servico, Integer idade) {
        try {
            Query query = getEntityManager().createNativeQuery(
                    "  SELECT sv.* \n "
                    + "  FROM fin_servico_valor sv \n "
                    + " WHERE sv.id_servico = " + id_servico + " \n "
                    + "   AND " + idade + " >= sv.nr_idade_ini \n"
                    + "   AND " + idade + " <= sv.nr_idade_fim", ServicoValor.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
