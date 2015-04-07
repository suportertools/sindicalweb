/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.ConfiguracaoArrecadacao;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ConfiguracaoArrecadacaoBean {
    private ConfiguracaoArrecadacao configuracaoArrecadacao;
    
    @PostConstruct
    public void init() {
        Dao dao = new Dao();
        configuracaoArrecadacao = (ConfiguracaoArrecadacao) dao.find(new ConfiguracaoArrecadacao(), 1);
        if (configuracaoArrecadacao == null) {
            configuracaoArrecadacao = new ConfiguracaoArrecadacao();
            dao.save(configuracaoArrecadacao, true);
        }
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoArrecadacaoBean");
    }
    

    public void load() {

    }    
    
    public void update() {
        Dao dao = new Dao();
        if (configuracaoArrecadacao.getId() != -1) {
            if (dao.update(configuracaoArrecadacao, true)) {
                GenericaMensagem.info("Sucesso", "Configurações Aplicadas");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar este registro!");
            }
        }
    }       

    public ConfiguracaoArrecadacao getConfiguracaoArrecadacao() {
        return configuracaoArrecadacao;
    }

    public void setConfiguracaoArrecadacao(ConfiguracaoArrecadacao configuracaoArrecadacao) {
        this.configuracaoArrecadacao = configuracaoArrecadacao;
    }
}
