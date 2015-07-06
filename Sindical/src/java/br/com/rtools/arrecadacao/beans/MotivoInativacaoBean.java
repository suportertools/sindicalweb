package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.MotivoInativacao;
import br.com.rtools.arrecadacao.db.MotivoInativacaoDB;
import br.com.rtools.arrecadacao.db.MotivoInativacaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MotivoInativacaoBean implements Serializable {

    private MotivoInativacao motivoInativacao = new MotivoInativacao();
    private String comoPesquisa = "T";
    private String descPesquisa = "";
    private String msgConfirma;
    private String linkVoltar;

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

    public MotivoInativacaoBean() {
//        htmlTable = new HtmlDataTable();
    }

    public MotivoInativacao getMotivoInativacao() {
        return motivoInativacao;
    }

    public void setMotivoInativacao(MotivoInativacao motivoInativacao) {
        this.motivoInativacao = motivoInativacao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        MotivoInativacaoDB db = new MotivoInativacaoDBToplink();
        NovoLog log = new NovoLog();
        if (motivoInativacao.getId() == -1) {
            if (motivoInativacao.getDescricao().equals("")) {
                msgConfirma = "Digite um Motivo de inativação por favor!";
            } else {
                if (db.idMotivoInativacao(motivoInativacao) == null) {
                    db.insert(motivoInativacao);
                    msgConfirma = "Motivo de Inativação salva com Sucesso!";
                    log.save("Motivo inserido " + motivoInativacao.getId() + " - " + motivoInativacao.getDescricao());
                } else {
                    msgConfirma = "Este Motivo de Inativação já existe no Sistema.";
                }
            }
        } else {
            MotivoInativacao mi = new MotivoInativacao();
            mi = db.pesquisaCodigo(motivoInativacao.getId());
            String antes = "De: " + mi.getDescricao();
            db.getEntityManager().getTransaction().begin();
            if (db.update(motivoInativacao)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Motivo de Inativação atualizado com Sucesso!";
                log.update(antes, motivoInativacao.getId() + " - " + motivoInativacao.getDescricao());
            } else {
                db.getEntityManager().getTransaction().rollback();
            }
        }
        return null;
    }

    public String novo() {
        motivoInativacao = new MotivoInativacao();
        msgConfirma = "";
        return "motivoInativacao";
    }

    public String excluir() {
        MotivoInativacaoDB db = new MotivoInativacaoDBToplink();
        NovoLog log = new NovoLog();
        if (motivoInativacao.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            motivoInativacao = db.pesquisaCodigo(motivoInativacao.getId());
            if (db.delete(motivoInativacao)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Motivo de Inativação Excluida com Sucesso!";
                log.delete(motivoInativacao.getId() + " - " + motivoInativacao.getDescricao());
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Motivo de Inativação não pode ser excluido";
            }
        }
        motivoInativacao = new MotivoInativacao();
        return null;
    }

    //ESTA PESQUISA ERA A ATUAL
    /*public List getListaMotivoInativacao(){
     Pesquisa pesquisa = new Pesquisa();
     List result = null;
     result = pesquisa.pesquisar("MotivoInativacao", "motivoInativacao" , descPesquisa, "inativacao", comoPesquisa);
     return result;
     }*/
    public List getListaMotivoInativacao() {
        MotivoInativacaoDB db = new MotivoInativacaoDBToplink();
        List result = null;
        result = db.pesquisaTodos();
        return result;
    }

    public void refreshForm() {
    }
    /*public String pesquisarMotivoInativacao(){
     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadMotivoInativacao");
     return "pesquisaMotivoInativacao";
     }*/

    public String editar() {
//       motivoInativacao = (MotivoInativacao) getHtmlTable().getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("motivoInativacaoPesquisa", motivoInativacao);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "motivoInativacao";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }
    /*public String linkVoltar(){
     if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
     return "menuArrecadacao";
     }else
     return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
     }*/
    /*public String linkVoltarPesquisaMotivoInativacao(){
     linkVoltar = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
     if ( linkVoltar == null){
     return "cadMotivoInativacao";
     }else
     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
     return linkVoltar;
     }*/
}
