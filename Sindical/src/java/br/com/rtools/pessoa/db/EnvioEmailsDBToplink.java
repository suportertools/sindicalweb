package br.com.rtools.pessoa.db;

import br.com.rtools.principal.DB;
import br.com.rtools.pessoa.EnvioEmails;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EnvioEmailsDBToplink extends DB implements EnvioEmailsDB {

    public boolean insert(EnvioEmails envioEmails) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(envioEmails);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(EnvioEmails envioEmails) {
        try {
            getEntityManager().merge(envioEmails);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(EnvioEmails envioEmails) {
        try {
            getEntityManager().remove(envioEmails);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public EnvioEmails pesquisaCodigo(int id) {
        EnvioEmails result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("EnvioEmails.pesquisaID");
            qry.setParameter("pid", id);
            result = (EnvioEmails) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select con from EnvioEmails con ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List<EnvioEmails> pesquisaTodosPorPessoa(int idPessoa) {
        List<EnvioEmails> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select e from EnvioEmails e where e.pessoa.id = " + idPessoa);
            result = (qry.getResultList());
            return result;
        } catch (Exception e) {
            return result;
        }
    }
}
