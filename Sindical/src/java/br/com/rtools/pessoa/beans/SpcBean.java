package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Spc;
import br.com.rtools.pessoa.db.SpcDB;
import br.com.rtools.pessoa.db.SpcDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SpcBean {

    private Spc spc = new Spc();
    private List<Spc> listaSPC = new ArrayList<Spc>();
    private String mensagem = "";
    private String botaoSalvar = "Adicionar";
    private String descricaoPesquisa = "";
    private String porPesquisa = "";
    private String comoPesquisa = "";
    private boolean filtro = false;
    private boolean filtroPorPessoa = true;

    public void novo () {
        botaoSalvar = "Adicionar";
        spc = new Spc();
        mensagem = "";
        listaSPC.clear();
        descricaoPesquisa = "";
    }
    
    public void salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (spc.getPessoa().getId() == -1) {
            mensagem = "Pesquisar pessoa!";
            return;
        }
        if (spc.getDataEntrada().equals("")) {
            mensagem = "Informar data de entrada!";
            return;
        }
        mensagem = "";
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
        listaSPC.clear();
        descricaoPesquisa = "";
        spc = s;
    }

    public Spc getSpc() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            Pessoa pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
            if (pessoa.getId() != spc.getPessoa().getId()) {
                listaSPC.clear();
                spc.setId(-1);
                spc.setPessoa(pessoa);
                spc.setObservacao("");
                spc.setDtSaida(null);
                descricaoPesquisa = "";
            }
            spc.setPessoa(pessoa);
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
            if (descricaoPesquisa.equals("")) {
                if (spc.getPessoa().getId() != -1) {
                    listaSPC = (List<Spc>) spcdb.lista(spc, true, true);
                } else {
                    listaSPC = (List<Spc>) spcdb.lista(spc, filtro, false);
                }
            } else {
                listaSPC = (List<Spc>) spcdb.lista(spc, filtro, false, descricaoPesquisa, porPesquisa, comoPesquisa);
            }
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

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }
    
    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }    

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }
}
