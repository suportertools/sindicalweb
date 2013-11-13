
package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Registro;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;



public class FilialDBToplink extends DB implements FilialDB{

    @Override
    public boolean insert(Filial filial) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(filial);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }   
    }

    @Override
    public boolean update(Filial filial) {
        try{
        getEntityManager().merge(filial);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(Filial filial) {
        try{
        getEntityManager().remove(filial);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }        
    }

    @Override
    public boolean insertRegistro(Registro registro) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(registro);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean updateRegistro(Registro registro) {
        try{
        getEntityManager().merge(registro);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteRegistro(Registro registro) {
        try{
        getEntityManager().remove(registro);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public Filial pesquisaCodigo(int id) {
        Filial result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Filial.pesquisaID");
            qry.setParameter("pid", id);
            result = (Filial) qry.getSingleResult(); 
        }
        catch(Exception e){
            String a = e.getMessage();
        }
        return result;
    }

    @Override
    public Registro pesquisaCodigoRegistro(int id) {
        Registro result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Registro.pesquisaID");
            qry.setParameter("pid", id);
            result = (Registro) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Registro pesquisaRegistroPorFilial(int id) {
        Registro result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select r " +
                    "  from Registro r" +
                    " where r.filial.id = :pid");
            qry.setParameter("pid", id);
            result = (Registro) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }


    @Override
    public List pesquisaFilialExiste(int idFilial) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select fil.filial.pessoa.nome"+
                    "  from Filial fil " +
                    " where fil.filial.id = :filial ");
            qry.setParameter("filial", idFilial);
            return qry.getResultList();
        }
        catch(Exception e){
            return null;
        }
    }   
    
    @Override
    public List<Filial> pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select fil from Filial fil order by fil.filial.pessoa.nome");
            return (qry.getResultList());
        }
        catch(Exception e){
            return new ArrayList();
        }                        
    }

    @Override
    public List pesquisaRazao(int idMatriz) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select fil.matriz.pessoa.nome"+
                    "  from Filial fil " +
                    " where fil.matriz.id = :matriz ");
            qry.setParameter("matriz", idMatriz);
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List pesquisaJuridicaFilial(int idMatriz) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select fili" +
                    "  from Filial fili" +
                    " where fili.matriz.id = :matriz" +
                    "   and fili.matriz.id <> fili.filial.id");
            qry.setParameter("matriz", idMatriz);
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public Filial pesquisaFilialPertencente(int idMatriz, int idFilial){
        Filial result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select fili" +
                    "  from Filial fili" +
                    " where fili.matriz.id = :matriz" +
                    "   and fili.filial.id = :filial");
            qry.setParameter("matriz", idMatriz);
            qry.setParameter("filial", idFilial);
            result = (Filial) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaFilial(String desc, String por, String como, int idmatriz) {
        List result = null;
        if (por.equals("todos")){
          try{
              Query qry = getEntityManager().createQuery("select jur from Juridica jur");
              result = qry.getResultList();
          }
          catch(Exception e){
              result = null;
          }
        }

        if (como.equals("I")){
          desc = desc.toLowerCase().toUpperCase()+"%";
          if (!(por.equals("todos")) && (desc != null) && (por.equals("CNPJ"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 2");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
          if (!(por.equals("todos")) && (desc != null) && (por.equals("CEI"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 3");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
          if (!(por.equals("todos")) && (desc != null) && (por.equals("CPF"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 1");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
          if (!(por.equals("todos")) && (desc != null) && (por.equals("nome"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.nome like :desc");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
        }else if (como.equals("D")){
          if (!(por.equals("todos")) && (desc != null) && (por.equals("CNPJ"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 2");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
          if (!(por.equals("todos")) && (desc != null) && (por.equals("CEI"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 3");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
          if (!(por.equals("todos")) && (desc != null) && (por.equals("CPF"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 1");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
          if (!(por.equals("todos")) && (desc != null) && (por.equals("nome"))){
            try{
                Query qry = getEntityManager().createQuery(
                        "select juri " +
                        "  from Juridica juri" +
                        " where juri.pessoa.nome = :desc");
                qry.setParameter("desc", desc);
                result = qry.getResultList();
            }
            catch(Exception e){
                result = null;
            }
          }
        }
        return result;
    }
    
    @Override
    public List pesquisaPessoaConvencaoGCidade(int idPessoa, int idConvencao, int idGCidade){
        List result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select patro" +
                    "  from Patronal patro" +
                    " where patro.pessoa.id = :pessoa" +
                    "   and patro.convencao.id = :convencao" +
                    "   and patro.grupoCidade.id = :grupoCidade");
            qry.setParameter("pessoa", idPessoa);
            qry.setParameter("convencao", idConvencao);
            qry.setParameter("grupoCidade", idGCidade);
            result = qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }
    
    @Override
    public List pesquisaPessoaPatronal (String desc, String por, String como){
        List lista = new Vector<Object>();
        String textQuery = null;
            if (como.equals("I")){
                desc = desc.toLowerCase().toUpperCase() + "%";
            }else{
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            }
            textQuery = "select patro from Patronal patro " +
                       " where UPPER(patro.pessoa." + por + ") like :desc" +
                       " order by patro.pessoa.nome";
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
