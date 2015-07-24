package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.Socios;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
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

    /*
     * Verifica se existe mais de uma pessoa com mais de uma matrícula.
     * Mensagem: Constam a mesma pessoa mais de uma vez na mesma matrícula
     * @return
     */
    public Boolean existPessoasMesmaMatricula() {
        return !listPessoasMesmaMatricula(false, 1).isEmpty();
    }

    /*
     * Traz uma lista das pessoas com mais de uma matrícula.
     * Mensagem: Constam a mesma pessoa mais de uma vez na mesma matrícula
     * @return
     */
    public List listPessoasMesmaMatricula() {
        return listPessoasMesmaMatricula(true, null);
    }

    /*
     * Pesquisar se existe pessoas com mais de uma matrícula.
     * Mensagem: Constam a mesma pessoa mais de uma vez na mesma matrícula
     *
     * @param return_pessoas (true = traz a lista de pessoas / false = traz uma lista de id das pessoas)
     * @param limit (Se o limit null traz todos as pessoas)
     * @return
     */
    public List listPessoasMesmaMatricula(Boolean return_pessoas, Integer limit) {
        try {
            String queryString
                    = "     SELECT SP.id_pessoa                                             \n"
                    + "       FROM soc_socios AS S                                          \n"
                    + " INNER JOIN fin_servico_pessoa AS SP ON SP.id = S.id_servico_pessoa  \n"
                    + "   GROUP BY SP.id_pessoa,                                            \n"
                    + "            S.id_matricula_socios                                    \n"
                    + "     HAVING COUNT(*) > 1                                            ";
            Query query;
            if (return_pessoas) {
                queryString = " SELECT P.* FROM pes_pessoa AS P WHERE P.id IN (\n" + queryString + "\n) ORDER BY P.ds_nome ";
                query = getEntityManager().createNativeQuery(queryString, Pessoa.class);
            } else {
                query = getEntityManager().createNativeQuery(queryString);
            }
            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /*
     * Verifica se existe Matrícula Ativa com id_servico_pessoa menor que último.
     * @return
     */
    public Boolean existMatriculaAtivaAtivacaoDesordenada() {
        return !listMatriculaAtivaAtivacaoDesordenada(false, 1).isEmpty();
    }
    /*
     * Traz uma lista Matrícula Ativa com id_servico_pessoa menor que último.
     * Mensagem: Constam a mesma pessoa mais de uma vez na mesma matrícula
     * @return
     */

    public List listMatriculaAtivaAtivacaoDesordenada() {
        return listMatriculaAtivaAtivacaoDesordenada(true, null);
    }
    /*
     * Matrícula Ativa com id_servico_pessoa menor que último, favor entrar em contato com nosso suporte técnico.
     * @return
     */

    public List listMatriculaAtivaAtivacaoDesordenada(Boolean return_pessoas, Integer limit) {
        try {
            String queryString
                    = "        SELECT SP.id_pessoa                                               \n"
                    + "         FROM soc_socios 	AS S                                     \n"
                    + "   INNER JOIN fin_servico_pessoa AS SP ON SP.id = S.id_servico_pessoa     \n"
                    + "   INNER JOIN (                                                           \n"
                    + "         SELECT SP.id_pessoa,                                             \n"
                    + "                max(SP.id) id_servico_pessoa                              \n"
                    + "           FROM soc_socios         AS S                                   \n"
                    + "     INNER JOIN fin_servico_pessoa AS SP ON SP.id = S.id_servico_pessoa   \n"
                    + "       GROUP BY SP.id_pessoa                                              \n"
                    + "    ) AS X ON X.id_pessoa = SP.id_pessoa                                  \n"
                    + "        WHERE SP.is_ativo = true                                          \n"
                    + "          AND SP.id <> X.id_servico_pessoa                                \n";
            Query query;
            if (return_pessoas) {
                queryString = " SELECT P.* FROM pes_pessoa AS P WHERE P.id IN (\n" + queryString + "\n) ORDER BY P.ds_nome ";
                query = getEntityManager().createNativeQuery(queryString, Pessoa.class);
            } else {
                query = getEntityManager().createNativeQuery(queryString);
            }
            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

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
                            = "      SELECT S.*                                                                                 \n"
                            + "        FROM pes_pessoa_endereco pesend                                                          \n"
                            + "  INNER JOIN pes_pessoa pes                      ON pes.id = pesend.id_pessoa                    \n"
                            + "  INNER JOIN soc_socios_vw AS SVW                ON SVW.codsocio = PES.id                        \n"
                            + "  INNER JOIN soc_socios AS S                     ON S.codsocio = PES.id                          \n"
                            + "  INNER JOIN end_endereco ende                   ON ende.id = pesend.id_endereco                 \n"
                            + "  INNER JOIN end_cidade cid                      ON cid.id = ende.id_cidade                      \n"
                            + "  INNER JOIN end_descricaoricao_endereco enddes  ON enddes.id = ende.id_descricaoricao_endereco  \n"
                            + "  INNER JOIN end_bairro bai                      ON bai.id = ende.id_bairro                      \n"
                            + "  INNER JOIN end_logradouro logr                 ON logr.id = ende.id_logradouro                 \n"
                            + "  INNER JOIN pes_fisica fis                      ON fis.id_pessoa = pes.id                       \n"
                            + "  WHERE (LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao || ', ' || bai.ds_descricaoricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)) LIKE '%" + descricao + "%' \n"
                            + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf)) LIKE '%" + descricao + "%'                       \n"
                            + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao || ', ' || cid.ds_cidade  )) LIKE '%" + descricao + "%'                                           \n"
                            + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricaoricao || ' ' || enddes.ds_descricaoricao)) LIKE '%" + descricao + "%'                                                                      \n"
                            + "     OR LOWER(FUNC_TRANSLATE(enddes.ds_descricaoricao)) LIKE '%" + descricao + "%'                                                                                                       \n"
                            + "     OR LOWER(FUNC_TRANSLATE(cid.ds_cidade)) LIKE '%" + descricao + "%'                                                                                                                  \n"
                            + "     OR LOWER(FUNC_TRANSLATE(ende.ds_cep)) = '" + descricao + "'                                                                                                                         \n"
                            + "  )                                                                  \n"
                            + "  AND pesend.id_tipo_endereco = 1                                    \n"
                            + "  AND pes.id IN (                                                    \n"
                            + "         SELECT p2.id FROM fin_servico_pessoa sp                     \n"
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa     \n"
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa          \n"
                            + "          WHERE sp.is_ativo = TRUE                                   \n"
                            + "  )                                                                  \n"
                            + "  ORDER BY pes.ds_nome LIMIT " + maxResults;
                    break;
                case "matricula":
                    textQuery
                            = "      SELECT S.*                                                         \n"
                            + "        FROM pes_fisica      AS F                                        \n"
                            + "  INNER JOIN pes_pessoa      AS P    ON P.id = f.id_pessoa               \n"
                            + "  INNER JOIN soc_socios_vw   AS SVW  ON SVW.codsocio = PES.id)           \n"
                            + "  INNER JOIN soc_socios      AS S    ON S.codsocio = PES.id              \n"
                            + "  WHERE P.id IN (                                                        \n"
                            + "         SELECT p2.id FROM fin_servico_pessoa sp                         \n"
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa         \n"
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa              \n"
                            + "          INNER JOIN matr_socios ms ON  ms.id = s.id_matricula_socios    \n"
                            + "          WHERE sp.is_ativo = TRUE                                       \n"
                            + "            AND ms.nr_matricula = " + descricao.replace("%", "") + "     \n"
                            + "    )                                                                    \n"
                            + "  ORDER BY P.ds_nome LIMIT " + maxResults;
                    break;
                case "codigo":
                    textQuery
                            = "      SELECT S.*                                                         \n"
                            + "        FROM pes_fisica          AS F                                    \n"
                            + "  INNER JOIN pes_pessoa          AS p        ON P.id = F.id_pessoa       \n"
                            + "  INNER JOIN soc_socios_vw       AS SVW      ON SVW.codsocio = PES.id    \n"
                            + "  INNER JOIN soc_socios          AS S        ON S.codsocio = PES.id      \n"
                            + "  INNER JOIN pes_pessoa_empresa  AS PE       ON f.id = pe.id_fisica      \n"
                            + "  WHERE pe.ds_codigo LIKE '" + descricao + "'                            \n"
                            + "    AND p.id IN (                                                        \n"
                            + "         SELECT p2.id FROM fin_servico_pessoa sp                         \n"
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa         \n"
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa              \n"
                            + "          WHERE sp.is_ativo = TRUE                                       \n"
                            + "    )                                                                    \n"
                            + "  ORDER BY P.ds_nome LIMIT " + maxResults;
                    break;
                default:
                    textQuery
                            = "      SELECT S.*                                                         \n"
                            + "        FROM pes_fisica         AS F                                     \n"
                            + "  INNER JOIN pes_pessoa          AS P        ON P.id = F.id_pessoa       \n"
                            + "  INNER JOIN soc_socios_vw       AS SVW      ON SVW.codsocio = PES.id    \n"
                            + "  INNER JOIN soc_socios          AS S        ON S.codsocio = PES.id      \n"
                            + "  WHERE LOWER(FUNC_TRANSLATE(" + field + ")) LIKE '" + descricao + "'    \n"
                            + "    AND p.id IN (                                                        \n"
                            + "         SELECT p2.id FROM fin_servico_pessoa sp                         \n"
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa         \n"
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa              \n"
                            + "          WHERE sp.is_ativo = TRUE                                       \n"
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
    }

}
