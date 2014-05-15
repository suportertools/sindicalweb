package br.com.rtools.arrecadacao.beans;

import br.com.rtools.associativo.LoteBoleto;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GerarBoletoBean {
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();
    private List<DataObject> listaGerados = new ArrayList<DataObject>();
    private List<DataObject> listaGeradosSelecionado = new ArrayList<DataObject>();
    private boolean imprimeVerso = true;
    
    private String ano = DataHoje.DataToArrayString(DataHoje.data())[2];
    private String mes = DataHoje.DataToArrayString(DataHoje.data())[1];
    private List listaData = new ArrayList();
    
    private List<Vector> listaServicoSemCobranca = new ArrayList();
    
    public GerarBoletoBean(){
        getListaServicoSemCobranca();
    }
    
    public void gerarTodos(){
        if (!listaServicoSemCobranca.isEmpty()){
            GenericaMensagem.warn("Atenção", "Não é possível gerar mensalidade, verifique os Serviços e Conta Cobrança!");
            return;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        if (listaData.isEmpty()){
            if (sv.executeQueryObject("select func_geramensalidades(null, '" + mes + "/" + ano + "')")) {
                sv.comitarTransacao();
                listaGerados.clear();
                GenericaMensagem.info("Sucesso", "Geração de Mensalidades concluída!");
            } else {
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Erro ao gerar Mensalidades!");
            }
        }else{
            for (Object listaDatax : listaData) {
                String vencto = listaDatax.toString().substring(0, 2) + "/" + listaDatax.toString().substring(3, 7);
                if (sv.executeQueryObject("select func_geramensalidades(null, '" + vencto + "')")) {
                    listaGerados.clear();
                    GenericaMensagem.info("Sucesso", "Geração de Mensalidades " + vencto + " concluída!");
                } else {
                    sv.desfazerTransacao();
                    GenericaMensagem.warn("Erro", "Erro ao gerar Mensalidades!");
                    return;
                }
            }
            
            sv.comitarTransacao();
        }
    }
    
    public void gerarLista(){
        if (!listaServicoSemCobranca.isEmpty()){
            GenericaMensagem.warn("Atenção", "Não é possível gerar mensalidade, verifique os Serviços e Conta Cobrança!");
            return;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        boolean erro = false;
        
        for (Pessoa pe : listaPessoa){
            if (listaData.isEmpty()){
                if (sv.executeQueryObject("select func_geramensalidades("+pe.getId()+", '" + mes + "/" + ano + "')")) {
                    GenericaMensagem.info("Sucesso", "Geração de Mensalidades concluída!");
                } else {
                    erro = true;
                    GenericaMensagem.warn("Erro", "Erro ao gerar Mensalidades!");
                }
            }else{
                for (Object listaDatax : listaData) {
                    String vencto = listaDatax.toString().substring(0, 2) + "/" + listaDatax.toString().substring(3, 7);
                    if (sv.executeQueryObject("select func_geramensalidades("+pe.getId()+", '" + vencto + "')")) {
                        GenericaMensagem.info("Sucesso", "Geração de Mensalidades "+ vencto + " concluída!");
                    } else {
                        erro = true;
                        GenericaMensagem.warn("Erro", "Erro ao gerar Mensalidades!");
                    }
                }
            }
        }
        
        if (erro){
            sv.desfazerTransacao();
        }else{
            sv.comitarTransacao();
            listaGerados.clear();
        }
    }
    
    public void adicionarPessoa() {
        listaPessoa.add(pessoa);
        pessoa = new Pessoa();
    }
    
    public void removerPessoaLista(int index) {
        listaPessoa.remove(index);
    }
    
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
    
    public void removerDataLista(int index) {
        listaData.remove(index);
    }
    
        
    public void removerPessoa(){
        pessoa = new Pessoa();
    }
    
    public Pessoa getPessoa() {
        if (GenericaSessao.getObject("pessoaPesquisa") != null){
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa");
            GenericaSessao.remove("pessoaPesquisa");
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

    public List<DataObject> getListaGerados() {
        if (listaGerados.isEmpty()){
            List<LoteBoleto> lista = (new SalvarAcumuladoDBToplink()).listaObjeto("LoteBoleto");
            for (LoteBoleto lb : lista){
                listaGerados.add(new DataObject(lb, null));
            }
        }
        return listaGerados;
    }

    public void setListaGerados(List<DataObject> listaGerados) {
        this.listaGerados = listaGerados;
    }

    public List<DataObject> getListaGeradosSelecionado() {
        return listaGeradosSelecionado;
    }

    public void setListaGeradosSelecionado(List<DataObject> listaGeradosSelecionado) {
        this.listaGeradosSelecionado = listaGeradosSelecionado;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
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

    public List getListaData() {
        return listaData;
    }

    public void setListaData(List listaData) {
        this.listaData = listaData;
    }
    
    public List<Vector> getListaServicoSemCobranca() {
        if (listaServicoSemCobranca.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            
            listaServicoSemCobranca = db.listaServicosSemCobranca();
            
            if (!listaServicoSemCobranca.isEmpty()){
                GenericaMensagem.fatal("Atenção", "Não é possível gerar mensalidades sem antes definir Conta Cobrança para os seguintes Serviços:");
                for (Vector linha : listaServicoSemCobranca){
                    GenericaMensagem.info("Serviço / Tipo: ", linha.get(1).toString() + " - " + linha.get(3).toString());
                }
            }
        }
        return listaServicoSemCobranca;
    }

    public void setListaServicoSemCobranca(List<Vector> listaServicoSemCobranca) {
        this.listaServicoSemCobranca = listaServicoSemCobranca;
    }
}
