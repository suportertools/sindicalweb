package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SubGrupoConvenioDBToplink extends DB implements SubGrupoConvenioDB {

    @Override
    public List listaSubGrupoConvenioPorGrupo(int idGrupoConvenio) {
        try {
            Query query = getEntityManager().createQuery(" SELECT SGC FROM SubGrupoConvenio AS SGC WHERE SGC.grupoConvenio.id = :pid ORDER BY SGC.descricao ASC ");
            query.setParameter("pid", idGrupoConvenio);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {}
        return new ArrayList() ;
    }

    @Override
    public boolean existeSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT SGC FROM SubGrupoConvenio AS SGC WHERE SGC.grupoConvenio.id = :grupoConvenio AND UPPER(SGC.descricao) = :descricao");
            qry.setParameter("grupoConvenio", subGrupoConvenio.getGrupoConvenio().getId());
            qry.setParameter("descricao", subGrupoConvenio.getDescricao().toUpperCase());
            return !((List) qry.getResultList()).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Servicos> listaServicosDisponiveis(int idSubGrupoConvenio) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.id NOT IN( SELECT CS.servicos.id FROM ConvenioServico AS CS WHERE CS.subGrupoConvenio.id = :pid ) ORDER BY S.descricao ASC ");
            qry.setParameter("pid", idSubGrupoConvenio);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {}
        return new ArrayList();
    }

    @Override
    public List<ConvenioServico> listaServicosAdicionados(int idSubGrupoConvenio) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT CS FROM ConvenioServico AS CS WHERE CS.subGrupoConvenio.id = :pid ORDER BY CS.servicos.descricao ASC, CS.subGrupoConvenio.descricao ASC ");
            qry.setParameter("pid", idSubGrupoConvenio);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {}
        return new ArrayList();
    }

//    public List pesquisaSubGrupoConvênioComServico(int idSubGrupo) {
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select s"
//                    + "  from Servicos s"
//                    + " where s.id not in (select cs.servicos.id from ConvenioServico cs where cs.subGrupoConvenio.id = " + idSubGrupo + ")");
//            return qry.getResultList();
//        } catch (EJBQLException e) {
//            e.getMessage();
//            return null;
//        }
//    }
//
//    public List pesquisaSubGrupoConvênioSemServico(int idSubGrupo) {
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select s"
//                    + "  from Servicos s"
//                    + " where s.id in (select cs.servicos.id from ConvenioServico cs where cs.subGrupoConvenio.id = " + idSubGrupo + ")");
//            return qry.getResultList();
//        } catch (EJBQLException e) {
//            e.getMessage();
//            return null;
//        }
//    }
}
