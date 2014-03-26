package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEndereco;
import br.com.rtools.associativo.EventoBaileMapa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class EventoBaileDBToplink extends DB implements EventoBaileDB {

    @Override
    public List pesquisaTodosAtuais(Date data) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select r from EventoBaile r where r.data >= :date");
            qry.setParameter("date", data);
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public String excluirBaile(int idEvento) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " delete from eve_evento_servico_valor where id_evento_servico in (select id from eve_evento_servico where id_evento = " + idEvento + ");  "
                    + " delete from eve_evento_baile where id_evento = " + idEvento + " ;                                                                       "
                    + " delete from eve_evento_servico where id_evento = " + idEvento + " ;                                                                     "
                    + " delete from eve_evento_Banda where id_evento = " + idEvento + " ;                                                                       "
                    + " delete from eve_endereco where id_evento = " + idEvento + " ;                                                                           "
                    + " delete from eve_evento where id = " + idEvento + " ;                                                                                   ");
            qry.executeUpdate();
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    @Override
    public AEndereco pesquisaEnderecoEvento(int idEvento) {
        AEndereco aEndereco;
        try {
            Query qry = getEntityManager().createQuery(
                    "select ev "
                    + "  from AEndereco ev"
                    + " where ev.evento.id = " + idEvento);
            aEndereco = (AEndereco) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            aEndereco = new AEndereco();
        }
        return aEndereco;
    }

    @Override
    public List pesquisaEventoDescricao(String desc, String como) {
        List lista;
        String textQuery = null;
        if (como.equals("P")) {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from EventoBaile objeto where UPPER(objeto.evento.descricaoEvento.descricao) like :desc"
                    + " order by objeto.evento.descricaoEvento.descricao";
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from EventoBaile objeto where UPPER(objeto.evento.descricaoEvento.descricao) like :desc"
                    + " order by objeto.evento.descricaoEvento.descricao";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (desc != null) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (EJBQLException e) {
            lista = new ArrayList();
        }
        return lista;
    }

    @Override
    public List listaBaileMapa(int id_baile) {
        String textQuery = "select ebm from EventoBaileMapa ebm where ebm.eventoBaile.id = " + id_baile;
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List listaBaileMapaDisponiveis(int id_baile) {
        String textQuery = "SELECT ebm FROM EventoBaileMapa ebm WHERE ebm.eventoBaile.id = " + id_baile + " AND ebm.id NOT IN(SELECT EM.eventoBaileMapa.id FROM EveMesa AS EM )" ;
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public EventoBaileMapa pesquisaMesaBaile(int id_baile, int mesa) {
        String textQuery = "SELECT EBM FROM EventoBaileMapa AS EBM WHERE EBM.eventoBaile.id = " + id_baile + " AND EBM.mesa = " + mesa;
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            return (EventoBaileMapa) qry.getSingleResult();
        } catch (Exception e) {
            return new EventoBaileMapa();
        }
    }

    @Override
    public List listaMesasEvento(int idEventoBaile) {
        try {
            Query query = getEntityManager().createQuery("SELECT M FROM Mesa AS M WHERE M.eventoBaileMapa.eventoBaile.id = :idEventoBaile");
            query.setParameter("idEventoBaile", idEventoBaile);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
