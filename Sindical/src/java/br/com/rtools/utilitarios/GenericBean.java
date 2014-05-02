package br.com.rtools.utilitarios;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
// @ViewScoped - Utilizar em relatórios simples que não necessitem de consultas em outros beans/páginas
public class GenericBean implements Serializable {

    Object object;

    /**
     * Pode iniciar o bean utilizando os métodos init e esstroy eliminando a
     * necessidade de ficar apagamdndo as sessões na chamada de página bean;
     */
    @PostConstruct
    public void init() {
        // object = new Object();
    }

    @PreDestroy
    public void destroy() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("genericBean");
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuarioBean");
    }

    /**
     * ou iniciar o bean utilizando o construtor sem parâmetros
     */
    public GenericBean() {
        //object = new Object();
    }

    /**
     * Utilizar o clear para limpar a sessão do bean atual
     */
    public void clear() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("genericBean");
    }

    /**
     * Utilizar métodos de fácil entendimento para os eventos
     */
    public void save() {
    }

    public void update() {
    }

    public void edit() {
        // Se utilizar indices, array....
    }

    public void edit(Object o) {
    }

    public void edit(Integer id) {
    }

    public void delete() {
    }

    public void addItem() {
    }

    public void removeItem() {
    }

}
