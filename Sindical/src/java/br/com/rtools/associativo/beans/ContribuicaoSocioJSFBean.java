package br.com.rtools.associativo.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class ContribuicaoSocioJSFBean {

    private String ano = DataHoje.DataToArrayString(DataHoje.data())[2];
    private String mes = DataHoje.DataToArrayString(DataHoje.data())[1];
    private String msgConfirma = "";
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();
    private List listaData = new ArrayList();
    
    public void adicionarData(){
        if (!listaData.isEmpty()){
            boolean existe = false;
            for (int i = 0; i < listaData.size(); i++){
                if (listaData.get(i).toString().equals(mes+"/"+ano)){
                    existe = true;
                }
            }
            if (!existe)
                listaData.add(mes+"/"+ano);
                
        }else{
            listaData.add(mes+"/"+ano);
        }
    }
    
    public void gerarParaTodos() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        if (listaData.isEmpty()){
            if (sv.executeQuery("select func_geramensalidades(null, '" + mes + "/" + ano + "')")) {
                sv.comitarTransacao();
                msgConfirma = "Mensalidades geradas com Sucesso!";
            } else {
                sv.desfazerTransacao();
                msgConfirma = "Erro ao gerar Mensalidades!";
            }
        }else{
            for (int i = 0; i < listaData.size(); i++){
                if (sv.executeQuery("select func_geramensalidades(null, '" + listaData.get(i).toString().substring(0,2) + "/" + listaData.get(i).toString().substring(3,7) + "')")) {
                    sv.comitarTransacao();
                    msgConfirma = "Mensalidades geradas com Sucesso!";
                } else {
                    sv.desfazerTransacao();
                    msgConfirma = "Erro ao gerar Mensalidades!";
                }
            }
        }
    }

    public void gerarParaEspecifico() {
        if (listaPessoa.isEmpty()){
            return;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        boolean erro = false;
        
        for (int i = 0; i < listaPessoa.size(); i++) {
            if (listaData.isEmpty()){
                if (sv.executeQuery("select func_geramensalidades("+listaPessoa.get(i).getId()+", '" + mes + "/" + ano + "')")) {
                    //sv.comitarTransacao();
                    msgConfirma = "Mensalidades geradas com Sucesso!";
                } else {
                    erro = true;
                    msgConfirma = "Erro ao gerar Mensalidades!";
                }
            }else{
                for (int w = 0; w < listaData.size(); w++){
                    if (sv.executeQueryVetor("select func_geramensalidades("+listaPessoa.get(i).getId()+", '" + listaData.get(w).toString().substring(0,2) + "/" + listaData.get(w).toString().substring(3,7) + "')")) {
                        //sv.comitarTransacao();
                        msgConfirma = "Mensalidades geradas com Sucesso!";
                    } else {
                        erro = true;
                        msgConfirma = "Erro ao gerar Mensalidades!";
                    }
                }
            }
        }
        
        if (erro){
            sv.desfazerTransacao();
        }else{
            sv.comitarTransacao();
        }
    }

    public String removerPessoaLista(int index) {
        listaPessoa.remove(index);
        return "contribuicaoSocio";
    }
    
    public String removerDataLista(int index) {
        listaData.remove(index);
        return null;
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

    public void limpar() {
        this.setAno(DataHoje.DataToArrayString(DataHoje.data())[2]);
        this.setMes(DataHoje.DataToArrayString(DataHoje.data())[1]);
    }

    public String getAno() {
        if (ano.length() != 4) {
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

    public void refreshForm() {
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public List getListaData() {
        return listaData;
    }

    public void setListaData(List listaData) {
        this.listaData = listaData;
    }
}
