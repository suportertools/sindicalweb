package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "soc_convenio_sub_grupo")
@NamedQueries({
    @NamedQuery(name = "SubGrupoConvenio.pesquisaID",   query = "SELECT SGC FROM SubGrupoConvenio AS SGC WHERE SGC.id = :pid"),
    @NamedQuery(name = "SubGrupoConvenio.findAll",      query = "SELECT SGC FROM SubGrupoConvenio AS SGC ORDER BY SGC.grupoConvenio.descricao ASC, SGC.descricao ASC "),
    @NamedQuery(name = "SubGrupoConvenio.findName",     query = "SELECT SGC FROM SubGrupoConvenio AS SGC WHERE upper(SGC.descricao) LIKE :pdescricao ORDER BY SGC.grupoConvenio.descricao ASC, SGC.descricao ASC ")
})
public class SubGrupoConvenio implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = true)
    private String descricao;
    @JoinColumn(name = "id_grupo_convenio", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoConvenio grupoConvenio;
    @Column(name = "ds_obs_guias", length = 1000, nullable = true)
    private String observacao;

    public SubGrupoConvenio() {
        this.id = -1;
        this.descricao = "";
        this.grupoConvenio = new GrupoConvenio();
        this.observacao = "";
    }

    public SubGrupoConvenio(int id, String descricao, GrupoConvenio grupoConvenio, String observacao) {
        this.id = id;
        this.descricao = descricao;
        this.grupoConvenio = grupoConvenio;
        this.observacao = observacao;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
