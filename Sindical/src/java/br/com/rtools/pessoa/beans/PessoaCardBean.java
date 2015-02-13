package br.com.rtools.pessoa.beans;

import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.sistema.beans.EmailBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class PessoaCardBean implements Serializable {

    private String cliente = "";
    private Fisica fisica = new Fisica();
    private Juridica juridica = new Juridica();
    private Pessoa pessoa = new Pessoa();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private String[] imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};

    public void cardPessoa(int idPessoa) {
        close();
        Dao dao = new Dao();
        pessoa = (Pessoa) dao.find(new Pessoa(), idPessoa);
    }

    public void cardFisica(int idPessoa) {
        close();
        FisicaDB fisicaDB = new FisicaDBToplink();
        fisica = (Fisica) fisicaDB.pesquisaFisicaPorPessoa(idPessoa);
        pessoa = fisica.getPessoa();
    }

    public void cardJuridica(int idPessoa) {
        close();
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        juridica = (Juridica) juridicaDB.pesquisaJuridicaPorPessoa(idPessoa);
        if (juridica == null) {
            juridica = (Juridica) new Dao().find(new Juridica(), idPessoa);
        }
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
        if (fisica != null && fisica.getPessoa().getId() != -1) {
            for (String imagensTipo1 : imagensTipo) {
                File file = new File(arquivo + "/" + fisica.getPessoa().getId() + "." + imagensTipo1);
                if (file.exists()) {
                    return caminhoTemp + "/" + fisica.getPessoa().getId() + "." + imagensTipo1;
                }
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

    public String getImagemPessoa(String caminho) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        if (pessoa != null && pessoa.getId() != -1) {
            for (String imagensTipo1 : imagensTipo) {
                File file = new File(arquivo + "/" + pessoa.getId() + "." + imagensTipo1);
                if (file.exists()) {
                    return caminhoTemp + "/" + pessoa.getId() + "." + imagensTipo1;
                }
            }
        }
        return "";
    }

    public String getImagemPessoa(String caminho, int idPessoa) {
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
        pessoa = new Pessoa();
        pessoaEndereco = new PessoaEndereco();
        pessoaEmpresa = new PessoaEmpresa();
    }

    public PessoaEndereco getPessoaEndereco() {
        if (pessoaEndereco.getId() == -1) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            if (fisica != null && fisica.getId() != -1) {
                pessoaEndereco = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 4);
            } else if (juridica != null && juridica.getId() != -1) {
                pessoaEndereco = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 4);
            } else if (pessoa != null && pessoa.getId() != -1) {
                pessoaEndereco = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 4);
            }
            if (pessoaEndereco == null) {
                pessoaEndereco = new PessoaEndereco();
            }
        }
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        if (pessoaEmpresa.getId() == -1) {
            PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
            if (fisica != null && fisica.getId() != -1) {
                pessoaEmpresa = (PessoaEmpresa) pessoaEmpresaDB.pesquisaPessoaEmpresaPorFisica(fisica.getId());
            } else if (pessoa != null && pessoa.getId() != -1) {
                pessoaEmpresa = (PessoaEmpresa) pessoaEmpresaDB.pesquisaPessoaEmpresaPorPessoa(pessoa.getId());
            }
        }
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public String enviaEmail(int idPessoa) throws IOException {
        String urlDestino = ((HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest())).getRequestURI();
        ChamadaPaginaBean chamadaPaginaBean = new ChamadaPaginaBean();
        urlDestino = chamadaPaginaBean.converteURL(urlDestino);
        return enviaEmail(idPessoa, urlDestino);
    }

    public String enviaEmail(int idPessoa, String urlRetorno) throws IOException {
        Dao dao = new Dao();
        Pessoa p = (Pessoa) dao.find(new Pessoa(), idPessoa);
        if (p == null || p.getEmail1().isEmpty()) {
            return null;
        }
        EmailBean emailBean = new EmailBean();
        emailBean.destroy();
        emailBean.init();
        emailBean.newMessage();
        emailBean.getEmail().setAssunto("Contato");
        emailBean.getEmail().setRotina((Rotina) dao.find(new Rotina(), 112));
        emailBean.getEmail().setMensagem("Prezada Sr(a) " + p.getNome());
        emailBean.setEmailPessoa(new EmailPessoa(-1, emailBean.getEmail(), p, p.getEmail1(), "", "", null, DataHoje.livre(new Date(), "H:m")));
        emailBean.addEmail();
        emailBean.setUrlRetorno(urlRetorno);
        GenericaSessao.put("emailBean", emailBean);
        return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pesquisa("email");
    }

    public String getStatusJuridica() {
        return status(juridica.getPessoa());
    }

    public String getStatusFisica() {
        return status(fisica.getPessoa());
    }

    public String getStatusPessoa() {
        return status(pessoa);
    }

    public String getContribuinteStatus() {
        return situacao(pessoa);
    }

    public String getStatusJuridicaPorPessoaEmpresa() {
        return status(pessoaEmpresa.getJuridica().getPessoa());
    }

    public String getStatusFisicaPorPessoaEmpresa() {
        return status(pessoaEmpresa.getFisica().getPessoa());
    }

    public String status(Pessoa p) {
        MovimentoDB movimentoDB = new MovimentoDBToplink();
        if (p.getId() != -1) {
            if (movimentoDB.existeDebitoPessoa(pessoa, DataHoje.dataHoje())) {
                return "EM DÉBITO";
            } else {
                return "REGULAR";
            }
        }
        return "";
    }

    public String situacao(Pessoa p) {
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        if (juridicaDB.empresaInativa(p.getId())) {
            return "CONTRIBUINTE INÁTIVO";
        } else {
            return "CONTRIBUINTE";
        }
    }

}
