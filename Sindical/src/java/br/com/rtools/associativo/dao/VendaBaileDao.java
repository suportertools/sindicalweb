package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.associativo.EventoBaileConvite;
import br.com.rtools.associativo.EventoBaileMapa;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class VendaBaileDao extends DB {

    public List<EventoBaile> listaBaile(Boolean todos) {
        try {
            String textqry
                    = " SELECT ev.* \n"
                    + "   FROM eve_evento_baile ev \n "
                    + (!todos ? "  WHERE ev.dt_data >= CURRENT_DATE \n " : " \n ")
                    + "  ORDER BY ev.dt_data";

            Query qry = getEntityManager().createNativeQuery(textqry, EventoBaile.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public List<EventoServicoValor> listaEventoServicoValor(Integer id_evento, Integer id_categoria, Integer id_servicos, Boolean is_mesa) {
        try {
            String textqry
                    = "SELECT esv.* \n"
                    + "  FROM eve_evento_servico_valor esv \n "
                    + " WHERE esv.id_evento_servico IN \n "
                    + " ( \n "
                    + "SELECT es.id \n "
                    + "  FROM eve_evento_servico es \n "
                    + " WHERE es.id_evento = " + id_evento + " \n "
                    + (id_categoria != null ? "   AND es.is_socio = TRUE AND es.id_categoria = " + id_categoria + " \n " : "")
                    + //"   AND es.id_servicos = "+id_servicos+" \n " +
                    "   AND es.is_mesa = " + is_mesa + " \n "
                    + " )";

            Query qry = getEntityManager().createNativeQuery(textqry, EventoServicoValor.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public List<EventoBaileMapa> listaEventoBaileMapaPorVenda(Integer id_venda) {
        try {
            String textqry
                    = "SELECT ebm.* \n"
                    + "  FROM eve_evento_baile_mapa ebm \n "
                    + " WHERE ebm.id_venda = " + id_venda;

            Query qry = getEntityManager().createNativeQuery(textqry, EventoBaileMapa.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public List<EventoBaileConvite> listaEventoBaileConvitePorVenda(Integer id_venda) {
        try {
            String textqry
                    = "SELECT ebc.* \n"
                    + "  FROM eve_evento_baile_convite ebc \n "
                    + " WHERE ebc.id_venda = " + id_venda;

            Query qry = getEntityManager().createNativeQuery(textqry, EventoBaileConvite.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public EventoServicoValor pesquisaEventoServicoValor(Integer id_evento, Integer id_categoria, String sexo, Integer idade, Integer id_servico, boolean mesa) {
        try {
            String textqry
                    = "SELECT esv.* \n "
                    + "  FROM eve_evento_servico_valor esv \n "
                    + " WHERE esv.id_evento_servico IN \n "
                    + "     ( \n "
                    + "     SELECT es.id \n "
                    + "       FROM eve_evento_servico es \n "
                    + "      WHERE es.id_evento = " + id_evento + " \n "
                    + "        AND es.is_mesa = " + mesa + " \n "
                    + "        AND es.id_servicos = " + id_servico + " \n "
                    + (id_categoria != null ? "   AND es.is_socio = TRUE AND es.id_categoria = " + id_categoria + " \n " : " AND es.id_categoria IS NULL \n ")
                    + "     ) \n "
                    + "   AND " + idade + " BETWEEN esv.nr_idade_inicial AND esv.nr_idade_final \n "
                    + "   AND (esv.ds_sexo = 'A' OR esv.ds_sexo = '" + sexo + "')";
            Query qry = getEntityManager().createNativeQuery(textqry, EventoServicoValor.class);
            return (EventoServicoValor) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public List<Vector> listaVendasMesa(Integer id_evento) {
        try {
            String textqry
                    = "SELECT e.id AS id, \n "
                    + "       e.dt_emissao AS emissao, \n "
                    + "       ebm.nr_mesa AS mesa, \n "
                    + "       sm.ds_descricao AS status_mesa, \n "
                    + "       p.ds_nome AS nome, \n "
                    + "       pu.ds_nome AS usuario, \n "
                    + "       m.nr_valor AS valor, \n "
                    + "       e.ds_obs AS observacao,  \n "
                    + "       es.id_servicos AS id_servico \n "
                    + "  FROM eve_venda e \n "
                    + "  LEFT JOIN eve_evento_baile_mapa ebm ON e.id = ebm.id_venda \n "
                    + " INNER JOIN eve_status sm ON sm.id = ebm.id_status \n "
                    + " INNER JOIN pes_pessoa p ON p.id = e.id_pessoa \n "
                    + " INNER JOIN seg_usuario u ON u.id = e.id_usuario \n "
                    + " INNER JOIN pes_pessoa pu ON pu.id = u.id_pessoa \n "
                    + "  LEFT JOIN fin_movimento m ON m.id = ebm.id_movimento \n "
                    + " INNER JOIN eve_evento_servico es ON e.id_evento_servico = es.id \n "
                    + " WHERE e.id_evento = " + id_evento
                    + " ORDER BY ebm.nr_mesa ";

            Query qry = getEntityManager().createNativeQuery(textqry);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public List<Vector> listaVendasConvite(Integer id_evento) {
        try {
            String textqry
                    = "SELECT e.id, \n "
                    + "       e.dt_emissao,\n "
                    + "       ebc.nr_convite, \n "
                    + "       sc.ds_descricao AS status_convite, \n "
                    + "       p.ds_nome AS nome, \n "
                    + "       pu.ds_nome AS usuario, \n "
                    + "       m.nr_valor AS valor, \n "
                    + "       e.ds_obs AS observacao,  \n "
                    + "       es.id_servicos AS id_servico \n "
                    + "  FROM eve_venda e \n "
                    + "  LEFT JOIN eve_evento_baile_convite ebc ON e.id = ebc.id_venda \n "
                    + " INNER JOIN eve_status sc ON sc.id = ebc.id_status \n "
                    + " INNER JOIN pes_pessoa p ON p.id = e.id_pessoa \n "
                    + " INNER JOIN seg_usuario u ON u.id = e.id_usuario \n "
                    + " INNER JOIN pes_pessoa pu ON pu.id = u.id_pessoa \n "
                    + "  LEFT JOIN fin_movimento m ON m.id = ebc.id_movimento \n "
                    + " INNER JOIN eve_evento_servico es ON e.id_evento_servico = es.id \n "                                    
                    + " WHERE e.id_evento = " + id_evento
                    + " ORDER BY ebc.nr_convite";

            Query qry = getEntityManager().createNativeQuery(textqry);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public EventoBaileMapa pesquisaMesaDisponivel(Integer id_evento_baile_mapa) {
        try {
            Query qry = getEntityManager().createNativeQuery("SELECT ebm.* FROM eve_evento_baile_mapa ebm WHERE ebm.id = " + id_evento_baile_mapa, EventoBaileMapa.class);
            return (EventoBaileMapa) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public EventoBaileConvite pesquisaConviteDisponivel(Integer id_evento_baile_convite) {
        try {
            Query qry = getEntityManager().createNativeQuery("SELECT ebc.* FROM eve_evento_baile_convite ebc WHERE ebc.id = " + id_evento_baile_convite, EventoBaileConvite.class);
            return (EventoBaileConvite) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
    
    public List listaMovimentosAtrasados(int id_pessoa) {
        try {
            String textqry = 
                      " SELECT * \n "
                    + "   FROM fin_movimento m \n "
                    + "  WHERE m.dt_vencimento < CURRENT_DATE \n "
                    + "    AND m.id_pessoa = " + id_pessoa + " "
                    + "    AND m.id_baixa IS NULL \n "
                    + "    AND m.is_ativo = true "
                    + "    AND m.id_servicos NOT IN (SELECT sr.id_servicos FROM fin_servico_rotina sr WHERE id_rotina = 4)";
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
}
