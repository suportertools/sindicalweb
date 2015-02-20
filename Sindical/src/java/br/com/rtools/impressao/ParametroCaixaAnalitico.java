package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroCaixaAnalitico {
    private String caixa;
    private String dt_baixa;
    private String lote_baixa;
    private String operador;
    private String responsavel;
    private String titular;
    private String beneficiario;
    private String servico;
    private String operacao;
    private BigDecimal valor;
    private BigDecimal valor_baixa;
    private String dt_fechamento;
    
    public ParametroCaixaAnalitico(String caixa, String dt_baixa, String lote_baixa, String operador, String responsavel, String titular, String beneficiario, String servico, String operacao, BigDecimal valor, BigDecimal valor_baixa, String dt_fechamento) {
        this.caixa = caixa;
        this.dt_baixa = dt_baixa;
        this.lote_baixa = lote_baixa;
        this.operador = operador;
        this.responsavel = responsavel;
        this.titular = titular;
        this.beneficiario = beneficiario;
        this.servico = servico;
        this.operacao = operacao;
        this.valor = valor;
        this.valor_baixa = valor_baixa;
        this.dt_fechamento = dt_fechamento;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getDt_baixa() {
        return dt_baixa;
    }

    public void setDt_baixa(String dt_baixa) {
        this.dt_baixa = dt_baixa;
    }

    public String getLote_baixa() {
        return lote_baixa;
    }

    public void setLote_baixa(String lote_baixa) {
        this.lote_baixa = lote_baixa;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor_baixa() {
        return valor_baixa;
    }

    public void setValor_baixa(BigDecimal valor_baixa) {
        this.valor_baixa = valor_baixa;
    }

    public String getDt_fechamento() {
        return dt_fechamento;
    }

    public void setDt_fechamento(String dt_fechamento) {
        this.dt_fechamento = dt_fechamento;
    }
}
