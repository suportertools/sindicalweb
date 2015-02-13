package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class SocioCarteirinhaDao extends DB {

    /**
     *
     * @param pessoa (ID)
     * @param modelo (ID)
     * @return
     */
    public SocioCarteirinha pesquisaPorPessoaModelo(Integer pessoa, Integer modelo) {
        try {
            Query query = getEntityManager().createQuery("SELECT SC FROM SocioCarteirinha AS SC WHERE SC.pessoa.id = :pessoa AND SC.modeloCarteirinha.id = :modelo");
            query.setParameter("pessoa", pessoa);
            query.setParameter("modelo", modelo);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (SocioCarteirinha) list.get(0);
            }
        } catch (Exception e) {

        }
        return null;
    }

}
