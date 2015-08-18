package br.com.rtools.pessoa.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.pessoa.PessoaComplemento;
import java.util.List;
import javax.persistence.Query;

public class PessoaComplementoDao extends DB {

    public PessoaComplemento findByPessoa(Integer pessoa_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT PC FROM PessoaComplemento AS PC WHERE PC.pessoa.id = :pessoa_id");
            query.setParameter("pessoa_id", pessoa_id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PessoaComplemento) list.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
