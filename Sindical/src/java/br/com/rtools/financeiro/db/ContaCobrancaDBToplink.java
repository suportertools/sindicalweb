package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Layout;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ContaCobrancaDBToplink extends DB implements ContaCobrancaDB {

    @Override
    public boolean insert(ContaCobranca contaCobranca) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(contaCobranca);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(ContaCobranca contaCobranca) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(contaCobranca);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(ContaCobranca contaCobranca) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(contaCobranca);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public ContaCobranca pesquisaCodigo(int id) {
        ContaCobranca result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ContaCobranca.pesquisaID");
            qry.setParameter("pid", id);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select con from ContaCobranca con ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaLayouts() {
        try {
            Query qry = getEntityManager().createQuery("select l from Layout l");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Layout pesquisaLayoutId(int id) {
        Layout result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Layout.pesquisaID");
            qry.setParameter("pid", id);
            result = (Layout) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public ContaCobranca idContaCobranca(ContaCobranca des_contaCobranca) {
        ContaCobranca result = null;
        String descricao = des_contaCobranca.getCedente().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select ced from ContaCobranca ced where UPPER(ced.contaCobranca) = :d_contaCobranca");
            qry.setParameter("d_contaCobranca", descricao);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public ContaCobranca pesquisaServicoCobranca(int idServicos, int idTipoServico) {
        ContaCobranca result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.contaCobranca "
                    + "  from ServicoContaCobranca c"
                    + " where c.tipoServico.id = :idTipo"
                    + "   and c.servicos.id = :idServicos");
            qry.setParameter("idTipo", idTipoServico);
            qry.setParameter("idServicos", idServicos);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public ContaCobranca pesquisaCobrancaCedente(String codCedente) {
        ContaCobranca result = new ContaCobranca();
        try {
            Query qry = getEntityManager().createQuery(
                    "select c "
                    + "  from ContaCobranca c"
                    + " where c.codCedente = :codCedente");
            qry.setParameter("codCedente", codCedente);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
