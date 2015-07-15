package br.com.rtools.financeiro.dao;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ServicoPessoaDao extends DB {
    /**
     * Pesquisa Serviço Pessoa por id da Pessoa e id do Serviço 
     * @param id_pessoa 
     * @param id_servico
     * @param is_ativo
     * @return 
     */
    public ServicoPessoa pesquisaServicoPessoa(int id_pessoa, int id_servico, boolean is_ativo) {
        try {
            Query query = getEntityManager().createQuery("SELECT sp FROM ServicoPessoa sp WHERE sp.pessoa.id = "+id_pessoa+" AND sp.servicos.id = "+id_servico+" AND sp.ativo = "+is_ativo);
            query.setMaxResults(1);
            return (ServicoPessoa) query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
    public List<ServicoPessoa> listaTodosServicoPessoaPorTitular(int id_titular_matricula) {
        try {
            Query query = getEntityManager().createNativeQuery(
                    "SELECT sp.* \n "+
                    "  FROM fin_servico_pessoa sp \n " +
                    " INNER JOIN soc_socios s ON s.id_servico_pessoa = sp.id \n " +
                    " INNER JOIN matr_socios m ON m.id = s.id_matricula_socios \n " +
                    " WHERE m.id_titular = "+id_titular_matricula, ServicoPessoa.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
