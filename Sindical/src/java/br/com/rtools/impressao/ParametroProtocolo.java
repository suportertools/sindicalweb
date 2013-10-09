package br.com.rtools.impressao;

public class ParametroProtocolo {
// MATRIZ -------------

    private String sinLogo;
    private String sinNome;
    private String sinSite;
    private String sinTipoDocumento;
    private String sinDocumento;
// FILIAL --------------
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
// Outros ---------------
    private String protocolo;// (id do agendamento)
    private String dataHomologacao;
    private String hora;// (da homologação)
    private String cnpjEmpresa;
    private String empresa;
    private String escritorio;
    private String funcionario;
    private String cpfFuncionario;
    private String documentos; //(documentosHomologacao em seg_registro)
    private String formasPagto; //(formasPagtoHomologacao em seg_registro)
    private String dataHoje;

    public ParametroProtocolo(String sinLogo, String sinNome, String sinSite, String sinTipoDocumento, String sinDocumento, String sinEndereco, String sinLogradouro, String sinNumero, String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinUF, String sinTelefone, String sinEmail, String protocolo, String dataHomologacao, String hora, String cnpjEmpresa, String empresa, String escritorio, String funcionario, String cpfFuncionario, String documentos, String formasPagto, String dataHoje) {
        this.sinLogo = sinLogo;
        this.sinNome = sinNome;
        this.sinSite = sinSite;
        this.sinTipoDocumento = sinTipoDocumento;
        this.sinDocumento = sinDocumento;
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
        this.protocolo = protocolo;
        this.dataHomologacao = dataHomologacao;
        this.hora = hora;
        this.cnpjEmpresa = cnpjEmpresa;
        this.empresa = empresa;
        this.escritorio = escritorio;
        this.funcionario = funcionario;
        this.cpfFuncionario = cpfFuncionario;
        this.documentos = documentos;
        this.formasPagto = formasPagto;
        this.dataHoje = dataHoje;
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

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getDataHomologacao() {
        return dataHomologacao;
    }

    public void setDataHomologacao(String dataHomologacao) {
        this.dataHomologacao = dataHomologacao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(String escritorio) {
        this.escritorio = escritorio;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getCpfFuncionario() {
        return cpfFuncionario;
    }

    public void setCpfFuncionario(String cpfFuncionario) {
        this.cpfFuncionario = cpfFuncionario;
    }

    public String getDocumentos() {
        return documentos;
    }

    public void setDocumentos(String documentos) {
        this.documentos = documentos;
    }

    public String getFormasPagto() {
        return formasPagto;
    }

    public void setFormasPagto(String formasPagto) {
        this.formasPagto = formasPagto;
    }

    public String getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(String dataHoje) {
        this.dataHoje = dataHoje;
    }
}
