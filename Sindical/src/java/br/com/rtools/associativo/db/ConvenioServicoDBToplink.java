package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ConvenioServicoDBToplink extends DB implements ConvenioServicoDB {

    public boolean insert(ConvenioServico convenioServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(convenioServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(ConvenioServico convenioServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(convenioServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(ConvenioServico convenioServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(convenioServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public ConvenioServico pesquisaCodigo(int id) {
        ConvenioServico result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ConvenioServico.pesquisaID");
            qry.setParameter("pid", id);
            result = (ConvenioServico) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cs from ConvenioServico cs");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public ConvenioServico pesquisaConvenioServicoPorServESubGrupo(int idServico, int idSubGrupo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select cs"
                    + "  from ConvenioServico cs"
                    + " where cs.servicos.id = " + idServico
                    + "   and cs.subGrupoConvenio.id = " + idSubGrupo);
            return (ConvenioServico) qry.getSingleResult();
        } catch (EJBQLException e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaServicosSubGrupoConvenio(int idSubGrupo) {
        try {
            Query qry = getEntityManager().createQuery("select cs.servicos from ConvenioServico cs where cs.subGrupoConvenio.id = " + idSubGrupo + " order by cs.servicos.descricao");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
