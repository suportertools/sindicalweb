package br.com.rtools.arrecadacao.dao;

import br.com.rtools.arrecadacao.Rais;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RaisDao extends DB {

    public boolean existeCadastroAno(Rais r) {
        try {
            String queryString = "SELECT id FROM arr_rais WHERE nr_ano_base = " + r.getAnoBase() + " AND id_sis_pessoa = " + r.getSisPessoa().getId() + " AND id_empresa = " + r.getEmpresa().getId() + " AND dt_admissao = '" + r.getAdmissaoString() + "'";
            Query q = getEntityManager().createNativeQuery(queryString);
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public List<Rais> pesquisa(String descricaoPesquisa, String tipoPesquisa, String comoPesquisa) {

        String filtroString = "";
        if (tipoPesquisa.equals("nome")) {
            filtroString = " WHERE UPPER(R.sisPessoa.nome) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("cpf")) {
            filtroString = " WHERE R.sisPessoa.documento LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("empresa")) {
            filtroString = " WHERE UPPER(R.empresa.pessoa.nome) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("cnpj")) {
            filtroString = " WHERE R.empresa.pessoa.documento LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("data")) {
            filtroString = " WHERE R.emissao = '" + DataHoje.livre(DataHoje.converte(descricaoPesquisa), "yyyy-MM-dd") + "'";
        } else if (tipoPesquisa.equals("profissao")) {
            filtroString = " WHERE UPPER(R.profissao.profissao) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("todos")) {
            DataHoje dh = new DataHoje();
            filtroString = " WHERE R.emissao >= '" + DataHoje.data() + "' ";
        }
        String queryString = " SELECT R FROM Rais AS R " + (filtroString) + " ORDER BY R.emissao DESC ";
        try {
            Query qry = getEntityManager().createQuery(queryString);
            if (!descricaoPesquisa.equals("") && !tipoPesquisa.equals("todos") && !tipoPesquisa.equals("data")) {
                if (comoPesquisa.equals("Inicial")) {
                    qry.setParameter("descricaoPesquisa", "" + descricaoPesquisa.toUpperCase() + "%");
                } else if (comoPesquisa.equals("Parcial")) {
                    qry.setParameter("descricaoPesquisa", "%" + descricaoPesquisa.toUpperCase() + "%");
                }
            }
            qry.setMaxResults(150);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List filtroRelatorio(Relatorios r, String ano, String emissaoInicial, String emissaoFinal, Integer idEmpresa, Integer idSisPessoa, String inProfissao, String faixaSalarialInicial, String faixaSalarialFinal, String inRaca, String inClassificaoEconomica, String inCidades, String inSexo, String order) {
        List listWhere = new ArrayList();
        String queryString = ""
                + "       SELECT r.nr_ano_base as ano_base,                               " // 0
                + "              r.is_alvara,                                             " // 1
                + "              r.dt_emissao,                                            " // 2
                + "              r.is_empregado_filiado,                                  " // 3
                + "              r.dt_emissao,                                            " // 4
                + "              r.ds_pis,                                                " // 5
                + "              r.ds_motivo_afastamento,                                 " // 6
                + "              r.ds_carteira,                                           " // 7
                + "              r.ds_serie,                                              " // 8
                + "              r.nr_ctps,                                               " // 9
                + "              r.nr_salario,                                            " // 10
                + "              r.dt_admissao,                                           " // 11
                + "              r.ds_funcao,                                             " // 12
                + "              r.dt_afastamento,                                        " // 13
                + "              r.nr_carga_horaria,                                      " // 14
                + "              r.ds_observacao,                                         " // 15
                + "              rc.ds_descricao  AS pes_raca,                            " // 16
                + "              id_indicador_alvara,                                     " // 17
                + "              sp.ds_nome       AS empregado,                           " // 18
                + "              td.ds_descricao  AS deficiencia,                         " // 19
                + "              pj.ds_documento  AS cnpj,                                " // 20
                + "              pj.ds_nome       AS Empresa,                             " // 21
                + "              esc.ds_descricao AS escolaridade,                        " // 22
                + "              resp.ds_nome     AS responsavel,                         " // 23
                + "              nac.ds_descricao AS nacionalidade,                       " // 24
                + "              pf.ds_profissao  AS profissao,                           " // 25
                + "              rem.ds_descricao AS tipo_remuneracao,                    " // 26
                + "              cl.ds_descricao  AS classificacao_economica,             " // 27
                + "              cid.ds_cidade    AS cidade,                              " // 28
                + "              sp.ds_sexo,                                              " // 29
                + "              sp.dt_nascimento as nascimento,                          " // 30
                + "              func_idade(dt_nascimento, current_date) as idade         " // 31
                + "         FROM arr_rais AS r                                            "
                + "    LEFT JOIN pes_raca             AS rc          ON rc.id            = r.id_raca                            "
                + "   INNER JOIN sis_pessoa           AS sp          ON sp.id            = r.id_sis_pessoa                      "
                + "    LEFT JOIN pes_tipo_deficiencia AS td          ON td.id            = r.id_tipo_deficiencia                "
                + "   INNER JOIN pes_juridica         AS j           ON j.id             = r.id_empresa                         "
                + "   INNER JOIN pes_pessoa           AS pj          ON pj.id            = j.id_pessoa                          "
                + "   INNER JOIN pes_pessoa_endereco  AS pend        ON pend.id_pessoa   = pj.id AND pend.id_tipo_endereco = 2  "
                + "   INNER JOIN end_endereco         AS ende        ON ende.id          = pend.id_endereco                     "
                + "   INNER JOIN end_cidade           AS cid         ON cid.id           = ende.id_cidade                       "
                + "    LEFT JOIN pes_escolaridade     AS esc         ON esc.id           = r.id_escolaridade                    "
                + "    LEFT JOIN pes_pessoa           AS resp        ON resp.id          = r.id_responsavel_cadastro            "
                + "    LEFT JOIN pes_nacionalidade    AS nac         ON nac.id           = r.id_nacionalidade                   "
                + "    LEFT JOIN pes_profissao        AS pf          ON pf.id            = r.id_profissao                       "
                + "    LEFT JOIN fin_tipo_remuneracao AS rem         ON rem.id           = r.id_tipo_remuneracao                "
                + "    LEFT JOIN pes_classificacao_economica AS cl   ON cl.id            = r.id_classificacao_economica         ";
        if (!ano.isEmpty()) {
            listWhere.add("r.nr_ano_base = " + ano);
        }
        if (!emissaoInicial.isEmpty() && !emissaoFinal.isEmpty()) {
            listWhere.add("r.dt_emissao BETWEEN '" + emissaoInicial + "' AND '" + emissaoFinal + "'");
        } else if (!emissaoFinal.isEmpty()) {
            listWhere.add("r.dt_emissao = '" + emissaoInicial + "'");
        } else if (!emissaoFinal.isEmpty()) {
            listWhere.add("r.dt_emissao = '" + emissaoFinal + "'");
        }
        if (faixaSalarialInicial != null && faixaSalarialFinal != null) {
            Float fsi = Moeda.converteUS$(faixaSalarialInicial);
            Float fsf = Moeda.converteUS$(faixaSalarialFinal);
            listWhere.add("r.nr_salario BETWEEN " + fsi + " AND " + fsf + "");
        } else if (faixaSalarialInicial != null) {
            Float fsi = Moeda.converteUS$(faixaSalarialInicial);
            listWhere.add("r.nr_salario = '" + fsi + "'");
        } else if (faixaSalarialFinal != null) {
            Float fsf = Moeda.converteUS$(faixaSalarialFinal);
            listWhere.add("r.nr_salario = '" + fsf + "'");
        }
        if (idEmpresa != null) {
            listWhere.add("j.id = " + idEmpresa);
        }
        if (idSisPessoa != null) {
            listWhere.add("sp.id = " + idSisPessoa);
        }
        if (inProfissao != null) {
            listWhere.add("pf.id IN(" + inProfissao + ")");
        }
        if (inRaca != null) {
            listWhere.add("rc.id IN(" + inRaca + ")");
        }
        if (inClassificaoEconomica != null) {
            listWhere.add("cl.id IN(" + inClassificaoEconomica + ")");
        }
        if (inCidades != null) {
            listWhere.add("cid.id IN(" + inCidades + ")");
        }
        if (inSexo != null && !inSexo.isEmpty()) {
            listWhere.add("sp.ds_sexo LIKE '" + inSexo + "'");
        }
        if (!listWhere.isEmpty()) {
            queryString += " WHERE ";
            for (int i = 0; i < listWhere.size(); i++) {
                if (i > 0) {
                    queryString += " AND ";
                }
                queryString += listWhere.get(i).toString();
            }
        }
        if (r != null && order.isEmpty()) {
            if (!r.getQryOrdem().isEmpty()) {
                queryString += " ORDER BY " + r.getQryOrdem();
            }
        } else if (!order.isEmpty()) {
            queryString += " ORDER BY " + order;
        }
        try {
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List filtroRelatorioNaoEnviadas(Relatorios r, String ano, Integer idEmpresa, Integer idEscritorio, String inCidades, String order, String tipo, boolean escritorios) {
        List listWhere = new ArrayList();
        String queryGroup = "";
        String queryString = "SELECT ";
        if (escritorios) {
            queryString += ""
                    + " C_pessoa.ds_nome                    AS escritorio,              " // 0
                    + " C_pessoa.ds_telefone1               AS esc_telefone,            " // 1
                    + " C_pessoa.ds_email1                  AS esc_email,               " // 2
                    + " C.id,                                                           " // 3
                    + " COUNT(*),                                                       " // 4
                    + " C_logradouro.ds_descricao           AS escritorio_logradouro,   " // 5
                    + " C_descricao_endereco.ds_descricao   AS escritorio_endereco,     " // 6
                    + " PEC.ds_complemento                  AS escritorio_complemento,  " // 7
                    + " C_bairro.ds_descricao               AS escritorio_bairro,       " // 8
                    + " C_cidade.ds_cidade                  AS escritorio_cidade,       " // 9
                    + " C_cidade.ds_uf                      AS escritorio_uf,           " // 10
                    + " C_endereco.ds_cep                   AS escritorio_cep,          " // 11
                    + " PEC.ds_numero                       AS escritorio_numero        "; //12

            queryGroup = " GROUP BY C_pessoa.ds_nome,                   "
                    + "             C_pessoa.ds_telefone1,              "
                    + "             C_pessoa.ds_email1,                 "
                    + "             C.id,                               "
                    + "             C_logradouro.ds_descricao,          "
                    + "             C_descricao_endereco.ds_descricao,  "
                    + "             PEC.ds_complemento,                 "
                    + "             C_bairro.ds_descricao,              "
                    + "             C_cidade.ds_cidade,                 "
                    + "             C_cidade.ds_uf,                     "
                    + "             C_endereco.ds_cep,                  "
                    + "             PEC.ds_numero                       ";

        } else {
            queryString += ""
                    + "                  P.ds_documento                     AS cnpj,                    " // 0
                    + "                  P.ds_nome                          AS empresa,                 " // 1
                    + "                  CO.ds_descricao                    AS convencao,               " // 2
                    + "                  GC.ds_descricao                    AS grupo,                   " // 3
                    + "                  P.ds_telefone1                     AS telefone,                " // 4
                    + "                  J_logradouro.ds_descricao          AS logradouro,              " // 5
                    + "                  J_descricao_endereco.ds_descricao  AS endereco,                " // 6
                    + "                  PE.ds_complemento                  AS complemento,             " // 7
                    + "                  J_bairro.ds_descricao              AS bairro,                  " // 8
                    + "                  J_cidade.ds_cidade                 AS cidade,                  " // 9
                    + "                  J_cidade.ds_uf                     AS uf,                      " // 10
                    + "                  E.ds_cep                           AS cep,                     " // 11
                    + "                  P.ds_email1                        AS email,                   " // 12
                    + "                  C_pessoa.ds_nome                   AS escritorio,              " // 13
                    + "                  C_pessoa.ds_telefone1              AS escritorio_telefone,     " // 14
                    + "                  C_pessoa.ds_email1                 AS escritorio_email,        " // 15
                    + "                  C_logradouro.ds_descricao          AS escritorio_logradouro,   " // 16
                    + "                  C_descricao_endereco.ds_descricao  AS escritorio_endereco,     " // 17
                    + "                  PEC.ds_complemento                 AS escritorio_complemento,  " // 18
                    + "                  C_bairro.ds_descricao              AS escritorio_bairro,       " // 19
                    + "                  C_cidade.ds_cidade                 AS escritorio_cidade,       " // 20
                    + "                  C_cidade.ds_uf                     AS escritorio_uf,           " // 21
                    + "                  C_endereco.ds_cep                  AS escritorio_cep,          " // 22
                    + "                  J.id,                                                          " // 23
                    + "                  C.id                               AS esc_id,                  " // 24
                    + "                  PE.ds_numero                       AS numero,                  " // 25
                    + "                  PEC.ds_numero                      AS escritorio_numero        ";// 26
        }
        queryString += ""
                + "         FROM pes_juridica           AS J                                                                                    "
                + "   INNER JOIN pes_pessoa_endereco    AS PE                   ON PE.id_pessoa     = J.id_pessoa AND PE.id_tipo_endereco = 5   "
                + "   INNER JOIN end_endereco           AS E                    ON E.id             = PE.id_endereco                            "
                + "   INNER JOIN pes_pessoa             AS P                    ON P.id             = J.id_pessoa                               "
                + "   INNER JOIN end_cidade             AS J_cidade             ON J_cidade.id      = E.id_cidade                               "
                + "   INNER JOIN end_logradouro         AS J_logradouro         ON J_logradouro.id  = E.id_logradouro                           "
                + "   INNER JOIN end_descricao_endereco AS J_descricao_endereco ON J_descricao_endereco.id = E.id_descricao_endereco            "
                + "   INNER JOIN end_bairro             AS J_bairro             ON J_bairro.id      = E.id_bairro                               "
                + "   INNER JOIN pes_tipo_documento     AS T                    ON T.id             = P.id_tipo_documento                       "
                + "   INNER JOIN arr_cnae_convencao     AS CCON                 ON CCON.id_cnae     = J.id_cnae                                 "
                + "   INNER JOIN arr_grupo_cidades      AS GCS                  ON GCS.id_cidade    = E.id_cidade                               "
                + "   INNER JOIN arr_convencao_cidade   AS CC                   ON CC.id_grupo_cidade    = gcs.id_grupo_cidade                  "
                + "                                                                AND CC.id_convencao   = CCON.id_convencao                    "
                + "   INNER JOIN arr_convencao          AS CO                   ON CO.id            = CC.id_convencao                           "
                + "   INNER JOIN arr_grupo_cidade       AS GC                   ON GC.id            = CC.id_grupo_cidade                        "
                + "    LEFT JOIN pes_juridica           AS C                    ON C.id             = J.id_contabilidade                        "
                + "    LEFT JOIN pes_pessoa             AS C_pessoa             ON C_pessoa.id      = C.id_pessoa                               "
                + "    LEFT JOIN pes_pessoa_endereco    AS PEC                  ON PEC.id_pessoa    = C.id_pessoa AND PEC.id_tipo_endereco = 2  "
                + "    LEFT JOIN end_endereco           AS C_endereco           ON C_endereco.id    = PEC.id_endereco                           "
                + "    LEFT JOIN end_cidade             AS C_cidade             ON C_cidade.id      = C_endereco.id_cidade                      "
                + "    LEFT JOIN end_logradouro         AS C_logradouro         ON C_logradouro.id  = C_endereco.id_logradouro                  "
                + "    LEFT JOIN end_descricao_endereco AS C_descricao_endereco ON C_descricao_endereco.id = C_endereco.id_descricao_endereco   "
                + "    LEFT JOIN end_bairro             AS C_bairro             ON C_bairro.id      = C_endereco.id_bairro                      "
                + "    LEFT JOIN arr_rais               AS R                    ON R.id_empresa     = J.id                                      ";
        if (!ano.isEmpty()) {
            // Filtro por Ano Base
            queryString += " AND R.nr_ano_base = " + ano;
        }
        if (idEmpresa != null && idEmpresa != -1) {
            listWhere.add(" J.id = " + idEmpresa);
            switch (tipo) {
                case "yes_email":
                    listWhere.add(" (P.ds_email1 IS NOT NULL AND P.ds_email1 <> '') ");
                    break;
                case "no_email":
                    listWhere.add(" P.ds_email1 = '' ");
                    break;
            }
        } else if (idEmpresa != null && idEmpresa == -1) {
            switch (tipo) {
                case "yes_email":
                    listWhere.add(" (P.ds_email1 IS NOT NULL AND P.ds_email1 <> '') ");
                    break;
                case "no_email":
                    listWhere.add(" P.ds_email1 = '' ");
                    break;
            }

        }
        if (idEscritorio != null && idEscritorio != -1) {
            listWhere.add(" C.id = " + idEscritorio);
            switch (tipo) {
                case "yes_email":
                    listWhere.add(" (C_pessoa.ds_email1 IS NOT NULL AND C_pessoa.ds_email1 <> '') ");
                    break;
                case "no_email":
                    listWhere.add(" C_pessoa.ds_email1 = '' ");
                    break;
            }
        } else if (idEscritorio != null && idEscritorio == -1) {
            switch (tipo) {
                case "yes_email":
                    listWhere.add(" (C_pessoa.ds_email1 IS NOT NULL AND C_pessoa.ds_email1 <> '') ");
                    break;
                case "no_email":
                    listWhere.add(" C_pessoa.ds_email1 = '' ");
                    break;
            }

        }
        if (inCidades != null) {
            listWhere.add(" J_cidade.id IN(" + inCidades + ") ");
        }
        listWhere.add(" R.id IS NULL ");
        listWhere.add(" J.id NOT IN (SELECT id_juridica FROM arr_contribuintes_inativos) ");
        if (!listWhere.isEmpty()) {
            queryString += " WHERE ";
            for (int i = 0; i < listWhere.size(); i++) {
                if (i > 0) {
                    queryString += " AND ";
                }
                queryString += listWhere.get(i).toString();
            }
        }
        if (!queryGroup.isEmpty()) {
            queryString += queryGroup + " ORDER BY C_pessoa.ds_nome, count(*) ";
        }
        if (r != null && order.isEmpty()) {
            if (!r.getQryOrdem().isEmpty()) {
                queryString += " ORDER BY " + r.getQryOrdem();
            }
        } else if (!order.isEmpty()) {
            queryString += " ORDER BY " + order;
        }
        try {
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List<Cidade> pesquisaTodasCidades() {
        String queryString = ""
                + "       SELECT C.*                                                                                            "
                + "         FROM arr_rais AS R                                                                                  "
                + "   INNER JOIN pes_juridica         AS j           ON j.id             = R.id_empresa                         "
                + "   INNER JOIN pes_pessoa           AS pj          ON pj.id            = j.id_pessoa                          "
                + "   INNER JOIN pes_pessoa_endereco  AS pend        ON pend.id_pessoa   = pj.id AND pend.id_tipo_endereco = 2  "
                + "   INNER JOIN end_endereco         AS ende        ON ende.id          = pend.id_endereco                     "
                + "   INNER JOIN end_cidade           AS C           ON C.id             = ende.id_cidade                       "
                + "     ORDER BY C.ds_cidade ASC                                                                                ";
        try {
            Query query = getEntityManager().createNativeQuery(queryString, Cidade.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List<Profissao> pesquisaTodasProfissoes() {
        String queryString = ""
                + "       SELECT P.*                                            "
                + "         FROM arr_rais AS R                                  "
                + "    LEFT JOIN pes_profissao AS P ON P.id = R.id_profissao    "
                + "        ORDER BY P.ds_profissao ASC                          ";
        try {
            Query query = getEntityManager().createNativeQuery(queryString, Profissao.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
