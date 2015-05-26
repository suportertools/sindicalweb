/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

@ManagedBean(name = "tabbedBean")
@SessionScoped
public class Tabbed {

    private String title;
    private String activeIndex;
    private Boolean dinamic;

    @PostConstruct
    public void init() {
        title = "";
        activeIndex = "";
    }

    @PreDestroy
    public void destroy() {
        title = "";
    }

    public void onTabChange(TabChangeEvent event) {
        event.getTab().getTitle();
    }

    public void onTabClose(TabCloseEvent event) {
        title = event.getTab().getTitle();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }
}
