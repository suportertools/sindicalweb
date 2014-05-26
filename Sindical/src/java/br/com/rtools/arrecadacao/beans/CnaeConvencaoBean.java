package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.ConvencaoDB;
import br.com.rtools.arrecadacao.db.ConvencaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CnaeConvencaoBean implements Serializable {

    private List<DataObject> listaCnaes = new ArrayList();
    private List<DataObject> listaCnaesAdc = new ArrayList();
    private Cnae cnae = new Cnae();
    private String descricao = "";
    private int idConvencao = 0;
    private String msgConfirma = "";

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

    public void refreshForm() {
    }

    public String salvarSelecionados() {
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        novoLog.startList();
        int iConvencao = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());
        Convencao convencao = (Convencao) di.find(new Convencao(), iConvencao);
        di.openTransaction();
        for (int i = 0; i < listaCnaes.size(); i++) {
            if ((Boolean) listaCnaes.get(i).getArgumento0()) {
                CnaeConvencao cnaeConvencao = new CnaeConvencao(-1, (Cnae) listaCnaes.get(i).getArgumento1(), convencao);
                if (di.save(cnaeConvencao)) {
                    novoLog.save(
                            "ID: " + cnaeConvencao.getId()
                            + " - Cnae: (" + cnaeConvencao.getCnae().getId() + ") " + cnaeConvencao.getCnae().getCnae() + " - " + cnaeConvencao.getCnae().getNumero()
                            + " - Descrição: " + cnaeConvencao.getConvencao().getDescricao()
                    );
                    msgConfirma = "CNAES adcionados com sucesso!";
                } else {
                    novoLog.cancelList();
                    di.rollback();
                    msgConfirma = "Erro ao adicionar CNAE!";
                    return null;
                }
            }
        }
        novoLog.saveList();
        di.commit();

        atualizaListaDisponiveis();
        atualizarListaAdc();
        return null;
    }

    public void atualizaListaDisponiveis() {
        listaCnaes.clear();
        CnaeDB dbCnae = new CnaeDBToplink();
        List<Cnae> list = dbCnae.pesquisaCnaeSemConvencao(descricao);
        for (int i = 0; i < list.size(); i++) {
            listaCnaes.add(new DataObject(false, list.get(i)));
        }
    }

    public void atualizarListaAdc() {
        listaCnaesAdc.clear();
        CnaeConvencaoDB db = new CnaeConvencaoDBToplink();
        int iConvencao = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());
        List<CnaeConvencao> listaCnaeCon = db.pesquisarCnaeConvencaoPorConvencao(iConvencao);
        for (int i = 0; i < listaCnaeCon.size(); i++) {
            listaCnaesAdc.add(new DataObject(false, (CnaeConvencao) (listaCnaeCon.get(i))));
        }
    }

    public String excluirTodos() {
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        novoLog.startList();
        if (!listaCnaesAdc.isEmpty()) {
            di.openTransaction();
            for (int i = 0; i < listaCnaesAdc.size(); i++) {
                CnaeConvencao cn = (CnaeConvencao) di.find(new CnaeConvencao(), ((CnaeConvencao) listaCnaesAdc.get(i).getArgumento1()).getId());
                if (!di.delete(cn)) {
                    di.rollback();
                    msgConfirma = "Erro ao excluir Registros!";
                    novoLog.cancelList();
                    return null;
                }
                novoLog.delete(
                        "ID: " + cn.getId()
                        + " - Cnae: (" + cn.getCnae().getId() + ") " + cn.getCnae().getCnae() + " - " + cn.getCnae().getNumero()
                        + " - Descrição: " + cn.getConvencao().getDescricao()
                );
            }
            novoLog.saveList();
            di.commit();
            atualizaListaDisponiveis();
            atualizarListaAdc();
            msgConfirma = "Cnaes excluídos com sucesso!";
        }
        return null;
    }

    public String excluirSelecionados() {
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        novoLog.startList();
        if (!listaCnaesAdc.isEmpty()) {
            di.openTransaction();
            for (int i = 0; i < listaCnaesAdc.size(); i++) {
                if ((Boolean) listaCnaesAdc.get(i).getArgumento0()) {
                    CnaeConvencao cn = (CnaeConvencao) di.find(new CnaeConvencao(), ((CnaeConvencao) listaCnaesAdc.get(i).getArgumento1()).getId());
                    if (!di.delete(cn)) {
                        di.rollback();
                        msgConfirma = "Erro ao excluir Registros!";
                        novoLog.cancelList();
                        return null;
                    }
                    novoLog.delete(
                            "ID: " + cn.getId()
                            + " - Cnae: (" + cn.getCnae().getId() + ") " + cn.getCnae().getCnae() + " - " + cn.getCnae().getNumero()
                            + " - Descrição: " + cn.getConvencao().getDescricao()
                    );
                }
            }
            di.commit();
            novoLog.saveList();
            atualizaListaDisponiveis();
            atualizarListaAdc();
            msgConfirma = "Cnaes excluídos com sucesso!";
        }
        return null;
    }

    public List<SelectItem> getListaConvencao() {
        List<SelectItem> convencoes = new ArrayList<SelectItem>();
        int i = 0;
        ConvencaoDB db = new ConvencaoDBToplink();
        List select = db.pesquisaTodos();
        while (i < select.size()) {
            convencoes.add(new SelectItem(i,
                    (String) ((Convencao) select.get(i)).getDescricao(),
                    Integer.toString(((Convencao) select.get(i)).getId())));
            i++;
        }
        return convencoes;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public List<DataObject> getListaCnaes() {
        if (listaCnaes.isEmpty() && !descricao.isEmpty()) {
            atualizaListaDisponiveis();
        }
        return listaCnaes;
    }

    public void setListaCnaes(List<DataObject> listaCnaes) {
        this.listaCnaes = listaCnaes;
    }

    public List<DataObject> getListaCnaesAdc() {
        if (!getListaConvencao().isEmpty()) {
            int iConvencao = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());
            if (listaCnaesAdc.isEmpty()) {
                atualizarListaAdc();
            } else if (iConvencao != ((CnaeConvencao) listaCnaesAdc.get(0).getArgumento1()).getConvencao().getId()) {
                atualizarListaAdc();
            }
        }
        return listaCnaesAdc;
    }

    public void setListaCnaesAdc(List<DataObject> listaCnaesAdc) {
        this.listaCnaesAdc = listaCnaesAdc;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}
