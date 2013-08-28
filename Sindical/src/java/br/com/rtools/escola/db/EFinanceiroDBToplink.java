package br.com.rtools.escola.db;

import br.com.rtools.escola.EFinanceiro;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EFinanceiroDBToplink extends DB implements EFinanceiroDB{
    public boolean insert(EFinanceiro eFinanceiro) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(eFinanceiro);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(EFinanceiro eFinanceiro) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(eFinanceiro);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(EFinanceiro eFinanceiro) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(eFinanceiro);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public EFinanceiro pesquisaCodigo(int id) {
        EFinanceiro result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("EFinanceiro.pesquisaID");
            qry.setParameter("pid", id);
            result = (EFinanceiro) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select ef from EFinanceiro ef");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
}
