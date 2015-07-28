package br.com.rtools.suporte.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.suporte.*;
import br.com.rtools.suporte.dao.PrioridadeDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AgendaTarefaBean {

    private OrdemServico ordemServico;
    private Interrupcao interrupcao;
    private Protocolo protocolo;
    private ProStatus proStatus;
    private Pessoa pessoa;
    private Usuario usuario;
    private String comoPesquisa;
    private String descPesquisa;
    private String mensagem;
    private String porPesquisa;
    private String dataHoje;
    private String novaTarefa;
    private List<SelectItem> listaPrioridade;
    private List<SelectItem> listaRotina;
    private List<SelectItem> listaUsuario;
    private List<SelectItem> listaModulo;
    private List<SelectItem> listaProStatus;
    private List<DataObject> listaTarefas;
    private List<Interrupcao> listaInterrupcao;
    private int idProStatus;
    private int idPrioridade;
    private int idRotina;
    private int idUsuario;
    private int idModulo;

    public AgendaTarefaBean() {
        ordemServico = new OrdemServico();
        interrupcao = new Interrupcao();
        protocolo = new Protocolo();
        proStatus = new ProStatus();
        comoPesquisa = "";
        descPesquisa = "";
        mensagem = "";
        listaPrioridade.clear();
        idPrioridade = 0;
        listaRotina.clear();
        idRotina = 0;
        listaUsuario.clear();
        idUsuario = 0;
        listaInterrupcao.clear();
        listaModulo.clear();
        idModulo = 0;
        listaProStatus.clear();
        idProStatus = 0;
        pessoa = new Pessoa();
        usuario = new Usuario();
        dataHoje = DataHoje.data();
        porPesquisa = "todos";
        listaTarefas.clear();
    }

    public void novo() {

    }

    public void salvar() {
        if (listaRotina.isEmpty()) {
            mensagem = "Cadastrar rotinas! Lista esta vazia.";
            return;
        }
        if (listaModulo.isEmpty()) {
            mensagem = "Cadastrar módulos! Lista esta vazia.";
            return;
        }
        if (listaUsuario.isEmpty()) {
            mensagem = "Cadastrar usuários! Lista esta vazia.";
            return;
        }
        if (listaPrioridade.isEmpty()) {
            mensagem = "Cadastrar prioridades! Lista esta vazia.";
            return;
        }
        if (listaProStatus.isEmpty()) {
            mensagem = "Cadastrar status! Lista esta vazia.";
            return;
        }
        Dao dao = new Dao();
        ordemServico.setRotina((Rotina) dao.find(new Rotina(), Integer.parseInt(listaRotina.get(idRotina).getDescription())));
        ordemServico.setModulo((Modulo) dao.find(new Modulo(), Integer.parseInt(listaModulo.get(idModulo).getDescription())));
        ordemServico.setResponsavel((Usuario) dao.find(new Usuario(), Integer.parseInt(listaUsuario.get(idUsuario).getDescription())));
        ordemServico.setPrioridade((Prioridade) dao.find(new Prioridade(), Integer.parseInt(listaPrioridade.get(idPrioridade).getDescription())));
        ordemServico.setProStatus((ProStatus) dao.find(new ProStatus(), Integer.parseInt(listaProStatus.get(idProStatus).getDescription())));
        dao.openTransaction();
        if (ordemServico.getId() == -1) {
            if (dao.save(ordemServico)) {
                dao.commit();
            } else {
                dao.rollback();
            }
        } else {
            if (dao.update(ordemServico)) {
                dao.commit();
            } else {
                dao.rollback();
            }

        }

    }

    public void editar(OrdemServico os) {
        ordemServico = os;
    }

    public void excluir() {

    }

    public void salvarInterrupcao(Interrupcao i) {
        interrupcao = i;
    }

    public void editarInterrupcao(Interrupcao i) {
        interrupcao = i;
    }

    public void excluirInterrupcao(Interrupcao i) {
        interrupcao = i;
    }

    public List<SelectItem> getListaModulo() {
        if (listaModulo.isEmpty()) {
            List<Modulo> list = (List<Modulo>) new Dao().list(new Modulo(), true);
            for (int i = 0; i < list.size(); i++) {
                listaModulo.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaModulo;
    }

    public List<SelectItem> getListaPrioridade() {
        if (listaPrioridade.isEmpty()) {
            List<Prioridade> list = (List<Prioridade>) new Dao().list(new Prioridade(), true);
            for (int i = 0; i < list.size(); i++) {
                listaPrioridade.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        PrioridadeDao db = new PrioridadeDao();
        return listaPrioridade;
    }

    public List<SelectItem> getListaRotina() {
        if (listaRotina.isEmpty()) {
            List<Rotina> list = (List<Rotina>) new Dao().list(new Rotina(), true);
            for (int i = 0; i < list.size(); i++) {
                listaRotina.add(new SelectItem(i, list.get(i).getRotina(), "" + list.get(i).getId()));
            }
        }
        return listaRotina;
    }

    public List<SelectItem> getListaUsuario() {
        if (listaUsuario.isEmpty()) {
            List<Usuario> list = (List<Usuario>) new Dao().list(new Usuario(), true);
            for (int i = 0; i < list.size(); i++) {
                listaUsuario.add(new SelectItem(i, list.get(i).getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listaUsuario;
    }

    public List<SelectItem> getListaProStatus() {
        if (listaProStatus.isEmpty()) {
            List<ProStatus> list = (List<ProStatus>) new Dao().list(new ProStatus(), true);
            for (int i = 0; i < list.size(); i++) {
                listaProStatus.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaProStatus;
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setListaPrioridade(List<SelectItem> listaPrioridade) {
        this.listaPrioridade = listaPrioridade;
    }

    public void setListaRotina(List<SelectItem> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public void setListaUsuario(List<SelectItem> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public void setListaModulo(List<SelectItem> listaModulo) {
        this.listaModulo = listaModulo;
    }

    public void setListaProStatus(List<SelectItem> listaProStatus) {
        this.listaProStatus = listaProStatus;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Interrupcao getInterrupcao() {
        return interrupcao;
    }

    public void setInterrupcao(Interrupcao interrupcao) {
        this.interrupcao = interrupcao;
    }

    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    public ProStatus getProStatus() {
        return proStatus;
    }

    public void setProStatus(ProStatus proStatus) {
        this.proStatus = proStatus;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(String dataHoje) {
        this.dataHoje = dataHoje;
    }

    public String getNovaTarefa() {
        return novaTarefa;
    }

    public void setNovaTarefa(String novaTarefa) {
        this.novaTarefa = novaTarefa;
    }

    public int getIdProStatus() {
        return idProStatus;
    }

    public void setIdProStatus(int idProStatus) {
        this.idProStatus = idProStatus;
    }

    public int getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(int idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }
}
