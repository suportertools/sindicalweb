package br.com.rtools.financeiro.dao;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CaixaDao extends DB {
    public List<Caixa> listaCaixaUsuario(Integer id_usuario){
        try{
            Query qry = getEntityManager().createQuery("SELECT c FROM Caixa c WHERE c.usuario.id = "+id_usuario);
            List<Caixa> result = qry.getResultList();
            return result;
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList(); 
    }
    
    public List<Caixa> listaOutrosCaixaUsuario(Integer id_usuario){
        try{
            Query qry = getEntityManager().createQuery("SELECT c FROM Caixa c WHERE c.usuario.id <> "+id_usuario);
            List<Caixa> result = qry.getResultList();
            return result;
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList(); 
    }
    
    public List<Caixa> listaTodosCaixas() {
        try {
            String text = 
                        "SELECT c.* " +
                        "  FROM fin_caixa c " +
                        "  LEFT JOIN pes_filial f ON c.id_filial = f.id " +
                        "  LEFT JOIN pes_juridica j ON j.id = f.id_filial " +
                        "  LEFT JOIN pes_pessoa p ON p.id = j.id_pessoa " +
                        " ORDER BY p.ds_nome, c.ds_descricao";
            
            //Query query = getEntityManager().createNativeQuery(text, new Caixa().getClass());
            Query query = getEntityManager().createNativeQuery(text, Caixa.class);
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }    
}
