

package br.com.rtools.agenda.db;

import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;



public class GrupoAgendaDBToplink extends DB implements GrupoAgendaDB {

    @Override
    public boolean insert(GrupoAgenda grupoAgenda) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(grupoAgenda);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(GrupoAgenda grupoAgenda) {
        try{
        getEntityManager().merge(grupoAgenda);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(GrupoAgenda grupoAgenda) {
        try{
        getEntityManager().remove(grupoAgenda);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public GrupoAgenda pesquisaCodigo(int id) {
        GrupoAgenda result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("GrupoAgenda.pesquisaID");
            qry.setParameter("pid", id);
            result = (GrupoAgenda) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select age from GrupoAgenda age ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public GrupoAgenda idGrupoAgenda(GrupoAgenda des_grupoAgenda){
        GrupoAgenda result = null;
        String descricao = des_grupoAgenda.getDescricao().toLowerCase().toUpperCase();
        try{
           Query qry = getEntityManager().createQuery("select grupAge from GrupoAgenda grupAge where UPPER(grupAge.descricao) = :d_grupoAgenda");
           qry.setParameter("d_grupoAgenda", descricao);
           result = (GrupoAgenda) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaGrupoAgenda (String desc, String como){
        List lista = new Vector<Object>();
        String textQuery = null;
                if (como.equals("P")){

                    desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select ga from GrupoAgenda ga    " +
                                " where UPPER(ga.descricao) like :desc";
                }else if (como.equals("I")){
                    desc = desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select ga from GrupoAgenda ga    " +
                                " where UPPER(ga.descricao) like :desc";
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

}
