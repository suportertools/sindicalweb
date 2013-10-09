package br.com.rtools.impressao;

import java.math.BigDecimal;
import java.util.Date;

public class Promissoria {

    private String numeropromissoria;
    private String extenso;
    private BigDecimal vlrpagar;
    private String razao;
    private String tipodocumento;
    private String cnpj;
    private String endereco;
    private String complemento;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private String sinnome;
    private String sindocumento;
    private String sincidade;
    private String sinuf;
    private String vencto;
    private String fundo_promissoria;
    private String venctoExtenso;

    public Promissoria() {
        this.numeropromissoria = "";
        this.extenso = "";
        this.vlrpagar = new BigDecimal(0);
        this.razao = "";
        this.tipodocumento = "";
        this.cnpj = "";
        this.endereco = "";
        this.complemento = "";
        this.numero = "";
        this.bairro = "";
        this.cidade = "";
        this.cep = "";
        this.uf = "";
        this.sinnome = "";
        this.sindocumento = "";
        this.sincidade = "";
        this.sinuf = "";
        this.vencto = "";
        this.fundo_promissoria = "";
        this.venctoExtenso = "";
    }

    public Promissoria(String numeropromissoria,
            String extenso,
            BigDecimal vlrpagar,
            String razao,
            String tipodocumento,
            String cnpj,
            String endereco,
            String complemento,
            String numero,
            String bairro,
            String cidade,
            String cep,
            String uf,
            String sinnome,
            String sindocumento,
            String sincidade,
            String sinuf,
            String vencto,
            String fundo_promissoria,
            String venctoExtenso) {
        this.numeropromissoria = numeropromissoria;
        this.extenso = extenso;
        this.vlrpagar = vlrpagar;
        this.razao = razao;
        this.tipodocumento = tipodocumento;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.complemento = complemento;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.uf = uf;
        this.sinnome = sinnome;
        this.sindocumento = sindocumento;
        this.sincidade = sincidade;
        this.sinuf = sinuf;
        this.vencto = vencto;
        this.fundo_promissoria = fundo_promissoria;
        this.venctoExtenso = venctoExtenso;
    }

    public String getNumeropromissoria() {
        return numeropromissoria;
    }

    public void setNumeropromissoria(String numeropromissoria) {
        this.numeropromissoria = numeropromissoria;
    }

    public String getExtenso() {
        return extenso;
    }

    public void setExtenso(String extenso) {
        this.extenso = extenso;
    }

    public BigDecimal getVlrpagar() {
        return vlrpagar;
    }

    public void setVlrpagar(BigDecimal vlrpagar) {
        this.vlrpagar = vlrpagar;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getSinnome() {
        return sinnome;
    }

    public void setSinnome(String sinnome) {
        this.sinnome = sinnome;
    }

    public String getSindocumento() {
        return sindocumento;
    }

    public void setSindocumento(String sindocumento) {
        this.sindocumento = sindocumento;
    }

    public String getSincidade() {
        return sincidade;
    }

    public void setSincidade(String sincidade) {
        this.sincidade = sincidade;
    }

    public String getSinuf() {
        return sinuf;
    }

    public void setSinuf(String sinuf) {
        this.sinuf = sinuf;
    }

    public String getVencto() {
        return vencto;
    }

    public void setVencto(String vencto) {
        this.vencto = vencto;
    }

    public String getFundo_promissoria() {
        return fundo_promissoria;
    }

    public void setFundo_promissoria(String fundo_promissoria) {
        this.fundo_promissoria = fundo_promissoria;
    }

    public String getVenctoExtenso() {
        return venctoExtenso;
    }

    public void setVenctoExtenso(String venctoExtenso) {
        this.venctoExtenso = venctoExtenso;
    }
}
