package br.com.rtools.locadoraFilme.db;

import br.com.rtools.locadoraFilme.Catalogo;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class CatalogoDBToplink extends DB implements CatalogoDB {

    public boolean insert(Catalogo catalogo) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(catalogo);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Catalogo catalogo) {
        try{
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(catalogo);
        getEntityManager().flush();
        getEntityManager().getTransaction().commit();
        return true;
        }
        catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Catalogo catalogo) {
        try{
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(catalogo);
        getEntityManager().flush();
        getEntityManager().getTransaction().commit();
        return true;
        }
        catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Catalogo pesquisaCodigo(int id) {
        Catalogo result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Catalogo.pesquisaID");
            qry.setParameter("pid", id);
            result = (Catalogo) qry.getSingleResult();
        }
        catch(Exception e){
           e.printStackTrace();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select c from Catalogo c ");
            return (qry.getResultList());
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean verificaFilial(Filial filial, Titulo titulo){
        boolean result = true;
        try{
            Query qry = getEntityManager().createQuery( "    SELECT c                                   "
                                                      + "      FROM Catalogo c                          "
                                                      + "     WHERE c.filial.id = "+filial.getId()+"    "
                                                      + "       AND c.titulo.id = "+titulo.getId()+"    ");
            Object o = qry.getSingleResult();
            if(o == null){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
           e.getMessage();
        }
        return result;
    }

    public List<Catalogo> pesquisaCatalogo (Filial filial, Titulo titulo){
        List<Catalogo> result = null;
        try{
            Query qry = getEntityManager().createQuery( "    SELECT c                                   "
                                                      + "      FROM Catalogo c                          "
                                                      + "     WHERE c.filial.id = "+filial.getId()+"    "
                                                      + "       AND c.titulo.id = "+titulo.getId()+"    "
                                                      + "  ORDER BY c.titulo.descricao                  ");
            result = qry.getResultList();

        }catch(Exception e){
           e.getMessage();
           result = null;
        }
        return result;
    }

    public List<Catalogo> pesquisaCatalogo (Filial filial){
        List<Catalogo> result = null;
        try{
            Query qry = getEntityManager().createQuery( "    SELECT c                                   "
                                                      + "      FROM Catalogo c                          "
                                                      + "     WHERE c.filial.id = "+filial.getId()+"    "
                                                      + "  ORDER BY c.titulo.descricao                  ");
            result = qry.getResultList();

        }catch(Exception e){
           e.getMessage();
           result = null;
        }
        return result;
    }

    public Catalogo pesquisaMatriz (int idTitulo){
        Catalogo result = null;
        try{
            Query qry = getEntityManager().createQuery( "    SELECT c                         "
                                                      + "      FROM Catalogo c,               "
                                                      + "           Filial f                  "
                                                      + "     WHERE c.filial.id = f.id        "
                                                      + "       and f.filial.id = f.matriz.id "
                                                      + "       and c.titulo.id = " + idTitulo);
            result = (Catalogo) qry.getSingleResult();
        }catch(Exception e){
           e.getMessage();
           result = null;
        }
        return result;
    }
}