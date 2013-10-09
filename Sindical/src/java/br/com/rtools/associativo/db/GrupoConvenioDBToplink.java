package br.com.rtools.associativo.db;

import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class GrupoConvenioDBToplink extends DB implements GrupoConvenioDB {

    @Override
    public boolean insert(GrupoConvenio grupoConvenio) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(grupoConvenio);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(GrupoConvenio grupoConvenio) {
        try {
            getEntityManager().merge(grupoConvenio);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(GrupoConvenio grupoConvenio) {
        try {
            getEntityManager().remove(grupoConvenio);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public GrupoConvenio pesquisaCodigo(int id) {
        GrupoConvenio result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("GrupoConvenio.pesquisaID");
            qry.setParameter("pid", id);
            result = (GrupoConvenio) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select g from GrupoConvenio g ");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaGrupoConvenioPorDescricao(String descricao) {
        List lista = null;
        descricao = descricao.toUpperCase().trim();
        try {
            Query qry = getEntityManager().createQuery(
                    "select g"
                    + "  from GrupoConvenio g"
                    + " where UPPER(g.descricao) like :descricao");
            qry.setParameter("descricao", descricao);
            lista = qry.getResultList();
            if (lista == null) {
                lista = new ArrayList();
            }
            return lista;
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
