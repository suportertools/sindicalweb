package br.com.rtools.impressao;

public class ParametroHomologacao {

    private String detalhes_relatorio;
    private String dataFinal;
    private String data;
    private String hora;
    private String cnpj;
    private String empresa;
    private String funcionario;
    private String contato;
    private String telefone;
    private String operador;
    private String obs;
    private String status;

    public ParametroHomologacao(String detalhes_relatorio, String data, String hora, String cnpj, String empresa, String funcionario, String contato, String telefone, String operador, String obs, String status) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.data = data;
        this.hora = hora;
        this.cnpj = cnpj;
        this.empresa = empresa;
        this.funcionario = funcionario;
        this.contato = contato;
        this.telefone = telefone;
        this.operador = operador;
        this.obs = obs;
        this.status = status;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
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

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
