package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroRecibo {
    private String sinLogo;
    private String sinNome;
    private String sinEndereco;
    private String sinLogradouro;
    private String sinNumero;
    private String sinComplemento;
    private String sinBairro;
    private String sinCep;
    private String sinCidade;
    private String sinUF;
    private String sinTelefone;
    private String sinEmail;
    private String sinSite;
    private String sinDocumento;
    private String responsavel;
    private String idResponsavel;
    private String idBaixa;
    private String beneficiario;
    private String servico;
    private String vencimento;
    private BigDecimal valorBaixa;
    private String usuario;
    private String dataPagamento;
    private String horaPagamento;
    private String formaPagamento1;
    private String formaPagamento2;
    private String formaPagamento3;
    private String formaPagamento4;
    private String formaPagamento5;
    private String formaPagamento6;
    private String formaPagamento7;
    private String formaPagamento8;
    private String formaPagamento9;
    private String formaPagamento10;
    private String conveniada;
    private String lblVencimento;
    private String mensagem;
    private String documento;
    private String totalDinheiroTroco;
    private String troco;
    

    public ParametroRecibo(String sinLogo, String sinNome, String sinEndereco, String sinLogradouro, String sinNumero, String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinUF, 
                           String sinTelefone, String sinEmail, String sinSite, String sinDocumento, String responsavel, String idResponsavel, String idBaixa, String beneficiario, String servico, String vencimento, 
                           BigDecimal valorBaixa, String usuario, String dataPagamento, String horaPagamento, String formaPagamento1, String formaPagamento2, String formaPagamento3, String formaPagamento4, String formaPagamento5,
                           String formaPagamento6, String formaPagamento7, String formaPagamento8, String formaPagamento9, String formaPagamento10, String conveniada, String lblVencimento, String mensagem, String documento,
                           String totalDinheiroTroco, String troco) {
        this.sinLogo = sinLogo;
        this.sinNome = sinNome;
        this.sinEndereco = sinEndereco;
        this.sinLogradouro = sinLogradouro;
        this.sinNumero = sinNumero;
        this.sinComplemento = sinComplemento;
        this.sinBairro = sinBairro;
        this.sinCep = sinCep;
        this.sinCidade = sinCidade;
        this.sinUF = sinUF;
        this.sinTelefone = sinTelefone;
        this.sinEmail = sinEmail;
        this.sinSite = sinSite;
        this.sinDocumento = sinDocumento;
        this.responsavel = responsavel;
        this.idResponsavel = idResponsavel;
        this.idBaixa = idBaixa;
        this.beneficiario = beneficiario;
        this.servico = servico;
        this.vencimento = vencimento;
        this.valorBaixa = valorBaixa;
        this.usuario = usuario;
        this.dataPagamento = dataPagamento;
        this.horaPagamento = horaPagamento;
        this.formaPagamento1 = formaPagamento1;
        this.formaPagamento2 = formaPagamento2;
        this.formaPagamento3 = formaPagamento3;
        this.formaPagamento4 = formaPagamento4;
        this.formaPagamento5 = formaPagamento5;
        this.formaPagamento6 = formaPagamento6;
        this.formaPagamento7 = formaPagamento7;
        this.formaPagamento8 = formaPagamento8;
        this.formaPagamento9 = formaPagamento9;
        this.formaPagamento10 = formaPagamento10;
        this.conveniada = conveniada;
        this.lblVencimento = lblVencimento;
        this.mensagem = mensagem;
        this.documento = documento;
        this.totalDinheiroTroco = totalDinheiroTroco;
        this.troco = troco;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public String getSinNome() {
        return sinNome;
    }

    public void setSinNome(String sinNome) {
        this.sinNome = sinNome;
    }

    public String getSinEndereco() {
        return sinEndereco;
    }

    public void setSinEndereco(String sinEndereco) {
        this.sinEndereco = sinEndereco;
    }

    public String getSinLogradouro() {
        return sinLogradouro;
    }

    public void setSinLogradouro(String sinLogradouro) {
        this.sinLogradouro = sinLogradouro;
    }

    public String getSinNumero() {
        return sinNumero;
    }

    public void setSinNumero(String sinNumero) {
        this.sinNumero = sinNumero;
    }

    public String getSinComplemento() {
        return sinComplemento;
    }

    public void setSinComplemento(String sinComplemento) {
        this.sinComplemento = sinComplemento;
    }

    public String getSinBairro() {
        return sinBairro;
    }

    public void setSinBairro(String sinBairro) {
        this.sinBairro = sinBairro;
    }

    public String getSinCep() {
        return sinCep;
    }

    public void setSinCep(String sinCep) {
        this.sinCep = sinCep;
    }

    public String getSinCidade() {
        return sinCidade;
    }

    public void setSinCidade(String sinCidade) {
        this.sinCidade = sinCidade;
    }

    public String getSinUF() {
        return sinUF;
    }

    public void setSinUF(String sinUF) {
        this.sinUF = sinUF;
    }

    public String getSinTelefone() {
        return sinTelefone;
    }

    public void setSinTelefone(String sinTelefone) {
        this.sinTelefone = sinTelefone;
    }

    public String getSinEmail() {
        return sinEmail;
    }

    public void setSinEmail(String sinEmail) {
        this.sinEmail = sinEmail;
    }

    public String getSinSite() {
        return sinSite;
    }

    public void setSinSite(String sinSite) {
        this.sinSite = sinSite;
    }

    public String getSinDocumento() {
        return sinDocumento;
    }

    public void setSinDocumento(String sinDocumento) {
        this.sinDocumento = sinDocumento;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(String idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getIdBaixa() {
        return idBaixa;
    }

    public void setIdBaixa(String idBaixa) {
        this.idBaixa = idBaixa;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public BigDecimal getValorBaixa() {
        return valorBaixa;
    }

    public void setValorBaixa(BigDecimal valorBaixa) {
        this.valorBaixa = valorBaixa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getHoraPagamento() {
        return horaPagamento;
    }

    public void setHoraPagamento(String horaPagamento) {
        this.horaPagamento = horaPagamento;
    }
   
    public String getFormaPagamento1() {
        return formaPagamento1;
    }

    public void setFormaPagamento1(String formaPagamento1) {
        this.formaPagamento1 = formaPagamento1;
    }

    public String getFormaPagamento2() {
        return formaPagamento2;
    }

    public void setFormaPagamento2(String formaPagamento2) {
        this.formaPagamento2 = formaPagamento2;
    }

    public String getFormaPagamento3() {
        return formaPagamento3;
    }

    public void setFormaPagamento3(String formaPagamento3) {
        this.formaPagamento3 = formaPagamento3;
    }

    public String getFormaPagamento4() {
        return formaPagamento4;
    }

    public void setFormaPagamento4(String formaPagamento4) {
        this.formaPagamento4 = formaPagamento4;
    }

    public String getFormaPagamento5() {
        return formaPagamento5;
    }

    public void setFormaPagamento5(String formaPagamento5) {
        this.formaPagamento5 = formaPagamento5;
    }

    public String getFormaPagamento6() {
        return formaPagamento6;
    }

    public void setFormaPagamento6(String formaPagamento6) {
        this.formaPagamento6 = formaPagamento6;
    }

    public String getFormaPagamento7() {
        return formaPagamento7;
    }

    public void setFormaPagamento7(String formaPagamento7) {
        this.formaPagamento7 = formaPagamento7;
    }

    public String getFormaPagamento8() {
        return formaPagamento8;
    }

    public void setFormaPagamento8(String formaPagamento8) {
        this.formaPagamento8 = formaPagamento8;
    }

    public String getFormaPagamento9() {
        return formaPagamento9;
    }

    public void setFormaPagamento9(String formaPagamento9) {
        this.formaPagamento9 = formaPagamento9;
    }

    public String getFormaPagamento10() {
        return formaPagamento10;
    }

    public void setFormaPagamento10(String formaPagamento10) {
        this.formaPagamento10 = formaPagamento10;
    }

    public String getConveniada() {
        return conveniada;
    }

    public void setConveniada(String conveniada) {
        this.conveniada = conveniada;
    }

    public String getLblVencimento() {
        return lblVencimento;
    }

    public void setLblVencimento(String lblVencimento) {
        this.lblVencimento = lblVencimento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTotalDinheiroTroco() {
        return totalDinheiroTroco;
    }

    public void setTotalDinheiroTroco(String totalDinheiroTroco) {
        this.totalDinheiroTroco = totalDinheiroTroco;
    }

    public String getTroco() {
        return troco;
    }

    public void setTroco(String troco) {
        this.troco = troco;
    }


  
    
}
