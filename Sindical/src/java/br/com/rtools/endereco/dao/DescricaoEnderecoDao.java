package br.com.rtools.endereco.dao;

import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class DescricaoEnderecoDao extends DB {

    public DescricaoEndereco find(String descricao) {
        try {
            return (DescricaoEndereco) find(descricao, true).get(0);
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
                query = getEntityManager().createNativeQuery("SELECT DE.* FROM end_descricao_endereco AS DE WHERE TRIM(UPPER(FUNC_TRANSLATE(DE.ds_descricao))) = TRIM(UPPER(FUNC_TRANSLATE('" + descricao + "'))) ", DescricaoEndereco.class);
            } else {
                query = getEntityManager().createNativeQuery("SELECT DE.* FROM end_descricao_endereco AS DE WHERE TRIM(UPPER(DE.ds_descricao)) = TRIM(UPPER('" + descricao + "')) ", DescricaoEndereco.class);
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
