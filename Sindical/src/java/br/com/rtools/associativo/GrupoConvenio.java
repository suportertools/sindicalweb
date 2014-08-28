package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "soc_convenio_grupo")
@NamedQueries({
    @NamedQuery(name = "GrupoConvenio.pesquisaID",  query = "SELECT G FROM GrupoConvenio AS G WHERE G.id = :pid"),
    @NamedQuery(name = "GrupoConvenio.findAll",     query = "SELECT G FROM GrupoConvenio AS G ORDER BY G.descricao ASC "),
    @NamedQuery(name = "GrupoConvenio.findName",    query = "SELECT G FROM GrupoConvenio AS G WHERE UPPER(G.descricao) LIKE :pdescricao ORDER BY G.descricao ASC ")
})
public class GrupoConvenio implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = true, unique = true)
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
