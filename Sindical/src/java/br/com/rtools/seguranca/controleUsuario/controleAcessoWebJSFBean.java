package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.utilitarios.EnviarEmail;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

public class controleAcessoWebJSFBean {

    private Registro registro = new Registro();
    private Pessoa pessoa = new Pessoa();
    private Pessoa pessoaContribuinte = null;
    private Pessoa pessoaContabilidade = null;
    private Pessoa pessoaPatronal = null;
    private Juridica empresa = new Juridica();
    private String login = "";
    private String status = "";
    private String strTipoPesquisa = "cnpj";
    private String descPesquisa = "";
    private String msgEmail = "";
    private String msgNovaSenha = "";
    private String verificaSenha = "";
    private String comoPesquisa = "";
    private String novaSenha = "";
    private String confirmaSenha = "";
    private String email = "";
    private String msgLoginInvalido = "";
    private boolean renEsqueci = false;
    private List listaEmpresas = new ArrayList();
    private int idJuridica = 0;
    private String link = "";
    private boolean tipoLink = false;

    public void refreshForm() {
    }

    public Pessoa getPessoaContribuinte() {
        return pessoaContribuinte;
    }

    public void setPessoaContribuinte(Pessoa pessoaContribuinte) {
        this.pessoaContribuinte = pessoaContribuinte;
    }

    public Pessoa getPessoaContabilidade() {
        return pessoaContabilidade;
    }

    public void setPessoaContabilidade(Pessoa pessoaContabilidade) {
        this.pessoaContabilidade = pessoaContabilidade;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String validacao() throws IOException {
        String pagina = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("indicaAcesso", "web");
        UsuarioDB db = new UsuarioDBToplink();
        pessoa = db.ValidaUsuarioWeb(pessoa.getLogin(), pessoa.getSenha());
        if (pessoa != null) {
            pessoaContribuinte = db.ValidaUsuarioContribuinteWeb(pessoa.getId());
            pessoaContabilidade = db.ValidaUsuarioContabilidadeWeb(pessoa.getId());
            if (pessoaContribuinte != null && pessoaContabilidade == null) {
                JuridicaDB dbj = new JuridicaDBToplink();
                List listax = dbj.listaJuridicaContribuinte(dbj.pesquisaJuridicaPorPessoa(pessoaContribuinte.getId()).getId());
                if (listax.isEmpty()) {
                    msgLoginInvalido = "Usuário não contribuinte!";
                    return null;
                } else {
                    if ( ((List) listax.get(0)).get(11) != null) {
                        msgLoginInvalido = "Contribuinte inativo, contate seu sindicato!";
                        return null;
                    } else {
                    }
                }
            }
            pessoaPatronal = db.ValidaUsuarioPatronalWeb(pessoa.getId());
        } else {
            pessoaContribuinte = null;
            pessoaContabilidade = null;
            pessoaPatronal = null;
        }

        if ((pessoaContribuinte != null) && (pessoaContabilidade != null) && pessoaPatronal == null) {
            status = "Contribuinte - Contabilidade";
        } else if (pessoaContribuinte != null && pessoaPatronal == null) {
            status = "Contribuinte";
        } else if (pessoaContabilidade != null && pessoaPatronal == null) {
            status = "Contabilidade";
        } else if (pessoaPatronal != null) {
            status = "Patronal";
        }

        if ((pessoaContribuinte != null) || (pessoaContabilidade != null) || (pessoaPatronal != null)) {
            pagina = "menuPrincipalAcessoWeb";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessaoUsuarioAcessoWeb", pessoa);
            if (pessoa.getTipoDocumento().getId() == 4) {
                login = pessoa.getNome() + " ( " + status + " )";
            } else {
                login = pessoa.getNome() + " - "
                        + pessoa.getTipoDocumento().getDescricao() + ": "
                        + pessoa.getDocumento() + " ( "
                        + status + " )";
            }
            pessoa = new Pessoa();
//           pessoaContribuinte = new Pessoa();
//           pessoaContabilidade = new Pessoa();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        } else {
            if (pessoa == null) {
                msgLoginInvalido = "Usuário ou/e senha inválidos!";
            } else {
                msgLoginInvalido = "Usuário não Contribuinte, ou Contabilidade sem Empresa!";
            }
            pessoa = new Pessoa();
//           pessoaContribuinte = new Pessoa();
//           pessoaContabilidade = new Pessoa();
        }
        return pagina;
    }

    public String enviarEmail() {
        FilialDB db = new FilialDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        empresa = new Juridica();
        if (!strTipoPesquisa.equals("nome")) {
            List lista = dbJur.pesquisaJuridicaPorDoc(descPesquisa);
            if (!lista.isEmpty()) {
                empresa = (Juridica) lista.get(0);
                if (!email.equals("")) {
                    if (validaEmail(empresa)) {
                        msgEmail = EnviarEmail.EnviarEmail(db.pesquisaCodigoRegistro(1), empresa);
                    } else {
                        msgEmail = "E-mail digitado inválido!";
                    }
                } else {
                    msgEmail = "Digite um e-mail para verificação!";
                }
            } else {
                msgEmail = "Empresa não encontrada em Sistema, Contate o seu Sindicato";
            }
        } else {
            if (!getListaEmpresa().isEmpty()) {
                empresa = dbJur.pesquisaCodigo(Integer.parseInt(getListaEmpresa().get(idJuridica).getDescription()));
                if (empresa != null) {
                    if (!email.equals("")) {
                        if (validaEmail(empresa)) {
                            msgEmail = EnviarEmail.EnviarEmail(db.pesquisaCodigoRegistro(1), empresa);
                        } else {
                            msgEmail = "E-mail digitado inválido!";
                        }
                    } else {
                        msgEmail = "Digite um e-mail para verificação!";
                    }
                } else {
                    msgEmail = "Empresa não encontrada em Sistema, Contate o seu Sindicato";
                }
            } else {
                msgEmail = "Pesquise a sua Empresa Para concluir solicitacao!";
            }
        }
        return "indexAcessoWeb";
    }

    public boolean validaEmail(Juridica emp) {
        if (emp.getPessoa().getEmail1() != null) {
            if (emp.getPessoa().getEmail1().equals(email)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<SelectItem> getListaEmpresa() {
        if (listaEmpresas.isEmpty()) {
            int i = 0;
            JuridicaDB db = new JuridicaDBToplink();
            List select = db.pesquisaPessoa(descPesquisa, "nome", comoPesquisa);
            while (i < select.size()) {
                listaEmpresas.add(new SelectItem(new Integer(i),
                        (String) ((Juridica) select.get(i)).getPessoa().getNome() + " - "
                        + (String) ((Juridica) select.get(i)).getPessoa().getTipoDocumento().getDescricao() + ": "
                        + (String) ((Juridica) select.get(i)).getPessoa().getDocumento(),
                        Integer.toString(((Juridica) select.get(i)).getId())));
                i++;
            }
        }
        return listaEmpresas;
    }

    public String salvarConf() {
        PessoaDB db = new PessoaDBToplink();
        if (!novaSenha.equals("")) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
            if (pessoa.getId() != -1) {
                if (verificaSenha.equals(pessoa.getSenha()) && confirmaSenha.equals(novaSenha)) {
                    pessoa.setSenha(novaSenha);
                    db.getEntityManager().getTransaction().begin();
                    if (db.update(pessoa)) {
                        msgNovaSenha = "Atualizado com sucesso!";
                        db.getEntityManager().getTransaction().commit();
                    } else {
                        msgNovaSenha = "Erro ao Atualizar Senha!";
                        db.getEntityManager().getTransaction().rollback();
                    }
                } else {
                    msgNovaSenha = "Senha de verificação inválida!";
                }
            }
        } else {
            msgNovaSenha = "Digite uma Senha para ser atualizado!";
        }
        pessoa = new Pessoa();
        return null;
    }

    public void inicial() {
        comoPesquisa = "I";
        listaEmpresas = new ArrayList();
    }

    public void parcial() {
        comoPesquisa = "P";
        listaEmpresas = new ArrayList();
    }

    public String esqueciASenha() {
        if (renEsqueci) {
            pessoa = new Pessoa();
            pessoaContribuinte = null;
            pessoaContabilidade = null;
            empresa = new Juridica();
            login = "";
            status = "";
            strTipoPesquisa = "cnpj";
            descPesquisa = "";
            msgEmail = "";
            msgNovaSenha = "";
            verificaSenha = "";
            comoPesquisa = "";
            msgNovaSenha = "";
            email = "";
            listaEmpresas = new ArrayList();
            idJuridica = 0;
            renEsqueci = false;
        } else {
            renEsqueci = true;
        }
        return "indexAcessoWeb";
    }

    public String getLinkSite() {
        PessoaDB db = new PessoaDBToplink();
        Pessoa p = db.pesquisaCodigo(1);
        if (p != null){
            if (p.getSite() != null) {
                return p.getSite();
            }
        }
        return "";
    }

    public void sairSistemaWeb() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuarioAcessoWeb");
        }
        String retorno = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
            retorno = "indexAcessoWeb.jsf?cliente=" + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente");
        }
        limparSessaoAcessoWeb();
        FacesContext.getCurrentInstance().getExternalContext().redirect(retorno);
    }

    public void limparSessaoAcessoWeb() {
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
    }

    public String getStrTipoPesquisa() {
        return strTipoPesquisa;
    }

    public void setStrTipoPesquisa(String strTipoPesquisa) {
        this.strTipoPesquisa = strTipoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getMsgEmail() {
        return msgEmail;
    }

    public void setMsgEmail(String msgEmail) {
        this.msgEmail = msgEmail;
    }

    public boolean isRenEsqueci() {
        return renEsqueci;
    }

    public void setRenEsqueci(boolean renEsqueci) {
        this.renEsqueci = renEsqueci;
    }

    public int getIdJuridica() {
        return idJuridica;
    }

    public void setIdJuridica(int idJuridica) {
        this.idJuridica = idJuridica;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getMsgNovaSenha() {
        return msgNovaSenha;
    }

    public void setMsgNovaSenha(String msgNovaSenha) {
        this.msgNovaSenha = msgNovaSenha;
    }

    public String getVerificaSenha() {
        return verificaSenha;
    }

    public void setVerificaSenha(String verificaSenha) {
        this.verificaSenha = verificaSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getMsgLoginInvalido() {
        return msgLoginInvalido;
    }

    public void setMsgLoginInvalido(String msgLoginInvalido) {
        this.msgLoginInvalido = msgLoginInvalido;
    }

    public Pessoa getPessoaPatronal() {
        return pessoaPatronal;
    }

    public void setPessoaPatronal(Pessoa pessoaPatronal) {
        this.pessoaPatronal = pessoaPatronal;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean getTipoLink() {
        PessoaDB db = new PessoaDBToplink();
        Pessoa p = new Pessoa();
        p = db.pesquisaCodigo(1);
        if (p.getSite().equals("")) {
            tipoLink = false;
        } else {
            tipoLink = true;
        }
        link = p.getSite();
        return tipoLink;
    }

    public void setTipoLink(boolean tipoLink) {
        this.tipoLink = tipoLink;
    }

    public Registro getRegistro() {
        if (registro.getId() == -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            registro = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }
}