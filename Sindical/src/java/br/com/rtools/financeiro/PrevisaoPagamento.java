package br.com.rtools.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_previsao_pagamento")
public class PrevisaoPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_tipo_pagamento", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private TipoPagamento tipoPagamento;
    @JoinColumn(name = "id_conta_banco", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private ContaBanco contaBanco;
    @Column(name = "ds_cheque", length = 100, nullable = true)
    private String cheque;

    public PrevisaoPagamento() {
        this.id = -1;
        this.movimento = new Movimento();
        this.tipoPagamento = new TipoPagamento();
        this.contaBanco = new ContaBanco();
        this.cheque = "";
    }

    public PrevisaoPagamento(int id, Movimento movimento, TipoPagamento tipoPagamento, ContaBanco contaBanco, String cheque) {
        this.id = id;
        this.movimento = movimento;
        this.tipoPagamento = tipoPagamento;
        this.contaBanco = contaBanco;
        this.cheque = cheque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public ContaBanco getContaBanco() {
        return contaBanco;
    }

    public void setContaBanco(ContaBanco contaBanco) {
        this.contaBanco = contaBanco;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    @Override
    public String toString() {
        return "PrevisaoPagamento{" + "id=" + id + ", movimento=" + movimento + ", tipoPagamento=" + tipoPagamento + ", contaBanco=" + contaBanco + ", cheque=" + cheque + '}';
    }
}
