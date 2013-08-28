
package br.com.rtools.seguranca.db;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Rotina;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;



public class RotinaDBToplink extends DB implements RotinaDB {

    @Override
    public boolean insert(Rotina rotina) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(rotina);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Rotina rotina) {
        try{
        getEntityManager().merge(rotina);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(Rotina rotina) {
        try{
        getEntityManager().remove(rotina);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }

    }

    @Override
    public Rotina pesquisaCodigo(int id) {
        Rotina result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Rotina.pesquisaID");
            qry.setParameter("pid", id);
            result = (Rotina) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaTodosOrdenado() {
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot order by rot.rotina asc ");
            return (qry.getResultList());
        }
        
        catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List pesquisaTodosOrdenadoAtivo() {
        try{
            Query qry = getEntityManager().createQuery(" SELECT rot FROM Rotina rot WHERE rot.ativo = true ORDER BY rot.rotina ASC ");
            return (qry.getResultList());
        }
        
        catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List pesquisaTodosSimples() {
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot where rot.classe is not null order by rot.rotina asc ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return new ArrayList();
        }
    }

    @Override
    public Rotina idRotina(Rotina des_rotina){
        Rotina result = null;
        String descricao = des_rotina.getRotina().toLowerCase().toUpperCase();
        try{
           Query qry = getEntityManager().createQuery("select rot from Rotina rot where UPPER(rot.rotina) = :d_rotina");
           qry.setParameter("d_rotina", descricao);
           result = (Rotina) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Rotina pesquisaPaginaRotina(String pagina) {
        Rotina rotina = new Rotina();
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot where rot.pagina like '%"+pagina+"%'");
            rotina = (Rotina)qry.getSingleResult();
        }
        catch(Exception e){
        }
        return rotina;
    }
    
    @Override
    public Rotina pesquisaAcesso(String pagina) {
        Rotina rotina = new Rotina();
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot where rot.pagina = '"+pagina+"'");
            if (!qry.getResultList().isEmpty()) {
                rotina = (Rotina) qry.getSingleResult();
            }
        }
        catch(Exception e){
        }
        return rotina;
    }

    @Override
    public List<Rotina> pesquisaRotina(String rotina) {
        List<Rotina> lista = new ArrayList();
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot where rot.rotina like '%"+rotina+"%'");
            lista = qry.getResultList();
        }
        catch(Exception e){
        }
        return lista;
    }
    
    @Override
    public List<Rotina> pesquisaAcessosOrdem(){
        List<Rotina> lista = new ArrayList();
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot where rot.acessos > 0 order by rot.acessos desc");
            lista = qry.getResultList();
        }
        catch(Exception e){
        }
        return lista;
    }
    
}
