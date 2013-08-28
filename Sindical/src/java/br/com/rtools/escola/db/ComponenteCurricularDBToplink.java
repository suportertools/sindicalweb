package br.com.rtools.escola.db;

import br.com.rtools.escola.ComponenteCurricular;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;


public class ComponenteCurricularDBToplink extends DB implements ComponenteCurricularDB{
    public boolean insert(ComponenteCurricular componenteCurricular) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(componenteCurricular);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(ComponenteCurricular componenteCurricular) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(componenteCurricular);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(ComponenteCurricular componenteCurricular) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(componenteCurricular);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public ComponenteCurricular pesquisaCodigo(int id) {
        ComponenteCurricular result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("ComponenteCurricular.pesquisaID");
            qry.setParameter("pid", id);
            result = (ComponenteCurricular) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select v from ComponenteCurricular v");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
    
    public ComponenteCurricular idComponenteCurricular(ComponenteCurricular des_ComponenteCurricular){
        ComponenteCurricular result = null;
        try{
           Query qry = getEntityManager().createQuery("select c from ComponenteCurricular c where c.descricao = :descricao");
           qry.setParameter("descricao", des_ComponenteCurricular.getDescricao());
           result = (ComponenteCurricular) qry.getSingleResult();
        }
        catch(Exception e){
           e.getMessage();
        }
        return result;
    }


}
