package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class AcordoDBToplink extends DB implements AcordoDB {

    @Override
    public boolean insert(Acordo acordo) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(acordo);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Acordo acordo) {
        try {
            getEntityManager().merge(acordo);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Acordo acordo) {
        try {
            getEntityManager().remove(acordo);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Acordo pesquisaCodigo(int id) {
        Acordo result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Acordo.pesquisaID");
            qry.setParameter("pid", id);
            result = (Acordo) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Historico pesquisaHistorico(int id) {
        Historico result = null;
        try {
            Query qry = getEntityManager().createQuery("select h from Historico h where h.movimento.id = :pid");
            qry.setParameter("pid", id);
            result = (Historico) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Historico pesquisaHistoricoBaixado(int idContaCobranca, String nrBoleto, int idServico) {
        Historico result = null;
        try {
            Query qry = getEntityManager().createQuery("select h "
                    + "  from Historico h "
                    + " where h.movimento.contaCobranca.id = :idCB"
                    + "   and h.movimento.documento = :nrBo"
                    + "   and h.movimento.servicos.id = :idS"
                    + "   and h.movimento.tipoServico.id = 4");
            qry.setParameter("idCB", idContaCobranca);
            qry.setParameter("nrBo", nrBoleto);
            qry.setParameter("idS", idServico);
            result = (Historico) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

//    public Historico pesquisaHistoricoMov(int idContaCobranca, int idMovimento) {
//        Historico result = null;
//        try{
//            Query qry = getEntityManager().createQuery("select h " +
//                                                       "  from Historico h " +
//                                                       " where h.movimento.id = " +idMovimento +
//                                                       "   and h.movimento.contaCobranca.id = " + idContaCobranca);
//            result = (Historico) qry.getSingleResult();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return result;
//    }
    @Override
    public Historico pesquisaHistoricoMov(int idContaCobranca, int idMovimento) {
        Historico result = null;
        String textQuery = "";
        List vetor;
        List<Historico> list = new ArrayList();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        try {
            textQuery = "select h.id ids     "
                    + "  from fin_historico h "
                    + "  inner join fin_movimento mov on (mov.id = h.id_movimento) "
                    + "  inner join fin_boleto bol on (mov.nr_ctr_boleto = bol.nr_ctr_boleto) "
                    + " where mov.id = " + idMovimento
                    + "   and bol.id_conta_cobranca = " + idContaCobranca;
            Query qry = getEntityManager().createNativeQuery(textQuery);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    list.add((Historico) sv.pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0), "Historico"));
                }
            }
            if (!list.isEmpty()) {
                result = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Acordo p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodasFolhas() {
        try {
            Query qry = getEntityManager().createQuery("select p from FolhaEmpresa p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List listaHistoricoAgrupado(int id_acordo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select h.historico from Historico h where h.movimento.id in ("
                    + " select m.id from Movimento m where m.acordo.id = " + id_acordo + " and m.ativo = true"
                    + ")group by h.historico");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
