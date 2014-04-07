package br.com.rtools.impressao;

public class ParametroOposicao {

    private String detalheRelatorio;
    private String emissao;
    private String tipo;
    private String documento;
    private String empresa;
    private String funcionario;
    private String sexo;
    private String cpf;
    private String rg;
    private String ref_i;
    private String ref_f;

    public ParametroOposicao() {
        this.detalheRelatorio = "";
        this.emissao = "";
        this.tipo = "";
        this.documento = "";
        this.empresa = "";
        this.funcionario = "";
        this.sexo = "";
        this.cpf = "";
        this.rg = "";
        this.ref_i = "";
        this.ref_f = "";
    }

    public ParametroOposicao(String detalheRelatorio, String emissao, String tipo, String documento, String empresa, String funcionario, String sexo, String cpf, String rg, String ref_i, String ref_f) {
        this.detalheRelatorio = detalheRelatorio;
        this.emissao = emissao;
        this.tipo = tipo;
        this.documento = documento;
        this.empresa = empresa;
        this.funcionario = funcionario;
        this.sexo = sexo;
        this.cpf = cpf;
        this.rg = rg;
        this.ref_i = ref_i;
        this.ref_f = ref_f;
    }

    public String getDetalheRelatorio() {
        return detalheRelatorio;
    }

    public void setDetalheRelatorio(String detalheRelatorio) {
        this.detalheRelatorio = detalheRelatorio;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getRef_i() {
        return ref_i;
    }

    public void setRef_i(String ref_i) {
        this.ref_i = ref_i;
    }

    public String getRef_f() {
        return ref_f;
    }

    public void setRef_f(String ref_f) {
        this.ref_f = ref_f;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

}
