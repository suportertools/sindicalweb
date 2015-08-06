package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioHomologacaoDao extends DB {

    private String order = "";

    public List find(Relatorios relatorio, Integer empresa, Integer funcionario, String tipoUsuarioOperacional, Integer usuarioOperacional, Integer status, Integer filial, Integer tCase, String dateStart, String dateFinish, Integer motivoDemissao, Boolean tipoAviso, String tipoAgendador, String sexo, Boolean webAgendamento, Integer idConvencao) {
        List listQuery = new ArrayList();
        String queryString = ""
                + "     SELECT to_char(A.dt_data,'dd/mm/yyyy')  AS dataInicial,         "
                + "            to_char(A.dt_data,'dd/mm/yyyy')  AS dataFinal,           "
                + "            to_char(A.dt_data,'dd/mm/yyyy')  AS data,                "
                + "            H.ds_hora                        AS hora,                "
                + "            PPE.ds_documento                 AS cnpj,                "
                + "            PPE.ds_nome                      AS empresa,             "
                + "            FUNC.ds_nome                     AS funcionario,         "
                + "            A.ds_contato                     AS contato,             "
                + "            A.ds_telefone                    AS telefone,            ";
        if (tipoUsuarioOperacional != null) {
            queryString += " UO.ds_nome AS usuario_operacional, ";
        } else {
            queryString += " '' AS usuario_operacional, ";
        }
        queryString += "       A.ds_obs                         AS obs,                 "
                + "            S.ds_descricao                   AS status               "
                + "       FROM hom_agendamento                  AS A                    "
                + " INNER JOIN hom_horarios       AS H       ON H.id    = A.id_horario          "
                + " INNER JOIN hom_status         AS S       ON S.id    = A.id_status           ";
        if (tipoUsuarioOperacional != null) {
            queryString += "  LEFT JOIN seg_usuario        AS U       ON U.id    = A." + tipoUsuarioOperacional
                    + "  LEFT JOIN pes_pessoa         AS UO      ON UO.id   = U.id_pessoa           ";

        }
        queryString += " INNER JOIN pes_pessoa_empresa AS PE      ON PE.id   = A.id_pessoa_empresa   "
                + " INNER JOIN pes_juridica       AS J       ON J.id    = PE.id_juridica        "
                + " INNER JOIN pes_fisica         AS F       ON F.id    = PE.id_fisica          "
                + " INNER JOIN pes_pessoa         AS FUNC    ON FUNC.id = F.id_pessoa           "
                + " INNER JOIN pes_pessoa         AS PPE     ON PPE.id  = J.id_pessoa           ";

        if (idConvencao != null){
            queryString += " INNER JOIN arr_contribuintes_vw AS CONTR      ON CONTR.id_juridica   = J.id AND CONTR.dt_inativacao IS NULL ";
        }
        
        if (relatorio.getQry() == null || relatorio.getQry().isEmpty()) {
            if (tCase != null) {
                if (tCase == 1) {
                    // DATA DE AGENDAMENTO ---------------
                    if (!dateStart.isEmpty() && !dateFinish.isEmpty()) {
                        listQuery.add(" A.dt_data >= '" + dateStart + "' AND A.dt_data <= '" + dateFinish + "'");
                    } else if (!dateStart.isEmpty()) {
                        listQuery.add(" A.dt_data = '" + dateStart + "'");
                    } else {
                        listQuery.add(" A.dt_data >= '01/01/1900' AND A.dt_data <= '01/01/2030'");
                    }
                } else {
                    // DATA DE DEMISSÃO ---------------
                    if (!dateStart.isEmpty() && !dateFinish.isEmpty()) {
                        listQuery.add(" PE.dt_demissao >= '" + dateStart + "' AND PE.dt_demissao <= '" + dateFinish + "'");
                    } else if (!dateStart.isEmpty()) {
                        listQuery.add(" PE.dt_demissao = '" + dateStart + "'");
                    } else {
                        listQuery.add(" PE.dt_demissao >= '01/01/1900' AND PE.dt_demissao <= '01/01/2030'");
                    }
                }
            }
            if (empresa != null) {
                listQuery.add("J.id = " + empresa);
            }
            if (funcionario != null) {
                listQuery.add("F.id = " + funcionario);
            }
            if (usuarioOperacional != null) {
                if (tipoUsuarioOperacional != null && tipoUsuarioOperacional.equals("id_agendador")) {
                    if (webAgendamento) {
                        listQuery.add("A." + tipoUsuarioOperacional + " IS NULL");
                    } else {
                        listQuery.add("A." + tipoUsuarioOperacional + " = " + usuarioOperacional);
                    }
                } else {
                    listQuery.add("A." + tipoUsuarioOperacional + " = " + usuarioOperacional);
                }
            } else {
                if (tipoUsuarioOperacional != null && tipoUsuarioOperacional.equals("id_agendador")) {
                    if (webAgendamento) {
                        listQuery.add("A." + tipoUsuarioOperacional + " IS NULL");
                    } else {
                        if(usuarioOperacional != null) {
                            listQuery.add("A." + tipoUsuarioOperacional + " = " + usuarioOperacional);
                        }
                    }
                }
            }
            if (filial != null) {
                listQuery.add("A.id_filial = " + filial);
            }
            if (status != null) {
                listQuery.add("A.id_status = " + status);
            }
            if (motivoDemissao != null) {
                listQuery.add("A.id_demissao = " + motivoDemissao);
            }
            if (sexo != null && !sexo.isEmpty()) {
                listQuery.add("F.ds_sexo = '" + sexo + "'");
            }
            if (tipoAviso != null) {
                if (tipoAviso) {
                    listQuery.add("PE.aviso_trabalhado = true");
                } else {
                    listQuery.add("PE.aviso_trabalhado = false");
                }
            }
            if (idConvencao != null) {
                listQuery.add("CONTR.id_convencao = " + idConvencao);
            }
            for (int i = 0; i < listQuery.size(); i++) {
                if (i == 0) {
                    queryString += " WHERE ";
                } else {
                    queryString += " AND ";
                }
                queryString += " " + listQuery.get(i).toString();
            }
        } else {
            queryString += " WHERE" + relatorio.getQry();
        }

        // ORDEM DA QRY
        if (relatorio.getQryOrdem() == null || relatorio.getQryOrdem().isEmpty()) {
            if (!order.isEmpty()) {
                switch (order) {
                    case "data":
                        queryString += " ORDER BY A.dt_data DESC, H.ds_hora, PPE.ds_nome";
                        break;
                    case "empresa":
                        queryString += " ORDER BY PPE.ds_nome ";
                        break;
                    case "funcionario":
                        queryString += " ORDER BY FUNC.ds_nome ";
                        break;
                    case "homologador":
                        queryString += " ORDER BY UO.ds_nome ";
                        break;
                }
            }
        } else {
            queryString += " ORDER BY" + relatorio.getQryOrdem();
        }

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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
