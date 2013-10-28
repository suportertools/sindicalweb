package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Spc;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;


public class SpcDBToplink extends DB implements SpcDB {

    @Override
    public List<Spc> lista (Spc spc, boolean filtro, boolean fitroPorPessoa) {
        return lista(spc, filtro, fitroPorPessoa, "", "", "");
    }
    
    @Override
    public List<Spc> lista (Spc spc, boolean filtro, boolean fitroPorPessoa, String descricaoPesquisa, String porPesquisa, String comoPesquisa) {
        List list = new ArrayList();
        Query query;
        String filtroQueryPessoa = "";
        String filtroQueryA;
        if (!descricaoPesquisa.equals("")) {
            if (comoPesquisa.equals("I")) {
               filtroQueryA = "'"+descricaoPesquisa+"%'";
            } else {
               filtroQueryA = "'%"+descricaoPesquisa+"%'";
            }
            try {
                if (porPesquisa.equals("nome")) {
                    query = getEntityManager().createQuery( " SELECT S FROM Spc AS S WHERE UPPER(S.pessoa.nome) LIKE "+filtroQueryA.toUpperCase() +" ORDER BY S.pessoa.nome ASC, S.dtEntrada DESC ");
                } else {
                    query = getEntityManager().createQuery( " SELECT S FROM Spc AS S WHERE S.pessoa.documento = '"+descricaoPesquisa+"' ORDER BY S.pessoa.nome ASC, S.dtEntrada DESC  ");
                }
                list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) { 
                return list;            
            }
        } else {
            if (fitroPorPessoa) {
                if (filtro) {
                    filtroQueryPessoa = " WHERE S.pessoa.id = "+spc.getPessoa().getId();
                } else {
                    filtroQueryPessoa = " AND S.pessoa.id = "+spc.getPessoa().getId();
                }
            }
            try {
                if (!filtro) {
                    query = getEntityManager().createQuery( " SELECT S FROM Spc AS S "+filtroQueryPessoa+" ORDER BY S.pessoa.nome ASC, S.dtEntrada DESC " );
                } else {
                    if (fitroPorPessoa) {
                        query = getEntityManager().createQuery( " SELECT S FROM Spc AS S "+filtroQueryPessoa+" ORDER BY S.pessoa.nome ASC, S.dtEntrada DESC " );
                    } else {
                        query = getEntityManager().createQuery( " SELECT S FROM Spc AS S WHERE S.dtSaida IS NULL "+filtroQueryPessoa+" ORDER BY S.pessoa.nome ASC, S.dtEntrada DESC " );                    
                    }
                }
                list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) { 
                return list;            
            }
        }
        return list;
    }
    
    @Override
    public boolean existeCadastroSPC(Spc spc) {
        try {
            Query query = getEntityManager().createQuery(" SELECT S FROM Spc AS S WHERE S.pessoa.id = :idPessoa AND S.dtEntrada = :dataEntrada");
            query.setParameter("idPessoa", spc.getPessoa().getId());
            query.setParameter("dataEntrada", spc.getDtEntrada());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    
    /**
     * Verifica se há pessoa está cadastrada no SPC/Serasa
     * @param pessoa
     * @return true / false
     */
    @Override
    public boolean existeRegistroPessoaSPC(Pessoa pessoa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT S FROM Spc AS S WHERE S.pessoa.id = :idPessoa AND S.dtSaida IS NULL ");
            query.setParameter("idPessoa", pessoa.getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
}