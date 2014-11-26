package br.com.rtools.impressao;

import java.math.BigDecimal;
import java.util.Date;

public class ParametroCertificado {

    private String patNome;
    private String patLogo;
    private String patBaseTerr;
    private String sinNome;
    private String sinLogo;
    private String empNome;
    private String empDoc;
    private String empPorte;
    private String descPisoSal;
    private BigDecimal valorPisoSal;
    private String mensagem;
    private String validade;
    private String sinCidade;
    private int ano;
    private String patLogoSelo;
    private String patLogoFundo;
    private String certificado;
    private String barras;
    private String emissao;
    private String empEndereco;
    private String periodoConvencao;
    private String imagemFundo;

    public ParametroCertificado(String patNome, String patLogo, String patBaseTerr, String sinNome, String sinLogo, String empNome, String empDoc, String empPorte, String descPisoSal, BigDecimal valorPisoSal, String mensagem,
            String validade, String sinCidade, int ano, String patLogoSelo, String patLogoFundo, String certificado, String barras, String emissao, String empEndereco, String periodoConvencao, String imagemFundo) {
        this.patNome = patNome;
        this.patLogo = patLogo;
        this.patBaseTerr = patBaseTerr;
        this.sinNome = sinNome;
        this.sinLogo = sinLogo;
        this.empNome = empNome;
        this.empDoc = empDoc;
        this.empPorte = empPorte;
        this.descPisoSal = descPisoSal;
        this.valorPisoSal = valorPisoSal;
        this.mensagem = mensagem;
        this.validade = validade;
        this.sinCidade = sinCidade;
        this.ano = ano;
        this.patLogoSelo = patLogoSelo;
        this.patLogoFundo = patLogoFundo;
        this.certificado = certificado;
        this.barras = barras;
        this.emissao = emissao;
        this.empEndereco = empEndereco;
        this.periodoConvencao = periodoConvencao;
        this.imagemFundo = imagemFundo;
        
    }

    public String getPatNome() {
        return patNome;
    }

    public void setPatNome(String patNome) {
        this.patNome = patNome;
    }

    public String getPatLogo() {
        return patLogo;
    }

    public void setPatLogo(String patLogo) {
        this.patLogo = patLogo;
    }

    public String getPatBaseTerr() {
        return patBaseTerr;
    }

    public void setPatBaseTerr(String patBaseTerr) {
        this.patBaseTerr = patBaseTerr;
    }

    public String getSinNome() {
        return sinNome;
    }

    public void setSinNome(String sinNome) {
        this.sinNome = sinNome;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public String getEmpNome() {
        return empNome;
    }

    public void setEmpNome(String empNome) {
        this.empNome = empNome;
    }

    public String getEmpDoc() {
        return empDoc;
    }

    public void setEmpDoc(String empDoc) {
        this.empDoc = empDoc;
    }

    public String getEmpPorte() {
        return empPorte;
    }

    public void setEmpPorte(String empPorte) {
        this.empPorte = empPorte;
    }

    public String getDescPisoSal() {
        return descPisoSal;
    }

    public void setDescPisoSal(String descPisoSal) {
        this.descPisoSal = descPisoSal;
    }

    public BigDecimal getValorPisoSal() {
        return valorPisoSal;
    }

    public void setValorPisoSal(BigDecimal valorPisoSal) {
        this.valorPisoSal = valorPisoSal;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getSinCidade() {
        return sinCidade;
    }

    public void setSinCidade(String sinCidade) {
        this.sinCidade = sinCidade;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPatLogoSelo() {
        return patLogoSelo;
    }

    public void setPatLogoSelo(String patLogoSelo) {
        this.patLogoSelo = patLogoSelo;
    }

    public String getPatLogoFundo() {
        return patLogoFundo;
    }

    public void setPatLogoFundo(String patLogoFundo) {
        this.patLogoFundo = patLogoFundo;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }
    
    public String getEmpEndereco() {
        return empEndereco;
    }

    public void setEmpEndereco(String empEndereco) {
        this.empEndereco = empEndereco;
    }
    
    public String getPeriodoConvencao() {
        return periodoConvencao;
    }

    public void setPeriodoConvencao(String periodoConvencao) {
        this.periodoConvencao = periodoConvencao;
    }
    
    public String getImagemFundo() {
        return imagemFundo;
    }

    public void setImagemFundo(String imagemFundo) {
        this.imagemFundo = imagemFundo;
    }
}
