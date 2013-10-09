package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Caravana;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class CaravanaDBToplink extends DB implements CaravanaDB {

    public Caravana pesquisaCodigo(int id) {
        Caravana result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Caravana.pesquisaID");
            qry.setParameter("pid", id);
            result = (Caravana) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select c from Caravana c");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
