package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaAcademia;
import br.com.rtools.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class MatriculaAcademiaDBToplink extends DB implements MatriculaAcademiaDB {

    @Override
    public boolean insert(MatriculaAcademia matriculaAcademia) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(matriculaAcademia);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(MatriculaAcademia matriculaAcademia) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(matriculaAcademia);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(MatriculaAcademia matriculaAcademia) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(matriculaAcademia);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public MatriculaAcademia pesquisaCodigo(int id) {
        MatriculaAcademia result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MatriculaAcademia.pesquisaID");
            qry.setParameter("pid", id);
            result = (MatriculaAcademia) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select ma from MatriculaAcademia ma");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaMatriculaAcademia(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (por.equals("nome")) {
            por = "nome";
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select ma from MatriculaAcademia ma"
                        + " where UPPER(ma.servicoPessoa.pessoa.nome) like :desc "
                        + " order by ma.servicoPessoa.pessoa.nome";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select ma from MatriculaAcademia ma"
                        + " where UPPER(ma.servicoPessoa.pessoa.nome) like :desc "
                        + " order by ma.servicoPessoa.pessoa.nome";
            }
        }
        if (por.equals("cpf")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ma from MatriculaAcademia ma"
                    + " where UPPER(ma.servicoPessoa.pessoa.documento) like :desc "
                    + " order by ma.servicoPessoa.pessoa.documento";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }
}
