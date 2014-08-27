package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_layout")
@NamedQuery(name = "Layout.pesquisaID", query = "select l from Layout l where l.id=:pid")
public class Layout implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = true)
    private String descricao;
    @Column(name = "url", length = 50, nullable = true)
    private String url;

    public Layout() {
        this.id = -1;
        this.descricao = "";
        this.url = "";
    }

    public Layout(int id, String descricao, String url) {
        this.id = id;
        this.descricao = descricao;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
