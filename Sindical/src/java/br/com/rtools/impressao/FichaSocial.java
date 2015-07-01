package br.com.rtools.impressao;

public class FichaSocial {

    private int ordem;
    private int codigo_socio;
    private int matricula;
    private String data_cadastro;
    private String recadastro;
    private String grupo;
    private String categoria;
    private String nome;
    private String sexo;
    private String data_nascimento;
    private String naturalidade;
    private String nacionalidade;
    private String rg;
    private String cpf;
    private String ctps;
    private String serie_ctps;
    private String estado_civil;
    private String pai;
    private String mae;
    private String telefone;
    private String celular;
    private String email;
    private String logradouro;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private boolean imprime_verso;
    private String dest_cnpj;
    private String dest_nome;
    private String dest_logradouro;
    private String dest_endereco;
    private String dest_numero;
    private String dest_complemento;
    private String dest_bairro;
    private String dest_cidade;
    private String dest_uf;
    private String dest_cep;
    private String empresa_nome;
    private String empresa_telefone;
    private String empresa_fax;
    private String empresa_admissao;
    private String empresa_cargo;
    private String empresa_endereco;
    private String empresa_numero;
    private String empresa_complemento;
    private String empresa_bairro;
    private String empresa_cidade;
    private String empresa_uf;
    private String empresa_cep;
    private String imagem;
    private String obs;
    private String grau;
    private String entidade;
    private String entidade_endereco;
    private String entidade_numero;
    private String entidade_complemento;
    private String entidade_bairro;
    private String entidade_cidade;
    private String entidade_uf;
    private String entidade_cep;
    private String entidade_cnpj;
    private String texto;
    private String imagem2;
    private String foto;
    private String entidade_email;
    private String entidade_site;
    private String entidade_telefone;
    private String caminho_verso;
    private String cnpj;
    private String data_recadastro;
    private String fantasia;
    private String entidade_logradouro;
    private String empresa_logradouro;
    private String assinatura;
    private String empresa_codigo;
    private String pis;

    public FichaSocial() {
        this.ordem = 0;
        this.codigo_socio = 0;
        this.matricula = 0;
        this.data_cadastro = "";
        this.recadastro = "";
        this.grupo = "";
        this.categoria = "";
        this.nome = "";
        this.sexo = "";
        this.data_nascimento = "";
        this.naturalidade = "";
        this.nacionalidade = "";
        this.rg = "";
        this.cpf = "";
        this.ctps = "";
        this.serie_ctps = "";
        this.estado_civil = "";
        this.pai = "";
        this.mae = "";
        this.telefone = "";
        this.celular = "";
        this.email = "";
        this.logradouro = "";
        this.endereco = "";
        this.numero = "";
        this.complemento = "";
        this.bairro = "";
        this.cidade = "";
        this.uf = "";
        this.cep = "";
        this.imprime_verso = false;
        this.dest_cnpj = "";
        this.dest_nome = "";
        this.dest_logradouro = "";
        this.dest_endereco = "";
        this.dest_numero = "";
        this.dest_complemento = "";
        this.dest_bairro = "";
        this.dest_cidade = "";
        this.dest_uf = "";
        this.dest_cep = "";
        this.empresa_nome = "";
        this.empresa_telefone = "";
        this.empresa_fax = "";
        this.empresa_admissao = "";
        this.empresa_cargo = "";
        this.empresa_endereco = "";
        this.empresa_numero = "";
        this.empresa_complemento = "";
        this.empresa_bairro = "";
        this.empresa_cidade = "";
        this.empresa_uf = "";
        this.empresa_cep = "";
        this.imagem = "";
        this.obs = "";
        this.grau = "";
        this.entidade = "";
        this.entidade_endereco = "";
        this.entidade_numero = "";
        this.entidade_complemento = "";
        this.entidade_bairro = "";
        this.entidade_cidade = "";
        this.entidade_uf = "";
        this.entidade_cep = "";
        this.entidade_cnpj = "";
        this.texto = "";
        this.imagem2 = "";
        this.foto = "";
        this.entidade_email = "";
        this.entidade_site = "";
        this.entidade_telefone = "";
        this.caminho_verso = "";
        this.cnpj = "";
        this.data_recadastro = "";
        this.fantasia = "";
        this.entidade_logradouro = "";
        this.empresa_logradouro = "";
        this.assinatura = "";
        this.empresa_codigo = "";
        this.pis = "";
    }

    public FichaSocial(int ordem, int codigo_socio, int matricula, String data_cadastro, String recadastro, String grupo, String categoria, String nome, String sexo, String data_nascimento, String naturalidade, String nacionalidade, String rg, String cpf, String ctps, String serie_ctps, String estado_civil, String pai, String mae, String telefone, String celular, String email, String logradouro, String endereco, String numero, String complemento, String bairro, String cidade, String uf, String cep, boolean imprime_verso, String dest_cnpj, String dest_nome, String dest_logradouro, String dest_endereco, String dest_numero, String dest_complemento, String dest_bairro, String dest_cidade, String dest_uf, String dest_cep, String empresa_nome, String empresa_telefone, String empresa_fax, String empresa_admissao, String empresa_cargo, String empresa_endereco, String empresa_numero, String empresa_complemento, String empresa_bairro, String empresa_cidade, String empresa_uf, String empresa_cep, String imagem, String obs, String grau, String entidade, String entidade_endereco, String entidade_numero, String entidade_complemento, String entidade_bairro, String entidade_cidade, String entidade_uf, String entidade_cep, String entidade_cnpj, String texto, String imagem2, String foto, String entidade_email, String entidade_site, String entidade_telefone, String caminho_verso, String cnpj, String data_recadastro, String fantasia, String entidade_logradouro, String empresa_logradouro, String assinatura, String empresa_codigo, String pis) {
        this.ordem = ordem;
        this.codigo_socio = codigo_socio;
        this.matricula = matricula;
        this.data_cadastro = data_cadastro;
        this.recadastro = recadastro;
        this.grupo = grupo;
        this.categoria = categoria;
        this.nome = nome;
        this.sexo = sexo;
        this.data_nascimento = data_nascimento;
        this.naturalidade = naturalidade;
        this.nacionalidade = nacionalidade;
        this.rg = rg;
        this.cpf = cpf;
        this.ctps = ctps;
        this.serie_ctps = serie_ctps;
        this.estado_civil = estado_civil;
        this.pai = pai;
        this.mae = mae;
        this.telefone = telefone;
        this.celular = celular;
        this.email = email;
        this.logradouro = logradouro;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.imprime_verso = imprime_verso;
        this.dest_cnpj = dest_cnpj;
        this.dest_nome = dest_nome;
        this.dest_logradouro = dest_logradouro;
        this.dest_endereco = dest_endereco;
        this.dest_numero = dest_numero;
        this.dest_complemento = dest_complemento;
        this.dest_bairro = dest_bairro;
        this.dest_cidade = dest_cidade;
        this.dest_uf = dest_uf;
        this.dest_cep = dest_cep;
        this.empresa_nome = empresa_nome;
        this.empresa_telefone = empresa_telefone;
        this.empresa_fax = empresa_fax;
        this.empresa_admissao = empresa_admissao;
        this.empresa_cargo = empresa_cargo;
        this.empresa_endereco = empresa_endereco;
        this.empresa_numero = empresa_numero;
        this.empresa_complemento = empresa_complemento;
        this.empresa_bairro = empresa_bairro;
        this.empresa_cidade = empresa_cidade;
        this.empresa_uf = empresa_uf;
        this.empresa_cep = empresa_cep;
        this.imagem = imagem;
        this.obs = obs;
        this.grau = grau;
        this.entidade = entidade;
        this.entidade_endereco = entidade_endereco;
        this.entidade_numero = entidade_numero;
        this.entidade_complemento = entidade_complemento;
        this.entidade_bairro = entidade_bairro;
        this.entidade_cidade = entidade_cidade;
        this.entidade_uf = entidade_uf;
        this.entidade_cep = entidade_cep;
        this.entidade_cnpj = entidade_cnpj;
        this.texto = texto;
        this.imagem2 = imagem2;
        this.foto = foto;
        this.entidade_email = entidade_email;
        this.entidade_site = entidade_site;
        this.entidade_telefone = entidade_telefone;
        this.caminho_verso = caminho_verso;
        this.cnpj = cnpj;
        this.data_recadastro = data_recadastro;
        this.fantasia = fantasia;
        this.entidade_logradouro = entidade_logradouro;
        this.empresa_logradouro = empresa_logradouro;
        this.assinatura = assinatura;
        this.empresa_codigo = empresa_codigo;
        this.pis = pis;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getCodigo_socio() {
        return codigo_socio;
    }

    public void setCodigo_socio(int codigo_socio) {
        this.codigo_socio = codigo_socio;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public String getRecadastro() {
        return recadastro;
    }

    public void setRecadastro(String recadastro) {
        this.recadastro = recadastro;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCtps() {
        return ctps;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean getImprime_verso() {
        return imprime_verso;
    }

    public void setImprime_verso(boolean imprime_verso) {
        this.imprime_verso = imprime_verso;
    }

    public String getDest_cnpj() {
        return dest_cnpj;
    }

    public void setDest_cnpj(String dest_cnpj) {
        this.dest_cnpj = dest_cnpj;
    }

    public String getDest_nome() {
        return dest_nome;
    }

    public void setDest_nome(String dest_nome) {
        this.dest_nome = dest_nome;
    }

    public String getDest_logradouro() {
        return dest_logradouro;
    }

    public void setDest_logradouro(String dest_logradouro) {
        this.dest_logradouro = dest_logradouro;
    }

    public String getDest_endereco() {
        return dest_endereco;
    }

    public void setDest_endereco(String dest_endereco) {
        this.dest_endereco = dest_endereco;
    }

    public String getDest_numero() {
        return dest_numero;
    }

    public void setDest_numero(String dest_numero) {
        this.dest_numero = dest_numero;
    }

    public String getDest_complemento() {
        return dest_complemento;
    }

    public void setDest_complemento(String dest_complemento) {
        this.dest_complemento = dest_complemento;
    }

    public String getDest_bairro() {
        return dest_bairro;
    }

    public void setDest_bairro(String dest_bairro) {
        this.dest_bairro = dest_bairro;
    }

    public String getDest_cidade() {
        return dest_cidade;
    }

    public void setDest_cidade(String dest_cidade) {
        this.dest_cidade = dest_cidade;
    }

    public String getDest_uf() {
        return dest_uf;
    }

    public void setDest_uf(String dest_uf) {
        this.dest_uf = dest_uf;
    }

    public String getDest_cep() {
        return dest_cep;
    }

    public void setDest_cep(String dest_cep) {
        this.dest_cep = dest_cep;
    }

    public String getEmpresa_nome() {
        return empresa_nome;
    }

    public void setEmpresa_nome(String empresa_nome) {
        this.empresa_nome = empresa_nome;
    }

    public String getEmpresa_telefone() {
        return empresa_telefone;
    }

    public void setEmpresa_telefone(String empresa_telefone) {
        this.empresa_telefone = empresa_telefone;
    }

    public String getEmpresa_fax() {
        return empresa_fax;
    }

    public void setEmpresa_fax(String empresa_fax) {
        this.empresa_fax = empresa_fax;
    }

    public String getEmpresa_admissao() {
        return empresa_admissao;
    }

    public void setEmpresa_admissao(String empresa_admissao) {
        this.empresa_admissao = empresa_admissao;
    }

    public String getEmpresa_cargo() {
        return empresa_cargo;
    }

    public void setEmpresa_cargo(String empresa_cargo) {
        this.empresa_cargo = empresa_cargo;
    }

    public String getEmpresa_endereco() {
        return empresa_endereco;
    }

    public void setEmpresa_endereco(String empresa_endereco) {
        this.empresa_endereco = empresa_endereco;
    }

    public String getEmpresa_numero() {
        return empresa_numero;
    }

    public void setEmpresa_numero(String empresa_numero) {
        this.empresa_numero = empresa_numero;
    }

    public String getEmpresa_complemento() {
        return empresa_complemento;
    }

    public void setEmpresa_complemento(String empresa_complemento) {
        this.empresa_complemento = empresa_complemento;
    }

    public String getEmpresa_bairro() {
        return empresa_bairro;
    }

    public void setEmpresa_bairro(String empresa_bairro) {
        this.empresa_bairro = empresa_bairro;
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

    public String getEmpresa_cep() {
        return empresa_cep;
    }

    public void setEmpresa_cep(String empresa_cep) {
        this.empresa_cep = empresa_cep;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getGrau() {
        return grau;
    }

    public void setGrau(String grau) {
        this.grau = grau;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getEntidade_endereco() {
        return entidade_endereco;
    }

    public void setEntidade_endereco(String entidade_endereco) {
        this.entidade_endereco = entidade_endereco;
    }

    public String getEntidade_numero() {
        return entidade_numero;
    }

    public void setEntidade_numero(String entidade_numero) {
        this.entidade_numero = entidade_numero;
    }

    public String getEntidade_complemento() {
        return entidade_complemento;
    }

    public void setEntidade_complemento(String entidade_complemento) {
        this.entidade_complemento = entidade_complemento;
    }

    public String getEntidade_bairro() {
        return entidade_bairro;
    }

    public void setEntidade_bairro(String entidade_bairro) {
        this.entidade_bairro = entidade_bairro;
    }

    public String getEntidade_cidade() {
        return entidade_cidade;
    }

    public void setEntidade_cidade(String entidade_cidade) {
        this.entidade_cidade = entidade_cidade;
    }

    public String getEntidade_uf() {
        return entidade_uf;
    }

    public void setEntidade_uf(String entidade_uf) {
        this.entidade_uf = entidade_uf;
    }

    public String getEntidade_cep() {
        return entidade_cep;
    }

    public void setEntidade_cep(String entidade_cep) {
        this.entidade_cep = entidade_cep;
    }

    public String getEntidade_cnpj() {
        return entidade_cnpj;
    }

    public void setEntidade_cnpj(String entidade_cnpj) {
        this.entidade_cnpj = entidade_cnpj;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getImagem2() {
        return imagem2;
    }

    public void setImagem2(String imagem2) {
        this.imagem2 = imagem2;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCaminho_verso() {
        return caminho_verso;
    }

    public void setCaminho_verso(String caminho_verso) {
        this.caminho_verso = caminho_verso;
    }

    public String getData_recadastro() {
        return data_recadastro;
    }

    public void setData_recadastro(String data_recadastro) {
        this.data_recadastro = data_recadastro;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getEntidade_logradouro() {
        return entidade_logradouro;
    }

    public void setEntidade_logradouro(String entidade_logradouro) {
        this.entidade_logradouro = entidade_logradouro;
    }

    public String getEmpresa_logradouro() {
        return empresa_logradouro;
    }

    public void setEmpresa_logradouro(String empresa_logradouro) {
        this.empresa_logradouro = empresa_logradouro;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getEmpresa_codigo() {
        return empresa_codigo;
    }

    public void setEmpresa_codigo(String empresa_codigo) {
        this.empresa_codigo = empresa_codigo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEntidade_site() {
        return entidade_site;
    }

    public void setEntidade_site(String entidade_site) {
        this.entidade_site = entidade_site;
    }

    public String getEntidade_telefone() {
        return entidade_telefone;
    }

    public void setEntidade_telefone(String entidade_telefone) {
        this.entidade_telefone = entidade_telefone;
    }

    public String getSerie_ctps() {
        return serie_ctps;
    }

    public void setSerie_ctps(String serie_ctps) {
        this.serie_ctps = serie_ctps;
    }

    public String getEntidade_email() {
        return entidade_email;
    }

    public void setEntidade_email(String entidade_email) {
        this.entidade_email = entidade_email;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

}
