package br.com.rtools.associativo.db;

import br.com.rtools.principal.DB;
import br.com.rtools.associativo.SubGrupoConvenio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class SubGrupoConvenioDBToplink extends DB implements SubGrupoConvenioDB {

    public boolean insert(SubGrupoConvenio subGrupoConvenio) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(subGrupoConvenio);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(SubGrupoConvenio subGrupoConvenio) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(subGrupoConvenio);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(SubGrupoConvenio subGrupoConvenio) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(subGrupoConvenio);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public SubGrupoConvenio pesquisaCodigo(int id) {
        SubGrupoConvenio result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("SubGrupoConvenio.pesquisaID");
            qry.setParameter("pid", id);
            result = (SubGrupoConvenio) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select g from SubGrupoConvenio g ");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public List pesquisaSubGrupoConvenioPorDescricao(String descricao) {
        List lista = null;
        descricao = descricao.toUpperCase().trim();
        try {
            Query qry = getEntityManager().createQuery(
                    "select g"
                    + "  from SubGrupoConvenio g"
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

    public List pesquisaSubGrupoConvênioPorGrupo(int idGrupoConvenio) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select g"
                    + "  from SubGrupoConvenio g"
                    + " where g.grupoConvenio.id = :pid");
            qry.setParameter("pid", idGrupoConvenio);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public List pesquisaSubGrupoConvênioComServico(int idSubGrupo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select s"
                    + "  from Servicos s"
                    + " where s.id not in (select cs.servicos.id from ConvenioServico cs where cs.subGrupoConvenio.id = " + idSubGrupo + ")");
            return qry.getResultList();
        } catch (EJBQLException e) {
            e.getMessage();
            return null;
        }
    }

    public List pesquisaSubGrupoConvênioSemServico(int idSubGrupo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select s"
                    + "  from Servicos s"
                    + " where s.id in (select cs.servicos.id from ConvenioServico cs where cs.subGrupoConvenio.id = " + idSubGrupo + ")");
            return qry.getResultList();
        } catch (EJBQLException e) {
            e.getMessage();
            return null;
        }
    }
}
