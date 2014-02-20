package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroConviteClube {

    private String convidado;
    private String socio;
    private String diretor;
    private String operador;
    private String emissao;
    private String validade;
    private String dataPagto;
    private BigDecimal valor;
    private BigDecimal valorPago;
    private String cortesia;
    private String obs;

    public ParametroConviteClube(String convidado, String socio, String diretor, String operador, String emissao, String validade, String dataPagto, BigDecimal valor, BigDecimal valorPago, String cortesia, String obs) {
        this.convidado = convidado;
        this.socio = socio;
        this.diretor = diretor;
        this.operador = operador;
        this.emissao = emissao;
        this.validade = validade;
        this.dataPagto = dataPagto;
        this.valor = valor;
        this.valorPago = valorPago;
        this.cortesia = cortesia;
        this.obs = obs;
    }

    public String getConvidado() {
        return convidado;
    }

    public void setConvidado(String convidado) {
        this.convidado = convidado;
    }

    public String getSocio() {
        return socio;
    }

    public void setSocio(String socio) {
        this.socio = socio;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getDataPagto() {
        return dataPagto;
    }

    public void setDataPagto(String dataPagto) {
        this.dataPagto = dataPagto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public String getCortesia() {
        return cortesia;
    }

    public void setCortesia(String cortesia) {
        this.cortesia = cortesia;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
