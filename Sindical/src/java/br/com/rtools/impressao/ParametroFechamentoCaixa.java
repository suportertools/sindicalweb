package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroFechamentoCaixa {
    private int caixa; 
    private int nr_filial; 
    private String filial; 
    private String usuarios; 
    private String sinal; 
    private String descricao; 
    private BigDecimal valor; 
    private int qtde_cheque; 
    private BigDecimal valor_total_cheque; 

    public ParametroFechamentoCaixa(int caixa, int nr_filial, String filial, String usuarios, String sinal, String descricao, BigDecimal valor, int qtde_cheque, BigDecimal valor_total_cheque) {
        this.caixa = caixa;
        this.nr_filial = nr_filial;
        this.filial = filial;
        this.usuarios = usuarios;
        this.sinal = sinal;
        this.descricao = descricao;
        this.valor = valor;
        this.qtde_cheque = qtde_cheque;
        this.valor_total_cheque = valor_total_cheque;
    }
    
    public int getCaixa() {
        return caixa;
    }

    public void setCaixa(int caixa) {
        this.caixa = caixa;
    }

    public int getNr_filial() {
        return nr_filial;
    }

    public void setNr_filial(int nr_filial) {
        this.nr_filial = nr_filial;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(String usuarios) {
        this.usuarios = usuarios;
    }

    public String getSinal() {
        return sinal;
    }

    public void setSinal(String sinal) {
        this.sinal = sinal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getQtde_cheque() {
        return qtde_cheque;
    }

    public void setQtde_cheque(int qtde_cheque) {
        this.qtde_cheque = qtde_cheque;
    }

    public BigDecimal getValor_total_cheque() {
        return valor_total_cheque;
    }

    public void setValor_total_cheque(BigDecimal valor_total_cheque) {
        this.valor_total_cheque = valor_total_cheque;
    }
    
    
    
}
