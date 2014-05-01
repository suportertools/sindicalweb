package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroChequesRecebidos {
    private String filial;//(** todas ou específica (nome fantasia da filial) **)
    private String emissao;
    private String vencimento;
    private String banco;
    private String agencia;
    private String conta;
    private String cheque;
    private BigDecimal valor;
    private String id_baixa;
    private String caixa; // (número do caixa e operadores) todos ou especidifcar qual caixa

    public ParametroChequesRecebidos() {
        this.filial = "";
        this.emissao = "";
        this.vencimento = "";
        this.banco = "";
        this.agencia = "";
        this.conta = "";
        this.cheque = "";
        this.valor = new BigDecimal(0);
        this.id_baixa = "";
        this.caixa = "";
    }
    
    public ParametroChequesRecebidos(String filial, String emissao, String vencimento, String banco, String agencia, String conta, String cheque, BigDecimal valor, String id_baixa, String caixa) {
        this.filial = filial;
        this.emissao = emissao;
        this.vencimento = vencimento;
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.cheque = cheque;
        this.valor = valor;
        this.id_baixa = id_baixa;
        this.caixa = caixa;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getId_baixa() {
        return id_baixa;
    }

    public void setId_baixa(String id_baixa) {
        this.id_baixa = id_baixa;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }
}
