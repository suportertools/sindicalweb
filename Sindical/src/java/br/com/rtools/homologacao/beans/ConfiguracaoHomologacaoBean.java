package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.ConfiguracaoHomologacao;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@ViewScoped
public class ConfiguracaoHomologacaoBean implements Serializable {

    private ConfiguracaoHomologacao configuracaoHomologacao;
    private Registro registro;

    @PostConstruct
    public void init() {
        Dao dao = new Dao();
        configuracaoHomologacao = (ConfiguracaoHomologacao) dao.find(new ConfiguracaoHomologacao(), 1);
        if (configuracaoHomologacao == null) {
            configuracaoHomologacao = new ConfiguracaoHomologacao();
            dao.save(configuracaoHomologacao, true);
        }
        if (registro == null) {
            registro = (Registro) dao.find(new Registro(), 1);
        }
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoHomologacaoBean");
    }

    public void update() {
        Dao dao = new Dao();
        if (configuracaoHomologacao.getId() != -1) {
            if (dao.update(configuracaoHomologacao, true)) {
                GenericaMensagem.info("Sucesso", "Configurações aplicadas");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar este registro!");
            }
        }
    }

    public ConfiguracaoHomologacao getConfiguracaoHomologacao() {
        return configuracaoHomologacao;
    }

    public void setConfiguracaoHomologacao(ConfiguracaoHomologacao configuracaoHomologacao) {
        this.configuracaoHomologacao = configuracaoHomologacao;
    }

    public void load() {

    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public void onChange(TabChangeEvent event) {
        Tab activeTab = event.getTab();
    }

}
