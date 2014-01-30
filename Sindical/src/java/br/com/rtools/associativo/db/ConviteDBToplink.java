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

    @Override
    public boolean existeSisPessoaSuspensa(ConviteSuspencao cs) {
        try {
            String queryString = "SELECT * FROM conv_suspencao WHERE id_sis_pessoa = " + cs.getSisPessoa().getId() + " AND dt_fim >= CURRENT_DATE ";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    
    
    
    @Override
    public List<ConviteSuspencao> listaPessoasSuspensas(ConviteSuspencao cs, boolean filtro, boolean fitroPorPessoa) {
        return listaPessoasSuspensas(cs, filtro, fitroPorPessoa, "", "", "");
    }

    @Override
    public List<ConviteSuspencao> listaPessoasSuspensas(ConviteSuspencao cs, boolean filtro, boolean fitroPorPessoa, String descricaoPesquisa, String porPesquisa, String comoPesquisa) {
        List list = new ArrayList();
        Query query;
        String queryString = "";
        String filtroQueryPessoa = "";
        String filtroQueryA;
        if (!descricaoPesquisa.equals("")) {
            if (comoPesquisa.equals("I")) {
                filtroQueryA = "'" + descricaoPesquisa + "%'";
            } else {
                filtroQueryA = "'%" + descricaoPesquisa + "%'";
            }
            try {
                if (porPesquisa.equals("nome")) {
                    queryString = " SELECT CS FROM ConviteSuspencao AS CS WHERE UPPER(CS.sisPessoa.nome) LIKE " + filtroQueryA.toUpperCase() + " ORDER BY CS.sisPessoa.nome ASC, CS.dtInicio DESC ";
                } else {
                    queryString = " SELECT CS FROM ConviteSuspencao AS CS WHERE CS.sisPessoa.documento = '" + descricaoPesquisa + "' ORDER BY CS.sisPessoa.nome ASC, CS.dtInicio DESC   ";
                }
                query = getEntityManager().createQuery(queryString);
                list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                return list;
            }
        } else {
            if (fitroPorPessoa) {
                if (filtro) {
                    filtroQueryPessoa = " WHERE CS.sisPessoa.id = " + cs.getSisPessoa().getId();
                } else {
                    filtroQueryPessoa = " AND CS.sisPessoa.id = " + cs.getSisPessoa().getId();
                }
            }
            try {
                if (!filtro) {
                    queryString = " SELECT CS FROM ConviteSuspencao AS CS " + filtroQueryPessoa + " ORDER BY CS.sisPessoa.nome ASC, CS.dtInicio DESC ";
                } else {
                    if (fitroPorPessoa) {
                        queryString = " SELECT CS FROM ConviteSuspencao AS CS " + filtroQueryPessoa + " ORDER BY CS.sisPessoa.nome ASC, CS.dtInicio DESC ";
                    } else {
                        queryString = " SELECT CS FROM ConviteSuspencao AS CS WHERE CS.dtFim IS NULL " + filtroQueryPessoa + " ORDER BY CS.sisPessoa.nome ASC, CS.dtInicio DESC ";
                    }
                }
                query = getEntityManager().createQuery(queryString);
                list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                return list;
            }
        }
        return list;
    }
}
