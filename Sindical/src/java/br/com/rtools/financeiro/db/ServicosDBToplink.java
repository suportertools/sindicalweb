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
    public boolean insert(Servicos servicos) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(servicos);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Servicos servicos) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(servicos);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }
        catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Servicos servicos) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(servicos);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }
        catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public synchronized  Servicos pesquisaCodigo(int id) {
        Servicos result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Servicos.pesquisaID");
            qry.setParameter("pid", id);
            result = (Servicos) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Correcao pesquisaCorrecao(int idServico) {
        Correcao result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select c " +
                    "  from Correcao c" +
                    " where c.servicos.id = :pid");
            qry.setParameter("pid", idServico);
            result = (Correcao) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public IndiceMensal pesquisaIndiceMensal(int mes, int ano, int idIndice) {
        IndiceMensal result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select i " +
                    "  from IndiceMensal i" +
                    " where i.mes = :mes" +
                    "   and i.ano = :ano"+
                    "   and i.indice.id = :i");
            qry.setParameter("mes", mes);
            qry.setParameter("ano", ano);
            qry.setParameter("i", idIndice);
            result = (IndiceMensal) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select p from Servicos p ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List pesquisaTodos(int idRotina) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select s.servicos " +
                    "  from ServicoRotina s" +
                    " where s.rotina.id = :r");
            qry.setParameter("r", idRotina);
            return (qry.getResultList());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaTodosPeloContaCobranca(int idRotina) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select sr.servicos " +
                    "  from ServicoRotina sr" +
                    " where sr.rotina.id = :r" +
                    "   and sr.servicos.id in (select s.servicos.id from ServicoContaCobranca s)");
            qry.setParameter("r", idRotina);
            return (qry.getResultList());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List pesquisaPlano5(){
        try{
            Query qry = getEntityManager().createQuery("select p5 from Plano5 p5 ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List pesquisaServicos (String desc, String por, String como){
        List lista = new Vector<Object>();
        String textQuery = null;

        if (como.equals("P")){
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ser from Servicos ser " +
                       "  where UPPER(ser." + por + ") like :desc" ;
        }else if (como.equals("I")){
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ser from Servicos ser " +
                       "  where UPPER(ser." + por + ") like :desc" ;
        }

        try{
            Query qry = getEntityManager().createQuery(textQuery);
                    if (!desc.equals("%%")&& !desc.equals("%"))
                        qry.setParameter("desc", desc);
                    lista = qry.getResultList();
        }
        catch(Exception e){
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public Servicos idServicos(Servicos des_servicos){
        Servicos result = null;
        String descricao = des_servicos.getDescricao().toLowerCase().toUpperCase();
        try{
           Query qry = getEntityManager().createQuery("select ser from Servicos ser where UPPER(ser.descricao) = :d_servicos");
           qry.setParameter("d_servicos", descricao);
           result = (Servicos) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }


}
