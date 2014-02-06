package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.associativo.ConviteSuspencao;
import br.com.rtools.principal.DB;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
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
        String queryString;
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

    public boolean pesquisaLimiteConvitesPorSocio(ConviteSuspencao cs, int quantidade) {
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

    public boolean pesquisaLimiteConvitesPorConvidado(ConviteSuspencao cs, int quantidade) {
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
    public List pesquisaConviteMovimento(String descricaoPesquisa, String porPesquisa, String comoPesquisa) {
        String filtroString = "";
        if (porPesquisa.equals("nome")) {
            filtroString = " WHERE UPPER(CM.sisPessoa.nome) LIKE :descricaoPesquisa ";
        } else if (porPesquisa.equals("cpf")) {
            filtroString = " WHERE CM.sisPessoa.documento LIKE :descricaoPesquisa ";
        } else if (porPesquisa.equals("codigo")) {
            filtroString = " WHERE CM.id = " + descricaoPesquisa;
        } else if (porPesquisa.equals("rg")) {
            filtroString = " WHERE UPPER(CM.sisPessoa.rg) LIKE :descricaoPesquisa ";
        } else if (porPesquisa.equals("socio")) {
            filtroString = " WHERE UPPER(CM.pessoa.nome) LIKE :descricaoPesquisa ";
        } else if (porPesquisa.equals("socioCPF")) {
            filtroString = " WHERE UPPER(CM.pessoa.documento) LIKE :descricaoPesquisa ";
        } else if (porPesquisa.equals("todos")) {
            DataHoje dh = new DataHoje();
            String dataAntiga = dh.decrementarMeses(1, DataHoje.data());
            filtroString = " WHERE CM.dtEmissao >= '" + dataAntiga + "' ";
        }
        String queryString = " SELECT CM FROM ConviteMovimento AS CM " + (filtroString) + " ORDER BY CM.dtEmissao DESC ";
        try {
            Query qry = getEntityManager().createQuery(queryString);
            if (!porPesquisa.equals("") && !porPesquisa.equals("todos") && !porPesquisa.equals("codigo")) {
                if (comoPesquisa.equals("Inicial")) {
                    qry.setParameter("descricaoPesquisa", "" + descricaoPesquisa.toUpperCase() + "%");
                } else if (comoPesquisa.equals("Parcial")) {
                    qry.setParameter("descricaoPesquisa", "%" + descricaoPesquisa.toUpperCase() + "%");
                }
            }
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
    public boolean limiteConvitePorSocio(int quantidadeConvites, int quantidadeDias, int idPessoaSocio) {
        try {
            String queryString = ""
                    + "     SELECT COUNT(*) AS qtde "
                    + "       FROM conv_movimento "
                    + "      WHERE id_pessoa = " + idPessoaSocio
                    + "        AND dt_emissao >= (NOW() - INTERVAL '" + quantidadeDias + "' DAY)";
            Query query = getEntityManager().createNativeQuery(queryString);
            List<List> list = query.getResultList();
            if (!list.isEmpty()) {
                int qtde = Integer.parseInt((list.get(0)).get(0).toString());
                if (quantidadeConvites > qtde) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return true;
    }

    @Override
    public boolean limiteConviteConvidado(int quantidadeConvites, int quantidadeDias, int idSisPessoa) {
        try {
            String queryString = ""
                    + "     SELECT count(*) AS qtde "
                    + "       FROM conv_movimento "
                    + "      WHERE id_sis_pessoa = " + idSisPessoa
                    + "        AND dt_emissao >= (NOW() - INTERVAL '" + quantidadeDias + "' DAY)";
            Query query = getEntityManager().createNativeQuery(queryString);            
            List<List> list = query.getResultList();
            if (!list.isEmpty()) {
                int qtde = Integer.parseInt((list.get(0)).get(0).toString());
                if (quantidadeConvites > qtde) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return true;
    }
    
    @Override
    public boolean socio(SisPessoa s) {
        try {
//          ##################
//          #     CAMPOS     #
//          ##################
//          # p.ds_nome      #
//          # dt_nascimento  #
//          # p.ds_documento #
//          # f.ds_rg        #
//          ##################
            String nome = s.getNome().toUpperCase();
            String rg = s.getRg().toUpperCase();
            String documento = s.getDocumento();
            String nascimento = s.getNascimento();
            String queryString = ""
               +"        SELECT *                                                                       "
               +"          FROM soc_socios_vw AS s                                                      "
               +"    INNER JOIN pes_pessoa AS p ON p.id = s.codsocio                                    "  
               +"    INNER JOIN pes_fisica AS f ON f.id_pessoa = p.id                                   "
               +"         WHERE (UPPER(p.ds_nome) = '"+nome+"' AND dt_nascimento = '"+nascimento+"')    "
               +"            OR (UPPER(f.ds_rg) = '"+rg+"')                                             "
               +"            OR (p.ds_documento = '"+documento+"')                                      "
               +"           AND inativacao IS NULL                                                      ";
            
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
