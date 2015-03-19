package br.com.rtools.pessoa.db;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class FisicaDBToplink extends DB implements FisicaDB {

    @Override
    public boolean insert(Fisica fisica) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(fisica);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Fisica fisica) {
        try {
            getEntityManager().merge(fisica);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Fisica fisica) {
        try {
            getEntityManager().remove(fisica);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Fisica pesquisaCodigo(int id) {
        Fisica result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Fisica.pesquisaID");
            qry.setParameter("pid", id);
            result = (Fisica) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select fis from Fisica fis ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Fisica> pesquisaPessoa(String desc, String por, String como) {
        if (desc.isEmpty()) {
            return new ArrayList();
        }
        try {
            String textQuery = "";

            desc = AnaliseString.normalizeLower(desc);
            desc = (como.equals("I") ? desc + "%" : "%" + desc + "%");

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

            int maxResults = 300;
            if (desc.length() == 1) {
                maxResults = 50;
            } else if (desc.length() == 2) {
                maxResults = 150;
            } else if (desc.length() == 3) {
                maxResults = 200;
            }

            if (por.equals("endereco")) {
                textQuery
                        = "       SELECT fis.* "
                        + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                        + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                        + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                        + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                        + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                        + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                        + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                        + "  INNER JOIN pes_fisica fis ON (fis.id_pessoa = pes.id)                                                                                                               "
                        + "  WHERE (LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)) LIKE '%" + desc + "%' "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf)) LIKE '%" + desc + "%'                     "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  )) LIKE '%" + desc + "%'                                         "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao)) LIKE '%" + desc + "%'                                                                    "
                        + "     OR LOWER(FUNC_TRANSLATE(enddes.ds_descricao)) LIKE '%" + desc + "%'                                                                                                "
                        + "     OR LOWER(FUNC_TRANSLATE(cid.ds_cidade)) LIKE '%" + desc + "%'                                                                                                      "
                        + "     OR LOWER(FUNC_TRANSLATE(ende.ds_cep)) = '" + desc + "')"
                        + "    AND pesend.id_tipo_endereco = 1 "
                        + "  ORDER BY pes.ds_nome LIMIT " + maxResults;

            } else if (por.equals("codigo")) {
                textQuery
                        = "      SELECT F.*                                             "
                        + "      FROM pes_fisica AS F                                   "
                        + "  INNER JOIN pes_pessoa AS P ON P.id = F.id_pessoa           "
                        + "  INNER JOIN pes_pessoa_empresa PE ON F.id = PE.id_fisica    "
                        + "       WHERE PE.ds_codigo LIKE '" + desc + "'                "
                        + "    ORDER BY P.ds_nome LIMIT " + maxResults;
            } else {

                textQuery = "    SELECT F.*                                             "
                        + "        FROM pes_fisica AS F                                 "
                        + "  INNER JOIN pes_pessoa AS P ON P.id = F.id_pessoa           "
                        + "       WHERE LOWER(FUNC_TRANSLATE(" + field + ")) LIKE '" + desc + "'"
                        + "       ORDER BY P.ds_nome LIMIT " + maxResults;
            }

            Query query = getEntityManager().createNativeQuery(textQuery, Fisica.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }

            //        List<Vector> result_list = qry.getResultList();
            //        List<Fisica> return_list = new ArrayList<Fisica>();
            //
            //        for (Vector result_list1 : result_list) {
            //            return_list.add((Fisica) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) result_list1.get(0), "Fisica"));
            //        }        
            //        return return_list;            
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaPessoaSocio(String desc, String por, String como) {
        if (desc.isEmpty()) {
            return new ArrayList();
        }
        try {
            String textQuery = "";

            desc = AnaliseString.normalizeLower(desc);
            desc = (como.equals("I") ? desc + "%" : "%" + desc + "%");

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

            int maxResults = 300;
            if (desc.length() == 1) {
                maxResults = 50;
            } else if (desc.length() == 2) {
                maxResults = 150;
            } else if (desc.length() == 3) {
                maxResults = 200;
            }

            if (por.equals("endereco")) {
                textQuery
                        = "       SELECT fis.* "
                        + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                        + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                        + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                        + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                        + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                        + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                        + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                        + "  INNER JOIN pes_fisica fis ON (fis.id_pessoa = pes.id)                                                                                                               "
                        + "  WHERE (LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)) LIKE '%" + desc + "%' "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf)) LIKE '%" + desc + "%'                     "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  )) LIKE '%" + desc + "%'                                         "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao)) LIKE '%" + desc + "%'                                                                    "
                        + "     OR LOWER(FUNC_TRANSLATE(enddes.ds_descricao)) LIKE '%" + desc + "%'                                                                                                "
                        + "     OR LOWER(FUNC_TRANSLATE(cid.ds_cidade)) LIKE '%" + desc + "%'                                                                                                      "
                        + "     OR LOWER(FUNC_TRANSLATE(ende.ds_cep)) = '" + desc + "'"
                        + "  ) "
                        + "  AND pesend.id_tipo_endereco = 1 "
                        + "  AND pes.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "  ) "
                        + "  ORDER BY pes.ds_nome LIMIT " + maxResults;

            } else if (por.equals("matricula")) {
                textQuery
                        = " SELECT f.* FROM pes_fisica f "
                        + "  INNER JOIN pes_pessoa p ON p.id = f.id_pessoa "
                        + "  WHERE p.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          INNER JOIN matr_socios ms ON  ms.id = s.id_matricula_socios "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "            AND ms.nr_matricula = " + desc.replace("%", "")
                        + "    ) "
                        + "  ORDER BY p.ds_nome LIMIT " + maxResults;
            } else if (por.equals("codigo")) {
                textQuery
                        = " SELECT f.* FROM pes_fisica f "
                        + "  INNER JOIN pes_pessoa p ON p.id = f.id_pessoa "
                        + "  INNER JOIN pes_pessoa_empresa pe ON f.id = pe.id_fisica "
                        + "  WHERE pe.ds_codigo LIKE '" + desc + "'"
                        + "    AND p.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "    ) "
                        + "  ORDER BY p.ds_nome LIMIT " + maxResults;
            } else {
                textQuery
                        = " SELECT f.* FROM pes_fisica f "
                        + "  INNER JOIN pes_pessoa p ON p.id = f.id_pessoa "
                        + "  WHERE LOWER(FUNC_TRANSLATE(" + field + ")) LIKE '" + desc + "'"
                        + "    AND p.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "    ) "
                        + "  ORDER BY p.ds_nome LIMIT " + maxResults;
            }

            Query query = getEntityManager().createNativeQuery(textQuery, Fisica.class);

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

    @Override
    public List pesquisaPessoaSocioInativo(String desc, String por, String como) {
        if (desc.isEmpty()) {
            return new ArrayList();
        }
        try {
            String textQuery = "";

            desc = AnaliseString.normalizeLower(desc);
            desc = (como.equals("I") ? desc + "%" : "%" + desc + "%");

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

            int maxResults = 300;
            if (desc.length() == 1) {
                maxResults = 50;
            } else if (desc.length() == 2) {
                maxResults = 150;
            } else if (desc.length() == 3) {
                maxResults = 200;
            }

            if (por.equals("endereco")) {
                textQuery
                        = "       SELECT fis.* "
                        + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                        + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                        + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                        + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                        + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                        + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                        + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                        + "  INNER JOIN pes_fisica fis ON (fis.id_pessoa = pes.id)                                                                                                               "
                        + "  WHERE (LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)) LIKE '%" + desc + "%' "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf)) LIKE '%" + desc + "%'                     "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  )) LIKE '%" + desc + "%'                                         "
                        + "     OR LOWER(FUNC_TRANSLATE(logr.ds_descricao || ' ' || enddes.ds_descricao)) LIKE '%" + desc + "%'                                                                    "
                        + "     OR LOWER(FUNC_TRANSLATE(enddes.ds_descricao)) LIKE '%" + desc + "%'                                                                                                "
                        + "     OR LOWER(FUNC_TRANSLATE(cid.ds_cidade)) LIKE '%" + desc + "%'                                                                                                      "
                        + "     OR LOWER(FUNC_TRANSLATE(ende.ds_cep)) = '" + desc + "'"
                        + "  ) "
                        + "  AND pesend.id_tipo_endereco = 1 "
                        + "  AND pes.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "  ) "
                        + "  AND pes.id NOT IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "  ) "
                        + "  ORDER BY pes.ds_nome LIMIT " + maxResults;

            } else if (por.equals("matricula")) {
                textQuery
                        = " SELECT f.* FROM pes_fisica f "
                        + "  INNER JOIN pes_pessoa p ON p.id = f.id_pessoa "
                        + "  WHERE p.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          INNER JOIN matr_socios ms ON  ms.id = s.id_matricula_socios "
                        + "            AND ms.nr_matricula = " + desc.replace("%", "")
                        + "  ) "
                        + "  AND p.id NOT IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "  ) "
                        + "  ORDER BY p.ds_nome LIMIT " + maxResults;
            } else if (por.equals("codigo")) {
                textQuery
                        = " SELECT f.* FROM pes_fisica f "
                        + "  INNER JOIN pes_pessoa p ON p.id = f.id_pessoa "
                        + "  INNER JOIN pes_pessoa_empresa pe ON f.id = pe.id_fisica "
                        + "  WHERE pe.ds_codigo LIKE '" + desc + "'"
                        + "    AND p.id IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "    ) "
                        + "    AND p.id NOT IN ( "
                        + "         SELECT p2.id FROM fin_servico_pessoa sp "
                        + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                        + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                        + "          WHERE sp.is_ativo = TRUE "
                        + "    ) "
                        + "  ORDER BY p.ds_nome LIMIT " + maxResults;
            } else {
                if (!field.isEmpty()) {
                    textQuery
                            = " SELECT f.* FROM pes_fisica f "
                            + "  INNER JOIN pes_pessoa p ON p.id = f.id_pessoa "
                            + "  WHERE LOWER(FUNC_TRANSLATE(" + field + ")) LIKE '" + desc + "'"
                            + "    AND p.id IN ( "
                            + "         SELECT p2.id FROM fin_servico_pessoa sp "
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                            + "    ) "
                            + "    AND p.id NOT IN ( "
                            + "         SELECT p2.id FROM fin_servico_pessoa sp "
                            + "          INNER JOIN soc_socios s ON sp.id = s.id_servico_pessoa "
                            + "          INNER JOIN pes_pessoa p2 ON  p2.id = sp.id_pessoa "
                            + "          WHERE sp.is_ativo = TRUE "
                            + "    ) "
                            + "  ORDER BY p.ds_nome LIMIT " + maxResults;
                } else {
                    textQuery = "";
                }
            }
            if (!textQuery.isEmpty()) {
                Query query = getEntityManager().createNativeQuery(textQuery, Fisica.class);

                List list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
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
//        return return_list;
    }

    @Override
    public Fisica idFisica(Fisica des_fisica) {
        Fisica result = null;
        String descricao = des_fisica.getPessoa().getNome().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select nom from Fisica nom where UPPER(nom.fisica) = :d_fisica");
            qry.setParameter("d_fisica", descricao);
            result = (Fisica) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<Fisica> pesquisaFisicaPorDoc(String doc) {
        return pesquisaFisicaPorDoc(doc, true);
    }

    @Override
    public List<Fisica> pesquisaFisicaPorDoc(String doc, boolean like) {
        String documento = doc;
        if (like) {
            documento = "%" + doc + "%";
        }
        try {
            Query qry = getEntityManager().createQuery("SELECT FIS FROM Fisica AS FIS WHERE FIS.pessoa.documento LIKE :documento");
            qry.setParameter("documento", documento);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<Fisica> pesquisaFisicaPorDocRG(String doc) {
        String documento = doc;
        try {
            Query qry = getEntityManager().createQuery("SELECT FIS FROM Fisica AS FIS WHERE FIS.rg LIKE :documento");
            qry.setParameter("documento", documento);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List<Fisica> pesquisaFisicaPorDocSemLike(String doc) {
        try {
            Query qry = getEntityManager().createQuery(
                    "   SELECT FIS                           "
                    + "   FROM Fisica AS FIS                 "
                    + "  WHERE FIS.pessoa.documento LIKE :documento ");
            qry.setParameter("documento", doc);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public Fisica pesquisaFisicaPorPessoa(int idPessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select f"
                    + "  from Fisica f "
                    + " where f.pessoa.id = :pid");
            qry.setParameter("pid", idPessoa);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Fisica) qry.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Fisica> pesquisaFisicaPorNomeNascRG(String nome, Date nascimento, String RG) {
        String textQuery = "";
        try {
            if (RG.isEmpty() && nascimento != null) {
                textQuery = "select f"
                        + "  from Fisica f "
                        + " where UPPER(f.pessoa.nome) like '" + nome.toLowerCase().toUpperCase() + "'"
                        + "   and f.dtNascimento = :nasc";
            } else if (!RG.isEmpty()) {
                textQuery = "select f"
                        + "  from Fisica f "
                        + " where f.rg = '" + RG + "'";
            } else {
                return new ArrayList();
            }
            Query qry = getEntityManager().createQuery(textQuery);
            if (RG.isEmpty()) {
                qry.setParameter("nasc", nascimento);
            }
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaFisicaPorNome(String nome) {
        try {
            String textQuery = "select f "
                    + "  from Fisica f "
                    + " where UPPER(f.pessoa.nome) like '%" + nome + "%'";
            Query qry = getEntityManager().createQuery(textQuery);

            qry.setMaxResults(200);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaPessoaSocioID(int id_pessoa) {
        List lista = new Vector<Object>();
        String textQuery = null;

        try {
            textQuery = "select fis from Fisica fis, "
                    + //"                 Pessoa pes     " +
                    "  where fis.pessoa.id = " + id_pessoa
                    + "   and pes.id in ( select soc.servicoPessoa.pessoa.id from Socios soc "
                    + " where soc.servicoPessoa.ativo = true )";
            Query qry = getEntityManager().createQuery(textQuery);
            lista = qry.getResultList();
        } catch (Exception e) {
            return lista;
        }
        return lista;
    }

    @Override
    public List<ServicoPessoa> listaServicoPessoa(int id_pessoa, boolean dependente) {
        List lista = new Vector<Object>();
        String textQuery = "SELECT sp FROM ServicoPessoa sp WHERE sp.ativo = TRUE";

        if (dependente) {
            textQuery += " AND sp.pessoa.id = " + id_pessoa;
        } else {
            //textQuery += " AND sp.cobranca.id = "+id_pessoa+" OR sp.pessoa.id = "+id_pessoa;
            textQuery += " AND sp.cobranca.id = " + id_pessoa + " OR (sp.pessoa.id = " + id_pessoa + " AND sp.ativo = TRUE)";
        }

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            lista = qry.getResultList();
        } catch (Exception e) {
            return lista;
        }
        return lista;
    }

    @Override
    public Fisica pesquisaFisicaPorNomeNascimento(String nome, Date nascimento) {
        if (nome.isEmpty() && nascimento != null) {
            return null;
        }
        try {
            Query query = getEntityManager().createQuery("SELECT F FROM Fisica AS F WHERE UPPER(F.pessoa.nome) LIKE :nome AND F.dtNascimento = :nascimento");
            query.setParameter("nascimento", nascimento);
            query.setParameter("nome", nome);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Fisica) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
