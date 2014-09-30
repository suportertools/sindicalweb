package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.arrecadacao.Empregados;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.JuridicaReceita;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.Porte;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.pessoa.db.TipoEnderecoDB;
import br.com.rtools.pessoa.db.TipoEnderecoDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class ControleAcessoWebBean implements Serializable {

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

    private String documento = "";
    private Empregados empregados = new Empregados();

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

    public JuridicaReceita pesquisaNaReceita(String documentox) {
        PessoaDB db = new PessoaDBToplink();
        JuridicaReceita jr = db.pesquisaJuridicaReceita(documentox);

        if (jr.getId() == -1) {
            try {
                System.loadLibrary("knu");
            } catch (Exception | UnsatisfiedLinkError e) {
                System.out.println(e.getMessage() + " Erro Carregar Lib ");
                GenericaMensagem.warn("Erro", "Consulta temporarimente indisponível!");
                return null;
            }

            knu.ReceitaCNPJ resultado = knu.knu.receitaCNPJ(documentox);

            if (resultado.getCod_erro() != 0) {
                GenericaMensagem.warn("Falha na Busca", resultado.getDesc_erro());
                return null;
            }

            if (resultado.getNome_empresarial().isEmpty()) {
                GenericaMensagem.warn("Erro", "Erro ao pesquisar na Receita!");
                return null;
            }

            if (resultado.getSituacao_cadastral().equals("BAIXADA")) {
                GenericaMensagem.warn("Erro", "Erro ao pesquisar na Receita!");
                return null;
            }

            jr.setNome(resultado.getNome_empresarial());
            jr.setFantasia(resultado.getNome_empresarial());
            jr.setDocumento(documentox);
            jr.setCep(resultado.getCep());
            jr.setDescricaoEndereco(resultado.getLogradouro());
            jr.setBairro(resultado.getBairro());
            jr.setComplemento(resultado.getComplemento());
            jr.setNumero(resultado.getNumero());
            jr.setCnae(resultado.getAtividade_principal());
            jr.setPessoa(null);
            jr.setStatus(resultado.getSituacao_cadastral());
            jr.setDtAbertura(DataHoje.converte(resultado.getData_abertura()));

            Dao di = new Dao();
            di.openTransaction();
            if (!di.save(jr)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
                di.rollback();
                return null;
            }
            di.commit();
        }

        Dao di = new Dao();
        di.openTransaction();
        if (jr.getPessoa() == null) {
            Pessoa pessoax = new Pessoa(
                    -1, jr.getNome(), (TipoDocumento) di.find(new TipoDocumento(), 2), "", "", DataHoje.data(), "", "", "", "", "", "", documento, "", ""
            );

            if (!di.save(pessoax)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
                di.rollback();
                return null;
            }
            jr.setPessoa(pessoax);
            di.update(jr);
        }

        Juridica juridica = new Juridica();
        juridica.setPessoa(jr.getPessoa());
        juridica.setFantasia(jr.getNome());

        String result[] = jr.getCnae().split(" ");
        CnaeDB dbc = new CnaeDBToplink();

        List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");

        if (listac.isEmpty()) {
            GenericaMensagem.warn("Erro", "Erro ao pesquisar CNAE");
            di.rollback();
            return null;
        }

        CnaeConvencaoDB dbCnaeCon = new CnaeConvencaoDBToplink();
        if (dbCnaeCon.pesquisaCnaeComConvencao(((Cnae) listac.get(0)).getId()) != null) {
            juridica.setCnae((Cnae) listac.get(0));
            // CNAE CONTRIBUINTE
        } else {
            // CNAE NÃO ESTA NA CONVENCAO
            GenericaMensagem.warn("Atenção", "Empresa não pertence a esta entidade!");
            di.rollback();
            return null;
        }

        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

        String cep = jr.getCep();
        cep = cep.replace(".", "").replace("-", "");

        String descricao[] = AnaliseString.removerAcentos(jr.getDescricaoEndereco()).split(" ");
        String bairros[] = AnaliseString.removerAcentos(jr.getBairro()).split(" ");

        Endereco endereco = dbe.enderecoReceita(cep, descricao, bairros);
        List<PessoaEndereco> listape = new ArrayList();
        if (endereco != null) {
            TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
            List tiposE = dbt.listaTipoEnderecoParaJuridica();
            for (int i = 0; i < tiposE.size(); i++) {
                PessoaEndereco pessoaEndereco = new PessoaEndereco();
                pessoaEndereco.setEndereco(endereco);
                pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
                pessoaEndereco.setPessoa(juridica.getPessoa());
                pessoaEndereco.setNumero(jr.getNumero());
                pessoaEndereco.setComplemento(jr.getComplemento());
                listape.add(pessoaEndereco);

            }
        } else {
            String msg = "Endereço não encontrado no Sistema - CEP: " + jr.getCep() + " DESC: " + jr.getDescricaoEndereco() + " BAIRRO: " + jr.getBairro();
            GenericaMensagem.warn("Erro", msg);
        }

        juridica.setPorte((Porte) di.find(new Porte(), 1));
        if (!di.save(juridica)) {
            GenericaMensagem.warn("Erro", "Não foi possível salvar EMPRESA, tente novamente!");
            di.rollback();
            return null;
        }

        for (PessoaEndereco listapex : listape) {
            if (!di.save(listapex)) {
                GenericaMensagem.warn("Erro", "Não foi possível salvar ENDEREÇO, tente novamente!");
                di.rollback();
                return null;
            }
        }

        di.commit();
        return jr;
    }

    public String salvarEmpregados() {

        Dao di = new Dao();

        di.openTransaction();
        JuridicaDB db = new JuridicaDBToplink();

        empregados.setJuridica(db.pesquisaJuridicaPorPessoa(pessoaContribuinte.getId()));
        empregados.setReferencia(DataHoje.data().substring(3));

        if (!di.save(empregados)) {
            GenericaMensagem.error("Erro", "Não foi possível salvar quantidade, tente novamente!");
            di.rollback();
            return null;
        }
        di.commit();

        status = "Contribuinte";
        login = pessoaContribuinte.getNome() + " - " + pessoaContribuinte.getTipoDocumento().getDescricao() + " : " + pessoaContribuinte.getDocumento() + " ( " + status + " )";

        GenericaSessao.put("sessaoUsuarioAcessoWeb", pessoaContribuinte);
        GenericaSessao.put("linkClicado", true);
        GenericaSessao.put("userName", pessoaContribuinte.getLogin());
        GenericaSessao.put("indicaAcesso", "web");
        return "menuPrincipalAcessoWeb";
    }

    public String validacaoDocumento() throws IOException {
        if (documento.isEmpty()) {
            GenericaMensagem.error("Login Inválido", "Digite um CNPJ válido!");
            return null;
        }

        String documentox = AnaliseString.extrairNumeros(documento);

        if (!ValidaDocumentos.isValidoCNPJ(documentox)) {
            GenericaMensagem.warn("Erro", "Documento Inválido!");
            return null;
        }

        JuridicaDB db = new JuridicaDBToplink();
        List<Juridica> listDocumento = db.pesquisaJuridicaPorDoc(documento);

        if (!listDocumento.isEmpty() && listDocumento.size() > 1) {
            GenericaMensagem.warn("Atenção", "Documento Inválido, contate seu Sindicato!");
            return null;
        }

        Juridica juridica = null;
        UsuarioDB dbu = new UsuarioDBToplink();

        // SE TER CADASTRO NO SISTEMA
        if (!listDocumento.isEmpty()) {
            juridica = listDocumento.get(0);
        } else {
            // SE NÃO TER CADASTRO NO SISTEMA
            JuridicaReceita jr = pesquisaNaReceita(documentox);
            if (jr == null) {
                return null;
            }

            juridica = db.pesquisaJuridicaPorPessoa(jr.getPessoa().getId());
        }

        pessoaPatronal = dbu.ValidaUsuarioPatronalWeb(juridica.getPessoa().getId());

        if (pessoaPatronal != null) {
            GenericaMensagem.info("Confirmação de Acesso PATRONAL", "Confirme seu Login e Senha!");
            PF.openDialog("dlg_patronal");
            return null;
        }

        pessoaContribuinte = dbu.ValidaUsuarioContribuinteWeb(juridica.getPessoa().getId());
        pessoaContabilidade = dbu.ValidaUsuarioContabilidadeWeb(juridica.getPessoa().getId());

        if (pessoaContribuinte == null && pessoaContabilidade != null) {
            GenericaMensagem.warn("Atenção", "CONTABILIDADE, entre com o CNPJ da Empresa!");
            return null;
        }

//        if (pessoaContribuinte == null){
        //            GenericaMensagem.warn("Atenção", "Empresa não contribuinte, contate seu Sindicato!");
        //            return null;
        //        }
        List<Vector> listax = db.listaJuridicaContribuinte(juridica.getId());

        if (!listax.isEmpty()) {
            // 11 - DATA DE INATIVACAO
            if (listax.get(0).get(11) != null) {
                GenericaMensagem.warn("Atenção", "Empresa Inativa, contate seu Sindicato!");
                return null;
            }
        } else {
            GenericaMensagem.warn("Atenção", "Empresa não contribuinte, contate seu Sindicato!");
            return null;
        }

        empregados = db.pesquisaEmpregados(juridica.getId());

        if (empregados == null) {
            empregados = new Empregados();
            PF.openDialog("dlg_empregados");
            return null;
        }

        status = "Contribuinte";
        login = pessoaContribuinte.getNome() + " - " + pessoaContribuinte.getTipoDocumento().getDescricao() + " : " + pessoaContribuinte.getDocumento() + " ( " + status + " )";

        GenericaSessao.put("sessaoUsuarioAcessoWeb", pessoaContribuinte);
        GenericaSessao.put("linkClicado", true);
        GenericaSessao.put("userName", pessoaContribuinte.getLogin());
        GenericaSessao.put("indicaAcesso", "web");
        return "menuPrincipalAcessoWeb";
    }

    public String validacao() throws IOException {
        if (pessoa.getLogin().isEmpty()) {
            GenericaMensagem.error("Login Inválido", "Digite um LOGIN válido!");
            return null;
        }

        if (pessoa.getSenha().isEmpty()) {
            GenericaMensagem.error("Login Inválido", "Digite uma SENHA válida!");
            return null;
        }
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
                    //msgLoginInvalido = "Usuário não contribuinte!";
                    GenericaMensagem.error("Login Inválido", "Usuário não Contribuinte");
                    return null;
                } else {
                    if (((List) listax.get(0)).get(11) != null) {
                        //msgLoginInvalido = "Contribuinte inativo, contate seu sindicato!";
                        GenericaMensagem.error("Login Inválido", "Contribuinte inativo, contate seu Sindicato!");
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
                //msgLoginInvalido = "Usuário ou/e senha inválidos!";
                GenericaMensagem.error("Login Inválido", "Usuário ou/e senha Inválidos!");
            } else {
                //msgLoginInvalido = "Usuário não Contribuinte, ou Contabilidade sem Empresa!";
                GenericaMensagem.error("Login Inválido", "Usuário não Contribuinte, ou Contabilidade sem Empresa!");
            }
            pessoa = new Pessoa();
//           pessoaContribuinte = new Pessoa();
//           pessoaContabilidade = new Pessoa();
        }
        if (pessoa != null) {
            GenericaSessao.put("userName", pessoa.getLogin());
        }
        return pagina;
    }

    public void enviarEmail() {
        if (email.isEmpty()) {
            GenericaMensagem.warn("Erro", "Digite um Email para verificação!");
            return;
        }
        Juridica empresax = new Juridica();

        if (strTipoPesquisa.equals("nome")) {
            if (descPesquisa.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Digite o nome da Empresa para Pesquisar!");
                return;
            }

            if (getListaEmpresa().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Pesquise sua empresa para concluir a Solicitação!");
                return;
            }

            empresax = (Juridica) new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(getListaEmpresa().get(idJuridica).getDescription()), "Juridica");
            if (empresax == null) {
                GenericaMensagem.warn("Não foi possível completar seu pedido", "Empresa não encontrada no Sistema, Contate o seu Sindicato.");
                return;
            }
        } else {
            if (descPesquisa.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Digite o documento da Empresa!");
                return;
            }

            JuridicaDB db = new JuridicaDBToplink();
            List<Juridica> lista = db.pesquisaJuridicaPorDoc(descPesquisa);

            if (lista.isEmpty()) {
                GenericaMensagem.warn("Não foi possível completar seu pedido", "Empresa não encontrada no Sistema, Contate o seu Sindicato.");
                return;
            }
            empresax = (Juridica) lista.get(0);
        }

        if (!validaEmail(empresax)) {
            GenericaMensagem.warn("Não foi possível completar seu pedido", "E-mail digitado é Inválido!");
            return;
        }

        DaoInterface di = new Dao();
        Mail mail = new Mail();
        mail.setFiles(new ArrayList());
        mail.setEmail(
                new Email(
                        -1,
                        DataHoje.dataHoje(),
                        DataHoje.livre(new Date(), "HH:mm"),
                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                        (Rotina) di.find(new Rotina(), 261),
                        null,
                        "Envio de Login e Senha",
                        "<h5><b>Login: </b> " + empresax.getPessoa().getLogin() + "</h5><br /> <h5><b>Senha: </b> " + empresax.getPessoa().getSenha() + "</h5>",
                        false,
                        false
                )
        );
        List<Pessoa> pessoas = new ArrayList();
        pessoas.add(empresax.getPessoa());

        List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
        EmailPessoa emailPessoa = new EmailPessoa();
        for (Pessoa pe : pessoas) {
            emailPessoa.setDestinatario(pe.getEmail1());
            emailPessoa.setPessoa(pe);
            emailPessoa.setRecebimento(null);
            emailPessoas.add(emailPessoa);
            mail.setEmailPessoas(emailPessoas);
            emailPessoa = new EmailPessoa();
        }

        String[] retorno = mail.send("personalizado");

        if (!retorno[1].isEmpty()) {
            GenericaMensagem.error("Erro", retorno[1]);
        } else {
            GenericaMensagem.info("Confirmação", "Seu LOGIN e SENHA foram enviados para o email cadastrado!");
        }
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
        if (p != null) {
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
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        //PessoaDB db = new PessoaDBToplink();
        //Pessoa p = new Pessoa();
        Pessoa p = (Pessoa) sadb.find(new Pessoa(), 1);
        //p = db.pesquisaCodigo(1);
        if (p != null) {
            tipoLink = !p.getSite().equals("");
            link = p.getSite();
        }
        return tipoLink;
    }

    public void setTipoLink(boolean tipoLink) {
        this.tipoLink = tipoLink;
    }

    public Registro getRegistro() {
        //if (registro.getId() == -1) {
        registro = registro.getRegistroEmpresarial();
        //}
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Empregados getEmpregados() {
        return empregados;
    }

    public void setEmpregados(Empregados empregados) {
        this.empregados = empregados;
    }
}
