package br.com.rtools.seguranca.beans;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.SisEmailProtocolo;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RegistroEmpresarialBean implements Serializable {

    private Registro registro;
    private String senha;
    private String confirmaSenha;
    private String mensagem;
    private String emailTeste;
    private int codigoModulo;
    private int codigoServico;
    private int idDiaVencimento;
    private int idSisEmailProtocolo;
    private List<SelectItem> listaDataVencimento;
    private boolean habilitaCorrecao;

    @PostConstruct
    public void init() {
        habilitaCorrecao = false;
        registro = new Registro();
        senha = "";
        confirmaSenha = "";
        mensagem = "";
        emailTeste = "";
        codigoModulo = 0;
        codigoServico = -1;
        idDiaVencimento = 0;
        idSisEmailProtocolo = 0;
        listaDataVencimento = new ArrayList<SelectItem>();
        if (registro.getId() == -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            registro = (Registro) sv.find(new Registro(), 1);
            senha = registro.getSenha();
            if (registro.getServicos() != null) {
                codigoServico = registro.getServicos().getId();
            }
            List<SelectItem> list = getListaSisEmailProtocolo();
            for (int i = 0; i < list.size(); i++) {
                if (registro.getSisEmailProtocolo().getId() == Integer.parseInt(list.get(i).getDescription())) {
                    idSisEmailProtocolo = i;
                    break;
                }
            }
            if (registro.getHomolocaoHabilitaCorrecao() != null && DataHoje.converteData(registro.getHomolocaoHabilitaCorrecao()).equals(DataHoje.data())) {
                habilitaCorrecao = true;
            }
        }
        if (registro == null) {
            return;
        }
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("registroEmpresarialBean");
    }

    public void salvar() {
        if (codigoModulo == 0) {
            if (!senha.isEmpty()) {
                if (!senha.equals(confirmaSenha)) {
                    GenericaMensagem.warn("Validação", "Senhas não correspondem");
                    return;
                }
                registro.setSenha(senha);
            }
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        Servicos servicos;
        if (codigoServico <= 0) {
            servicos = null;
        } else {
            servicos = (Servicos) sv.pesquisaObjeto(codigoServico, "Servicos");
        }
        registro.setSisEmailProtocolo((SisEmailProtocolo) sv.find(new SisEmailProtocolo(), Integer.parseInt(getListaSisEmailProtocolo().get(idSisEmailProtocolo).getDescription())));
        registro.setFinDiaVencimentoCobranca(idDiaVencimento);
        registro.setServicos(servicos);
        if (habilitaCorrecao) {
            registro.setHomolocaoHabilitaCorrecao(new Date());
        } else {
            registro.setHomolocaoHabilitaCorrecao(null);

        }
        if (sv.alterarObjeto(registro)) {
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro atualizado");
        } else {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Ao atualizar registro!");
        }
    }

    public void salvarSemSenha() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (sv.alterarObjeto(registro)) {
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro atualizado");
        } else {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Ao atualizar registro!");
        }
    }

    public boolean validacao() {
        int nrDataRetroativo = DataHoje.converteDataParaInteger(registro.getAgendamentoRetroativoString());
        int nrDataHoje = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
        if (nrDataRetroativo <= nrDataHoje) {
            GenericaMensagem.warn("Validação", "Data do agendamento retroativo deve ser maior ou igual a data de hoje");
            return false;
        }
        return true;
    }

    public void criarLoginsUsuarios() {
        PessoaDB db = new PessoaDBToplink();
        List<Pessoa> listaPessoas = db.pesquisaTodosSemLogin();
        // String login = "", senha = "";
        String senha = "";
        senha = senha + DataHoje.hora().replace(":", "");
        for (int i = 0; i < listaPessoas.size(); i++) {
            try {
                senha = Integer.toString(Integer.parseInt(senha) + Integer.parseInt(senha + "43"));
                senha = getGerarLoginSenhaPessoa(listaPessoas.get(i), senha);
            } catch (NumberFormatException e) {
                mensagem = "Erro ao Gerar!" + e;
            }
        }
        mensagem = "Geração concluída!";
    }

    public Registro getRegistro() {
        return registro;
    }

    public String getGerarLoginSenhaPessoa(Pessoa pessoa, String senhaInicial) {
        senhaInicial = senhaInicial.substring(senhaInicial.length() - 6, senhaInicial.length());
        String nome = AnaliseString.removerAcentos(pessoa.getNome().replace(" ", "X").toUpperCase());
        nome = nome.replace("-", "Y");
        nome = nome.replace(".", "W");
        nome = nome.replace("/", "Z");
        nome = nome.replace("A", "Q");
        nome = nome.replace("E", "R");
        nome = nome.replace("I", "H");
        nome = nome.replace("O", "P");
        nome = nome.replace("U", "M");
        nome = ("JHSRGDQ" + nome) + pessoa.getId();
        String login = nome.substring(nome.length() - 6, nome.length());
        pessoa.setLogin(login);
        pessoa.setSenha(senhaInicial);
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.alterarObjeto(pessoa)) {
            salvarAcumuladoDB.comitarTransacao();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
        }
        return senhaInicial;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getCodigoModulo() {
        String urlDestino = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        codigoModulo = 0;
        if (!urlDestino.equals("/Sindical/menuPrincipal.jsf")) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null) {
                codigoModulo = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
            }
        }
        return codigoModulo;
    }

    public void setCodigoModulo(int codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public void limparModulo() {
        GenericaSessao.remove("idModulo");
    }

    public void onChange(TabChangeEvent event) {
        Tab activeTab = event.getTab();
    }

    public int getCodigoServico() {
        return codigoServico;
    }

    public void setCodigoServico(int codigoServico) {
        this.codigoServico = codigoServico;
    }

    public List<SelectItem> getListaDataVencimento() {
        if (listaDataVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDataVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDataVencimento;
    }

    public void setListaDataVencimento(List<SelectItem> listaDataVencimento) {
        this.listaDataVencimento = listaDataVencimento;
    }

    public int getIdDiaVencimento() {
        for (int i = 1; i <= listaDataVencimento.size(); i++) {
            if (registro.getFinDiaVencimentoCobranca() == i) {
                idDiaVencimento = registro.getFinDiaVencimentoCobranca();
            }
        }
        return idDiaVencimento;
    }

    public void setIdDiaVencimento(int idDiaVencimento) {
        this.idDiaVencimento = idDiaVencimento;
    }

    public int getIdSisEmailProtocolo() {
        return idSisEmailProtocolo;
    }

    public void setIdSisEmailProtocolo(int idSisEmailProtocolo) {
        this.idSisEmailProtocolo = idSisEmailProtocolo;
    }

    public List<SelectItem> getListaSisEmailProtocolo() {
        List<SelectItem> selectItems = new ArrayList();
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        List<SisEmailProtocolo> seps = (List<SisEmailProtocolo>) sadb.listaObjeto("SisEmailProtocolo");
        for (int i = 0; i < seps.size(); i++) {
            selectItems.add(new SelectItem(i, seps.get(i).getDescricao(), "" + seps.get(i).getId()));
        }
        return selectItems;
    }

    public void enviarEmailTeste() {
        if (emailTeste.isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar e-mail!");
            return;
        }
        DaoInterface di = new Dao();
        Mail mail = new Mail();
        mail.setEmail(
                new Email(
                        -1,
                        DataHoje.dataHoje(),
                        DataHoje.livre(new Date(), "HH:mm"),
                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                        (Rotina) di.find(new Rotina(), 111),
                        null,
                        "Email teste.",
                        "",
                        false,
                        false
                )
        );
        List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
        EmailPessoa emailPessoa = new EmailPessoa();
        emailPessoa.setDestinatario(emailTeste);
        emailPessoa.setPessoa(null);
        emailPessoa.setRecebimento(null);
        emailPessoas.add(emailPessoa);
        mail.setEmailPessoas(emailPessoas);
        String[] string = mail.send();
        if (string[0].isEmpty()) {
            GenericaMensagem.warn("Validação", "Erro ao enviar mensagem!" + string[0]);
        } else {
            GenericaMensagem.info("Sucesso", "Email enviado com sucesso!");
        }
//        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
//        Juridica juridica = (Juridica) sadb.find(new Juridica(), 1);
//        juridica.getPessoa().setEmail1(emailTeste);
//        String msgEmail = EnviarEmail.EnviarEmailTeste(emailTeste);
//        if (msgEmail.isEmpty()) {
//            GenericaMensagem.warn("Validação", "Erro ao enviar mensagem!");
//            return;
//        }
        //GenericaMensagem.info("Sucesso", msgEmail);
    }

    public String getEmailTeste() {
        return emailTeste;
    }

    public void setEmailTeste(String emailTeste) {
        this.emailTeste = emailTeste;
    }

    public boolean isHabilitaCorrecao() {
        return habilitaCorrecao;
    }

    public void setHabilitaCorrecao(boolean habilitaCorrecao) {
        this.habilitaCorrecao = habilitaCorrecao;
    }

}
