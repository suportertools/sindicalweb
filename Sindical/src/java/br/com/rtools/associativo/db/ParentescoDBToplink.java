package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Parentesco;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ParentescoDBToplink extends DB implements ParentescoDB {

    @Override
    public boolean insert(Parentesco parentesco) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(parentesco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Parentesco parentesco) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(parentesco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Parentesco parentesco) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(parentesco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Parentesco pesquisaCodigo(int id) {
        Parentesco result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Parentesco.pesquisaID");
            qry.setParameter("pid", id);
            result = (Parentesco) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Parentesco p order by p.id asc");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosSemTitular() {
        try {
            Query qry = getEntityManager().createQuery("select p from Parentesco p "
                    + " where p.ativo = true "
                    + "   and p.id <> 1 order by p.id");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public List<Parentesco> pesquisaTodosSemTitularCategoria(int id_categoria) {
        try {
            Query qry = getEntityManager().createQuery(
                            "  SELECT sc.parentesco "
                            + "  FROM ServicoCategoria sc "
                            + " WHERE sc.categoria.id = "+id_categoria+" "
                            + "   AND sc.parentesco.id <> 1 "
                            + " ORDER BY sc.parentesco.id"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList<Parentesco>();
        }
    }
    
    @Override
    public List<Parentesco> pesquisaTodosSemTitularCategoriaSemDesconto(int id_categoria, int id_categoria_desconto) {
        try {
            Query qry = getEntityManager().createQuery(
                            "  SELECT sc.parentesco "
                            + "  FROM ServicoCategoria sc "
                            + " WHERE sc.categoria.id = "+id_categoria+" "
                            + "   AND sc.parentesco.id <> 1 "
                            + "   AND sc.parentesco.id NOT IN (SELECT cdd.parentesco.id FROM CategoriaDescontoDependente cdd WHERE cdd.categoriaDesconto.id = " + id_categoria_desconto + ")"
                            + " ORDER BY sc.parentesco.id"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList<Parentesco>();
        }
    }
    
    @Override
    public List<Parentesco> pesquisaTodosSemTitularCategoriaSexo(int id_categoria, String sexo) {
        try {
            Query qry = getEntityManager().createQuery(
                      " SELECT sc.parentesco "
                    + "   FROM ServicoCategoria sc "
                    + "  WHERE sc.categoria.id = "+id_categoria+" "
                    + " AND sc.parentesco.id <> 1 "
                    + " AND sc.parentesco.sexo = '"+sexo+"' "
                    + " ORDER BY sc.parentesco.id"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList<Parentesco>();
        }
    }

//    @Override
//    public List pesquisaTodosSemTitularPorSexo(String sexo) {
//        try {
//            Query qry = getEntityManager().createQuery("select p from Parentesco p "
//                    + " where p.ativo = true "
//                    + "   and p.id <> 1 "
//                    + "   and p.sexo = '" + sexo + "'"
//                    + "order by p.id");
//            return (qry.getResultList());
//        } catch (Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
}