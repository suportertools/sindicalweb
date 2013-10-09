package br.com.rtools.associativo;

import br.com.rtools.pessoa.Juridica;
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
@Table(name = "SOC_CONVENIO")
@NamedQuery(name = "Convenio.pesquisaID", query = "select c from Convenio c where c.id=:pid")
public class Convenio implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_JURIDICA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Juridica juridica;
    @JoinColumn(name = "ID_CONVENIO_SUB_GRUPO", referencedColumnName = "ID", nullable = false)
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
