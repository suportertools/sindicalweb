package br.com.rtools.impressao;

public class ParametroHomologacao {

    private String dataInicial;
    private String dataFinal;
    private String data;
    private String hora;
    private String cnpj;
    private String empresa;
    private String funcionario;
    private String contato;
    private String telefone;
    private String homologador;
    private String obs;

    public ParametroHomologacao(String dataInicial, String dataFinal, String data, String hora, String cnpj, String empresa, String funcionario, String contato, String telefone, String homologador, String obs) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.data = data;
        this.hora = hora;
        this.cnpj = cnpj;
        this.empresa = empresa;
        this.funcionario = funcionario;
        this.contato = contato;
        this.telefone = telefone;
        this.homologador = homologador;
        this.obs = obs;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
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

    public String getHomologador() {
        return homologador;
    }

    public void setHomologador(String homologador) {
        this.homologador = homologador;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
