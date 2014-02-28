package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.DescricaoEvento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class DescricaoEventoDBToplink extends DB implements DescricaoEventoDB {

    @Override
    public List<DescricaoEvento> pesquisaDescricaoPorGrupo(int idGrupoEvento) {
        List<DescricaoEvento> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select de from DescricaoEvento de where de.grupoEvento.id = :pid");
            qry.setParameter("pid", idGrupoEvento);
            result = qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List<AEvento> listaEventoPorDescricao(int idDescEvento) {
        List<AEvento> list;
        try {
            Query qry = getEntityManager().createQuery("select e from AEvento e where e.descricaoEvento.id = :pid");
            qry.setParameter("pid", idDescEvento);
            list = qry.getResultList();
        } catch (Exception e) {
            list = new ArrayList();
        }
        return list;
    }

    @Override
    public boolean existeDescricaoEvento(DescricaoEvento de) {
        try {
            Query query = getEntityManager().createQuery("SELECT DE FROM DescricaoEvento AS DE WHERE DE.grupoEvento.id = :grupoEvento AND DE.descricao = :descricao");
            query.setParameter("grupoEvento", de.getGrupoEvento().getId());
            query.setParameter("descricao", de.getDescricao());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
}
