//
//package br.com.rtools.associativo.beans;
//
//import br.com.rtools.classeOperacao.OperacaoSocio;
//import br.com.rtools.utilitarios.DataHoje;
//
//
//public class ContribuicaoSocioJSFBean {
//
//    private String ano = DataHoje.DataToArrayString(DataHoje.data())[2];
//    private String mes = DataHoje.DataToArrayString(DataHoje.data())[1];
//    private String mensagem = "";
//
//    public void gerarParaTodos(){
//        mensagem = (new OperacaoSocio(getMes(), getAno())).geracaoMovimento();
//    }
//
//    public void limpar(){
//        this.setAno(DataHoje.DataToArrayString(DataHoje.data())[2]);
//        this.setMes(DataHoje.DataToArrayString(DataHoje.data())[1]);
//    }
//
//    public String getAno() {
//        if (ano.length() != 4){
//            ano = DataHoje.DataToArrayString(DataHoje.data())[2];
//        }
//        return ano;
//    }
//
//    public void setAno(String ano) {
//        this.ano = ano;
//    }
//
//    public String getMes() {
//        return mes;
//    }
//
//    public void setMes(String mes) {
//        this.mes = mes;
//    }
//
//    public String getMensagem() {
//        return mensagem;
//    }
//
//    public void setMensagem(String mensagem) {
//        this.mensagem = mensagem;
//    }
//
//    public void refreshForm(){
//
//    }
//
//
//}
