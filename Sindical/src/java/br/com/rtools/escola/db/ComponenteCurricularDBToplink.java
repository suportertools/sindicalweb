package br.com.rtools.escola.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ComponenteCurricularDBToplink extends DB implements ComponenteCurricularDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("SELECT V FROM ComponenteCurricular V ORDER BY V.descricao ASC ");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
