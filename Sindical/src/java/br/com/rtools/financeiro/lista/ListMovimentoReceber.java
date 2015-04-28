package br.com.rtools.financeiro.lista;

import java.io.Serializable;

public class ListMovimentoReceber implements Serializable {

    private boolean selected;
    private String boleto;
    private String servico;
    private String tipo;
    private String referencia;
    private String vencimento;
    private String valorMovimento;
    private String valorFolha;
    private String multa;
    private String juros;
    private String correcao;
    private String desconto;
    private String valorCalculado;
    private String mesesAtraso;
    private String diasAtraso;
    private String indice;
    private String idMovimento;
    private String valorCalculadoOriginal;

    public ListMovimentoReceber() {
        this.selected = false;
        this.boleto = "";
        this.servico = "";
        this.tipo = "";
        this.referencia = "";
        this.vencimento = "";
        this.valorMovimento = "";
        this.valorFolha = "";
        this.multa = "";
        this.juros = "";
        this.correcao = "";
        this.desconto = "";
        this.valorCalculado = "";
        this.mesesAtraso = "";
        this.diasAtraso = "";
        this.indice = "";
        this.idMovimento = "";
        this.valorCalculadoOriginal = "";
    }

    public ListMovimentoReceber(boolean selected, String boleto, String servico, String tipo, String referencia, String vencimento, String valorMovimento, String valorFolha, String multa, String juros, String correcao, String desconto, String valorCalculado, String mesesAtraso, String diasAtraso, String indice, String idMovimento, String valorCalculadoOriginal) {
        this.selected = selected;
        this.boleto = boleto;
        this.servico = servico;
        this.tipo = tipo;
        this.referencia = referencia;
        this.vencimento = vencimento;
        this.valorMovimento = valorMovimento;
        this.valorFolha = valorFolha;
        this.multa = multa;
        this.juros = juros;
        this.correcao = correcao;
        this.desconto = desconto;
        this.valorCalculado = valorCalculado;
        this.mesesAtraso = mesesAtraso;
        this.diasAtraso = diasAtraso;
        this.indice = indice;
        this.idMovimento = idMovimento;
        this.valorCalculadoOriginal = valorCalculadoOriginal;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getValorMovimento() {
        return valorMovimento;
    }

    public void setValorMovimento(String valorMovimento) {
        this.valorMovimento = valorMovimento;
    }

    public String getValorFolha() {
        return valorFolha;
    }

    public void setValorFolha(String valorFolha) {
        this.valorFolha = valorFolha;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(String valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public String getMesesAtraso() {
        return mesesAtraso;
    }

    public void setMesesAtraso(String mesesAtraso) {
        this.mesesAtraso = mesesAtraso;
    }

    public String getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(String diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(String idMovimento) {
        this.idMovimento = idMovimento;
    }

    public String getValorCalculadoOriginal() {
        return valorCalculadoOriginal;
    }

    public void setValorCalculadoOriginal(String valorCalculadoOriginal) {
        this.valorCalculadoOriginal = valorCalculadoOriginal;
    }

}
