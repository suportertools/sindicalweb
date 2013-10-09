package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name = "SOC_CONVENIO_GRUPO")
@NamedQuery(name = "GrupoConvenio.pesquisaID", query = "select g from GrupoConvenio g where g.id=:pid")
public class GrupoConvenio implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true, unique = true)
    private String descricao;

    public GrupoConvenio() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoConvenio(int id, String descricao) {
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
