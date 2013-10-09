package br.com.rtools.utilitarios;

public class MenuLinks {

    private String link = "";
    private String descricao = "";
    private boolean ativo = false;

    public MenuLinks() {
        link = "";
        descricao = "";
        ativo = false;
    }

    public MenuLinks(String link, String descricao, boolean ativo) {
        this.link = link;
        this.descricao = descricao;
        this.ativo = ativo;
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
