package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConfiguracaoSocial;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@ViewScoped
public class ConfiguracaoSocialBean implements Serializable {
    
    private ConfiguracaoSocial configuracaoSocial;
    
    private int idGrupoCategoria;
    private List<SelectItem> listaGrupoCategoria;
    
    @PostConstruct
    public void init() {
        idGrupoCategoria = 0;
        listaGrupoCategoria = new ArrayList();
        
        loadGrupoCategoria();
        
        Dao dao = new Dao();
        configuracaoSocial = (ConfiguracaoSocial) dao.find(new ConfiguracaoSocial(), 1);
        if (configuracaoSocial == null) {
            configuracaoSocial = new ConfiguracaoSocial();
            dao.save(configuracaoSocial, true);
        }
        
        if (configuracaoSocial.getGrupoCategoriaInativaDemissionado() == null || (listaGrupoCategoria.size() == 1)){
            idGrupoCategoria = 0;
        }else{
            for(int i = 0; i < listaGrupoCategoria.size(); i++){
                if (configuracaoSocial.getGrupoCategoriaInativaDemissionado().getId() == Integer.valueOf(listaGrupoCategoria.get(i).getDescription()) ){
                    idGrupoCategoria = i;
                }
            }
        }
    }
    
    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoSocialBean");
    }    

    public void update() {
        Dao dao = new Dao();
        if (configuracaoSocial.getId() != -1) {
            if (configuracaoSocial.getCartaoPosicaoCodigo() > 12){
                GenericaMensagem.error("Atenção", "Posição máxima para a via é 12 !");
                return;
            }
            
            if (configuracaoSocial.getCartaoPosicaoCodigo() > 6){
                GenericaMensagem.error("Atenção", "Posição máxima para o código é 6 !");
                return;
            }
            
            if (Integer.valueOf(listaGrupoCategoria.get(idGrupoCategoria).getDescription()) == 0){
                configuracaoSocial.setGrupoCategoriaInativaDemissionado(null);
            }else{
                configuracaoSocial.setGrupoCategoriaInativaDemissionado((GrupoCategoria) new Dao().find(new GrupoCategoria(), Integer.valueOf(listaGrupoCategoria.get(idGrupoCategoria).getDescription())));
            }
            
            if (dao.update(configuracaoSocial, true)) {
                GenericaMensagem.info("Sucesso", "Configurações Aplicadas");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar este registro!");
            }
        }
    }    

    public ConfiguracaoSocial getConfiguracaoSocial() {
        return configuracaoSocial;
    }

    public void setConfiguracaoSocial(ConfiguracaoSocial configuracaoSocial) {
        this.configuracaoSocial = configuracaoSocial;
    }
    
    
    public void load() {

    }

    public void loadGrupoCategoria(){
        listaGrupoCategoria.clear();
        idGrupoCategoria = 0;
        
        listaGrupoCategoria.add(new SelectItem(0, "Selecione um Grupo Categoria", "0"));
        
        List<GrupoCategoria> grupoCategorias = (List<GrupoCategoria>) new Dao().list("GrupoCategoria");
        
        if (!grupoCategorias.isEmpty()) {
            for (int i = 0; i < grupoCategorias.size(); i++) {
                listaGrupoCategoria.add(new SelectItem(i+1, grupoCategorias.get(i).getGrupoCategoria(), "" + grupoCategorias.get(i).getId()));
            }
        } else {
            listaGrupoCategoria.add(new SelectItem(0, "Nenhum Grupo Categoria Encontrado", "0"));
        }        
    }
        
    public int getIdGrupoCategoria() {
        return idGrupoCategoria;
    }

    public void setIdGrupoCategoria(int idGrupoCategoria) {
        this.idGrupoCategoria = idGrupoCategoria;
    }

    public List<SelectItem> getListaGrupoCategoria() {
        return listaGrupoCategoria;
    }

    public void setListaGrupoCategoria(List<SelectItem> listaGrupoCategoria) {
        this.listaGrupoCategoria = listaGrupoCategoria;
    }

}
