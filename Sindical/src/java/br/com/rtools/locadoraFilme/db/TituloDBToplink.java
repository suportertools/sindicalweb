package br.com.rtools.locadoraFilme.db;

import br.com.rtools.locadoraFilme.Genero;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class TituloDBToplink extends DB implements TituloDB {

    @Override
    public boolean insert(Titulo titulo) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(titulo);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Titulo titulo) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(titulo);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Titulo titulo) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(titulo);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Titulo pesquisaCodigo(int id) {
        Titulo result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Titulo.pesquisaID");
            qry.setParameter("pid", id);
            result = (Titulo) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select t from Titulo t ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String pesquisaTitulo(String des_titulo) {
        String result = null;
        des_titulo = "%" + des_titulo + "%";
        try {
            Query qry = getEntityManager().createQuery("select t.descricao from Titulo t where t.descricao like '" + des_titulo + "'");
            result = (String) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTitulos(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (como.equals("T")) {
            textQuery = "";
            //textQuery = "select objeto from Titulo objeto";
        } else if (como.equals("P")) {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from Titulo objeto where UPPER(objeto." + por + ") like :desc"
                    + " order by objeto.descricao";
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from Titulo objeto where UPPER(objeto." + por + ") like :desc"
                    + " order by objeto.descricao";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if ((desc != null) && (!(como.equals("T")))) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public Genero pesquisaCodigoGenero(int id) {
        Genero result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Genero.pesquisaID");
            qry.setParameter("pid", id);
            result = (Genero) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodosGenero() {
        try {
            Query qry = getEntityManager().createQuery("select g from Genero g ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String pesquisaGenero(String des_genero) {
        String result = null;
        des_genero = "%" + des_genero + "%";
        try {
            Query qry = getEntityManager().createQuery("select g.descricao from Genero g where g.descricao like '" + des_genero + "'");
            result = (String) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
