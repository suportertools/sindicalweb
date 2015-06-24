package br.com.rtools.impressao;

import java.io.Serializable;

public class ParametroFechamentoRepasse implements Serializable {
    private String servico;
    private Integer idPessoa;
    private String nome;
    private String vencimento;
    private String baixa;
    private float valorBaixa;

    public ParametroFechamentoRepasse(String servico, Integer idPessoa, String nome, String vencimento, String baixa, float valorBaixa) {
        this.servico = servico;
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.vencimento = vencimento;
        this.baixa = baixa;
        this.valorBaixa = valorBaixa;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getBaixa() {
        return baixa;
    }

    public void setBaixa(String baixa) {
        this.baixa = baixa;
    }

    public float getValorBaixa() {
        return valorBaixa;
    }

    public void setValorBaixa(float valorBaixa) {
        this.valorBaixa = valorBaixa;
    }
}
