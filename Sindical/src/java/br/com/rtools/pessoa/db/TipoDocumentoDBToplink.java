package br.com.rtools.pessoa.db;

import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class TipoDocumentoDBToplink extends DB implements TipoDocumentoDB {

    @Override
    public List<String> pesquisaTipoDocumento(String des_tipo) {
        List<String> result = null;
        try {
            Query qry = getEntityManager().createQuery("SELECT TIPO.descricao FROM TipoDocumento AS TIPO WHERE TIPO.descricao LIKE :texto");
            qry.setParameter("texto", des_tipo);
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
}
