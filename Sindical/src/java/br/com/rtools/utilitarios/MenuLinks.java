package br.com.rtools.utilitarios;

public class MenuLinks {

    private int indice = 0;
    private String link = "";
    private String descricao = "";
    private boolean ativo = false;

    public MenuLinks() {
        indice = 0;
        link = "";
        descricao = "";
        ativo = false;
    }

    public MenuLinks(int indice, String link, String descricao, boolean ativo) {
        this.indice = indice;
        this.link = link;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
