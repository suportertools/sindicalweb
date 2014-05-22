package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.FilialCidade;
import br.com.rtools.principal.DB;
import javax.persistence.Query;

public class FilialCidadeDBToplink extends DB implements FilialCidadeDB {

    @Override
    public FilialCidade pesquisaFilialCidade(int idFilial, int idCidade) {
        FilialCidade result = new FilialCidade();
        try {
            Query qry = getEntityManager().createQuery("select fc from FilialCidade fc"
                    + " where fc.cidade.id = " + idCidade
                    + "   and fc.filial.id = " + idFilial);
            if (!qry.getResultList().isEmpty()) {
                result = (FilialCidade) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public FilialCidade pesquisaFilialPorCidade(int idCidade) {
        FilialCidade result = new FilialCidade();
        try {
            Query qry = getEntityManager().createQuery("select fc from FilialCidade fc"
                    + " where fc.cidade.id = " + idCidade);
            if (!qry.getResultList().isEmpty()) {
                result = (FilialCidade) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}
