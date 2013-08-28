
package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class TipoDocumentoDBToplink extends DB implements TipoDocumentoDB {

    @Override
    public boolean insert(TipoDocumento tipoDocumento) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(tipoDocumento);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(TipoDocumento tipoDocumento) {
        try{
        getEntityManager().merge(tipoDocumento);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(TipoDocumento tipoDocumento) {
        try{
        getEntityManager().remove(tipoDocumento);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public TipoDocumento pesquisaCodigo(int id) {
        TipoDocumento result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("TipoDocumento.pesquisaID");
            qry.setParameter("pid", id);
            result = (TipoDocumento) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select tipo from TipoDocumento tipo ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List<String> pesquisaTipoDocumento(String des_tipo){
        List<String> result = null;
        try{
           Query qry = getEntityManager().createQuery("select tipo.descricao from TipoDocumento tipo where tipo.descricao like :texto");
           qry.setParameter("texto", des_tipo);
           result = (List<String>) qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTipoDocumento(){
        List result = null;
        try{
           Query qry = getEntityManager().createQuery("select tipo from TipoDocumento tipo");
           result = (List) qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public TipoDocumento idTipoDocumento(TipoDocumento des_tipo){
        TipoDocumento result = null;
        try{
           Query qry = getEntityManager().createQuery("select tipo from TipoDocumento tipo where tipo.descricao = :d_tipo");
           qry.setParameter("d_tipo", des_tipo.getDescricao());
           result = (TipoDocumento) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }
}
