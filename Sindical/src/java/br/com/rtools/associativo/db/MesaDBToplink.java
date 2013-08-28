package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Mesa;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class MesaDBToplink extends DB implements MesaDB{
    public boolean insert(Mesa mesa) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(mesa);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Mesa mesa) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(mesa);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Mesa mesa) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(mesa);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public Mesa pesquisaCodigo(int id) {
        Mesa result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Mesa.pesquisaID");
            qry.setParameter("pid", id);
            result = (Mesa) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select r from Mesa r");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    
    public List<Mesa> pesquisaTodosPorEvento(int idEvento) {
        try{
            Query qry = getEntityManager().createQuery("select m from Mesa m where m.bVenda.evento.id = " + idEvento);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public Mesa pesquisaVendaPorEvt(int idVenda) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select b from Mesa b where b.bVenda.id = " + idVenda
                    );
            return (Mesa) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public Movimento pesquisaMovimentoPorVenda(int idVenda) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select m "
                    + "from Movimento m,"
                    + "     BVenda v "
                    + "where v.id = " + idVenda
                    + "  and v.evt.id = m.evt.id"
                    );
            return (Movimento) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
