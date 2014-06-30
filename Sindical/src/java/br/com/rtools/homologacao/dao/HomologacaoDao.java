package br.com.rtools.homologacao.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class HomologacaoDao extends DB {

    public List pesquisaAgendamentoAtendimentoAberto(int idUsuario) {
        try {
            Query query = getEntityManager().createQuery("SELECT A FROM Agendamento AS A WHERE A.homologador.id = :usuario AND A.dtData < CURRENT_TIMESTAMP AND A.status.id = 5 ORDER BY A.dtData DESC, A.horarios.hora DESC ");
            query.setParameter("usuario", idUsuario);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

}
