package br.com.rtools.arrecadacao.beans;

import br.com.rtools.associativo.dao.PesquisaBoletosSocialDao;
import br.com.rtools.financeiro.MovimentoBoleto;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class PesquisaBoletosSocialBean {
    private String boleto = "";
    private List<DataObject> lista = new ArrayList();
    private List<MovimentoBoleto> listaMovimentoBoleto = new ArrayList();
    
    // NÃO UTILIZAVEL POR ENQUANTO
//    @PostConstruct
//    public void init(){
//    
//    }
        
    @PreDestroy
    public void destroy(){
        GenericaSessao.remove("pesquisaBoletosSocialBean");
    }
    
    public void voltarBoletos(){
        if (listaMovimentoBoleto.isEmpty()){
            GenericaMensagem.warn("Atenção", "Lista de Movimentos vazia!");
            return;
        }
        
        for (MovimentoBoleto mb : listaMovimentoBoleto) {
            if (mb.getMovimento().getBaixa() != null){
                GenericaMensagem.warn("Atenção", "Existem Movimentos Baixados!");
                return;
            }
        }
        
        Dao dao = new Dao();
        
        dao.openTransaction();
        for (MovimentoBoleto mb : listaMovimentoBoleto) {
            mb.getMovimento().setDocumento(boleto);
            mb.getMovimento().setNrCtrBoleto(mb.getBoleto().getNrCtrBoleto());
            
            if (!dao.update(mb.getMovimento())){
                GenericaMensagem.error("Erro", "Não foi possível atualizar Boleto!");
                dao.rollback();
                return;
            }
        }

        dao.commit();
        GenericaMensagem.info("Sucesso", "Movimentos atualizados!");
        loadList();
    }
    
    public void loadList() {
        lista.clear();
        listaMovimentoBoleto.clear();

        if (boleto.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite um número de boleto!");
            return;
        }

        listaMovimentoBoleto = new PesquisaBoletosSocialDao().listaMovimentoBoleto(boleto);

        if (listaMovimentoBoleto.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhum Boleto encontrado!");
        }
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public List<DataObject> getLista() {
        return lista;
    }

    public void setLista(List<DataObject> lista) {
        this.lista = lista;
    }

    public List<MovimentoBoleto> getListaMovimentoBoleto() {
        return listaMovimentoBoleto;
    }

    public void setListaMovimentoBoleto(List<MovimentoBoleto> listaMovimentoBoleto) {
        this.listaMovimentoBoleto = listaMovimentoBoleto;
    }

}
