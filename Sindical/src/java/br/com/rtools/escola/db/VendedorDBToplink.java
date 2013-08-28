package br.com.rtools.escola.db;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;


public class VendedorDBToplink extends DB implements VendedorDB{
    public boolean insert(Vendedor vendedor) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(vendedor);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Vendedor vendedor) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(vendedor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Vendedor vendedor) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(vendedor);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Vendedor pesquisaCodigo(int id) {
        Vendedor result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Vendedor.pesquisaID");
            qry.setParameter("pid", id);
            result = (Vendedor) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select v from Vendedor v");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
}
