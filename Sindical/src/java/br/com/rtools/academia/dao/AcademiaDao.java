package br.com.rtools.academia.dao;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.associativo.MatriculaAcademia;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class AcademiaDao extends DB {

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

    public AcademiaServicoValor existeAcademiaServicoValor(AcademiaServicoValor asv) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servico AND ASV.periodo.id = :periodo");
            query.setParameter("servico", asv.getServicos().getId());
            query.setParameter("periodo", asv.getPeriodo().getId());

            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AcademiaServicoValor) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List<AcademiaSemana> existeAcademiaSemana(int id_grade, int id_semana, int id_servico, int id_periodo) {
        try {
            //Query query = getEntityManager().createQuery("SELECT s FROM AcademiaSemana s WHERE s.academiaGrade.id = :id_grade AND s.semana = :id_semana AND s.academiaServicoValor.id = :id_servico_valor");
            Query query = getEntityManager().createQuery("SELECT s FROM AcademiaSemana s WHERE s.academiaGrade.id = :id_grade AND s.semana.id = :id_semana AND s.academiaServicoValor.periodo.id = :id_periodo AND s.academiaServicoValor.servicos.id = :id_servico");
            query.setParameter("id_grade", id_grade);
            query.setParameter("id_semana", id_semana);
            query.setParameter("id_servico", id_servico);
            query.setParameter("id_periodo", id_periodo);

            List list = query.getResultList();
            if (!list.isEmpty()) {
                return query.getResultList();
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List<AcademiaServicoValor> listaAcademiaServicoValor(int idServico) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servicos ORDER BY ASV.periodo.descricao ASC, ASV.servicos.descricao ASC");
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

    public List<AcademiaServicoValor> listaServicoValorPorRotina() {
        try {
            Query query = getEntityManager().createQuery(" SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id IN(SELECT S.servicos.id FROM ServicoRotina AS S WHERE S.rotina.id = 122) AND ASV.periodo.id IN(SELECT PER.id FROM Periodo AS PER) ORDER BY ASV.servicos.descricao ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List<AcademiaServicoValor> listaAcademiaServicoValorPorServico(int idServico) {
        try {
            Query query = getEntityManager().createQuery(" SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servicos ORDER BY ASV.servicos.descricao ASC, ASV.periodo.dias ASC");
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

    public List<AcademiaSemana> listaAcademiaSemana(int id_servico_valor) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASE FROM AcademiaSemana AS ASE WHERE ASE.academiaServicoValor.id = :servicoValor ORDER BY ASE.semana.id");
            query.setParameter("servicoValor", id_servico_valor);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List<AcademiaSemana> listaAcademiaSemana() {
        try {
            Query query = getEntityManager().createQuery("SELECT ASE FROM AcademiaSemana AS ASE");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

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

    public boolean desfazerMovimento(MatriculaAcademia ma) {
        LoteDB loteDB = new LoteDBToplink();
        Lote lote = (Lote) loteDB.pesquisaLotePorEvt(ma.getEvt());
        if (lote == null) {
            return false;
        }
        if (lote.getId() == -1) {
            return false;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        try {
            Query queryMovimentos = getEntityManager().createQuery("SELECT M FROM Movimento AS M WHERE M.lote.evt.id = " + ma.getEvt().getId());
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
            int idEvt = ma.getEvt().getId();
            ma.setEvt(null);
            if (!salvarAcumuladoDB.alterarObjeto(ma)) {
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

    public List<MatriculaAcademia> pesquisaMatriculaAcademia(String tipo, String por, String como, String descricao, boolean ativo, int servicos) {
        String queryString = "";

        if (por.equals("cpf")) {
            if (como.equals("I")) {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.documento) LIKE '" + descricao.toUpperCase() + "%'";
            } else {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.documento) LIKE '%" + descricao.toUpperCase() + "%'";
            }
        } else {
            if (como.equals("I")) {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.nome) LIKE '" + descricao.toUpperCase() + "%'";
            } else {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.nome) LIKE '%" + descricao.toUpperCase() + "%'";
            }
        }
        if (!queryString.isEmpty()) {
            queryString += " AND ";
        }
        queryString += " MATR.servicoPessoa.ativo = " + ativo + " ";
        if (servicos > 0) {
            if (!queryString.isEmpty()) {
                queryString += " AND ";
            }
            queryString += " MATR.servicoPessoa.servicos.id = " + servicos + " ";
        }
        try {
            Query query = getEntityManager().createQuery("SELECT MATR FROM MatriculaAcademia AS MATR WHERE " + queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public boolean existeAlunoModalidade(int aluno, int modalidade, Date emissao) {
        try {
            Query query = getEntityManager().createQuery("SELECT MA FROM MatriculaAcademia AS MA WHERE MA.servicoPessoa.pessoa.id = :aluno AND MA.academiaServicoValor.servicos.id = :modalidade AND MA.dtInativo IS NULL AND (MA.dtValidade IS NULL OR MA.dtValidade > CURRENT_TIMESTAMP)");
            query.setMaxResults(1);
            query.setParameter("aluno", aluno);
            query.setParameter("modalidade", modalidade);
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }
}
