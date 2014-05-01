package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.TipoPagamento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FTipoDocumentoDBToplink extends DB implements FTipoDocumentoDB {

    @Override
    public FTipoDocumento pesquisaCodigo(int id) {
        FTipoDocumento result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("FTipoDocumento.pesquisaID");
            qry.setParameter("pid", id);
            result = (FTipoDocumento) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from FTipoDocumento p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosStringsPartida(String desc) {
        try {
            Query qry = getEntityManager().createQuery("select p.tipoDocumento "
                    + "   from FTipoDocumento p "
                    + "  where p.id in " + desc);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosStrings(String desc) {
        try {
            Query qry = getEntityManager().createQuery("select p"
                    + "   from FTipoDocumento p "
                    + "  where p.id in " + desc);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosStringsCPartida() {
        try {
            Query qry = getEntityManager().createQuery("select p.tipoDocumento "
                    + "   from FTipoDocumento p "
                    + "  where p.id in (1 , 2)");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaListaTipoExtrato() {
        try {
            Query qry = getEntityManager().createQuery("select p"
                    + "   from FTipoDocumento p "
                    + "  where p.id in (13,14,15,16,17,18,19,20,21,22,23)");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public FTipoDocumento pesquisaTipoDocPorDesc(String desc) {
        FTipoDocumento result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select doc"
                    + "  from FTipoDocumento doc"
                    + " where doc.tipoDocumento like :desc");
            qry.setParameter("desc", desc);
            result = (FTipoDocumento) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<TipoPagamento> pesquisaTodosTipoPagamento() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select tp"
                    + "  from TipoPagamento tp"
                    + " order by tp.id");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public TipoPagamento pesquisaCodigoTipoPagamento(int id) {
        TipoPagamento result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("TipoPagamento.pesquisaID");
            qry.setParameter("pid", id);
            result = (TipoPagamento) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<TipoPagamento> pesquisaCodigoTipoPagamentoIDS(String ids) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select tp"
                    + "  from TipoPagamento tp "
                    + " where tp.id in (" + ids + ")"
                    + " order by tp.id");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
    
    @Override
    public List<FTipoDocumento> pesquisaCodigoTipoDocumentoIDS(String ids) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "  select tp"
                    + "  from FTipoDocumento tp "
                    + " where tp.id in (" + ids + ")"
                    + " order by tp.id");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
}