package br.com.rtools.pessoa.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CentroComercialDBToplink extends DB implements CentroComercialDB{
    public List pesquisaTodosOrdernado() {
        try{
            Query qry = getEntityManager().createQuery("select cc from CentroComercial cc order by cc.tipoCentroComercial.id");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    public List listaCentros(int idTipoCentroComercial,int idJuridica){
        try{
            Query qry = getEntityManager().createQuery("select cc from CentroComercial cc "+
                                                       " where cc.tipoCentroComercial.id = "+idTipoCentroComercial +
                                                       "   and cc.juridica.id = "+idJuridica);
            return qry.getResultList();
        }
        catch(Exception e){
            return new ArrayList();
        }
    }
}