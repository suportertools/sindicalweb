package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class LoteDBToplink extends DB implements LoteDB {

    public Lote pesquisaCodigo(int id) {
        Lote result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Lote.pesquisaID");
            qry.setParameter("pid", id);
            result = (Lote) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public boolean insert(Lote lote) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(lote);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void commit() {
        getEntityManager().getTransaction().commit();
    }

    public void roolback() {
        getEntityManager().getTransaction().rollback();
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Lote p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public int ultimoCodigo() {
        int result = 0;
        try {
            Query qry = getEntityManager().createQuery("select max(p.id) from Lote p ");
            result = (Integer) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List dependentesTransferencia(int idLote) {
        List lista = null;
        try {
            Query qry = getEntityManager().createQuery("select m from Movimento m where m.lote.id =" + idLote);
            lista = qry.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }

    public List pesquisarLoteEsp(String desc, String por, String como) {
        List lista = null;
        String textQuery = null;
        if ((como.equals("T")) || (desc.equals(""))) {
            textQuery = "select lote from Lote lote where lote.rotina.id in (1,2)";
        } else if (como.equals("D")) {
            if (por.equals("q")) {
                textQuery = "select lote from Lote lote where lote.qtde = " + desc + " and lote.rotina.id in (1,2)";
            } else if (por.equals("t")) {
                textQuery = "select lote from Lote lote where lote.total = " + desc + " and lote.rotina.id in (1,2)";
            } else if (por.equals("d")) {
                textQuery = "select lote from Lote lote where lote.data = :desc and lote.rotina.id in (1,2)";
            }
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if ((por.equals("d")) && (!(como.equals("T")))) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = null;
        }
        return lista;
    }

    @Override
    public List pesquisarLoteTransferência(String desc, String por, String como) {
        List lista = null;
        String textQuery = null;
        if ((como.equals("T")) || (desc.equals(""))) {
            textQuery = "select lote from Lote lote where lote.rotina.id = 3";
        } else if (como.equals("D")) {
            if (por.equals("q")) {
                textQuery = "select lote from Lote lote where lote.qtde = " + desc + " and lote.rotina.id = 3";
            } else if (por.equals("t")) {
                textQuery = "select lote from Lote lote where lote.total = " + desc + " and lote.rotina.id = 3";
            } else if (por.equals("d")) {
                textQuery = "select lote from Lote lote where lote.data = :desc and lote.rotina.id = 3";
            }
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if ((por.equals("d")) && (!(como.equals("T")))) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = null;
        }
        return lista;
    }

    public boolean delete(Lote lote) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(lote);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Lote pesquisaLotePorEvt(Evt evt) {
        try {
            Query query = getEntityManager().createQuery(" SELECT L FROM Lote AS L WHERE L.evt.id = :idEvt");
            query.setParameter("idEvt", evt.getId());
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Lote) query.getSingleResult();
            }
        } catch (Exception e) {
            return new Lote();
        }
        return new Lote();

    }

    @Override
    public Lote pesquisaLotePorEvt(int evt) {
        return pesquisaLotePorEvt(new Evt(evt));
    }

    @Override
    public List<Lote> pesquisaLotesPorEvt(Evt evt) {
        try {
            Query query = getEntityManager().createQuery(" SELECT L FROM Lote AS L WHERE L.evt.id = :idEvt");
            query.setParameter("idEvt", evt.getId());
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
    public List<Lote> pesquisaLotesPorEvt(int evt) {
        return pesquisaLotesPorEvt(new Evt(evt));
    }
}
