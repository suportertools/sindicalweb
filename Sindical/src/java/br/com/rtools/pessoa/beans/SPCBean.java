package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Spc;
import br.com.rtools.pessoa.db.SpcDB;
import br.com.rtools.pessoa.db.SpcDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SPCBean {

    private Spc spc = new Spc();
    private List<Spc> listaSPC = new ArrayList<Spc>();
    private String mensagem = "";
    private String botaoSalvar = "Adicionar";
    private boolean filtro = false;
    private boolean filtroPorPessoa = false;

    public void novo () {
        botaoSalvar = "Adicionar";
        spc = new Spc();
        mensagem = "";
    }
    
    public void salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (spc.getId() == -1) {
            SpcDB spcdb = new SpcDBToplink();
            if(spcdb.existeCadastroSPC(spc)) {
                mensagem = "Pessoa já existe para data específicada";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(spc)) {
                salvarAcumuladoDB.comitarTransacao();
                listaSPC.clear();
                mensagem = "Registro inserido com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao inserir este registro!";
            }
        } else {
            botaoSalvar = "Atualizar";
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(spc)) {
                salvarAcumuladoDB.comitarTransacao();
                listaSPC.clear();
                mensagem = "Registro atualizado com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Pessoa já existe para data específicada";
            }
        }
    }

    public void editar(Spc s) {
        spc = s;
    }

    public Spc getSpc() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            spc.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        if (spc.getId() == -1) {
            botaoSalvar = "Adicionar";
        } else {
            botaoSalvar = "Atualizar";
        }
        return spc;
    }

    public void setSpc(Spc spc) {
        this.spc = spc;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getBotaoSalvar() {
        return botaoSalvar;
    }

    public void setBotaoSalvar(String botaoSalvar) {
        this.botaoSalvar = botaoSalvar;
    }

    public List<Spc> getListaSPC() {
        if (listaSPC.isEmpty()) {
            SpcDB spcdb = new SpcDBToplink();
            listaSPC = (List<Spc>) spcdb.lista(spc, filtro, filtroPorPessoa);
        }
        return listaSPC;
    }

    public void setListaSPC(List<Spc> listaSPC) {
        this.listaSPC = listaSPC;
    }

    public boolean isFiltro() {
        return filtro;
    }

    public void setFiltro(boolean filtro) {
        this.filtro = filtro;
    }

    public boolean isFiltroPorPessoa() {
        return filtroPorPessoa;
    }

    public void setFiltroPorPessoa(boolean filtroPorPessoa) {
        this.filtroPorPessoa = filtroPorPessoa;
    }
}
