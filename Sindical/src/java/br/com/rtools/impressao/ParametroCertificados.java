package br.com.rtools.impressao;

public class ParametroCertificados {

    private String detalhes_relatorio;
    private String cnpj;
    private String nome;
    private String repis_status;
    private String certidao_tipo;
    private String data_emissao;
    private String data_resposta;
    private String ano;
    private String solicitante;
    private String email;
    private String telefone;
    private String logradouro;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String observacao;

    public ParametroCertificados(String detalhes_relatorio, String cnpj, String nome, String repis_status, String certidao_tipo, String data_emissao, String data_resposta, String ano, String solicitante, String email, String telefone, String cidade) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.cnpj = cnpj;
        this.nome = nome;
        this.repis_status = repis_status;
        this.certidao_tipo = certidao_tipo;
        this.data_emissao = data_emissao;
        this.data_resposta = data_resposta;
        this.ano = ano;
        this.solicitante = solicitante;
        this.email = email;
        this.telefone = telefone;
        this.cidade = cidade;
    }

    public ParametroCertificados(String nome, String logradouro, String endereco, String numero, String complemento, String bairro, String cidade, String uf, String cep) {
        this.nome = nome;
        this.logradouro = logradouro;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public ParametroCertificados(String detalhes_relatorio, String cnpj, String nome, String repis_status, String certidao_tipo, String data_emissao, String data_resposta, String ano, String solicitante, String email, String telefone, String logradouro, String endereco, String numero, String complemento, String bairro, String cidade, String uf, String cep) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.cnpj = cnpj;
        this.nome = nome;
        this.repis_status = repis_status;
        this.certidao_tipo = certidao_tipo;
        this.data_emissao = data_emissao;
        this.data_resposta = data_resposta;
        this.ano = ano;
        this.solicitante = solicitante;
        this.email = email;
        this.telefone = telefone;
        this.logradouro = logradouro;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRepis_status() {
        return repis_status;
    }

    public void setRepis_status(String repis_status) {
        this.repis_status = repis_status;
    }

    public String getCertidao_tipo() {
        return certidao_tipo;
    }

    public void setCertidao_tipo(String certidao_tipo) {
        this.certidao_tipo = certidao_tipo;
    }

    public String getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(String data_emissao) {
        this.data_emissao = data_emissao;
    }

    public String getData_resposta() {
        return data_resposta;
    }

    public void setData_resposta(String data_resposta) {
        this.data_resposta = data_resposta;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}
