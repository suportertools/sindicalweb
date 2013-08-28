

package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;



public class CategoriaDescontoDBToplink extends DB implements CategoriaDescontoDB {

    @Override
    public boolean insert(CategoriaDesconto categoriaDesconto) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(categoriaDesconto);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(CategoriaDesconto categoriaDesconto) {
        try{
            getEntityManager().merge(categoriaDesconto);
            getEntityManager().flush();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(CategoriaDesconto categoriaDesconto) {
        try{
            getEntityManager().remove(categoriaDesconto);
            getEntityManager().flush();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public CategoriaDesconto pesquisaCodigo(int id) {
        CategoriaDesconto result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("CategoriaDesconto.pesquisaID");
            qry.setParameter("pid", id);
            result = (CategoriaDesconto) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select g from CategoriaDesconto g ");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public List<Categoria> pesquisaCategoriasSemServico(int idServicos) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select c " +
                    "  from Categoria c" +
                    " where c.id not in (select cd.categoria.id from CategoriaDesconto cd where cd.servicos.id = :id)");
            qry.setParameter("id", idServicos);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaTodosPorServico(int idServicos) {
        try{
            Query qry = getEntityManager().createQuery("select cd from CategoriaDesconto cd where cd.servicos.id = :id");
            qry.setParameter("id", idServicos);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public CategoriaDesconto pesquisaTodosPorServicoCategoria(int idServicos, int idCategoria) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select cd " +
                    "  from CategoriaDesconto cd " +
                    " where cd.servicos.id = :idS" +
                    "   and cd.categoria.id = :idC");
            qry.setParameter("idS", idServicos);
            qry.setParameter("idC", idCategoria);
            return (CategoriaDesconto) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }



}
