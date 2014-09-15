package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Spc;
import br.com.rtools.pessoa.db.SpcDB;
import br.com.rtools.pessoa.db.SpcDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SpcBean {

    private Spc spc;
    private List<Spc> listaSPC;
    private String mensagem;
    private String botaoSalvar;
    private String descricaoPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private boolean filtro;
    private boolean filtroPorPessoa;

    @PostConstruct
    public void init() {
        spc = new Spc();
        listaSPC = new ArrayList<Spc>();
        mensagem = "";
        botaoSalvar = "Adicionar";
        descricaoPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        filtro = false;
        filtroPorPessoa = true;
    }

    @PreDestroy
    public void destroy() {
        clear();
        GenericaSessao.remove("pessoaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("spcBean");
    }

    public void save() {
        if (spc.getPessoa().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar pessoa!");
            return;
        }
        if (spc.getDataEntrada().equals("")) {
            GenericaMensagem.warn("Validação", "Informar data de entrada!");
            return;
        }
        mensagem = "";
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        if (spc.getId() == -1) {
            SpcDB spcdb = new SpcDBToplink();
            if (spcdb.existeCadastroSPC(spc)) {
                GenericaMensagem.warn("Validação", "Pessoa já existe para data específicada");
                return;
            }
            dao.openTransaction();
            if (dao.save(spc)) {
                novoLog.save("ID: " + spc.getId() + " - Entrada: " + spc.getDataEntrada() + " - Saída: " + spc.getDataSaida() + " - Obs: " + spc.getObservacao() + " - Pessoa (" + spc.getPessoa().getId() + ") " + spc.getPessoa().getNome());
                dao.commit();
                listaSPC.clear();
                GenericaMensagem.info("Sucesso", "Registro inserido");
            } else {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Ao inserir registro!");
            }
        } else {
            Spc s = (Spc) dao.find(spc);
            String beforeUpdate = "ID: " + s.getId() + " - Entrada: " + s.getDataEntrada() + " - Saída: " + s.getDataSaida() + " - Obs: " + s.getObservacao() + " - Pessoa (" + s.getPessoa().getId() + ") " + s.getPessoa().getNome();
            botaoSalvar = "Atualizar";
            dao.openTransaction();
            if (dao.update(spc)) {
                dao.commit();
                novoLog.update(beforeUpdate, "ID: " + spc.getId() + " - Entrada: " + spc.getDataEntrada() + " - Saída: " + spc.getDataEntrada() + " - Obs: " + spc.getObservacao() + " - Pessoa (" + spc.getPessoa().getId() + ") " + spc.getPessoa().getNome());
                listaSPC.clear();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void edit(Spc s) {
        listaSPC.clear();
        descricaoPesquisa = "";
        DaoInterface di = new Dao();
        spc = (Spc) di.rebind(s);
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
