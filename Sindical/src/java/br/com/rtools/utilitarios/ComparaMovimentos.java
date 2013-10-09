package br.com.rtools.utilitarios;

public class ComparaMovimentos {

    private String arqBoleto;
    private float arqValorPago;
    private String arqDataPagamento;
    private String arqDataVencimento;
    private String movBoleto;
    private float movValorPago;
    private String movDataPagamento;
    private String movDataVencimento;
    private String movCnpj;
    private String movNome;
    private String movServico;
    private String movTipoServico;
    private String nomePasta;
    private String nomeArquivo;

    public ComparaMovimentos(String arqBoleto,
            float arqValorPago,
            String arqDataPagamento,
            String arqDataVencimento,
            String movBoleto,
            float movValorPago,
            String movDataPagamento,
            String movDataVencimento,
            String movCnpj,
            String movNome,
            String movServico,
            String movTipoServico,
            String nomePasta,
            String nomeArquivo) {
        this.arqBoleto = arqBoleto;
        this.arqValorPago = arqValorPago;
        this.arqDataPagamento = arqDataPagamento;
        this.arqDataVencimento = arqDataVencimento;
        this.movBoleto = movBoleto;
        this.movValorPago = movValorPago;
        this.movDataPagamento = movDataPagamento;
        this.movDataVencimento = movDataVencimento;
        this.movCnpj = movCnpj;
        this.movNome = movNome;
        this.movServico = movServico;
        this.movTipoServico = movTipoServico;
        this.nomePasta = nomePasta;
        this.nomeArquivo = nomeArquivo;
    }

    public ComparaMovimentos() {
        this.arqBoleto = "";
        this.arqValorPago = 0;
        this.arqDataPagamento = "";
        this.arqDataVencimento = "";
        this.movBoleto = "";
        this.movValorPago = 0;
        this.movDataPagamento = "";
        this.movDataVencimento = "";
        this.movCnpj = "";
        this.movNome = "";
        this.movServico = "";
        this.movTipoServico = "";
        this.nomePasta = "";
        this.nomeArquivo = "";
    }

    public String getArqBoleto() {
        return arqBoleto;
    }

    public void setArqBoleto(String arqBoleto) {
        this.arqBoleto = arqBoleto;
    }

    public float getArqValorPago() {
        return arqValorPago;
    }

    public void setArqValorPago(float arqValorPago) {
        this.arqValorPago = arqValorPago;
    }

    public String getArqDataPagamento() {
        return arqDataPagamento;
    }

    public void setArqDataPagamento(String arqDataPagamento) {
        this.arqDataPagamento = arqDataPagamento;
    }

    public String getArqDataVencimento() {
        return arqDataVencimento;
    }

    public void setArqDataVencimento(String arqDataVencimento) {
        this.arqDataVencimento = arqDataVencimento;
    }

    public String getMovBoleto() {
        return movBoleto;
    }

    public void setMovBoleto(String movBoleto) {
        this.movBoleto = movBoleto;
    }

    public float getMovValorPago() {
        return movValorPago;
    }

    public void setMovValorPago(float movValorPago) {
        this.movValorPago = movValorPago;
    }

    public String getMovDataPagamento() {
        return movDataPagamento;
    }

    public void setMovDataPagamento(String movDataPagamento) {
        this.movDataPagamento = movDataPagamento;
    }

    public String getMovDataVencimento() {
        return movDataVencimento;
    }

    public void setMovDataVencimento(String movDataVencimento) {
        this.movDataVencimento = movDataVencimento;
    }

    public String getMovCnpj() {
        return movCnpj;
    }

    public void setMovCnpj(String movCnpj) {
        this.movCnpj = movCnpj;
    }

    public String getMovNome() {
        return movNome;
    }

    public void setMovNome(String movNome) {
        this.movNome = movNome;
    }

    public String getMovServico() {
        return movServico;
    }

    public void setMovServico(String movServico) {
        this.movServico = movServico;
    }

    public String getMovTipoServico() {
        return movTipoServico;
    }

    public void setMovTipoServico(String movTipoServico) {
        this.movTipoServico = movTipoServico;
    }

    public String getNomePasta() {
        return nomePasta;
    }

    public void setNomePasta(String nomePasta) {
        this.nomePasta = nomePasta;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
}
