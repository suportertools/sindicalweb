package br.com.rtools.financeiro.lista;

import br.com.rtools.financeiro.Cartao;
import br.com.rtools.financeiro.ChequePag;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.TipoPagamento;

public class ListValoresBaixaGeral {
    private String vencimento;
    private String valor;
    private String numeroCheque;
    private TipoPagamento tipoPagamento;
    private ChequePag chequePag;
    private ChequeRec chequeRec;
    private Plano5 plano5;
    private Cartao cartao;
    private String valorDigitado;

    public ListValoresBaixaGeral(String vencimento, String valor, String numeroCheque, TipoPagamento tipoPagamento, ChequePag chequePag, ChequeRec chequeRec, Plano5 plano5, Cartao cartao, String valorDigitado) {
        this.vencimento = vencimento;
        this.valor = valor;
        this.numeroCheque = numeroCheque;
        this.tipoPagamento = tipoPagamento;
        this.chequePag = chequePag;
        this.chequeRec = chequeRec;
        this.plano5 = plano5;
        this.cartao = cartao;
        this.valorDigitado = valorDigitado;
    }
    
    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public ChequePag getChequePag() {
        return chequePag;
    }

    public void setChequePag(ChequePag chequePag) {
        this.chequePag = chequePag;
    }

    public ChequeRec getChequeRec() {
        return chequeRec;
    }

    public void setChequeRec(ChequeRec chequeRec) {
        this.chequeRec = chequeRec;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
    
    public String getValorDigitado() {
        return valorDigitado;
    }

    public void setValorDigitado(String valorDigitado) {
        this.valorDigitado = valorDigitado;
    }
}
