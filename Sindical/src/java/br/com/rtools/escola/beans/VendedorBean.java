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
    private String msgConfirma = "";
    private List<Vendedor> listaVendedores = new ArrayList();
    private Pessoa pessoa = new Pessoa();
    private int idIndex = -1;

    public String adicionar() {
        if (pessoa.getId() == -1) {
            msgConfirma = "Pesquise uma Pessoa para ser vendedora!";
            return null;
        }
        vendedor.setPessoa(pessoa);
        VendedorDB vendedorDB = new VendedorDBToplink();
        if (vendedorDB.existeVendedor(vendedor)) {
            msgConfirma = "Este vendedor já existe!";
            pessoa = new Pessoa();
            vendedor = new Vendedor();
            return null;
        }

        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(vendedor)) {
            salvarAcumuladoDB.comitarTransacao();
            msgConfirma = "Vendedor adicionado com sucesso!";
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            msgConfirma = "Erro ao adicionar!";
        }
        listaVendedores.clear();
        pessoa = new Pessoa();
        vendedor = new Vendedor();
        return null;
    }

    public String excluir(Vendedor v) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        vendedor = (Vendedor) salvarAcumuladoDB.pesquisaCodigo(v.getId(), "Vendedor");
        if (vendedor != null) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(vendedor)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Excluído com sucesso!";
                listaVendedores.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao Excluir!";
            }
        }
        vendedor = new Vendedor();
        return null;
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
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
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