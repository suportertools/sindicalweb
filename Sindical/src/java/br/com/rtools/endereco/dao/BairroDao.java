package br.com.rtools.endereco.dao;

import br.com.rtools.endereco.Bairro;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class BairroDao extends DB {

    public Bairro find(String descricao) {
        try {
            return (Bairro) find(descricao, true).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean exists(String descricao) {
        return find(descricao) != null;
    }

    public List find(String descricao, Boolean filter) {
        try {
            Query query;
            if (filter) {
                query = getEntityManager().createNativeQuery("SELECT B.* FROM end_bairro AS B WHERE TRIM(UPPER(FUNC_TRANSLATE(B.ds_descricao))) = TRIM(UPPER(FUNC_TRANSLATE('" + descricao + "'))) ", Bairro.class);
            } else {
                query = getEntityManager().createNativeQuery("SELECT B.* FROM end_bairro AS B WHERE TRIM(UPPER(B.ds_descricao)) = TRIM(UPPER('" + descricao + "')) ", Bairro.class);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
