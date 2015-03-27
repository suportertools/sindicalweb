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
        } catch (Exception e) {
        }
        return new ArrayList();
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
    public List listaServicosDisponiveis(int idSubGrupoConvenio) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.situacao = 'A' AND S.id NOT IN (SELECT SR.servicos.id FROM ServicoRotina AS SR WHERE SR.rotina.id = 4 GROUP BY SR.servicos.id) AND S.id NOT IN( SELECT CS.servicos.id FROM ConvenioServico AS CS WHERE CS.subGrupoConvenio.id = :pid ) ORDER BY S.descricao ASC ");
            qry.setParameter("pid", idSubGrupoConvenio);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List listaServicosDisponiveisPorGrupoFinanceiro(Integer idSubGrupoConvenio, Integer idGrupoFinanceiro) {
        String queryString = ""
                + "     SELECT S.* FROM fin_servicos AS S                                                                                                               "
                + "  LEFT JOIN fin_subgrupo AS SGF ON SGF.id = S.id_subgrupo                                                                                          "
                + "  WHERE S.id IN (                                                                                                                                    "
                + "            SELECT SV.id FROM fin_servicos AS SV                                                                                                     "
                + "         LEFT JOIN fin_subgrupo AS SG ON SG.id = SV.id_subgrupo                                                                                      "
                + "             WHERE SV.ds_situacao = 'A'                                                                                                              "
                + "               AND SV.id NOT IN (SELECT SR.id_servicos FROM fin_servico_rotina AS SR WHERE SR.id_rotina = 4 GROUP BY SR.id_servicos)                 "
                + "               AND SV.id NOT IN (SELECT CS.id_servico FROM soc_convenio_servico AS CS WHERE CS.id_convenio_sub_grupo = " + idSubGrupoConvenio + " )  "
                + "               AND SG.id_grupo = " + idGrupoFinanceiro
                + "          GROUP BY SV.id, SV.id_subgrupo)                                                                                                            "
                + "   ORDER BY SGF.ds_descricao ASC";
        try {
            Query query = getEntityManager().createNativeQuery(queryString, Servicos.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List listaServicosDisponiveisPorSubGrupoFinanceiro(Integer idSubGrupoConvenio, Integer idSubGrupoFinanceiro) {
        String queryString = " "
                + "     SELECT S.*                                                                                                                              "
                + "       FROM fin_servicos AS S                                                                                                                "
                + "      WHERE S.id_subgrupo = ?                                                                                                                "
                + "        AND S.ds_situacao = 'A'                                                                                                              "
                + "        AND S.id NOT IN (SELECT SR.id_servicos FROM fin_servico_rotina AS SR WHERE SR.id_rotina = 4 GROUP BY SR.id_servicos)                 "
                + "        AND S.id NOT IN (SELECT CS.id_servico FROM soc_convenio_servico AS CS WHERE CS.id_convenio_sub_grupo = " + idSubGrupoConvenio + " )  "
                + "   ORDER BY S.ds_descricao ASC";
        try {
            Query query = getEntityManager().createNativeQuery(queryString, Servicos.class);
            query.setParameter("1", idSubGrupoFinanceiro);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
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
        } catch (Exception e) {
        }
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
