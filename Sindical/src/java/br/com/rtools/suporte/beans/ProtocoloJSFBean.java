package br.com.rtools.suporte.beans;

import br.com.rtools.suporte.Protocolo;
import br.com.rtools.suporte.db.ProtocoloDB;
import br.com.rtools.suporte.db.ProtocoloDBToplink;
import java.util.List;
import javax.faces.context.FacesContext;

public class ProtocoloJSFBean {

    private Protocolo protocolo;
    private String comoPesquisa;
    private String descPesquisa;
    private String msgConfirma;

    public ProtocoloJSFBean() {
        protocolo = new Protocolo();
        comoPesquisa = "";
        descPesquisa = "";
    }

    public String novo() {
        setProtocolo(new Protocolo());
        return "protocolo";
    }

    public String salvar() {
        ProtocoloDB protocoloDB = new ProtocoloDBToplink();
        if (getProtocolo().getId() == -1) {
            if (getProtocolo().getSolicitante().equals("")) {
                setMsgConfirma("Digite o a situação do histórico atual!");
            } else {
                if (protocoloDB.pesquisaCodigo(getProtocolo().getId()) == null) {
                    if (protocoloDB.insert(getProtocolo())) {
                        setMsgConfirma("Cadastro efetuado com sucesso!");
                    } else {
                        setMsgConfirma("Erro! Cadastro não foi efetuado.");
                    }
                } else {
                    setMsgConfirma("Já existe um componente curricular com esse nome.");
                }
            }
        } else {
            if (protocoloDB.update(getProtocolo())) {
                setMsgConfirma("Cadastro atualizado com sucesso!");
            } else {
            }
        }
        setProtocolo(new Protocolo());
        return null;
    }

    public String excluir() {
        ProtocoloDB protocoloDB = new ProtocoloDBToplink();
        if (getProtocolo().getId() != -1) {
            setProtocolo(protocoloDB.pesquisaCodigo(getProtocolo().getId()));
            if (protocoloDB.delete(getProtocolo())) {
                setMsgConfirma("Cadastro excluído com sucesso!");
            } else {
                setMsgConfirma("Erro! Cadastro não foi excluído.");
            }
        } else {
            setMsgConfirma("Não há registro para excluir.");
        }
        setProtocolo(new Protocolo());
        return null;
    }

    public String editar() {
//        setProtocolo((Protocolo) getHtmlTable().getRowData());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ordemProtocolo", getProtocolo());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        setDescPesquisa("");
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "ordemServico";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public void acaoPesquisaInicial() {
        setComoPesquisa("I");
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("P");
    }

    public List getListaOrdemServico() {
//        Pesquisa pesquisa = new Pesquisa();
        List result = null;
//        result = pesquisa.pesquisar("OrdemServico", "descricao" , getDescPesquisa(), "descricao", getComoPesquisa());
        return result;
    }

    public void refreshForm() {
    }

    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}
