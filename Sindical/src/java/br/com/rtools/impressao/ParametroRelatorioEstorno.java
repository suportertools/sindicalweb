package br.com.rtools.impressao;

public class ParametroRelatorioEstorno {
    private String dataLancamento;
    private String dataBaixa;
    private String responsavel;
    private String titular;
    private String beneficiario;
    private String nrIdBaixa;
    private String usuario;
    private String operador;
    private String caixa;
    private String motivoEstorno;
    private String vencimento;
    private float valor;
    
    public ParametroRelatorioEstorno(String dataLancamento, String dataBaixa, String responsavel, String titular, String beneficiario, String nrIdBaixa, String usuario, String operador, String caixa, String motivoEstorno, String vencimento, float valor) {
        this.dataLancamento = dataLancamento;
        this.dataBaixa = dataBaixa;
        this.responsavel = responsavel;
        this.titular = titular;
        this.beneficiario = beneficiario;
        this.nrIdBaixa = nrIdBaixa;
        this.usuario = usuario;
        this.operador = operador;
        this.caixa = caixa;
        this.motivoEstorno = motivoEstorno;
        this.vencimento = vencimento;
        this.valor = valor;
    }
    
    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(String dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getNrIdBaixa() {
        return nrIdBaixa;
    }

    public void setNrIdBaixa(String nrIdBaixa) {
        this.nrIdBaixa = nrIdBaixa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getMotivoEstorno() {
        return motivoEstorno;
    }

    public void setMotivoEstorno(String motivoEstorno) {
        this.motivoEstorno = motivoEstorno;
    }
    
    
    
}
