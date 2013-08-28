package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDB;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDBTopLink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class ConvencaoPeriodoJSFBean {
    
    private ConvencaoPeriodo convencaoPeriodo = new ConvencaoPeriodo();
    private String msg = "";
    private int idConvencao = 0;
    private int idGrupoCidade = 0;
    private int idIndex = -1;
    private List<ConvencaoPeriodo> listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();

    public String novo(){
        setConvencaoPeriodo(new ConvencaoPeriodo());
        listaConvencaoPeriodos.clear();
        setMsg("");
        return "convencaoPeriodo";
    }
    
    public void atualizarLista(){
//        if (listaConvencaoPeriodos == null)
            listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
    }
    
    public String editar(){
        setConvencaoPeriodo( (ConvencaoPeriodo) getListaConvencaoPeriodos().get(idIndex));
        return "convencaoPeriodo";
    }
    
    public String salvar(){
        ConvencaoPeriodoDB convencaoPeriodoDB = new ConvencaoPeriodoDBTopLink();
        if(getConvencaoPeriodo().getReferenciaInicial().equals("__/____") || getConvencaoPeriodo().getReferenciaInicial().equals("")){
            setMsg("Informar a referência inicial!");
            return null;
        }
        if(getConvencaoPeriodo().getReferenciaFinal().equals("__/____") || getConvencaoPeriodo().getReferenciaFinal().equals("")){
            setMsg("Informar a referência final!");
            return null;
        }
        getConvencaoPeriodo().getGrupoCidade().setId(Integer.parseInt(getComboGrupoCidade().get(idGrupoCidade).getDescription()));
        getConvencaoPeriodo().getConvencao().setId(Integer.parseInt(getComboConvencao().get(idConvencao).getDescription()));        
        if(convencaoPeriodoDB.convencaoPeriodoExiste(getConvencaoPeriodo())){
            setMsg("Convenção período já existe!");
            return null;
        }
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        if(getConvencaoPeriodo().getId() == -1){
            db.abrirTransacao();
            if(db.inserirObjeto(getConvencaoPeriodo())){
                db.comitarTransacao();                
                novo();
                setMsg("Registro inserido com sucesso.");
                return null;
            }else{
                db.desfazerTransacao();
                setMsg("Erro ao inserir esse registro!");
                return null;
            }
        }else{
            db.abrirTransacao();
            if(db.alterarObjeto(getConvencaoPeriodo())){
                db.comitarTransacao();
                novo();
                setMsg("Registro atualizado com sucesso.");
                return null;
            }else{
                db.desfazerTransacao();
                setMsg("Erro ao atualizar esse registro!");
                return null;
            }
        }
    }
    
    public String excluir(){
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        setConvencaoPeriodo((ConvencaoPeriodo) db.pesquisaObjeto(getConvencaoPeriodo().getId(), "ConvencaoPeriodo"));                
        if(getConvencaoPeriodo().getId() != -1){
            db.abrirTransacao();
            if(db.deletarObjeto(getConvencaoPeriodo())){
                db.comitarTransacao();
                novo();
                setMsg("Registro excluído com sucesso.");
                return null;
            }else{
                db.desfazerTransacao();
                setMsg("Erro ao excluir esse registro!");
                return null;
            }
        }
        return "convencaoPeriodo";
    }
    
    public List<SelectItem> getComboConvencao(){
        List<SelectItem> result = new Vector<SelectItem>();
        List<Convencao> select = new ArrayList();
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        select = db.listaObjetoGenericoOrdem("Convencao");
        for(int i = 0; i < select.size(); i++){
            result.add(new SelectItem(new Integer(i),
                                      select.get(i).getDescricao(),
                                      Integer.toString(select.get(i).getId())));
        }
        return result;
    }    
    
    public List<SelectItem> getComboGrupoCidade(){
        List<SelectItem> result = new Vector<SelectItem>();
        List<ConvencaoCidade> select = new ArrayList();
        ConvencaoPeriodoDB db = new ConvencaoPeriodoDBTopLink();
        select = db.listaGrupoCidadePorConvencao( Integer.parseInt(getComboConvencao().get(idConvencao).getDescription()));
        for(int i = 0; i < select.size(); i++){
            result.add(new SelectItem(new Integer(i),
                                      select.get(i).getGrupoCidade().getDescricao(),
                                      Integer.toString(select.get(i).getGrupoCidade().getId())));
        }
        return result;
    }    

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIdConvencao() {
        return idConvencao;
    }

    public void setIdConvencao(int idConvencao) {
        this.idConvencao = idConvencao;
    }

    public int getIdGrupoCidade() {
        return idGrupoCidade;
    }

    public void setIdGrupoCidade(int idGrupoCidade) {
        this.idGrupoCidade = idGrupoCidade;
    }
    
    public List<ConvencaoPeriodo> getListaConvencaoPeriodos() {
        if(listaConvencaoPeriodos.isEmpty()){
            ConvencaoPeriodoDB db = new ConvencaoPeriodoDBTopLink();
            setListaConvencaoPeriodos((List<ConvencaoPeriodo>) db.listaConvencaoPeriodo());
        }
        return listaConvencaoPeriodos;
    }
    
    public void setListaConvencaoPeriodos(List<ConvencaoPeriodo> listaConvencaoPeriodos) {
        this.listaConvencaoPeriodos = listaConvencaoPeriodos;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public ConvencaoPeriodo getConvencaoPeriodo() {
        return convencaoPeriodo;
    }

    public void setConvencaoPeriodo(ConvencaoPeriodo convencaoPeriodo) {
        this.convencaoPeriodo = convencaoPeriodo;
    }

    
}
