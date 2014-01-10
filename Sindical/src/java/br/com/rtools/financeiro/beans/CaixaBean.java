package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;


@ManagedBean
@SessionScoped
public class CaixaBean implements Serializable{
    private int idFilial;
    private List<SelectItem> listaFiliais = new ArrayList<SelectItem>();
    private Caixa caixa = new Caixa();
    private List<Caixa> listaCaixa = new ArrayList<Caixa>();
    
    public void excluir(Caixa c){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (!sv.deletarObjeto(sv.pesquisaCodigo(c.getId(), "Caixa"))){
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Não foi possível excluir Caixa!");
        }else{
            sv.comitarTransacao();
            listaCaixa.clear();
            GenericaMensagem.info("Sucesso", "Caixa excluido com Sucesso!");
        }
        
    }
    
    public void salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        caixa.setFilial( (Filial)sv.pesquisaCodigo(Integer.valueOf(listaFiliais.get(idFilial).getDescription()), "Filial") );
        if (!sv.inserirObjeto(caixa)){
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Não foi possível salvar Caixa!");
        }else{
            sv.comitarTransacao();
            caixa = new Caixa();
            listaCaixa.clear();
            GenericaMensagem.info("Sucesso", "Caixa adicionado com Sucesso!");
        }
    }
    
    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Filial> list = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(new Integer(i),
                        list.get(i).getFilial().getPessoa().getDocumento() + " - " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public void setListaFiliais(List<SelectItem> listaFiliais) {
        this.listaFiliais = listaFiliais;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public List<Caixa> getListaCaixa() {
        if (listaCaixa.isEmpty()){
            SalvarAcumuladoDB sv  = new SalvarAcumuladoDBToplink();
            listaCaixa = sv.listaObjeto("Caixa");
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<Caixa> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }
}
