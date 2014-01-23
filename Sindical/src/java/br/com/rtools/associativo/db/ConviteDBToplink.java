package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.associativo.ConviteSuspencao;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConviteDBToplink extends DB implements ConviteDB {

    @Override
    public List<ConviteServico> conviteServicoExiste(ConviteServico cs) {
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM ConviteServico AS C WHERE C.servicos.id = :servicos AND C.domingo = :domingo AND C.segunda = :segunda AND C.terca = :terca AND C.quarta = :quarta AND C.quinta = :quinta AND C.sexta = :sexta AND C.sabado = :sabado AND C.feriado = :feriado");
            query.setParameter("servicos", cs.getServicos().getId());
            query.setParameter("domingo", cs.isDomingo());
            query.setParameter("segunda", cs.isSegunda());
            query.setParameter("terca", cs.isTerca());
            query.setParameter("quarta", cs.isQuarta());
            query.setParameter("quinta", cs.isQuinta());
            query.setParameter("sexta", cs.isSexta());
            query.setParameter("sabado", cs.isSabado());
            query.setParameter("feriado", cs.isFeriado());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public boolean existeSisPessoaSuspensa(ConviteSuspencao cs) {
        try {
            Query query = getEntityManager().createNativeQuery("SELECT * FROM conv_suspencao WHERE id_sis_pessoa = "+cs.getSisPessoa().getId()+" AND ( dt_fim != null AND dt_fim > CURRENT_DATE);");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
