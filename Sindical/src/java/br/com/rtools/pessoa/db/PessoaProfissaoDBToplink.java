package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.PessoaProfissao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class PessoaProfissaoDBToplink extends DB implements PessoaProfissaoDB {

    @Override
    public boolean insert(PessoaProfissao pessoaProfissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(pessoaProfissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(PessoaProfissao pessoaProfissao) {
        try {
            getEntityManager().merge(pessoaProfissao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(PessoaProfissao pessoaProfissao) {
        try {
            getEntityManager().remove(pessoaProfissao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PessoaProfissao pesquisaCodigo(int id) {
        PessoaProfissao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("PessoaProfissao.pesquisaID");
            qry.setParameter("pid", id);
            result = (PessoaProfissao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pesp from PessoaProfissao pesp ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PessoaProfissao pesquisaProfPorFisica(int id) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select pf"
                    + "  from PessoaProfissao pf"
                    + " where pf.fisica.id = :pid");
            qry.setParameter("pid", id);
            return (PessoaProfissao) qry.getSingleResult();
        } catch (Exception e) {
            return new PessoaProfissao();
        }
    }
}
