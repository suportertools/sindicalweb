package br.com.rtools.associativo;

import br.com.rtools.pessoa.Juridica;
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
@Table(name = "soc_convenio")
@NamedQueries({
    @NamedQuery(name = "Convenio.pesquisaID",  query = "SELECT CON FROM Convenio AS CON WHERE CON.id = :pid"),
    @NamedQuery(name = "Convenio.findAll",     query = "SELECT CON FROM Convenio AS CON ORDER BY CON.juridica.pessoa.nome ASC, CON.subGrupoConvenio.grupoConvenio.descricao ASC, CON.subGrupoConvenio.descricao ASC "),
    @NamedQuery(name = "Convenio.findName",    query = "SELECT CON FROM Convenio AS CON WHERE UPPER(CON.juridica.pessoa.nome) LIKE :pdescricao ORDER BY CON.juridica.pessoa.nome ASC, CON.subGrupoConvenio.grupoConvenio.descricao ASC, CON.subGrupoConvenio.descricao ASC  ")
})
public class Convenio implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_juridica", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Juridica juridica;
    @JoinColumn(name = "id_convenio_sub_grupo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private SubGrupoConvenio subGrupoConvenio;

    public Convenio() {
        this.id = -1;
        this.juridica = new Juridica();
        this.subGrupoConvenio = new SubGrupoConvenio();
    }

    public Convenio(int id, Juridica juridica, SubGrupoConvenio subGrupoConvenio) {
        this.id = id;
        this.juridica = juridica;
        this.subGrupoConvenio = subGrupoConvenio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public SubGrupoConvenio getSubGrupoConvenio() {
        return subGrupoConvenio;
    }

    public void setSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        this.subGrupoConvenio = subGrupoConvenio;
    }
}
