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
@Table(name = "soc_grupo_categoria")
@NamedQueries({
    @NamedQuery(name = "GrupoCategoria.pesquisaID", query = "SELECT GC FROM GrupoCategoria AS GC WHERE GC.id = :pid "),
    @NamedQuery(name = "GrupoCategoria.findAll", query = "SELECT GC FROM GrupoCategoria AS GC ORDER BY GC.grupoCategoria ASC ")
})
public class GrupoCategoria implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_grupo_categoria", length = 50, nullable = true)
    private String grupoCategoria;
    @Column(name = "nr_proxima_matricula", length = 10, nullable = true)
    private int nrProximaMatricula;

    public GrupoCategoria() {
        this.id = -1;
        this.grupoCategoria = "";
        this.nrProximaMatricula = 1;
    }

    public GrupoCategoria(int id, String grupoCategoria, int nrProximaMatricula) {
        this.id = id;
        this.grupoCategoria = grupoCategoria;
        this.nrProximaMatricula = nrProximaMatricula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(String grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public int getNrProximaMatricula() {
        return nrProximaMatricula;
    }

    public void setNrProximaMatricula(int nrProximaMatricula) {
        this.nrProximaMatricula = nrProximaMatricula;
    }
}
