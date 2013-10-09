package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEndereco;
import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.principal.DB;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class EventoBaileDBToplink extends DB implements EventoBaileDB {

    public void abrirTransacao() {
        getEntityManager().getTransaction().begin();
    }

    public void comitarTransacao() {
        getEntityManager().getTransaction().commit();
    }

    public void desfazerTransacao() {
        getEntityManager().getTransaction().rollback();
    }

    public boolean insert(EventoBaile eventoBaile) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(eventoBaile);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(EventoBaile eventoBaile) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(eventoBaile);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(EventoBaile eventoBaile) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(eventoBaile);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public EventoBaile pesquisaCodigo(int id) {
        EventoBaile result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("EventoBaile.pesquisaID");
            qry.setParameter("pid", id);
            result = (EventoBaile) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select r from EventoBaile r");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

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

    public AEndereco pesquisaEnderecoEvento(int idEvento) {
        AEndereco aEndereco = new AEndereco();
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
        List lista = new Vector<Object>();
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
            lista = new Vector<Object>();
        }
        return lista;
    }
}
