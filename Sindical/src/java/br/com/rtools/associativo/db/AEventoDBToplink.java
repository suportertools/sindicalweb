package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class AEventoDBToplink extends DB implements AEventoDB{
    public boolean insert(AEvento aEvento) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(aEvento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(AEvento aEvento) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(aEvento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(AEvento aEvento) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(aEvento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public AEvento pesquisaCodigo(int id) {
        AEvento result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("AEvento.pesquisaID");
            qry.setParameter("pid", id);
            result = (AEvento) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select r from AEvento r");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public AEvento pesquisaEvento(EventoBaile eventoBaile) {
        AEvento aEvento = new AEvento();
        try{
            Query qry = getEntityManager().createQuery(
                    "select eb.evento " +
                    "  from EventoBaile eb " +
                    " where eb.evento.descricaoEvento.id = :id" +
                    "   and eb.data = :data" +
                    "   and eb.horaInicio = :hora"
            );
            qry.setParameter("id", eventoBaile.getEvento().getDescricaoEvento().getId());
            qry.setParameter("data", eventoBaile.getData());
            qry.setParameter("hora", eventoBaile.getHoraInicio());
            aEvento = (AEvento) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            aEvento = new AEvento();
        }
        return aEvento;
    }
}
