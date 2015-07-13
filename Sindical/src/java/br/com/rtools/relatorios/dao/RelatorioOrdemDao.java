package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.RelatorioOrdem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioOrdemDao extends DB {

    public List findAllByRelatorio(Integer relatorio) {
        try {
            Query query = getEntityManager().createNativeQuery("SELECT RO.* FROM sis_relatorio_ordem AS RO WHERE RO.id_relatorio = " + relatorio + " ORDER BY RO.is_default DESC, RO.ds_descricao ASC", RelatorioOrdem.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public Boolean defineDefault(RelatorioOrdem ro) {
        try {
            getEntityManager().getTransaction().begin();
            Query query = getEntityManager().createNativeQuery("UPDATE sis_relatorio_ordem SET is_default = false WHERE id_relatorio = " + ro.getRelatorios().getId());
            if (query.executeUpdate() == 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            query = getEntityManager().createNativeQuery("UPDATE sis_relatorio_ordem SET is_default = true WHERE id = " + ro.getPrincipal());
            if (query.executeUpdate() == 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
        return true;
    }

}
