package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.IndiceMensal;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class ServicosDBToplink extends DB implements ServicosDB {

    @Override
    public Correcao pesquisaCorrecao(int idServico) {
        Correcao result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c "
                    + "  from Correcao c"
                    + " where c.servicos.id = :pid");
            qry.setParameter("pid", idServico);
            result = (Correcao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public IndiceMensal pesquisaIndiceMensal(int mes, int ano, int idIndice) {
        IndiceMensal result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT I                   "
                    + "  FROM IndiceMensal AS I   "
                    + " WHERE I.mes = :mes        "
                    + "   AND I.ano = :ano        "
                    + "   AND I.indice.id = :i");
            qry.setParameter("mes", mes);
            qry.setParameter("ano", ano);
            qry.setParameter("i", idIndice);
            result = (IndiceMensal) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos(int idRotina) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT S.servicos "
                    + "  FROM ServicoRotina AS S"
                    + " WHERE S.rotina.id = :r");
            qry.setParameter("r", idRotina);
            return (qry.getResultList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaTodosPeloContaCobranca(int idRotina) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT SR.servicos "
                    + "  FROM ServicoRotina AS SR"
                    + " WHERE SR.rotina.id = :r"
                    + "   AND SR.servicos.id IN(SELECT S.servicos.id FROM ServicoContaCobranca AS S)");
            qry.setParameter("r", idRotina);
            return (qry.getResultList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List pesquisaPlano5() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT P5 FROM Plano5 AS P5 ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaServicos(String desc, String por, String como, String situacao) {
        String textQuery = "";
        switch (como) {
            case "P":
                textQuery = "SELECT S.*                             "
                        + "    FROM fin_servicos AS S               "
                        + "  WHERE TRIM(UPPER(FUNC_TRANSLATE(S." + por + "))) LIKE TRIM(UPPER(FUNC_TRANSLATE('%" + desc + "%')))";
                break;
            case "I":
                textQuery = "SELECT S.*                             "
                        + "    FROM fin_servicos AS S               "
                        + "  WHERE TRIM(UPPER(FUNC_TRANSLATE(S." + por + "))) LIKE TRIM(UPPER(FUNC_TRANSLATE('" + desc + "%')))";
                break;
        }
        textQuery += " AND S.ds_situacao LIKE '" + situacao + "'";

        try {
            Query qry = getEntityManager().createNativeQuery(textQuery, Servicos.class);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public Servicos idServicos(Servicos des_servicos) {
        Servicos result = null;
        String descricao = des_servicos.getDescricao().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select ser from Servicos ser where UPPER(ser.descricao) = :d_servicos");
            qry.setParameter("d_servicos", descricao);
            result = (Servicos) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<Servicos> listaServicoSituacao(int id_rotina, String situacao) {
        try {
            String queryString = ""
                    + "  SELECT S.servicos "
                    + "    FROM ServicoRotina AS S"
                    + "   WHERE S.rotina.id = " + id_rotina;

            if (situacao != null && !situacao.isEmpty()) {
                queryString += " AND S.servicos.situacao = '" + situacao + "'";
            }

            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Servicos> listaServicoSituacaoAtivo() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.situacao = 'A' ORDER BY S.descricao ASC ");
            return qry.getResultList();
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    @Override
    public List<Servicos> listaServicosPorSubGrupoFinanceiro(Integer subgrupo) {
        return listaServicosPorSubGrupoFinanceiro(subgrupo, 4);
    }

    @Override
    public List<Servicos> listaServicosPorSubGrupoFinanceiro(Integer subgrupo, Integer rotina) {
        try {
            Query query;
            if (rotina == 4) {
                query = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.subGrupoFinanceiro.id = :subgrupo AND S.situacao = 'A' AND S.id NOT IN (SELECT SR.servicos.id FROM ServicoRotina AS SR WHERE SR.rotina.id = 4 GROUP BY SR.servicos.id) ");
            } else {
                query = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.subGrupoFinanceiro.id = :subgrupo AND S.situacao = 'A' AND S.id IN (SELECT SR.servicos.id FROM ServicoRotina AS SR WHERE SR.rotina.id = :rotina GROUP BY SR.servicos.id) ");
                query.setParameter("rotina", rotina);
            }
            query.setParameter("subgrupo", subgrupo);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

}
