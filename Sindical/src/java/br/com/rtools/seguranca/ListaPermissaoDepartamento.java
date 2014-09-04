package br.com.rtools.seguranca;

public class ListaPermissaoDepartamento {

    private PermissaoDepartamento permissaoDepartamento;
    private Permissao permissao;
    private boolean selected;

    public ListaPermissaoDepartamento() {
        this.permissaoDepartamento = new PermissaoDepartamento();
        this.permissao = new Permissao();
        this.selected = false;
    }

    public ListaPermissaoDepartamento(PermissaoDepartamento permissaoDepartamento, Permissao permissao, boolean selected) {
        this.permissaoDepartamento = permissaoDepartamento;
        this.permissao = permissao;
        this.selected = selected;
    }

    public PermissaoDepartamento getPermissaoDepartamento() {
        return permissaoDepartamento;
    }

    public void setPermissaoDepartamento(PermissaoDepartamento permissaoDepartamento) {
        this.permissaoDepartamento = permissaoDepartamento;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
