package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.FilialRotina;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Rotina;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FilialRotinaDao extends DB {

    public Boolean exist(Integer filial_id, Integer rotina_id) {
        return find(filial_id, rotina_id) != null;
    }

    public FilialRotina find(Integer filial_id, Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT FR FROM FilialRotina AS FR WHERE FR.filial.id = :filial_id AND FR.rotina.id = :rotina_id");
            query.setParameter("filial_id", filial_id);
            query.setParameter("rotina_id", rotina_id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (FilialRotina) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List findByRotina(Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT FR FROM FilialRotina AS FR WHERE FR.rotina.id = :rotina_id ORDER BY FR.filial.filial.pessoa.nome");
            query.setParameter("rotina_id", rotina_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();

    }

    public List findByFilial(Integer filial_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT FR FROM FilialRotina AS FR WHERE FR.filial.id = :filial_id ORDER BY FR.rotina.rotina ASC ");
            query.setParameter("filial_id", filial_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();

    }

}
