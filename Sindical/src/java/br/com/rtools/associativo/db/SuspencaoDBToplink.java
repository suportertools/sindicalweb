package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Suspencao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class SuspencaoDBToplink extends DB implements SuspencaoDB {

    @Override
    public boolean existeSuspensaoSocio(Suspencao suspencao) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Suspencao AS S WHERE S.pessoa.id = :pessoa AND S.dtFinal < CURRENT_DATE");
            query.setParameter("pessoa", suspencao.getPessoa().getId());
            query.setParameter("dataFinal", suspencao.getDataFinal());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
