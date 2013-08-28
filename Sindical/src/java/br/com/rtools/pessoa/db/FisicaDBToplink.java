
package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;



public class FisicaDBToplink extends DB implements FisicaDB {

    @Override
    public boolean insert(Fisica fisica) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(fisica);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }   
    }

    @Override
    public boolean update(Fisica fisica) {
        try{
        getEntityManager().merge(fisica);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(Fisica fisica) {
        try{
        getEntityManager().remove(fisica);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }        
    }

    @Override
    public Fisica pesquisaCodigo(int id) {
        Fisica result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Fisica.pesquisaID");
            qry.setParameter("pid", id);
            result = (Fisica) qry.getSingleResult(); 
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select fis from Fisica fis ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }                        
    }

    @Override
    public List pesquisaPessoa(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
            if(por.equals("nome")){
               por = "nome";
                if (como.equals("P")){
                    desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select fis from Fisica fis, " +
                               "                 Pessoa pes     " +
                               "  where fis.pessoa.id = pes.id  " +
                               "   and UPPER(pes." + por + ") like :desc";
                }else if (como.equals("I")){
                    por = "nome";
                    desc = desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select fis from Fisica fis, " +
                               "                 Pessoa pes     " +
                               "  where fis.pessoa.id = pes.id  " +
                               "   and UPPER(pes." + por + ") like :desc";
                }
            }
            if (por.equals("rg")){
                        por = "rg";
                        desc = desc.toLowerCase().toUpperCase() + "%";
                            textQuery = "select fis from Fisica fis, " +
                                       "                 Pessoa pes     " +
                                       "  where fis.pessoa.id = pes.id  " +
                                       "   and UPPER(fis." + por + ") like :desc";
            }
            if (por.equals("cpf")){
                        por = "documento";
                        desc = desc.toLowerCase().toUpperCase() + "%";
                            textQuery = "select fis from Fisica fis, " +
                                       "                 Pessoa pes     " +
                                       "  where fis.pessoa.id = pes.id  " +
                                       "   and UPPER(pes." + por + ") like :desc";
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
    public List pesquisaPessoaSocio(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
            if(por.equals("nome")){
               por = "nome";
                if (como.equals("P")){
                    desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select fis from Fisica fis, " +
                               "                 Pessoa pes     " +
                               "  where fis.pessoa.id = pes.id  " +
                               "   and UPPER(pes." + por + ") like :desc "+
                               "   and pes.id in ( select soc.servicoPessoa.pessoa.id from Socios soc " +
                                                       " where soc.matriculaSocios.motivoInativacao is null )";
                }else if (como.equals("I")){
                    por = "nome";
                    desc = desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select fis from Fisica fis, " +
                               "                 Pessoa pes     " +
                               "  where fis.pessoa.id = pes.id  " +
                               "   and UPPER(pes." + por + ") like :desc "+
                               "   and pes.id in ( select soc.servicoPessoa.pessoa.id from Socios soc " +
                                                     " where soc.matriculaSocios.motivoInativacao is null )";
                }
            }
            if (por.equals("rg")){
                        por = "rg";
                        desc = desc.toLowerCase().toUpperCase() + "%";
                            textQuery = "select fis from Fisica fis, " +
                                       "                 Pessoa pes     " +
                                       "  where fis.pessoa.id = pes.id  " +
                                       "   and UPPER(fis." + por + ") like :desc "+
                                       "   and pes.id in ( select soc.servicoPessoa.pessoa.id from Socios soc " +
                                                             " where soc.matriculaSocios.motivoInativacao is null )";
            }
            if (por.equals("cpf")){
                        por = "documento";
                        desc = desc.toLowerCase().toUpperCase() + "%";
                            textQuery = "select fis from Fisica fis, " +
                                       "                 Pessoa pes     " +
                                       "  where fis.pessoa.id = pes.id  " +
                                       "   and UPPER(pes." + por + ") like :desc"+
                                       "   and pes.id in ( select soc.servicoPessoa.pessoa.id from Socios soc " +
                                                             " where soc.matriculaSocios.motivoInativacao is null )";
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
    public List pesquisaPessoaSocioInativo(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
            if(por.equals("nome")){
               por = "nome";
                if (como.equals("P")){
                    desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select fis from Fisica fis " +
                               //"                 Pessoa pes     " +
                               //"  where fis.pessoa.id = pes.id  " +
                               "  where UPPER(fis.pessoa." + por + ") like :desc "+
                               "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.matriculaSocios.motivoInativacao is not null ) " +
                               "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
                }else if (como.equals("I")){
                    por = "nome";
                    desc = desc.toLowerCase().toUpperCase() + "%";
                    textQuery = "select fis from Fisica fis " +
                               //"                 Pessoa pes     " +
                               //"  where fis.pessoa.id = pes.id  " +
                               "  where UPPER(fis.pessoa." + por + ") like :desc "+
                               "   and fis.pessoa.id in ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.matriculaSocios.motivoInativacao is not null ) " +
                               "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
                }
            }
            if (por.equals("rg")){
                        por = "rg";
                        desc = desc.toLowerCase().toUpperCase() + "%";
                            textQuery = "select fis from Fisica fis " +
                                       //"                 Pessoa pes     " +
                                       //"  where fis.pessoa.id = pes.id  " +
                                       "  where UPPER(fis." + por + ") like :desc "+
                                       "    and fis.pessoa.id in     ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.matriculaSocios.motivoInativacao is not null ) " +
                                       "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
            }
            if (por.equals("cpf")){
                        por = "documento";
                        desc = desc.toLowerCase().toUpperCase() + "%";
                            textQuery = "select fis from Fisica fis " +
                                       //"                 Pessoa pes     " +
                                       //"  where fis.pessoa.id = pes.id  " +
                                       "  where UPPER(fis.pessoa." + por + ") like :desc"+
                                       "   and fis.pessoa.id in     ( select soc.servicoPessoa.pessoa.id from Socios soc where soc.matriculaSocios.motivoInativacao is not null ) " +
                                       "   and fis.pessoa.id not in ( select soc2.servicoPessoa.pessoa.id from Socios soc2 where soc2.servicoPessoa.ativo = true ) ";
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
    public Fisica idFisica(Fisica des_fisica){
        Fisica result = null;
        String descricao = des_fisica.getPessoa().getNome().toLowerCase().toUpperCase();
        try{
           Query qry = getEntityManager().createQuery("select nom from Fisica nom where UPPER(nom.fisica) = :d_fisica");
           qry.setParameter("d_fisica", descricao);
           result = (Fisica) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaFisicaPorDoc(String doc) {
        List result = new ArrayList();
        String documento = "%"+doc+"%";
        try{
            Query qry = getEntityManager().createQuery("select fis from Fisica fis," +
                                                       "                Pessoa pes " +
                                                       " where fis.pessoa.id = pes.id" +
                                                       "   and pes.documento like :Sdoc");
            qry.setParameter("Sdoc", documento);
            result =  qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaFisicaPorDocSemLike(String doc) {
        List result = new ArrayList();
        try{
            Query qry = getEntityManager().createQuery("select fis from Fisica fis," +
                                                       "                Pessoa pes " +
                                                       " where fis.pessoa.id = pes.id" +
                                                       "   and pes.documento like :Sdoc");
            qry.setParameter("Sdoc", doc);
            result =  qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Fisica pesquisaFisicaPorPessoa(int idPessoa) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select f"+
                    "  from Fisica f " +
                    " where f.pessoa.id = :pid");
            qry.setParameter("pid", idPessoa);
            return (Fisica) qry.getSingleResult();
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List pesquisaFisicaPorNomeNascRG(String nome, Date nascimento,String RG) {
        String textQuery = "";
        try{
            if (RG.isEmpty() && nascimento != null){
                textQuery = "select f"+
                            "  from Fisica f " +
                            " where UPPER(f.pessoa.nome) like '"+nome.toLowerCase().toUpperCase()+"'"+
                            "   and f.dtNascimento = :nasc";
            }else if (!RG.isEmpty()){
                textQuery = "select f"+
                            "  from Fisica f " +
                            " where f.rg = '"+RG+"'";
            }else{
                return new ArrayList();
            }
            Query qry = getEntityManager().createQuery(textQuery);
            if (RG.isEmpty())
                qry.setParameter("nasc", nascimento);
            return qry.getResultList();
        }
        catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List pesquisaFisicaPorNome(String nome) {
        try{
            String textQuery = "select f "+
                               "  from Fisica f " +
                               " where UPPER(f.pessoa.nome) like '%"+nome+"%'";
            Query qry = getEntityManager().createQuery(textQuery);
            return qry.getResultList();
        }
        catch(Exception e){
            return new ArrayList();
        }
    }
    
    public List pesquisaPessoaSocioID(int id_pessoa) {
        List lista = new Vector<Object>();
        String textQuery = null;
            
        try{
            textQuery = "select fis from Fisica fis, " +
                   //"                 Pessoa pes     " +
                   "  where fis.pessoa.id = "+ id_pessoa +
                   "   and pes.id in ( select soc.servicoPessoa.pessoa.id from Socios soc " +
                                           " where soc.matriculaSocios.motivoInativacao is null )";
            Query qry = getEntityManager().createQuery(textQuery);
            lista = qry.getResultList();
        }catch(Exception e){
            return lista;
        }
        return lista;
    }    
    
}
