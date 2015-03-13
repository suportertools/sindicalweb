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
}
