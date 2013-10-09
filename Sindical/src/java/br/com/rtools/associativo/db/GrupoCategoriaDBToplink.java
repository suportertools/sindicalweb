package br.com.rtools.associativo.db;

import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class GrupoCategoriaDBToplink extends DB implements GrupoCategoriaDB {

    public boolean insert(GrupoCategoria grupoCategoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(grupoCategoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(GrupoCategoria grupoCategoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(grupoCategoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(GrupoCategoria grupoCategoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(grupoCategoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public GrupoCategoria pesquisaCodigo(int id) {
        GrupoCategoria result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("GrupoCategoria.pesquisaID");
            qry.setParameter("pid", id);
            result = (GrupoCategoria) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select g from GrupoCategoria g order by g.grupoCategoria");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
//    public List<GrupoCategoria> listaGrupoCategoria(String ids) {
//        try{
//            Query qry = getEntityManager().createQuery("select gc from GrupoCategoria gc where id in ("+ids+") order by g.grupoCategoria");
//            return (qry.getResultList());
//        }catch(Exception e){
//            return new ArrayList();
//        }
//    }
}
