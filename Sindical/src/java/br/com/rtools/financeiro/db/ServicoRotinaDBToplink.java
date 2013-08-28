package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ServicoRotina;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class ServicoRotinaDBToplink extends DB implements ServicoRotinaDB{
    public boolean insert(ServicoRotina servicoRotina) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(servicoRotina);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(ServicoRotina servicoRotina) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(servicoRotina);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(ServicoRotina servicoRotina) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(servicoRotina);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public ServicoRotina pesquisaCodigo(int id) {
        ServicoRotina result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("ServicoRotina.pesquisaID");
            qry.setParameter("pid", id);
            result = (ServicoRotina) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select sr from ServicoRotina sr");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public List pesquisaServicoRotinaPorServico(int idServico) {
        try{
            Query qry = getEntityManager().createQuery("select sr from ServicoRotina sr where sr.servicos.id = "+idServico);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

    public List pesquisaTodasRotinasSemServicoOrdenado(int idServico) {
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot " +
                                                       " where rot.id not in (select sc.rotina.id from ServicoRotina sc where sc.servicos.id = "+idServico+")" +
                                                       " order by rot.rotina");
            return (qry.getResultList());
        }
        catch(EJBQLException e){
            return new ArrayList();
        }
    }

    public List pesquisaTodasRotinasOrdenado(int idServico) {
        try{
            Query qry = getEntityManager().createQuery("select rot from Rotina rot " +
                                                       " where rot.id not in (select sc.rotina.id from ServicoRotina sc where sc.servicos.id = "+idServico+")" +
                                                       " order by rot.rotina");
            return (qry.getResultList());
        }
        catch(EJBQLException e){
            return new ArrayList();
        }
    }

    public List pesquisaTodosServicosComRotinas(int idRotina) {
        try{
            Query qry = getEntityManager().createQuery("select s from Servicos s " +
                                                       " where s.id in (select sr.servicos.id from ServicoRotina sr where sr.rotina.id = "+idRotina+")" +
                                                       " order by s.descricao");
            return (qry.getResultList());
        }
        catch(EJBQLException e){
            return new ArrayList();
        }
    }
}
