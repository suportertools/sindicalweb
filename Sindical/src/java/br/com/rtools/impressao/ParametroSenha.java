package br.com.rtools.impressao;

public class ParametroSenha {

    private String sinLogo;
    private String sindNome; //– String (nome da filial do MAC)
    private String sindDocumento; //– String (CNPJ da Matriz)
    private String empresaNome;
    private String empresaDocumento;
    private String preposto;
    private String funcionario;
    private String usuarioNome;
    private String data;
    private String hora;
    private String senha;

    public ParametroSenha(String sinLogo, String sindNome, String sindDocumento, String empresaNome, String empresaDocumento, String preposto, String funcionario, String usuarioNome, String data, String hora, String senha) {
        this.sinLogo = sinLogo;
        this.sindNome = sindNome;
        this.sindDocumento = sindDocumento;
        this.empresaNome = empresaNome;
        this.empresaDocumento = empresaDocumento;
        this.preposto = preposto;
        this.funcionario = funcionario;
        this.usuarioNome = usuarioNome;
        this.data = data;
        this.hora = hora;
        this.senha = senha;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public String getSindNome() {
        return sindNome;
    }

    public void setSindNome(String sindNome) {
        this.sindNome = sindNome;
    }

    public String getSindDocumento() {
        return sindDocumento;
    }

    public void setSindDocumento(String sindDocumento) {
        this.sindDocumento = sindDocumento;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public String getEmpresaDocumento() {
        return empresaDocumento;
    }

    public void setEmpresaDocumento(String empresaDocumento) {
        this.empresaDocumento = empresaDocumento;
    }

    public String getPreposto() {
        return preposto;
    }

    public void setPreposto(String preposto) {
        this.preposto = preposto;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
