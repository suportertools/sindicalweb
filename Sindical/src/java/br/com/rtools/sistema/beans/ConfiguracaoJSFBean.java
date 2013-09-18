package br.com.rtools.sistema.beans;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.sistema.Configuracao;
import br.com.rtools.sistema.db.ConfiguracaoDB;
import br.com.rtools.sistema.db.ConfiguracaoDBTopLink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class ConfiguracaoJSFBean implements java.io.Serializable {

    private List<Configuracao> listaConfiguracao = new ArrayList();
    private Configuracao configuracao = new Configuracao();
    private String mensagem = "";
    private String descricaoPesquisa = "";
    private Juridica juridica = new Juridica();

    public String salvar() {

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        configuracao.setJuridica(juridica);
        
        if (configuracao.getJuridica().getId() == -1) {
            setMensagem("Pesquisar pessoa jurídica!");
            return null;
        }
        if (configuracao.getIdentifica().equals("")) {
            setMensagem("Informar o identificador do cliente, deve ser único!");
            return null;
        }


        if (configuracao.getIdentifica().equals("")) {
            setMensagem("Informar o identificador do cliente, deve ser único!");
            return null;
        }

        if (getConfiguracao().getId() == -1) {
            ConfiguracaoDB configuracaoDB = new ConfiguracaoDBTopLink();
            if (configuracaoDB.existeIdentificador(configuracao)) {
                setMensagem("Identificador já existe!");
                return null;
            }


            if (configuracaoDB.existeIdentificadorPessoa(configuracao)) {
                setMensagem("Identificador já existe para essa pessoa!");
                return null;
            }            
            sv.abrirTransacao();
            if (sv.inserirObjeto(configuracao)) {
                sv.comitarTransacao();
                setMensagem("Configuração efetuada com sucesso");
            } else {
                sv.desfazerTransacao();
                setMensagem("Erro ao criar configuração.");
            }
        } else {
            sv.abrirTransacao();
            if (sv.alterarObjeto(configuracao)) {
                sv.comitarTransacao();
                setMensagem("Configuração atualizada com sucesso");
            } else {
                sv.desfazerTransacao();
                setMensagem("Erro ao atualizar configuração.");
            }
        }
        return "configuracao";
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (getConfiguracao().getId() != -1) {
            if (sv.deletarObjeto((Configuracao) sv.pesquisaCodigo(configuracao.getId(), "Configuracao"))) {
                sv.comitarTransacao();
                setMensagem("Configuração excluída com sucesso");
            } else {
                sv.desfazerTransacao();
                setMensagem("Erro ao excluir configuração.");
            }

        }
        return "configuracao";
    }

    public String editar(Configuracao configuracao1) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("configuracaoPesquisa", configuracao1);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "configuracao";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String novo() {
        configuracao = new Configuracao();
        descricaoPesquisa = "";
        mensagem = "";
        juridica = new Juridica();
        return "configuracao";
    }

    public List<Configuracao> getListaConfiguracao() {
        if (listaConfiguracao.isEmpty()) {
            if (!descricaoPesquisa.equals("")) {
                ConfiguracaoDB configuracaoDB = new ConfiguracaoDBTopLink();
                listaConfiguracao = (List<Configuracao>) configuracaoDB.listaConfiguracao(descricaoPesquisa);
            } else {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                listaConfiguracao = (List<Configuracao>) salvarAcumuladoDB.listaObjeto("Configuracao");
            }
        }
        return listaConfiguracao;
    }

    public void limparListaConfiguracao() {
        listaConfiguracao.clear();
    }

    public void setListaConfiguracao(List<Configuracao> listaConfiguracao) {
        this.listaConfiguracao = listaConfiguracao;
    }

    public Configuracao getConfiguracao() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("configuracaoPesquisa") != null) {
            configuracao = (Configuracao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("configuracaoPesquisa");
            juridica = configuracao.getJuridica();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("configuracaoPesquisa");
        }        
        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }
}
