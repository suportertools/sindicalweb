//package br.com.rtools.associativo.beans;
//
//import br.com.rtools.classeOperacao.OperacaoSocio;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.utilitarios.DataHoje;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.context.FacesContext;
//import org.richfaces.component.html.HtmlDataTable;
//
//
//public class ContribuicaoPorSocioJSFBean {
//
//    private String ano = DataHoje.DataToArrayString(DataHoje.data())[2];
//    private String mes = DataHoje.DataToArrayString(DataHoje.data())[1];
//    private List<String> lista = new ArrayList<String>();
//    private HtmlDataTable htmlTable;
//    private String mensagem = "";
//    private Pessoa pessoa = new Pessoa();
//    private boolean desabilitaAno = false;
//
//    public void gerarMovimento(){
//        if (pessoa.getId() != -1){
//            if (!lista.isEmpty()){
//                mensagem = (new OperacaoSocio(getMes(), getAno(), pessoa)).geracaoMovimento(lista);
//            }else{
//                mensagem = "Adicione pelo menos uma referẽncia.";
//            }
//        }else{
//            mensagem = "Pesquise a pessoa!";
//        }
//    }
//
//    public void limpar(){
//        this.setAno(DataHoje.DataToArrayString(DataHoje.data())[2]);
//        this.setMes(DataHoje.DataToArrayString(DataHoje.data())[1]);
//    }
//
//    public void adicionarReferencia(){
//        if(lista.isEmpty()){
//            desabilitaAno = true;
//        }
//        if (!lista.contains(mes + "/" + ano)){
//            lista.add(mes + "/" + ano);
//        }
//    }
//
//    public void removerReferenciaDaLista(){
//        int ref = htmlTable.getRowIndex();
//        lista.remove(ref);
//        if (lista.isEmpty()){
//            desabilitaAno = false;
//        }
//    }
//
//    public Pessoa getPessoaPesquisa(){
//        Pessoa p = new Pessoa();
//        try{
//            p = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
//            if (p == null){
//                return new Pessoa();
//            }else{
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
//                return p;
//            }
//        }catch(Exception e){
//            p = new Pessoa();
//            return p;
//        }
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
//    public List<String> getLista() {
//        return lista;
//    }
//
//    public void setLista(List<String> lista) {
//        this.lista = lista;
//    }
//
//    public Pessoa getPessoa() {
//        Pessoa p = this.getPessoaPesquisa();
//        if (p.getId() != -1){
//            this.pessoa = p;
//        }
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public boolean isDesabilitaAno() {
//        return desabilitaAno;
//    }
//
//    public void setDesabilitaAno(boolean desabilitaAno) {
//        this.desabilitaAno = desabilitaAno;
//    }
//
//    public HtmlDataTable getHtmlTable() {
//        return htmlTable;
//    }
//
//    public void setHtmlTable(HtmlDataTable htmlTable) {
//        this.htmlTable = htmlTable;
//    }
//}
