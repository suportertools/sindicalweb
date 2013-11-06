package br.com.rtools.seguranca.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RegistroEmpresarialBean implements Serializable {

    private Registro registro = new Registro();
    private String senha = "";
    private String confirmaSenha = "";
    private String mensagem = "";
    private int codigoModulo = 0;

    public void salvar() {
//        if (!validacao()) {
//            return;
//        }        
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
        if (sv.alterarObjeto(registro)) {
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro atualizado");
        } else {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Ao atualizar registro!");
        }
    }

    public void salvarSemSenha() {
//        if (!validacao()) {
//            return;
//        }
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
        if (registro != null) {
            if (registro.getId() == -1) {
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                registro = (Registro) sv.pesquisaCodigo(1, "Registro");
                senha = registro.getSenha();
            }
        }
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

}
