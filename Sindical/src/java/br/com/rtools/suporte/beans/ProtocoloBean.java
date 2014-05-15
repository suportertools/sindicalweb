package br.com.rtools.suporte.beans;

import java.util.List;
import br.com.rtools.suporte.Protocolo;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ProtocoloBean {

    private Protocolo protocolo;
    private String comoPesquisa;
    private String descPesquisa;
    private String message;

    @PostConstruct
    public void init() {
        protocolo = new Protocolo();
        comoPesquisa = "";
        descPesquisa = "";
        message = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("protocoloBean");
    }

    public void save() {
        if (protocolo.getSolicitante().isEmpty()) {
            message = "Digite o a situação do histórico atual!";
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        if (protocolo.getId() == -1) {
            if (di.save(protocolo)) {
                di.commit();
                message = "Registro inserido com sucesso!";
            } else {
                di.rollback();
                message = "Erro ao inserir este registro!";
            }
        } else {
            if (di.update(protocolo)) {
                di.commit();
                message = "Registro atualizar com sucesso!";
            } else {
                di.rollback();
                message = "Erro ao atualizar este registro!";
            }
        }
        protocolo = new Protocolo();
    }

    public void delete() {
        if (protocolo.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(protocolo)) {
                di.commit();
                message = "Registro excluído com sucesso!";
            } else {
                di.rollback();
                message = "Erro ao excluir este registro!";
            }
        }
        protocolo = new Protocolo();
    }

    public String editar() {
        GenericaSessao.put("ordemProtocolo", protocolo);
        GenericaSessao.put("linkClicado", true);
        setDescPesquisa("");
        if (GenericaSessao.exists("urlRetorno")) {
            return "ordemServico";
        } else {
            return (String) GenericaSessao.getString("urlRetorno");
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
