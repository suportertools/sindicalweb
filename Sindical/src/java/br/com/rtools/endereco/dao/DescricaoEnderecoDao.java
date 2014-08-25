package br.com.rtools.endereco.dao;

import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.List;
import javax.persistence.Query;

public class DescricaoEnderecoDao extends DB {

    public DescricaoEndereco pesquisaDescricaoEnderecoPorDescricao(String descricao) {
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createNativeQuery("SELECT DE.* FROM end_descricao_endereco AS DE WHERE UPPER(TRANSLATE(DE.ds_descricao)) = '" + AnaliseString.removerAcentos(descricao) + "' ", DescricaoEndereco.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (DescricaoEndereco) list.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
