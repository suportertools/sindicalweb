package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class EnviarArquivosDBToplink  extends DB implements EnviarArquivosDB {
 
    @Override
    public Juridica pesquisaCodigo(int id) {
        Juridica result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Juridica.pesquisaID");
            qry.setParameter("pid", id);
            result = (Juridica) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }
    
    @Override
    public List pesquisaContabilidades(){
        String textQuery = "";
        try{
            textQuery = "     SELECT jc.id,                                           " +
                        "            p.ds_nome as nome,                               " +
                        "            p.ds_telefone1 as telefone,                      " +
                        "            count(*) qtde,                                   " +
                        "            p.ds_email1 as email                             " +
                        "       FROM arr_contribuintes_vw as c                        " +
                        " INNER JOIN pes_juridica as jc on jc.id = c.id_contabilidade " +
                        " INNER JOIN pes_pessoa as p on p.id = jc.id_pessoa           " +
                        "      WHERE c.dt_inativacao is null                          " +
                        "        AND length(rtrim(p.ds_email1)) > 0                   " +
                        "   GROUP BY jc.id,                                           " +
                        "            p.ds_nome,                                       " +
                        "            p.ds_telefone1,                                  " +
                        "            ds_email1                                        " +
                        "   ORDER BY p.ds_nome                                        ";

            Query qry = getEntityManager().createNativeQuery(textQuery);
            return qry.getResultList();
            
        }catch(EJBQLException e){
            return new ArrayList();
        }
    }
    
    @Override
    public List pesquisaContabilidadesSimples(){
        String textQuery = "";
        try{
            textQuery = "     SELECT c.id_juridica,                                   " +
                        "            p.ds_nome as nome,                               " +
                        "            p.ds_telefone1 as telefone,                      " +
                        "            p.ds_email1 as email                             " +
                        "       FROM arr_contribuintes_vw as c                        " +
                        " INNER JOIN pes_pessoa as p on p.id = c.id_pessoa            " +
                        "      WHERE c.dt_inativacao is null                          " +
                        "        AND length(rtrim(p.ds_email1)) > 0                   " +
                        "   ORDER BY p.ds_nome                                        ";

            Query qry = getEntityManager().createNativeQuery(textQuery);
            return qry.getResultList();
            
        }catch(EJBQLException e){
            return new ArrayList();
        }
    }
}