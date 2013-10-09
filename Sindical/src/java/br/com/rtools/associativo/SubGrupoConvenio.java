package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SOC_CONVENIO_SUB_GRUPO")
@NamedQuery(name = "SubGrupoConvenio.pesquisaID", query = "select g from SubGrupoConvenio g where g.id=:pid")
public class SubGrupoConvenio implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true)
    private String descricao;
    @JoinColumn(name = "ID_GRUPO_CONVENIO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private GrupoConvenio grupoConvenio;

    public SubGrupoConvenio() {
        this.id = -1;
        this.descricao = "";
        this.grupoConvenio = new GrupoConvenio();
    }

    public SubGrupoConvenio(int id, String descricao, GrupoConvenio grupoConvenio) {
        this.id = id;
        this.descricao = descricao;
        this.grupoConvenio = grupoConvenio;
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

    public GrupoConvenio getGrupoConvenio() {
        return grupoConvenio;
    }

    public void setGrupoConvenio(GrupoConvenio grupoConvenio) {
        this.grupoConvenio = grupoConvenio;
    }
}
