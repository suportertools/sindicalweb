package br.com.rtools.impressao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ParametroFechamentoGuias implements Serializable {

    private String empresa_nome;
    private Integer guia_id;
    private Integer baixa_id;
    private String servico_descricao;
    private Integer quantidade;
    private String atendente_nome;
    private String beneficiario_nome;
    private Date data_pagto;
    private BigDecimal servico_valor;

    public ParametroFechamentoGuias(String empresa_nome, Integer guia_id, Integer baixa_id, String servico_descricao, Integer quantidade, String atendente_nome, String beneficiario_nome, Date data_pagto, BigDecimal servico_valor) {
        this.empresa_nome = empresa_nome;
        this.guia_id = guia_id;
        this.baixa_id = baixa_id;
        this.servico_descricao = servico_descricao;
        this.quantidade = quantidade;
        this.atendente_nome = atendente_nome;
        this.beneficiario_nome = beneficiario_nome;
        this.data_pagto = data_pagto;
        this.servico_valor = servico_valor;
    }

    public String getEmpresa_nome() {
        return empresa_nome;
    }

    public void setEmpresa_nome(String empresa_nome) {
        this.empresa_nome = empresa_nome;
    }

    public Integer getGuia_id() {
        return guia_id;
    }

    public void setGuia_id(Integer guia_id) {
        this.guia_id = guia_id;
    }

    public Integer getBaixa_id() {
        return baixa_id;
    }

    public void setBaixa_id(Integer baixa_id) {
        this.baixa_id = baixa_id;
    }

    public String getServico_descricao() {
        return servico_descricao;
    }

    public void setServico_descricao(String servico_descricao) {
        this.servico_descricao = servico_descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getAtendente_nome() {
        return atendente_nome;
    }

    public void setAtendente_nome(String atendente_nome) {
        this.atendente_nome = atendente_nome;
    }

    public String getBeneficiario_nome() {
        return beneficiario_nome;
    }

    public void setBeneficiario_nome(String beneficiario_nome) {
        this.beneficiario_nome = beneficiario_nome;
    }

    public Date getData_pagto() {
        return data_pagto;
    }

    public void setData_pagto(Date data_pagto) {
        this.data_pagto = data_pagto;
    }

    public BigDecimal getServico_valor() {
        return servico_valor;
    }

    public void setServico_valor(BigDecimal servico_valor) {
        this.servico_valor = servico_valor;
    }

}
