package br.com.rtools.seguranca.db;

import br.com.rtools.principal.DB;
import javax.persistence.Query;

public class PesquisaLogDBTopLink extends DB implements PesquisaLogDB{
    
    @Override
    public boolean verificaRotina(String rotina){
        try{
            Query qry = getEntityManager().createQuery("   select rot " +
                                                       "     from Rotina rot " +
                                                       "    where upper(rot.pagina) = upper(:descricao) or " +
                                                       "          upper(rot.rotina) = upper(:dRotina) " +
                                                       " order by rot.rotina ");
            qry.setParameter("descricao", '"'+"/Sindical/"+rotina+".jsf"+'"');
            qry.setParameter("dRotina", ""+rotina+"%");
            if(qry.getResultList().size() > 0){
                return true;
            }else{
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
    }
}