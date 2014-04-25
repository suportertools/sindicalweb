package br.com.rtools.impressao;

import br.com.rtools.utilitarios.DataHoje;
import java.math.BigDecimal;
import java.util.Date;

public class ParametroPrevisaoPagto {

    private String fin_lote_dt_emissao;
    private BigDecimal fin_lote_nr_valor;
    private String pes_ds_nome;
    private String fin_plano_5;
    private String fin_lote_ds_documento;
    private String fin_movimento_dt_vencimento;
    private BigDecimal nr_valor_devido;
    private String fin_tipo_documento_ds_descricao;

    public ParametroPrevisaoPagto() {
        this.fin_lote_dt_emissao = DataHoje.data();
        this.fin_lote_nr_valor = new BigDecimal(0);
        this.pes_ds_nome = "";
        this.fin_plano_5 = "";
        this.fin_lote_ds_documento = "";
        this.fin_movimento_dt_vencimento = DataHoje.data();
        this.nr_valor_devido = new BigDecimal(0);
        this.fin_tipo_documento_ds_descricao = "";
    }

    public ParametroPrevisaoPagto(String fin_lote_dt_emissao, BigDecimal fin_lote_nr_valor, String pes_ds_nome, String fin_plano_5, String fin_lote_ds_documento, String fin_movimento_dt_vencimento, BigDecimal nr_valor_devido, String fin_tipo_documento_ds_descricao) {
        this.fin_lote_dt_emissao = fin_lote_dt_emissao;
        this.fin_lote_nr_valor = fin_lote_nr_valor;
        this.pes_ds_nome = pes_ds_nome;
        this.fin_plano_5 = fin_plano_5;
        this.fin_lote_ds_documento = fin_lote_ds_documento;
        this.fin_movimento_dt_vencimento = fin_movimento_dt_vencimento;
        this.nr_valor_devido = nr_valor_devido;
        this.fin_tipo_documento_ds_descricao = fin_tipo_documento_ds_descricao;
    }

    public String getFin_lote_dt_emissao() {
        return fin_lote_dt_emissao;
    }

    public void setFin_lote_dt_emissao(String fin_lote_dt_emissao) {
        this.fin_lote_dt_emissao = fin_lote_dt_emissao;
    }

    public BigDecimal getFin_lote_nr_valor() {
        return fin_lote_nr_valor;
    }

    public void setFin_lote_nr_valor(BigDecimal fin_lote_nr_valor) {
        this.fin_lote_nr_valor = fin_lote_nr_valor;
    }

    public String getPes_ds_nome() {
        return pes_ds_nome;
    }

    public void setPes_ds_nome(String pes_ds_nome) {
        this.pes_ds_nome = pes_ds_nome;
    }

    public String getFin_plano_5() {
        return fin_plano_5;
    }

    public void setFin_plano_5(String fin_plano_5) {
        this.fin_plano_5 = fin_plano_5;
    }

    public String getFin_lote_ds_documento() {
        return fin_lote_ds_documento;
    }

    public void setFin_lote_ds_documento(String fin_lote_ds_documento) {
        this.fin_lote_ds_documento = fin_lote_ds_documento;
    }

    public String getFin_movimento_dt_vencimento() {
        return fin_movimento_dt_vencimento;
    }

    public void setFin_movimento_dt_vencimento(String fin_movimento_dt_vencimento) {
        this.fin_movimento_dt_vencimento = fin_movimento_dt_vencimento;
    }

    public BigDecimal getNr_valor_devido() {
        return nr_valor_devido;
    }

    public void setNr_valor_devido(BigDecimal nr_valor_devido) {
        this.nr_valor_devido = nr_valor_devido;
    }

    public String getFin_tipo_documento_ds_descricao() {
        return fin_tipo_documento_ds_descricao;
    }

    public void setFin_tipo_documento_ds_descricao(String fin_tipo_documento_ds_descricao) {
        this.fin_tipo_documento_ds_descricao = fin_tipo_documento_ds_descricao;
    }
}
