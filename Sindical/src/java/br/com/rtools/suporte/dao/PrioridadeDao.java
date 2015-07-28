package br.com.rtools.suporte.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.suporte.Prioridade;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class PrioridadeDao extends DB  {

    public boolean insert(Prioridade prioridade) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(prioridade);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Prioridade prioridade) {
        try {
            getEntityManager().merge(prioridade);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Prioridade prioridade) {
        try {
            getEntityManager().remove(prioridade);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Prioridade pesquisaCodigo(int id) {
        Prioridade result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Prioridade.pesquisaID");
            qry.setParameter("pid", id);
            result = (Prioridade) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pri from Prioridade pri order by pri.prioridade desc ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> pesquisaPrioridade(String des_tipo) {
        List<String> result = null;
        try {
            Query qry = getEntityManager().createQuery("select pri.prioridade from Prioridade pri where pri.prioridade like :texto");
            qry.setParameter("texto", des_tipo);
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaPrioridadeParametros(String por, String combo, String desc) {
        String textQuery = "";
        if (!desc.equals("") && !por.equals("")) {
            if (por.equals("I")) {
                desc = desc + "%";
            } else if (por.equals("P")) {
                desc = "%" + desc + "%";
            }
        } else {
            desc = "";
            return null;
        }
        if (combo.equals("")) {
            combo = "prioridade";
        }
        try {
            textQuery = "select pri from Prioridade pri where upper(pri." + combo + ") like :prioridade order by pri.prioridade";
            Query qry = getEntityManager().createQuery(textQuery);
            qry.setParameter("prioridade", desc.toLowerCase().toUpperCase());
            return (qry.getResultList());
        } catch (EJBQLException e) {
            return null;
        }
    }
}
