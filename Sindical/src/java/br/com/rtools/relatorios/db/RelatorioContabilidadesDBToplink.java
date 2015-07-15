package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class RelatorioContabilidadesDBToplink extends DB implements RelatorioContabilidadesDB {

    @Override
    public List pesquisaContabilidades() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select distinct jur.contabilidade "
                    + "  from Juridica jur"
                    + " where jur.contabilidade is not null"
                    + " order by jur.contabilidade.id");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaQntEmpresas(int id_contabilidade) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select count(jur) "
                    + "  from Juridica jur"
                    + " where jur.contabilidade.id = " + id_contabilidade);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
    
    @Override
    public int quantidadeEmpresas() {
        int quantidade = 0;
        try {
            Query qry = getEntityManager().createNativeQuery(""
                    + "    SELECT COUNT(*) "
                    + "      FROM arr_contribuintes_vw AS C "
                    + "     WHERE C.dt_inativacao IS NULL "
                    + "       AND C.id_contabilidade > 0 "
                    + "  GROUP BY C.id_contabilidade "
                    + "  ORDER BY COUNT(*) DESC "
                    + "     LIMIT 1");
            List list = qry.getResultList();
            quantidade = Integer.parseInt(((List) list.get(0)).get(0).toString());
        } catch (Exception e) {
            quantidade = 0;
        }
        return quantidade;
    }

    @Override
    public List pesquisarCnaeContabilidade() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select distinct j.contabilidade.cnae "
                    + "  from Juridica j"
                    + " where j.contabilidade.id is not null"
                    + " order by j.contabilidade.cnae.cnae");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaRelatorioContabilidades(String pEmpresas, int indexEmp1, int indexEmp2, String tipoPCidade, String cidade, String ordem, int idTipoEndereco) {
        String textQueryNativa;
        List list = new ArrayList();
        try {
            String textFrom = "";
            String textFaixa = "";
            if (ordem.equals("razao")) {
                textFrom = " , P.ds_nome ";
            }
            textQueryNativa
                    = "      SELECT C.id_contabilidade,                                      "
                    + "             PE.id as id_pessoa_endereco,                             "
                    + "             COUNT(*) qtde                                           "
                    + textFrom
                    + "        FROM arr_contribuintes_vw AS C                                "
                    + "  INNER JOIN pes_juridica         AS J ON J.id = C.id_contabilidade   "
                    + "  INNER JOIN pes_pessoa_endereco  AS PE ON PE.id_pessoa = J.id_pessoa "
                    + "  INNER JOIN end_endereco         AS E ON E.id = PE.id_endereco "
                    + "  INNER JOIN endereco_vw          AS ENDE ON ENDE.id = PE.id_endereco "
                    + "  INNER JOIN pes_pessoa           AS P ON P.ID = J.id_pessoa          "
                    + "       WHERE C.dt_inativacao IS NULL AND C.id_contabilidade > 0       ";

            if (pEmpresas.equals("comEmpresas")) {
                textFaixa = " HAVING COUNT(C.id_contabilidade) >= " + indexEmp1 + " AND COUNT(C.id_contabilidade) <= " + indexEmp2;
            }
            // CIDADE -------------------------------------------------------
            if (tipoPCidade.equals("todas")) {
                textQueryNativa += " AND PE.id_tipo_endereco = " + idTipoEndereco;
            } else if (tipoPCidade.equals("especificas")) {
                textQueryNativa += " AND PE.id_tipo_endereco = " + idTipoEndereco + " AND E.id_cidade IN (" + Integer.parseInt(cidade)+")";
            } else if (tipoPCidade.equals("local")) {
                textQueryNativa += " AND PE.id_tipo_endereco = " + idTipoEndereco + " AND E.id_cidade IN (" + Integer.parseInt(cidade)+")";
            } else if (tipoPCidade.equals("outras")) {
                textQueryNativa += " AND PE.id_tipo_endereco = " + idTipoEndereco + " AND ENDE.cidade <> (SELECT ds_cidade FROM end_cidade WHERE id = " + Integer.parseInt(cidade)+")";
            }
            // AGRUPAR ------------------------------------------------------------------------
            if (ordem.equals("razao")) {
                textQueryNativa += " GROUP BY C.id_contabilidade, PE.id, P.ds_nome " + textFaixa;
            } else if (ordem.equals("endereco")) {
                textQueryNativa += " GROUP BY C.id_contabilidade, PE.id, ENDE.uf, ENDE.cidade, ENDE.logradouro, ENDE.endereco " + textFaixa;
            } else if (ordem.equals("cep")) {
                textQueryNativa += " GROUP BY C.id_contabilidade, PE.id, ENDE.cep, P.ds_nome,ENDE.uf, ENDE.cidade, ENDE.logradouro,ENDE.endereco " + textFaixa;
            } else if (ordem.equals("qtde")) {
                textQueryNativa += " GROUP BY C.id_contabilidade, PE.id, ENDE.cep, P.ds_nome,ENDE.uf, ENDE.cidade, ENDE.logradouro,ENDE.endereco " + textFaixa;
            }
            // ORDEM ------------------------------------------------------------------------
            if (ordem.equals("razao")) {
                textQueryNativa = textQueryNativa + " ORDER BY P.ds_nome ASC ";
            } else if (ordem.equals("endereco")) {
                textQueryNativa += " ORDER BY ENDE.uf  ASC,                     "
                        + "                   ENDE.cidade  ASC,                 "
                        + "                   ENDE.logradouro ASC,              "
                        + "                   ENDE.endereco ASC,                "
                        + "                   PE.ds_numero ASC                  ";
            } else if (ordem.equals("cep")) {
                textQueryNativa += " ORDER BY ENDE.cep ASC,                     "
                        + "                   ENDE.uf  ASC,                     "
                        + "                   ENDE.cidade  ASC,                 "
                        + "                   ENDE.logradouro ASC,              "
                        + "                   ENDE.endereco ASC,                "
                        + "                   PE.ds_numero ASC                  ";
            } else if (ordem.equals("qtde")) {
                textQueryNativa += " ORDER BY qtde ASC                          ";
            }
            Query queryNativa = getEntityManager().createNativeQuery(textQueryNativa);
            list = queryNativa.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (EJBQLException e) {
            return list;
        } catch (NumberFormatException e) {
            return list;
        } catch (Exception e) {
            return list;
        }
        return list;
    }
}
