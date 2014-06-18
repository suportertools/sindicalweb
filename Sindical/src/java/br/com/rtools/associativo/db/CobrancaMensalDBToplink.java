package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CobrancaMensalDBToplink extends DB implements CobrancaMensalDB {
    
    public List<ServicoPessoa> listaCobrancaMensal(int id_pessoa) {
        try{
            List<ServicoPessoa> result = new ArrayList();
            
            String textqry = "SELECT sp "
                    + "  FROM ServicoPessoa sp "
                    + " WHERE sp.ativo = TRUE ";
                    
    
            if (id_pessoa != -1){
                textqry += " AND sp.pessoa.id = "+id_pessoa;
            }
            
            textqry += " ORDER BY sp.pessoa.nome ASC";
            Query qry = getEntityManager().createQuery(textqry);
            
            result = qry.getResultList();
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
}
