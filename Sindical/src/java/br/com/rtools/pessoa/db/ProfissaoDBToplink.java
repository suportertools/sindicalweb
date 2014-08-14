package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Profissao;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.SelectTranslate;
import java.util.List;
import javax.persistence.Query;

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
        SelectTranslate st = new SelectTranslate();
        desc = (por.equals("I") ? desc+"%" : "%"+desc+"%");
        return st.select(new Profissao()).where("profissao", desc).find();
    }
}
