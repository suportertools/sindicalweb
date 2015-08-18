package br.com.rtools.associativo.lista;

import br.com.rtools.financeiro.Movimento;

public class ListaMovimentosReceberSocial {

    private Boolean selected; // 0
    private Movimento movimento; // 1
    private String servico_descricao; // 2
    private String tipo_servico_descricao; // 3
    private String referencia; // 4
    private String vencimento; // 5
    private String valor; // 6
    private String acrescimo; // 7
    private String desconto; // 8
    private String valor_calculado; // 9
    private String data_baixa; // 10
    private String valor_baixa; // 11
    private String es; // 12
    private String responsavel_nome; // 13
    private String beneficiario_nome; // 14
    private String titular_id; // 15
    private String data_criacao; // 16
    private String boleto; // 17
    private String dias_atraso; // 18
    private String multa; // 19
    private String juros; // 20
    private String correcao; // 21
    private String caixa_nome; // 22
    private String documento; // 23
    private String valor_calculado_original; // 24
    private Boolean disabled; // 25
    private String baixa_id; // 26
    private String lote_id; // 27
    private String styleClass; // 28
    private Boolean check_juros; // 29
    private String titular_nome; // 30
    private String beneficiario_id; // 31

    public ListaMovimentosReceberSocial() {
        this.selected = false;
        this.movimento = new Movimento();
        this.servico_descricao = "";
        this.tipo_servico_descricao = "";
        this.referencia = "";
        this.vencimento = "";
        this.valor = "";
        this.acrescimo = "";
        this.desconto = "";
        this.valor_calculado = "";
        this.data_baixa = "";
        this.valor_baixa = "";
        this.es = "";
        this.responsavel_nome = "";
        this.beneficiario_nome = "";
        this.titular_id = "";
        this.data_criacao = "";
        this.boleto = "";
        this.dias_atraso = "";
        this.multa = "";
        this.juros = "";
        this.correcao = "";
        this.caixa_nome = "";
        this.documento = "";
        this.valor_calculado_original = "";
        this.disabled = false;
        this.baixa_id = "";
        this.lote_id = "";
        this.styleClass = "";
        this.check_juros = false;
        this.titular_nome = "";
        this.beneficiario_id = "";
    }

    /**
     *
     * @param selected
     * @param movimento
     * @param servico_descricao
     * @param tipo_servico_descricao
     * @param referencia
     * @param vencimento
     * @param valor
     * @param beneficiario_nome
     * @param acrescimo
     * @param desconto
     * @param data_baixa
     * @param valor_calculado
     * @param valor_baixa
     * @param es
     * @param caixa_nome
     * @param disabled
     * @param responsavel_nome
     * @param titular_id
     * @param data_criacao
     * @param boleto
     * @param lote_id
     * @param multa
     * @param dias_atraso
     * @param documento
     * @param valor_calculado_original
     * @param juros
     * @param baixa_id
     * @param correcao
     * @param titular_nome
     * @param check_juros
     * @param styleClass
     * @param beneficiario_id
     */
    public ListaMovimentosReceberSocial(Boolean selected, Movimento movimento, String servico_descricao, String tipo_servico_descricao, String referencia, String vencimento, String valor, String acrescimo, String desconto, String valor_calculado, String data_baixa, String valor_baixa, String es, String responsavel_nome, String beneficiario_nome, String titular_id, String data_criacao, String boleto, String dias_atraso, String multa, String juros, String correcao, String caixa_nome, String documento, String valor_calculado_original, Boolean disabled, String baixa_id, String lote_id, String styleClass, Boolean check_juros, String titular_nome, String beneficiario_id) {
        this.selected = selected;
        this.movimento = movimento;
        this.servico_descricao = servico_descricao;
        this.tipo_servico_descricao = tipo_servico_descricao;
        this.referencia = referencia;
        this.vencimento = vencimento;
        this.valor = valor;
        this.acrescimo = acrescimo;
        this.desconto = desconto;
        this.valor_calculado = valor_calculado;
        this.data_baixa = data_baixa;
        this.valor_baixa = valor_baixa;
        this.es = es;
        this.responsavel_nome = responsavel_nome;
        this.beneficiario_nome = beneficiario_nome;
        this.titular_id = titular_id;
        this.data_criacao = data_criacao;
        this.boleto = boleto;
        this.dias_atraso = dias_atraso;
        this.multa = multa;
        this.juros = juros;
        this.correcao = correcao;
        this.caixa_nome = caixa_nome;
        this.documento = documento;
        this.valor_calculado_original = valor_calculado_original;
        this.disabled = disabled;
        this.baixa_id = baixa_id;
        this.lote_id = lote_id;
        this.styleClass = styleClass;
        this.check_juros = check_juros;
        this.titular_nome = titular_nome;
        this.beneficiario_id = beneficiario_id;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getServico_descricao() {
        return servico_descricao;
    }

    public void setServico_descricao(String servico_descricao) {
        this.servico_descricao = servico_descricao;
    }

    public String getTipo_servico_descricao() {
        return tipo_servico_descricao;
    }

    public void setTipo_servico_descricao(String tipo_servico_descricao) {
        this.tipo_servico_descricao = tipo_servico_descricao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(String acrescimo) {
        this.acrescimo = acrescimo;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getValor_calculado() {
        return valor_calculado;
    }

    public void setValor_calculado(String valor_calculado) {
        this.valor_calculado = valor_calculado;
    }

    public String getData_baixa() {
        return data_baixa;
    }

    public void setData_baixa(String data_baixa) {
        this.data_baixa = data_baixa;
    }

    public String getValor_baixa() {
        return valor_baixa;
    }

    public void setValor_baixa(String valor_baixa) {
        this.valor_baixa = valor_baixa;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getResponsavel_nome() {
        return responsavel_nome;
    }

    public void setResponsavel_nome(String responsavel_nome) {
        this.responsavel_nome = responsavel_nome;
    }

    public String getBeneficiario_nome() {
        return beneficiario_nome;
    }

    public void setBeneficiario_nome(String beneficiario_nome) {
        this.beneficiario_nome = beneficiario_nome;
    }

    public String getTitular_id() {
        return titular_id;
    }

    public void setTitular_id(String titular_id) {
        this.titular_id = titular_id;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getDias_atraso() {
        return dias_atraso;
    }

    public void setDias_atraso(String dias_atraso) {
        this.dias_atraso = dias_atraso;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getCaixa_nome() {
        return caixa_nome;
    }

    public void setCaixa_nome(String caixa_nome) {
        this.caixa_nome = caixa_nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getValor_calculado_original() {
        return valor_calculado_original;
    }

    public void setValor_calculado_original(String valor_calculado_original) {
        this.valor_calculado_original = valor_calculado_original;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getBaixa_id() {
        return baixa_id;
    }

    public void setBaixa_id(String baixa_id) {
        this.baixa_id = baixa_id;
    }

    public String getLote_id() {
        return lote_id;
    }

    public void setLote_id(String lote_id) {
        this.lote_id = lote_id;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public Boolean getCheck_juros() {
        return check_juros;
    }

    public void setCheck_juros(Boolean check_juros) {
        this.check_juros = check_juros;
    }

    public String getTitular_nome() {
        return titular_nome;
    }

    public void setTitular_nome(String titular_nome) {
        this.titular_nome = titular_nome;
    }

    public String getBeneficiario_id() {
        return beneficiario_id;
    }

    public void setBeneficiario_id(String beneficiario_id) {
        this.beneficiario_id = beneficiario_id;
    }
}
