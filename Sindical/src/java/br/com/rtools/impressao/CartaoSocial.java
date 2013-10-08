package br.com.rtools.impressao;

public class CartaoSocial {

    private String matricula;
    private String barras;
    private String nome;
    private String empresa;
    private String cnpj;
    private String admissao;
    private String validade;
    private String e_cidade;
    private String e_uf;
    private String logo;
    private String imagem;
    private String filiacao;
    private String profissao;
    private String cpf;
    private String rg;

    public CartaoSocial(String matricula, String barras, String nome, String empresa, String cnpj, String admissao, String validade, String e_cidade, String e_uf, String logo, String imagem, String filiacao, String profissao, String cpf, String rg) {
        this.matricula = matricula;
        this.barras = barras;
        this.nome = nome;
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.admissao = admissao;
        this.validade = validade;
        this.e_cidade = e_cidade;
        this.e_uf = e_uf;
        this.logo = logo;
        this.imagem = imagem;
        this.filiacao = filiacao;
        this.profissao = profissao;
        this.cpf = cpf;
        this.rg = rg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCidade() {
        return e_cidade;
    }

    public void setCidade(String cidade) {
        this.e_cidade = cidade;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getUf() {
        return e_uf;
    }

    public void setUf(String uf) {
        this.e_uf = uf;
    }

    public String getAdmissao() {
        return admissao;
    }

    public void setAdmissao(String admissao) {
        this.admissao = admissao;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getE_cidade() {
        return e_cidade;
    }

    public void setE_cidade(String e_cidade) {
        this.e_cidade = e_cidade;
    }

    public String getE_uf() {
        return e_uf;
    }

    public void setE_uf(String e_uf) {
        this.e_uf = e_uf;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getFiliacao() {
        return filiacao;
    }

    public void setFiliacao(String filiacao) {
        this.filiacao = filiacao;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCPF() {
        return cpf;
    }

    public void setCPF(String cpf) {
        this.cpf = cpf;
    }

    public String getRG() {
        return rg;
    }

    public void setRG(String rg) {
        this.rg = rg;
    }
}