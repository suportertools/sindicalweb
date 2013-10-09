package br.com.rtools.impressao;

import java.util.Date;

public class FichaSocial {

    private int ordem;
    private int codsocio;
    private int matricula;
    private Date datacadastro;
    private Date recadastro;
    private String grupo;
    private String categoria;
    private String nome;
    private String sexo;
    private Date datanascimento;
    private String naturalidade;
    private String nacionalidade;
    private String rg;
    private String cpf;
    private String ctps;
    private String seriectps;
    private String estadocivil;
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
    private boolean imprimeVerso;
    private String dest_cgc;
    private String dest_nome;
    private String dest_logradouro;
    private String dest_endereco;
    private String dest_numero;
    private String dest_complemento;
    private String dest_bairro;
    private String dest_cidade;
    private String dest_uf;
    private String dest_cep;
    private String e_nome;
    private String e_telefone;
    private String e_fax;
    private Date e_admissao;
    private String e_cargo;
    private String e_endereco;
    private String e_numero;
    private String e_complemento;
    private String e_bairro;
    private String e_cidade;
    private String e_uf;
    private String e_cep;
    private String imagem;
    private String obs;
    private String grau;
    private String entidade;
    private String endent;
    private String nument;
    private String compent;
    private String baient;
    private String cident;
    private String estent;
    private String cepent;
    private String cgcent;
    private String texto;
    private String imagem2;
    private String foto;
    private String emailent;
    private String siteent;
    private String telent;
    private String caminhoverso;
    private String cgc;
    private Date datarecadastro;
    private String fantasia;
    private String logrent;
    private String e_logradouro;

    public FichaSocial(int ordem, int codsocio, int matricula, Date datacadastro, Date recadastro, String grupo, String categoria, String nome, String sexo, Date datanascimento,
            String naturalidade, String nacionalidade, String rg, String cpf, String ctps, String seriectps, String estadocivil, String pai, String mae, String telefone,
            String celular, String email, String logradouro, String endereco, String numero, String complemento, String bairro, String cidade, String uf, String cep,
            boolean imprimeVerso, String dest_cgc, String dest_nome, String dest_logradouro, String dest_endereco, String dest_numero, String dest_complemento,
            String dest_bairro, String dest_cidade, String dest_uf, String dest_cep, String e_nome, String e_telefone, String e_fax, Date e_admissao, String e_cargo,
            String e_endereco, String e_numero, String e_complemento, String e_bairro, String e_cidade, String e_uf, String e_cep, String imagem, String obs, String grau,
            String entidade, String endent, String nument, String compent, String baient, String cident, String estent, String cepent, String cgcent, String texto,
            String imagem2, String foto, String emailent, String siteent, String telent, String caminhoverso, String cgc, Date datarecadastro, String fantasia, String logrent, String e_logradouro) {
        this.ordem = ordem;
        this.codsocio = codsocio;
        this.matricula = matricula;
        this.datacadastro = datacadastro;
        this.recadastro = recadastro;
        this.grupo = grupo;
        this.categoria = categoria;
        this.nome = nome;
        this.sexo = sexo;
        this.datanascimento = datanascimento;
        this.naturalidade = naturalidade;
        this.nacionalidade = nacionalidade;
        this.rg = rg;
        this.cpf = cpf;
        this.ctps = ctps;
        this.seriectps = seriectps;
        this.estadocivil = estadocivil;
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
        this.imprimeVerso = imprimeVerso;
        this.dest_cgc = dest_cgc;
        this.dest_nome = dest_nome;
        this.dest_logradouro = dest_logradouro;
        this.dest_endereco = dest_endereco;
        this.dest_numero = dest_numero;
        this.dest_complemento = dest_complemento;
        this.dest_bairro = dest_bairro;
        this.dest_cidade = dest_cidade;
        this.dest_uf = dest_uf;
        this.dest_cep = dest_cep;
        this.e_nome = e_nome;
        this.e_telefone = e_telefone;
        this.e_fax = e_fax;
        this.e_admissao = e_admissao;
        this.e_cargo = e_cargo;
        this.e_endereco = e_endereco;
        this.e_numero = e_numero;
        this.e_complemento = e_complemento;
        this.e_bairro = e_bairro;
        this.e_cidade = e_cidade;
        this.e_uf = e_uf;
        this.e_cep = e_cep;
        this.imagem = imagem;
        this.obs = obs;
        this.grau = grau;
        this.entidade = entidade;
        this.endent = endent;
        this.nument = nument;
        this.compent = compent;
        this.baient = baient;
        this.cident = cident;
        this.estent = estent;
        this.cepent = cepent;
        this.cgcent = cgcent;
        this.texto = texto;
        this.imagem2 = imagem2;
        this.foto = foto;
        this.emailent = emailent;
        this.siteent = siteent;
        this.telent = telent;
        this.caminhoverso = caminhoverso;
        this.cgc = cgc;
        this.datarecadastro = datarecadastro;
        this.fantasia = fantasia;
        this.logrent = logrent;
        this.e_logradouro = e_logradouro;
    }

    public FichaSocial() {
        this.ordem = 0;
        this.codsocio = 0;
        this.matricula = 0;
        this.datacadastro = null;
        this.recadastro = null;
        this.grupo = "";
        this.categoria = "";
        this.nome = "";
        this.sexo = "";
        this.datanascimento = null;
        this.naturalidade = "";
        this.nacionalidade = "";
        this.rg = "";
        this.cpf = "";
        this.ctps = "";
        this.seriectps = "";
        this.estadocivil = "";
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
        this.imprimeVerso = false;
        this.dest_cgc = "";
        this.dest_nome = "";
        this.dest_logradouro = "";
        this.dest_endereco = "";
        this.dest_numero = "";
        this.dest_complemento = "";
        this.dest_bairro = "";
        this.dest_cidade = "";
        this.dest_uf = "";
        this.dest_cep = "";
        this.e_nome = "";
        this.e_telefone = "";
        this.e_fax = "";
        this.e_admissao = null;
        this.e_cargo = "";
        this.e_endereco = "";
        this.e_numero = "";
        this.e_complemento = "";
        this.e_bairro = "";
        this.e_cidade = "";
        this.e_uf = "";
        this.e_cep = "";
        this.imagem = "";
        this.obs = "";
        this.grau = "";
        this.entidade = "";
        this.endent = "";
        this.nument = "";
        this.compent = "";
        this.baient = "";
        this.cident = "";
        this.estent = "";
        this.cepent = "";
        this.cgcent = "";
        this.texto = "";
        this.imagem2 = "";
        this.foto = "";
        this.emailent = "";
        this.siteent = "";
        this.telent = "";
        this.caminhoverso = "";
        this.cgc = "";
        this.datarecadastro = null;
        this.fantasia = "";
        this.logrent = "";
        this.e_logradouro = "";
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getCodsocio() {
        return codsocio;
    }

    public void setCodsocio(int codsocio) {
        this.codsocio = codsocio;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public Date getDatacadastro() {
        return datacadastro;
    }

    public void setDatacadastro(Date datacadastro) {
        this.datacadastro = datacadastro;
    }

    public Date getRecadastro() {
        return recadastro;
    }

    public void setRecadastro(Date recadastro) {
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

    public Date getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(Date datanascimento) {
        this.datanascimento = datanascimento;
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

    public String getSeriectps() {
        return seriectps;
    }

    public void setSeriectps(String seriectps) {
        this.seriectps = seriectps;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
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

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public String getDest_cgc() {
        return dest_cgc;
    }

    public void setDest_cgc(String dest_cgc) {
        this.dest_cgc = dest_cgc;
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

    public String getE_nome() {
        return e_nome;
    }

    public void setE_nome(String e_nome) {
        this.e_nome = e_nome;
    }

    public String getE_telefone() {
        return e_telefone;
    }

    public void setE_telefone(String e_telefone) {
        this.e_telefone = e_telefone;
    }

    public String getE_fax() {
        return e_fax;
    }

    public void setE_fax(String e_fax) {
        this.e_fax = e_fax;
    }

    public Date getE_admissao() {
        return e_admissao;
    }

    public void setE_admissao(Date e_admissao) {
        this.e_admissao = e_admissao;
    }

    public String getE_cargo() {
        return e_cargo;
    }

    public void setE_cargo(String e_cargo) {
        this.e_cargo = e_cargo;
    }

    public String getE_endereco() {
        return e_endereco;
    }

    public void setE_endereco(String e_endereco) {
        this.e_endereco = e_endereco;
    }

    public String getE_numero() {
        return e_numero;
    }

    public void setE_numero(String e_numero) {
        this.e_numero = e_numero;
    }

    public String getE_complemento() {
        return e_complemento;
    }

    public void setE_complemento(String e_complemento) {
        this.e_complemento = e_complemento;
    }

    public String getE_bairro() {
        return e_bairro;
    }

    public void setE_bairro(String e_bairro) {
        this.e_bairro = e_bairro;
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

    public String getE_cep() {
        return e_cep;
    }

    public void setE_cep(String e_cep) {
        this.e_cep = e_cep;
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

    public String getEndent() {
        return endent;
    }

    public void setEndent(String endent) {
        this.endent = endent;
    }

    public String getNument() {
        return nument;
    }

    public void setNument(String nument) {
        this.nument = nument;
    }

    public String getCompent() {
        return compent;
    }

    public void setCompent(String compent) {
        this.compent = compent;
    }

    public String getBaient() {
        return baient;
    }

    public void setBaient(String baient) {
        this.baient = baient;
    }

    public String getCident() {
        return cident;
    }

    public void setCident(String cident) {
        this.cident = cident;
    }

    public String getEstent() {
        return estent;
    }

    public void setEstent(String estent) {
        this.estent = estent;
    }

    public String getCepent() {
        return cepent;
    }

    public void setCepent(String cepent) {
        this.cepent = cepent;
    }

    public String getCgcent() {
        return cgcent;
    }

    public void setCgcent(String cgcent) {
        this.cgcent = cgcent;
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

    public String getEmailent() {
        return emailent;
    }

    public void setEmailent(String emailent) {
        this.emailent = emailent;
    }

    public String getSiteent() {
        return siteent;
    }

    public void setSiteent(String siteent) {
        this.siteent = siteent;
    }

    public String getTelent() {
        return telent;
    }

    public void setTelent(String telent) {
        this.telent = telent;
    }

    public String getCaminhoverso() {
        return caminhoverso;
    }

    public void setCaminhoverso(String caminhoverso) {
        this.caminhoverso = caminhoverso;
    }

    public String getCgc() {
        return cgc;
    }

    public void setCgc(String cgc) {
        this.cgc = cgc;
    }

    public Date getDatarecadastro() {
        return datarecadastro;
    }

    public void setDatarecadastro(Date datarecadastro) {
        this.datarecadastro = datarecadastro;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getLogrent() {
        return logrent;
    }

    public void setLogrent(String logrent) {
        this.logrent = logrent;
    }

    public String getE_logradouro() {
        return e_logradouro;
    }

    public void setE_logradouro(String e_logradouro) {
        this.e_logradouro = e_logradouro;
    }
}
