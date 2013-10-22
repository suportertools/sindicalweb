package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Spc;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;


public class SpcDBToplink extends DB implements SpcDB {

    @Override
    public List<Spc> lista (Spc spc, boolean filtro, boolean fitroPorPessoa) {
        List list = new ArrayList();
        Query query;
        String filtroQueryPessoa = "";
        if (fitroPorPessoa) {
            if (filtro) {
                filtroQueryPessoa = " AND S.pessoa.id = "+spc.getPessoa().getId();
            } else {
                filtroQueryPessoa = " WHERE S.pessoa.id = "+spc.getPessoa().getId();                
            }
        }
        try {
            if (filtro) {
                query = getEntityManager().createQuery( " SELECT S FROM SPC AS S "+filtroQueryPessoa+" ORDER BY S.pessoa.nome ASC, DESC, S.dtSaida DESC " );
            } else {
                query = getEntityManager().createQuery( " SELECT S FROM SPC AS S WHERE S.dtSaida IS NULL "+filtroQueryPessoa+" ORDER BY S.pessoa.nome ASC, S.dtEntrada DESC " );
            }
            list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            
        } 
        return list;
    }
    
    @Override
    public boolean existeCadastroSPC(Spc spc) {
        try {
            Query query = getEntityManager().createQuery(" SELECT S FROM SCP AS S WHERE S.pessoa.id = :idPessoa AND S.dtEntrada = :dataEntrada");
            query.setParameter("idPessoa", spc.getPessoa().getId());
            query.setParameter("dataEntrada", spc.getDataEntrada());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {}
        return false;
    }
    
}