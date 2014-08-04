package br.com.rtools.pessoa.db;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.principal.DB;
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
    public List pesquisaPessoa(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (por.equals("nome")) {
            por = "nome";
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis"
                        + "  where UPPER(fis.pessoa." + por + ") like :desc";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc";
            }
        }
        if (por.equals("email1") || por.equals("email2")) {
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis, "
                        + "  where UPPER(pes." + por + ") like :desc";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc";
            }
        }
        if (por.equals("rg")) {
            por = "rg";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select fis from Fisica fis "
                    + "  where UPPER(fis." + por + ") like :desc";
        }
        if (por.equals("cpf")) {
            por = "documento";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select fis from Fisica fis "
                    + "  where UPPER(fis.pessoa." + por + ") like :desc";
        }
        if (por.equals("endereco")) {
            desc = desc.toLowerCase().toUpperCase();
            if (desc.isEmpty()) {
                return new Vector<Object>();
            }
            String queryEndereco = ""
                    + "       SELECT fis.id "
                    + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                    + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                    + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                    + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                    + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                    + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                    + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                    + "  INNER JOIN pes_fisica fis ON (fis.id_pessoa = pes.id)                                                                                                               "
                    + "  WHERE UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + desc + "%')  "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf) LIKE UPPER('%" + desc + "%')                                "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  ) LIKE UPPER('%" + desc + "%')                                                    "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                               "
                    + "     OR UPPER(enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                                                           "
                    + "     OR UPPER(cid.ds_cidade) LIKE UPPER('%" + desc + "%')                                                                                                                 "
                    + "     OR UPPER(ende.ds_cep) = '" + desc + "' ";

            Query qryEndereco = getEntityManager().createNativeQuery(queryEndereco);
            List listEndereco = qryEndereco.getResultList();
            String listaId = "";
            if (!listEndereco.isEmpty()) {
                for (int i = 0; i < listEndereco.size(); i++) {
                    if (i == 0) {
                        listaId = ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    } else {
                        listaId += ", " + ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    }
                }
                textQuery = " SELECT FIS FROM Fisica AS FIS WHERE FIS.id IN(" + listaId + ")";
            }
        }

        if (por.equals("codigo")) {
            textQuery
                    = "SELECT pe.fisica FROM PessoaEmpresa pe "
                    + " WHERE pe.codigo = :desc ";
        }

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                if (!por.equals("endereco")) {
                    qry.setParameter("desc", desc);
                }
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public List pesquisaPessoaSocio(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (por.equals("nome")) {
            por = "nome";
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc  where soc.servicoPessoa.ativo = true )";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.servicoPessoa.ativo = true )";
            }
        }

        if (por.equals("email1") || por.equals("email2")) {
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.servicoPessoa.ativo = true )";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.servicoPessoa.ativo = true )";
            }
        }

        if (por.equals("rg")) {
            por = "rg";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select fis from Fisica fis "
                    + "  where UPPER(fis." + por + ") like :desc "
                    + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.servicoPessoa.ativo = true )";
        }
        if (por.equals("cpf")) {
            por = "documento";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select fis from Fisica fis "
                    + "  where UPPER(fis.pessoa." + por + ") like :desc"
                    + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.servicoPessoa.ativo = true )";
        }

        if (por.equals("endereco")) {
            desc = desc.toLowerCase().toUpperCase();
            if (desc.isEmpty()) {
                return new Vector<Object>();
            }
            String queryEndereco = ""
                    + "       SELECT fis.id "
                    + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                    + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                    + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                    + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                    + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                    + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                    + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                    + "  INNER JOIN pes_fisica fis ON (fis.id_pessoa = pes.id)                                                                                                               "
                    + "  WHERE UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + desc + "%')  "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf) LIKE UPPER('%" + desc + "%')                                "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  ) LIKE UPPER('%" + desc + "%')                                                    "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                               "
                    + "     OR UPPER(enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                                                           "
                    + "     OR UPPER(cid.ds_cidade) LIKE UPPER('%" + desc + "%')                                                                                                                 "
                    + "     OR UPPER(ende.ds_cep) = '" + desc + "' ";

            Query qryEndereco = getEntityManager().createNativeQuery(queryEndereco);
            List listEndereco = qryEndereco.getResultList();
            String listaId = "";
            if (!listEndereco.isEmpty()) {
                for (int i = 0; i < listEndereco.size(); i++) {
                    if (i == 0) {
                        listaId = ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    } else {
                        listaId += ", " + ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    }
                }
                textQuery = " SELECT FIS FROM Fisica AS FIS, Pessoa AS PES WHERE FIS.id IN(" + listaId + ") AND PES.id IN( SELECT SOC.servicoPessoa.pessoa.id from Socios AS SOC WHERE soc.servicoPessoa.ativo = true )";
            }
        }

        if (por.equals("matricula")) {
            //por = "documento";
            //desc = desc.toLowerCase().toUpperCase();
            if (!desc.isEmpty()) {
                textQuery = "SELECT fis "
                        + "  FROM Fisica fis "
                        + " WHERE fis.pessoa.id IN ( SELECT soc.servicoPessoa.pessoa.id FROM Socios soc WHERE soc.servicoPessoa.ativo = true AND soc.matriculaSocios.nrMatricula = " + Integer.valueOf(desc) + " )";
            }
        }

        if (por.equals("codigo")) {
            textQuery
                    = "  SELECT pe.fisica FROM PessoaEmpresa pe "
                    + " WHERE pe.codigo = :desc "
                    + "   AND pe.fisica.pessoa.id IN ( SELECT soc.servicoPessoa.pessoa.id FROM Socios soc WHERE soc.servicoPessoa.ativo = true )";
        }

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                if (!por.equals("endereco") && !por.equals("matricula")) {
                    qry.setParameter("desc", desc);
                }
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public List pesquisaPessoaSocioInativo(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (por.equals("nome")) {
            por = "nome";
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc ) "
                        + "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc ) "
                        + "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
            }
        }
        if (por.equals("email1") || por.equals("email2")) {
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc ) "
                        + "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = " select fis from Fisica fis "
                        + "  where UPPER(fis.pessoa." + por + ") like :desc "
                        + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc ) "
                        + "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
            }
        }
        if (por.equals("rg")) {
            por = "rg";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select fis from Fisica fis "
                    + "  where UPPER(fis." + por + ") like :desc "
                    + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc ) "
                    + "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
        }
        if (por.equals("cpf")) {
            por = "documento";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select fis from Fisica fis "
                    + "  where UPPER(fis.pessoa." + por + ") like :desc"
                    + "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc ) "
                    + "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
        }
        if (por.equals("endereco")) {
            desc = desc.toLowerCase().toUpperCase();
            if (desc.isEmpty()) {
                return new Vector<Object>();
            }
            String queryEndereco = ""
                    + "       SELECT fis.id "
                    + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                    + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                    + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                    + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                    + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                    + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                    + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                    + "  INNER JOIN pes_fisica fis ON (fis.id_pessoa = pes.id)                                                                                                               "
                    + "  WHERE UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + desc + "%')  "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf) LIKE UPPER('%" + desc + "%')                                "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  ) LIKE UPPER('%" + desc + "%')                                                    "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                               "
                    + "     OR UPPER(enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                                                           "
                    + "     OR UPPER(cid.ds_cidade) LIKE UPPER('%" + desc + "%')                                                                                                                 "
                    + "     OR UPPER(ende.ds_cep) = '" + desc + "' ";

            Query qryEndereco = getEntityManager().createNativeQuery(queryEndereco);
            List listEndereco = qryEndereco.getResultList();
            String listaId = "";
            if (!listEndereco.isEmpty()) {
                for (int i = 0; i < listEndereco.size(); i++) {
                    if (i == 0) {
                        listaId = ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    } else {
                        listaId += ", " + ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    }
                }
                textQuery = " SELECT FIS                    "
                        + "     FROM Fisica AS FIS,         "
                        + "          Pessoa AS PES          "
                        + "    WHERE FIS.id IN(" + listaId + ") "
                        //+ "      AND FIS.pessoa.id IN( SELECT SOC.servicoPessoa.pessoa.id FROM Socios SOC WHERE SOC.matriculaSocios.motivoInativacao IS NOT NULL )  "
                        + "      AND FIS.pessoa.id IN( SELECT SOC.servicoPessoa.pessoa.id FROM Socios SOC )  "
                        + "      AND FIS.pessoa.id NOT IN( SELECT SOC2.servicoPessoa.pessoa.id FROM Socios SOC2 WHERE SOC2.servicoPessoa.ativo = true )             ";
            }
        }

        if (por.equals("matricula")) {
            if (!desc.isEmpty()) {
                textQuery = "SELECT fis "
                        + "  FROM Fisica fis "
                        + "  WHERE fis.pessoa.id IN ( SELECT soc.servicoPessoa.pessoa.id FROM Socios soc WHERE soc.matriculaSocios.nrMatricula = :desc) "
                        + "    AND fis.pessoa.id NOT IN ( SELECT soc2.servicoPessoa.pessoa.id FROM Socios soc2 WHERE soc2.servicoPessoa.ativo = true AND soc2.matriculaSocios.nrMatricula = " + Integer.valueOf(desc) + " ) ";
            }
        }

        if (por.equals("codigo")) {
            textQuery
                    = " SELECT pe.fisica FROM PessoaEmpresa pe "
                    + " WHERE pe.codigo = :desc "
                    + "   AND pe.fisica.pessoa.id IN ( SELECT soc.servicoPessoa.pessoa.id FROM Socios soc ) "
                    + "   AND pe.fisica.pessoa.id NOT IN ( SELECT soc2.servicoPessoa.pessoa.id FROM Socios soc2 WHERE soc2.servicoPessoa.ativo = true ) ";
        }

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                if (!por.equals("endereco") && !por.equals("matricula")) {
                    qry.setParameter("desc", desc);
                }
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
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
    public List pesquisaFisicaPorDoc(String doc) {
        return pesquisaFisicaPorDoc(doc, true);
    }

    @Override
    public List pesquisaFisicaPorDoc(String doc, boolean like) {
        String documento = doc;
        if (like) {
            documento = "%" + doc + "%";
        }
        try {
            Query qry = getEntityManager().createQuery(
                    "   SELECT FIS                          "
                    + "   FROM Fisica AS FIS,               "
                    + "        Pessoa AS PES                "
                    + "  WHERE FIS.pessoa.id = PES.id       "
                    + "    AND PES.documento LIKE :documento");
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
    public List pesquisaFisicaPorDocSemLike(String doc) {
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
    public List pesquisaFisicaPorNomeNascRG(String nome, Date nascimento, String RG) {
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
