package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConvencaoCidadeDBToplink extends DB implements ConvencaoCidadeDB {

    @Override
    public boolean insert(ConvencaoCidade convencaoCidade) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(convencaoCidade);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(ConvencaoCidade convencaoCidade) {
        try {
            getEntityManager().merge(convencaoCidade);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(ConvencaoCidade convencaoCidade) {
        try {
            getEntityManager().remove(convencaoCidade);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ConvencaoCidade pesquisaCodigo(int id) {
        ConvencaoCidade result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ConvencaoCidade.pesquisaID");
            qry.setParameter("pid", id);
            result = (ConvencaoCidade) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from ConvencaoCidade cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisarGrupos(int idConvencao, int idGrupoCidade) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.grupoCidade "
                    + "  from ConvencaoCidade c"
                    + " where c.convencao.id = :idCon "
                    + " and c.grupoCidade.id = :idGpCid");
            qry.setParameter("idCon", idConvencao);
            qry.setParameter("idGpCid", idGrupoCidade);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ConvencaoCidade pesquisarConvencao(int idConvencao, int idGrupoCidade) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c "
                    + "  from ConvencaoCidade c"
                    + " where c.convencao.id = :idCon "
                    + " and c.grupoCidade.id = :idGpCid");
            qry.setParameter("idCon", idConvencao);
            qry.setParameter("idGpCid", idGrupoCidade);
            return (ConvencaoCidade) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List ListaCidadesConvencao(int idConvencao, int idGrupoCidade) {
        try {
            Query qry = getEntityManager().createQuery(
                    "        select c.id"
                    + "          from ConvencaoCidade cc, "
                    + "               GrupoCidades gc inner join gc.cidade c,"
                    + "               GrupoCidades gc2 inner join gc2.cidade c2 "
                    + "         where gc.grupoCidade.id = cc.grupoCidade.id"
                    + "           and gc2.cidade.id = c2.id "
                    + "           and gc2.grupoCidade.id = :idGpCid "
                    + "           and cc.convencao.id = :idCon"
                    + "           and c.id = c2.id"
                    + "      group by c.id");
            qry.setParameter("idCon", idConvencao);
            qry.setParameter("idGpCid", idGrupoCidade);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public GrupoCidade pesquisaGrupoCidadeJuridica(int idConvencao, int idCidade) {
        try {
            Query qry = getEntityManager().createQuery(
                    "        select gc"
                    + "          from ConvencaoCidade cc inner join cc.grupoCidade gc"
                    + "                                  inner join cc.convencao c,"
                    + "               GrupoCidades gcs inner join gcs.cidade cid"
                    + "         where gc.id = gcs.grupoCidade.id"
                    + "           and cid.id = :idCid "
                    + "           and cc.convencao.id = :idCon"
                    + "      group by gc");
            qry.setParameter("idCon", idConvencao);
            qry.setParameter("idCid", idCidade);
            return (GrupoCidade) qry.getSingleResult();
        } catch (Exception e) {
            return new GrupoCidade();
        }
    }

    @Override
    public List<GrupoCidade> pesquisarGruposPorConvencao(int idConvencao) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.grupoCidade "
                    + "  from ConvencaoCidade c"
                    + " where c.convencao.id = :idCon"
                    + " order by c.grupoCidade.descricao");
            qry.setParameter("idCon", idConvencao);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisarConvencaoPorGrupos(int idGrupoCidade) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.convencao "
                    + "  from ConvencaoCidade c"
                    + " where c.grupoCidade.id = :idGpCid");
            qry.setParameter("idGpCid", idGrupoCidade);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisarConvencaoCidade(List<Integer> listaParametro) {
        try {
            String query = "select c.grupoCidade "
                    + "  from ConvencaoCidade c"
                    + " where c.convencao.id in (";
            int i = 0;
            while (i < listaParametro.size()) {
                query += listaParametro.get(i);
                if ((i + 1) < listaParametro.size()) {
                    query += ",";
                }
                i++;
            }
            query += ") group by c.grupoCidade";
            Query qry = getEntityManager().createQuery(query);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ConvencaoCidade pesquisarConvencaoCidade(int idPessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    " select coci                                                "
                    + "   from Juridica j,                                         "
                    + "        Contribuintes c,                                    "
                    + "        PessoaEndereco pe,                                  "
                    + "        CnaeConvencao cc,                                   "
                    + "        GrupoCidades gc,                                    "
                    + "        ConvencaoCidade coci                                "
                    + "  where c.juridica.id = j.id                                "
                    + "    and cc.cnae.id = j.cnae.id                              "
                    + "    and cc.convencao.id = coci.convencao.id                 "
                    + "    and pe.pessoa.id = j.pessoa.id                          "
                    + "    and pe.tipoEndereco.id = 5                              "
                    + "    and pe.endereco.cidade.id = gc.cidade.id                "
                    + "    and gc.grupoCidade.id = coci.grupoCidade.id             "
                    + "    and j.pessoa.id = :pid");
            qry.setParameter("pid", idPessoa);
            return (ConvencaoCidade) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
