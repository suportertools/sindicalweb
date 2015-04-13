package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ConfiguracaoFinanceiro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ConfiguracaoFinanceiroBean implements Serializable {

    private ConfiguracaoFinanceiro configuracaoFinanceiro;

    @PostConstruct
    public void init() {
        Dao dao = new Dao();
        configuracaoFinanceiro = (ConfiguracaoFinanceiro) dao.find(new ConfiguracaoFinanceiro(), 1);
        if (configuracaoFinanceiro == null) {
            configuracaoFinanceiro = new ConfiguracaoFinanceiro();
            dao.save(configuracaoFinanceiro, true);
        }
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoFinanceiroBean");
    }

    public void update() {
        Dao dao = new Dao();
        if (configuracaoFinanceiro.getId() != -1) {
            alterModalTransferencia();
            if (dao.update(configuracaoFinanceiro, true)) {
                GenericaMensagem.info("Sucesso", "Configurações Aplicadas");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar este registro!");
            }
        }
    }

    public void load() {

    }

    public void alterModalTransferencia() {
        if (!configuracaoFinanceiro.isTransferenciaAutomaticaCaixa()) {
            configuracaoFinanceiro.setModalTransferencia(false);
        }
    }

    public ConfiguracaoFinanceiro getConfiguracaoFinanceiro() {
        return configuracaoFinanceiro;
    }

    public void setConfiguracaoFinanceiro(ConfiguracaoFinanceiro configuracaoFinanceiro) {
        this.configuracaoFinanceiro = configuracaoFinanceiro;
    }
}
