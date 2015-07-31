package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.MalaDireta;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class MalaDiretaDao extends DB {

    public MalaDireta findByPessoa(Integer pessoa_id) {
        try {
            getEntityManager().clear();
            Query query = getEntityManager().createQuery("SELECT MD FROM MalaDireta AS MD WHERE MD.pessoa.id = :pessoa_id");
            query.setParameter("pessoa_id", pessoa_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (MalaDireta) list.get(0);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
