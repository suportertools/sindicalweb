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
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaEscolaDao extends DB {

//    public List<MatriculaEscola> pesquisaMatriculaEscola(String tipoMatricula, String descricaoCurso, String descricao, String comoPesquisa, String porPesquisa, int filtroStatus, Filial filial) {
//        try {
//            List<MatriculaEscola> list;
//            String complementoQuery = "";
//            Query query;
//            String queryString = "";
//            if (tipoMatricula.equals("Individual")) {
//                if (!descricaoCurso.equals("")) {
//                    complementoQuery = " AND UPPER(mi.curso.descricao) LIKE :descricaoCurso ";
//                }
//                if (filial != null) {
//                    if (filial.getId() != -1) {
//                        queryString = " mi.matriculaEscola.filial.id = " + filial.getId() + " AND ";
//                    }
//                }
//                if (filtroStatus != 0 && filtroStatus != 5) {
//                    queryString += " mi.matriculaEscola.escStatus.id = " + filtroStatus + " AND ";
//                }
//                query = getEntityManager().createQuery(" SELECT mi.matriculaEscola FROM MatriculaIndividual mi WHERE " + queryString + " UPPER(mi.matriculaEscola." + porPesquisa + ".nome) LIKE :descricaoAluno AND mi.matriculaEscola.habilitado = true " + complementoQuery);
//            } else {
//                if (!descricaoCurso.equals("")) {
//                    complementoQuery = " AND UPPER(mt.turma.cursos.descricao) LIKE :descricaoCurso ";
//                }
//                if (filial != null) {
//                    if (filial.getId() != -1) {
//                        queryString = " mt.matriculaEscola.filial.id = " + filial.getId() + " AND ";
//                    }
//                }
//                if (filtroStatus != 0 && filtroStatus != 5) {
//                    queryString += " mt.matriculaEscola.escStatus.id = " + filtroStatus + " AND ";
//                }
//                query = getEntityManager().createQuery(" SELECT mt.matriculaEscola FROM MatriculaTurma mt WHERE " + queryString + " UPPER(mt.matriculaEscola." + porPesquisa + ".nome) LIKE :descricaoAluno AND mt.matriculaEscola.habilitado = true " + complementoQuery);
//            }
//            if (comoPesquisa.equals("Inicial")) {
//                query.setParameter("descricaoAluno", "" + descricao.toUpperCase() + "%");
//                if (!descricaoCurso.equals("")) {
//                    query.setParameter("descricaoCurso", "" + descricaoCurso.toUpperCase() + "%");
//                }
//                if (!descricaoCurso.equals("")) {
//                    query.setParameter("descricaoCurso", "" + descricaoCurso.toUpperCase() + "%");
//                }
//            } else if (comoPesquisa.equals("Parcial")) {
//                query.setParameter("descricaoAluno", "%" + descricao.toUpperCase() + "%");
//                if (!descricaoCurso.equals("")) {
//                    query.setParameter("descricaoCurso", "%" + descricaoCurso.toUpperCase() + "%");
//                }
//            }
//            list = query.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return new ArrayList();
//    }
    public List<MatriculaEscola> pesquisaMatriculaEscola(String tipoMatricula, String descricaoCurso, String descricao, String comoPesquisa, String porPesquisa, int filtroStatus, Filial filial) {
        try {
            String text = "", inner = "", filter = "";
            
            text =  "SELECT me.* \n " +
                    "  FROM matr_escola me \n " +
                    " INNER JOIN fin_servico_pessoa sp ON sp.id = me.id_servico_pessoa \n " +
                    " INNER JOIN fin_servicos s ON s.id = sp.id_servico \n " + 
                    " INNER JOIN esc_status es ON es.id = me.id_status \n ";
            if (filtroStatus != 0 && filtroStatus != 5) {
                filter += " WHERE es.id = " + filtroStatus + " \n ";
            }else{
                filter += " WHERE es.id IN (SELECT id FROM esc_status) \n ";
            }
            
            if (tipoMatricula.equals("Individual")) {
                inner +=  " INNER JOIN esc_matr_individual mi ON mi.id_matr_escola = me.id \n ";
            }else{
                inner +=  " INNER JOIN esc_matr_turma mt ON mt.id_matr_escola = me.id \n ";
            }
            
            if (porPesquisa.equals("aluno")) {
                inner +=  " INNER JOIN pes_pessoa p ON p.id = sp.id_pessoa \n ";
            }else{
                inner +=  " INNER JOIN pes_pessoa p ON p.id = sp.id_cobranca \n ";
            }
            
            
            if (!descricaoCurso.isEmpty()){
                descricaoCurso = AnaliseString.normalizeLower(descricaoCurso);
                descricaoCurso = (comoPesquisa.equals("Inicial") ? descricaoCurso+"%" : "%"+descricaoCurso+"%");                
                filter += " AND LOWER(FUNC_TRANSLATE(s.ds_descricao)) LIKE '"+descricaoCurso+"' \n ";
            }
            
            if (!descricao.isEmpty()){
                descricao = AnaliseString.normalizeLower(descricao);
                descricao = (comoPesquisa.equals("Inicial") ? descricao+"%" : "%"+descricao+"%");
                filter += " AND LOWER(FUNC_TRANSLATE(p.ds_nome)) LIKE '"+descricao+"' \n ";
            }
            
            if (filial != null){
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
            for (Movimento listMovimento : listMovimentos) {
                if (!salvarAcumuladoDB.deletarObjeto((Movimento) salvarAcumuladoDB.pesquisaCodigo(listMovimento.getId(), "Movimento"))) {
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

    public boolean existeMatriculaTurma(MatriculaTurma mt) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MT FROM MatriculaTurma AS MT WHERE MT.turma.id = :idTurma AND MT.matriculaEscola.aluno.id = :idAluno");
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
            Query query = getEntityManager().createQuery(" SELECT MI FROM MatriculaIndividual AS MI WHERE MI.curso.id = :idCurso AND MI.matriculaEscola.aluno.id = :idAluno AND MI.dataInicio = :dataInicio AND MI.dataTermino = :dataTermino");
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
}
