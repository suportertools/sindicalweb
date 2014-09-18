package br.com.rtools.arrecadacao.lista;

import br.com.rtools.arrecadacao.ConvencaoCidade;

public class ListaContribuicao {

    private int sequencia;
    private ConvencaoCidade convencaoCidade;
    private String vencimento;
    private String referencia;
    private int contribuicao;

    public ListaContribuicao() {
        this.sequencia = 0;
        this.convencaoCidade = new ConvencaoCidade();
        this.vencimento = "";
        this.referencia = "";
        this.contribuicao = 0;
    }

    public ListaContribuicao(int sequencia, ConvencaoCidade convencaoCidade, String vencimento, String referencia, int contribuicao) {
        this.sequencia = sequencia;
        this.convencaoCidade = convencaoCidade;
        this.vencimento = vencimento;
        this.referencia = referencia;
        this.contribuicao = contribuicao;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public ConvencaoCidade getConvencaoCidade() {
        return convencaoCidade;
    }

    public void setConvencaoCidade(ConvencaoCidade convencaoCidade) {
        this.convencaoCidade = convencaoCidade;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getContribuicao() {
        return contribuicao;
    }

    public void setContribuicao(int contribuicao) {
        this.contribuicao = contribuicao;
    }

}
