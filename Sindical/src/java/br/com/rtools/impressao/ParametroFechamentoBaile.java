package br.com.rtools.impressao;

public class ParametroFechamentoBaile {
    private String emissao;
    private String operador;
    private String codigo; 
    private String convidado; 
    private String status;
    private String mesa;
    private String convite;
    private String vencimento; 
    private String pagamento;
    private float valor;
    private String caixa;
    private String obs;

    public ParametroFechamentoBaile(String emissao, String operador, String codigo, String convidado, String status, String mesa, String convite, String vencimento, String pagamento, float valor, String caixa, String obs) {
        this.emissao = emissao;
        this.operador = operador;
        this.codigo = codigo;
        this.convidado = convidado;
        this.status = status;
        this.mesa = mesa;
        this.convite = convite;
        this.vencimento = vencimento;
        this.pagamento = pagamento;
        this.valor = valor;
        this.caixa = caixa;
        this.obs = obs;
    }
    
    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getConvidado() {
        return convidado;
    }

    public void setConvidado(String convidado) {
        this.convidado = convidado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getConvite() {
        return convite;
    }

    public void setConvite(String convite) {
        this.convite = convite;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
