package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.Socios;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SociosDao extends DB {

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param id Pessoa do serviço pessoa
     * @return
     */
    public Socios pesquisaTitularPorDependente(Integer id) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :id AND S.matriculaSocios.dtInativo IS NULL AND S.servicoPessoa.ativo = true");
            query.setParameter("id", id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Socios) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Retorna o sócio titular
     *
     * @param idPessoa da Pessoa
     * @return
     */
    public Socios pesquisaTitularPorPessoa(Integer idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.matriculaSocios.titular.id = :pessoa AND S.matriculaSocios.titular.id = S.servicoPessoa.pessoa.id AND S.matriculaSocios.dtInativo IS NULL AND S.servicoPessoa.ativo = true");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Socios) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param pessoa (id) Pessoa
     * @return
     */
    public List listaPorPessoa(Integer pessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param pessoa (id) Pessoa
     * @param todos
     * @return
     */
    public List listaPorPessoa(Integer pessoa, Boolean todos) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param idServicoPessoa Serviço pessoa
     * @return
     */
    public Socios pesquisaSocioPorServicoPessoa(Integer idServicoPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.id = :id");
            query.setParameter("id", idServicoPessoa);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Socios) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Retorna o sócios por pessoa empresa
     *
     * @param idJuridica Serviço pessoa
     * @return
     */
    public List pesquisaSocioPorEmpresa(Integer idJuridica) {
        try {
            String queryString = ""
                    + "     SELECT S.nome           AS nome,                    " // 0 - NOME
                    + "            S.matricula      AS matricula,               " // 1 - MATRÍCULA
                    + "            S.categoria      As categoria,               " // 2 - CATEGORIA
                    + "            S.filiacao       As filiacao,                " // 3 - FILIAÇÃO
                    + "            P.admissao       AS admissao,                " // 4 - ADMISSÃO
                    + "            S.desconto_folha AS desconto_folha           " // 5 - DESCONTO FOLHA
                    + "       FROM soc_socios_vw AS S                           "
                    + " INNER JOIN pes_pessoa_vw AS P ON P.codigo = S.codsocio  "
                    + "      WHERE P.e_id_pessoa = ?                            ";
            Query query = getEntityManager().createNativeQuery(queryString);
            query.setParameter("1", idJuridica);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     * Retorna o sócio inativo por matricula
     *
     * @param idPessoa
     * @param idMatriculaSocios
     * @return
     */
    public Socios pesquisaDependenteInativoPorMatricula(Integer idPessoa, Integer idMatriculaSocios) {
        try {
            Query query = getEntityManager().createQuery(" SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa AND S.matriculaSocios.id = :matriculaSocios AND S.servicoPessoa.ativo = false ");
            query.setParameter("pessoa", idPessoa);
            query.setParameter("matriculaSocios", idMatriculaSocios);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Socios) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Retorna o dependentes por titular e matrícula
     *
     * @param idMatriculaSocios
     * @return
     */
    public List pesquisaDependentePorMatricula(Integer idMatriculaSocios) {
        return pesquisaDependentePorMatricula(idMatriculaSocios, false);
    }

    /**
     * Retorna o dependentes por titular e matrícula
     *
     * @param idMatriculaSocios
     * @param com_titular
     * @return
     */
    public List pesquisaDependentePorMatricula(Integer idMatriculaSocios, Boolean com_titular) {
        try {
            Query query;
            if (com_titular) {
                query = getEntityManager().createQuery(" SELECT S FROM Socios AS S WHERE S.matriculaSocios.id = :matriculaSocios AND S.servicoPessoa.ativo = true AND S.servicoPessoa.pessoa.id <> S.matriculaSocios.titular.id ORDER BY S.servicoPessoa.pessoa.nome ASC");
            } else {
                query = getEntityManager().createQuery(" SELECT S FROM Socios AS S WHERE S.matriculaSocios.id = :matriculaSocios AND S.servicoPessoa.ativo = true ORDER BY S.servicoPessoa.pessoa.nome ASC");
            }
            query.setParameter("matriculaSocios", idMatriculaSocios);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     * Retorna serviços pessoas que pertencem a sócios
     *
     * @param idPessoa
     * @return
     */
    public List listServicoPessoaInSociosByPessoa(Integer idPessoa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT S.servicoPessoa FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa AND S.servicoPessoa.ativo = true ");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

//    public List pesquisaSocios(String descricao, String por, String como) {
//        String textQry = ""
//                + "SELECT     p.codigo,                                         " // 01
//                + "           p.cadastro,                                       " // 02
//                + "           p.nome,                                           " // 03
//                + "           p.cpf,                                            " // 04
//                + "           p.telefone,                                       " // 05
//                + "           p.ds_uf_emissao_rg,                               " // 06
//                + "           p.estado_civil,                                   " // 07
//                + "           p.ctps,                                           " // 08
//                + "           p.pai,                                            " // 09
//                + "           p.sexo,                                           " // 10
//                + "           p.mae,                                            " // 11
//                + "           p.nacionalidade,                                  " // 12
//                + "           p.nit,                                            " // 13
//                + "           p.ds_orgao_emissao_rg,                            " // 14
//                + "           p.ds_pis,                                         " // 15
//                + "           p.ds_serie,                                       " // 16
//                + "           p.dt_aposentadoria,                               " // 17
//                + "           p.ds_naturalidade,                                " // 18
//                + "           p.recadastro,                                     " // 19
//                + "           p.dt_nascimento,                                  " // 20
//                + "           p.dt_foto,                                        " // 21
//                + "           p.ds_rg,                                          " // 22
//                + "           foto,                                             " // 23
//                + "           p.logradouro,                                     " // 24
//                + "           p.endereco,                                       " // 25
//                + "           p.numero,                                         " // 26
//                + "           p.complemento,                                    " // 27
//                + "           p.bairro,                                         " // 28
//                + "           p.cidade,                                         " // 29
//                + "           p.uf,                                             " // 30
//                + "           p.cep,                                            " // 31
//                + "           p.setor,                                          " // 32
//                + "           p.admissao,                                       " // 33
//                + "           p.profissao,                                      " // 34
//                + "           p.fantasia,                                       " // 35
//                + "           p.empresa,                                        " // 36
//                + "           p.cnpj,                                           " // 37
//                + "           p.e_telefone,                                     " // 38
//                + "           p.e_logradouro,                                   " // 39
//                + "           p.e_endereco,                                     " // 40
//                + "           p.e_numero,                                       " // 41
//                + "           p.e_complemento,                                  " // 42
//                + "           p.e_bairro,                                       " // 43
//                + "           p.e_cidade,                                       " // 44
//                + "           p.e_uf,                                           " // 45
//                + "           p.e_cep,                                          " // 46
//                + "           titular,                                          " // 47
//                + "           so.codsocio,                                      " // 48
//                + "           pt.ds_nome as titular,                            " // 49
//                + "           so.parentesco,                                    " // 50
//                + "           so.matricula,                                     " // 51
//                + "           so.categoria,                                     " // 52
//                + "           so.grupo_categoria,                               " // 53
//                + "           so.filiacao,                                      " // 54
//                + "           so.inativacao,                                    " // 55
//                + "           so.votante,                                       " // 56
//                + "           so.grau,                                          " // 57
//                + "           so.nr_desconto,                                   " // 58
//                + "           so.desconto_folha,                                " // 59
//                + "           so.tipo_cobranca,                                 " // 60
//                + "           so.cod_tipo_cobranca,                             " // 62
//                + "           p.telefone2,                                      " // 62
//                + "           p.telefone3,                                      " // 63           
//                + "           p.email,                                          " // 64
//                + "           PC.ds_nome,                                       " // 65
//                + "           PJC.ds_contato,                                   " // 66
//                + "           PC.ds_telefone1                                   " // 67
//                + "      FROM pes_pessoa_vw      AS p                           "
//                + "INNER JOIN soc_socios_vw      AS so   ON so.codsocio     = p.codigo              "
//                + "INNER JOIN pes_juridica_vw    AS sind ON sind.id_pessoa  = 1                     "
//                + "INNER JOIN pes_pessoa         AS pt   ON pt.id           = so.titular            "
//                + " LEFT JOIN pes_juridica       AS J    ON J.id            = P.e_id                "
//                + " LEFT JOIN pes_juridica       AS PJC  ON PJC.id          = J.id_contabilidade    "
//                + " LEFT JOIN pes_pessoa         AS PC   ON PC.id           = PJC.id_pessoa         ";
//        String filtro = "";
//        switch (por) {
//            case "matricula":
//                filtro += " so.matricula = " + descricao;
//                break;
//            case "cidade":
//                filtro += " AND p.id_cidade IN(" + descricao + ")";
//                break;
//            case "empresa":
//                filtro += " AND p.e_id_cidade IN(" + descricao + ")";
//                break;
//            case "email":
//                filtro += " AND p.email = '%" + descricao + "%'";
//                break;
//            case "telefone":
//                filtro += " AND p.telefone = '" + descricao + "'";
//                break;
//        }
//
//        try {
//            String queryString = textQry + filtro;
//            Query query = getEntityManager().createNativeQuery(queryString);
//            List list = query.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//        }
//        return new ArrayList();
//    }
    public List pesquisaSocios(String descricao, String por, String como) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        try {
            String textQuery = "";

            descricao = AnaliseString.normalizeLower(descricao);
            descricao = (como.equals("I") ? descricao + "%" : "%" + descricao + "%");

            String field = "";

            if (por.equals("nome")) {
                field = "p.ds_nome";
            }
            if (por.equals("email1")) {
                field = "p.ds_email1";
            }
            if (por.equals("email2")) {
                field = "p.ds_email2";
            }
            if (por.equals("rg")) {
                field = "f.ds_rg";
            }
            if (por.equals("cpf")) {
                field = "p.ds_documento";
            }

            int maxResults = 1000;
            if (descricao.length() == 1) {
                maxResults = 50;
            } else if (descricao.length() == 2) {
                maxResults = 150;
            } else if (descricao.length() == 3) {
                maxResults = 500;
            }

            switch (por) {
                case "endereco":
                    textQuery
                            = "      SELECT S.*                                                                                 "
                            + "        FROM pes_pessoa_endereco pesend                                                          "
                            + "  INNER JOIN pes_pessoa pes                      ON pes.id = pesend.id_pessoa                    "
                            + "  INNER JOIN soc_socios_vw AS SVW                ON SVW.codsocio = PES.id                        "
                            + "  INNER JOIN soc_socios AS S                     ON S.codsocio = PES.id                          "
                            + "  INNER JOIN end_endereco ende                   ON ende.id = pesend.id_endereco                 "
                            + "  INNER JOIN end_cidade cid                      ON cid.id = ende.id_cidade                      "
                            + "  INNER JOIN end_descricaoricao_endereco enddes  ON enddes.id = ende.id_descricaoricao_endereco  "
                            + "  INNER JOIN end_bairro bai                      ON bai.id = ende.id_bairro                      "
                            + "  INNER JOIN end_logradouro logr                 ON logr.id = ende.id_logradouro                 "
                            + "  INNER JOIN pes_fisica fis                      ON fis.id_pessoa = pes.id                       "
                            + "  WHERE (LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao || ', ' || bai.ds_descricaoricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)) LIKE '%" + descricao + "%' "
                            + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf)) LIKE '%" + descricao + "%'                     "
                            + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao || ', ' || cid.ds_cidade  )) LIKE '%" + descricao + "%'                                         "
                            + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao)) LIKE '%" + descricao + "%'                                                                    "
                            + "     OR LOWER(FUNC_TRANSLATE(enddes.ds_descricaoricao)) LIKE '%" + descricao + "%'                                                                                                "
                            + "     OR LOWER(FUNC_TRANSLATE(cid.ds_cidade)) LIKE '%" + descricao + "%'                                                                                                      "
                            + "     OR LOWER(FUNC_TRANSLATE(ende.ds_cep)) = '" + descricao + "'"
                            + "  ) "
                            + "  AND pesend.id_tipo_endereco = 1 "
                            + "  AND pes.id IN ( "
                            + "         SELECT p2.id FROM fin_servico_pessoa sp "
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                            + "          WHERE sp.is_ativo = TRUE "
                            + "  ) "
                            + "  ORDER BY pes.ds_nome LIMIT " + maxResults;
                    break;
                case "matricula":
                    textQuery
                            = "      SELECT S.*                                                 "
                            + "        FROM pes_fisica      AS F                                "
                            + "  INNER JOIN pes_pessoa      AS P    ON P.id = f.id_pessoa       "
                            + "  INNER JOIN soc_socios_vw   AS SVW  ON SVW.codsocio = PES.id)   "
                            + "  INNER JOIN soc_socios      AS S    ON S.codsocio = PES.id      "
                            + "  WHERE P.id IN (                                                "
                            + "         SELECT p2.id FROM fin_servico_pessoa sp                 "
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa      "
                            + "          INNER JOIN matr_socios ms ON  ms.id = s.id_matricula_socios "
                            + "          WHERE sp.is_ativo = TRUE "
                            + "            AND ms.nr_matricula = " + descricao.replace("%", "")
                            + "    ) "
                            + "  ORDER BY P.ds_nome LIMIT " + maxResults;
                    break;
                case "codigo":
                    textQuery
                            = "      SELECT S.*                                                         "
                            + "        FROM pes_fisica          AS F                                    "
                            + "  INNER JOIN pes_pessoa          AS p        ON P.id = F.id_pessoa       "
                            + "  INNER JOIN soc_socios_vw       AS SVW      ON SVW.codsocio = PES.id    "
                            + "  INNER JOIN soc_socios          AS S        ON S.codsocio = PES.id      "
                            + "  INNER JOIN pes_pessoa_empresa  AS PE       ON f.id = pe.id_fisica      "
                            + "  WHERE pe.ds_codigo LIKE '" + descricao + "'"
                            + "    AND p.id IN ( "
                            + "         SELECT p2.id FROM fin_servico_pessoa sp "
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                            + "          WHERE sp.is_ativo = TRUE "
                            + "    ) "
                            + "  ORDER BY P.ds_nome LIMIT " + maxResults;
                    break;
                default:
                    textQuery
                            = "      SELECT S.*                                                         "
                            + "        FROM pes_fisica         AS F                                     "
                            + "  INNER JOIN pes_pessoa          AS P        ON P.id = F.id_pessoa       "
                            + "  INNER JOIN soc_socios_vw       AS SVW      ON SVW.codsocio = PES.id    "
                            + "  INNER JOIN soc_socios          AS S        ON S.codsocio = PES.id      "
                            + "  WHERE LOWER(FUNC_TRANSLATE(" + field + ")) LIKE '" + descricao + "'"
                            + "    AND p.id IN ( "
                            + "         SELECT p2.id FROM fin_servico_pessoa sp "
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                            + "          WHERE sp.is_ativo = TRUE "
                            + "    ) "
                            + "  ORDER BY P.ds_nome LIMIT " + maxResults;
                    break;
            }

            Query query = getEntityManager().createNativeQuery(textQuery, Socios.class);

            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();

//        Query qry = getEntityManager().createNativeQuery(textQuery);
//
//        List<Vector> result_list = qry.getResultList();
//        List<Object> return_list = new ArrayList<Object>();
//
//        if (!result_list.isEmpty()) {
//            if (result_list.size() > 1) {
//                String listId = "";
//                for (int i = 0; i < result_list.size(); i++) {
//                    if (i == 0) {
//                        listId = result_list.get(i).get(0).toString();
//                    } else {
//                        listId += ", " + result_list.get(i).get(0).toString();
//                    }
//                }
//                return getEntityManager().createQuery("SELECT f FROM Fisica f WHERE f.id IN ( " + listId + " )").getResultList();
//            } else {
//                return getEntityManager().createQuery("SELECT f FROM Fisica f WHERE f.id = " + (Integer) result_list.get(0).get(0)).getResultList();
//            }
//        }
        // return return_list;
    }

}
