package br.com.rtools.seguranca.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class RegistroEmpresarialJSFBean {

    private Registro registro = new Registro();
    private String senha = "";
    private String confirmaSenha = "";
    private String msgConfirma = "";
    private int codigoModulo = 0;

    public void refreshForm() {
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (!senha.isEmpty()) {
            if (!senha.equals(confirmaSenha)) {
                msgConfirma = "Senhas não correspondem";
                return null;
            }
            registro.setSenha(senha);
        }
        sv.abrirTransacao();
        if (sv.alterarObjeto(registro)) {
            sv.comitarTransacao();
            msgConfirma = "Cadastro atualizado com sucesso!";
        } else {
            sv.desfazerTransacao();
            msgConfirma = "Não foi possivel atualizar Cadastro!";
        }
        return null;
    }

    public String salvarSemSenha() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (sv.alterarObjeto(registro)) {
            sv.comitarTransacao();
            msgConfirma = "Cadastro atualizado com sucesso!";
        } else {
            sv.desfazerTransacao();
            msgConfirma = "Não foi possivel atualizar Cadastro!";
        }
        return null;
    }

    public String criarLoginsUsuarios() {
        List<Pessoa> listaPessoas = new ArrayList<Pessoa>();
        PessoaDB db = new PessoaDBToplink();
        listaPessoas = db.pesquisaTodosSemLogin();
        // String login = "", senha = "";
        String senha = "";
        senha = senha + DataHoje.hora().replace(":", "");
        for (int i = 0; i < listaPessoas.size(); i++) {
            try {
                senha = Integer.toString(Integer.parseInt(senha) + Integer.parseInt(senha + "43"));
                senha = getGerarLoginSenhaPessoa(listaPessoas.get(i), senha);
            } catch (Exception e) {
                // String erros = nome+"--"+login+"--";
                msgConfirma = "Erro ao Gerar!" + e;
                continue;
            }
        }
        msgConfirma = "Geração concluída!";
        return null;
    }

    public String novo() {
        return "registroEmpresarial";
    }

    public Registro getRegistro() {
        if (registro != null) {
            if (registro.getId() == -1) {
                FilialDB db = new FilialDBToplink();
                registro = db.pesquisaCodigoRegistro(1);
                senha = registro.getSenha();
            }
        }
        return registro;
    }

    public String getGerarLoginSenhaPessoa(Pessoa pessoa, String senhaInicial) {
        PessoaDB db = new PessoaDBToplink();
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
        db.getEntityManager().getTransaction().begin();
        if (db.update(pessoa)) {
            db.getEntityManager().getTransaction().commit();
        } else {
            db.getEntityManager().getTransaction().rollback();
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
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
}