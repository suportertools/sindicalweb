package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioOrdemDao extends DB {

    public List findAllByRelatorio(Integer relatorio) {
        try {
            Query query = getEntityManager().createQuery("SELECT RO FROM RelatorioOrdem AS RO WHERE RO.relatorios.id = :relatorio");
            query.setParameter("relatorio", relatorio);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();

    }

}
