package br.com.rtools.impressao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ParametroSocios {

    private String sinLogo;
    private String sinSite;
    private String sinNome;
    private String sinEndereco;
    private String sinLogradouro;
    private String sinNumero;
    private String sinComplemento;
    private String sinBairro;
    private String sinCep;
    private String sinCidade;
    private String sinCidadeUf;
    private String sinDocumento;
    private int codigo;
    private Date cadastro;
    private String nome;
    private String cpf;
    private String telefone;
    private String ds_uf_emissao_rg;
    private String estado_civil;
    private String ctps;
    private String pai;
    private String sexo;
    private String mae;
    private String nacionalidade;
    private String nit;
    private String ds_orgao_emissao_rg;
    private String pis;
    private String ds_serie;
    private Date dt_aposentadoria;
    private String ds_naturalidade;
    private Date recadastro;
    private Date dt_nascimento;
    private Date dt_foto;
    private String ds_rg;
    private String foto;
    private String logradouro;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String setor;
    private Date admissao;
    private String profissao;
    private String fantasia;
    private String empresa;
    private String cnpj;
    private String e_telefone;
    private String e_logradouro;
    private String e_endereco;
    private String e_numero;
    private String e_complemento;
    private String e_bairro;
    private String e_cidade;
    private String e_uf;
    private String e_cep;
    private String titular;
    private String codsocio;
    private String nomeTitular;
    private String parentesco;
    private int matricula;
    private String categoria;
    private String grupo_categoria;
    private Date filiacao;
    private Date inativacao;
    private boolean votante;
    private String grau;
    private BigDecimal nr_deconto;
    private boolean desconto_folha;
    private String tipo_cobranca;
    private int cod_tipo_cobranca;
    private String telefone2;
    private String telefone3;
    private String email;
    private String contabilidade_nome;
    private String contabilidade_contato;
    private String contabilidade_telefone;
    private String admissao_empresa_demissionada;
    private String demissao_empresa_demissionada;
    private String cnpj_empresa_demissionada;
    private String empresa_demissionada;
    private String idade;

    public ParametroSocios() {
        this.sinLogo = "";
        this.sinSite = "";
        this.sinNome = "";
        this.sinEndereco = "";
        this.sinLogradouro = "";
        this.sinNumero = "";
        this.sinComplemento = "";
        this.sinBairro = "";
        this.sinCep = "";
        this.sinCidade = "";
        this.sinCidadeUf = "";
        this.sinDocumento = "";
        this.codigo = 0;
        this.cadastro = new Date();
        this.nome = "";
        this.cpf = "";
        this.telefone = "";
        this.ds_uf_emissao_rg = "";
        this.estado_civil = "";
        this.ctps = "";
        this.pai = "";
        this.sexo = "";
        this.mae = "";
        this.nacionalidade = "";
        this.nit = "";
        this.ds_orgao_emissao_rg = "";
        this.pis = "";
        this.ds_serie = "";
        this.dt_aposentadoria = new Date();
        this.ds_naturalidade = "";
        this.recadastro = new Date();
        this.dt_nascimento = new Date();
        this.dt_foto = new Date();
        this.ds_rg = "";
        this.foto = "";
        this.logradouro = "";
        this.endereco = "";
        this.numero = "";
        this.complemento = "";
        this.bairro = "";
        this.cidade = "";
        this.uf = "";
        this.cep = "";
        this.setor = "";
        this.admissao = new Date();
        this.profissao = "";
        this.fantasia = "";
        this.empresa = "";
        this.cnpj = "";
        this.e_telefone = "";
        this.e_logradouro = "";
        this.e_endereco = "";
        this.e_numero = "";
        this.e_complemento = "";
        this.e_bairro = "";
        this.e_cidade = "";
        this.e_uf = "";
        this.e_cep = "";
        this.titular = "";
        this.codsocio = "";
        this.nomeTitular = "";
        this.parentesco = "";
        this.matricula = 0;
        this.categoria = "";
        this.grupo_categoria = "";
        this.filiacao = new Date();
        this.inativacao = new Date();
        this.votante = false;
        this.grau = "";
        this.nr_deconto = new BigDecimal(0);
        this.desconto_folha = false;
        this.tipo_cobranca = "";
        this.cod_tipo_cobranca = 0;
        this.telefone2 = "";
        this.telefone3 = "";
        this.email = "";
        this.contabilidade_nome = "";
        this.contabilidade_contato = "";
        this.contabilidade_telefone = "";
        this.admissao_empresa_demissionada = "";
        this.demissao_empresa_demissionada = "";
        this.cnpj_empresa_demissionada = "";
        this.empresa_demissionada = "";
        this.idade = "";
    }

    public ParametroSocios(String sinLogo, String sinSite, String sinNome, String sinEndereco, String sinLogradouro, String sinNumero, String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinCidadeUf, String sinDocumento, int codigo, Date cadastro, String nome, String cpf, String telefone, String ds_uf_emissao_rg, String estado_civil, String ctps, String pai, String sexo, String mae, String nacionalidade, String nit, String ds_orgao_emissao_rg, String pis, String ds_serie, Date dt_aposentadoria, String ds_naturalidade, Date recadastro, Date dt_nascimento, Date dt_foto, String ds_rg, String foto, String logradouro, String endereco, String numero, String complemento, String bairro, String cidade, String uf, String cep, String setor, Date admissao, String profissao, String fantasia, String empresa, String cnpj, String e_telefone, String e_logradouro, String e_endereco, String e_numero, String e_complemento, String e_bairro, String e_cidade, String e_uf, String e_cep, String titular, String codsocio, String nomeTitular, String parentesco, int matricula, String categoria, String grupo_categoria, Date filiacao, Date inativacao, boolean votante, String grau, BigDecimal nr_deconto, boolean desconto_folha, String tipo_cobranca, int cod_tipo_cobranca, String telefone2, String telefone3, String email, String contabilidade_nome, String contabilidade_contato, String contabilidade_telefone, String admissao_empresa_demissionada, String demissao_empresa_demissionada, String cnpj_empresa_demissionada, String empresa_demissionada, String idade) {
        this.sinLogo = sinLogo;
        this.sinSite = sinSite;
        this.sinNome = sinNome;
        this.sinEndereco = sinEndereco;
        this.sinLogradouro = sinLogradouro;
        this.sinNumero = sinNumero;
        this.sinComplemento = sinComplemento;
        this.sinBairro = sinBairro;
        this.sinCep = sinCep;
        this.sinCidade = sinCidade;
        this.sinCidadeUf = sinCidadeUf;
        this.sinDocumento = sinDocumento;
        this.codigo = codigo;
        this.cadastro = cadastro;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ds_uf_emissao_rg = ds_uf_emissao_rg;
        this.estado_civil = estado_civil;
        this.ctps = ctps;
        this.pai = pai;
        this.sexo = sexo;
        this.mae = mae;
        this.nacionalidade = nacionalidade;
        this.nit = nit;
        this.ds_orgao_emissao_rg = ds_orgao_emissao_rg;
        this.pis = pis;
        this.ds_serie = ds_serie;
        this.dt_aposentadoria = dt_aposentadoria;
        this.ds_naturalidade = ds_naturalidade;
        this.recadastro = recadastro;
        this.dt_nascimento = dt_nascimento;
        this.dt_foto = dt_foto;
        this.ds_rg = ds_rg;
        this.foto = foto;
        this.logradouro = logradouro;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.setor = setor;
        this.admissao = admissao;
        this.profissao = profissao;
        this.fantasia = fantasia;
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.e_telefone = e_telefone;
        this.e_logradouro = e_logradouro;
        this.e_endereco = e_endereco;
        this.e_numero = e_numero;
        this.e_complemento = e_complemento;
        this.e_bairro = e_bairro;
        this.e_cidade = e_cidade;
        this.e_uf = e_uf;
        this.e_cep = e_cep;
        this.titular = titular;
        this.codsocio = codsocio;
        this.nomeTitular = nomeTitular;
        this.parentesco = parentesco;
        this.matricula = matricula;
        this.categoria = categoria;
        this.grupo_categoria = grupo_categoria;
        this.filiacao = filiacao;
        this.inativacao = inativacao;
        this.votante = votante;
        this.grau = grau;
        this.nr_deconto = nr_deconto;
        this.desconto_folha = desconto_folha;
        this.tipo_cobranca = tipo_cobranca;
        this.cod_tipo_cobranca = cod_tipo_cobranca;
        this.telefone2 = telefone2;
        this.telefone3 = telefone3;
        this.email = email;
        this.contabilidade_nome = contabilidade_nome;
        this.contabilidade_contato = contabilidade_contato;
        this.contabilidade_telefone = contabilidade_telefone;
        this.admissao_empresa_demissionada = admissao_empresa_demissionada;
        this.demissao_empresa_demissionada = demissao_empresa_demissionada;
        this.cnpj_empresa_demissionada = cnpj_empresa_demissionada;
        this.empresa_demissionada = empresa_demissionada;
        this.idade = idade;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public String getSinSite() {
        return sinSite;
    }

    public void setSinSite(String sinSite) {
        this.sinSite = sinSite;
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

    public String getSinCidadeUf() {
        return sinCidadeUf;
    }

    public void setSinCidadeUf(String sinCidadeUf) {
        this.sinCidadeUf = sinCidadeUf;
    }

    public String getSinDocumento() {
        return sinDocumento;
    }

    public void setSinDocumento(String sinDocumento) {
        this.sinDocumento = sinDocumento;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDs_uf_emissao_rg() {
        return ds_uf_emissao_rg;
    }

    public void setDs_uf_emissao_rg(String ds_uf_emissao_rg) {
        this.ds_uf_emissao_rg = ds_uf_emissao_rg;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public String getCtps() {
        return ctps;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDs_orgao_emissao_rg() {
        return ds_orgao_emissao_rg;
    }

    public void setDs_orgao_emissao_rg(String ds_orgao_emissao_rg) {
        this.ds_orgao_emissao_rg = ds_orgao_emissao_rg;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getDs_serie() {
        return ds_serie;
    }

    public void setDs_serie(String ds_serie) {
        this.ds_serie = ds_serie;
    }

    public Date getDt_aposentadoria() {
        return dt_aposentadoria;
    }

    public void setDt_aposentadoria(Date dt_aposentadoria) {
        this.dt_aposentadoria = dt_aposentadoria;
    }

    public String getDs_naturalidade() {
        return ds_naturalidade;
    }

    public void setDs_naturalidade(String ds_naturalidade) {
        this.ds_naturalidade = ds_naturalidade;
    }

    public Date getRecadastro() {
        return recadastro;
    }

    public void setRecadastro(Date recadastro) {
        this.recadastro = recadastro;
    }

    public Date getDt_nascimento() {
        return dt_nascimento;
    }

    public void setDt_nascimento(Date dt_nascimento) {
        this.dt_nascimento = dt_nascimento;
    }

    public Date getDt_foto() {
        return dt_foto;
    }

    public void setDt_foto(Date dt_foto) {
        this.dt_foto = dt_foto;
    }

    public String getDs_rg() {
        return ds_rg;
    }

    public void setDs_rg(String ds_rg) {
        this.ds_rg = ds_rg;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public Date getAdmissao() {
        return admissao;
    }

    public void setAdmissao(Date admissao) {
        this.admissao = admissao;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
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

    public String getE_telefone() {
        return e_telefone;
    }

    public void setE_telefone(String e_telefone) {
        this.e_telefone = e_telefone;
    }

    public String getE_logradouro() {
        return e_logradouro;
    }

    public void setE_logradouro(String e_logradouro) {
        this.e_logradouro = e_logradouro;
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

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCodsocio() {
        return codsocio;
    }

    public void setCodsocio(String codsocio) {
        this.codsocio = codsocio;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGrupo_categoria() {
        return grupo_categoria;
    }

    public void setGrupo_categoria(String grupo_categoria) {
        this.grupo_categoria = grupo_categoria;
    }

    public Date getFiliacao() {
        return filiacao;
    }

    public void setFiliacao(Date filiacao) {
        this.filiacao = filiacao;
    }

    public Date getInativacao() {
        return inativacao;
    }

    public void setInativacao(Date inativacao) {
        this.inativacao = inativacao;
    }

    public boolean isVotante() {
        return votante;
    }

    public void setVotante(boolean votante) {
        this.votante = votante;
    }

    public String getGrau() {
        return grau;
    }

    public void setGrau(String grau) {
        this.grau = grau;
    }

    public BigDecimal getNr_deconto() {
        return nr_deconto;
    }

    public void setNr_deconto(BigDecimal nr_deconto) {
        this.nr_deconto = nr_deconto;
    }

    public boolean isDesconto_folha() {
        return desconto_folha;
    }

    public void setDesconto_folha(boolean desconto_folha) {
        this.desconto_folha = desconto_folha;
    }

    public String getTipo_cobranca() {
        return tipo_cobranca;
    }

    public void setTipo_cobranca(String tipo_cobranca) {
        this.tipo_cobranca = tipo_cobranca;
    }

    public int getCod_tipo_cobranca() {
        return cod_tipo_cobranca;
    }

    public void setCod_tipo_cobranca(int cod_tipo_cobranca) {
        this.cod_tipo_cobranca = cod_tipo_cobranca;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContabilidade_nome() {
        return contabilidade_nome;
    }

    public void setContabilidade_nome(String contabilidade_nome) {
        this.contabilidade_nome = contabilidade_nome;
    }

    public String getContabilidade_contato() {
        return contabilidade_contato;
    }

    public void setContabilidade_contato(String contabilidade_contato) {
        this.contabilidade_contato = contabilidade_contato;
    }

    public String getContabilidade_telefone() {
        return contabilidade_telefone;
    }

    public void setContabilidade_telefone(String contabilidade_telefone) {
        this.contabilidade_telefone = contabilidade_telefone;
    }

    public String getAdmissao_empresa_demissionada() {
        return admissao_empresa_demissionada;
    }

    public void setAdmissao_empresa_demissionada(String admissao_empresa_demissionada) {
        this.admissao_empresa_demissionada = admissao_empresa_demissionada;
    }

    public String getDemissao_empresa_demissionada() {
        return demissao_empresa_demissionada;
    }

    public void setDemissao_empresa_demissionada(String demissao_empresa_demissionada) {
        this.demissao_empresa_demissionada = demissao_empresa_demissionada;
    }

    public String getCnpj_empresa_demissionada() {
        return cnpj_empresa_demissionada;
    }

    public void setCnpj_empresa_demissionada(String cnpj_empresa_demissionada) {
        this.cnpj_empresa_demissionada = cnpj_empresa_demissionada;
    }

    public String getEmpresa_demissionada() {
        return empresa_demissionada;
    }

    public void setEmpresa_demissionada(String empresa_demissionada) {
        this.empresa_demissionada = empresa_demissionada;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }
}
