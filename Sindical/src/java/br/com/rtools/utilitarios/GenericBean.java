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
     * PODE INICIAR O BEAN UTILIZANDO OS MÉTODOS INIT E DESTROY, ELIMINANDO A
     * NECESSIDADE DE FICAR APAGANDO AS SESSÕES NA CHAMADA DE PÁGINA
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
     * OU INICIAR O BEAN UTILIZANDO O CONSTRUTOR SEM PARÂMETROS
     */
    public GenericBean() {
        //object = new Object();
    }

    /**
     * UTILIZAR O CLEAR PARA LIMPAR A SESSÃO DO BEAN ATUAL
     */
    public void clear() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("genericBean");
    }

    /**
     * UTILIZAR MÉTODOS DE FÁCIL ENTENDIMENTO PARA OS EVENTOS
     */
    public void save() {
    }

    public void update() {
    }

    public void edit() {
        // SE UTILIZAR INDICE
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
