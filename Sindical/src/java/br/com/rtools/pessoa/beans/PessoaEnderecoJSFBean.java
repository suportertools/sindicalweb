
package br.com.rtools.pessoa.beans;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.*;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;

public class PessoaEnderecoJSFBean {
    private PessoaEndereco pessoaEndereco = new PessoaEndereco(); 
    private String indicaTab;
    private String enderecoCompleto;
    private int Cpessoa;

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
    
    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }                            
                                
    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }
    
    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }
    public String salvar(){
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        TipoEnderecoDB db_tipoEndereco = new TipoEnderecoDBToplink();
        PessoaDB db_pessoa = new PessoaDBToplink();
        if (pessoaEndereco.getId()==-1){
            pessoaEndereco.setTipoEndereco(db_tipoEndereco.idTipoEndereco(pessoaEndereco.getTipoEndereco()));            
            db.insert(pessoaEndereco);
        }
        else{
            db.getEntityManager().getTransaction().begin();
            if (db.update(pessoaEndereco))
            {db.getEntityManager().getTransaction().commit();}
            else
            {db.getEntityManager().getTransaction().rollback();}
        }        
        return null;
    }
    
   public String novo(){
       pessoaEndereco = new PessoaEndereco();//zera objeto
       return "cadPessoaEndereco";
   }    

   public String excluir(){
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        if (pessoaEndereco.getId()!=-1){
            db.getEntityManager().getTransaction().begin();
            pessoaEndereco = db.pesquisaCodigo(pessoaEndereco.getId());
            if (db.delete(pessoaEndereco))
            {db.getEntityManager().getTransaction().commit();}
            else
            {db.getEntityManager().getTransaction().rollback();}
        }
       pessoaEndereco = new PessoaEndereco(); 
       return "pesquisaPessoaEndereco";
   }       
   
   public String editar(){
       //pessoaEndereco = (PessoaEndereco) getHtmlTable().getRowData();
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
       return "cadFilial";
   }
   
   public List getListaPessoaEndereco(){
       List result = null;
       PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
       result = db.pesquisaTodos();
       return result;
   }
    
    public List<String> BuscaTipoEndereco(Object event) {
        List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        TipoEnderecoDB db = new TipoEnderecoDBToplink();
        result = db.pesquisaTipoEndereco('%' + txtDigitado + '%');
        return (result);
    }

    public List getListaEnderecoCep() {
        List result = null;
        EnderecoDB db = new EnderecoDBToplink();
        result = db.pesquisaEnderecoCep(pessoaEndereco.getEndereco().getCep());
        return result;
    }

    public List getListaEnderecoDes() {
        List result = null;
        EnderecoDB db = new EnderecoDBToplink();
        String uf = pessoaEndereco.getEndereco().getCidade().getUf();
        String cidade = pessoaEndereco.getEndereco().getCidade().getCidade();
        String log =  pessoaEndereco.getEndereco().getLogradouro().getDescricao();
        String desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
        result = db.pesquisaEnderecoDes(uf, cidade, log, desc, "P");



        /*result = db.pesquisaEnderecoDes(
                pessoaEndereco.getEndereco().getCidade().getUf(),
                pessoaEndereco.getEndereco().getCidade().getCidade(),
                pessoaEndereco.getEndereco().getLogradouro().getLogradouro(),
                pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricaoEndereco());*/
        return result;
    }

    public Endereco getRetornaLinha() {
        //pessoaEndereco.setEndereco((Endereco) getHtmlTableEndereco().getRowData());
        return pessoaEndereco.getEndereco();
    }


    public String LimpaObjetoDesFisica(){
        Endereco endereco = new Endereco();
        pessoaEndereco.setEndereco(endereco);
        return "pesquisaEndDesFisica";
    }

    public String LimpaObjetoCepFisica(){
        Endereco endereco = new Endereco();
        pessoaEndereco.setEndereco(endereco);
        return "pesquisaCepFisica";
    }

    public String LimpaObjetoDesJuridica(){
        Endereco endereco = new Endereco();
        pessoaEndereco.setEndereco(endereco);
        return "pesquisaEndDesJuridica";
    }

    public String LimpaObjetoCepJuridica(){
        Endereco endereco = new Endereco();
        pessoaEndereco.setEndereco(endereco);
        return "pesquisaCepJuridica";
    }

    public void refreshForm(){
    }

    public List<String> BuscaCidade(Object event) {
        List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        CidadeDB db = new CidadeDBToplink();
        String str = pessoaEndereco.getEndereco().getCidade().getUf();
        result = db.pesquisaCidade('%' + txtDigitado + '%', str);
        return (result);
    }             

    public List<String> BuscaLogradouro(Object event) {
        List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        //LogradouroDB db = new LogradouroDBToplink();
        //result = db.pesquisaLogradouro('%' + txtDigitado + '%');
        return (result);
    }

    public List<String> BuscaDescricaoEndereco(Object event) {
        List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        //DescricaoEnderecoDB db = new DescricaoEnderecoDBToplink();
        //result = db.pesquisaDescricaoEndereco('%' + txtDigitado + '%');
        return (result);
    }
    
}
