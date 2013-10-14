package br.com.rtools.associativo.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;


public class ContribuicaoSocioJSFBean {

    private String ano = DataHoje.DataToArrayString(DataHoje.data())[2];
    private String mes = DataHoje.DataToArrayString(DataHoje.data())[1];
    private String mensagem = "";
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();

    public String removerPessoaLista(int index) {
        listaPessoa.remove(index);
        return "contribuicaoSocio";
    }    
    
    public String removerPesquisa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        pessoa = new Pessoa();
        return "contribuicaoSocio";
    }
    
    public String adicionarPesquisa() {
        listaPessoa.add(pessoa);
        pessoa = new Pessoa();
        return "contribuicaoSocio";
    }
    
    public void gerarParaTodos(){
        //mensagem = (new OperacaoSocio(getMes(), getAno())).geracaoMovimento();
    }

    public void limpar(){
        this.setAno(DataHoje.DataToArrayString(DataHoje.data())[2]);
        this.setMes(DataHoje.DataToArrayString(DataHoje.data())[1]);
    }

    public String getAno() {
        if (ano.length() != 4){
            ano = DataHoje.DataToArrayString(DataHoje.data())[2];
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void refreshForm(){

    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListaPessoa() {
        return listaPessoa;
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }


}
