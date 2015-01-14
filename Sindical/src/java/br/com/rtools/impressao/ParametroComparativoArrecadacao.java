package br.com.rtools.impressao;

public class ParametroComparativoArrecadacao {

    private String detalhes_relatorio;
    private String cnpj;            // 0
    private String empresa;         // 1
    private String contribuicao;    // 2
    private String referencia1;     // 3
    private String valor1;          // 4
    private String referencia2;     // 5
    private String valor2;          // 6
    private String percentual;      // 7

    public ParametroComparativoArrecadacao() {
        this.detalhes_relatorio = "";
        this.cnpj = "";
        this.empresa = "";
        this.contribuicao = "";
        this.referencia1 = "";
        this.valor1 = "";
        this.referencia2 = "";
        this.valor2 = "";
    }

    public ParametroComparativoArrecadacao(String detalhes_relatorio, String cnpj, String empresa, String contribuicao, String referencia1, String valor1, String referencia2, String valor2, String percentual) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.cnpj = cnpj;
        this.empresa = empresa;
        this.contribuicao = contribuicao;
        this.referencia1 = referencia1;
        this.valor1 = valor1;
        this.referencia2 = referencia2;
        this.valor2 = valor2;
        this.percentual = percentual;
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

    public String getContribuicao() {
        return contribuicao;
    }

    public void setContribuicao(String contribuicao) {
        this.contribuicao = contribuicao;
    }

    public String getReferencia1() {
        return referencia1;
    }

    public void setReferencia1(String referencia1) {
        this.referencia1 = referencia1;
    }

    public String getValor1() {
        return valor1;
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getReferencia2() {
        return referencia2;
    }

    public void setReferencia2(String referencia2) {
        this.referencia2 = referencia2;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }

    public String getPercentual() {
        return percentual;
    }

    public void setPercentual(String percentual) {
        this.percentual = percentual;
    }
}
