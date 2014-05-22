package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.financeiro.Servicos;
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
    private List<SelectItem> listGrupoConvenio = new ArrayList<SelectItem>();
    private List<SelectItem> listSubGrupoConvenio = new ArrayList<SelectItem>();
    private int idGrupoConvenio = 0;
    private int idGrupoConvenioFiltro = 0;
    private int idSubGrupoConvenio = 0;
    private String message;
    private Servicos[] servicoSelecionado;
    private ConvenioServico[] convenioServicoSelecionado;
    private SubGrupoConvenio subGrupoConvenio = new SubGrupoConvenio();
    private List<Servicos> listServicosDisponiveis = new ArrayList<Servicos>();
    private List<ConvenioServico> listServicosAdicionados = new ArrayList<ConvenioServico>();

    // Tela pesquisa
    private List<SubGrupoConvenio> listSubGrupoConvenios = new ArrayList<SubGrupoConvenio>();

    @PostConstruct
    public void init() {
        // Tela cadastro
        listGrupoConvenio = new ArrayList<SelectItem>();
        listSubGrupoConvenio = new ArrayList<SelectItem>();
        idGrupoConvenio = 0;
        idGrupoConvenioFiltro = 0;
        idSubGrupoConvenio = 0;
        message = "";
        subGrupoConvenio = new SubGrupoConvenio();
        listServicosDisponiveis = new ArrayList<Servicos>();
        listServicosAdicionados = new ArrayList<ConvenioServico>();

        // Tela pesquisa
        listSubGrupoConvenios = new ArrayList<SubGrupoConvenio>();
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

    public List<Servicos> getListServicosDisponiveis() {
        listServicosDisponiveis.clear();
        if (!listSubGrupoConvenio.isEmpty()) {
            SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
            listServicosDisponiveis = subGrupoConvenioDB.listaServicosDisponiveis(Integer.parseInt(listSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
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
        for (int i = 0; i < listServicosDisponiveis.size(); i++) {
            for (Servicos servicos : servicoSelecionado) {
                if (servicos.getId() == listServicosDisponiveis.get(i).getId()) {
                    ConvenioServico convenioServico = new ConvenioServico();
                    convenioServico.setServicos(listServicosDisponiveis.get(i));
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
        if (sucesso) {
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
        if (listSubGrupoConvenio.isEmpty()) {
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

    public int getIdGrupoConvenio() {
        return idGrupoConvenio;
    }

    public void setIdGrupoConvenio(int idGrupoConvenio) {
        this.idGrupoConvenio = idGrupoConvenio;
    }

    public int getIdSubGrupoConvenio() {
        return idSubGrupoConvenio;
    }

    public void setIdSubGrupoConvenio(int idSubGrupoConvenio) {
        this.idSubGrupoConvenio = idSubGrupoConvenio;
    }

    public int getIdGrupoConvenioFiltro() {
        return idGrupoConvenioFiltro;
    }

    public void setIdGrupoConvenioFiltro(int idGrupoConvenioFiltro) {
        this.idGrupoConvenioFiltro = idGrupoConvenioFiltro;
    }

    public Servicos[] getServicoSelecionado() {
        return servicoSelecionado;
    }

    public void setServicoSelecionado(Servicos[] servicoSelecionado) {
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
}
