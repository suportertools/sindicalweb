package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.TipoServicoDB;
import br.com.rtools.financeiro.db.TipoServicoDBToplink;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class TipoServicoBean implements Serializable {

    private TipoServico tipoServico = new TipoServico();
    private String comoPesquisa = "P";
    private String descPesquisa = "";
    private String msgConfirma;

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

    public TipoServicoBean() {
//        htmlTable = new HtmlDataTable();
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        TipoServicoDB db = new TipoServicoDBToplink();
        if (tipoServico.getId() == -1) {
            if (tipoServico.getDescricao().equals("")) {
                msgConfirma = "Digite um Tipo de serviço!";
            } else {
                if (db.idTipoServico(tipoServico) == null) {
                    db.insert(tipoServico);
                    msgConfirma = "Tipo de Serviço salvo com Sucesso!";
                } else {
                    msgConfirma = "Este Tipo de serviço já existe no Sistema.";
                }
            }
        } else {
            db.getEntityManager().getTransaction().begin();
            if (db.update(tipoServico)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Tipo de Serviço atualizado com Sucesso!";
            } else {
                db.getEntityManager().getTransaction().rollback();
            }
        }
        return null;
    }

    public String novo() {
        tipoServico = new TipoServico();
        return "tipoServico";
    }

    public String excluir() {
        TipoServicoDB db = new TipoServicoDBToplink();
        if (tipoServico.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            tipoServico = db.pesquisaCodigo(tipoServico.getId());
            if (db.delete(tipoServico)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Tipo de Serviço Excluido com Sucesso!";
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Tipo de Serviço não pode ser Excluida!";
            }
        }
        tipoServico = new TipoServico();
        return null;
    }

    public List getListaTipoServico() {
        TipoServicoDB db = new TipoServicoDBToplink();
        List result = null;
        result = db.pesquisaTipoServico(descPesquisa, comoPesquisa);
        return result;
    }

    public void refreshForm() {
    }

    public String pesquisarTipoServico() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "endereco");
        return "pesquisaTipoServico";
    }
    /*public String pesquisaTipoServico(){
     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "tipoServico");
     descPesquisa = "";
     return "pesquisaTipoServico";
     }*/

    public String editar() {
//        tipoServico = (TipoServico) getHtmlTable().getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoServicoPesquisa", tipoServico);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "tipoServico";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    /*public String linkVoltar(){
     if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
     return "menuFinanceiro";
     }else
     return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
     }*/
}
