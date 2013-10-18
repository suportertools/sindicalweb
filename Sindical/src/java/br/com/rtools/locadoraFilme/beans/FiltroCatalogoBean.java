package br.com.rtools.locadoraFilme.beans;
//package org.richfaces.demo.filteringFeature;
import br.com.rtools.locadoraFilme.Catalogo;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class FiltroCatalogoBean {

    private String filtroDescricao = "5";
    private String filtroValor = "";
    private ArrayList<SelectItem> filtrarDescricoes = new ArrayList<SelectItem>();

    public boolean filtrarDescricao(Object current) {
        Catalogo catalogo = (Catalogo) current;
        if (getFiltroValor().length() == 0) {
            return true;
        }
        if (catalogo.getTitulo().getDescricao().toLowerCase().startsWith(getFiltroValor().toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public FiltroCatalogoBean() {
        for (int i = 5; i < 11; i++) {
            SelectItem select = new SelectItem();
            select.setLabel("-" + i);
            select.setValue(i);
            filtrarDescricoes.add(select);
        }
    }

    public String getFiltroDescricao() {
        return filtroDescricao;
    }

    public void setFiltroDescricao(String filtroDescricao) {
        this.filtroDescricao = filtroDescricao;
    }

    public String getFiltroValor() {
        return filtroValor;
    }

    public void setFiltroValor(String filtroValor) {
        this.filtroValor = filtroValor;
    }

    public ArrayList<SelectItem> getFiltrarDescricoes() {
        return filtrarDescricoes;
    }

    public void setFiltrarDescricoes(ArrayList<SelectItem> filtrarDescricoes) {
        this.filtrarDescricoes = filtrarDescricoes;
    }
}