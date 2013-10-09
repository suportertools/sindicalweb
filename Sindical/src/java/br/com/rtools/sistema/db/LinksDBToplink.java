package br.com.rtools.sistema.db;

import br.com.rtools.principal.DB;
import br.com.rtools.sistema.Links;
import javax.persistence.Query;

public class LinksDBToplink extends DB implements LinksDB {

    @Override
    public Links pesquisaNomeArquivo(String arquivo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select l "
                    + "  from Links l "
                    + " where l.nomeArquivo = '" + arquivo + "'");
            return (Links) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
