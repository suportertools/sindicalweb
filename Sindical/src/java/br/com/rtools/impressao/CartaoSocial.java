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
    private int id_pessoa;
    private String endereco;
    private String cidade;
    private String nacionalidade;
    private String nascimento;
    private String estadocivil;
    private String carteira;
    private String serie;
    private String imagemfundo;

    public CartaoSocial() {
        this.matricula = "";
        this.barras = "";
        this.nome = "";
        this.empresa = "";
        this.cnpj = "";
        this.admissao = "";
        this.validade = "";
        this.e_cidade = "";
        this.e_uf = "";
        this.logo = "";
        this.imagem = "";
        this.filiacao = "";
        this.profissao = "";
        this.cpf = "";
        this.rg = "";
        this.id_pessoa = 0;
        this.endereco = "";
        this.cidade = "";
        this.nacionalidade = "";
        this.nascimento = "";
        this.estadocivil = "";
        this.carteira = "";
        this.serie = "";
        this.imagemfundo = "";
    }
        
    public CartaoSocial(String matricula, String barras, String nome, String empresa, String cnpj, String admissao, String validade, String e_cidade, String e_uf, String logo, String imagem, String filiacao, String profissao, String cpf, String rg, int id_pessoa, String endereco, String cidade, String nacionalidade, String nascimento, String estadocivil, String carteira, String serie, String imagemfundo) {
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
        this.id_pessoa = id_pessoa;
        this.endereco = endereco;
        this.cidade = cidade;
        this.nacionalidade = nacionalidade;
        this.nascimento = nascimento;
        this.estadocivil = estadocivil;
        this.carteira = carteira;
        this.serie = serie;
        this.imagemfundo = imagemfundo;
    }
    
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAdmissao() {
        return admissao;
    }

    public void setAdmissao(String admissao) {
        this.admissao = admissao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
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

    public int getId_pessoa() {
        return id_pessoa;
    }

    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getImagemfundo() {
        return imagemfundo;
    }

    public void setImagemfundo(String imagemfundo) {
        this.imagemfundo = imagemfundo;
    }
}