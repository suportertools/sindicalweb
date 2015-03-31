package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.financeiro.lista.ListServicosSubGrupoFinanceiro;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class SubGrupoConvenioBean implements Serializable {

    // Tela cadastro
    private List<SelectItem> listGrupoConvenio;
    private List<SelectItem> listSubGrupoConvenio;
    private List<SelectItem> listGrupoFinanceiro;
    private List<SelectItem> listSubGrupoFinanceiro;
    private Integer idGrupoConvenio;
    private Integer idGrupoConvenioFiltro;
    private Integer idSubGrupoConvenio;
    private Integer idGrupoFinanceiro;
    private Integer idSubGrupoFinanceiro;
    private String message;
    private ListServicosSubGrupoFinanceiro[] servicoSelecionado;
    private ConvenioServico[] convenioServicoSelecionado;
    private SubGrupoConvenio subGrupoConvenio;
    private List<ListServicosSubGrupoFinanceiro> listServicosDisponiveis;
    private List<ConvenioServico> listServicosAdicionados;
    private Boolean enableGroup;
    private Boolean enableSubGroup;

    // Tela pesquisa
    private List<SubGrupoConvenio> listSubGrupoConvenios;

    @PostConstruct
    public void init() {
        // Tela cadastro
        listGrupoConvenio = new ArrayList<>();
        listSubGrupoConvenio = new ArrayList<>();
        listGrupoFinanceiro = new ArrayList<>();
        listSubGrupoFinanceiro = new ArrayList<>();
        idGrupoConvenio = 0;
        idGrupoConvenioFiltro = 0;
        idSubGrupoConvenio = 0;
        idGrupoFinanceiro = 0;
        idSubGrupoFinanceiro = 0;
        message = "";
        subGrupoConvenio = new SubGrupoConvenio();
        listServicosDisponiveis = new ArrayList<>();
        listServicosAdicionados = new ArrayList<>();

        // Tela pesquisa
        listSubGrupoConvenios = new ArrayList<>();
        enableGroup = false;
        enableSubGroup = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("subGrupoConvenioBean");
        GenericaSessao.remove("subGrupoConvenioPesquisa");
    }

    public String novo() {
        idGrupoConvenio = 0;
        subGrupoConvenio = new SubGrupoConvenio();
        message = "";
        return null;
    }

    public void clear(Integer tcase) {
        if (tcase == 0) {
            clear(1);
            listGrupoConvenio.clear();
            idGrupoFinanceiro = null;
        }
        if (tcase == 1) {
            listServicosDisponiveis.clear();
            servicoSelecionado = null;
            listServicosAdicionados.clear();
        }
        if (tcase == 2) {
            listServicosDisponiveis.clear();
            servicoSelecionado = null;
            listServicosAdicionados.clear();
            listSubGrupoFinanceiro.clear();
        }
        if (tcase == 3) {
            idSubGrupoConvenio = 0;
            listSubGrupoConvenio.clear();
            listServicosDisponiveis.clear();
            servicoSelecionado = null;
        }
    }

    public void add() {
        if (listGrupoConvenio.isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar o grupo convênio!");
            return;
        }
        if (subGrupoConvenio.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar a descrição do subgrupo convênio!");
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        SubGrupoConvenioDB sgcdb = new SubGrupoConvenioDBToplink();
        subGrupoConvenio.setGrupoConvenio((GrupoConvenio) di.find(new GrupoConvenio(), Integer.parseInt(listGrupoConvenio.get(idGrupoConvenio).getDescription())));
        if (subGrupoConvenio.getId() == -1) {
            if (sgcdb.existeSubGrupoConvenio(subGrupoConvenio)) {
                GenericaMensagem.info("Validação", "SubGrupo já existe!");
                return;
            }
            di.openTransaction();
            if (di.save(subGrupoConvenio)) {
                novoLog.save(
                        "ID: " + subGrupoConvenio.getId()
                        + " - Grupo Convenio: (" + subGrupoConvenio.getGrupoConvenio().getId() + ") - " + subGrupoConvenio.getGrupoConvenio().getDescricao()
                        + " - Descrição: " + subGrupoConvenio.getDescricao()
                );
                di.commit();
                listSubGrupoConvenio.clear();
                clear(1);
                GenericaMensagem.info("Sucesso", "Cadastro realizado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao inserir este registro!");
            }
        } else {
            di.openTransaction();
            SubGrupoConvenio sgc = (SubGrupoConvenio) di.find(subGrupoConvenio);
            String beforeUpdate
                    = "ID: " + sgc.getId()
                    + " - Grupo Convenio: (" + sgc.getGrupoConvenio().getId() + ") - " + sgc.getGrupoConvenio().getDescricao()
                    + " - Descrição: " + sgc.getDescricao();
            if (di.update(subGrupoConvenio)) {
                novoLog.update(beforeUpdate,
                        "ID: " + subGrupoConvenio.getId()
                        + " - Grupo Convenio: (" + subGrupoConvenio.getGrupoConvenio().getId() + ") - " + subGrupoConvenio.getGrupoConvenio().getDescricao()
                        + " - Descrição: " + subGrupoConvenio.getDescricao()
                );
                di.commit();
                listSubGrupoConvenio.clear();
                clear(1);
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao atualizar este registro!");
            }
        }
        for (int i = 0; i < listGrupoConvenio.size(); i++) {
            if (subGrupoConvenio.getGrupoConvenio().getId() == Integer.parseInt(listGrupoConvenio.get(i).getDescription())) {
                idGrupoConvenioFiltro = i;
                break;
            }
        }
    }

    public void remove() {
        DaoInterface di = new Dao();
        SubGrupoConvenio sgc = (SubGrupoConvenio) di.find(new SubGrupoConvenio(), Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
        remove(sgc);
    }

    public void remove(SubGrupoConvenio sgc) {
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        if (sgc.getId() != -1) {
            if (di.delete(sgc, true)) {
                novoLog.delete(
                        "ID: " + sgc.getId()
                        + " - Grupo Convenio: (" + sgc.getGrupoConvenio().getId() + ") - " + sgc.getGrupoConvenio().getDescricao()
                        + " - Descrição: " + sgc.getDescricao()
                );
                listSubGrupoConvenio.clear();
                idSubGrupoConvenio = 0;
                subGrupoConvenio = new SubGrupoConvenio();
                clear(1);
                GenericaMensagem.info("Sucesso", "Registro removido");
            } else {
                GenericaMensagem.warn("Erro", "SubGrupo convênio não pode ser excluido!");
            }
        }
    }

    public void edit() {
        DaoInterface di = new Dao();
        subGrupoConvenio = (SubGrupoConvenio) di.find(new SubGrupoConvenio(), Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
        for (int i = 0; i < listGrupoConvenio.size(); i++) {
            if (subGrupoConvenio.getGrupoConvenio().getId() == Integer.parseInt(listGrupoConvenio.get(i).getDescription())) {
                idGrupoConvenio = i;
                break;
            }
        }
    }

    public String edit(SubGrupoConvenio sgc) {
        String url = null;
        if (GenericaSessao.exists("urlRetorno")) {
            GenericaSessao.put("linkClicado", true);
            GenericaSessao.put("subGrupoConvenioPesquisa", sgc);
            url = GenericaSessao.getString("urlRetorno");
        }
        return url;
    }

    public List<ListServicosSubGrupoFinanceiro> getListServicosDisponiveis() {
        if (listServicosDisponiveis.isEmpty()) {
            if (!listSubGrupoConvenio.isEmpty()) {
                SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
                if (enableGroup) {
                    if (enableSubGroup) {
                        List<Servicos> list = (List<Servicos>) subGrupoConvenioDB.listaServicosDisponiveisPorGrupoFinanceiro(Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()), Integer.parseInt(getListGrupoFinanceiro().get(idGrupoFinanceiro).getDescription()));
                        ListServicosSubGrupoFinanceiro lssgf;
                        Integer idMemory = 0;
                        for (int i = 0; i < list.size(); i++) {
                            if (idMemory != ((Servicos) list.get(i)).getSubGrupoFinanceiro().getId() || idMemory == 0) {
                                lssgf = new ListServicosSubGrupoFinanceiro();
                                lssgf.setDescricao(((Servicos) list.get(i)).getSubGrupoFinanceiro().getDescricao());
                                lssgf.setSubGrupoFinanceiro(((Servicos) list.get(i)).getSubGrupoFinanceiro());
                                lssgf.setServicos(((Servicos) list.get(i)));
                                lssgf.setListServicos(subGrupoConvenioDB.listaServicosDisponiveisPorSubGrupoFinanceiro(Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()), ((Servicos) list.get(i)).getSubGrupoFinanceiro().getId()));
                                listServicosDisponiveis.add(lssgf);
                                idMemory = ((Servicos) list.get(i)).getSubGrupoFinanceiro().getId();
                            }
                        }
                    } else {
                        if (!listSubGrupoFinanceiro.isEmpty()) {
                            List<Servicos> list = (List<Servicos>) subGrupoConvenioDB.listaServicosDisponiveisPorSubGrupoFinanceiro(Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()), Integer.parseInt(getListSubGrupoFinanceiro().get(idSubGrupoFinanceiro).getDescription()));
                            ListServicosSubGrupoFinanceiro lssgf;
                            for (int i = 0; i < list.size(); i++) {
                                lssgf = new ListServicosSubGrupoFinanceiro();
                                lssgf.setDescricao(((Servicos) list.get(i)).getDescricao());
                                lssgf.setSubGrupoFinanceiro(((Servicos) list.get(i)).getSubGrupoFinanceiro());
                                lssgf.setServicos(((Servicos) list.get(i)));
                                listServicosDisponiveis.add(lssgf);
                            }
                        }
                    }
                } else {
                    List list = subGrupoConvenioDB.listaServicosDisponiveis(Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
                    ListServicosSubGrupoFinanceiro lssgf;
                    for (int i = 0; i < list.size(); i++) {
                        lssgf = new ListServicosSubGrupoFinanceiro();
                        lssgf.setServicos(((Servicos) list.get(i)));
                        lssgf.setSubGrupoFinanceiro(((Servicos) list.get(i)).getSubGrupoFinanceiro());
                        lssgf.setDescricao(((Servicos) list.get(i)).getDescricao());
                        listServicosDisponiveis.add(lssgf);
                    }
                }
            }
        }
        return listServicosDisponiveis;
    }

    public List<ConvenioServico> getListServicosAdicionados() {
        listServicosAdicionados.clear();
        if (!listSubGrupoConvenio.isEmpty()) {
            SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
            listServicosAdicionados = subGrupoConvenioDB.listaServicosAdicionados(Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
        }
        return listServicosAdicionados;
    }

    public void addConvenioServico() {
        if (servicoSelecionado.length == 0) {
            GenericaMensagem.warn("Validação", "Selecionar pelo menos um registro");
            return;
        }
        boolean sucesso = false;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (enableGroup) {
            ServicosDB servicosDB = new ServicosDBToplink();
            for (ListServicosSubGrupoFinanceiro lssgf : servicoSelecionado) {
                List<Servicos> list = servicosDB.listaServicosPorSubGrupoFinanceiro(lssgf.getSubGrupoFinanceiro().getId());
                for (Servicos s : list) {
                    ConvenioServico convenioServico = new ConvenioServico();
                    convenioServico.setServicos(s);
                    convenioServico.setSubGrupoConvenio((SubGrupoConvenio) di.find(new SubGrupoConvenio(), Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription())));
                    if (di.save(convenioServico, true)) {
                        novoLog.save(
                                "Convênio Serviço - ID: " + convenioServico.getId()
                                + " - Sub Grupo Convenio: (" + convenioServico.getSubGrupoConvenio().getId() + ") - " + convenioServico.getSubGrupoConvenio().getDescricao()
                                + " - Serviço: (" + convenioServico.getServicos().getId() + ") - " + convenioServico.getServicos().getDescricao()
                                + " - Encaminhamento: " + convenioServico.isEncaminhamento()
                        );
                        sucesso = true;
                    }
                }
            }
        } else {
            for (int i = 0; i < listServicosDisponiveis.size(); i++) {
                for (ListServicosSubGrupoFinanceiro lssgf : servicoSelecionado) {
                    if (lssgf.getServicos().getId() == listServicosDisponiveis.get(i).getServicos().getId()) {
                        ConvenioServico convenioServico = new ConvenioServico();
                        convenioServico.setServicos(listServicosDisponiveis.get(i).getServicos());
                        convenioServico.setSubGrupoConvenio((SubGrupoConvenio) di.find(new SubGrupoConvenio(), Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription())));
                        if (di.save(convenioServico, true)) {
                            novoLog.save(
                                    "Convênio Serviço - ID: " + convenioServico.getId()
                                    + " - Sub Grupo Convenio: (" + convenioServico.getSubGrupoConvenio().getId() + ") - " + convenioServico.getSubGrupoConvenio().getDescricao()
                                    + " - Serviço: (" + convenioServico.getServicos().getId() + ") - " + convenioServico.getServicos().getDescricao()
                                    + " - Encaminhamento: " + convenioServico.isEncaminhamento()
                            );
                            sucesso = true;
                        }
                    }
                }
            }
        }
        if (sucesso) {
            clear(1);
            GenericaMensagem.info("Sucesso", "Registro(s) adicionado(s)");
        }
    }

    public void removeConvenioServico() {
        if (convenioServicoSelecionado.length == 0) {
            GenericaMensagem.warn("Validação", "selecionar pelo menos um registro");
            return;
        }
        boolean sucesso = false;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        for (int i = 0; i < listServicosAdicionados.size(); i++) {
            for (ConvenioServico convenioServico : convenioServicoSelecionado) {
                if (convenioServico.getId() == listServicosAdicionados.get(i).getId()) {
                    if (di.delete(listServicosAdicionados.get(i), true)) {
                        novoLog.delete(
                                "Convênio Serviço - ID: " + convenioServico.getId()
                                + " - Sub Grupo Convenio: (" + convenioServico.getSubGrupoConvenio().getId() + ") - " + convenioServico.getSubGrupoConvenio().getDescricao()
                                + " - Serviço: (" + convenioServico.getServicos().getId() + ") - " + convenioServico.getServicos().getDescricao()
                                + " - Encaminhamento: " + convenioServico.isEncaminhamento()
                        );
                        sucesso = true;
                    }
                }
            }
        }
        if (sucesso) {
            clear(1);
            GenericaMensagem.info("Sucesso", "Registro(s) removido(s)");
        }
    }

    public void updateConvenioServico(ConvenioServico convenioServico) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (convenioServico.isEncaminhamento()) {
            convenioServico.setEncaminhamento(false);
        } else {
            convenioServico.setEncaminhamento(true);
        }
        ConvenioServico cs = (ConvenioServico) di.find(convenioServico);
        String beforeUpdate
                = "Convênio Serviço - ID: " + cs.getId()
                + " - Sub Grupo Convenio: (" + cs.getSubGrupoConvenio().getId() + ") - " + cs.getSubGrupoConvenio().getDescricao()
                + " - Serviço: (" + cs.getServicos().getId() + ") - " + cs.getServicos().getDescricao()
                + " - Encaminhamento: " + cs.isEncaminhamento();
        if (di.update(convenioServico, true)) {
            novoLog.update(beforeUpdate,
                    "Convênio Serviço - ID: " + convenioServico.getId()
                    + " - Sub Grupo Convenio: (" + convenioServico.getSubGrupoConvenio().getId() + ") - " + convenioServico.getSubGrupoConvenio().getDescricao()
                    + " - Serviço: (" + convenioServico.getServicos().getId() + ") - " + convenioServico.getServicos().getDescricao()
                    + " - Encaminhamento: " + convenioServico.isEncaminhamento()
            );
            GenericaMensagem.info("Sucesso", "Registro(s) atualizado(s)");
        } else {
            GenericaMensagem.warn("Erro", "Ao atualizar este registro");
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SubGrupoConvenio getSubGrupoConvenio() {
        return subGrupoConvenio;
    }

    public List<SelectItem> getListSubGrupoConvenio() {
        if (listSubGrupoConvenio.isEmpty() && !listGrupoConvenio.isEmpty()) {
            SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
            List<SubGrupoConvenio> list = subGrupoConvenioDB.listaSubGrupoConvenioPorGrupo(Integer.parseInt(listGrupoConvenio.get(idGrupoConvenioFiltro).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listSubGrupoConvenio.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listSubGrupoConvenio;
    }

    public void setSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        this.subGrupoConvenio = subGrupoConvenio;
    }

    public List<SelectItem> getListGrupoConvenio() {
        if (listGrupoConvenio.isEmpty()) {
            DaoInterface di = new Dao();
            List<GrupoConvenio> list = (List<GrupoConvenio>) di.list(new GrupoConvenio(), true);
            for (int i = 0; i < list.size(); i++) {
                listGrupoConvenio.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listGrupoConvenio;
    }

    public void setListGrupoConvenio(List<SelectItem> listGrupoConvenio) {
        this.listGrupoConvenio = listGrupoConvenio;
    }

    public Integer getIdGrupoConvenio() {
        return idGrupoConvenio;
    }

    public void setIdGrupoConvenio(Integer idGrupoConvenio) {
        this.idGrupoConvenio = idGrupoConvenio;
    }

    public Integer getIdSubGrupoConvenio() {
        return idSubGrupoConvenio;
    }

    public void setIdSubGrupoConvenio(Integer idSubGrupoConvenio) {
        this.idSubGrupoConvenio = idSubGrupoConvenio;
    }

    public Integer getIdGrupoConvenioFiltro() {
        return idGrupoConvenioFiltro;
    }

    public void setIdGrupoConvenioFiltro(Integer idGrupoConvenioFiltro) {
        this.idGrupoConvenioFiltro = idGrupoConvenioFiltro;
    }

    public ListServicosSubGrupoFinanceiro[] getServicoSelecionado() {
        return servicoSelecionado;
    }

    public void setServicoSelecionado(ListServicosSubGrupoFinanceiro[] servicoSelecionado) {
        this.servicoSelecionado = servicoSelecionado;
    }

    public ConvenioServico[] getConvenioServicoSelecionado() {
        return convenioServicoSelecionado;
    }

    public void setConvenioServicoSelecionado(ConvenioServico[] convenioServicoSelecionado) {
        this.convenioServicoSelecionado = convenioServicoSelecionado;
    }

    public List<SubGrupoConvenio> getListSubGrupoConvenios() {
        if (listSubGrupoConvenios.isEmpty()) {
            DaoInterface di = new Dao();
            listSubGrupoConvenios = (List<SubGrupoConvenio>) di.list(new SubGrupoConvenio(), true);
        }
        return listSubGrupoConvenios;
    }

    public void setListSubGrupoConvenios(List<SubGrupoConvenio> listSubGrupoConvenios) {
        this.listSubGrupoConvenios = listSubGrupoConvenios;
    }

    public List<SelectItem> getListGrupoFinanceiro() {
        if (listGrupoFinanceiro.isEmpty()) {
            List<GrupoFinanceiro> list = new Dao().list(new GrupoFinanceiro(), true);
            if (enableGroup) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        idGrupoFinanceiro = i;
                    }
                    listGrupoFinanceiro.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                }
            }
        }
        return listGrupoFinanceiro;
    }

    public void setListGrupoFinanceiro(List<SelectItem> listGrupoFinanceiro) {
        this.listGrupoFinanceiro = listGrupoFinanceiro;
    }

    public Integer getIdGrupoFinanceiro() {
        return idGrupoFinanceiro;
    }

    public void setIdGrupoFinanceiro(Integer idGrupoFinanceiro) {
        this.idGrupoFinanceiro = idGrupoFinanceiro;
    }

    public List<SelectItem> getListSubGrupoFinanceiro() {
        if (!listGrupoFinanceiro.isEmpty()) {
            if (listSubGrupoFinanceiro.isEmpty()) {
                FinanceiroDB financeiroDB = new FinanceiroDBToplink();
                List<SubGrupoFinanceiro> list = financeiroDB.listaSubGrupo(Integer.parseInt(listGrupoFinanceiro.get(idGrupoFinanceiro).getDescription()));
                if (enableGroup) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            idSubGrupoFinanceiro = i;
                        }
                        listSubGrupoFinanceiro.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                    }
                }
            }
        }
        return listSubGrupoFinanceiro;
    }

    public void setListSubGrupoFinanceiro(List<SelectItem> listSubGrupoFinanceiro) {
        this.listSubGrupoFinanceiro = listSubGrupoFinanceiro;
    }

    public Integer getIdSubGrupoFinanceiro() {
        return idSubGrupoFinanceiro;
    }

    public void setIdSubGrupoFinanceiro(Integer idSubGrupoFinanceiro) {
        this.idSubGrupoFinanceiro = idSubGrupoFinanceiro;
    }

    public Boolean getEnableGroup() {
        return enableGroup;
    }

    public void setEnableGroup(Boolean enableGroup) {
        if (!enableGroup) {
            listGrupoFinanceiro.clear();
            enableSubGroup = false;
            listSubGrupoFinanceiro.clear();
        }
        this.enableGroup = enableGroup;
    }

    public Boolean getEnableSubGroup() {
        return enableSubGroup;
    }

    public void setEnableSubGroup(Boolean enableSubGroup) {
        if (enableSubGroup) {
            clear(2);
        }
        this.enableSubGroup = enableSubGroup;
    }
}
