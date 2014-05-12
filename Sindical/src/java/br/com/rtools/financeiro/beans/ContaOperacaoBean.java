package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.CentroCustoContabil;
import br.com.rtools.financeiro.CentroCustoContabilSub;
import br.com.rtools.financeiro.ContaOperacao;
import br.com.rtools.financeiro.Operacao;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.dao.ContaOperacaoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
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

public class ContaOperacaoBean implements Serializable {

    private ContaOperacao contaOperacao;
    private ContaOperacao[] selectedContaOperacao;
    private List<ContaOperacao> listContaOperacao;
    private List<Plano5> listPlano5;
    private Plano5[] selectedPlano5;
    private Boolean[] hidden;
    /**
     * [0] Operação | [1] Centro Custo Contábil | [2] Centro Custo Contábil Sub
     * | [3] Plano de Contas 5 - Agrupado
     */
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;

    @PostConstruct
    public void init() {
        contaOperacao = new ContaOperacao();
        selectedContaOperacao = null;
        listContaOperacao = new ArrayList<ContaOperacao>();
        listPlano5 = new ArrayList<Plano5>();
        selectedPlano5 = null;
        listSelectItem = new ArrayList[]{
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>()
        };
        index = new Integer[]{0, 0, 0, 0};
        hidden = new Boolean[]{false};
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("contaOperacaoBean");
    }

    public void clear() {
        GenericaSessao.remove("contaOperacaoBean");
    }

    public void clear(int type) {
        hidden[0] = false;
        if (type == 1 || type == 4) {
            if (type == 1) {
                index[3] = 0;
            }
            index[1] = 0;
            index[2] = 0;
            listSelectItem[3] = new ArrayList<SelectItem>();
            listPlano5.clear();
            listContaOperacao.clear();
            if (type == 4) {
                contaOperacao = new ContaOperacao();
                PF.closeDialog("dlg_co");
                PF.update("form_co");
            }
        } else if (type == 2) {
            getListPlano5().clear();
            getListContaOperacao().clear();
        } else if (type == 3) {
            getListContaOperacao().clear();
        }
    }

    public void setItem(Plano5 p) {
        contaOperacao = new ContaOperacao();
        contaOperacao.setPlano5(p);
        DaoInterface di = new Dao();
        contaOperacao.setOperacao((Operacao) di.find(new Operacao(), Integer.parseInt(getListOperacoes().get(index[0]).getDescription())));
        if (contaOperacao.getOperacao().getId() == 1 || contaOperacao.getOperacao().getId() == 2) {
            hidden[0] = true;
        } else {
            hidden[0] = false;
        }
        PF.openDialog("dlg_co");
        PF.update("form_co:i_panel_co");
    }

    public void save() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (contaOperacao.getOperacao().getId() == 1 || contaOperacao.getOperacao().getId() == 2) {
            if (getListCentroCustoContabil().isEmpty()) {
                GenericaMensagem.warn("Validação", "Cadastrar Centro de Custo Contábil");
                PF.update("form_co:i_message_co");
                return;
            }
            if (getListCentroCustoContabilSub().isEmpty()) {
                GenericaMensagem.warn("Validação", "Cadastrar Centro de Custo Contábil Sub");
                PF.update("form_co:i_message_co");
                return;
            }
            contaOperacao.setCentroCustoContabilSub((CentroCustoContabilSub) di.find(new CentroCustoContabilSub(), Integer.parseInt(getListCentroCustoContabilSub().get(index[2]).getDescription())));
        } else {
            contaOperacao.setCentroCustoContabilSub(null);
        }
        di.openTransaction();
        if (contaOperacao.getId() == -1) {
            if (di.save(contaOperacao)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                clear(2);
                PF.update("form_co:i_panel_co");
                PF.update("form_co:i_message_co");
                PF.update("form_co:i_tbl_co");
                //novoLog.save(""+contaOperacao);
            } else {
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
                PF.update("form_co:i_panel_co");
                PF.update("form_co:i_message_co");
                di.rollback();
            }
        } else {
            ContaOperacao co = (ContaOperacao) di.find(contaOperacao);
            if (di.update(contaOperacao)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
                clear(2);
                PF.update("form_co:i_panel_co");
                PF.update("form_co:i_message_co");
                PF.update("form_co:i_tbl_co");
                //novoLog.update(contaOperacao.toString(), co.toString());
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizado registro!");
                PF.update("form_co:i_panel_co");
                PF.update("form_co:i_message_co");
                di.rollback();
            }
        }
    }

    public void editItem(ContaOperacao co) {
        contaOperacao = co;
        DaoInterface di = new Dao();
        if (contaOperacao.getOperacao().getId() == 1 || contaOperacao.getOperacao().getId() == 2) {
            for (int i = 0; i < getListCentroCustoContabil().size(); i++) {
                if (contaOperacao.getCentroCustoContabilSub().getCentroCustoContabil().getId() == Integer.parseInt(getListCentroCustoContabil().get(i).getDescription())) {
                    index[1] = i;
                    break;
                }
            }
            index[2] = 0;
            listSelectItem[2].clear();
            for (int i = 0; i < getListCentroCustoContabilSub().size(); i++) {
                if (contaOperacao.getCentroCustoContabilSub().getId() == Integer.parseInt(getListCentroCustoContabilSub().get(i).getDescription())) {
                    index[2] = i;
                    break;
                }
            }
        }
        PF.openDialog("dlg_co");
        PF.update("form_co:i_panel_co");
    }

    public void removeItens() {
        boolean err = false;
        if (selectedContaOperacao == null) {
            GenericaMensagem.warn("Validação", "Nenhum item selecionado!");
            return;
        }
        if (selectedContaOperacao.length == 0) {
            GenericaMensagem.warn("Validação", "Nenhum item selecionado!");
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        for (int i = 0; i < selectedContaOperacao.length; i++) {
            for (int j = 0; j < listContaOperacao.size(); j++) {
                if (listContaOperacao.get(j).getId() == selectedContaOperacao[i].getId()) {
                    if (!di.delete(di.find(listContaOperacao.get(j)))) {
                        err = true;
                        break;
                    }
                }
            }
            if (err) {
                break;
            }
        }
        if (err) {
            GenericaMensagem.warn("Erro", "Ao excluir registros selecionados!");
            di.rollback();
        } else {
            GenericaMensagem.info("Sucesso", "Registro(s) excluídos com sucesso");
            clear(2);
            PF.update("form_co");
            di.commit();
        }
    }

    public void removeAllItens() {
        DaoInterface di = new Dao();
        di.openTransaction();
        boolean err = false;
        for (int i = 0; i < listContaOperacao.size(); i++) {
            if (!di.delete(di.find(listContaOperacao.get(i)))) {
                err = true;
                break;
            }
        }
        if (err) {
            GenericaMensagem.warn("Erro", "Ao excluir registros selecionados!");
            di.rollback();
        } else {
            GenericaMensagem.info("Sucesso", "Registro(s) excluídos com sucesso");
            selectedContaOperacao = null;
            clear(2);
            PF.update("form_co");
            di.commit();
        }
    }

    public void updateContaOperacao(ContaOperacao co) {
        if (co.isContaFixa()) {
            co.setContaFixa(false);
        } else {
            co.setContaFixa(true);
        }
        DaoInterface di = new Dao();
        if (di.update(co, true)) {
            clear(2);
            PF.update("form_co");
            GenericaMensagem.info("Sucesso", "Registro atualizado");
        } else {
            GenericaMensagem.warn("Erro", "Ao atualizar");
        }
    }

    public void removeItem(ContaOperacao co) {
        DaoInterface di = new Dao();
        if (di.delete(co, true)) {
            clear(2);
            PF.update("form_co");
            GenericaMensagem.info("Sucesso", "Registro excluído");
        } else {
            GenericaMensagem.warn("Erro", "Ao excluir");
        }
    }

    /**
     * <strong>[0] Operações</strong>
     *
     * @return
     */
    public List<SelectItem> getListOperacoes() {
        if (listSelectItem[0].isEmpty()) {
            DaoInterface di = new Dao();
            List<Operacao> list = (List<Operacao>) di.list(new Operacao());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[0];
    }

    /**
     * <strong>[1] Centro de Custo Contábil</strong>
     *
     * @return
     */
    public List<SelectItem> getListCentroCustoContabil() {
        if (contaOperacao.getCentroCustoContabilSub() == null) {
            listSelectItem[1].clear();
            return listSelectItem[1];
        }
        if (listSelectItem[1].isEmpty()) {
            DaoInterface di = new Dao();
            List<CentroCustoContabil> list = (List<CentroCustoContabil>) di.list(new CentroCustoContabil(), true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getDescricao() + " - " + list.get(i).getCodigo(), "" + list.get(i).getId()));
            }
            if (listSelectItem[1].isEmpty()) {
                listSelectItem[1] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[1];
    }

    /**
     * <strong>[2] Centro de Custo Contábil Sub</strong>
     *
     * @return
     */
    public List<SelectItem> getListCentroCustoContabilSub() {
        if (contaOperacao.getCentroCustoContabilSub() == null) {
            listSelectItem[1].clear();
            return listSelectItem[1];
        }
        if (listSelectItem[2].isEmpty()) {
            index[2] = 0;
            if (!getListCentroCustoContabil().isEmpty()) {
                DaoInterface di = new Dao();
                int id = Integer.parseInt(getListCentroCustoContabil().get(index[1]).getDescription());
                List<CentroCustoContabilSub> list = (List<CentroCustoContabilSub>) di.listQuery(new CentroCustoContabilSub(), "findByCCC", new Object[]{id});
                for (int i = 0; i < list.size(); i++) {
                    listSelectItem[2].add(new SelectItem(i, list.get(i).getDescricao() + " - " + list.get(i).getCodigo(), "" + list.get(i).getId()));
                }
            }
            if (listSelectItem[2].isEmpty()) {
                listSelectItem[2] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[2];
    }

    /**
     * Usando plano_vw
     *
     * @return
     */
    public List<SelectItem> getListPlano4Group() {
        if (listSelectItem[3].isEmpty()) {
            index[3] = 0;
            if (!getListOperacoes().isEmpty()) {
                ContaOperacaoDao cod = new ContaOperacaoDao();
                List list = cod.listPlano4AgrupadoPlanoVwNotInContaOperacao(Integer.parseInt(getListOperacoes().get(index[0]).getDescription()));
                for (int i = 0; i < list.size(); i++) {
                    listSelectItem[3].add(new SelectItem(i, ((List) list.get(i)).get(1).toString(), ((List) list.get(i)).get(0).toString()));
                }
            }
            if (listSelectItem[3].isEmpty()) {
                listSelectItem[3] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[3];
    }

    public ContaOperacao getContaOperacao() {
        return contaOperacao;
    }

    public void setContaOperacao(ContaOperacao contaOperacao) {
        this.contaOperacao = contaOperacao;
    }

    public ContaOperacao[] getSelectedContaOperacao() {
        return selectedContaOperacao;
    }

    public void setSelectedContaOperacao(ContaOperacao[] selectedContaOperacao) {
        this.selectedContaOperacao = selectedContaOperacao;
    }

    public List<ContaOperacao> getListContaOperacao() {
        if (!getListOperacoes().isEmpty()) {
            try {
                ContaOperacaoDao cod = new ContaOperacaoDao();
                listContaOperacao = (List<ContaOperacao>) cod.listContaOperacaoPorOperacao(Integer.parseInt(getListOperacoes().get(index[0]).getDescription()), Integer.parseInt(getListPlano4Group().get(index[3]).getDescription()));
            } catch (NumberFormatException e) {
                listContaOperacao.clear();
                return new ArrayList();
            }
        }
        return listContaOperacao;
    }

    public void setListContaOperacao(List<ContaOperacao> listContaOperacao) {
        this.listContaOperacao = listContaOperacao;
    }

    public List<Plano5> getListPlano5() {
        if (listPlano5.isEmpty()) {
            ContaOperacaoDao cod = new ContaOperacaoDao();
            listPlano5 = (List<Plano5>) cod.findPlano5ByPlano4NotInContaOperacao(Integer.parseInt(getListPlano4Group().get(index[3]).getDescription()), Integer.parseInt(getListOperacoes().get(index[0]).getDescription()));
        }
        return listPlano5;
    }

    public void setListPlano5(List<Plano5> listPlano5) {
        this.listPlano5 = listPlano5;
    }

    public Plano5[] getSelectedPlano5() {
        return selectedPlano5;
    }

    public void setSelectedPlano5(Plano5[] selectedPlano5) {
        this.selectedPlano5 = selectedPlano5;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public Boolean[] getHidden() {
        return hidden;
    }

    public void setHidden(Boolean[] hidden) {
        this.hidden = hidden;
    }
}
