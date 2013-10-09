package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroMovimentos {

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
    private String sinTipoDocumento;
    private String sinDocumento;
    private int jurId = 0;
    private String jurNome;
    private String jurEndereco;
    private String jurLogradouro;
    private String jurNumero;
    private String jurComplemento;
    private String jurBairro;
    private String jurCep;
    private String jurCidade;
    private String jurUF;
    private String jurTelefone;
    private String jurEmail;
    private String jurTipoDocumento;
    private String jurDocumento;
    private int jurIdCnae = 0;
    private String jurNumeroCnae;
    private String jurCnae;
    private int escId = 0;
    private String escNome;
    private String escEndereco;
    private String escLogradouro;
    private String escNumero;
    private String escComplemento;
    private String escBairro;
    private String escCep;
    private String escCidade;
    private String escUF;
    private String escTelefone;
    private String escEmail;
    private String movNumeroBoleto;
    private String movServico;
    private String movTipoServico;
    private String movReferencia;
    private String movVencimento;
    private String movQuitacao;
    private BigDecimal movValorRecebido;
    private BigDecimal movTaxa;
    private String movImportacao;
    private String movUsuario;
    private BigDecimal movJuros;
    private BigDecimal movMulta;
    private BigDecimal movCorrecao;
    private BigDecimal movValorTotal;
    private BigDecimal movRepasse;
    private BigDecimal movLiquido;
    private boolean totaliza;

    public ParametroMovimentos(String sinLogo, String sinNome, String sinEndereco, String sinLogradouro, String sinNumero,
            String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinUF, String sinTelefone,
            String sinEmail, String sinSite, String sinTipoDocumento, String sinDocumento, int jurId, String jurNome, String jurEndereco,
            String jurLogradouro, String jurNumero, String jurComplemento, String jurBairro, String jurCep, String jurCidade,
            String jurUF, String jurTelefone, String jurEmail, String jurTipoDocumento, String jurDocumento, int jurIdCnae, String jurNumeroCnae,
            String jurCnae, int escId, String escNome, String escEndereco, String escLogradouro, String escNumero, String escComplemento,
            String escBairro, String escCep, String escCidade, String escUF, String escTelefone, String escEmail, String movNumeroBoleto,
            String movServico, String movTipoServico, String movReferencia, String movVencimento, String movQuitacao, BigDecimal movValorRecebido,
            BigDecimal movTaxa, String movImportacao, String movUsuario, BigDecimal movJuros, BigDecimal movMulta, BigDecimal movCorrecao, BigDecimal movValorTotal, BigDecimal movRepasse, BigDecimal movLiquido, boolean totaliza) {
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
        this.sinTipoDocumento = sinTipoDocumento;
        this.sinDocumento = sinDocumento;
        this.jurId = jurId;
        this.jurNome = jurNome;
        this.jurEndereco = jurEndereco;
        this.jurLogradouro = jurLogradouro;
        this.jurNumero = jurNumero;
        this.jurComplemento = jurComplemento;
        this.jurBairro = jurBairro;
        this.jurCep = jurCep;
        this.jurCidade = jurCidade;
        this.jurUF = jurUF;
        this.jurTelefone = jurTelefone;
        this.jurEmail = jurEmail;
        this.jurTipoDocumento = jurTipoDocumento;
        this.jurDocumento = jurDocumento;
        this.jurIdCnae = jurIdCnae;
        this.jurNumeroCnae = jurNumeroCnae;
        this.jurCnae = jurCnae;
        this.escId = escId;
        this.escNome = escNome;
        this.escEndereco = escEndereco;
        this.escLogradouro = escLogradouro;
        this.escNumero = escNumero;
        this.escComplemento = escComplemento;
        this.escBairro = escBairro;
        this.escCep = escCep;
        this.escCidade = escCidade;
        this.escUF = escUF;
        this.escTelefone = escTelefone;
        this.escEmail = escEmail;
        this.movNumeroBoleto = movNumeroBoleto;
        this.movServico = movServico;
        this.movTipoServico = movTipoServico;
        this.movReferencia = movReferencia;
        this.movVencimento = movVencimento;
        this.movQuitacao = movQuitacao;
        this.movValorRecebido = movValorRecebido;
        this.movTaxa = movTaxa;
        this.movImportacao = movImportacao;
        this.movUsuario = movUsuario;
        this.movJuros = movJuros;
        this.movMulta = movMulta;
        this.movCorrecao = movCorrecao;
        this.movValorTotal = movValorTotal;
        this.movRepasse = movRepasse;
        this.movLiquido = movLiquido;
        this.totaliza = totaliza;
    }

    public ParametroMovimentos() {
        this.sinLogo = "";
        this.sinNome = "";
        this.sinEndereco = "";
        this.sinLogradouro = "";
        this.sinNumero = "";
        this.sinComplemento = "";
        this.sinBairro = "";
        this.sinCep = "";
        this.sinCidade = "";
        this.sinUF = "";
        this.sinTelefone = "";
        this.sinEmail = "";
        this.sinSite = "";
        this.sinTipoDocumento = "";
        this.sinDocumento = "";
        this.jurId = 0;
        this.jurNome = "";
        this.jurEndereco = "";
        this.jurLogradouro = "";
        this.jurNumero = "";
        this.jurComplemento = "";
        this.jurBairro = "";
        this.jurCep = "";
        this.jurCidade = "";
        this.jurUF = "";
        this.jurTelefone = "";
        this.jurEmail = "";
        this.jurTipoDocumento = "";
        this.jurDocumento = "";
        this.jurIdCnae = 0;
        this.jurNumeroCnae = "";
        this.jurCnae = "";
        this.escId = 0;
        this.escNome = "";
        this.escEndereco = "";
        this.escLogradouro = "";
        this.escNumero = "";
        this.escComplemento = "";
        this.escBairro = "";
        this.escCep = "";
        this.escCidade = "";
        this.escUF = "";
        this.escTelefone = "";
        this.escEmail = "";
        this.movNumeroBoleto = "";
        this.movServico = "";
        this.movTipoServico = "";
        this.movReferencia = "";
        this.movVencimento = "";
        this.movQuitacao = "";
        this.movValorRecebido = new BigDecimal(0);
        this.movTaxa = new BigDecimal(0);
        this.movImportacao = "";
        this.movUsuario = "";
        this.movJuros = new BigDecimal(0);
        this.movMulta = new BigDecimal(0);
        this.movCorrecao = new BigDecimal(0);
        this.movValorTotal = new BigDecimal(0);
        this.movRepasse = new BigDecimal(0);
        this.movLiquido = new BigDecimal(0);
        this.totaliza = true;
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

    public String getSinTipoDocumento() {
        return sinTipoDocumento;
    }

    public void setSinTipoDocumento(String sinTipoDocumento) {
        this.sinTipoDocumento = sinTipoDocumento;
    }

    public String getSinDocumento() {
        return sinDocumento;
    }

    public void setSinDocumento(String sinDocumento) {
        this.sinDocumento = sinDocumento;
    }

    public int getJurId() {
        return jurId;
    }

    public void setJurId(int jurId) {
        this.jurId = jurId;
    }

    public String getJurNome() {
        return jurNome;
    }

    public void setJurNome(String jurNome) {
        this.jurNome = jurNome;
    }

    public String getJurEndereco() {
        return jurEndereco;
    }

    public void setJurEndereco(String jurEndereco) {
        this.jurEndereco = jurEndereco;
    }

    public String getJurLogradouro() {
        return jurLogradouro;
    }

    public void setJurLogradouro(String jurLogradouro) {
        this.jurLogradouro = jurLogradouro;
    }

    public String getJurNumero() {
        return jurNumero;
    }

    public void setJurNumero(String jurNumero) {
        this.jurNumero = jurNumero;
    }

    public String getJurComplemento() {
        return jurComplemento;
    }

    public void setJurComplemento(String jurComplemento) {
        this.jurComplemento = jurComplemento;
    }

    public String getJurBairro() {
        return jurBairro;
    }

    public void setJurBairro(String jurBairro) {
        this.jurBairro = jurBairro;
    }

    public String getJurCep() {
        return jurCep;
    }

    public void setJurCep(String jurCep) {
        this.jurCep = jurCep;
    }

    public String getJurCidade() {
        return jurCidade;
    }

    public void setJurCidade(String jurCidade) {
        this.jurCidade = jurCidade;
    }

    public String getJurUF() {
        return jurUF;
    }

    public void setJurUF(String jurUF) {
        this.jurUF = jurUF;
    }

    public String getJurTelefone() {
        return jurTelefone;
    }

    public void setJurTelefone(String jurTelefone) {
        this.jurTelefone = jurTelefone;
    }

    public String getJurEmail() {
        return jurEmail;
    }

    public void setJurEmail(String jurEmail) {
        this.jurEmail = jurEmail;
    }

    public String getJurTipoDocumento() {
        return jurTipoDocumento;
    }

    public void setJurTipoDocumento(String jurTipoDocumento) {
        this.jurTipoDocumento = jurTipoDocumento;
    }

    public String getJurDocumento() {
        return jurDocumento;
    }

    public void setJurDocumento(String jurDocumento) {
        this.jurDocumento = jurDocumento;
    }

    public int getJurIdCnae() {
        return jurIdCnae;
    }

    public void setJurIdCnae(int jurIdCnae) {
        this.jurIdCnae = jurIdCnae;
    }

    public String getJurNumeroCnae() {
        return jurNumeroCnae;
    }

    public void setJurNumeroCnae(String jurNumeroCnae) {
        this.jurNumeroCnae = jurNumeroCnae;
    }

    public String getJurCnae() {
        return jurCnae;
    }

    public void setJurCnae(String jurCnae) {
        this.jurCnae = jurCnae;
    }

    public int getEscId() {
        return escId;
    }

    public void setEscId(int escId) {
        this.escId = escId;
    }

    public String getEscNome() {
        return escNome;
    }

    public void setEscNome(String escNome) {
        this.escNome = escNome;
    }

    public String getEscEndereco() {
        return escEndereco;
    }

    public void setEscEndereco(String escEndereco) {
        this.escEndereco = escEndereco;
    }

    public String getEscLogradouro() {
        return escLogradouro;
    }

    public void setEscLogradouro(String escLogradouro) {
        this.escLogradouro = escLogradouro;
    }

    public String getEscNumero() {
        return escNumero;
    }

    public void setEscNumero(String escNumero) {
        this.escNumero = escNumero;
    }

    public String getEscComplemento() {
        return escComplemento;
    }

    public void setEscComplemento(String escComplemento) {
        this.escComplemento = escComplemento;
    }

    public String getEscBairro() {
        return escBairro;
    }

    public void setEscBairro(String escBairro) {
        this.escBairro = escBairro;
    }

    public String getEscCep() {
        return escCep;
    }

    public void setEscCep(String escCep) {
        this.escCep = escCep;
    }

    public String getEscCidade() {
        return escCidade;
    }

    public void setEscCidade(String escCidade) {
        this.escCidade = escCidade;
    }

    public String getEscUF() {
        return escUF;
    }

    public void setEscUF(String escUF) {
        this.escUF = escUF;
    }

    public String getEscTelefone() {
        return escTelefone;
    }

    public void setEscTelefone(String escTelefone) {
        this.escTelefone = escTelefone;
    }

    public String getEscEmail() {
        return escEmail;
    }

    public void setEscEmail(String escEmail) {
        this.escEmail = escEmail;
    }

    public String getMovNumeroBoleto() {
        return movNumeroBoleto;
    }

    public void setMovNumeroBoleto(String movNumeroBoleto) {
        this.movNumeroBoleto = movNumeroBoleto;
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

    public String getMovReferencia() {
        return movReferencia;
    }

    public void setMovReferencia(String movReferencia) {
        this.movReferencia = movReferencia;
    }

    public String getMovVencimento() {
        return movVencimento;
    }

    public void setMovVencimento(String movVencimento) {
        this.movVencimento = movVencimento;
    }

    public String getMovQuitacao() {
        return movQuitacao;
    }

    public void setMovQuitacao(String movQuitacao) {
        this.movQuitacao = movQuitacao;
    }

    public BigDecimal getMovValorRecebido() {
        return movValorRecebido;
    }

    public void setMovValorRecebido(BigDecimal movValorRecebido) {
        this.movValorRecebido = movValorRecebido;
    }

    public BigDecimal getMovTaxa() {
        return movTaxa;
    }

    public void setMovTaxa(BigDecimal movTaxa) {
        this.movTaxa = movTaxa;
    }

    public String getMovImportacao() {
        return movImportacao;
    }

    public void setMovImportacao(String movImportacao) {
        this.movImportacao = movImportacao;
    }

    public String getMovUsuario() {
        return movUsuario;
    }

    public void setMovUsuario(String movUsuario) {
        this.movUsuario = movUsuario;
    }

    public BigDecimal getMovJuros() {
        return movJuros;
    }

    public void setMovJuros(BigDecimal movJuros) {
        this.movJuros = movJuros;
    }

    public BigDecimal getMovMulta() {
        return movMulta;
    }

    public void setMovMulta(BigDecimal movMulta) {
        this.movMulta = movMulta;
    }

    public BigDecimal getMovCorrecao() {
        return movCorrecao;
    }

    public void setMovCorrecao(BigDecimal movCorrecao) {
        this.movCorrecao = movCorrecao;
    }

    public BigDecimal getMovValorTotal() {
        return movValorTotal;
    }

    public void setMovValorTotal(BigDecimal movValorTotal) {
        this.movValorTotal = movValorTotal;
    }

    public boolean isTotaliza() {
        return totaliza;
    }

    public void setTotaliza(boolean totaliza) {
        this.totaliza = totaliza;
    }

    public BigDecimal getMovRepasse() {
        return movRepasse;
    }

    public void setMovRepasse(BigDecimal movRepasse) {
        this.movRepasse = movRepasse;
    }

    public BigDecimal getMovLiquido() {
        return movLiquido;
    }

    public void setMovLiquido(BigDecimal movLiquido) {
        this.movLiquido = movLiquido;
    }
}
