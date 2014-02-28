package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "EVE_DESC_EVENTO")
@NamedQuery(name = "DescricaoEvento.findAll", query = "SELECT DE FROM DescricaoEvento AS DE ORDER BY DE.grupoEvento.descricao ASC, DE.descricao ASC ")
public class DescricaoEvento implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true)
    private String descricao;
    @JoinColumn(name = "ID_GRUPO_EVENTO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private GrupoEvento grupoEvento;

    public DescricaoEvento(int id, String descricao, GrupoEvento grupoEvento) {
        this.id = id;
        this.descricao = descricao;
        this.grupoEvento = grupoEvento;
    }

    public DescricaoEvento() {
        this.id = -1;
        this.descricao = "";
        this.grupoEvento = new GrupoEvento();
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

    public GrupoEvento getGrupoEvento() {
        return grupoEvento;
    }

    public void setGrupoEvento(GrupoEvento grupoEvento) {
        this.grupoEvento = grupoEvento;
    }

    @Override
    public String toString() {
        return "DescricaoEvento{" + "id=" + id + ", descricao=" + descricao + ", grupoEvento=" + grupoEvento + '}';
    }
}
