package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Profissao;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ProfissaoDBToplink extends DB implements ProfissaoDB {

    @Override
    public boolean insert(Profissao profissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(profissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Profissao profissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(profissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Profissao profissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(profissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Profissao pesquisaCodigo(int id) {
        Profissao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Profissao.pesquisaID");
            qry.setParameter("pid", id);
            result = (Profissao) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pro from Profissao pro order by pro.profissao");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> pesquisaProfissao(String des_tipo) {
        List<String> result = null;
        try {
            Query qry = getEntityManager().createQuery("select prof.profissao from Profissao prof where prof.profissao like :texto");
            qry.setParameter("texto", des_tipo);
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public Profissao idProfissao(Profissao des_prof) {
        Profissao result = null;
        try {
            Query qry = getEntityManager().createQuery("select prof from Profissao prof where prof.profissao = :d_prof");
            qry.setParameter("d_prof", des_prof.getProfissao());
            result = (Profissao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaProfParametros(String por, String combo, String desc) {
        String textQuery = "";
        if (!desc.equals("") && !por.equals("")) {
            if (por.equals("I")) {
                desc = desc + "%";
            } else if (por.equals("P")) {
                desc = "%" + desc + "%";
            }
        } else {
            desc = "";
            return new ArrayList();
        }
        if (combo.equals("")) {
            combo = "profissao";
        }
        try {
            textQuery = "select prof from Profissao prof where upper(prof." + combo + ") like :profissao order by prof.profissao";
            Query qry = getEntityManager().createQuery(textQuery);
            qry.setParameter("profissao", desc.toLowerCase().toUpperCase());
            return (qry.getResultList());
        } catch (EJBQLException e) {
            return new ArrayList();
        }
    }
}
