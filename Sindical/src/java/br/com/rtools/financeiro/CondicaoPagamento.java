package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_condicao_pagamento")
@NamedQuery(name = "CondicaoPagamento.pesquisaID", query = "select cp from CondicaoPagamento cp where cp.id=:pid")
public class CondicaoPagamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20, unique = true)
    private String descricao;

    public CondicaoPagamento() {
        this.id = -1;
        this.descricao = "";
    }

    public CondicaoPagamento(int id, String descricao) {
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
