package br.com.rtools.sistema.beans;

import br.com.rtools.seguranca.Registro;
import br.com.rtools.sistema.ConfiguracaoCnpj;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
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
public class ConfiguracaoCnpjBean implements Serializable {

    private ConfiguracaoCnpj configuracaoCnpj;
    private Registro registro;

    @PostConstruct
    public void init() {
        Dao dao = new Dao();
        configuracaoCnpj = (ConfiguracaoCnpj) dao.find(new ConfiguracaoCnpj(), 1);
        if (configuracaoCnpj == null) {
            configuracaoCnpj = new ConfiguracaoCnpj();
            dao.save(configuracaoCnpj, true);
        }
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoCnpjBean");
    }

    public void update() {
        Dao dao = new Dao();
        if (configuracaoCnpj.getId() != -1) {
            if (dao.update(configuracaoCnpj, true)) {
                configuracaoCnpj.setDataAtualizacao(DataHoje.dataHoje());
                GenericaMensagem.info("Sucesso", "Configurações aplicadas");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar este registro!");
            }
        }
    }

    public ConfiguracaoCnpj getConfiguracaoCnpj() {
        return configuracaoCnpj;
    }

    public void setConfiguracaoCnpj(ConfiguracaoCnpj configuracaoCnpj) {
        this.configuracaoCnpj = configuracaoCnpj;
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

//    @Column(name = "is_cadastro_cnpj", columnDefinition = "boolean default false")
//    private boolean cadastroCnpj;

}
