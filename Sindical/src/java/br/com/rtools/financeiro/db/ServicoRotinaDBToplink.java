package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ServicoRotinaDBToplink extends DB implements ServicoRotinaDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("SELECT SR From ServicoRotina AS SR");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaServicoRotinaPorServico(int idServico) {
        try {
            Query qry = getEntityManager().createQuery("SELECT SR FROM ServicoRotina AS SR WHERE SR.servicos.id = " + idServico + " ORDER BY SR.rotina.rotina ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaTodasRotinasSemServicoOrdenado(int idServico) {
        try {
            Query qry = getEntityManager().createQuery(
                    "   SELECT ROT "
                    + "     FROM Rotina AS ROT "
                    + "    WHERE ROT.id NOT IN(SELECT SC.rotina.id FROM ServicoRotina AS SC WHERE SC.servicos.id = " + idServico + ")"
                    + "      AND ROT.ativo = true "
                    + " ORDER BY ROT.rotina");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaTodosServicosComRotinas(int idRotina) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT S "
                    + "     FROM Servicos AS S "
                    + "    WHERE S.id IN(SELECT SR.servicos.id FROM ServicoRotina SR WHERE SR.rotina.id = " + idRotina + ")"
                    + " ORDER BY S.descricao");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public boolean existeServicoRotina(int idServico, int idRotina) {
        try {
            Query query = getEntityManager().createQuery(" SELECT SR FROM ServicoRotina AS SR WHERE SR.servicos.id = :servicos AND SR.rotina.id = :rotina ");
            query.setParameter(":servicos", idServico);
            query.setParameter(":rotina ", idRotina);
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    public List<Servicos> listaServicosNotIn(String ids) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT sr.servicos "
                    + "   FROM ServicoRotina AS sr"
                    + "  WHERE sr.rotina.id NOT IN ("+ids+") "
                    + "  ORDER BY sr.servicos.descricao");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }    
}
