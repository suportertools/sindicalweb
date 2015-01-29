package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioCertificadosDao extends DB {

    private String order = "";

    /**
     *
     * @param relatorios
     * @param empresa
     * @param tipo
     * @param referencia
     * @param inRepisStatus
     * @param inCertidoesTipo
     * @param inCidadeBase
     * @return
     */
    public List find(Relatorios relatorios, Integer empresa, Integer tipo, String referencia[], String inRepisStatus, String inCertidoesTipo, String inCidadeBase) {
        try {
            String queryString;
            List listQuery = new ArrayList();
            queryString = ""
                    + "      SELECT P.ds_documento,                                                                 " // 0  - DOCUMENTO
                    + "             P.ds_nome,                                                                      " // 1  - NOME
                    + "             CT.ds_descricao,                                                                " // 2  - CERTIDÃO STATUS
                    + "             RS.ds_descricao,                                                                " // 3  - REPIS STATUS
                    + "             RM.dt_emissao,                                                                  " // 4  - DATA EMISSÃO
                    + "             RM.dt_resposta,                                                                 " // 5  - DATA RESPOSTA
                    + "             RM.nr_ano,                                                                      " // 6  - ANO
                    + "             RM.ds_solicitante,                                                              " // 7  - SOLICITANTE
                    + "             P.ds_email1,                                                                    " // 8  - EMAIL 1
                    + "             P.ds_telefone1,                                                                 " // 9 - TELEFONE 1
                    + "             L.ds_descricao,                                                                 " // 10 - LOGRADOURO
                    + "             DE.ds_descricao,                                                                " // 11 - DESCRIÇÃO ENDEREÇO
                    + "             PE.ds_numero,                                                                   " // 12 - NUMERO
                    + "             PE.ds_complemento,                                                              " // 13 - COMPLEMENTO
                    + "             B.ds_descricao,                                                                 " // 14 - BAIRRO
                    + "             C.ds_cidade,                                                                    " // 15 - CIDADE
                    + "             C.ds_uf,                                                                        " // 16 - UF
                    + "             ENDE.ds_cep                                                                     " // 17 - CEP
                    + "        FROM arr_repis_movimento     AS RM                                                   "
                    + "  INNER JOIN pes_pessoa              AS P    ON P.id         = RM.id_pessoa                      "
                    + "  INNER JOIN pes_pessoa_endereco     AS PE   ON PE.id_pessoa = P.id                              "
                    + "  INNER JOIN end_endereco            AS ENDE ON ENDE.id      = PE.id_endereco                    "
                    + "  INNER JOIN arr_repis_status        AS RS   ON RS.id        = RM.id_repis_status                "
                    + "  INNER JOIN arr_certidao_tipo       AS CT   ON CT.id        = RM.id_certidao_tipo               "
                    + "  INNER JOIN end_logradouro          AS L    ON L.id         = ENDE.id_logradouro                "
                    + "  INNER JOIN end_descricao_endereco  AS DE   ON DE.id        = ENDE.id_descricao_endereco        "
                    + "  INNER JOIN end_bairro              AS B    ON B.id         = ENDE.id_bairro                    "
                    + "  INNER JOIN end_cidade              AS C    ON C.id         = ENDE.id_cidade                    "
                    + "";
            if (tipo != null) {
                listQuery.add("PE.id_tipo_endereco = 2");
                if (tipo == 1) {
                    listQuery.add("RM.nr_ano = " + referencia[0]);
                } else if (tipo == 2) {
                    if (referencia[1].isEmpty()) {
                        if (!referencia[0].isEmpty()) {
                            listQuery.add("RM.dt_emissao = '" + referencia[0] + "'");
                        }
                    } else {
                        listQuery.add("RM.dt_emissao BETWEEN '" + referencia[0] + "' AND '" + referencia[1] + "'");
                    }
                } else if (tipo == 3) {
                    if (referencia[1].isEmpty()) {
                        if (!referencia[0].isEmpty()) {
                            listQuery.add("RM.dt_resposta = '" + referencia[0] + "'");
                        }
                    } else {
                        listQuery.add("RM.dt_resposta BETWEEN '" + referencia[0] + "' AND '" + referencia[1] + "'");
                    }
                } else if (tipo == 4) {
                    listQuery.add("RM.dt_resposta IS NULL");
                }
            }
            if (!inRepisStatus.isEmpty()) {
                listQuery.add("RM.id_repis_status IN(" + inRepisStatus + ")");
            }
            if (!inCertidoesTipo.isEmpty()) {
                listQuery.add("RM.id_certidao_tipo IN(" + inCertidoesTipo + ")");
            }
            if (!inCidadeBase.isEmpty()) {
                listQuery.add("C.id IN(" + inCidadeBase + ")");
            }
            if (empresa != null) {
                listQuery.add("RM.id_pessoa = " + empresa);
            }
            for (int i = 0; i < listQuery.size(); i++) {
                if (i == 0) {
                    queryString += " WHERE ";
                } else {
                    queryString += " AND ";
                }
                queryString += " " + listQuery.get(i).toString();
            }
            if (!relatorios.getQryOrdem().isEmpty()) {
                queryString += " ORDER BY " + relatorios.getQry();
            } else {
                if (order.isEmpty()) {
                    queryString += " ORDER BY P.ds_nome ASC";
                } else {
                    queryString += " ORDER BY " + order;
                }
            }
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

    /**
     *
     * @param tipo
     * @param referencia
     * @param inRepisStatus
     * @param inCertidoesTipo
     * @param inCidadeBase
     * @return
     */
    public List find(Integer tipo, String referencia[], String inRepisStatus, String inCertidoesTipo, String inCidadeBase) {
        List listQuery = new ArrayList();
        try {
            String queryString = ""
                    + "      SELECT CVW.ds_nome,                                                                                    " // 0 - NOME
                    + "             L.ds_descricao,                                                                                 " // 1 - LOGRADOURO
                    + "             DE.ds_descricao,                                                                                " // 2 - DESCRIÇÃO ENDEREÇO
                    + "             PE.ds_numero,                                                                                   " // 3 - NUMERO
                    + "             PE.ds_complemento,                                                                              " // 4 - COMPLEMENTO
                    + "             B.ds_descricao,                                                                                 " // 5 - BAIRRO
                    + "             C.ds_cidade,                                                                                    " // 6 - CIDADE
                    + "             C.ds_uf,                                                                                        " // 7 - UF
                    + "             ENDE.ds_cep                                                                                     " // 8 - CEP
                    + "        FROM arr_contribuintes_vw AS CVW                                                                     "
                    + "  INNER JOIN pes_pessoa_endereco     AS PE   ON PE.id_pessoa = CVW.id_pessoa                                 "
                    + "  INNER JOIN end_endereco            AS ENDE ON ENDE.id      = PE.id_endereco AND PE.id_tipo_endereco = 2    "
                    + "  INNER JOIN end_logradouro          AS L    ON L.id         = ENDE.id_logradouro                            "
                    + "  INNER JOIN end_descricao_endereco  AS DE   ON DE.id        = ENDE.id_descricao_endereco                    "
                    + "  INNER JOIN end_bairro              AS B    ON B.id         = ENDE.id_bairro                                "
                    + "  INNER JOIN end_cidade              AS C    ON C.id         = ENDE.id_cidade                                "
                    + "       WHERE                                                                                                 ";

            String subQueryString = ""
                    + "CVW.id_pessoa NOT IN (                                                                                           "
                    + "      SELECT RM.id_pessoa                                                                                        "
                    + "        FROM arr_repis_movimento AS RM                                                                           ";
            if (tipo != null) {
                if (tipo == 1) {
                    listQuery.add("RM.nr_ano = " + referencia[0]);
                } else if (tipo == 2) {
                    if (referencia[1].isEmpty()) {
                        listQuery.add("RM.dt_emissao = '" + referencia[0] + "'");
                    } else {
                        listQuery.add("RM.dt_emissao BETWEEN '" + referencia[0] + "' AND '" + referencia[1] + "'");
                    }
                } else if (tipo == 3) {
                    if (referencia[1].isEmpty()) {
                        listQuery.add("RM.dt_resposta = '" + referencia[0] + "'");
                    } else {
                        listQuery.add("RM.dt_resposta BETWEEN '" + referencia[0] + "' AND '" + referencia[1] + "'");
                    }
                } else if (tipo == 4) {
                    listQuery.add("RM.dt_resposta IS NULL");
                }
            }
            if (!inRepisStatus.isEmpty()) {
                listQuery.add("RM.id_repis_status IN(" + inRepisStatus + ")");
            }
            if (!inCertidoesTipo.isEmpty()) {
                listQuery.add("RM.id_certidao_tipo IN(" + inCertidoesTipo + ")");
            }
//            if (!inCidadeBase.isEmpty()) {
//                listQuery.add("ENDE2.id_cidade IN(" + inCidadeBase + ")");
//            }
            for (int i = 0; i < listQuery.size(); i++) {
                if (i == 0) {
                    subQueryString += " WHERE ";
                } else {
                    subQueryString += " AND ";
                }
                subQueryString += " " + listQuery.get(i).toString();
            }
            subQueryString += " GROUP BY RM.id_pessoa) AND CVW.dt_inativacao IS NULL ";
            if (!inCidadeBase.isEmpty()) {
                subQueryString += " AND C.id IN ( " + inCidadeBase + ") ";
            }
            queryString += subQueryString + " "
                    + " ORDER BY C.ds_cidade ASC,   "
                    + "          L.ds_descricao,    "
                    + "          DE.ds_descricao,   "
                    + "          PE.ds_numero,      "
                    + "          CVW.ds_nome        ";
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
