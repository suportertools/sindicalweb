package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.FolhaEmpresa;
import br.com.rtools.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class FolhaEmpresaDBToplink extends DB implements FolhaEmpresaDB {

    @Override
    public boolean insert(FolhaEmpresa folhaEmpresa) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(folhaEmpresa);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(FolhaEmpresa folhaEmpresa) {
        try {
            getEntityManager().merge(folhaEmpresa);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(FolhaEmpresa folhaEmpresa) {
        try {
            getEntityManager().remove(folhaEmpresa);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FolhaEmpresa pesquisaCodigo(int id) {
        FolhaEmpresa result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("FolhaEmpresa.pesquisaID");
            qry.setParameter("pid", id);
            result = (FolhaEmpresa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select de from FolhaEmpresa de ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public FolhaEmpresa pesquisaPorPessoa(int idPessoa, int idTipoServico, String referencia) {
        FolhaEmpresa result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select f"
                    + "  from FolhaEmpresa f "
                    + " where f.referencia = :r"
                    + "   and f.tipoServico.id = :t"
                    + "   and f.juridica.pessoa.id = :p");
            qry.setParameter("p", idPessoa);
            qry.setParameter("t", idTipoServico);
            qry.setParameter("r", referencia);
            result = (FolhaEmpresa) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
