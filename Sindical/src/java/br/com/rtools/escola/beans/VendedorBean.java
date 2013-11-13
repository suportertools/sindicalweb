package br.com.rtools.escola.beans;

import br.com.rtools.escola.Vendedor;
import br.com.rtools.escola.db.VendedorDB;
import br.com.rtools.escola.db.VendedorDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class VendedorBean implements java.io.Serializable {

    private Vendedor vendedor = new Vendedor();
    private String mensagem = "";
    private List<Vendedor> listaVendedores = new ArrayList();

    public void adicionar() {
        if (vendedor.getPessoa().getId() == -1) {
            mensagem = "Pesquise uma Pessoa para ser vendedora!";
            return;
        }
        VendedorDB vendedorDB = new VendedorDBToplink();
        if (vendedorDB.existeVendedor(vendedor)) {
            mensagem = "Este vendedor já existe!";
            vendedor = new Vendedor();
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(vendedor)) {
            salvarAcumuladoDB.comitarTransacao();
            mensagem = "Vendedor adicionado com sucesso!";
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            mensagem = "Erro ao adicionar!";
        }
        listaVendedores.clear();
        vendedor = new Vendedor();
    }

    public void excluir(Vendedor v) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        vendedor = (Vendedor) salvarAcumuladoDB.pesquisaCodigo(v.getId(), "Vendedor");
        if (vendedor != null) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(vendedor)) {
                salvarAcumuladoDB.comitarTransacao();
                mensagem = "Excluído com sucesso!";
                listaVendedores.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao Excluir!";
            }
        }
        vendedor = new Vendedor();
    }

    public List<Vendedor> getListaVendedores() {
        if (listaVendedores.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaVendedores = (List<Vendedor>) salvarAcumuladoDB.listaObjeto("Vendedor", true);
        }
        return listaVendedores;
    }

    public void setListaVendedores(List<Vendedor> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Vendedor getVendedor() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            vendedor.setPessoa((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}
