package br.com.rtools.suporte.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.suporte.Protocolo;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ProtocoloDao extends DB {

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
