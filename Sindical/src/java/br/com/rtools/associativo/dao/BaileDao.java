package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class BaileDao extends DB{

    public List listaCategoriaPorEventoServico(Integer id_evento) {
        try {
            String textqry
                    = " SELECT c.* \n"
                    + "   FROM soc_categoria c \n "
                    + "  WHERE c.id NOT IN (SELECT id_categoria FROM eve_evento_servico WHERE id_evento = "+id_evento+" AND id_categoria IS NOT NULL)";

            Query qry = getEntityManager().createNativeQuery(textqry, Categoria.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }    
}
