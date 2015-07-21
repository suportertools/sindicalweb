package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioDao extends DB {

    public List findByRotina(Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Relatorios AS R WHERE R.rotina.id = :rotina_id ORDER BY R.nome");
            query.setParameter("rotina_id", rotina_id);
            return query.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Relatorios findByJasper(String relatorio_jasper) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Relatorios AS R WHERE R.jasper = :relatorio_jasper");
            query.setParameter("relatorio_jasper", relatorio_jasper);
            return (Relatorios) query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public Boolean defineDefault(Relatorios r) {
        if (r.getId() == -1) {
            return false;
        }
        try {
            getEntityManager().getTransaction().begin();
            Query query = getEntityManager().createNativeQuery("UPDATE sis_relatorios SET is_default = false WHERE id_rotina = " + r.getRotina().getId());
            if (query.executeUpdate() == 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            if(r.getPrincipal()) {
                query = getEntityManager().createNativeQuery("UPDATE sis_relatorios SET is_default = true WHERE id = " + r.getId());
                if (query.executeUpdate() == 0) {
                    getEntityManager().getTransaction().rollback();
                    return false;
                }                
            }
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
        return true;
    }
}
