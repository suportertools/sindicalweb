package br.com.rtools.impressao;

import java.math.BigDecimal;
import java.util.Date;

public class ParametroAcordoAnalitico {

    private String cnpj;
    private String empresa;
    private int idAcordo;
    private String boleto;
    private String contribuicao;
    private Date dtImportacao;
    private Date dtRecebimento;
    private Date dtVencimento;
    private BigDecimal valorRecebido;
    private BigDecimal taxa;
    private BigDecimal repasse;
    private BigDecimal liquido;
    private Date dtFechamento;
    private Date dtInicio;
    private BigDecimal comissao;

    public ParametroAcordoAnalitico(String cnpj, String empresa, int idAcordo, String boleto, String contribuicao, Date dtImportacao, Date dtRecebimento, Date dtVencimento, BigDecimal valorRecebido, BigDecimal taxa, BigDecimal repasse, BigDecimal liquido, Date dtFechamento, Date dtInicio, BigDecimal comissao) {
        this.cnpj = cnpj;
        this.empresa = empresa;
        this.idAcordo = idAcordo;
        this.boleto = boleto;
        this.contribuicao = contribuicao;
        this.dtImportacao = dtImportacao;
        this.dtRecebimento = dtRecebimento;
        this.dtVencimento = dtVencimento;
        this.valorRecebido = valorRecebido;
        this.taxa = taxa;
        this.repasse = repasse;
        this.liquido = liquido;
        this.dtFechamento = dtFechamento;
        this.dtInicio = dtInicio;
        this.comissao = comissao;
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

    public int getIdAcordo() {
        return idAcordo;
    }

    public void setIdAcordo(int idAcordo) {
        this.idAcordo = idAcordo;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getContribuicao() {
        return contribuicao;
    }

    public void setContribuicao(String contribuicao) {
        this.contribuicao = contribuicao;
    }

    public Date getDtImportacao() {
        return dtImportacao;
    }

    public void setDtImportacao(Date dtImportacao) {
        this.dtImportacao = dtImportacao;
    }

    public Date getDtRecebimento() {
        return dtRecebimento;
    }

    public void setDtRecebimento(Date dtRecebimento) {
        this.dtRecebimento = dtRecebimento;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public BigDecimal getRepasse() {
        return repasse;
    }

    public void setRepasse(BigDecimal repasse) {
        this.repasse = repasse;
    }

    public BigDecimal getLiquido() {
        return liquido;
    }

    public void setLiquido(BigDecimal liquido) {
        this.liquido = liquido;
    }

    public Date getDtFechamento() {
        return dtFechamento;
    }

    public void setDtFechamento(Date dtFechamento) {
        this.dtFechamento = dtFechamento;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }
}
