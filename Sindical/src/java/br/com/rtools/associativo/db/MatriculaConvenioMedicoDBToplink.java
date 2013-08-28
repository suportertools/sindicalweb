package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import br.com.rtools.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class MatriculaConvenioMedicoDBToplink extends DB implements MatriculaConvenioMedicoDB{
    @Override
    public boolean insert(MatriculaConvenioMedico matriculaConvenioMedico) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(matriculaConvenioMedico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(MatriculaConvenioMedico matriculaConvenioMedico) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(matriculaConvenioMedico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(MatriculaConvenioMedico matriculaConvenioMedico) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(matriculaConvenioMedico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public MatriculaConvenioMedico pesquisaCodigo(int id) {
        MatriculaConvenioMedico result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("MatriculaConvenioMedico.pesquisaID");
            qry.setParameter("pid", id);
            result = (MatriculaConvenioMedico) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select mc from MatriculaConvenioMedico mc");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public List pesquisaConvenioMedico(String desc, String por, String como){
        List lista = new Vector<Object>();
        String textQuery = null;
        if(por.equals("nome")){
           por = "nome";
            if (como.equals("P")){
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select mc from MatriculaConvenioMedico mc" +
                            " where UPPER(mc.servicoPessoa.pessoa.nome) like :desc " +
                            " order by mc.servicoPessoa.pessoa.nome";
            }else if (como.equals("I")){
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select mc from MatriculaConvenioMedico mc" +
                            " where UPPER(mc.servicoPessoa.pessoa.nome) like :desc " +
                            " order by mc.servicoPessoa.pessoa.nome";
            }
        }
        if(por.equals("cpf")){
            desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select mc from MatriculaConvenioMedico mc" +
                            " where UPPER(mc.servicoPessoa.pessoa.documento) like :desc " +
                            " order by mc.servicoPessoa.pessoa.nome";
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
