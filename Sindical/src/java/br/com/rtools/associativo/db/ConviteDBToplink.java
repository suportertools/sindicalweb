package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConviteAutorizaCortesia;
import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.associativo.ConviteSuspencao;
import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.seguranca.Usuario;
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
        return pesquisaConviteMovimento(descricaoPesquisa, porPesquisa, comoPesquisa, true);
    }

    public List pesquisaConviteMovimento(String descricaoPesquisa, String porPesquisa, String comoPesquisa, boolean ativo) {
        if (!ativo) {
            ativo = false;
        }
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
        if (ativo) {
            if (filtroString.equals("")) {
                filtroString = " WHERE CM.ativo = true";
            } else {
                filtroString += " AND CM.ativo = true";
            }
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
            String documento = s.getDocumento().toUpperCase();
            String nascimento = s.getNascimento();
            String queryString = ""
                    + "                SELECT *                                                                                                                                                                                                                                 "
                    + "                 FROM soc_socios_vw AS s                                                                                                                                                                                                                 "
                    + "            INNER JOIN pes_pessoa AS p ON p.id = s.codsocio                                                                                                                                                                                              "
                    + "            INNER JOIN pes_fisica AS f ON f.id_pessoa = p.id                                                                                                                                                                                             "
                    + "                WHERE (p.ds_documento = '"+documento+"' AND UPPER(p.ds_nome) = '"+nome+"' AND dt_nascimento = '"+nascimento+"' AND UPPER(f.ds_rg) = '"+rg+"' AND inativacao IS NULL AND p.ds_nome <> '' AND p.ds_documento <> '' AND f.ds_rg <> '')      "
                    + "                   OR (UPPER(p.ds_nome) = '"+nome+"' AND dt_nascimento = '"+nascimento+"' AND UPPER(f.ds_rg) = '"+rg+"' AND inativacao IS NULL AND p.ds_nome <> '' AND f.ds_rg <> '')                                                                    "
                    + "                   OR (UPPER(p.ds_nome) = '"+nome+"' AND dt_nascimento = '"+nascimento+"' AND inativacao IS NULL AND p.ds_nome <> '')                                                                                                                    "
                    + "                   OR (UPPER(p.ds_nome) = '"+nome+"' AND UPPER(f.ds_rg) = '"+rg+"' AND inativacao IS NULL AND p.ds_nome <> '')                                                                                                                           "
                    + "                   OR (UPPER(f.ds_rg) = '"+rg+"' AND inativacao IS NULL AND p.ds_nome <> '' AND p.ds_nome <> '' AND f.ds_rg <> '')                                                                                                                       "
                    + "                   OR (p.ds_documento = '"+documento+"' AND inativacao IS NULL AND p.ds_documento <> '')";
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

    @Override
    public List<Usuario> listaUsuariosDisponiveis() {
        try {
            Query query = getEntityManager().createQuery("SELECT CM.usuario FROM ConviteMovimento AS CM GROUP BY CM.usuario ");
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
    public List filtroRelatorio(
            int idSisPessoa,
            int idPessoa,
            int idDiretor,
            int idOperador,
            String emissaInicial,
            String emissaFinal,
            String validadeInicial,
            String validadeFinal,
            String cortesia,
            String obs,
            Relatorios r
    ) {
        List listString = new ArrayList();
        List listOrder = new ArrayList();
        boolean emissaoIndividual = false;
        boolean validadeIndividual = false;
        if (idSisPessoa != 0) {
            listString.add(" c.id_sis_pessoa = " + idSisPessoa);
            listOrder.add(" sp.ds_nome ASC ");
        }
        if (idPessoa != 0) {
            listString.add(" c.id_pessoa = " + idPessoa);
            listOrder.add(" p.ds_nome ASC ");
        }
        if (idDiretor != 0) {
            listString.add(" c.id_autoriza_cortesia = " + idDiretor);
            listOrder.add(" pc.ds_nome ASC ");
        }
        if (idOperador != 0) {
            listString.add(" c.id_usuario = " + idOperador);
            listOrder.add(" pu.ds_nome ASC ");
        }
        if (!emissaInicial.isEmpty() && !emissaFinal.isEmpty()) {
            emissaoIndividual = true;
            listString.add(" c.dt_emissao BETWEEN '" + emissaInicial + "' AND '" + emissaFinal + "' ");
            listOrder.add(" c.dt_emissao DESC ");
        }
        if (!emissaoIndividual) {
            if (!emissaInicial.isEmpty()) {
                listString.add(" c.dt_emissao = '" + emissaInicial + "' ");
                listOrder.add(" c.dt_emissao DESC ");
            }
        }
        if (!validadeInicial.isEmpty() && !validadeFinal.isEmpty()) {
            validadeIndividual = true;
            listString.add(" c.dt_validade BETWEEN '" + validadeInicial + "' AND '" + validadeFinal + "' ");
            listOrder.add(" c.dt_validade DESC ");
        }
        if (!validadeIndividual) {
            if (!validadeInicial.isEmpty()) {
                listString.add(" c.dt_validade = '" + validadeInicial + "' ");
                listOrder.add(" c.dt_validade DESC ");
            }
        }
        if (!cortesia.isEmpty()) {
            if (cortesia.equals("sim")) {
                listString.add(" c.is_cortesia = true ");
            } else if (cortesia.equals("nao")) {
                listString.add(" c.is_cortesia = false ");
            }
            listOrder.add(" c.is_cortesia ");
        }
        String filtroString = "";
        if (!listString.isEmpty()) {
            filtroString = " WHERE ";
            for (int i = 0; i < listString.size(); i++) {
                if (i == 0) {
                    filtroString += " " + listString.get(i).toString();
                } else {
                    filtroString += " AND " + listString.get(i).toString();
                }
            }
        }
        String filtroOrder = " ";
        if (!listOrder.isEmpty()) {
            filtroOrder = " ORDER BY  ";
            for (int i = 0; i < listOrder.size(); i++) {
                if (i == 0) {
                    filtroOrder += " " + listOrder.get(i).toString();
                } else {
                    filtroOrder += " , " + listOrder.get(i).toString();
                }
            }
        }
        if (r != null) {
            if (!r.getQryOrdem().equals("")) {
                filtroOrder = " ORDER BY " + r.getQryOrdem();
            }
        }

        String queryString = ""
                + "      SELECT                                                             "
                + "            sp.ds_nome    AS convidado,                                  " //  0 - CONVIDADO
                + "            p.ds_nome     AS socio,                                      " //  1 - SOCIO
                + "            pc.ds_nome    AS diretor,                                    " //  2 - DIRETOR
                + "            pu.ds_nome    AS operador,                                   " //  3 - OPERADOR
                + "            to_char(c.dt_emissao, 'DD/MM/YYYY')  AS emissao,             " //  4 - EMISSAO
                + "            to_char(c.dt_validade, 'DD/MM/YYYY') AS validade,            " //  5 - VALIDADE
                + "            to_char(b.dt_baixa, 'DD/MM/YYYY')    AS dataPagto,           " //  6 - DATA PAGTO
                + "            m.nr_valor    AS valor,                                      " //  7 - VALOR
                + "            m.nr_valor_baixa AS valor_pago,                              " //  8 - VALOR PAGO
                + "            c.is_cortesia    AS cortesia,                                " //  9 - CORTESIA
                + "            c.ds_obs         AS obs                                      " // 10 - OBS
                + "       FROM conv_movimento AS c                                          "
                + " INNER JOIN sis_pessoa       AS sp   ON sp.id = c.id_sis_pessoa              "
                + " INNER JOIN pes_pessoa       AS p    ON p.id = id_pessoa                     "
                + " INNER JOIN seg_usuario      AS u    ON u.id = c.id_usuario                  "
                + " INNER JOIN pes_pessoa       AS pu   ON pu.id = u.id_pessoa                  "
                + "  LEFT JOIN pes_pessoa       AS pc   ON pc.id = c.id_autoriza_cortesia       "
                + "  LEFT JOIN fin_lote         AS l    ON l.id_evt = c.id_evt                  "
                + "  LEFT JOIN fin_movimento    AS m    ON m.id_lote = l.id                     "
                + "  LEFT JOIN fin_baixa        AS b    ON b.id = m.id_baixa                    "
                + "" + filtroString
                + "" + filtroOrder;
        try {
            Query query = getEntityManager().createNativeQuery(queryString);
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
    public List<ConviteAutorizaCortesia> listaConviteAutorizaCortesia(boolean is_ativo){
        try{
            String text = "";
            
            if (is_ativo){
                text = "SELECT cac.* FROM conv_autoriza_cortesia cac WHERE cac.is_ativo = TRUE";
            }else{
                text = "SELECT cac.* FROM conv_autoriza_cortesia cac";
            }
            
            Query query = getEntityManager().createNativeQuery(text, ConviteAutorizaCortesia.class);
            return query.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List<ConviteServico> listaConviteServico(Integer id_servico){
        try{
            Query query = getEntityManager().createQuery("SELECT cs FROM ConviteServico cs WHERE cs.servicos.id = "+id_servico);
            return query.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }

}
