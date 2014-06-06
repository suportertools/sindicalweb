package br.com.rtools.sistema.beans;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.Configuracao;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.sistema.db.ConfiguracaoDB;
import br.com.rtools.sistema.db.ConfiguracaoDBTopLink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Upload;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ConfiguracaoBean implements Serializable {

    private List<Configuracao> listaConfiguracao;
    private Configuracao configuracao;
    private String mensagem;
    private String descricaoPesquisa;
    private Juridica juridica;
    private Usuario usuario;

    @PostConstruct
    public void init() {
        listaConfiguracao = new ArrayList();
        configuracao = new Configuracao();
        mensagem = "";
        descricaoPesquisa = "";
        juridica = new Juridica();
        usuario = new Usuario();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoBean");
        GenericaSessao.remove("configuracaoPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("configuracaoBean");
    }

    public void save() {

        DaoInterface di = new Dao();

        configuracao.setJuridica(juridica);

        if (configuracao.getJuridica().getId() == -1) {
            setMensagem("Pesquisar pessoa jurídica!");
            return;
        }
        if (configuracao.getIdentifica().equals("")) {
            setMensagem("Informar o identificador do cliente, deve ser único!");
            return;
        }

        if (configuracao.getIdentifica().equals("")) {
            setMensagem("Informar o identificador do cliente, deve ser único!");
            return;
        }

        if (getConfiguracao().getId() == -1) {
            ConfiguracaoDB configuracaoDB = new ConfiguracaoDBTopLink();
            if (configuracaoDB.existeIdentificador(configuracao)) {
                setMensagem("Identificador já existe!");
                return;
            }

            if (configuracaoDB.existeIdentificadorPessoa(configuracao)) {
                setMensagem("Identificador já existe para essa pessoa!");
                return;
            }
            di.openTransaction();
            if (di.save(configuracao)) {
                di.commit();
                setMensagem("Configuração efetuada com sucesso");
            } else {
                di.rollback();
                setMensagem("Erro ao criar configuração.");
            }
        } else {
            di.openTransaction();
            if (di.update(configuracao)) {
                di.commit();
                setMensagem("Configuração atualizada com sucesso");
            } else {
                di.rollback();
                setMensagem("Erro ao atualizar configuração.");
            }
        }
    }

    public void delete() {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (getConfiguracao().getId() != -1) {
            if (di.delete((Configuracao) di.find(configuracao))) {
                di.commit();
                configuracao = new Configuracao();
                setMensagem("Configuração excluída com sucesso");
            } else {
                di.commit();
                setMensagem("Erro ao excluir configuração.");
            }
        }
    }

    public String edit(Configuracao c) {
        GenericaSessao.put("linkClicado", true);
        configuracao = c;
        juridica = configuracao.getJuridica();
        return "configuracao";
    }

    public List<Configuracao> getListaConfiguracao() {
        if (listaConfiguracao.isEmpty()) {
            if (!descricaoPesquisa.equals("")) {
                ConfiguracaoDB configuracaoDB = new ConfiguracaoDBTopLink();
                listaConfiguracao = (List<Configuracao>) configuracaoDB.listaConfiguracao(descricaoPesquisa);
            } else {
                DaoInterface di = new Dao();
                listaConfiguracao = (List<Configuracao>) di.list("Configuracao");
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
        if (GenericaSessao.exists("juridicaPesquisa")) {
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public void upload(FileUploadEvent event) {
        ConfiguracaoUpload cu = new ConfiguracaoUpload();
        cu.setArquivo(event.getFile().getFileName());
        cu.setDiretorio("Imagens");
        //cu.setArquivo("l");
        cu.setSubstituir(true);
        cu.setRenomear("LogoCliente" + ".png");
        cu.setEvent(event);
        Upload.enviar(cu, true);
    }

    public Usuario getUsuario() {
        usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
