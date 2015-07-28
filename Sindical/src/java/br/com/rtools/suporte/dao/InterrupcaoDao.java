package br.com.rtools.suporte.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.suporte.Interrupcao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class InterrupcaoDao extends DB {

    public Interrupcao idInterrupcao(Interrupcao des_Interrupcao) {
        Interrupcao result = null;
        try {
            Query qry = getEntityManager().createQuery("select i from Interrupcao os where i.descricao = :descricao");
            qry.setParameter("descricao", des_Interrupcao.getMotivo());
            result = (Interrupcao) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List<String> pesquisaInterrupcao(String des_tipo) {
        List<String> result = null;
        try {
            Query qry = getEntityManager().createQuery("select int.interrupcao from Interrupcao int where int.interrupcao like :texto");
            qry.setParameter("texto", des_tipo);
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaInterrupcaoParametros(String por, String combo, String desc) {
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
            combo = "interrupcao";
        }
        try {
            textQuery = "select int from Interrupcao int where upper(int." + combo + ") like :interrupcao order by int.interrupcao";
            Query qry = getEntityManager().createQuery(textQuery);
            qry.setParameter("interrupcao", desc.toLowerCase().toUpperCase());
            return (qry.getResultList());
        } catch (EJBQLException e) {
            return null;
        }
    }

    public List<Interrupcao> listaInterrupcao(int idOrdemServico) {
        try {
            Query qry = getEntityManager().createQuery(
                    " select i "
                    + "   from Interrupcao i"
                    + "  where i.ordemServico.id = " + idOrdemServico
                    + "  order by i.date desc");
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
