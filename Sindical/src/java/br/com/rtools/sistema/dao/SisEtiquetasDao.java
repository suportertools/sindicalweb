package br.com.rtools.sistema.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.sistema.SisEtiquetas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SisEtiquetasDao extends DB {

    public List execute(Integer id) {
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM SisEtiquetas AS E WHERE E.id = :id");
            query.setParameter("id", id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                SisEtiquetas sisEtiquetas = (SisEtiquetas) query.getSingleResult();
                query = getEntityManager().createNativeQuery(sisEtiquetas.getSql());
                list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findByUser(Integer usuario) {
        try {
            Query query;
            if(usuario == 1) {
                query = getEntityManager().createQuery("SELECT E FROM SisEtiquetas AS E ORDER BY E.dtSolicitacao DESC, E.titulo ASC");
            } else {
                query = getEntityManager().createQuery("SELECT E FROM SisEtiquetas AS E WHERE E.solicitante.id = :usuario AND E.solicitante IS NULL ORDER BY E.dtSolicitacao DESC, E.titulo ASC");
                query.setParameter("usuario", usuario);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

}
