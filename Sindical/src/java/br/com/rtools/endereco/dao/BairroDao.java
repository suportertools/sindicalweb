package br.com.rtools.endereco.dao;

import br.com.rtools.endereco.Bairro;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.List;
import javax.persistence.Query;

public class BairroDao extends DB {

    public Bairro pesquisaBairroPorDescricaoCliente(String descricao) {
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createNativeQuery("SELECT B.* FROM end_bairro AS B WHERE UPPER(TRANSLATE(B.ds_descricao)) = '" + AnaliseString.removerAcentos(descricao) + "' ", Bairro.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Bairro) list.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
