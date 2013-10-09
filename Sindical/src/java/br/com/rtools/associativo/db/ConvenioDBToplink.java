package br.com.rtools.associativo.db;

import br.com.rtools.principal.DB;
import br.com.rtools.associativo.Convenio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConvenioDBToplink extends DB implements ConvenioDB {

    public boolean insert(Convenio convenio) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(convenio);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Convenio convenio) {
        try {
            getEntityManager().merge(convenio);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Convenio convenio) {
        try {
            getEntityManager().remove(convenio);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Convenio pesquisaCodigo(int id) {
        Convenio result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Convenio.pesquisaID");
            qry.setParameter("pid", id);
            result = (Convenio) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select g from Convenio g ");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public List pesquisaConvenioPorGrupoPessoa(int idPessoaJuridica, int idGrupoConvenio) {
        List lista = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c"
                    + "  from Convenio c"
                    + " where c.juridica.pessoa.id = :idPessoa"
                    + "   and c.grupoConvenio.id = :idGrupo");
            qry.setParameter("idPessoa", idPessoaJuridica);
            qry.setParameter("idGrupo", idGrupoConvenio);
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

    public List pesquisaJuridicaPorGrupoESubGrupo(int idSubGrupoConvenio, int idGrupo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.juridica"
                    + "  from Convenio c"
                    + " where c.subGrupoConvenio.id = " + idSubGrupoConvenio
                    + "   and c.subGrupoConvenio.grupoConvenio.id = " + idGrupo);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
