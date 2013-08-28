package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Parentesco;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ParentescoDBToplink extends DB implements ParentescoDB{
    @Override
    public boolean insert(Parentesco parentesco) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(parentesco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Parentesco parentesco) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(parentesco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Parentesco parentesco) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(parentesco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Parentesco pesquisaCodigo(int id) {
        Parentesco result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Parentesco.pesquisaID");
            qry.setParameter("pid", id);
            result = (Parentesco) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select p from Parentesco p order by p.id asc");
            return (qry.getResultList());
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public List pesquisaTodosSemTitular() {
        try{
            Query qry = getEntityManager().createQuery("select p from Parentesco p "
                                                     + " where p.ativo = true "
                                                     + "   and p.id <> 1 order by p.id");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaTodosSemTitularPorSexo(String sexo) {
        try{
            Query qry = getEntityManager().createQuery("select p from Parentesco p "
                                                     + " where p.ativo = true "
                                                     + "   and p.id <> 1 "
                                                     + "   and p.sexo = '"+sexo+"'"
                                                     + "order by p.id");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}