package br.com.rtools.relatorios.db;

import br.com.rtools.pessoa.PessoaEndereco;
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
    public List listaRelatorioContabilidades(String pEmpresas, int indexEmp1, int indexEmp2, String tipoPCidade, String cidade, String ordem, String cnaes, int idTipoEndereco) {
        String textQuery;
        String textQueryNativa;
        List list = new ArrayList();
        try {
//            Query query = getEntityManager().createQuery("SELECT CI.juridica FROM ContribuintesInativos CI LEFT OUTER JOIN FETCH CI.juridica J");
//            boolean verdade = true;
//            List list = query.getResultList();                
//            return list;
            textQuery = "   SELECT J "
                    + "       FROM Juridica AS J, "
                    + " INNER JOIN PessoaEndereco AS PE "
                    ;
            textQueryNativa = 
                   "        SELECT JUR.id                                                                       "
                   +"         FROM pes_juridica                AS JUR                                           "
                   +"   INNER JOIN pes_pessoa_endereco         AS PE   ON PE.id    = JUR.id_pessoa              "
                   +"   INNER JOIN pes_pessoa                  AS PES  ON PES.id   = JUR.id_pessoa              "
                   +"    LEFT JOIN end_endereco                AS ENDE ON ENDE.id  = PE.id_endereco             "
                   +"   INNER JOIN end_logradouro              AS LOGR ON LOGR.id  = ENDE.id_logradouro         "
                   +"   INNER JOIN end_cidade                  AS CID  ON CID.id   = ENDE.id_cidade             "
                   +"   INNER JOIN end_descricao_endereco      AS DE   ON DE.id    = ENDE.id_descricao_endereco "
                   //+"  LEFT JOIN arr_contribuintes_inativos  AS CI   ON CI.id    = JUR.id                   ";
                   +"    LEFT JOIN arr_contribuintes_inativos  AS CI   ON CI.id_juridica = JUR.id AND CI.dt_ativacao IS NULL ";
                   // -- left join contribuintes_inativos i on i.juridica.id=j.id and i.dt_ativacao IS NULL
            if (pEmpresas.equals("todas")) {
                textQuery = textQuery + " WHERE J.id IN(SELECT JC.contabilidade.id FROM Juridica AS JC WHERE JC.contabilidade IS NOT NULL) ";
                textQueryNativa = textQueryNativa + "WHERE JUR.id IN(SELECT jc.id FROM pes_juridica AS jc where jc.id_contabilidade IS NOT NULL)";
            } else if (pEmpresas.equals("semEmpresas")) {
                textQuery = textQuery + " WHERE J.id NOT IN (SELECT JC.contabilidade.id FROM Juridica AS JC WHERE JC.contabilidade IS NOT NULL) ";
                textQueryNativa = textQueryNativa + "WHERE JUR.id NOT IN(SELECT jc.id FROM pes_juridica AS jc where jc.id_contabilidade IS NOT NULL)";
            } else if (pEmpresas.equals("comEmpresas")) {
                textQuery = textQuery + " WHERE J.id IN (SELECT JC.contabilidade.id FROM Juridica JC WHERE JC.contabilidade IS NOT NULL GROUP BY JC.contabilidade.id HAVING COUNT(JC.contabilidade.id) >= " + indexEmp1 + " AND COUNT(JC.contabilidade.id) <= " + indexEmp2 +") ";
                textQueryNativa = textQueryNativa + "WHERE JUR.id_contabilidade IS NOT NULL GROUP BY JUR.id_contabilidade HAVING COUNT(JUR.id_contabilidade) >= " + indexEmp1 + " AND COUNT(JUR.id_contabilidade) <= " + indexEmp2 +")";
            }
            // CIDADE -------------------------------------------------------
            if (tipoPCidade.equals("todas")) {
                textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id and pe.tipoEndereco.id = " + idTipoEndereco;
                textQueryNativa = textQueryNativa + " AND PE.id_tipo_endereco = " + idTipoEndereco;
            } else if (tipoPCidade.equals("especificas")) {
                textQuery = textQuery + " AND PE.pessoa.id = J.pessoa.id AND PE.tipoEndereco.id = " + idTipoEndereco + " AND PE.endereco.cidade.id = " + Integer.parseInt(cidade);
                textQueryNativa = textQueryNativa + " PE.id_tipo_endereco = " + idTipoEndereco + " AND ENDE.id_cidade = " + Integer.parseInt(cidade);
            } else if (tipoPCidade.equals("local")) {
                textQuery = textQuery + " AND PE.pessoa.id = J.pessoa.id AND PE.tipoEndereco.id = " + idTipoEndereco + " AND PE.endereco.cidade.id = " + Integer.parseInt(cidade);
                textQueryNativa = textQueryNativa + " PE.id_tipo_endereco = " + idTipoEndereco + " AND ENDE.id_cidade = " + Integer.parseInt(cidade);
            } else if (tipoPCidade.equals("outras")) {
                textQuery = textQuery + " AND PE.pessoa.id = J.pessoa.id AND PE.tipoEndereco.id = " + idTipoEndereco + " AND PE.endereco.cidade.id <> " + Integer.parseInt(cidade);
                textQueryNativa = textQueryNativa + " AND PE.id_tipo_endereco = " + idTipoEndereco + " AND ENDE.id_cidade <> " + Integer.parseInt(cidade);
            }
            // textQueryNativa = textQueryNativa + "CI.dt_ativacao IS NULL ";
            // CNAES
//            if (cnaes.length() != 0) {
//                textQuery = textQuery + " AND J.cnae.id in ( " + cnaes + " ) ";
//            }
//            textQuery = textQuery + " AND CI.dtInativacao IS NULL ";

            // ORDEM ------------------------------------------------------------------------
            if (ordem.equals("razao")) {
                textQuery = textQuery + " ORDER BY J.pessoa.nome";
                textQueryNativa = textQueryNativa + " ORDER BY PES.ds_nome ASC ";
            } else if (ordem.equals("documento")) {
                textQuery = textQuery + " ORDER BY J.pessoa.documento";
                textQueryNativa = textQueryNativa + " ORDER BY PES.ds_documento ASC ";
            } else if (ordem.equals("endereco")) {
                textQuery = textQuery + " ORDER BY PE.endereco.cidade.uf,                          "
                                      + "          PE.endereco.cidade.cidade,                      "
                                      + "          PE.endereco.logradouro.logradouro,              "
                                      + "          PE.endereco.descricaoEndereco.descricaoEndereco,"
                                      + "          PE.numero ";
                textQueryNativa = textQueryNativa + " ORDER BY CID.ds_uf ASC,                      "
                                      + "                      CID.ds_cidade ASC,                  "
                                      + "                      LOGR.ds_descricao ASC,              "
                                      + "                      DE.ds_descricao ASC,                "
                                      + "                      PE.ds_numero ASC                    ";
            } else if (ordem.equals("cep")) {
                textQuery = textQuery + " ORDER BY PE.endereco.cep,                                "
                                      + "          PE.endereco.cidade.uf,                          "
                                      + "          PE.endereco.cidade.cidade,                      "
                                      + "          PE.endereco.logradouro.logradouro,              "
                                      + "          PE.endereco.descricaoEndereco.descricaoEndereco,"
                                      + "          PE.numero";
                textQueryNativa = textQueryNativa + " ORDER BY ENDE.ds_cep ASC,                    "
                                      + "                      CID.ds_uf  ASC,                     "
                                      + "                      CID.ds_cidade  ASC,                 "
                                      + "                      LOGR.ds_descricao  ASC,             "
                                      + "                      DE.ds_descricao ASC,                "
                                      + "                      PE.ds_numero ASC                    ";                
            }
            Query queryNativa = getEntityManager().createNativeQuery(textQueryNativa);    
            list = (List) queryNativa.getResultList();
            if (!list.isEmpty()) {
                String queryString = "";
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        queryString = ((List) list.get(i)).get(0).toString();
                    } else {
                        queryString += ","+((List) list.get(i)).get(0).toString();
                    }
                }
                Query qry = getEntityManager().createQuery("SELECT J FROM Juridica AS J WHERE J.id IN("+queryString+")");    
                list = qry.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            }
            return list;
        } catch (EJBQLException e) {
            return new ArrayList();
        } catch (Exception e) {
            return new ArrayList();            
        }
    }
}
