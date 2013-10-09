package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Suspencao;
import br.com.rtools.associativo.db.SuspencaoDB;
import br.com.rtools.associativo.db.SuspencaoDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class SuspencaoJSFBean {

    private Suspencao suspencao = new Suspencao();
    private String msgConfirma = "";
    private int idIndex = -1;
    private List<Suspencao> listaSuspencao = new ArrayList();
    private boolean limpar = true;

    public String salvar() {
        SuspencaoDB db = new SuspencaoDBToplink();
        if (suspencao.getPessoa().getId() == -1) {
            msgConfirma = "Pesquise um sócio para Suspender!";
            return null;
        }
        if (suspencao.getDataInicial().length() < 7 || suspencao.getDataFinal().length() < 7) {
            msgConfirma = "Data inválida!";
            return null;
        }
        if (DataHoje.converteDataParaInteger(suspencao.getDataInicial())
                > DataHoje.converteDataParaInteger(suspencao.getDataFinal())) {
            msgConfirma = "Data inicial não pode ser maior que data final!";
            return null;
        }
        if (suspencao.getMotivo().equals("") || suspencao.getMotivo() == null) {
            msgConfirma = "Digite um motivo de Suspensão!";
            return null;
        }
        if (suspencao.getId() == -1) {
            if (db.insert(suspencao)) {
                msgConfirma = "Suspensão salva com sucesso.";
            } else {
                msgConfirma = "Erro ao salvar suspensão!";
            }
        } else {
            if (db.update(suspencao)) {
                msgConfirma = "Suspensão atualizada com sucesso.";
            } else {
                msgConfirma = "Erro ao atualizar suspensão!";
            }
        }
        limpar = false;
        return null;
    }

    public String excluir() {
        SuspencaoDB db = new SuspencaoDBToplink();
        if (suspencao.getId() == -1) {
            msgConfirma = "Selecione uma suspensão para ser excluída!";
            return null;
        }
        suspencao = db.pesquisaCodigo(suspencao.getId());
        if (db.delete(suspencao)) {
            msgConfirma = "Suspensão deletada com sucesso!";
            limpar = true;
            listaSuspencao.clear();
        } else {
            msgConfirma = "Erro ao deletar suspensão!";
        }
        return null;
    }

    public String limpa() {
        limpar = true;
        return novo();
    }

    public String novo() {
        if (limpar) {
            suspencao = new Suspencao();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return "suspencao";
    }

    public String editar() {
        suspencao = (Suspencao) listaSuspencao.get(getIdIndex());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", suspencao.getPessoa());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "suspencao";
    }

    public List<Suspencao> getListaSuspencao() {
        if (listaSuspencao.isEmpty()) {
            SuspencaoDB db = new SuspencaoDBToplink();
            listaSuspencao = db.pesquisaTodos();
        }
        return listaSuspencao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public void setListaSuspencao(List<Suspencao> listaSuspencao) {
        this.listaSuspencao = listaSuspencao;
    }

    public Suspencao getSuspencao() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            suspencao.setPessoa((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return suspencao;
    }

    public void setSuspencao(Suspencao suspencao) {
        this.suspencao = suspencao;
    }
}
