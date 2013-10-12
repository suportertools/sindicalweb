package br.com.rtools.suporte.beans;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.suporte.*;
import br.com.rtools.suporte.db.*;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class AgendaTarefaJSFBean {

    private OrdemServico ordemServico;
    private Interrupcao interrupcao;
    private Protocolo protocolo;
    private ProStatus proStatus;
    private String comoPesquisa;
    private String descPesquisa;
    private String msgConfirma;
    private String msgAlerta;
    private List<SelectItem> listaPrioridadeCombo;
    private List<Prioridade> listaPrioridade;
    private int idPrioridade;
    private List<SelectItem> listaRotinaCombo;
    private List<Rotina> listaRotina;
    private int idRotina;
    private List<SelectItem> listaUsuarioCombo;
    private List<Usuario> listaUsuario;
    private int idUsuario;
    private List<SelectItem> listaModuloCombo;
    private List<Interrupcao> listaInterrupcao;
    private List<Modulo> listaModulo;
    private int idModulo;
    private List<SelectItem> listaProStatusCombo;
    private List<ProStatus> listaProStatus;
    private int idProStatus;
    private Pessoa pessoa;
    private Usuario usuario;
    private boolean renderJuridicaPesquisa = false;
    private String dataHoje = DataHoje.data();
    private boolean reInderiza = false;
    private String novaTarefa;
    private boolean finalizar = false;
    private String porPesquisa = "todos";
    private List<DataObject> listaTarefas = new ArrayList();

    public AgendaTarefaJSFBean() {
        ordemServico = new OrdemServico();
        interrupcao = new Interrupcao();
        protocolo = new Protocolo();
        proStatus = new ProStatus();
        comoPesquisa = "";
        descPesquisa = "";
        idPrioridade = 0;
        listaPrioridadeCombo = new ArrayList<SelectItem>();
        listaPrioridade = new ArrayList<Prioridade>();
        idRotina = 0;
        listaRotinaCombo = new ArrayList<SelectItem>();
        listaRotina = new ArrayList<Rotina>();
        idUsuario = 0;
        listaUsuarioCombo = new ArrayList<SelectItem>();
        listaUsuario = new ArrayList<Usuario>();
        idModulo = 0;
        listaProStatusCombo = new ArrayList<SelectItem>();
        listaProStatus = new ArrayList<ProStatus>();
        idProStatus = 0;
        listaModuloCombo = new ArrayList<SelectItem>();
        listaModulo = new ArrayList<Modulo>();
        pessoa = new Pessoa();
        usuario = new Usuario();
        listaInterrupcao = new ArrayList();
    }

    public String novo() {
        setOrdemServico(new OrdemServico());
        setInterrupcao(new Interrupcao());
        setProtocolo(new Protocolo());
        idPrioridade = 0;
        listaPrioridadeCombo = new ArrayList<SelectItem>();
        listaPrioridade = new ArrayList<Prioridade>();
        setIdProStatus(0);
        setListaProStatusCombo(new ArrayList<SelectItem>());
        setListaProStatus(new ArrayList<ProStatus>());
        setListaRotinaCombo(new ArrayList<SelectItem>());
        setListaRotina(new ArrayList<Rotina>());
        setIdRotina(0);
        setListaModuloCombo(new ArrayList<SelectItem>());
        setListaModulo(new ArrayList<Modulo>());
        setIdModulo(0);
        setListaUsuarioCombo(new ArrayList<SelectItem>());
        setListaUsuario(new ArrayList<Usuario>());
        setIdUsuario(0);
        setPessoa(new Pessoa());
        setUsuario(new Usuario());
        listaInterrupcao = new ArrayList();
        refreshForm();
        listaTarefas.clear();
        return "agendaTarefa";
    }

    public String novoRegistro() {
        if (!isReInderiza()) {
            reInderiza = true;
        } else {
            reInderiza = false;
        }
        return "agendaTarefa";
    }

    public void pesquisar() {
        listaTarefas.clear();
    }

    public List getListaInterrupcao() {
        InterrupcaoDB interrupcaoDB = new InterrupcaoDBToplink();
        if (listaInterrupcao.isEmpty()) {
            listaInterrupcao = interrupcaoDB.listaInterrupcao(ordemServico.getId());
        }
        return listaInterrupcao;
    }

    public List<SelectItem> getListaModuloCombo() {
        int i = 0;
//        ModuloDB db = new ModuloDBToplink();
//        if (listaModuloCombo.isEmpty()){
//            setListaModulo((List<Modulo>) db.pesquisaTodosOrdenado());
//            while (i < getListaModulo().size()){
//                listaModuloCombo.add(new SelectItem(
//                       new Integer(i),
//                       getListaModulo().get(i).getModulo()
//                ));
//                i++;
//            }
//        }
        return listaModuloCombo;
    }

    public List<SelectItem> getListaPrioridadeCombo() {
        int i = 0;
        PrioridadeDB db = new PrioridadeDBToplink();
        if (listaPrioridadeCombo.isEmpty()) {
            listaPrioridade = db.pesquisaTodos();
            while (i < getListaPrioridade().size()) {
                listaPrioridadeCombo.add(new SelectItem(
                        new Integer(i),
                        getListaPrioridade().get(i).getDescricao()));
                i++;
            }
        }
        return listaPrioridadeCombo;
    }

    public List<SelectItem> getListaRotinaCombo() {
        int i = 0;
        RotinaDB db = new RotinaDBToplink();
        if (listaRotinaCombo.isEmpty()) {
            listaRotina = db.pesquisaTodosOrdenado();
            while (i < getListaRotina().size()) {
                listaRotinaCombo.add(new SelectItem(
                        new Integer(i),
                        getListaRotina().get(i).getRotina()));
                i++;
            }
        }
        return listaRotinaCombo;
    }

    public List<SelectItem> getListaUsuarioCombo() {
        if (listaUsuarioCombo.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            int i = 0;
            listaUsuario = salvarAcumuladoDB.listaObjeto("Usuario", true);
            while (i < getListaUsuario().size()) {
                listaUsuarioCombo.add(new SelectItem(
                        new Integer(i),
                        getListaUsuario().get(i).getPessoa().getNome()));
                i++;
            }
        }
        return listaUsuarioCombo;
    }

    public List<SelectItem> getListaProStatusCombo() {
        int i = 0;
        ProStatusDB db = new ProStatusDBToplink();
        if (listaProStatusCombo.isEmpty()) {
            setListaProStatus((List<ProStatus>) db.pesquisaTodos());
            while (i < getListaProStatus().size()) {
                listaProStatusCombo.add(new SelectItem(
                        new Integer(i),
                        getListaProStatus().get(i).getDescricao()));
                i++;
            }
        }
        return listaProStatusCombo;
    }

    public String salvar() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        if (protocolo.getId() == -1) {
            if (validaProtocolo()) {
                dbSalvar.abrirTransacao();
                OrdemServicoDB ordemServicoDB = new OrdemServicoDBToplink();
                if (ordemServicoDB.idOrdemServico(ordemServico) == null) {
                    if (dbSalvar.inserirObjeto(protocolo)) {
                        if (validaOrdemServico()) {
                            if (dbSalvar.inserirObjeto(ordemServico)) {
                                dbSalvar.comitarTransacao();
                                setMsgConfirma("Cadastro efetuado com sucesso!");
                            }
                        }
                    } else {
                        dbSalvar.desfazerTransacao();
                        setMsgConfirma("Erro! Cadastro não foi efetuado.");
                    }
                } else {
                    dbSalvar.desfazerTransacao();
                    setMsgConfirma("Já existe uma O.S. com esse nome.");
                }
            }
        } else {
            if (validaProtocolo()) {
                dbSalvar.abrirTransacao();
                if (dbSalvar.alterarObjeto(protocolo)) {
                    if (validaOrdemServico()) {
                        if (dbSalvar.alterarObjeto(ordemServico)) {
                            dbSalvar.comitarTransacao();
                            setMsgConfirma("Cadastro atualizado com sucesso!");
                        } else {
                            dbSalvar.desfazerTransacao();
                            setMsgConfirma("Erro! Cadastro não foi efetuado.");
                        }
                    } else {
                        dbSalvar.desfazerTransacao();
                        setMsgConfirma("Erro! Cadastro não foi efetuado.");
                    }
                }
            } else {
                dbSalvar.desfazerTransacao();
            }
        }
        ordemServico = new OrdemServico();
        protocolo = new Protocolo();
        pessoa = new Pessoa();
        reInderiza = false;
        novo();
        refreshForm();
        return "agendaTarefa";
    }

    public String excluir() {
        if (ordemServico.getId() != -1) {
            SalvarAcumuladoDB dbExcluir = new SalvarAcumuladoDBToplink();
            try {
                dbExcluir.abrirTransacao();
                if (dbExcluir.deletarObjeto((OrdemServico) dbExcluir.pesquisaCodigo(ordemServico.getId(), "OrdemServico"))) {
                    if (dbExcluir.deletarObjeto((Protocolo) dbExcluir.pesquisaCodigo(protocolo.getId(), "Protocolo"))) {
                        dbExcluir.comitarTransacao();
                        setMsgConfirma("Exclusão realizada com sucesso atualizado com sucesso!");
                    } else {
                        dbExcluir.desfazerTransacao();
                        setMsgConfirma("Erro! Falha ao excluir o registro.");
                    }
                } else {
                    dbExcluir.desfazerTransacao();
                    setMsgConfirma("Erro! Não foi possível realiza a exclusão");
                }
            } catch (Exception e) {
                msgConfirma = e.getMessage();
            }
        } else {
            msgConfirma = "Registro inválido!";
        }
        ordemServico = new OrdemServico();
        protocolo = new Protocolo();
        pessoa = new Pessoa();
        reInderiza = false;
        novo();
        refreshForm();
        return "agendaTarefa";
    }

    public String editar() {
//        ordemServico = ((OrdemServico)((DataObject) getHtmlTable().getRowData()).getArgumento1());
        protocolo = getOrdemServico().getProtocolo();
        pessoa = getOrdemServico().getProtocolo().getEmpresa();
        usuario = getOrdemServico().getResponsavel();
        setProStatus(getOrdemServico().getProStatus());
        for (int j = 0; j < getListaModuloCombo().size(); j++) {
            if (ordemServico.getModulo().getId() == getListaModulo().get(j).getId()) {
                setIdModulo(j);
            }
        }
        for (int j = 0; j < getListaPrioridadeCombo().size(); j++) {
            if (ordemServico.getPrioridade().getId() == listaPrioridade.get(j).getId()) {
                idPrioridade = j;
            }
        }
        for (int j = 0; j < getListaRotinaCombo().size(); j++) {
            if (ordemServico.getRotina().getId() == listaRotina.get(j).getId()) {
                idRotina = j;
            }
        }
        for (int j = 0; j < getListaUsuarioCombo().size(); j++) {
            if (usuario.getId() == listaUsuario.get(j).getId()) {
                idUsuario = j;
            }
        }
        for (int j = 0; j < getListaProStatusCombo().size(); j++) {
            if (ordemServico.getProStatus().getId() == getListaProStatus().get(j).getId()) {
                setIdProStatus(j);
            }
        }
        reInderiza = true;
        return "agendaTarefa";
    }

    public void linhaInt() {
//        ordemServico = ((OrdemServico) ((DataObject)getHtmlTable().getRowData()).getArgumento1()) ;
        listaInterrupcao.clear();
        interrupcao = new Interrupcao();
    }

    public void linhaInterrupcao() {
//        interrupcao = (Interrupcao) getHtmlTableInterrupcao().getRowData();
        listaInterrupcao.clear();
    }

    public String salvarInterrupcao() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        if (interrupcao.getId() == -1) {
            if (validaInterrupcao()) {
                dbSalvar.abrirTransacao();
                InterrupcaoDB interrupcaoDB = new InterrupcaoDBToplink();
                if (interrupcaoDB.idInterrupcao(interrupcao) == null) {
                    if (dbSalvar.inserirObjeto(interrupcao)) {
                        dbSalvar.comitarTransacao();
                        setMsgConfirma("Cadastro efetuado com sucesso!");
                    } else {
                        dbSalvar.desfazerTransacao();
                        setMsgConfirma("Erro! Cadastro não foi efetuado.");
                    }
                } else {
                    dbSalvar.desfazerTransacao();
                    setMsgConfirma("Já existe uma O.S. com esse nome.");
                }
            } else {
                dbSalvar.desfazerTransacao();
                setMsgConfirma("Já existe uma O.S. com esse nome.");
            }
        } else {
            if (validaInterrupcao()) {
                dbSalvar.abrirTransacao();
                if (dbSalvar.alterarObjeto(interrupcao)) {
                    setMsgConfirma("Cadastro atualizado com sucesso!");
                    dbSalvar.comitarTransacao();
                } else {
                    dbSalvar.desfazerTransacao();
                    setMsgConfirma("Erro! Cadastro não foi efetuado.");
                }
            } else {
                dbSalvar.desfazerTransacao();
                setMsgConfirma("Erro! Cadastro não foi efetuado.");
            }
        }
        interrupcao = new Interrupcao();
        listaInterrupcao.clear();
        listaInterrupcao = new ArrayList();
        refreshForm();
        return null;
    }

    public String excluirInterrupcao() {
        if (interrupcao.getId() != -1) {
            SalvarAcumuladoDB dbExcluir = new SalvarAcumuladoDBToplink();
            try {
                dbExcluir.abrirTransacao();
                if (dbExcluir.deletarObjeto((Interrupcao) dbExcluir.pesquisaCodigo(interrupcao.getId(), "Interrupcao"))) {
                    dbExcluir.comitarTransacao();
                    setMsgConfirma("Exclusão realizada com sucesso atualizado com sucesso!");
                } else {
                    dbExcluir.desfazerTransacao();
                    setMsgConfirma("Erro! Falha ao excluir o registro.");
                }
            } catch (Exception e) {
                msgConfirma = e.getMessage();
            }
        } else {
            msgConfirma = "Registro inválido!";
        }
        interrupcao = new Interrupcao();
        listaInterrupcao.clear();
        refreshForm();
        return null;
    }

    public String editarInterrupcao() {
//        interrupcao = (Interrupcao) getHtmlTableInterrupcao().getRowData();

        return null;
    }

    public void acaoPesquisaInicial() {
        setComoPesquisa("I");
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("P");
    }

    public List getListaOrdemServico() {
//        Pesquisa pesquisa = new Pesquisa();
        List result = null;
//        result = pesquisa.pesquisar("OrdemServico", "descricao" , getDescPesquisa(), "descricao", getComoPesquisa());
        return result;
    }

    public void refreshForm() {
    }

    public boolean validaInterrupcao() {
        interrupcao.setOrdemServico(ordemServico);
        if (interrupcao.getMotivo().isEmpty()) {
            setMsgConfirma("Digite a situção!");
            return false;
        }
        return true;
    }

    public boolean validaOrdemServico() {
        ordemServico.setProtocolo(protocolo);
        ordemServico.setModulo(getListaModulo().get(idModulo));
        ordemServico.setRotina(getListaRotina().get(idRotina));
        ordemServico.setPrioridade(getListaPrioridade().get(idPrioridade));
        ordemServico.setResponsavel(getListaUsuario().get(idUsuario));
        if (ordemServico.getHistorico().isEmpty()) {
            setMsgConfirma("Digite o a situação do histórico atual!");
            return false;
        } else if (ordemServico.getHistoricoInterno().isEmpty()) {
            setMsgConfirma("Digite o a situação do histórico interno atual!");
            return false;
        } else if (DataHoje.converteDataParaInteger(ordemServico.getDataPrevisaoString())
                < DataHoje.converteDataParaInteger(DataHoje.data())) {
            if (protocolo.getId() == -1) {
                setMsgConfirma("A data deve ser posterior a data Atual!");
                return false;
            }
        } else if (ordemServico.getModulo().getId() == -1) {
            setMsgConfirma("O modulo esta vazio!");
            return false;
        } else if (ordemServico.getRotina().getId() == -1) {
            setMsgConfirma("A rotina esta vazia!");
            return false;
        } else if (ordemServico.getPrioridade().getId() == -1) {
            setMsgConfirma("A prioridade esta vazia!");
            return false;
        }
        if (finalizar && ordemServico.getDataConclusao() == null || getListaProStatus().get(idProStatus).getId() == 3) {
            ordemServico.setDataConclusao(DataHoje.dataHoje());
            ordemServico.setProStatus(getListaProStatus().get(1));
        } else if (!finalizar && (ordemServico.getProStatus().getId() == 3)) {
            ordemServico.setProStatus(getListaProStatus().get(0));
            ordemServico.setDataConclusao(null);
        } else {
            ordemServico.setProStatus(getListaProStatus().get(getIdProStatus()));
            ordemServico.setDataConclusao(null);
        }
        return true;
    }

    public boolean validaProtocolo() {
        protocolo.setEmpresa(pessoa);
        if (protocolo.getSolicitante().isEmpty()) {
            setMsgConfirma("Digite o nome do solicitante!");
            return false;
        } else if (protocolo.getEmpresa().getId() == -1) {
            setMsgConfirma("Digite o nome da Empresa!");
            return false;
        } else if (protocolo.getDataString().isEmpty()) {
            setMsgConfirma("Digite uma data!");
            return false;
        } else if (protocolo.getHora().isEmpty()) {
            setMsgConfirma("Digite um horário!");
            return false;
        }
        return true;
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public List<Prioridade> getListaPrioridade() {
        return listaPrioridade;
    }

    public void setListaPrioridade(List<Prioridade> listaPrioridade) {
        this.listaPrioridade = listaPrioridade;
    }

    public int getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(int idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public boolean isRenderJuridicaPesquisa() {
        return renderJuridicaPesquisa;
    }

    public void setRenderJuridicaPesquisa(boolean renderJuridicaPesquisa) {
        this.renderJuridicaPesquisa = renderJuridicaPesquisa;
    }

    public void setListaRotinaCombo(List<SelectItem> listaRotinaCombo) {
        this.listaRotinaCombo = listaRotinaCombo;
    }

    public List<Rotina> getListaRotina() {
        return listaRotina;
    }

    public void setListaRotina(List<Rotina> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public void setListaUsuarioCombo(List<SelectItem> listaUsuarioCombo) {
        this.listaUsuarioCombo = listaUsuarioCombo;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setListaModuloCombo(List<SelectItem> listaModuloCombo) {
        this.listaModuloCombo = listaModuloCombo;
    }

    public List<Modulo> getListaModulo() {
        return listaModulo;
    }

    public void setListaModulo(List<Modulo> listaModulo) {
        this.listaModulo = listaModulo;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public boolean isReInderiza() {
        return reInderiza;
    }

    public void setReInderiza(boolean reInderiza) {
        this.reInderiza = reInderiza;
    }

    public String getNovaTarefa() {
        return novaTarefa;
    }

    public void setNovaTarefa(String novaTarefa) {
        this.novaTarefa = novaTarefa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Interrupcao getInterrupcao() {
        return interrupcao;
    }

    public void setInterrupcao(Interrupcao interrupcao) {
        this.interrupcao = interrupcao;
    }

    public boolean isFinalizar() {
        if (!ordemServico.getDataConclusaoString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
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

    public List<DataObject> getListaTarefas() {
        if (listaTarefas.isEmpty()) {
            OrdemServicoDB ordemServicoDB = new OrdemServicoDBToplink();
            List<OrdemServico> lista = ordemServicoDB.pesquisaTodos(porPesquisa);
            for (int i = 0; i < lista.size(); i++) {
                listaTarefas.add(new DataObject(null, lista.get(i), false, "null.png", "", null));
                if (lista.get(i).getDataConclusao() == null) {
                    if ((DataHoje.converteDataParaInteger(DataHoje.data()) == DataHoje.converteDataParaInteger(lista.get(i).getDataPrevisaoString())) || (DataHoje.dataHoje() == lista.get(i).getDataPrevisao()) && lista.get(i).getProStatus().getId() != 4) {
                        listaTarefas.get(i).setArgumento2(true);
                        listaTarefas.get(i).setArgumento3("Prazo Expira Hoje!");
                        listaTarefas.get(i).setArgumento4("alertGreen.png");
                    } else if (DataHoje.converteDataParaInteger(DataHoje.data()) > DataHoje.converteDataParaInteger(lista.get(i).getDataPrevisaoString()) && lista.get(i).getProStatus().getId() != 4) {
                        listaTarefas.get(i).setArgumento2(true);
                        listaTarefas.get(i).setArgumento3("Prazo Expirado!");
                        listaTarefas.get(i).setArgumento4("alertOrange.png");
                    } else if (lista.get(i).getProStatus().getId() == 4) {
                        listaTarefas.get(i).setArgumento2(true);
                        listaTarefas.get(i).setArgumento3("OS esta parada, verificar informações nas Interrupções!");
                        listaTarefas.get(i).setArgumento4("alertPause.png");
                    }
                } else if (lista.get(i).getDataConclusao() != null) {
                    if (DataHoje.converteDataParaInteger(lista.get(i).getDataConclusaoString()) < DataHoje.converteDataParaInteger(lista.get(i).getDataPrevisaoString())) {
                        listaTarefas.get(i).setArgumento2(true);
                        listaTarefas.get(i).setArgumento3("Você foi Ninja!");
                        listaTarefas.get(i).setArgumento4("alertNinja.png");
                    } else {
                        listaTarefas.get(i).setArgumento2(true);
                        listaTarefas.get(i).setArgumento3("Concluído!");
                        listaTarefas.get(i).setArgumento4("concluido.png");
                    }
                }
            }
        }
        return listaTarefas;
    }

    public void setListaTarefas(List<DataObject> listaTarefas) {
        this.listaTarefas = listaTarefas;
    }

    public ProStatus getProStatus() {
        return proStatus;
    }

    public void setProStatus(ProStatus proStatus) {
        this.proStatus = proStatus;
    }

    public void setListaProStatusCombo(List<SelectItem> listaProStatusCombo) {
        this.listaProStatusCombo = listaProStatusCombo;
    }

    public List<ProStatus> getListaProStatus() {
        return listaProStatus;
    }

    public void setListaProStatus(List<ProStatus> listaProStatus) {
        this.listaProStatus = listaProStatus;
    }

    public int getIdProStatus() {
        return idProStatus;
    }

    public void setIdProStatus(int idProStatus) {
        this.idProStatus = idProStatus;
    }
}