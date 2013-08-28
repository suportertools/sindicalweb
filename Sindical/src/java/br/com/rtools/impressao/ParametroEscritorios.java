package br.com.rtools.impressao;

public class ParametroEscritorios {
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
    private int escId;
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
    private int escQuantidadeEmpresas;

    public ParametroEscritorios(String sinLogo, String sinNome, String sinEndereco, String sinLogradouro, String sinNumero, 
                                String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinUF,
                                String sinTelefone, String sinEmail, String sinSite, String sinTipoDocumento, String sinDocumento,
                                int escId, String escNome, String escEndereco, String escLogradouro, String escNumero, String escComplemento,
                                String escBairro, String escCep, String escCidade, String escUF, String escTelefone, String escEmail, int escQuantidadeEmpresas) {
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
        this.escId = escId;
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
        this.escQuantidadeEmpresas = escQuantidadeEmpresas;
    }


    public ParametroEscritorios() {
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
        this.escId = 0;
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
        this.escQuantidadeEmpresas = 0;
    }
    
    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public int getEscId() {
        return escId;
    }

    public void setEscId(int escId) {
        this.escId = escId;
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

    public int getEscQuantidadeEmpresas() {
        return escQuantidadeEmpresas;
    }

    public void setEscQuantidadeEmpresas(int escQuantidadeEmpresas) {
        this.escQuantidadeEmpresas = escQuantidadeEmpresas;
    }

}
