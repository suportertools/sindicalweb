package br.com.rtools.suporte.db;

import br.com.rtools.principal.DB;
import br.com.rtools.suporte.Protocolo;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ProtocoloDBToplink extends DB implements ProtocoloDB {

    public boolean insert(Protocolo protocolo) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(protocolo);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Protocolo protocolo) {
        try {
            getEntityManager().merge(protocolo);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Protocolo protocolo) {
        try {
            getEntityManager().remove(protocolo);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Protocolo pesquisaCodigo(int id) {
        Protocolo result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Protocolo.pesquisaID");
            qry.setParameter("pid", id);
            result = (Protocolo) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pro from Protocolo pro order by pro.protocolo");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> pesquisaProtocolo(String des_tipo) {
        List<String> result = null;
        try {
            Query qry = getEntityManager().createQuery("select pro.protocolo from Protocolo pro where pro.protocolo like :texto");
            qry.setParameter("texto", des_tipo);
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaProtocoloParametros(String por, String combo, String desc) {
        String textQuery = "";
        if (!desc.equals("") && !por.equals("")) {
            if (por.equals("I")) {
                desc = desc + "%";
            } else if (por.equals("P")) {
                desc = "%" + desc + "%";
            }
        } else {
            desc = "";
            return null;
        }
        if (combo.equals("")) {
            combo = "protocolo";
        }
        try {
            textQuery = "select pro from Protocolo pro where upper(pro." + combo + ") like :protocolo order by pro.protocolo";
            Query qry = getEntityManager().createQuery(textQuery);
            qry.setParameter("protocolo", desc.toLowerCase().toUpperCase());
            return (qry.getResultList());
        } catch (EJBQLException e) {
            return null;
        }
    }

    public Protocolo idProtocolo(Protocolo des_Protocolo) {
        Protocolo result = null;
        try {
            Query qry = getEntityManager().createQuery("select p from Protocolo p where p.descricao = :descricao");
            qry.setParameter("descricao", des_Protocolo.getEmpresa());
            result = (Protocolo) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}
