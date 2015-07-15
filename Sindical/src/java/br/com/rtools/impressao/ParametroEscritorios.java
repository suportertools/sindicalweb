package br.com.rtools.impressao;

public class ParametroEscritorios {

    private Integer escritorio_id;
    private String escritorio_nome;
    private String escritorio_endereco;
    private String escritorio_logradouro;
    private String escritorio_numero;
    private String escritorio_complemento;
    private String escritorio_bairro;
    private String escritorio_cep;
    private String escritorio_cidade;
    private String escritorio_uf;
    private String escritorio_telefone;
    private String escritorio_email;
    private Integer escritorio_quantidade_empresas;

    public ParametroEscritorios(Integer escritorio_id, String escritorio_nome, String escritorio_endereco, String escritorio_logradouro, String escritorio_numero, String escritorio_complemento, String escritorio_bairro, String escritorio_cep, String escritorio_cidade, String escritorio_uf, String escritorio_telefone, String escritorio_email, Integer escritorio_quantidade_empresas) {
        this.escritorio_id = escritorio_id;
        this.escritorio_nome = escritorio_nome;
        this.escritorio_endereco = escritorio_endereco;
        this.escritorio_logradouro = escritorio_logradouro;
        this.escritorio_numero = escritorio_numero;
        this.escritorio_complemento = escritorio_complemento;
        this.escritorio_bairro = escritorio_bairro;
        this.escritorio_cep = escritorio_cep;
        this.escritorio_cidade = escritorio_cidade;
        this.escritorio_uf = escritorio_uf;
        this.escritorio_telefone = escritorio_telefone;
        this.escritorio_email = escritorio_email;
        this.escritorio_quantidade_empresas = escritorio_quantidade_empresas;
    }

    public Integer getEscritorio_id() {
        return escritorio_id;
    }

    public void setEscritorio_id(Integer escritorio_id) {
        this.escritorio_id = escritorio_id;
    }

    public String getEscritorio_nome() {
        return escritorio_nome;
    }

    public void setEscritorio_nome(String escritorio_nome) {
        this.escritorio_nome = escritorio_nome;
    }

    public String getEscritorio_endereco() {
        return escritorio_endereco;
    }

    public void setEscritorio_endereco(String escritorio_endereco) {
        this.escritorio_endereco = escritorio_endereco;
    }

    public String getEscritorio_logradouro() {
        return escritorio_logradouro;
    }

    public void setEscritorio_logradouro(String escritorio_logradouro) {
        this.escritorio_logradouro = escritorio_logradouro;
    }

    public String getEscritorio_numero() {
        return escritorio_numero;
    }

    public void setEscritorio_numero(String escritorio_numero) {
        this.escritorio_numero = escritorio_numero;
    }

    public String getEscritorio_complemento() {
        return escritorio_complemento;
    }

    public void setEscritorio_complemento(String escritorio_complemento) {
        this.escritorio_complemento = escritorio_complemento;
    }

    public String getEscritorio_bairro() {
        return escritorio_bairro;
    }

    public void setEscritorio_bairro(String escritorio_bairro) {
        this.escritorio_bairro = escritorio_bairro;
    }

    public String getEscritorio_cep() {
        return escritorio_cep;
    }

    public void setEscritorio_cep(String escritorio_cep) {
        this.escritorio_cep = escritorio_cep;
    }

    public String getEscritorio_cidade() {
        return escritorio_cidade;
    }

    public void setEscritorio_cidade(String escritorio_cidade) {
        this.escritorio_cidade = escritorio_cidade;
    }

    public String getEscritorio_uf() {
        return escritorio_uf;
    }

    public void setEscritorio_uf(String escritorio_uf) {
        this.escritorio_uf = escritorio_uf;
    }

    public String getEscritorio_telefone() {
        return escritorio_telefone;
    }

    public void setEscritorio_telefone(String escritorio_telefone) {
        this.escritorio_telefone = escritorio_telefone;
    }

    public String getEscritorio_email() {
        return escritorio_email;
    }

    public void setEscritorio_email(String escritorio_email) {
        this.escritorio_email = escritorio_email;
    }

    public Integer getEscritorio_quantidade_empresas() {
        return escritorio_quantidade_empresas;
    }

    public void setEscritorio_quantidade_empresas(Integer escritorio_quantidade_empresas) {
        this.escritorio_quantidade_empresas = escritorio_quantidade_empresas;
    }

}
