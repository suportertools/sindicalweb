package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.DescontoServicoEmpresa;
import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.db.DescontoServicoEmpresaDB;
import br.com.rtools.financeiro.db.DescontoServicoEmpresaDBTopLink;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Dao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.RowEditEvent;

@ManagedBean
@SessionScoped
public class DescontoServicoEmpresaBean implements Serializable {

    private DescontoServicoEmpresa descontoServicoEmpresa;
    private List<Servicos> listServicos;
    private List<DescontoServicoEmpresa> listDescontoServicoEmpresa;
    private List<Servicos> selectedServicos;
    private List<DescontoServicoEmpresa> listDSEPorEmpresa;
    private List<SelectItem> listGrupoFinanceiro;
    private List<SelectItem> listSubGrupoFinanceiro;
    private Integer idServicos;
    private Integer idGrupoFinanceiro;
    private Integer idSubGrupoFinanceiro;
    private String descricaoPesquisaNome;
    private String descricaoPesquisaCNPJ;
    private String comoPesquisa;
    private String porPesquisa;
    private String message;
    private boolean desabilitaPesquisaNome;
    private boolean desabilitaPesquisaCNPJ;
    private Boolean habilitaSubGrupo;

    @PostConstruct
    public void init() {
        descontoServicoEmpresa = new DescontoServicoEmpresa();
        idServicos = 0;
        idGrupoFinanceiro = 0;
        idSubGrupoFinanceiro = 0;
        listServicos = new ArrayList<>();
        listGrupoFinanceiro = new ArrayList<>();
        listSubGrupoFinanceiro = new ArrayList<>();
        listDescontoServicoEmpresa = new ArrayList<>();
        listDSEPorEmpresa = new ArrayList<>();
        descricaoPesquisaNome = "";
        descricaoPesquisaCNPJ = "";
        comoPesquisa = "";
        porPesquisa = "";
        message = "";
        desabilitaPesquisaNome = false;
        desabilitaPesquisaCNPJ = false;
        selectedServicos = null;
        habilitaSubGrupo = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("descontoServicoEmpresaBean");
        GenericaSessao.remove("descontoServicoEmpresaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("descontoServicoEmpresaBean");
    }

    public void clear(Integer tcase) {
        if (tcase == 0) {
            descontoServicoEmpresa = new DescontoServicoEmpresa();
            idServicos = 0;
            listServicos.clear();
            listDescontoServicoEmpresa.clear();
            listDSEPorEmpresa.clear();
            listServicos.clear();
        }
        if (tcase == 1) {
            listSubGrupoFinanceiro.clear();
            listServicos.clear();
            idSubGrupoFinanceiro = null;
            selectedServicos = null;
        }
        if (tcase == 2) {
            listServicos.clear();
            selectedServicos = null;
        }
    }

    public void save() {
        if (descontoServicoEmpresa.getJuridica().getId() == -1) {
            message = "Pesquisar pessoa jurídica!";
            GenericaMensagem.warn("Validação", message);
            return;
        }
        if (listServicos.isEmpty()) {
            message = "Cadastrar serviços!";
            GenericaMensagem.warn("Validação", message);
            return;
        }
        if (selectedServicos.isEmpty()) {
            message = "Selecionar serviços!";
            GenericaMensagem.warn("Validação", message);
            return;
        }
        if (descontoServicoEmpresa.getDesconto() <= 0) {
            message = "Informar o valor do desconto!";
            GenericaMensagem.warn("Validação", message);
            return;
        }
        // descontoServicoEmpresa.setDesconto(Moeda.converteUS$(desconto));
        Dao dao = new Dao();
        int idServicoAntes = -1;
        if (descontoServicoEmpresa.getId() != -1) {
            idServicoAntes = descontoServicoEmpresa.getServicos().getId();
        }
        Juridica juridica = descontoServicoEmpresa.getJuridica();
        DescontoServicoEmpresa dse = new DescontoServicoEmpresa();
        dao.openTransaction();
        Boolean error = false;
        for (int i = 0; i < selectedServicos.size(); i++) {
            dse.setServicos(selectedServicos.get(i));
            dse.setJuridica(descontoServicoEmpresa.getJuridica());
            DescontoServicoEmpresaDB descontoServicoEmpresaDB = new DescontoServicoEmpresaDBTopLink();
            NovoLog novoLog = new NovoLog();
            dse.setDescontoString(descontoServicoEmpresa.getDescontoString());
            if (descontoServicoEmpresaDB.existeDescontoServicoEmpresa(descontoServicoEmpresa)) {
                message = "Desconto já cadastrado para essa empresa!";
                GenericaMensagem.warn("Validação", message);
                dao.rollback();
                break;
            }
            if (dao.save(dse)) {
                novoLog.save(
                        "ID: " + dse.getId()
                        + " - Serviços: (" + dse.getServicos().getId() + ") " + descontoServicoEmpresa.getServicos().getDescricao()
                        + " - Jurídica: (" + dse.getJuridica().getId() + ") " + descontoServicoEmpresa.getJuridica().getPessoa().getNome()
                        + " - Desconto (%): " + dse.getDescontoString()
                );
                dse = new DescontoServicoEmpresa();
            } else {
                error = true;
                break;
            }
//                DescontoServicoEmpresa dse = (DescontoServicoEmpresa) di.find(descontoServicoEmpresa);
//                String beforeUpdate
//                        = "ID: " + dse.getId()
//                        + " - Serviços: (" + dse.getServicos().getId() + ") " + dse.getServicos().getDescricao()
//                        + " - Jurídica: (" + dse.getJuridica().getId() + ") " + dse.getJuridica().getPessoa().getNome()
//                        + " - Desconto (%): " + dse.getDesconto();
//                di.openTransaction();
//                if (di.update(descontoServicoEmpresa)) {
//                    novoLog.update(beforeUpdate,
//                            "ID: " + descontoServicoEmpresa.getId()
//                            + " - Serviços: (" + descontoServicoEmpresa.getServicos().getId() + ") " + descontoServicoEmpresa.getServicos().getDescricao()
//                            + " - Jurídica: (" + descontoServicoEmpresa.getJuridica().getId() + ") " + descontoServicoEmpresa.getJuridica().getPessoa().getNome()
//                            + " - Desconto (%): " + descontoServicoEmpresa.getDesconto()
//                    );
//                    di.commit();
//                    message = "Registro atualizado";
//                    GenericaMensagem.info("Sucesso", message);
//                    clear();
//                } else {
//                    di.rollback();
//                    message = "Erro ao atualizar este registro!";
//                    GenericaMensagem.warn("Erro", message);
//                }
        }
        if (error) {
            dao.rollback();
            descontoServicoEmpresa.setId(-1);
            message = "Erro ao atualizar este registro!";
            GenericaMensagem.warn("Erro", message);
        } else {
            message = "Registro cadastrado";
            GenericaMensagem.info("Sucesso", message);
            dao.commit();
            listDSEPorEmpresa.clear();
            selectedServicos.clear();
            listServicos.clear();
        }
        descontoServicoEmpresa.setJuridica(juridica);
    }

    public void update(RowEditEvent event) {
        DescontoServicoEmpresa dse = (DescontoServicoEmpresa) event.getObject();
        if (dse.getId() != -1) {
            if (dse.getDesconto() <= 0) {
                message = "Informar o valor do desconto!";
                GenericaMensagem.warn("Validação", message);
                return;
            }
            NovoLog novoLog = new NovoLog();
            Dao dao = new Dao();
            DescontoServicoEmpresa dseBefore = (DescontoServicoEmpresa) dao.find(dse);
            String beforeUpdate
                    = "ID: " + dseBefore.getId()
                    + " - Serviços: (" + dseBefore.getServicos().getId() + ") " + dseBefore.getServicos().getDescricao()
                    + " - Jurídica: (" + dseBefore.getJuridica().getId() + ") " + dseBefore.getJuridica().getPessoa().getNome()
                    + " - Desconto (%): " + dseBefore.getDesconto();
            dao.openTransaction();
            if (dao.update(dse)) {
                novoLog.update(beforeUpdate,
                        "ID: " + dse.getId()
                        + " - Serviços: (" + dse.getServicos().getId() + ") " + dse.getServicos().getDescricao()
                        + " - Jurídica: (" + dse.getJuridica().getId() + ") " + dse.getJuridica().getPessoa().getNome()
                        + " - Desconto (%): " + dse.getDesconto()
                );
                dao.commit();
                message = "Desconto atualizado com sucesso";
                GenericaMensagem.info("Sucesso", message);
            } else {
                dao.rollback();
                message = "Erro ao atualizar este desconto!";
                GenericaMensagem.warn("Erro", message);
            }
        }
    }

//    public String edit(DescontoServicoEmpresa dse) {
//        descontoServicoEmpresa = dse;
//        GenericaSessao.put("descontoServicoEmpresaPesquisa", dse);
//        for (int i = 0; i < listServicos.size(); i++) {
//            if (Integer.parseInt(listServicos.get(i).getDescription()) == dse.getServicos().getId()) {
//                idServicos = i;
//            }
//        }
//        GenericaSessao.put("linkClicado", true);
//        return GenericaSessao.getString("urlRetorno");
//    }
//    public void editDSE(DescontoServicoEmpresa dse) {
//        descontoServicoEmpresa = dse;
//        for (int i = 0; i < listServicos.size(); i++) {
//            if (Integer.parseInt(listServicos.get(i).getDescription()) == dse.getServicos().getId()) {
//                idServicos = i;
//            }
//        }
//    }
    public void delete() {
        if (descontoServicoEmpresa.getId() != -1) {
            Juridica juridica = descontoServicoEmpresa.getJuridica();
            boolean isMantemJuridica = true;
            if (listDSEPorEmpresa.isEmpty()) {
                isMantemJuridica = false;
            }
            listDSEPorEmpresa.size();
            NovoLog novoLog = new NovoLog();
            Dao dao = new Dao();
            descontoServicoEmpresa = (DescontoServicoEmpresa) dao.find(descontoServicoEmpresa);
            dao.openTransaction();
            if (dao.delete(descontoServicoEmpresa)) {
                novoLog.delete(
                        "ID: " + descontoServicoEmpresa.getId()
                        + " - Serviços: (" + descontoServicoEmpresa.getServicos().getId() + ") " + descontoServicoEmpresa.getServicos().getDescricao()
                        + " - Jurídica: (" + descontoServicoEmpresa.getJuridica().getId() + ") " + descontoServicoEmpresa.getJuridica().getPessoa().getNome()
                        + " - Desconto (%): " + descontoServicoEmpresa.getDesconto()
                );
                dao.commit();
                GenericaMensagem.info("Sucesso", message);
                clear();
            } else {
                dao.rollback();
                message = "Erro ao excluir registro!";
                GenericaMensagem.warn("Erro", message);
            }
            descontoServicoEmpresa.setJuridica(juridica);
        } else {
            message = "Pesquisar registro a ser excluído!";
            GenericaMensagem.warn("Erro", message);
        }
    }

    public void remove(RowEditEvent event) {
        remove((DescontoServicoEmpresa) event.getObject());
    }

    public void remove(DescontoServicoEmpresa dse) {
        if (dse.getId() != -1) {
            Dao dao = new Dao();
            NovoLog novoLog = new NovoLog();
            dao.openTransaction();
            if (dao.delete(dse)) {
                novoLog.delete(
                        "ID: " + dse.getId()
                        + " - Serviços: (" + dse.getServicos().getId() + ") " + dse.getServicos().getDescricao()
                        + " - Jurídica: (" + dse.getJuridica().getId() + ") " + dse.getJuridica().getPessoa().getNome()
                        + " - Desconto (%): " + dse.getDesconto()
                );
                dao.commit();
                message = "Registro excluído";
                GenericaMensagem.info("Sucesso", message);
                listDSEPorEmpresa.clear();
                listServicos.clear();
            } else {
                dao.rollback();
                message = "Erro ao excluir registro!";
                GenericaMensagem.warn("Erro", message);
            }
        }
    }

    public List<DescontoServicoEmpresa> getListDescontoServicoEmpresa() {
        if (listDescontoServicoEmpresa.isEmpty()) {
            DescontoServicoEmpresaDB descontoServicoEmpresaDB = new DescontoServicoEmpresaDBTopLink();
            if (desabilitaPesquisaCNPJ && !descricaoPesquisaNome.equals("")) {
                listDescontoServicoEmpresa = descontoServicoEmpresaDB.pesquisaDescontoServicoEmpresas("nome", descricaoPesquisaNome, comoPesquisa);
            } else if (desabilitaPesquisaNome && !descricaoPesquisaCNPJ.equals("")) {
                listDescontoServicoEmpresa = descontoServicoEmpresaDB.pesquisaDescontoServicoEmpresas("cnpj", descricaoPesquisaCNPJ, comoPesquisa);
            } else {
                listDescontoServicoEmpresa = descontoServicoEmpresaDB.listaTodos();
            }
        }
        return listDescontoServicoEmpresa;
    }

    public List<DescontoServicoEmpresa> getListDSEPorEmpresa() {
        if (listDSEPorEmpresa.isEmpty()) {
            if (descontoServicoEmpresa.getJuridica().getId() != -1) {
                DescontoServicoEmpresaDB descontoServicoEmpresaDB = new DescontoServicoEmpresaDBTopLink();
                listDSEPorEmpresa = descontoServicoEmpresaDB.listaTodosPorEmpresa(descontoServicoEmpresa.getJuridica().getId());
            }
        }
        return listDSEPorEmpresa;
    }

    public DescontoServicoEmpresa getDescontoServicoEmpresa() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            Juridica juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            if (descontoServicoEmpresa.getId() == -1) {
                descontoServicoEmpresa.setJuridica(juridica);
            } else {
                if (descontoServicoEmpresa.getJuridica().getId() != juridica.getId()) {
                    listDSEPorEmpresa.clear();
                    descontoServicoEmpresa.setId(-1);
                    descontoServicoEmpresa.setJuridica(juridica);
                }
            }
        }
        if (GenericaSessao.exists("descontoServicoEmpresaPesquisa")) {
            listDSEPorEmpresa.clear();
            descontoServicoEmpresa = ((DescontoServicoEmpresa) GenericaSessao.getObject("descontoServicoEmpresaPesquisa", true));
        }
        return descontoServicoEmpresa;
    }

    public void setDescontoServicoEmpresa(DescontoServicoEmpresa descontoServicoEmpresa) {
        this.descontoServicoEmpresa = descontoServicoEmpresa;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

    public List<Servicos> getListServicos() {
        if (descontoServicoEmpresa.getJuridica().getId() != -1) {
            if (listServicos.isEmpty()) {
                DescontoServicoEmpresaDB dsedb = new DescontoServicoEmpresaDBTopLink();
                if (habilitaSubGrupo) {
                    listServicos = dsedb.listaTodosServicosDisponiveis(descontoServicoEmpresa.getJuridica().getId(), Integer.parseInt(listSubGrupoFinanceiro.get(idSubGrupoFinanceiro).getDescription()));
                } else {
                    listServicos = dsedb.listaTodosServicosDisponiveis(descontoServicoEmpresa.getJuridica().getId(), Integer.parseInt(listGrupoFinanceiro.get(idGrupoFinanceiro).getDescription()), null);
                }
            }
        }
        return listServicos;
    }

    public void setListServicos(List<Servicos> listServicos) {
        this.listServicos = listServicos;
    }

    public String getDescricaoPesquisaNome() {
        return descricaoPesquisaNome;
    }

    public void setDescricaoPesquisaNome(String descricaoPesquisaNome) {
        this.descricaoPesquisaNome = descricaoPesquisaNome;
    }

    public String getDescricaoPesquisaCNPJ() {
        return descricaoPesquisaCNPJ;
    }

    public void setDescricaoPesquisaCNPJ(String descricaoPesquisaCNPJ) {
        this.descricaoPesquisaCNPJ = descricaoPesquisaCNPJ;
    }

    public boolean isDesabilitaPesquisaNome() {
        return desabilitaPesquisaNome;
    }

    public void setDesabilitaPesquisaNome(boolean desabilitaPesquisaNome) {
        this.desabilitaPesquisaNome = desabilitaPesquisaNome;
    }

    public boolean isDesabilitaPesquisaCNPJ() {
        return desabilitaPesquisaCNPJ;
    }

    public void setDesabilitaPesquisaCNPJ(boolean desabilitaPesquisaCNPJ) {
        this.desabilitaPesquisaCNPJ = desabilitaPesquisaCNPJ;
    }

    public void tipoPesquisa() {
        if (!descricaoPesquisaNome.equals("")) {
            desabilitaPesquisaCNPJ = true;
            descricaoPesquisaCNPJ = "";
        } else if (!descricaoPesquisaCNPJ.equals("")) {
            desabilitaPesquisaNome = true;
            descricaoPesquisaNome = "";
        } else {
            desabilitaPesquisaNome = false;
            desabilitaPesquisaCNPJ = false;
            descricaoPesquisaNome = "";
            descricaoPesquisaCNPJ = "";
        }

    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listDescontoServicoEmpresa.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listDescontoServicoEmpresa.clear();
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SelectItem> getListGrupoFinanceiro() {
        if (listGrupoFinanceiro.isEmpty()) {
            List<GrupoFinanceiro> list = new Dao().list(new GrupoFinanceiro(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idGrupoFinanceiro = i;
                }
                listGrupoFinanceiro.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
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
        if (!listGrupoFinanceiro.isEmpty() && habilitaSubGrupo) {
            if (listSubGrupoFinanceiro.isEmpty()) {
                FinanceiroDB financeiroDB = new FinanceiroDBToplink();
                List<SubGrupoFinanceiro> list = financeiroDB.listaSubGrupo(Integer.parseInt(listGrupoFinanceiro.get(idGrupoFinanceiro).getDescription()));
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        idSubGrupoFinanceiro = i;
                    }
                    listSubGrupoFinanceiro.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
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

    public List<Servicos> getSelectedServicos() {
        return selectedServicos;
    }

    public void setSelectedServicos(List<Servicos> selectedServicos) {
        this.selectedServicos = selectedServicos;
    }

    public Boolean getHabilitaSubGrupo() {
        return habilitaSubGrupo;
    }

    public void setHabilitaSubGrupo(Boolean habilitaSubGrupo) {
        this.habilitaSubGrupo = habilitaSubGrupo;
    }
}
