package br.com.rtools.financeiro.lista;

import br.com.rtools.financeiro.PrevisaoPagamento;

public class ListaPrevisaoPagamento {

    private PrevisaoPagamento previsaoPagamento;

    public ListaPrevisaoPagamento() {
        this.previsaoPagamento = new PrevisaoPagamento();
    }

    public ListaPrevisaoPagamento(PrevisaoPagamento previsaoPagamento) {
        this.previsaoPagamento = previsaoPagamento;
    }

    public PrevisaoPagamento getPrevisaoPagamento() {
        return previsaoPagamento;
    }

    public void setPrevisaoPagamento(PrevisaoPagamento previsaoPagamento) {
        this.previsaoPagamento = previsaoPagamento;
    }

}
