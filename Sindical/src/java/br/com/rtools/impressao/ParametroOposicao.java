package br.com.rtools.impressao;

public class ParametroOposicao {

    private String detalheRelatorio;
    private String emissao;
    private String tipo;
    private String documento;
    private String empresa;
    private String funcionario;
    private String sexo;
    private String cpf;
    private String rg;
    private String ref_i;
    private String ref_f;
    private String logradouro;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public ParametroOposicao() {
        this.detalheRelatorio = "";
        this.emissao = "";
        this.tipo = "";
        this.documento = "";
        this.empresa = "";
        this.funcionario = "";
        this.sexo = "";
        this.cpf = "";
        this.rg = "";
        this.ref_i = "";
        this.ref_f = "";
        this.logradouro = "";
        this.endereco = "";
        this.numero = "";
        this.complemento = "";
        this.bairro = "";
        this.cidade = "";
        this.uf = "";
        this.cep = "";
    }

    public ParametroOposicao(String detalheRelatorio, String emissao, String tipo, String documento, String empresa, String funcionario, String sexo, String cpf, String rg, String ref_i, String ref_f, String logradouro, String endereco, String numero, String complemento, String bairro, String cidade, String uf, String cep) {
        this.detalheRelatorio = detalheRelatorio;
        this.emissao = emissao;
        this.tipo = tipo;
        this.documento = documento;
        this.empresa = empresa;
        this.funcionario = funcionario;
        this.sexo = sexo;
        this.cpf = cpf;
        this.rg = rg;
        this.ref_i = ref_i;
        this.ref_f = ref_f;
        this.logradouro = logradouro;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public String getDetalheRelatorio() {
        return detalheRelatorio;
    }

    public void setDetalheRelatorio(String detalheRelatorio) {
        this.detalheRelatorio = detalheRelatorio;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getRef_i() {
        return ref_i;
    }

    public void setRef_i(String ref_i) {
        this.ref_i = ref_i;
    }

    public String getRef_f() {
        return ref_f;
    }

    public void setRef_f(String ref_f) {
        this.ref_f = ref_f;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

}
