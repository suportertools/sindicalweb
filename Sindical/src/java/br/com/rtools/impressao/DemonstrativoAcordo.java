package br.com.rtools.impressao;

import java.math.BigDecimal;

public class DemonstrativoAcordo {

    private int codacordo;
    private String data;
    private String contato;
    private String razao;
    private String cnpj;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private String telefone;
    private String obs;
    private String desc_contribuicao;
    private String boleto;
    private String vencto;
    private BigDecimal vlrpagar;
    private String sinLogo;
    private String sinNome;
    private String sinEndereco;
    private String sinLogradouro;
    private String sinNumero;
    private String sinComplemento;
    private String sinBairro;
    private String sinCep;
    private String sinCidade;
    private String sinUF;
    private String sinTelefone;
    private String sinEmail;
    private String sinSite;
    private String sinTipoDocumento;
    private String sinDocumento;
    private String escNome;
    private String escEndereco;
    private String escLogradouro;
    private String escNumero;
    private String escComplemento;
    private String escBairro;
    private String escCep;
    private String escCidade;
    private String escUF;
    private String escTelefone;
    private String escEmail;
    private BigDecimal valor;
    private BigDecimal multa;
    private BigDecimal juros;
    private BigDecimal correcao;
    private BigDecimal desconto;
    private String tipo;
    private String referencia;
    private String titulo;
    private String usuario;
    private String emailContato;

    public DemonstrativoAcordo(
            int codacordo,
            String data,
            String contato,
            String razao,
            String cnpj,
            String endereco,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String cep,
            String uf,
            String telefone,
            String obs,
            String desc_contribuicao,
            String boleto,
            String vencto,
            BigDecimal vlrpagar,
            String sinLogo,
            String sinNome,
            String sinEndereco,
            String sinLogradouro,
            String sinNumero,
            String sinComplemento,
            String sinBairro,
            String sinCep,
            String sinCidade,
            String sinUF,
            String sinTelefone,
            String sinEmail,
            String sinSite,
            String sinTipoDocumento,
            String sinDocumento,
            String escNome,
            String escEndereco,
            String escLogradouro,
            String escNumero,
            String escComplemento,
            String escBairro,
            String escCep,
            String escCidade,
            String escUF,
            String escTelefone,
            String escEmail,
            BigDecimal valor,
            BigDecimal multa,
            BigDecimal juros,
            BigDecimal correcao,
            BigDecimal desconto,
            String tipo,
            String referencia,
            String titulo,
            String usuario,
            String emailContato) {
        this.codacordo = codacordo;
        this.data = data;
        this.contato = contato;
        this.razao = razao;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.uf = uf;
        this.telefone = telefone;
        this.obs = obs;
        this.desc_contribuicao = desc_contribuicao;
        this.boleto = boleto;
        this.vencto = vencto;
        this.vlrpagar = vlrpagar;
        this.sinLogo = sinLogo;
        this.sinNome = sinNome;
        this.sinEndereco = sinEndereco;
        this.sinLogradouro = sinLogradouro;
        this.sinNumero = sinNumero;
        this.sinComplemento = sinComplemento;
        this.sinBairro = sinBairro;
        this.sinCep = sinCep;
        this.sinCidade = sinCidade;
        this.sinUF = sinUF;
        this.sinTelefone = sinTelefone;
        this.sinEmail = sinEmail;
        this.sinSite = sinSite;
        this.sinTipoDocumento = sinTipoDocumento;
        this.sinDocumento = sinDocumento;
        this.escNome = escNome;
        this.escEndereco = escEndereco;
        this.escLogradouro = escLogradouro;
        this.escNumero = escNumero;
        this.escComplemento = escComplemento;
        this.escBairro = escBairro;
        this.escCep = escCep;
        this.escCidade = escCidade;
        this.escUF = escUF;
        this.escTelefone = escTelefone;
        this.escEmail = escEmail;
        this.valor = valor;
        this.multa = multa;
        this.juros = juros;
        this.correcao = correcao;
        this.desconto = desconto;
        this.tipo = tipo;
        this.referencia = referencia;
        this.titulo = titulo;
        this.usuario = usuario;
        this.emailContato = emailContato;
    }

    public DemonstrativoAcordo() {
        this.codacordo = 0;
        this.data = "";
        this.contato = "";
        this.razao = "";
        this.cnpj = "";
        this.endereco = "";
        this.numero = "";
        this.complemento = "";
        this.bairro = "";
        this.cidade = "";
        this.cep = "";
        this.uf = "";
        this.telefone = "";
        this.obs = "";
        this.desc_contribuicao = "";
        this.boleto = "";
        this.vencto = "";
        this.vlrpagar = new BigDecimal(0);
        this.sinLogo = "";
        this.sinNome = "";
        this.sinEndereco = "";
        this.sinLogradouro = "";
        this.sinNumero = "";
        this.sinComplemento = "";
        this.sinBairro = "";
        this.sinCep = "";
        this.sinCidade = "";
        this.sinUF = "";
        this.sinTelefone = "";
        this.sinEmail = "";
        this.sinSite = "";
        this.sinTipoDocumento = "";
        this.sinDocumento = "";
        this.escNome = "";
        this.escEndereco = "";
        this.escLogradouro = "";
        this.escNumero = "";
        this.escComplemento = "";
        this.escBairro = "";
        this.escCep = "";
        this.escCidade = "";
        this.escUF = "";
        this.escTelefone = "";
        this.escEmail = "";
        this.valor = new BigDecimal(0);
        this.multa = new BigDecimal(0);
        this.juros = new BigDecimal(0);
        this.correcao = new BigDecimal(0);
        this.desconto = new BigDecimal(0);
        this.tipo = "";
        this.referencia = "";
        this.titulo = "";
        this.usuario = "";
        this.emailContato = "";
    }

    public int getCodacordo() {
        return codacordo;
    }

    public void setCodacordo(int codacordo) {
        this.codacordo = codacordo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getDesc_contribuicao() {
        return desc_contribuicao;
    }

    public void setDesc_contribuicao(String desc_contribuicao) {
        this.desc_contribuicao = desc_contribuicao;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getVencto() {
        return vencto;
    }

    public void setVencto(String vencto) {
        this.vencto = vencto;
    }

    public BigDecimal getVlrpagar() {
        return vlrpagar;
    }

    public void setVlrpagar(BigDecimal vlrpagar) {
        this.vlrpagar = vlrpagar;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
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

    public String getSinUF() {
        return sinUF;
    }

    public void setSinUF(String sinUF) {
        this.sinUF = sinUF;
    }

    public String getSinTelefone() {
        return sinTelefone;
    }

    public void setSinTelefone(String sinTelefone) {
        this.sinTelefone = sinTelefone;
    }

    public String getSinEmail() {
        return sinEmail;
    }

    public void setSinEmail(String sinEmail) {
        this.sinEmail = sinEmail;
    }

    public String getSinSite() {
        return sinSite;
    }

    public void setSinSite(String sinSite) {
        this.sinSite = sinSite;
    }

    public String getSinTipoDocumento() {
        return sinTipoDocumento;
    }

    public void setSinTipoDocumento(String sinTipoDocumento) {
        this.sinTipoDocumento = sinTipoDocumento;
    }

    public String getSinDocumento() {
        return sinDocumento;
    }

    public void setSinDocumento(String sinDocumento) {
        this.sinDocumento = sinDocumento;
    }

    public String getEscNome() {
        return escNome;
    }

    public void setEscNome(String escNome) {
        this.escNome = escNome;
    }

    public String getEscEndereco() {
        return escEndereco;
    }

    public void setEscEndereco(String escEndereco) {
        this.escEndereco = escEndereco;
    }

    public String getEscLogradouro() {
        return escLogradouro;
    }

    public void setEscLogradouro(String escLogradouro) {
        this.escLogradouro = escLogradouro;
    }

    public String getEscNumero() {
        return escNumero;
    }

    public void setEscNumero(String escNumero) {
        this.escNumero = escNumero;
    }

    public String getEscComplemento() {
        return escComplemento;
    }

    public void setEscComplemento(String escComplemento) {
        this.escComplemento = escComplemento;
    }

    public String getEscBairro() {
        return escBairro;
    }

    public void setEscBairro(String escBairro) {
        this.escBairro = escBairro;
    }

    public String getEscCep() {
        return escCep;
    }

    public void setEscCep(String escCep) {
        this.escCep = escCep;
    }

    public String getEscCidade() {
        return escCidade;
    }

    public void setEscCidade(String escCidade) {
        this.escCidade = escCidade;
    }

    public String getEscUF() {
        return escUF;
    }

    public void setEscUF(String escUF) {
        this.escUF = escUF;
    }

    public String getEscTelefone() {
        return escTelefone;
    }

    public void setEscTelefone(String escTelefone) {
        this.escTelefone = escTelefone;
    }

    public String getEscEmail() {
        return escEmail;
    }

    public void setEscEmail(String escEmail) {
        this.escEmail = escEmail;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public BigDecimal getCorrecao() {
        return correcao;
    }

    public void setCorrecao(BigDecimal correcao) {
        this.correcao = correcao;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
}
