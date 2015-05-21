package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.CategoriaDescontoDependente;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CategoriaDescontoDBToplink extends DB implements CategoriaDescontoDB {

    @Override
    public boolean insert(CategoriaDesconto categoriaDesconto) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(categoriaDesconto);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(CategoriaDesconto categoriaDesconto) {
        try {
            getEntityManager().merge(categoriaDesconto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(CategoriaDesconto categoriaDesconto) {
        try {
            getEntityManager().remove(categoriaDesconto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CategoriaDesconto pesquisaCodigo(int id) {
        CategoriaDesconto result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("CategoriaDesconto.pesquisaID");
            qry.setParameter("pid", id);
            result = (CategoriaDesconto) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select g from CategoriaDesconto g ");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List<Categoria> pesquisaCategoriasSemServico(int idServicos) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c "
                    + "  from Categoria c"
                    + " where c.id not in (select cd.categoria.id from CategoriaDesconto cd where cd.servicoValor.servicos.id = :id)");
            qry.setParameter("id", idServicos);
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaTodosPorServico(int idServicos) {
        try {
            Query qry = getEntityManager().createQuery("select cd from CategoriaDesconto cd where cd.servicoValor.servicos.id = :id order by cd.categoria.categoria, cd.servicoValor.idadeIni");
            qry.setParameter("id", idServicos);
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public CategoriaDesconto pesquisaTodosPorServicoCategoria(int idServicos, int idCategoria) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select cd "
                    + "  from CategoriaDesconto cd "
                    + " where cd.servicoValor.servicos.id = :idS"
                    + "   and cd.categoria.id = :idC");
            qry.setParameter("idS", idServicos);
            qry.setParameter("idC", idCategoria);
            return (CategoriaDesconto) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public List<CategoriaDescontoDependente> listaDescontoDependentePorCategoria(int id_categoria_desconto) {
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT cd " +
                    "   FROM CategoriaDescontoDependente cd " +
                    "  WHERE cd.categoriaDesconto.id = :id_categoria_desconto " +
                    "  ORDER BY cd.parentesco.parentesco ASC"
            );
            
            qry.setParameter("id_categoria_desconto", id_categoria_desconto);
            return qry.getResultList();
        } catch (Exception e) {
            //e.getMessage();
            return new ArrayList();
        }
    }
    
    @Override
    public CategoriaDescontoDependente pesquisaDescontoDependentePorCategoria(int id_parentesco, int id_categoria_desconto) {
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT cd " +
                    "   FROM CategoriaDescontoDependente cd " +
                    "  WHERE cd.categoriaDesconto.id = :id_categoria_desconto " +
                    "    AND cd.parentesco.id = :id_parentesco"
            );
            
            qry.setParameter("id_categoria_desconto", id_categoria_desconto);
            qry.setParameter("id_parentesco", id_parentesco);
            return (CategoriaDescontoDependente) qry.getSingleResult();
        } catch (Exception e) {
            //e.getMessage();
            return null;
        }
    }
    
    @Override
    public List<CategoriaDesconto> listaCategoriaDescontoCategoriaServicoValor(int id_categoria, int id_servico_valor) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " SELECT cd.* " +
                    "   FROM soc_categoria_desconto cd " +
                    "  WHERE cd.id_categoria = " +id_categoria +
                    "    AND cd.id_servico_valor = " + id_servico_valor, CategoriaDesconto.class
            );
            
            return qry.getResultList();
        } catch (Exception e) {
            //e.getMessage();
            return new ArrayList();
        }
    }    
    
    public List<CategoriaDesconto> listaCategoriaDescontoServicoValor(int id_servico_valor) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " SELECT cd.* " +
                    "   FROM soc_categoria_desconto cd " +
                    "  WHERE cd.id_servico_valor = " + id_servico_valor, CategoriaDesconto.class
            );
            
            return qry.getResultList();
        } catch (Exception e) {
            //e.getMessage();
            return new ArrayList();
        }
    }    
}
