package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean
@ViewScoped
public class PessoaCardBean implements Serializable {

    private String cliente = "";
    private Fisica fisica = new Fisica();
    private Juridica juridica = new Juridica();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private String[] imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};

    public void cardFisica(int idPessoa) {
        FisicaDB fisicaDB = new FisicaDBToplink();
        fisica = (Fisica) fisicaDB.pesquisaFisicaPorPessoa(idPessoa);
    }

    public void cardJuridica(int idPessoa) {
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        juridica = (Juridica) juridicaDB.pesquisaJuridicaPorPessoa(idPessoa);
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getImagemJuridica(String caminho) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + juridica.getPessoa().getId() + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + juridica.getPessoa().getId() + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getImagemJuridica(String caminho, int idPessoa) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + idPessoa + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + idPessoa + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getImagemFisica(String caminho) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + fisica.getPessoa().getId() + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + fisica.getPessoa().getId() + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getImagemFisica(String caminho, int idPessoa) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + idPessoa + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + idPessoa + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String[] getImagensTipo() {
        return imagensTipo;
    }

    public void setImagensTipo(String[] imagensTipo) {
        this.imagensTipo = imagensTipo;
    }

    public String getCliente() {
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
        }
        return cliente;
    }

    public void close() {
        juridica = new Juridica();
        fisica = new Fisica();
    }

    public PessoaEndereco getPessoaEndereco() {
        if (fisica != null) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            pessoaEndereco = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 4);
        } else if (juridica != null) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            pessoaEndereco = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 4);
        } else {
            pessoaEndereco = new PessoaEndereco();
        }
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

}
