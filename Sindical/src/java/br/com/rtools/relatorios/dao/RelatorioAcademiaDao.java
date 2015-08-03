package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioAcademiaDao extends DB {

    /**
     * 0 - NOME 1 - IDADE 2 - NASCIMENTO 3 - SEXO 4 - CIDADE 5 - RESPONSÁVEL 6 -
     * SERVIÇO 7 - PERÍODO - 8 EMISSÃO
     *
     * @param r
     * @param emissaoInicial
     * @param emissaoFinal
     * @param idResponsavel
     * @param idAluno
     * @param inModalidade
     * @param inIdPeriodos
     * @param inSexo
     * @param periodo
     * @param ativos
     * @param order
     * @param in_grupo_categoria
     * @param in_categoria
     * @param nao_socio
     * @param convenio_empresa
     * @param idade
     * @return
     */
    public List find(Relatorios r, String emissaoInicial, String emissaoFinal, Integer idResponsavel, Integer idAluno, String inModalidade, String inIdPeriodos, String inSexo, String periodo, Boolean ativos, Integer[] idade, String in_grupo_categoria, String in_categoria, Boolean nao_socio, Boolean convenio_empresa, String order) {
        List listWhere = new ArrayList();
        String queryString = ""
                + "     SELECT PA.nome,                                                       \n" // 0 - NOME
                + "            func_idade(PA.dt_nascimento, current_date)  AS idade,          \n" // 1 - IDADE
                + "            PA.dt_nascimento                            AS nascimento,     \n" // 2 - NASCIMENTO
                + "            PA.sexo,                                                       \n" // 3 - SEXO
                + "            PA.cidade,                                                     \n" // 4 - CIDADE
                + "            PR.ds_nome                                  AS responsavel,    \n" // 5 - RESPONSÁVEL
                + "            S.ds_descricao                              AS servico,        \n" // 6 - SERVIÇO
                + "            P.ds_descricao                              AS periodo,        \n" // 7 - PERÍODO
                + "            SP.dt_emissao                               AS emissao         \n" // 8 - EMISSÃO
                + "       FROM matr_academia AS A                                             \n"
                + " INNER JOIN fin_servico_pessoa   AS SP  ON SP.id         = A.id_servico_pessoa   \n"
                + " INNER JOIN aca_servico_valor    AS ASV ON ASV.id        = A.id_servico_valor    \n"
                + " INNER JOIN fin_servicos         AS S   ON S.id          = ASV.id_servico        \n"
                + " INNER JOIN sis_periodo          AS P   ON P.id          = ASV.id_periodo        \n"
                + " INNER JOIN pes_fisica_vw        AS PA  ON PA.codigo     = SP.id_pessoa          \n"
                + " INNER JOIN pes_pessoa           AS PR  ON PR.id         = SP.id_cobranca        \n"
                + "  LEFT JOIN soc_socios_vw        AS SOC ON SOC.codsocio  = SP.id_pessoa          \n"
                + "  LEFT JOIN                                                                      \n"
                + "  (SELECT SP.id_pessoa, id_servico                                               \n"
                + "      FROM fin_servico_pessoa    AS SP                                           \n"
                + "INNER JOIN matr_academia         AS M    ON M.id_servico_pessoa = SP.id          \n"
                + "     WHERE SP.is_ativo = true                                                    \n"
                + " ) AS MA ON MA.id_servico = SP.id_servico AND MA.id_pessoa = SP.id_pessoa        \n";
        if (convenio_empresa != null && convenio_empresa) {
            queryString += " INNER JOIN fin_desconto_servico_empresa AS FDSE ON FDSE.id_juridica = PA.id_juridica AND FDSE.id_servico = SP.id_servico ";
            listWhere.add("SP.id_pessoa NOT IN (SELECT SOCVW.codsocio FROM soc_socios_vw AS SOCVW GROUP BY SOCVW.codsocio )");
        }
        String emissaoInativacaoString = "";
        if (periodo != null) {
            if (periodo.equals("emissao")) {
                if (ativos != null) {
                    if (ativos) {
                        emissaoInativacaoString += " SP.is_ativo = true AND ";
                    } else {
                        listWhere.add(" MA.id_pessoa IS NULL ");
                        emissaoInativacaoString += " SP.is_ativo = false AND ";
                    }
                    emissaoInativacaoString = emissaoInativacaoString + " SP.dt_emissao ";
                }
            } else if (periodo.equals("inativacao")) {
                emissaoInativacaoString = " SP.is_ativo = false AND A.dt_inativo ";
            }
            if (!emissaoInicial.isEmpty() && !emissaoFinal.isEmpty()) {
                listWhere.add(emissaoInativacaoString + "BETWEEN '" + emissaoInicial + "' AND '" + emissaoFinal + "'");
            } else if (!emissaoFinal.isEmpty()) {
                listWhere.add(emissaoInativacaoString + " = '" + emissaoInicial + "'");
            } else if (!emissaoFinal.isEmpty()) {
                listWhere.add(emissaoInativacaoString + " = '" + emissaoFinal + "'");
            } else if (emissaoInicial.isEmpty() || emissaoFinal.isEmpty()) {
                if (ativos) {
                    listWhere.add(emissaoInativacaoString + " IS NOT NULL ");
                }
            }
        } else {
            if (ativos) {
                listWhere.add(" SP.is_ativo = true  ");
            } else {
                listWhere.add(" SP.is_ativo = false ");
                listWhere.add(" MA.id_pessoa IS NULL ");
            }
        }

        if (idade[0] != 0 || idade[1] != 0) {
            if (idade[0].equals(idade[1])) {
                listWhere.add(" func_idade(PA.dt_nascimento, current_date) = " + idade[0]);
            } else if (idade[0] >= 0 && idade[1] == 0) {
                listWhere.add(" func_idade(PA.dt_nascimento, current_date) >= " + idade[0]);
            } else {
                listWhere.add(" func_idade(PA.dt_nascimento, current_date) BETWEEN " + idade[0] + " AND " + idade[1]);
            }
        }
        if (idResponsavel != null) {
            listWhere.add("SP.id_cobranca = " + idResponsavel);
        }
        if (idAluno != null) {
            listWhere.add("SP.id_pessoa = " + idAluno);
        }
        if (inModalidade != null) {
            listWhere.add("SP.id_servico IN(" + inModalidade + ")");
        }
        if (inIdPeriodos != null) {
            listWhere.add("ASV.id_periodo IN(" + inIdPeriodos + ")");
        }
        if (inSexo != null && !inSexo.isEmpty()) {
            listWhere.add("PA.sexo LIKE '" + inSexo + "'");
        }
        if (nao_socio != null && nao_socio) {
            listWhere.add("SP.id_pessoa NOT IN (SELECT SOCVW.codsocio FROM soc_socios_vw AS SOCVW GROUP BY SOCVW.codsocio)");
        } else {
            if ((in_grupo_categoria != null && !in_grupo_categoria.isEmpty()) || (in_categoria != null && !in_categoria.isEmpty())) {
                if (in_categoria != null && !in_categoria.isEmpty()) {
                    listWhere.add("SOC.id_categoria IN (" + in_categoria + ")");
                } else if (in_grupo_categoria != null && !in_grupo_categoria.isEmpty()) {
                    listWhere.add("SOC.id_grupo_categoria IN (" + in_grupo_categoria + ")");
                }
            }
        }

        if (!listWhere.isEmpty()) {
            queryString += " WHERE ";
            for (int i = 0; i < listWhere.size(); i++) {
                if (i > 0) {
                    queryString += " AND ";
                }
                queryString += listWhere.get(i).toString() + " \n";

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
}
