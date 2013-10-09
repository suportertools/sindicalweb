package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConvencaoPeriodoDBTopLink extends DB implements ConvencaoPeriodoDB {

    @Override
    public ConvencaoPeriodo pesquisaCodigo(int id) {
        ConvencaoPeriodo convencaoPeriodo = new ConvencaoPeriodo();
        try {
            Query qry = getEntityManager().createQuery(" select cp from ConvencaoPeriodo cp where cp.id = :id ");
            qry.setParameter("id", id);
            if (!qry.getResultList().isEmpty()) {
                convencaoPeriodo = (ConvencaoPeriodo) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return convencaoPeriodo;
    }

    @Override
    public List listaGrupoCidadePorConvencao(int idConvencao) {
        List<ConvencaoCidade> lista;
        try {
            Query query = getEntityManager().createQuery("      "
                    + "     SELECT cc                           "
                    + "       FROM ConvencaoCidade cc           "
                    + "      WHERE cc.convencao.id = :id        "
                    + "   ORDER BY cc.grupoCidade.descricao ASC ");
            query.setParameter("id", idConvencao);
            if (!query.getResultList().isEmpty()) {
                lista = query.getResultList();
                return lista;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public boolean convencaoPeriodoExiste(ConvencaoPeriodo convencaoPeriodo) {
        try {
            Query query = getEntityManager().createQuery("                      "
                    + "     SELECT cp                                           "
                    + "       FROM ConvencaoPeriodo cp                          "
                    + "      WHERE cp.convencao.id = :convencao                 "
                    + "        AND cp.grupoCidade.id = :grupoCidade             "
                    + "        AND cp.referenciaInicial = :referenciaInicial    "
                    + "        AND cp.referenciaFinal = :referenciaFinal        ");
            query.setParameter("convencao", convencaoPeriodo.getConvencao().getId());
            query.setParameter("grupoCidade", convencaoPeriodo.getGrupoCidade().getId());
            query.setParameter("referenciaInicial", convencaoPeriodo.getReferenciaInicial());
            query.setParameter("referenciaFinal", convencaoPeriodo.getReferenciaFinal());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public List<ConvencaoPeriodo> listaConvencaoPeriodo() {
        try {
            Query query = getEntityManager().createQuery("      "
                    + "     SELECT cp                           "
                    + "       FROM ConvencaoPeriodo cp          "
                    + "   ORDER BY cp.id DESC                   ");
            if (!query.getResultList().isEmpty()) {
                return (List<ConvencaoPeriodo>) query.getResultList();
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public ConvencaoPeriodo convencaoPeriodoConvencaoGrupoCidade(int idConvencao, int idGrupoCidade) {
        ConvencaoPeriodo convencaoPeriodo = new ConvencaoPeriodo();
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        String queryString = "";
        if (idConvencao > 0 && idGrupoCidade > 0) {
            queryString = ""
                    + " SELECT id "
                    + "   FROM arr_convencao_periodo "
                    + "  WHERE to_char(CURRENT_DATE, 'YYYY-MM') BETWEEN concat(SUBSTRING(TRANSLATE(ds_referencia_inicial, '/', '-'), 4, 9),'-', substring(TRANSLATE(ds_referencia_inicial, '/', '-'), 0,3)) "
                    + "    AND concat(substring(TRANSLATE(ds_referencia_final, '/', '-'), 4, 9),'-',substring(TRANSLATE(ds_referencia_final, '/', '-'), 0,3)) "
                    + "    AND id_convencao = " + idConvencao + " "
                    + "    AND id_grupo_cidade = " + idGrupoCidade + " LIMIT 1";
        }
        List list;
        try {
            if (idConvencao > 0 && idGrupoCidade > 0) {
                Query query = getEntityManager().createNativeQuery(queryString);
                if (!query.getResultList().isEmpty()) {
                    list = (List) query.getSingleResult();
                    convencaoPeriodo = (ConvencaoPeriodo) dB.pesquisaCodigo((Integer) list.get(0), "ConvencaoPeriodo");
                }
            }
        } catch (Exception e) {
        }
        return convencaoPeriodo;
    }
}
