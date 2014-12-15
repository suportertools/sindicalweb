package br.com.rtools.sistema;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "sis_tipo_resolucao")
@NamedQuery(name = "TipoResolucao.pesquisaID", query = "select tr from TipoResolucao tr where tr.id = :pid")
public class TipoResolucao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20)
    private String descricao;
    @Column(name = "ds_tamanho", length = 20)
    private String tamanho;

    public TipoResolucao() {
        this.id = -1;
        this.descricao = "";
        this.tamanho = "1200";
    }
    
    public TipoResolucao(int id, String descricao, String tamanho) {
        this.id = id;
        this.descricao = descricao;
        this.tamanho = tamanho;
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
    
    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }
    
}
