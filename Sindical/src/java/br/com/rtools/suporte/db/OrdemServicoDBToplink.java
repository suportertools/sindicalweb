package br.com.rtools.suporte.db;

import br.com.rtools.principal.DB;
import br.com.rtools.suporte.OrdemServico;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class OrdemServicoDBToplink extends DB implements OrdemServicoDB {

    public boolean insert(OrdemServico ordemServico) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(ordemServico);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(OrdemServico ordemServico) {
        try{
        getEntityManager().merge(ordemServico);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean delete(OrdemServico ordemServico) {
        try{
        getEntityManager().remove(ordemServico);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public OrdemServico idOrdemServico(OrdemServico des_OrdemServico){
        OrdemServico result = null;
        try{
           Query qry = getEntityManager().createQuery("select os from OrdemServico os where os.descricao = :descricao");
           qry.setParameter("descricao", des_OrdemServico.getHistorico());
           result = (OrdemServico) qry.getSingleResult();
        }
        catch(Exception e){
           e.getMessage();
        }
        return result;
    }

    public OrdemServico pesquisaCodigo(int id) {
        OrdemServico result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("OrdemServico.pesquisaID");
            qry.setParameter("pid", id);
            result = (OrdemServico) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    public List pesquisaTodos(String des_porPesquisa) {
        String textQuery = "";
        if (!des_porPesquisa.equals("")){                
            if (des_porPesquisa.equals("todos")){
                textQuery = "";
            }else if (des_porPesquisa.equals("concluido")){
                textQuery = "where os.dataConclusao is not null";
            }else if (des_porPesquisa.equals("aberto")){
                textQuery = "where os.dataConclusao is null";
            }else if (des_porPesquisa.equals("parado")){
                textQuery = "where os.proStatus.id = 4";
            }
        }else{
            textQuery = "";
            return null;
        }
        try{
            Query qry = getEntityManager().createQuery("select os from OrdemServico os " + textQuery + " order by os.dataPrevisao, os.proStatus.status" );
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    public List<String> pesquisaOrdemServico(String des_tipo){
        List<String> result = null;
        try{
           Query qry = getEntityManager().createQuery("select os.ordemServico from OrdemServico os where os.ordemServico like :texto");
           qry.setParameter("texto", des_tipo);
           result = (List<String>) qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    public List pesquisaOrdemServicoParametros(String por,String combo,String desc) {
        String textQuery = "";
        if (!desc.equals("") && !por.equals("")){
            if (por.equals("I")){
                desc = desc+"%";
            }else if(por.equals("P")){
                desc = "%"+desc+"%";
            }
        }else{
            desc = "";
            return null;
        }
        if (combo.equals("")){
            combo = "ordemServico";
        }
        try{
            textQuery = "select os from ordemServico os where upper(os."+combo+") like :ordemServico order by os.ordemServico";
            Query qry = getEntityManager().createQuery(textQuery);
            qry.setParameter("ordemServico", desc.toLowerCase().toUpperCase());
            return (qry.getResultList());
        }
        catch(EJBQLException e){
            return null;
        }
    }

}

