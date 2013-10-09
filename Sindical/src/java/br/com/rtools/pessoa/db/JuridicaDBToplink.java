package br.com.rtools.pessoa.db;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.MotivoInativacao;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class JuridicaDBToplink extends DB implements JuridicaDB {

    @Override
    public boolean insert(Juridica juridica) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(juridica);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Juridica juridica) {
        try {
            getEntityManager().merge(juridica);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Juridica juridica) {
        try {
            getEntityManager().remove(juridica);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Juridica pesquisaCodigo(int id) {
        Juridica result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Juridica.pesquisaID");
            qry.setParameter("pid", id);
            result = (Juridica) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select jur from Juridica jur ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PessoaEndereco pesquisarPessoaEnderecoJuridica(int id) {
        PessoaEndereco result;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pe "
                    + "  from  PessoaEndereco pe"
                    + " where pe.pessoa.id = :id");
            qry.setParameter("id", id);
            result = (PessoaEndereco) qry.getSingleResult();
        } catch (Exception e) {
            result = null;
        }
        return null;
    }

    @Override
    public List pesquisaPessoa(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (por.equals("fantasia")) {
            por = "fantasia";
            if (como.equals("P")) {

                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(jur." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            } else if (como.equals("I")) {
                por = "fantasia";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(jur." + por + ") like :desc "
                        + " order by jur.pessoa.nome";
            }
        }
        if (por.equals("nome")) {
            por = "nome";
            if (como.equals("P")) {

                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(pes." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(pes." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            }
        }

        if (por.equals("email1") || por.equals("email2")) {
            if (como.equals("P")) {

                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(pes." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            } else if (como.equals("I")) {
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(pes." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            }
        }
        if (por.equals("endereco")) {
            desc = desc.toLowerCase().toUpperCase();
            String queryEndereco = ""
                    + "       SELECT jur.id "
                    + "        FROM pes_pessoa_endereco pesend                                                                                                                               "
                    + "  INNER JOIN pes_pessoa pes ON (pes.id = pesend.id_pessoa)                                                                                                            "
                    + "  INNER JOIN end_endereco ende ON (ende.id = pesend.id_endereco)                                                                                                      "
                    + "  INNER JOIN end_cidade cid ON (cid.id = ende.id_cidade)                                                                                                              "
                    + "  INNER JOIN end_descricao_endereco enddes ON (enddes.id = ende.id_descricao_endereco)                                                                                "
                    + "  INNER JOIN end_bairro bai ON (bai.id = ende.id_bairro)                                                                                                              "
                    + "  INNER JOIN end_logradouro logr ON (logr.id = ende.id_logradouro)                                                                                                    "
                    + "  INNER JOIN pes_juridica jur ON (jur.id_pessoa = pes.id)                                                                                                               "
                    + "  WHERE UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + desc + "%')  "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf) LIKE UPPER('%" + desc + "%')                                "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  ) LIKE UPPER('%" + desc + "%')                                                    "
                    + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                               "
                    + "     OR UPPER(enddes.ds_descricao) LIKE UPPER('%" + desc + "%')                                                                                                           "
                    + "     OR UPPER(cid.ds_cidade) LIKE UPPER('%" + desc + "%')                                                                                                                 "
                    + "     OR UPPER(ende.ds_cep) = '" + desc + "' LIMIT 5000 ";

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
                textQuery = " SELECT JUR FROM Juridica AS JUR WHERE JUR.id IN(" + listaId + ") ORDER BY JUR.pessoa.nome ASC";
            }
        }

        if (por.equals("cnpj") || por.equals("cpf") || por.equals("cei")) {
            por = "documento";
            if (como.equals("P")) {

                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(pes." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            } else if (como.equals("I")) {
                por = "documento";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select jur from Juridica jur, "
                        + "                Pessoa pes     "
                        + " where jur.pessoa.id = pes.id  "
                        + "   and UPPER(pes." + por + ") like :desc"
                        + " order by jur.pessoa.nome";
            }
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
    public CnaeConvencao pesquisaCnaeParaContribuicao(int id) {
        CnaeConvencao result = null;
        try {
            Query qry = getEntityManager().createQuery("select cc from Cnae c,      "
                    + "	      CnaeConvencao   cc"
                    + " where c.id  = cc.cnae.id   "
                    + "   and cc.id = :id_cnae");
            qry.setParameter("id_cnae", id);
            result = (CnaeConvencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaMotivoInativacao() {
        List result;
        try {
            Query qry = getEntityManager().createQuery("select mi from MotivoInativacao mi");
            result = qry.getResultList();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    public Juridica pesquisaJuridicaPorPessoa(int id) {
        Juridica result = null;
        try {
            Query qry = getEntityManager().createQuery("select jur from Juridica jur "
                    + " where jur.pessoa.id = :id_jur"
                    + " order by jur.pessoa.nome");
            qry.setParameter("id_jur", id);
            result = (Juridica) qry.getSingleResult();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    public MotivoInativacao pesquisaCodigoMotivoInativacao(int id) {
        MotivoInativacao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MotivoInativacao.pesquisaID");
            qry.setParameter("pid", id);
            result = (MotivoInativacao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaJuridicaPorDoc(String doc) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select jur from Juridica jur"
                    + "  where jur.pessoa.documento = :doc "
                    + " order by jur.pessoa.nome ");
            qry.setParameter("doc", doc);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaPesEndEmpresaComContabil(int idJurCon) {
        try {
            Query qry = getEntityManager().createQuery("select pe"
                    + "  from PessoaEndereco pe, "
                    + "       Juridica jur "
                    + " where pe.pessoa.id = jur.pessoa.id "
                    + "   and pe.tipoEndereco.id = 3 "
                    + "   and jur.contabilidade.id = :idJurCon");
            qry.setParameter("idJurCon", idJurCon);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaJuridicaComEmail() {
        try {
            Query qry = getEntityManager().createQuery("select jur"
                    + "  from Juridica jur "
                    + " where jur.id <> 1 and jur.pessoa.email1 <> ''");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Juridica> pesquisaJuridicaParaRetorno(String documento) {
        List vetor;
        List<Juridica> listJur = new ArrayList();
        String textQuery = "";
        try {
            textQuery = "select jur.id "
                    + "  from pes_juridica jur,"
                    + "       pes_pessoa pes "
                    + " where pes.id = jur.id_pessoa "
                    + "   and substring('00'||substring(replace( "
                    + " replace( "
                    + "    replace('0000000000'||pes.ds_documento,'/',''),'-',''),'.',''),length( "
                    + " replace( "
                    + "    replace( "
                    + "          replace('0000000000'||pes.ds_documento,'/',''),'-',''),'.',''))-14,length( "
                    + " replace( "
                    + "          replace( "
                    + " replace('0000000000'||pes.ds_documento,'/',''),'-',''),'.',''))),0,16) = '" + documento + "'";

            Query qry = getEntityManager().createNativeQuery(textQuery);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    listJur.add(pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }
            return listJur;
        } catch (EJBQLException e) {
            return listJur;
        }
    }

    @Override
    public int quantidadeEmpresas(int idContabilidade) {
        try {
            Query qry = getEntityManager().createQuery("select count(j) from Juridica j"
                    + " where j.contabilidade.id = " + idContabilidade);
            return Integer.parseInt(String.valueOf(qry.getSingleResult()));
        } catch (EJBQLException e) {
            return -1;
        }
    }

    @Override
    public List listaJuridicaContribuinte(int id_juridica) {
        try {
            String textQuery = "select * from arr_contribuintes_vw where id_juridica = " + id_juridica;
            Query qry = getEntityManager().createNativeQuery(textQuery);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List listaJuridicaContribuinteID() {
        try {
            String textQuery = "select id_juridica from arr_contribuintes_vw";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            return (Vector) qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List listaContabilidadePertencente(int id_juridica) {
        List vetor;
        List listJur = new ArrayList();
        try {
            String textQuery = "select id_juridica from arr_contribuintes_vw where id_contabilidade = " + id_juridica + " and dt_inativacao is null order by ds_nome";

            Query qry = getEntityManager().createNativeQuery(textQuery);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    listJur.add(pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }
            return listJur;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaContabilidade() {
        try {
            Query qry = getEntityManager().createQuery("select jur.contabilidade "
                    + "  from Juridica jur "
                    + " group by jur.contabilidade, jur.contabilidade.pessoa.nome "
                    + " order by jur.contabilidade.pessoa.nome asc");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public int[] listaInadimplencia(int id_juridica) {
        List vetor;
        int[] result = new int[2];


        try {
            // QUERY QUANTIDADE DE MESES INADIMPLENTES -------------------
            String textQry = "select extract(month from  dt_vencimento),extract(year from  dt_vencimento) from fin_movimento where is_ativo=true and id_baixa is null and id_pessoa=" + id_juridica + " and dt_vencimento < '" + DataHoje.data() + "'"
                    + " group by extract(month from  dt_vencimento),extract(year from  dt_vencimento)";

            Query qry = getEntityManager().createNativeQuery(textQry);
            vetor = qry.getResultList();

            if (vetor != null && !vetor.isEmpty()) {
//                for (int i = 0; i < vetor.size(); i++){
                result[0] = vetor.size();
//                }
            } else {
                result[0] = 0;
            }
            // ----------------------------------------------------------

            // QUANTIDADE DE PARCELAS INADIMPLENTES ---------------------
            textQry = "select count(*) from fin_movimento where  is_ativo=true and id_baixa is null and id_pessoa=" + id_juridica + " and dt_vencimento < '" + DataHoje.data() + "'";

            qry = getEntityManager().createNativeQuery(textQry);
            vetor = qry.getResultList();

//          for (int i = 0; i < vetor.size(); i++){
            result[1] = Integer.parseInt(String.valueOf((Long) ((Vector) vetor.get(0)).get(0)));
//           }
            // ----------------------------------------------------------
            return result;
        } catch (Exception e) {
            return result;
        }
    }
}
