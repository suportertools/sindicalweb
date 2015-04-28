package br.com.rtools.financeiro.dao;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.principal.DB;
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
}
