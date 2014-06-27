package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CobrancaMensalDBToplink extends DB implements CobrancaMensalDB {
    
    @Override
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
    
    @Override
    public List<ServicoPessoa> listaCobrancaMensalServico(int id_pessoa, int id_servico) {
        try{
            List<ServicoPessoa> result = new ArrayList();
            
            String textqry = "SELECT sp "
                    + "  FROM ServicoPessoa sp "
                    + " WHERE sp.ativo = TRUE "
                    + "   AND sp.pessoa.id = " + id_pessoa 
                    + "   AND sp.servicos.id = " + id_servico;
            Query qry = getEntityManager().createQuery(textqry);
            
            result = qry.getResultList();
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List<ServicoPessoa> listaCobrancaMensalFiltro(String por, String desc) {
        try{
            List<ServicoPessoa> result = new ArrayList();
            desc = desc.toUpperCase();
            String textqry = 
                    "   SELECT sp "
                    + "   FROM ServicoPessoa sp "
                    + "  WHERE sp.ativo = TRUE ";
                    
    
            if (por.equals("beneficiario")){
                textqry += " AND sp.pessoa.nome like '%"+desc+"%'";
            }else if (por.equals("responsavel")){
                textqry += " AND sp.cobranca.nome like '%"+desc+"%'";
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
