package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.GrupoCidadeDB;
import br.com.rtools.arrecadacao.db.GrupoCidadeDBToplink;
import br.com.rtools.logSistema.NovoLog;
import java.util.List;
import javax.faces.context.FacesContext;

public class GrupoCidadeJSFBean {

    private GrupoCidade grupoCidade = new GrupoCidade();
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

    public GrupoCidadeJSFBean() {
//        htmlTable = new HtmlDataTable();
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        GrupoCidadeDB db = new GrupoCidadeDBToplink();
        NovoLog log = new NovoLog();
        if (grupoCidade.getId() == -1) {
            if (grupoCidade.getDescricao().equals("")) {
                msgConfirma = "Digite uma Grupo por favor!";
            } else {
                if (db.idGrupoCidade(grupoCidade) == null) {
                    db.insert(grupoCidade);
                    msgConfirma = "Grupo salvo com Sucesso!";
                    log.novo("Novo registro", "Grupo Cidade inserido " + grupoCidade.getId() + " - " + grupoCidade.getDescricao());
                } else {
                    msgConfirma = "Este Grupo já existe no Sistema.";
                }
            }
        } else {
            GrupoCidade gc = new GrupoCidade();
            gc = db.pesquisaCodigo(grupoCidade.getId());
            String antes = "De: " + gc.getDescricao();
            db.getEntityManager().getTransaction().begin();
            if (db.update(grupoCidade)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Grupo atualizado com Sucesso!";
                log.novo("Atualizado", antes + " - para: " + grupoCidade.getId() + " - " + grupoCidade.getDescricao());
            } else {
                db.getEntityManager().getTransaction().rollback();
            }

        }
        return null;
    }

    public String novo() {
        grupoCidade = new GrupoCidade();
        msgConfirma = "";
        return "grupoCidade";
    }

    public String excluir() {
        GrupoCidadeDB db = new GrupoCidadeDBToplink();
        NovoLog log = new NovoLog();
        if (grupoCidade.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            grupoCidade = db.pesquisaCodigo(grupoCidade.getId());
            if (db.delete(grupoCidade)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Grupo Excluida com Sucesso!";
                log.novo("Excluido", grupoCidade.getId() + " - " + grupoCidade.getDescricao());
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Grupo não pode ser Excluido!";
            }

        }
        grupoCidade = new GrupoCidade();
        return null;
    }

    public List getListaGrupoCidade() {
        //Pesquisa pesquisa = new Pesquisa();
        GrupoCidadeDB db = new GrupoCidadeDBToplink();
        List result = null;
        //result = pesquisa.pesquisar("GrupoCidade", "descricao" , descPesquisa, "descricao", comoPesquisa);
        result = db.pesquisaTodos();
        return result;
    }

    public void refreshForm() {
    }

    /*public String pesquisarGrupoCidade(){
     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadGrupoCidade");
     return "pesquisaGrupoCidade";
     }*/

    /*public String pesquisarGrupoCidadeParaGrupo(){
     FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadGrupoCidades");
     return "pesquisaGrupoCidade";
     }*/
    public String editar() {
//       grupoCidade = (GrupoCidade) getHtmlTable().getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("grupoCidadePesquisa", grupoCidade);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        linkVoltar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (linkVoltar == null) {
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
            return "grupoCidade";
        } else //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
        {
            return linkVoltar;
        }
    }
    /*public String linkVoltar(){
     if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
     return "menuArrecadacao";
     }else
     return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
     }*/

    public String linkVoltarPesquisaGrupoCidade() {
        linkVoltar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (linkVoltar == null) {
            return "grupoCidade";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
        }
        return linkVoltar;
    }
}
