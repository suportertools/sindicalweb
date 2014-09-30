package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.utilitarios.Dao;
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
public class CnaeConvencaoBean implements Serializable {

    private List<Cnae> listCnaes;
    private List<CnaeConvencao> listCnaesConvencao;
    private List<SelectItem> listConvencao;
    private List<Cnae> selectedCnae;
    private List<CnaeConvencao> selectedCnaeConvencao;
    private Cnae cnae;
    private String descricao;
    private int idConvencao;

    @PostConstruct
    public void init() {
        listCnaes = new ArrayList<>();
        listCnaesConvencao = new ArrayList<>();
        listConvencao = new ArrayList<>();
        selectedCnae = null;
        selectedCnaeConvencao = null;
        cnae = new Cnae();
        descricao = "";
        idConvencao = 0;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("cnaeConvencaoBean");
    }

    public int getIdConvencao() {
        return idConvencao;
    }

    public void setIdConvencao(int idConvencao) {
        this.idConvencao = idConvencao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void adddeleteSelected() {
        if (selectedCnae != null) {
            NovoLog novoLog = new NovoLog();
            novoLog.startList();
            int iConvencao = Integer.parseInt(getListConvencao().get(idConvencao).getDescription());
            Dao dao = new Dao();
            Convencao convencao = (Convencao) dao.find(new Convencao(), iConvencao);
            dao.openTransaction();
            for (int i = 0; i < selectedCnae.size(); i++) {
                CnaeConvencao cnaeConvencao = new CnaeConvencao(-1, (Cnae) selectedCnae.get(i), convencao);
                if (dao.save(cnaeConvencao)) {
                    novoLog.save(
                            "ID: " + cnaeConvencao.getId()
                            + " - Cnae: (" + cnaeConvencao.getCnae().getId() + ") " + cnaeConvencao.getCnae().getCnae() + " - " + cnaeConvencao.getCnae().getNumero()
                            + " - Descrição: " + cnaeConvencao.getConvencao().getDescricao()
                    );
                    GenericaMensagem.info("Sucesso", "Registro adicionado");
                } else {
                    novoLog.cancelList();
                    dao.rollback();
                    GenericaMensagem.warn("Erro", "Ao adicionar registro");
                    return;
                }
            }
            novoLog.saveList();
            dao.commit();
            listCnaes.clear();
            listCnaesConvencao.clear();
            selectedCnae = null;
            selectedCnaeConvencao = null;
        }
    }

    public void updateDisponiveis() {
        listCnaes.clear();

    }

    public void updateSelected() {
        listCnaesConvencao.clear();
        getListCnaesConvencao();
    }

    public void deleteAll() {
        NovoLog novoLog = new NovoLog();
        Dao dao = new Dao();
        novoLog.startList();
        if (!listCnaesConvencao.isEmpty()) {
            dao.openTransaction();
            for (int i = 0; i < listCnaesConvencao.size(); i++) {
                if (!dao.delete(listCnaesConvencao.get(i))) {
                    dao.rollback();
                    GenericaMensagem.warn("Erro", "Ao excluir registro!");
                    novoLog.cancelList();
                    return;
                }
                novoLog.delete(
                        "ID: " + listCnaesConvencao.get(i).getId()
                        + " - Cnae: (" + listCnaesConvencao.get(i).getCnae().getId() + ") " + listCnaesConvencao.get(i).getCnae().getCnae() + " - " + listCnaesConvencao.get(i).getCnae().getNumero()
                        + " - Descrição: " + listCnaesConvencao.get(i).getConvencao().getDescricao()
                );
            }
            novoLog.saveList();
            dao.commit();
            listCnaes.clear();
            listCnaesConvencao.clear();
            GenericaMensagem.info("Sucesso", "Registro(s) removido(s)");
            selectedCnae = null;
            selectedCnaeConvencao = null;
        }
    }

    public String deleteSelected() {
        NovoLog novoLog = new NovoLog();
        Dao dao = new Dao();
        novoLog.startList();
        if (selectedCnaeConvencao != null) {
            dao.openTransaction();
            for (int i = 0; i < selectedCnaeConvencao.size(); i++) {
                if (!dao.delete(selectedCnaeConvencao.get(i))) {
                    dao.rollback();
                    GenericaMensagem.warn("Erro", "Ao excluir registro!");
                    novoLog.cancelList();
                    return null;
                }
                novoLog.delete(
                        "ID: " + selectedCnaeConvencao.get(i).getId()
                        + " - Cnae: (" + selectedCnaeConvencao.get(i).getCnae().getId() + ") " + selectedCnaeConvencao.get(i).getCnae().getCnae() + " - " + selectedCnaeConvencao.get(i).getCnae().getNumero()
                        + " - Descrição: " + selectedCnaeConvencao.get(i).getConvencao().getDescricao()
                );
            }
            novoLog.saveList();
            dao.commit();
            listCnaes.clear();
            listCnaesConvencao.clear();
            GenericaMensagem.info("Sucesso", "Registro(s) removido(s)");
            selectedCnae = null;
            selectedCnaeConvencao = null;
        }
        return null;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public List<Cnae> getListCnaes() {
        if(!descricao.isEmpty()) {
            if (listCnaes.isEmpty()) {
                CnaeDB dbCnae = new CnaeDBToplink();
                listCnaes = dbCnae.pesquisaCnaeSemConvencao(descricao);
            }            
        }
        return listCnaes;
    }

    public void setListCnaes(List<Cnae> listCnaes) {
        this.listCnaes = listCnaes;
    }

    public List<CnaeConvencao> getListCnaesConvencao() {
        if (listCnaesConvencao.isEmpty()) {
            CnaeConvencaoDB db = new CnaeConvencaoDBToplink();
            int iConvencao = Integer.parseInt(getListConvencao().get(idConvencao).getDescription());
            listCnaesConvencao = db.pesquisarCnaeConvencaoPorConvencao(iConvencao);
        }
        return listCnaesConvencao;
    }

    public void setListCnaesConvencao(List<CnaeConvencao> listCnaesConvencao) {
        this.listCnaesConvencao = listCnaesConvencao;
    }

    public List<Cnae> getSelectedCnae() {
        return selectedCnae;
    }

    public void setSelectedCnae(List<Cnae> selectedCnae) {
        this.selectedCnae = selectedCnae;
    }

    public List<CnaeConvencao> getSelectedCnaeConvencao() {
        return selectedCnaeConvencao;
    }

    public void setSelectedCnaeConvencao(List<CnaeConvencao> selectedCnaeConvencao) {
        this.selectedCnaeConvencao = selectedCnaeConvencao;
    }

    public List<SelectItem> getListConvencao() {
        if (listConvencao.isEmpty()) {
            Dao dao = new Dao();
            List<Convencao> list = (List<Convencao>) dao.list(new Convencao(), true);
            for (int i = 0; i < list.size(); i++) {
                listConvencao.add(new SelectItem(i, (String) ((Convencao) list.get(i)).getDescricao(), Integer.toString(((Convencao) list.get(i)).getId())));
            }
        }
        return listConvencao;
    }

    public void setListConvencao(List<SelectItem> listConvencao) {
        this.listConvencao = listConvencao;
    }
}
