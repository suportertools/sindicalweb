package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CategoriaDBToplink extends DB implements CategoriaDB {

    @Override
    public boolean insert(Categoria categoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(categoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Categoria categoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(categoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Categoria categoria) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(categoria);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Categoria pesquisaCodigo(int id) {
        Categoria result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Categoria.pesquisaID");
            qry.setParameter("pid", id);
            result = (Categoria) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select c from Categoria c order by c.categoria");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaCategoriaPorGrupo(int idGrupoCategoria) {
        try {
            Query qry = getEntityManager().createQuery("select c "
                    + "  from Categoria c "
                    + " where c.grupoCategoria.id = " + idGrupoCategoria
                    + " order by c.categoria");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaCategoriaPorGrupoIds(String ids) {
        try {
            Query qry = getEntityManager().createQuery("select c "
                    + "  from Categoria c "
                    + " where c.grupoCategoria.id in (" + ids + ")"
                    + " order by c.categoria");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public GrupoCategoria pesquisaGrupoPorCategoria(int idCategoria) {
        try {
            Query qry = getEntityManager().createQuery("select gc "
                    + "  from GrupoCategoria gc "
                    + " where gc.id in (select c.grupoCategoria.id  from Categoria c where c.id = " + idCategoria + ")");
            return ((GrupoCategoria) qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<GrupoCategoria> pesquisaGrupoCategoriaOrdenada() {
        try {
            Query query = getEntityManager().createQuery("SELECT GC FROM GrupoCategoria AS GC ORDER BY GC.grupoCategoria");
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
