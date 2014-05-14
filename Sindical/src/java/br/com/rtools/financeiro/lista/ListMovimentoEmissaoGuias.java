package br.com.rtools.financeiro.lista;

import br.com.rtools.financeiro.Movimento;

public class ListMovimentoEmissaoGuias {

    private Movimento movimento;
    private String valor;
    private String desconto;
    private String total;

    public ListMovimentoEmissaoGuias() {
        this.movimento = new Movimento();
        this.valor = "0,00";
        this.desconto = "0,00";
        this.total = "0,00";
    }

    public ListMovimentoEmissaoGuias(Movimento movimento, String valor, String desconto, String total) {
        this.movimento = movimento;
        this.valor = valor;
        this.desconto = desconto;
        this.total = total;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
