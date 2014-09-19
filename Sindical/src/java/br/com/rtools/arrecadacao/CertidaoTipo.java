package br.com.rtools.arrecadacao;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "arr_certidao_tipo")
@NamedQuery(name = "CertidaoTipo.pesquisaID", query = "select c from CertidaoTipo c where c.id = :pid")
public class CertidaoTipo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100)
    private String descricao;
    @Column(name = "is_ativo", columnDefinition = "boolean default true")
    private boolean ativo;

    public CertidaoTipo() {
        this.id = -1;
        this.descricao = "";
        this.ativo = true;
    }
    
    public CertidaoTipo(int id, String descricao, boolean ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
