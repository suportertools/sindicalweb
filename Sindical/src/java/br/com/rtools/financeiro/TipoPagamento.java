package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_tipo_pagamento")
@NamedQuery(name = "TipoPagamento.pesquisaID", query = "select tp from TipoPagamento tp where tp.id=:pid")
public class TipoPagamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20, unique = true)
    private String descricao;

    public TipoPagamento() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoPagamento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
