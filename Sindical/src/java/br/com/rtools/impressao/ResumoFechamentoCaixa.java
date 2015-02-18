/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.impressao;

import java.math.BigDecimal;

/**
 *
 * @author rtools
 */
public class ResumoFechamentoCaixa {
    private String data;
    private String operacao;
    private String grupo;
    private String subgrupo;
    private String servico;
    private BigDecimal valor_baixa;

    public ResumoFechamentoCaixa(String data, String operacao, String grupo, String subgrupo, String servico, BigDecimal valor_baixa) {
        this.data = data;
        this.operacao = operacao;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.servico = servico;
        this.valor_baixa = valor_baixa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public BigDecimal getValor_baixa() {
        return valor_baixa;
    }

    public void setValor_baixa(BigDecimal valor_baixa) {
        this.valor_baixa = valor_baixa;
    }
    
    
}
