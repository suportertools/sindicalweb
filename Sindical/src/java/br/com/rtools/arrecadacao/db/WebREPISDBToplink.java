package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.RepisMovimento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class WebREPISDBToplink extends DB implements WebREPISDB {

    @Override
    public RepisMovimento pesquisaCodigoRepisMovimento(int id) {
        RepisMovimento repis = new RepisMovimento();
        try {
            Query qry = getEntityManager().createQuery(
                    "select rm from RepisMovimento rm where rm.id = " + id);
            repis = (RepisMovimento) qry.getSingleResult();
        } catch (Exception e) {
        }
        return repis;
    }

    @Override
    public List listaContribuinteWeb() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select pes "
                    + "  from Pessoa pes, "
                    + "       Juridica jur, "
                    + "       CnaeConvencao cnaeCon "
                    + " where jur.pessoa.id = pes.id "
                    + "   and cnaeCon.cnae.id = jur.cnae.id order by pes.nome ASC ");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaContabilidadeWeb() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select pes "
                    + "  from Pessoa pes, "
                    + "       Juridica jur "
                    + " where jur.pessoa.id = pes.id"
                    + "   and jur.id in (select j.contabilidade.id from Juridica j where j.contabilidade.id is not null) order by pes.nome ASC");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List validaPessoaRepisAno(int idPessoa, int ano) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select rm "
                    + "  from RepisMovimento rm "
                    + " where rm.pessoa.id = :idPessoa "
                    + "   and rm.ano = :ano ");
            qry.setParameter("idPessoa", idPessoa);
            qry.setParameter("ano", ano);
            result = qry.getResultList();
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    @Override
    public List listaProtocolosPorContabilidade(int idPessoa, int ano) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "  select rm "
                    + "    from RepisMovimento rm "
                    + "   where rm.pessoa.id in ("
                    + "select j.pessoa.id "
                    + "from Juridica j "
                    + "where j.contabilidade.pessoa.id = :idPessoa"
                    + ") and rm.ano = :ano");
            qry.setParameter("idPessoa", idPessoa);
            qry.setParameter("ano", ano);

            result = qry.getResultList();

            return result;
        } catch (Exception e) {
            return result;
        }
    }

    @Override
    public List listaProtocolosPorPatronal(int idConvencao, int idGrupoCidade) {
        List<RepisMovimento> result = new ArrayList();
        List vetor;
        String text = " select m.id "
                + "   from arr_contribuintes_vw as c "
                + "  inner join pes_pessoa          as p  on p.id=c.id_pessoa "
                + "  inner join pes_pessoa_endereco as pe on pe.id_pessoa=p.id and id_tipo_endereco = 5 "
                + "  inner join endereco_vw         as e  on e.id=pe.id_endereco "
                + "  inner join arr_patronal        as pt on pt.id_convencao = c.id_convencao and pt.id_grupo_cidade =c.id_grupo_cidade "
                + "  inner join arr_repis_movimento as m  on m.id_pessoa=c.id_pessoa "
                + "  inner join arr_repis_status    as s  on s.id=m.id_repis_status "
                + "  where c.dt_inativacao is null "
                + "    and c.id_convencao = " + idConvencao
                + "    and c.id_grupo_cidade = " + idGrupoCidade;
        try {
            Query qry = getEntityManager().createNativeQuery(text);

            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    result.add(pesquisaCodigoRepisMovimento((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }

            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * <p>Mudança de estrutura.</p>
     * @deprecated 
     * @param id_patronal
     * @return 
     */
    @Override
    public List listaProtocolosPorPatronalCnae(int id_patronal) {
        List<RepisMovimento> result = new ArrayList();
        List vetor;
        String text = " select m.id "
                + "   from arr_contribuintes_vw as c "
                + "  inner join pes_pessoa          as p  on p.id = c.id_pessoa "
                + "  inner join pes_juridica        as j  on j.id_pessoa = p.id "
                + "  inner join arr_patronal        as pt on pt.id = " + id_patronal + " and  pt.id_grupo_cidade = c.id_grupo_cidade"
                + "  inner join arr_repis_movimento as m  on m.id_pessoa = c.id_pessoa "
                + "  inner join arr_repis_status    as s  on s.id = m.id_repis_status "
                + "  inner join arr_patronal_cnae   as cp on cp.id_cnae = j.id_cnae and cp.id_patronal = " + id_patronal + "  "
                + "  where c.dt_inativacao is null order by m.dt_emissao desc";
        try {
            Query qry = getEntityManager().createNativeQuery(text);

            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    result.add(pesquisaCodigoRepisMovimento((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }

            return result;
        } catch (Exception e) {
            return result;
        }
    }

    @Override
    public List<RepisMovimento> listaProtocolosPorPatronal(int idPatronal) {
        try {
            Query query = getEntityManager().createQuery("SELECT RM FROM RepisMovimento AS RM WHERE RM.patronal.id = :p1 ORDER BY RM.dataEmissao DESC");
            query.setParameter("p1", idPatronal);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public Patronal pesquisaPatronalPorPessoa(int idPessoa) {
        Patronal patronal = new Patronal();
        try {
            Query qry = getEntityManager().createQuery("select p from Patronal p where p.pessoa.id = " + idPessoa);
            patronal = (Patronal) qry.getSingleResult();
        } catch (Exception e) {
        }
        return patronal;
    }

    @Override
    public Patronal pesquisaPatronalPorConvGrupo(int id_convencao, int id_grupo_cidade) {
        Patronal patronal = new Patronal();
        try {
            Query qry = getEntityManager().createQuery("select p from Patronal p where p.convencao.id = " + id_convencao + " and p.grupoCidade.id = " + id_grupo_cidade);
            patronal = (Patronal) qry.getSingleResult();
        } catch (Exception e) {
        }
        return patronal;
    }

    @Override
    public PisoSalarialLote pesquisaPisoSalarial(int ano, int id_patronal, int id_porte) {
        PisoSalarialLote ps = new PisoSalarialLote();
        try {
            Query qry = getEntityManager().createQuery("SELECT PS FROM PisoSalarialLote AS PS WHERE PS.ano = :p1 AND PS.patronal.id = :p2 AND PS.porte.id = :p3");
            qry.setParameter("p1", ano);
            qry.setParameter("p2", id_patronal);
            qry.setParameter("p3", id_porte);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                ps = (PisoSalarialLote) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return ps;
    }

    @Override
    public List<PisoSalarial> listaPisoSalarialLote(int id_piso_lote) {
        List<PisoSalarial> lista = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select ps from PisoSalarial ps where ps.pisoLote.id = " + id_piso_lote + " order by ps.id");
            lista = qry.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }

    @Override
    public boolean pesquisaCnaePermitido(int id_cnae, int id_grupo_cidade) {
        List vetor;
        try {
            Query qry = getEntityManager().createNativeQuery("");
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<RepisMovimento> listaRepisMovimento(String por, String descricao) {
        List result = new ArrayList();
        try {
            String text
                    = "SELECT rm "
                    + "  FROM RepisMovimento rm ";

            if (por.equals("nome")) {
                text += " WHERE rm.pessoa.nome LIKE '%" + descricao + "%'";
            }

            Query qry = getEntityManager().createQuery(text);
            return qry.getResultList();
        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public Juridica pesquisaEscritorioDaEmpresa(int id_pessoa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT j.contabilidade FROM Juridica j where j.pessoa.id = " + id_pessoa);

            return (Juridica) qry.getSingleResult();
        } catch (Exception e) {
            return new Juridica();
        }
    }

    @Override
    public List<Movimento> listaAcordoAberto(int id_pessoa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT m FROM Movimento m WHERE m.tipoServico.id = 4 AND m.baixa IS NULL AND m.pessoa.id = " + id_pessoa);

            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList<Movimento>();
        }
    }

    @Override
    public Patronal pesquisaPatronalPorSolicitante(int id_solicitante) {
        try {
            Query queryNative = getEntityManager().createNativeQuery(""
                    + "SELECT C.id_convencao,            "
                    + "       C.id_grupo_cidade          "
                    + "  FROM arr_contribuintes_vw AS C  "
                    + " WHERE C.id_pessoa = " + id_solicitante);
            List list = queryNative.getResultList();
            if (!list.isEmpty()) {
                Query query = getEntityManager().createQuery("SELECT PC.patronal FROM PatronalConvencao AS PC WHERE PC.convencao.id = :p1 AND PC.grupoCidade.id = :p2");
                int idConvencao = Integer.parseInt(((List) list.get(0)).get(0).toString());
                int idGrupoCidade = Integer.parseInt(((List) list.get(0)).get(1).toString());
                query.setParameter("p1", idConvencao);
                query.setParameter("p2", idGrupoCidade);
                if (!query.getResultList().isEmpty()) {
                    return (Patronal) query.getSingleResult();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
