package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.Socios;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class SociosDao extends DB {

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     * @param id Pessoa do serviço pessoa
     * @return
     */
    public Socios pesquisaTitularPorDependente(Integer id) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :id AND S.matriculaSocios.dtInativo IS NULL AND S.servicoPessoa.ativo = true");
            query.setParameter("id", id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Socios) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

}
