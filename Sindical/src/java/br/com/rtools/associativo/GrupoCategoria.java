package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SOC_GRUPO_CATEGORIA")
@NamedQuery(name = "GrupoCategoria.pesquisaID", query = "select gc from GrupoCategoria gc where gc.id = :pid")
public class GrupoCategoria implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_GRUPO_CATEGORIA", length = 50, nullable = true)
    private String grupoCategoria;
    @Column(name = "NR_PROXIMA_MATRICULA", length = 10, nullable = true)
    private int nrProximaMatricula;
    @Column(name = "NR_VALIDADE_MESES_CARTAO", length = 10, nullable = true)
    private int nrValidadeMesCartao;

    public GrupoCategoria() {
        this.id = -1;
        this.grupoCategoria = "";
        this.nrProximaMatricula = 1;
        this.nrValidadeMesCartao = 12;
    }

    public GrupoCategoria(int id, String grupoCategoria, int nrProximaMatricula, int nrValidadeMesCartao) {
        this.id = id;
        this.grupoCategoria = grupoCategoria;
        this.nrProximaMatricula = nrProximaMatricula;
        this.nrValidadeMesCartao = nrValidadeMesCartao;
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

    public int getNrValidadeMesCartao() {
        return nrValidadeMesCartao;
    }

    public void setNrValidadeMesCartao(int nrValidadeMesCartao) {
        this.nrValidadeMesCartao = nrValidadeMesCartao;
    }
}
