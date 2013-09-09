package br.com.rtools.escola.beans;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.escola.db.VendedorDB;
import br.com.rtools.escola.db.VendedorDBToplink;
import br.com.rtools.pessoa.Pessoa;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class VendedorJSFBean implements java.io.Serializable {
    private Vendedor vendedor = new Vendedor();
    private String msgConfirma = "";
    private List<Vendedor> listaVendedores = new ArrayList();
    private Pessoa pessoa = new Pessoa();
    private int idIndex = -1;

    public String adicionar(){
        VendedorDB db = new VendedorDBToplink();
        if (pessoa.getId() == -1){
            msgConfirma = "Pesquise uma Pessoa para ser vendedora!";
            return null;
        }
        
        for(int i = 0; i < listaVendedores.size(); i++){
            if (listaVendedores.get(i).getPessoa().getId() == pessoa.getId()){
                msgConfirma = "Este vendedor já existe!";
                pessoa = new Pessoa();
                vendedor = new Vendedor();
                return null;
            }
        }
        vendedor.setPessoa(pessoa);
        if (db.insert(vendedor)){
            msgConfirma = "Vendedor adicionado com sucesso!";
        }else{
            msgConfirma = "Erro ao adicionar!";
        }
        listaVendedores.clear();
        pessoa = new Pessoa();
        vendedor = new Vendedor();
        return null;
    }

    public String excluir(){
        VendedorDB db = new VendedorDBToplink();
        vendedor = (Vendedor)listaVendedores.get(idIndex);
        if (db.delete(db.pesquisaCodigo(vendedor.getId()))){
            msgConfirma = "Excluído com sucesso!";
            listaVendedores.clear();
        }else{
            msgConfirma = "Erro ao Excluir!";
        }
        vendedor = new Vendedor();
        return null;
    }

    public List<Vendedor> getListaVendedores() {
        VendedorDB db = new VendedorDBToplink();
        if (listaVendedores.isEmpty()){
            listaVendedores = db.pesquisaTodos();
        }
        return listaVendedores;
    }

    public void setListaVendedores(List<Vendedor> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null){
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}