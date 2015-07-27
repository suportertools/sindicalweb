package br.com.rtools.seguranca.db;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.MacFilial;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MacFilialDBToplink extends DB {

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("SELECT MF FROM MacFilial MF ORDER BY MF.filial.filial.pessoa.nome ASC, MF.departamento.descricao ASC, MF.mesa ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public MacFilial pesquisaMac(String mac) {
        try {
            Query query = getEntityManager().createQuery("SELECT MF FROM MacFilial AS MF WHERE MF.mac = :mac");
            query.setParameter("mac", mac);
            if (!query.getResultList().isEmpty()) {
                return (MacFilial) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List listaTodosPorFilial(Integer filial) {
        try {
            Query query;
            if (filial == null) {
                query = getEntityManager().createQuery("SELECT MF FROM MacFilial MF ORDER BY MF.filial.filial.pessoa.nome ASC, MF.departamento.descricao ASC, MF.mesa ASC ");
            } else {
                query = getEntityManager().createQuery("SELECT MF FROM MacFilial MF WHERE MF.filial.id = :filial ORDER BY MF.filial.filial.pessoa.nome ASC, MF.departamento.descricao ASC, MF.mesa ASC ");
                query.setParameter("filial", filial);
            }
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
