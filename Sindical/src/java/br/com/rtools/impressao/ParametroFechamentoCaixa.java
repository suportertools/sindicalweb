package br.com.rtools.impressao;

public class ParametroFechamentoCaixa {
    private String data = "";
    private String filial = "";
    private String caixa = "";
    private String usuario = "";
    private String valor_fechamento = "";
    private String valor_informado = "";
    private String saldo_anterior = "";
    private String dinheiro = "";
    private String cheque = "";
    private String cheque_pre = "";
    private String cartao_credito = "";
    private String cartao_debito = "";
    private String transferencia_entrada = "";
    private String transferencia_saida = "";
    private String pagamento = "";
    private String status = "";
    private String banco = "";
    private String banco_dados = "";
    private String descricao_caixa = "";
    private boolean mostrar_valor_informado = false;
    private String valor_caixa = "";
    private String valor_transferido = "";
    private String saldo_atual = "";
    private String deposito_bancario = "";
    private String doc_bancario = "";
    private String transferencia_bancaria = "";
    private String ticket = "";
    private String debito_automatico = "";
    private String boleto = "";
    
    public ParametroFechamentoCaixa(String data, String filial, String caixa, String usuario, String valor_fechamento, String valor_informado, String saldo_anterior,
            String dinheiro, String cheque, String cheque_pre, String cartao_credito, String cartao_debito, String transferencia_entrada, String transferencia_saida, String pagamento, String status, String banco, String banco_dados, 
            String descricao_caixa, boolean mostrar_valor_informado, String valor_caixa, String valor_transferido, String saldo_atual, String deposito_bancario, String doc_bancario, String transferencia_bancaria,
            String ticket, String debito_automatico, String boleto) {
        this.data = data;
        this.filial = filial;
        this.caixa = caixa;
        this.usuario = usuario;
        this.valor_fechamento = valor_fechamento;
        this.valor_informado = valor_informado;
        this.saldo_anterior = saldo_anterior;
        this.dinheiro = dinheiro;
        this.cheque = cheque;
        this.cheque_pre = cheque_pre;
        this.cartao_credito = cartao_credito;
        this.cartao_debito = cartao_debito;
        this.transferencia_entrada = transferencia_entrada;
        this.transferencia_saida = transferencia_saida;
        this.pagamento = pagamento;
        this.status = status;
        this.banco = banco;
        this.banco_dados = banco_dados;
        this.descricao_caixa = descricao_caixa;
        this.mostrar_valor_informado = mostrar_valor_informado;
        this.valor_caixa = valor_caixa;
        this.valor_transferido = valor_transferido;
        this.saldo_atual = saldo_atual;
        this.deposito_bancario = deposito_bancario;
        this.doc_bancario = doc_bancario;
        this.transferencia_bancaria = transferencia_bancaria;
        this.ticket = ticket;
        this.debito_automatico = debito_automatico;
        this.boleto = boleto;        
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getValor_fechamento() {
        return valor_fechamento;
    }

    public void setValor_fechamento(String valor_fechamento) {
        this.valor_fechamento = valor_fechamento;
    }

    public String getValor_informado() {
        return valor_informado;
    }

    public void setValor_informado(String valor_informado) {
        this.valor_informado = valor_informado;
    }

    public String getSaldo_anterior() {
        return saldo_anterior;
    }

    public void setSaldo_anterior(String saldo_anterior) {
        this.saldo_anterior = saldo_anterior;
    }

    public String getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(String dinheiro) {
        this.dinheiro = dinheiro;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public String getCheque_pre() {
        return cheque_pre;
    }

    public void setCheque_pre(String cheque_pre) {
        this.cheque_pre = cheque_pre;
    }

    public String getCartao_credito() {
        return cartao_credito;
    }

    public void setCartao_credito(String cartao_credito) {
        this.cartao_credito = cartao_credito;
    }

    public String getCartao_debito() {
        return cartao_debito;
    }

    public void setCartao_debito(String cartao_debito) {
        this.cartao_debito = cartao_debito;
    }

    public String getTransferencia_entrada() {
        return transferencia_entrada;
    }

    public void setTransferencia_entrada(String transferencia_entrada) {
        this.transferencia_entrada = transferencia_entrada;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getBanco_dados() {
        return banco_dados;
    }

    public void setBanco_dados(String banco_dados) {
        this.banco_dados = banco_dados;
    }

    public String getTransferencia_saida() {
        return transferencia_saida;
    }

    public void setTransferencia_saida(String transferencia_saida) {
        this.transferencia_saida = transferencia_saida;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getDescricao_caixa() {
        return descricao_caixa;
    }

    public void setDescricao_caixa(String descricao_caixa) {
        this.descricao_caixa = descricao_caixa;
    }

    public boolean isMostrar_valor_informado() {
        return mostrar_valor_informado;
    }

    public void setMostrar_valor_informado(boolean mostrar_valor_informado) {
        this.mostrar_valor_informado = mostrar_valor_informado;
    }

    public String getValor_caixa() {
        return valor_caixa;
    }

    public void setValor_caixa(String valor_caixa) {
        this.valor_caixa = valor_caixa;
    }

    public String getValor_transferido() {
        return valor_transferido;
    }

    public void setValor_transferido(String valor_transferido) {
        this.valor_transferido = valor_transferido;
    }

    public String getSaldo_atual() {
        return saldo_atual;
    }

    public void setSaldo_atual(String saldo_atual) {
        this.saldo_atual = saldo_atual;
    }

    public String getDeposito_bancario() {
        return deposito_bancario;
    }

    public void setDeposito_bancario(String deposito_bancario) {
        this.deposito_bancario = deposito_bancario;
    }

    public String getDoc_bancario() {
        return doc_bancario;
    }

    public void setDoc_bancario(String doc_bancario) {
        this.doc_bancario = doc_bancario;
    }

    public String getTransferencia_bancaria() {
        return transferencia_bancaria;
    }

    public void setTransferencia_bancaria(String transferencia_bancaria) {
        this.transferencia_bancaria = transferencia_bancaria;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getDebito_automatico() {
        return debito_automatico;
    }

    public void setDebito_automatico(String debito_automatico) {
        this.debito_automatico = debito_automatico;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    
}
