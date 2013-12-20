package br.com.rtools.academia.db;

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
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AcademiaDBToplink extends DB implements AcademiaDB {

    @Override
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

    @Override
    public AcademiaServicoValor existeAcademiaServicoValor(AcademiaServicoValor asv) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servico AND ASV.periodo.id = :periodo AND ASV.academiaGrade.id = :grade");
            query.setParameter("servico", asv.getServicos().getId());
            query.setParameter("periodo", asv.getPeriodo().getId());
            query.setParameter("grade", asv.getAcademiaGrade().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AcademiaServicoValor) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<AcademiaServicoValor> listaAcademiaServicoValor(int idServico) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servicos ORDER BY ASV.periodo.descricao ASC, ASV.servicos.descricao ASC, ASV.academiaGrade.horaInicio ASC");
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

    @Override
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

    @Override
    public List<AcademiaServicoValor> listaAcademiaServicoValorPorServico(int idServico) {
        try {
            Query query = getEntityManager().createQuery(" SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.servicos.id = :servicos ORDER BY ASV.academiaGrade.horaInicio ASC, ASV.academiaGrade.horaFim ASC, ASV.periodo.id ASC");
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

    @Override
    public List<AcademiaSemana> listaAcademiaSemana(int idAcademiaGrade) {
        try {
            Query query = getEntityManager().createQuery("SELECT ASE FROM AcademiaSemana AS ASE WHERE ASE.academiaGrade.id = :academiaGrade");
            query.setParameter("academiaGrade", idAcademiaGrade);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<MatriculaAcademia> pesquisaMatriculaAcademia(String tipo, String por, String como, String descricao) {
        String queryString = "";

        if (por.equals("cpf")) {
            if (como.equals("I")) {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.documento) LIKE '"+descricao.toUpperCase()+"%'";
            } else {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.documento) LIKE '%"+descricao.toUpperCase()+"%'";
            }
        } else {
            if (como.equals("I")) {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.nome) LIKE '"+descricao.toUpperCase()+"%'";
            } else {
                queryString = " UPPER(MATR.servicoPessoa.pessoa.nome) LIKE '%"+descricao.toUpperCase()+"%'";
            }
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
}
