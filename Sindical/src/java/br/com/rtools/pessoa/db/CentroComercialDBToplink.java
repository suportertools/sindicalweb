package br.com.rtools.pessoa.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CentroComercialDBToplink extends DB implements CentroComercialDB {

    @Override
    public List listaCentroComercial(int idTipoCentroComercial, int idJuridica) {
        try {
            Query qry = getEntityManager().createQuery(""
                    + "   SELECT CC FROM CentroComercial AS CC                          "
                    + "    WHERE CC.tipoCentroComercial.id = " + idTipoCentroComercial
                    + "      AND CC.juridica.id = " + idJuridica
                    + " ORDER BY CC.tipoCentroComercial.descricao ASC,                  "
                    + "          CC.juridica.pessoa.nome ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}