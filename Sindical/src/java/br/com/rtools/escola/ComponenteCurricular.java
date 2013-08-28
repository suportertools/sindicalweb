package br.com.rtools.escola;

import javax.persistence.*;

@Entity
@Table(name="ESC_COMPONENTE_CURRICULAR")
@NamedQuery(name="ComponenteCurricular.pesquisaID", query="select cc from ComponenteCurricular cc where cc.id = :pid")
public class ComponenteCurricular implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=50, nullable = true, unique = true)
    private String descricao;

    public ComponenteCurricular() {
        this.id = -1;
        this.descricao = "";
    }

    public ComponenteCurricular(int id, String descricao) {
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