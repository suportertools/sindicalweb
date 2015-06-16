package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CategoriaDao extends DB {

    public List<?> findCategoriaByGrupoCategoria(String in) {
        if (in == null || in.isEmpty()) {
            return new ArrayList();
        }
        try {
            String queryString = "SELECT C.* FROM soc_categoria AS C WHERE id_grupo_categoria IN (" + in + ") ORDER BY ds_categoria ";
            Query query = getEntityManager().createNativeQuery(queryString, Categoria.class);
            return query.getResultList();
        } catch (Exception e) {

        }
        return new ArrayList();
    }

}
