package br.com.rtools.impressao;

public class CartaoSocial {

    private String matricula;
    private String barras;
    private String nome;
    private String empresa;
    private String cnpj;
    private String admissao;
    private String validade;
    private String empresa_cidade;
    private String empresa_uf;
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
    private String estado_civil;
    private String carteira;
    private String serie;
    private String imagem_fundo;
    private String codigo_funcional;
    private String orgao_expeditor;
    private String parentesco;
    private String categoria;
    private String fantasia;
    private String titular;
    private String dependente;
    private String fantasia_titular;
    private String codigo_funcional_titular;
    private Integer titular_id;
    private String grupo_categoria;
    private String imagemExtra;
    private String imagemExtra2;

    public CartaoSocial() {
        this.matricula = "";
        this.barras = "";
        this.nome = "";
        this.empresa = "";
        this.cnpj = "";
        this.admissao = "";
        this.validade = "";
        this.empresa_cidade = "";
        this.empresa_uf = "";
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
        this.estado_civil = "";
        this.carteira = "";
        this.serie = "";
        this.imagem_fundo = "";
        this.codigo_funcional = "";
        this.orgao_expeditor = "";
        this.parentesco = "";
        this.categoria = "";
        this.fantasia = "";
        this.titular = "";
        this.dependente = "";
        this.fantasia_titular = "";
        this.codigo_funcional_titular = "";
        this.titular_id = null;
        this.grupo_categoria = "";
        this.imagemExtra = "";
        this.imagemExtra2 = "";
    }

    public CartaoSocial(String matricula, String barras, String nome, String empresa, String cnpj, String admissao, String validade, String empresa_cidade, String empresa_uf, String logo, String imagem, String filiacao, String profissao, String cpf, String rg, int id_pessoa, String endereco, String cidade, String nacionalidade, String nascimento, String estado_civil, String carteira, String serie, String imagem_fundo, String codigo_funcional, String orgao_expeditor, String parentesco, String categoria, String fantasia, String titular, String dependente, String fantasia_titular, String codigo_funcional_titular, Integer titular_id, String grupo_categoria, String imagemExtra, String imagemExtra2) {
        this.matricula = matricula;
        this.barras = barras;
        this.nome = nome;
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.admissao = admissao;
        this.validade = validade;
        this.empresa_cidade = empresa_cidade;
        this.empresa_uf = empresa_uf;
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
        this.estado_civil = estado_civil;
        this.carteira = carteira;
        this.serie = serie;
        this.imagem_fundo = imagem_fundo;
        this.codigo_funcional = codigo_funcional;
        this.orgao_expeditor = orgao_expeditor;
        this.parentesco = parentesco;
        this.categoria = categoria;
        this.fantasia = fantasia;
        this.titular = titular;
        this.dependente = dependente;
        this.fantasia_titular = fantasia_titular;
        this.codigo_funcional_titular = codigo_funcional_titular;
        this.titular_id = titular_id;
        this.grupo_categoria = grupo_categoria;
        this.imagemExtra = imagemExtra;
        this.imagemExtra2 = imagemExtra2;
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

    public String getEmpresa_cidade() {
        return empresa_cidade;
    }

    public void setEmpresa_cidade(String empresa_cidade) {
        this.empresa_cidade = empresa_cidade;
    }

    public String getEmpresa_uf() {
        return empresa_uf;
    }

    public void setEmpresa_uf(String empresa_uf) {
        this.empresa_uf = empresa_uf;
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

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
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

    public String getImagem_fundo() {
        return imagem_fundo;
    }

    public void setImagem_fundo(String imagem_fundo) {
        this.imagem_fundo = imagem_fundo;
    }

    public String getCodigo_funcional() {
        return codigo_funcional;
    }

    public void setCodigo_funcional(String codigo_funcional) {
        this.codigo_funcional = codigo_funcional;
    }

    public String getOrgao_expeditor() {
        return orgao_expeditor;
    }

    public void setOrgao_expeditor(String orgao_expeditor) {
        this.orgao_expeditor = orgao_expeditor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getDependente() {
        return dependente;
    }

    public void setDependente(String dependente) {
        this.dependente = dependente;
    }

    public String getCodigo_funcional_titular() {
        return codigo_funcional_titular;
    }

    public void setCodigo_funcional_titular(String codigo_funcional_titular) {
        this.codigo_funcional_titular = codigo_funcional_titular;
    }

    public String getFantasia_titular() {
        return fantasia_titular;
    }

    public void setFantasia_titular(String fantasia_titular) {
        this.fantasia_titular = fantasia_titular;
    }

    public Integer getTitular_id() {
        return titular_id;
    }

    public void setTitular_id(Integer titular_id) {
        this.titular_id = titular_id;
    }

    public String getGrupo_categoria() {
        return grupo_categoria;
    }

    public void setGrupo_categoria(String grupo_categoria) {
        this.grupo_categoria = grupo_categoria;
    }

    public String getImagemExtra() {
        return imagemExtra;
    }

    public void setImagemExtra(String imagemExtra) {
        this.imagemExtra = imagemExtra;
    }

    public String getImagemExtra2() {
        return imagemExtra2;
    }

    public void setImagemExtra2(String imagemExtra2) {
        this.imagemExtra2 = imagemExtra2;
    }
}
