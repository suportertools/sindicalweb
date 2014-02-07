package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Cartao;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CartaoBean implements Serializable{
    private Cartao cartao = new Cartao();
    private List<Cartao> listaCartao = new ArrayList();
    
    public void editar(Cartao c){
        cartao = c;
    }
    
    public void salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        if (cartao.getDescricao().isEmpty()){
            GenericaMensagem.warn("Erro", "O campo descrição não pode estar vazio!");
            return;
        }
        
        sv.abrirTransacao();
        if (cartao.getId() == -1){
            if (sv.inserirObjeto(cartao)){
                sv.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Cartão salvo com Sucesso!");
                listaCartao.clear();
                cartao = new Cartao();
            }else{
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Não foi possível salvar este Cartão!");
            }
        }else{
            if (sv.alterarObjeto(cartao)){
                sv.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Cartão alterado com Sucesso!");
                listaCartao.clear();
                cartao = new Cartao();
            }else{
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Não foi possível alterar este Cartão!");
            }
        }
    }
    
    public void excluir(Cartao c){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (sv.deletarObjeto(sv.pesquisaCodigo(c.getId(), "Cartao"))){
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Cartão excluído com Sucesso!");
            listaCartao.clear();
            cartao = new Cartao();
        }else{
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Este cartão não pode ser excluido!");
        }
    }
    
    public void removerPlano(){
        cartao.setPlano5(new Plano5());
    }
    
    public Cartao getCartao() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaPlano") != null) {
            cartao.setPlano5((Plano5) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaPlano"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pesquisaPlano");
        }
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public List<Cartao> getListaCartao() {
        if (listaCartao.isEmpty()){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            
            listaCartao = sv.listaObjeto("Cartao");
        }
        return listaCartao;
    }

    public void setListaCartao(List<Cartao> listaCartao) {
        this.listaCartao = listaCartao;
    }

}
