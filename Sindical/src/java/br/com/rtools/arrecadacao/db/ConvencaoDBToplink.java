package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ConvencaoDBToplink extends DB implements ConvencaoDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from Convencao cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Convencao pesquisaConvencaoDesc(String descricao) {
        Convencao result = null;
        try {
            Query qry = getEntityManager().createQuery("select con from Convencao con where con.descricao like :d_convencao");
            qry.setParameter("d_convencao", descricao);
            result = (Convencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
