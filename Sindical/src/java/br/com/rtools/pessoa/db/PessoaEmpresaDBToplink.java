package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class PessoaEmpresaDBToplink extends DB implements PessoaEmpresaDB {

    @Override
    public boolean insert(PessoaEmpresa pessoaEmpresa) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(pessoaEmpresa);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(PessoaEmpresa pessoaEmpresa) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(pessoaEmpresa);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(PessoaEmpresa pessoaEmpresa) {
        try {
            getEntityManager().remove(pessoaEmpresa);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PessoaEmpresa pesquisaCodigo(int id) {
        PessoaEmpresa result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("PessoaEmpresa.pesquisaID");
            qry.setParameter("pid", id);
            result = (PessoaEmpresa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pes from PessoaEmpresa pes");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List listaPessoaEmpresaPorFisica(int id) {
        List list = null;
        try {
            Query qry = getEntityManager().createQuery(" SELECT pesEmp FROM PessoaEmpresa pesEmp WHERE pesEmp.fisica.id = :id AND pesEmp.dtDemissao IS NOT NULL ");
            qry.setParameter("id", id);
            list = qry.getResultList();
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List listaPessoaEmpresaTodos(int id) {
        try {
            Query qry = getEntityManager().createQuery("select pesEmp "
                    + "  from PessoaEmpresa pesEmp"
                    + " where pesEmp.fisica.id = " + id);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PessoaEmpresa pesquisaPessoaEmpresaPorFisica(int id) {
        PessoaEmpresa pesEmp = new PessoaEmpresa();
        try {
            Query qry = getEntityManager().createQuery("select pesEmp "
                    + "  from PessoaEmpresa pesEmp"
                    + " where pesEmp.fisica.id = " + id
                    + "   and pesEmp.dtDemissao is null");
            pesEmp = ((PessoaEmpresa) qry.getSingleResult());
            return pesEmp;
        } catch (Exception e) {
            return pesEmp;
        }
    }

    @Override
    public PessoaEmpresa pesquisaPessoaEmpresaPorPessoa(int idPessoa) {
        try {
            Query query = getEntityManager().createQuery(
                    "  SELECT PE                                    "
                    + "  FROM PessoaEmpresa AS PE                   "
                    + " WHERE PE.fisica.pessoa.id = " + idPessoa      
                    + "   AND PE.dtDemissao IS NULL                 ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (PessoaEmpresa) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return new PessoaEmpresa();
    }
}