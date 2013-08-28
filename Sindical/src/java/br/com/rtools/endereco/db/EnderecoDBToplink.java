package br.com.rtools.endereco.db;

import br.com.rtools.endereco.*;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;



public class EnderecoDBToplink extends DB implements EnderecoDB {

    @Override
    public boolean insert(Endereco endereco) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(endereco);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }   
    }

    @Override
    public boolean update(Endereco endereco) {
        try{
        getEntityManager().merge(endereco);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }
    }
        
    @Override
    public boolean delete(Endereco endereco) {
        try{
        getEntityManager().remove(endereco);
        getEntityManager().flush();
        return true;
        }
        catch(Exception e){
            return false;
        }        
    }

    @Override
    public Endereco pesquisaCodigo(int id) {
        Endereco result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Endereco.pesquisaID");
            qry.setParameter("pid", id);
            result = (Endereco) qry.getSingleResult(); 
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select e from Endereco e ");
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }                       
    }
    
    
    @Override
    public List pesquisaEnderecoCep(String cep){       
        try{
            Query qry = getEntityManager().createQuery("select ende from Endereco ende " +
                                                       " where ende.cep = :d_cep " +
                                                       " order by ende.descricaoEndereco.descricao");
            qry.setParameter("d_cep", cep);  
            return (qry.getResultList());
        }
        catch(Exception e){
            return null;
        }                       
    }

    @Override
    public PessoaEndereco pesquisaPessoaEndCobranca(int idPessoa){
        try{
            Query qry = getEntityManager().createQuery("select pesEnd " +
                                                       "  from PessoaEndereco pesEnd " +
                                                       " where pesEnd.pessoa.id = :id_pes" +
                                                       "   and pesEnd.tipoEndereco.id = 3 ");
            qry.setParameter("id_pes", idPessoa);
            return (PessoaEndereco) qry.getSingleResult();
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public List pesquisaEnderecoDes(String uf, String cidade, String logradouro, String descricao, String iniParcial){
        List vetor;
        List<Endereco> listEnd = new ArrayList();
        try{
            if (iniParcial.equals("I"))
               descricao = descricao.toLowerCase().toUpperCase() + "%";
            else
               descricao = "%" + descricao.toLowerCase().toUpperCase() + "%";

            Query qry = getEntityManager().createNativeQuery(
              "select ende.id " +
              "  from end_endereco ende," +
              "       end_cidade cid," +
              "       end_logradouro logr," +
              "       end_descricao_endereco des" +
              " where ende.id_cidade = cid.id" +
              "   and ende.id_logradouro = logr.id" +
              "   and ende.id_descricao_endereco = des.id" +
              "   and cid.ds_cidade = '" +cidade+"'"+
              "   and cid.ds_uf = '"+ uf+"'"+
              "   and logr.ds_descricao = '" + logradouro+"'"+
              "   and UPPER(translate(des.ds_descricao)) like '"+ AnaliseString.removerAcentos(descricao)+"'" +
              " order by des.ds_descricao");
            vetor = qry.getResultList();
            if (!vetor.isEmpty()){
                for (int i = 0; i < vetor.size(); i++){
                    listEnd.add(pesquisaCodigo( (Integer) ((Vector) vetor.get(i)).get(0) ));
                }
            }
            return listEnd;
        }
        catch(EJBQLException e){
            return null;
        }
    }

    @Override
    public List pesquisaEndereco(int idDescricao, int idCidade, int idBairro, int idLogradouro){
        try{
            Query qry = getEntityManager().createQuery("select ende " +
                                                       "  from Endereco ende " +
                                                       " where ende.descricaoEndereco.id = :idDesc " +
                                                       "   and ende.cidade.id = :idCid " +
                                                       "   and ende.bairro.id = :idBai " +
                                                       "   and ende.logradouro.id = :idLog");
            qry.setParameter("idDesc", idDescricao);
            qry.setParameter("idCid", idCidade);
            qry.setParameter("idBai", idBairro);
            qry.setParameter("idLog", idLogradouro);
            return qry.getResultList();
        }
        catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List<Logradouro> pesquisaTodosOrdenado() {
        try{
            Query qry = getEntityManager().createQuery("select logr from Logradouro logr order by logr.descricao");
            return (qry.getResultList());
        }
        catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public DescricaoEndereco pesquisaDescricaoEndereco(int id) {
        DescricaoEndereco result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("DescricaoEndereco.pesquisaID");
            qry.setParameter("pid", id);
            result = (DescricaoEndereco) qry.getSingleResult(); 
        }
        catch(Exception e){
        }
        return result;
    }    
    
    @Override
    public Logradouro pesquisaLogradouro(int id) {
        Logradouro result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Logradouro.pesquisaID");
            qry.setParameter("pid", id);
            result = (Logradouro) qry.getSingleResult(); 
        }
        catch(Exception e){
        }
        return result;
    }  
    
    @Override
    public Bairro pesquisaBairro(int id) {
        Bairro result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Bairro.pesquisaID");
            qry.setParameter("pid", id);
            result = (Bairro) qry.getSingleResult(); 
        }
        catch(Exception e){
        }
        return result;
    }    
    
    @Override
    public Cidade pesquisaCidade(int id) {
        Cidade result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Cidade.pesquisaID");
            qry.setParameter("pid", id);
            result = (Cidade) qry.getSingleResult(); 
        }
        catch(Exception e){
        }
        return result;
    }    
}
