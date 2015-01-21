package br.com.rtools.impressao;

public class ParametroCertificados {

    private String detalhes_relatorio;
    private String cnpj;
    private String empresa;
    private String cidade;
    private String repis_status;
    private String certidao_tipo;
    private String data_emissao;
    private String data_resposta;
    private String ano;

    public ParametroCertificados(String detalhes_relatorio, String cnpj, String empresa, String cidade, String repis_status, String certidao_tipo, String data_emissao, String data_resposta, String ano) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.cnpj = cnpj;
        this.empresa = empresa;
        this.cidade = cidade;
        this.repis_status = repis_status;
        this.certidao_tipo = certidao_tipo;
        this.data_emissao = data_emissao;
        this.data_resposta = data_resposta;
        this.ano = ano;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRepis_status() {
        return repis_status;
    }

    public void setRepis_status(String repis_status) {
        this.repis_status = repis_status;
    }

    public String getCertidao_tipo() {
        return certidao_tipo;
    }

    public void setCertidao_tipo(String certidao_tipo) {
        this.certidao_tipo = certidao_tipo;
    }

    public String getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(String data_emissao) {
        this.data_emissao = data_emissao;
    }

    public String getData_resposta() {
        return data_resposta;
    }

    public void setData_resposta(String data_resposta) {
        this.data_resposta = data_resposta;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

}
