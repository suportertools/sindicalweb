package br.com.rtools.impressao;

import java.util.List;

public class ParametroCarneMensalidades {
    private String sinLogo;
    private String sinNome;
    private String sinEndereco;
    private String sinLogradouro;
    private String sinNumero;
    private String sinComplemento;
    private String sinBairro;
    private String sinCep;
    private String sinCidade;
    private String sinUF;
    private String sinTelefone;
    private String sinEmail;
    private String sinSite;
    private String sinDocumento;
    
    private String titular;
    private String matricula;
    private String codigo;
    private String categoria;
    
    private Integer parcela;
    private String vencimento;
    private String valor;
    private String valor_total;
    
    private List listaServicos;
    
    public ParametroCarneMensalidades(String sinLogo, String sinNome, String sinEndereco, String sinLogradouro, String sinNumero, String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinUF, String sinTelefone, String sinEmail, String sinSite, String sinDocumento, String titular, String matricula, String codigo, String categoria, Integer parcela, String vencimento, String valor, String valor_total, List listaServicos) {
        this.sinLogo = sinLogo;
        this.sinNome = sinNome;
        this.sinEndereco = sinEndereco;
        this.sinLogradouro = sinLogradouro;
        this.sinNumero = sinNumero;
        this.sinComplemento = sinComplemento;
        this.sinBairro = sinBairro;
        this.sinCep = sinCep;
        this.sinCidade = sinCidade;
        this.sinUF = sinUF;
        this.sinTelefone = sinTelefone;
        this.sinEmail = sinEmail;
        this.sinSite = sinSite;
        this.sinDocumento = sinDocumento;
        this.titular = titular;
        this.matricula = matricula;
        this.codigo = codigo;
        this.categoria = categoria;
        this.parcela = parcela;
        this.vencimento = vencimento;
        this.valor = valor;
        this.valor_total = valor_total;
        this.listaServicos = listaServicos;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public String getSinNome() {
        return sinNome;
    }

    public void setSinNome(String sinNome) {
        this.sinNome = sinNome;
    }

    public String getSinEndereco() {
        return sinEndereco;
    }

    public void setSinEndereco(String sinEndereco) {
        this.sinEndereco = sinEndereco;
    }

    public String getSinLogradouro() {
        return sinLogradouro;
    }

    public void setSinLogradouro(String sinLogradouro) {
        this.sinLogradouro = sinLogradouro;
    }

    public String getSinNumero() {
        return sinNumero;
    }

    public void setSinNumero(String sinNumero) {
        this.sinNumero = sinNumero;
    }

    public String getSinComplemento() {
        return sinComplemento;
    }

    public void setSinComplemento(String sinComplemento) {
        this.sinComplemento = sinComplemento;
    }

    public String getSinBairro() {
        return sinBairro;
    }

    public void setSinBairro(String sinBairro) {
        this.sinBairro = sinBairro;
    }

    public String getSinCep() {
        return sinCep;
    }

    public void setSinCep(String sinCep) {
        this.sinCep = sinCep;
    }

    public String getSinCidade() {
        return sinCidade;
    }

    public void setSinCidade(String sinCidade) {
        this.sinCidade = sinCidade;
    }

    public String getSinUF() {
        return sinUF;
    }

    public void setSinUF(String sinUF) {
        this.sinUF = sinUF;
    }

    public String getSinTelefone() {
        return sinTelefone;
    }

    public void setSinTelefone(String sinTelefone) {
        this.sinTelefone = sinTelefone;
    }

    public String getSinEmail() {
        return sinEmail;
    }

    public void setSinEmail(String sinEmail) {
        this.sinEmail = sinEmail;
    }

    public String getSinSite() {
        return sinSite;
    }

    public void setSinSite(String sinSite) {
        this.sinSite = sinSite;
    }

    public String getSinDocumento() {
        return sinDocumento;
    }

    public void setSinDocumento(String sinDocumento) {
        this.sinDocumento = sinDocumento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
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

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }

    public List getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List listaServicos) {
        this.listaServicos = listaServicos;
    }

    
}
