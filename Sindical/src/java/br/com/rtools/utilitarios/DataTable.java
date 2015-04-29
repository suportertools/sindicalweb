package br.com.rtools.utilitarios;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "dataTableBean")
@SessionScoped
public class DataTable {

    private Integer page;

    public DataTable() {
        this.page = 0;
    }

    public DataTable(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
