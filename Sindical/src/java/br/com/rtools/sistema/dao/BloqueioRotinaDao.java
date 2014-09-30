package br.com.rtools.sistema.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.sistema.BloqueioRotina;
import java.util.List;
import javax.persistence.Query;

public class BloqueioRotinaDao extends DB {

    public BloqueioRotina existUsuarioRotinaPessoa(int idRotina, int idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT BR FROM BloqueioRotina AS BR WHERE BR.rotina.id = :idRotina AND BR.pessoa.id = :idPessoa");
            query.setParameter("idRotina", idRotina);
            query.setParameter("idPessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (BloqueioRotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
