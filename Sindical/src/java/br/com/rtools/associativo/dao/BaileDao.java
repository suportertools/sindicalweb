package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.Banda;
import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.EventoServico;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class BaileDao extends DB {

    public List listaCategoriaPorEventoServico(Integer id_servicos, String sexo, Integer faixaInicial, Integer faixaFinal, Integer id_evento) {
        try {
            String textqry
                    = " SELECT c.* \n"
                    + "   FROM soc_categoria c \n "
                    + "  WHERE c.id NOT IN ("
                    + "     SELECT es.id_categoria \n "
                    + "       FROM eve_evento_servico es \n "
                    + "      INNER JOIN eve_evento_servico_valor esv ON esv.id_evento_servico = es.id "
                    + "      WHERE es.id_servicos = " + id_servicos
                    + "        AND es.id_categoria IS NOT NULL "
                    + "        AND es.id_evento = " + id_evento
                    + "        AND esv.nr_idade_inicial = " + faixaInicial
                    + "        AND esv.nr_idade_final = " + faixaFinal
                    + "        AND esv.ds_sexo = '" + sexo + "'"
                    + " ) "
                    + " ORDER BY c.ds_categoria";

            Query qry = getEntityManager().createNativeQuery(textqry, Categoria.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public List<Banda> listaBanda() {
        try {
            String textqry
                    = "";
            Query qry = getEntityManager().createNativeQuery(textqry, Categoria.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public EventoServico pesquisaEventoServico(Integer id_servicos, Integer id_categoria, Integer id_evento, Integer faixaInicial, Integer faixaFinal, String sexo) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT es.* \n "
                    + "  FROM eve_evento_servico es \n "
                    + " WHERE es.id_servicos = " + id_servicos + " \n "
                    + "   AND es.id_evento = " + id_evento + " \n "
                    + "   AND es.id_categoria " + (id_categoria == null ? " IS NULL" : " = " + id_categoria + " \n "
                            + "   AND esv.nr_idade_inicial = " + faixaInicial + " \n "
                            + "   AND esv.nr_idade_final = " + faixaFinal + " \n "
                            + "   AND esv.ds_sexo = '" + sexo + "' \n "), EventoServico.class
            );
            return (EventoServico) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public List<EventoServicoValor> listaServicoValorPorEvento(int idEvento) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT esv.* \n "
                    + "  FROM eve_evento_servico_valor esv \n "
                    + " INNER JOIN eve_evento_servico es ON es.id = esv.id_evento_servico \n "
                    + " INNER JOIN eve_evento e ON e.id = es.id_evento \n "
                    + "  LEFT JOIN soc_categoria c ON c.id = es.id_categoria \n "
                    + " INNER JOIN fin_servicos s ON s.id = es.id_servicos \n "
                    + " WHERE e.id = " + idEvento + " \n "
                    + " ORDER BY s.ds_descricao, c.ds_categoria", EventoServicoValor.class
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

}
